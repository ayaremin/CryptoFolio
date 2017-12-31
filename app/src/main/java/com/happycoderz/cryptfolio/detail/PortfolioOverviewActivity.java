package com.happycoderz.cryptfolio.detail;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.amulyakhare.textdrawable.TextDrawable;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.github.mikephil.charting.charts.PieChart;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.happycoderz.cryptfolio.BaseActivity;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.adapters.CoinDetailFragmentPagerAdapter;
import com.happycoderz.cryptfolio.adapters.PortfolioListAdapter;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import io.realm.Realm;
import io.realm.RealmResults;

public class PortfolioOverviewActivity extends BaseActivity implements PortfolioOverviewView {

  @BindView(R.id.list_view) protected ListView listView;
  @BindView(R.id.chart) protected PieChart pieChart;
  @BindView(R.id.usd_price_text_view) protected TextView usdPriceTextView;
  @BindView(R.id.btc_price_text_view) protected TextView btcPriceTextView;

  @InjectPresenter PortfolioOverviewPresenter presenter;

  private PortfolioListAdapter adapter;
  private boolean nameSort = true, holdingSort = true, valueSort = true;
  @ProvidePresenter(type = PresenterType.GLOBAL) PortfolioOverviewPresenter provideContext() {
    PortfolioOverviewPresenter presenter = new PortfolioOverviewPresenter();
    presenter.setContext(this);
    return presenter;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my_portfolio);
    ButterKnife.bind(this);

    presenter.start();
    presenter.setContext(this);
    pieChart.invalidate();
    pieChart.setData(presenter.setPieChartData());
    pieChart.animateY(2500);
    pieChart.setCenterText(getString(R.string.label_my_holdings) + "($)");
    pieChart.setCenterTextColor(ContextCompat.getColor(this, R.color.gray));
    pieChart.setCenterTextTypeface(
        Typeface.createFromAsset(getAssets(), "fonts/Exo2-SemiBold" + ".ttf"));
    pieChart.setCenterTextRadiusPercent(70);
    pieChart.setUsePercentValues(false);
    pieChart.setCenterTextSize(10);
    pieChart.setDescription("");
  }

  @Override public void setViews() {
    super.setViews();
    presenter.setUpListView();

  }

  @OnClick(R.id.back_image_view) protected void onBackClicked() {
    finish();
  }

  @OnClick(R.id.name_sort) protected void onNameSorted() {
    if (nameSort)
      nameSort = false;
    else
      nameSort = true;
    adapter.sort("name", nameSort );
  }

  @OnClick(R.id.value_sort) protected void onValueSorted() {
    if (valueSort)
      valueSort = false;
    else
      valueSort = true;
    adapter.sort("value", valueSort );
  }

  @OnClick(R.id.holding_sort) protected void onHoldingsSorted() {
    if (holdingSort)
      holdingSort = false;
    else
      holdingSort = true;
    adapter.sort("holdings", holdingSort );
  }

  @Override public void onListResult(RealmResults<Coin> coins, String btc, String usd) {
    adapter = new PortfolioListAdapter(this, 0, Realm.getDefaultInstance().copyFromRealm(coins));
    listView.setAdapter(adapter);
    btcPriceTextView.setText(btc);
    usdPriceTextView.setText(usd);
  }

}
