package com.happycoderz.cryptfolio.dashboard;

import android.content.Intent;
import android.net.Uri;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.SearchEvent;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.happycoderz.cryptfolio.BaseFragment;
import com.happycoderz.cryptfolio.BuildConfig;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.adapters.CoinAdapter;
import com.happycoderz.cryptfolio.adapters.ICOAdapter;
import com.happycoderz.cryptfolio.detail.CoinDetailActivity;
import com.happycoderz.cryptfolio.interfaces.CoinListInterface;
import com.happycoderz.cryptfolio.interfaces.CoinListener;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.ICO;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import com.pnikosis.materialishprogress.ProgressWheel;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EminAyar on 11.12.2017.
 */

public class ICOFragment extends BaseFragment
    implements ICOView, SwipeRefreshLayout.OnRefreshListener {

  @InjectPresenter ICOPresenter presenter;

  @ProvidePresenter(type = PresenterType.GLOBAL) ICOPresenter provideContext() {
    ICOPresenter presenter = new ICOPresenter();
    presenter.setContext(getContext());
    return presenter;
  }

  @BindView(R.id.progress_wheel) protected ProgressWheel progressWheel;
  @BindView(R.id.list_view) protected ListView listView;
  @BindView(R.id.swiperefresh) protected SwipeRefreshLayout swipeRefreshLayout;
  @BindView(R.id.adView) protected AdView adView;

  private ICOAdapter adapter;
  private ArrayList<ICO> coinList = new ArrayList<>();
  private CacheHelper cacheHelper;
  DashboardActivity activity;

  @Nullable @Override public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
  ) {
    View view = inflater.inflate(R.layout.fragment_icos, container, false);
    ButterKnife.bind(this, view);
    presenter.setContext(getContext());
    presenter.start();

    activity = (DashboardActivity) getActivity();
    swipeRefreshLayout.setOnRefreshListener(this);

    return view;
  }

  @Override public void setViews() {
    super.setViews();
  }

  @Override public void hideLoading() {
    progressWheel.setVisibility(View.GONE);
    swipeRefreshLayout.setRefreshing(false);
  }

  @Override public void setListView(ArrayList<ICO> icos) {
    adapter = new ICOAdapter(getContext(), icos);
    listView.setAdapter(adapter);
    this.coinList.clear();
    this.coinList.addAll(icos);
  }

  @Override public void refresh() {
    swipeRefreshLayout.setRefreshing(true);
  }

  @Override public void hideBanner() {
    adView.setVisibility(View.GONE);
  }

  @OnItemClick(R.id.list_view) public void onItemClick(AdapterView<?> parent, int position) {
    if (!adapter.getItem(position).isAd()) {
      Answers.getInstance()
          .logContentView(new ContentViewEvent().putCustomAttribute("ico",
              adapter.getItem(position).getName()));
      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse(adapter.getItem(position).getWebsite()));
      startActivity(i);
    }
  }

  @Override public void onRefresh() {
    presenter.fetchICOs();
  }
}
