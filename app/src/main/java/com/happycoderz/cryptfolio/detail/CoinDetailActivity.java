package com.happycoderz.cryptfolio.detail;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.chatsdk.core.session.NM;
import co.chatsdk.core.types.AccountDetails;
import co.chatsdk.ui.manager.InterfaceManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.happycoderz.cryptfolio.BaseActivity;
import com.happycoderz.cryptfolio.BuildConfig;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.adapters.ChatListAdapter;
import com.happycoderz.cryptfolio.adapters.CoinDetailFragmentPagerAdapter;
import com.happycoderz.cryptfolio.chat.ChatListPresenter;
import com.happycoderz.cryptfolio.chat.ChatListView;
import com.happycoderz.cryptfolio.interfaces.ChatListClickListener;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.util.Arrays;
import java.util.List;

import static co.chatsdk.core.types.AccountDetails.signUp;

public class CoinDetailActivity extends BaseActivity implements CoinDetailView {

  @BindView(R.id.name_text_view) protected TextView nameTextView;
  @BindView(R.id.price_text_view) protected TextView priceTextView;
  @BindView(R.id.percentage_text_view) protected TextView percentageTextView;
  @BindView(R.id.market_cap_text_view) protected TextView marketCapTextView;
  @BindView(R.id.coin_image_view) protected ImageView coinImageView;

  private InterstitialAd mInterstitialAd;
  private Coin selectedCoin;

  @InjectPresenter CoinDetailPresenter presenter;

  @ProvidePresenter(type = PresenterType.GLOBAL) CoinDetailPresenter provideContext() {
    CoinDetailPresenter presenter = new CoinDetailPresenter();
    presenter.setContext(this);
    return presenter;
  }

  @BindView(R.id.pager) protected ViewPager pager;
  @BindView(R.id.tabs) protected TabLayout tabs;
  private String currency;

  private CoinDetailFragmentPagerAdapter adapter;
  private CacheHelper cacheHelper;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_coin_detail);
    ButterKnife.bind(this);

    presenter.start();
    cacheHelper = CacheHelper.getInstance(this);

    MobileAds.initialize(this, getString(R.string.app_id));

    mInterstitialAd = new InterstitialAd(this);
    mInterstitialAd.setAdUnitId(getString(R.string.detail_screen_id));
    mInterstitialAd.loadAd(new AdRequest.Builder().build());
    mInterstitialAd.setAdListener(new AdListener() {
      @Override public void onAdLoaded() {
        super.onAdLoaded();
        if (mInterstitialAd.isLoaded()) {
          presenter.startAd();
        }
      }
    });

    selectedCoin = new Gson().fromJson(getIntent().getStringExtra("coin"), Coin.class);
    currency = selectedCoin.getSymbol();

    Answers.getInstance()
        .logContentView(new ContentViewEvent().putContentName("CoinDetail")
            .putContentType("CoinDetail")
            .putCustomAttribute("coinName", selectedCoin.getSymbol()));

    setUpCoinView();

    FragmentManager fragmentManager = getSupportFragmentManager();
    adapter = new CoinDetailFragmentPagerAdapter(this, fragmentManager);
    pager.setAdapter(adapter);
    tabs.setupWithViewPager(pager);
    setFontForTabs();
  }

  @OnClick(R.id.back_image_view) protected void onBackClicked() {
    finish();
  }

  public String getCurrency() {
    return currency;
  }

  public Coin getSelectedCoin() {
    return selectedCoin;
  }

  private void setUpCoinView() {
    TextDrawable drawable = TextDrawable.builder()
        .beginConfig()
        .textColor(Color.WHITE)
        .useFont(Typeface.createFromAsset(getAssets(), "fonts/Exo2-SemiBold.ttf"))
        .fontSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18,
            getResources().getDisplayMetrics()))
        .toUpperCase()
        .endConfig()
        .buildRound(selectedCoin.getSymbol().toUpperCase(),
            ContextCompat.getColor(this, R.color.purple));

    coinImageView.setImageDrawable(drawable);

    nameTextView.setText(selectedCoin.getName());

    try {
      if (Double.valueOf(selectedCoin.getPercentChange()) < 0) {
        percentageTextView.setTextColor(Color.parseColor("#ff5640"));
        percentageTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);
        percentageTextView.setCompoundDrawablePadding(4);
      } else {
        percentageTextView.setTextColor(Color.parseColor("#6fdc6f"));
        percentageTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up, 0, 0, 0);
        percentageTextView.setCompoundDrawablePadding(4);
      }

      percentageTextView.setText(selectedCoin.getPercentChange() + "%");
    } catch (Exception e) {
      Crashlytics.logException(e);
    }

    priceTextView.setText(selectedCoin.getPriceUSD());
    marketCapTextView.setText(selectedCoin.getMarketCap());
  }

  private void setFontForTabs() {
    for (int i = 0; i < tabs.getTabCount(); i++) {
      //noinspection ConstantConditions
      TextView tv = new TextView(this);
      tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Exo2-SemiBold.ttf"));
      tv.setTextSize(14);
      tv.setGravity(Gravity.CENTER);
      tv.setTextColor(ContextCompat.getColor(this, R.color.white));
      tv.setText(tabs.getTabAt(i).getText());
      tabs.getTabAt(i).setCustomView(tv);
    }
  }

  @Override public void showAd() {
    mInterstitialAd.show();
  }
}
