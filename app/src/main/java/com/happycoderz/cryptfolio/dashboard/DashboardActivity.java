package com.happycoderz.cryptfolio.dashboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.AddToCartEvent;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.PurchaseEvent;
import com.crashlytics.android.answers.StartCheckoutEvent;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.happycoderz.cryptfolio.BaseActivity;
import com.happycoderz.cryptfolio.BuildConfig;
import com.happycoderz.cryptfolio.MyApp;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.adapters.DashboardFragmentPagerAdapter;
import com.happycoderz.cryptfolio.adapters.PortfolioAdapterNew;
import com.happycoderz.cryptfolio.chat.ChatListActivity;
import com.happycoderz.cryptfolio.detail.PortfolioOverviewActivity;
import com.happycoderz.cryptfolio.interfaces.CoinListInterface;
import com.happycoderz.cryptfolio.interfaces.CoinListener;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Portfolio;
import com.happycoderz.cryptfolio.utils.AnalyticHelper;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import hotchemi.android.rate.AppRate;
import hotchemi.android.rate.OnClickButtonListener;
import io.realm.Realm;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import org.solovyev.android.checkout.ActivityCheckout;
import org.solovyev.android.checkout.BillingRequests;
import org.solovyev.android.checkout.Checkout;
import org.solovyev.android.checkout.Inventory;
import org.solovyev.android.checkout.ProductTypes;
import org.solovyev.android.checkout.Purchase;
import org.solovyev.android.checkout.RequestListener;

import static java.util.Arrays.asList;

public class DashboardActivity extends BaseActivity
    implements DashboardView, CoinListener, RequestListener<Purchase>, OnMenuItemClickListener {

  @InjectPresenter(type = PresenterType.GLOBAL) DashboardPresenter presenter;

  @ProvidePresenter(type = PresenterType.GLOBAL) DashboardPresenter provideContext() {
    DashboardPresenter presenter = new DashboardPresenter();
    presenter.setContext(this);
    return presenter;
  }

  @BindView(R.id.empty_place_holder) protected RelativeLayout emptyPlaceHolder;
  @BindView(R.id.portfolio_layout) protected RelativeLayout portfolioLayout;
  @BindView(R.id.portfolio_recycler_view) protected RecyclerView recyclerView;
  @BindView(R.id.currency_button) protected Button currencyButton;
  @BindView(R.id.total_holdings_text_view) protected TextView totalHoldingsTextView;
  @BindView(R.id.menu_image_view) protected ImageView menuImageView;
  @BindView(R.id.pager) protected ViewPager pager;
  @BindView(R.id.tabs) protected TabLayout tabs;
  @BindView(R.id.ad_container) protected RelativeLayout adContainer;

  private DashboardFragmentPagerAdapter adapter;
  private PortfolioAdapterNew portfolioAdapter;
  private Portfolio portfolio;
  private CacheHelper cacheHelper;
  private InterstitialAd mInterstitialAd, coinAddedAd, amountChangedAd;
  private final ActivityCheckout mCheckout = Checkout.forActivity(this, MyApp.get().getBilling());
  private CoinListInterface coinListInterface;
  private Inventory mInventory;
  private Realm realm;
  private ContextMenuDialogFragment mMenuDialogFragment;
  private AnalyticHelper analyticHelper;
  private com.facebook.ads.InterstitialAd faceInterstitial;

  private void loadInterstitialAd(Context ctx) {
    faceInterstitial = new com.facebook.ads.InterstitialAd(ctx, "302436203608267_311335676051653");
    faceInterstitial.setAdListener(new InterstitialAdListener() {
      @Override public void onInterstitialDisplayed(Ad ad) {

      }

      @Override public void onInterstitialDismissed(Ad ad) {

      }

      @Override public void onError(Ad ad, AdError adError) {

      }

      @Override public void onAdLoaded(Ad ad) {
        adContainer.setVisibility(View.VISIBLE);
      }

      @Override public void onAdClicked(Ad ad) {
        analyticHelper.sendEvent("adclicked", "hat");
      }

      @Override public void onLoggingImpression(Ad ad) {

      }
    });
    faceInterstitial.loadAd();
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_dashboard);
    ButterKnife.bind(this);
    presenter.start();

    mCheckout.start();
    mCheckout.createPurchaseFlow(this);

    FragmentManager fragmentManager = getSupportFragmentManager();
    adapter = new DashboardFragmentPagerAdapter(this, fragmentManager);
    pager.setAdapter(adapter);
    tabs.setupWithViewPager(pager);
    setFontForTabs();

    analyticHelper = AnalyticHelper.getInstance(this);

    AppRate.with(this).setInstallDays(0) // default 10, 0 means install day.
        .setLaunchTimes(3) // default 10
        .setRemindInterval(2) // default 1
        .setShowLaterButton(true) // default true
        .setDebug(false) // default false
        .setOnClickButtonListener(new OnClickButtonListener() { // callback listener.
          @Override public void onClickButton(int which) {
            Answers.getInstance()
                .logContentView(new ContentViewEvent().putContentName("ShareDialog")
                    .putContentType("Clicked")
                    .putCustomAttribute("where", "rate")
                    .putCustomAttribute("selection", Integer.toString(which)));
          }
        }).monitor();

    AppRate.showRateDialogIfMeetsConditions(this);

    Answers.getInstance()
        .logContentView(new ContentViewEvent().putContentName("App").putContentType("Launched"));

    RecyclerView.LayoutManager mLayoutManager =
        new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    realm = Realm.getDefaultInstance();
    portfolioAdapter = new PortfolioAdapterNew(realm.where(Coin.class).findAll(), this, this);
    recyclerView.setAdapter(portfolioAdapter);
    cacheHelper = CacheHelper.getInstance(this);
    portfolio = cacheHelper.getPortfolio();
    recyclerView.setAdapter(portfolioAdapter);
    MobileAds.initialize(this, getString(R.string.app_id));

    initMenuFragment();

    mInterstitialAd = new InterstitialAd(this);
    coinAddedAd = new InterstitialAd(this);
    amountChangedAd = new InterstitialAd(this);
    mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
    mInterstitialAd.loadAd(new AdRequest.Builder().build());
    mInterstitialAd.setAdListener(new AdListener() {
      @Override public void onAdClosed() {
        super.onAdClosed();
        try {
          showInAppDialog();
        } catch (Exception e) {
          Crashlytics.logException(e);
        }
      }

      @Override public void onAdLoaded() {
        super.onAdLoaded();
        if (!cacheHelper.isAdFree()) {
          mInterstitialAd.show();
        }
      }
    });
    amountChangedAd.setAdUnitId(getString(R.string.amount_changed_id));
    amountChangedAd.loadAd(new AdRequest.Builder().build());
    coinAddedAd.setAdUnitId(getString(R.string.coin_added_id));
    coinAddedAd.loadAd(new AdRequest.Builder().build());

    mInventory = mCheckout.makeInventory();
    restorePurchaces(mInventory);
    loadInterstitialAd(this);
    //presenter.startAdShowing();
  }

  @Override public void hidePlaceHolderView() {
    emptyPlaceHolder.setVisibility(View.GONE);
    portfolioLayout.setVisibility(View.VISIBLE);
  }

  @Override public void showPlaceHolderView() {
    emptyPlaceHolder.setVisibility(View.VISIBLE);
    portfolioLayout.setVisibility(View.GONE);
  }

  @Override public void writeTotalAmount(double amount, String currency) {
    DecimalFormat df = new DecimalFormat("#.##");
    totalHoldingsTextView.setText(
        getString(R.string.label_total_holdings, df.format(amount), currency));
  }

  @Override public void showInterstitial() {
    if (mInterstitialAd.isLoaded()) {
      mInterstitialAd.show();
    }
    mInterstitialAd.loadAd(new AdRequest.Builder().build());
  }

  @Override public void showCoinAddedAd() {
    if (coinAddedAd.isLoaded()) {
      coinAddedAd.show();
    }
    coinAddedAd.loadAd(new AdRequest.Builder().build());
  }

  @Override public void showAmountChangedAd() {
    if (amountChangedAd.isLoaded()) {
      amountChangedAd.show();
    }
    amountChangedAd.loadAd(new AdRequest.Builder().build());
  }

  @Override public void showInAppDialog() {
    new MaterialDialog.Builder(this).title(R.string.label_remove_ads)
        .content(R.string.label_remove_ads_detail)
        .positiveText(R.string.label_ok)
        .negativeText(R.string.label_no)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(
              @NonNull MaterialDialog dialog, @NonNull DialogAction which
          ) {
            checkout(CacheHelper.REMOVE_ADS, "ad_free", 1.99);
          }
        })
        .show();
  }

  public void setListView() {
    portfolioAdapter.notifyDataSetChanged();
  }

  @Override public void setViews() {
    super.setViews();
    presenter.animateButton(adContainer);
    currencyButton.setText(portfolio.getCurrency().toUpperCase());
  }

  @OnClick(R.id.menu_image_view) protected void onMenuClicked() {
    mMenuDialogFragment.show(getSupportFragmentManager(), ContextMenuDialogFragment.TAG);
  }

  @OnClick (R.id.ad_container) protected void adImageClicked () {
    analyticHelper.sendEvent("adshowclicked", "hat");
    faceInterstitial.show();
    loadInterstitialAd(this);
  }

  protected void onShareClicked() {
    Answers.getInstance()
        .logContentView(new ContentViewEvent().putContentName("App")
            .putContentType("Clicked")
            .putCustomAttribute("where", "share"));
    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
    sharingIntent.setType("text/plain");
    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "CryptoFolio");
    sharingIntent.putExtra(
        android.content.Intent.EXTRA_TEXT,
        getString(R.string.label_download, BuildConfig.APPLICATION_ID));
    startActivity(Intent.createChooser(sharingIntent, "Share via"));
  }

  @OnClick(R.id.currency_button) protected void onCurrencyClicked() {
    new MaterialDialog.Builder(this).title(R.string.label_change_currency)
        .items(R.array.currencies)
        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
          @Override public boolean onSelection(
              MaterialDialog dialog, View view, int which, CharSequence text
          ) {
            if (TextUtils.isEmpty(text)) return false;
            Answers.getInstance()
                .logContentView(new ContentViewEvent().putContentName("Currency")
                    .putContentType("Changed")
                    .putCustomAttribute("name", text.toString()));
            analyticHelper.sendEvent("currencyChanged", text.toString());
            currencyButton.setText(text.toString().toUpperCase());
            cacheHelper.updateCurrency(text.toString());
            new Handler().postDelayed(new Runnable() {
              @Override public void run() {
                coinListInterface.onCoinsFetched();
              }
            }, 300);
            return false;
          }
        })
        .positiveText(R.string.label_ok)
        .show();
  }

  @Override public void onCoinAdded(final Coin coin) {
    if (!realm.where(Coin.class).equalTo("name", coin.getName()).findAll().isEmpty()) {
      new MaterialDialog.Builder(this).cancelable(false)
          .title(getString(R.string.error_coin_already_exist))
          .content(R.string.error_coin_already_exist_detail)
          .positiveText("OK")
          .show();
      return;
    }
    new MaterialDialog.Builder(this).title(R.string.label_add)
        .content(getString(R.string.label_add_detail, coin.getName()))
        .positiveText(R.string.label_ok)
        .negativeText(R.string.label_no)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(
              @NonNull MaterialDialog dialog, @NonNull DialogAction which
          ) {
            Answers.getInstance()
                .logContentView(new ContentViewEvent().putContentName("Coin")
                    .putContentType("CoinAdded")
                    .putCustomAttribute("name", coin.getName()));
            analyticHelper.sendEvent("portfolioAdded", coin.getName());
            presenter.addCoin(coin);
            portfolioAdapter.addCoin();
            presenter.checkPortfolioContainer();
            presenter.calculateTotalAmount();
            presenter.startCoinAddedAd();
          }
        })
        .show();
  }

  @Override public void onCoinDeleted(final String name, final int index) {
    new MaterialDialog.Builder(this).title(R.string.label_delete)
        .content(getString(R.string.label_delete_detail, name))
        .positiveText(R.string.label_ok)
        .negativeText(R.string.label_no)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(
              @NonNull MaterialDialog dialog, @NonNull DialogAction which
          ) {
            Answers.getInstance()
                .logContentView(new ContentViewEvent().putContentName("Coin")
                    .putContentType("Removed")
                    .putCustomAttribute("name", name));
            analyticHelper.sendEvent("portfolioRemoved", name);
            portfolioAdapter.removeCoin(index);
            presenter.checkPortfolioContainer();
            presenter.calculateTotalAmount();
          }
        })
        .show();
  }

  @Override public void onCoinHoldingChanged() {
    runOnUiThread(new Runnable() {
      @Override public void run() {
        presenter.calculateTotalAmount();
        presenter.startAmountChangedAd();
      }
    });
  }

  @Override protected void onDestroy() {
    mCheckout.stop();
    super.onDestroy();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    mCheckout.onActivityResult(requestCode, resultCode, data);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  @Override public void onSuccess(@Nonnull Purchase result) {
    double price;
    if (result.sku.equalsIgnoreCase(CacheHelper.REMOVE_ADS)) {
      price = 1.99;
    } else {
      price = 5.99;
    }
    Answers.getInstance()
        .logPurchase(new PurchaseEvent().putItemId(result.sku)
            .putItemName(result.sku)
            .putItemPrice(BigDecimal.valueOf(price)));
    cacheHelper.removeAds();
  }

  @Override public void onError(int response, @Nonnull Exception e) {
    Crashlytics.logException(e);
  }

  @OnClick(R.id.chat_image_view) protected void onChatClicked() {
    Answers.getInstance()
        .logContentView(new ContentViewEvent().putContentName("App")
            .putContentType("Clicked")
            .putCustomAttribute("where", "ChatList"));
    startActivity(new Intent(this, ChatListActivity.class));
  }

  private void checkout(final String sku, String name, final double price) {
    Answers.getInstance()
        .logAddToCart(new AddToCartEvent().putItemName(sku)
            .putItemPrice(BigDecimal.valueOf(price))
            .putItemId(sku));
    mCheckout.whenReady(new Checkout.EmptyListener() {
      @Override public void onReady(BillingRequests requests) {
        Answers.getInstance()
            .logStartCheckout(
                new StartCheckoutEvent().putItemCount(1).putTotalPrice(BigDecimal.valueOf(price)));
        requests.purchase(ProductTypes.IN_APP, sku, null, mCheckout.getPurchaseFlow());
      }
    });
  }

  public void setOnCoinListListener(CoinListInterface coinListInterface) {
    this.coinListInterface = coinListInterface;
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

  @OnClick(R.id.expand_image_view) protected void onExpandClicked() {
    if (recyclerView.getVisibility() == View.VISIBLE) {
      recyclerView.setVisibility(View.GONE);
      analyticHelper.sendEvent("clicked", "unexpand");
    } else {
      recyclerView.setVisibility(View.VISIBLE);
      analyticHelper.sendEvent("clicked", "expand");
    }
  }

  private void restorePurchaces(Inventory mInventory) {
    mInventory.load(
        Inventory.Request.create()
            .loadAllPurchases()
            .loadSkus(ProductTypes.IN_APP, asList(CacheHelper.REMOVE_ADS, CacheHelper.DONATE_US)),
        new Inventory.Callback() {

          @Override public void onLoaded(@Nonnull Inventory.Products products) {
            for (Inventory.Product product : products) {
              if (product.isPurchased(CacheHelper.REMOVE_ADS) || product.isPurchased(
                  CacheHelper.DONATE_US)) {
                cacheHelper.removeAds();
              } else {
                cacheHelper.showAds();
              }
            }
          }
        });
  }

  private void initMenuFragment() {
    MenuParams menuParams = new MenuParams();
    menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.toolbar_height));
    menuParams.setMenuObjects(getMenuObjects());
    menuParams.setClosableOutside(false);
    menuParams.setClipToPadding(false);
    mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    mMenuDialogFragment.setItemClickListener(this);
  }

  private List<MenuObject> getMenuObjects() {

    List<MenuObject> menuObjects = new ArrayList<>();

    MenuObject close = new MenuObject();
    close.setResource(R.drawable.ic_close);
    close.setDividerColor(R.color.purple);

    MenuObject send = new MenuObject(getString(R.string.menu_portolio));
    send.setResource(R.drawable.ic_chart);
    send.setDividerColor(R.color.purple);

    MenuObject like = new MenuObject(getString(R.string.menu_share));
    like.setResource(R.drawable.ic_share);
    like.setDividerColor(R.color.purple);

    MenuObject addFr = new MenuObject(getString(R.string.menu_rate_us));
    addFr.setResource(R.drawable.ic_rate_us);
    addFr.setDividerColor(R.color.purple);

    MenuObject addFav = new MenuObject(getString(R.string.menu_remove_ads));
    addFav.setResource(R.drawable.ic_remove_ad);
    addFav.setDividerColor(R.color.purple);

    MenuObject block = new MenuObject(getString(R.string.menu_donate_us));
    block.setResource(R.drawable.ic_donate);
    block.setDividerColor(R.color.purple);

    menuObjects.add(close);
    menuObjects.add(send);
    menuObjects.add(like);
    menuObjects.add(addFr);
    menuObjects.add(addFav);
    menuObjects.add(block);
    return menuObjects;
  }

  @Override public void onMenuItemClick(View clickedView, int position) {
    switch (position) {
      case 0:
        mMenuDialogFragment.dismiss();
        break;
      case 1:
        analyticHelper.sendEvent("menuEvent", "portfolio");
        if (realm.where(Coin.class).notEqualTo("myHoldings", 0.0).findAllSorted("name").isEmpty()) {
          showError(getString(R.string.message_no_coin));
        } else {
          startActivity(new Intent(this, PortfolioOverviewActivity.class));
        }
        break;
      case 2:
        analyticHelper.sendEvent("menuEvent", "share");
        onShareClicked();
        break;

      case 3:
        analyticHelper.sendEvent("menuEvent", "rate");
        Answers.getInstance()
            .logContentView(new ContentViewEvent().putCustomAttribute("rate", "clicked"));
        String url = "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
        break;

      case 4:
        analyticHelper.sendEvent("menuEvent", "removead");
        checkout(CacheHelper.REMOVE_ADS, "ad_free", 1.99);
        break;

      case 5:
        analyticHelper.sendEvent("menuEvent", "donateus");
        checkout(CacheHelper.DONATE_US, "donate_us", 5.99);
        break;
    }
  }
}
