package com.happycoderz.cryptfolio.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.SearchEvent;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.happycoderz.cryptfolio.BaseFragment;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.adapters.CoinAdapter;
import com.happycoderz.cryptfolio.adapters.PortfolioAdapter;
import com.happycoderz.cryptfolio.detail.CoinDetailActivity;
import com.happycoderz.cryptfolio.detail.GraphPresenter;
import com.happycoderz.cryptfolio.detail.GraphView;
import com.happycoderz.cryptfolio.interfaces.CoinListInterface;
import com.happycoderz.cryptfolio.interfaces.CoinListener;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import com.pnikosis.materialishprogress.ProgressWheel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EminAyar on 11.12.2017.
 */

public class CoinFragment extends BaseFragment implements CoinListView, CoinListener, TextWatcher,
    SwipeRefreshLayout.OnRefreshListener, CoinListInterface {

  @InjectPresenter CoinPresenter presenter;

  @ProvidePresenter(type = PresenterType.GLOBAL) CoinPresenter provideContext() {
    CoinPresenter presenter = new CoinPresenter();
    presenter.setContext(getContext());
    return presenter;
  }

  @BindView(R.id.progress_wheel) protected ProgressWheel progressWheel;
  @BindView(R.id.list_view) protected ListView listView;
  @BindView(R.id.swiperefresh) protected SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.adView) protected AdView adView;
  @BindView(R.id.search_edit_text) protected EditText searchEditText;

  private CoinAdapter adapter;
  private ArrayList<Coin> coinList = new ArrayList<>();
  private CacheHelper cacheHelper;
  DashboardActivity activity;

  @Nullable @Override public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
  ) {
    View view = inflater.inflate(R.layout.fragment_coins, container, false);
    ButterKnife.bind(this, view);
    presenter.setContext(getContext());
    presenter.start();

    activity = (DashboardActivity) getActivity();
    activity.setOnCoinListListener(this);
    swipeRefreshLayout.setOnRefreshListener(this);
    AdRequest adRequest = new AdRequest.Builder().build();
    adView.loadAd(adRequest);

    return view;
  }

  @Override public void setViews() {
    super.setViews();
    searchEditText.addTextChangedListener(this);
  }

  @Override public void noDataFound() {

  }

  @Override public void hideLoading() {
    progressWheel.setVisibility(View.GONE);
    swipeRefreshLayout.setRefreshing(false);
  }

  @Override public void setListView(List<Coin> coins) {
    adapter = new CoinAdapter(getContext(), coins, this);
    listView.setAdapter(adapter);
    this.coinList.clear();
    this.coinList.addAll(coins);
    activity.setListView();
  }

  @Override public void refresh() {
    swipeRefreshLayout.setRefreshing(true);
  }

  @Override public void hideBanner() {
    adView.setVisibility(View.GONE);
  }

  @OnItemClick(R.id.list_view) public void onItemClick(AdapterView<?> parent, int position) {
    if (!adapter.getItem(position).isAd()) {
      Intent i = new Intent(getActivity(), CoinDetailActivity.class);
      i.putExtra("coin", new Gson().toJson(adapter.getItem(position)));
      startActivity(i);
    }
  }

  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

  }

  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
    try {
      adapter.getCoinFilter().performFiltering(s.toString());
      Answers.getInstance().logSearch(new SearchEvent()
          .putQuery(s.toString()));
    } catch (Exception e) {
      Crashlytics.logException(e);
    }
  }

  @Override public void afterTextChanged(Editable s) {

  }

  @Override public void onCoinAdded(Coin coin) {
    activity.onCoinAdded(coin);
  }

  @Override public void onCoinDeleted(String name, int index) {
    activity.onCoinDeleted(name, index);
  }

  @Override public void onCoinHoldingChanged() {
    activity.onCoinHoldingChanged();
  }

  @Override public void onRefresh() {
    presenter.fetchCoins();
  }

  @Override public void writeTotalAmount(double amount, String currency) {
    activity.writeTotalAmount(amount, currency);
  }

  @Override public void onCoinsFetched() {
    presenter.fetchCoins();
  }


}
