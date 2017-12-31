package com.happycoderz.cryptfolio.dashboard;

import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.arellomobile.mvp.InjectViewState;
import com.google.android.gms.ads.AdListener;
import com.happycoderz.cryptfolio.BasePresenter;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.api.RestService;
import com.happycoderz.cryptfolio.models.Alert;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Portfolio;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import com.happycoderz.cryptfolio.utils.MyBounceInterpolator;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.List;
import org.solovyev.android.checkout.EmptyRequestListener;
import org.solovyev.android.checkout.Purchase;

/**
 * Created by EminAyar on 17.10.2017.
 */

@InjectViewState public class DashboardPresenter extends BasePresenter<DashboardView> {

  public void showViews() {

  }

  @Override public void start() {
    super.start();
    getViewState().setViews();
    checkPortfolioContainer();
  }

  public void calculateTotalAmount() {
    Realm realm = Realm.getDefaultInstance();
    Portfolio portfolio = cacheHelper.getPortfolio();
    double price = 0;

    for (Coin coin : realm.where(Coin.class).findAll()) {
      double coinMultiplier =
          (coin.getSelectedCoin() == Coin.NO_SELECTED_CURRENCY) ? coin.getPriceUSDasDouble() : coin
              .getSelectedCoin();

      price += (coinMultiplier * coin.getMyHoldings());
    }

    getViewState().writeTotalAmount(price, portfolio.getCurrency());
  }

  public void checkPortfolioContainer() {
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Coin> coins = realm.where(Coin.class)
        .findAll();

    if (coins.isEmpty()) {
      getViewState().showPlaceHolderView();
    } else {
      getViewState().hidePlaceHolderView();
    }
  }

  public void startAdShowing () {
    if (cacheHelper.isAdFree()) {
      return;
    }
    new Handler().postDelayed(new Runnable() {
      @Override public void run() {
        getViewState().showInterstitial();
      }
    },1000);
  }

  public void startCoinAddedAd () {
    if (cacheHelper.isAdFree()) {
      return;
    }
    if (cacheHelper.willAdShown(CacheHelper.COIN_ADD)) {
      getViewState().showCoinAddedAd();
    }
  }

  public void startAmountChangedAd () {
    /*if (cacheHelper.isAdFree()) {
      return;
    }
    if (cacheHelper.willAdShown(CacheHelper.AMOUNT_CHANGED)) {
      getViewState().showAmountChangedAd();
    }*/
  }

  public void addCoin (Coin coin) {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.copyToRealm(coin);
    realm.commitTransaction();
  }

  void animateButton(final RelativeLayout view) {
    // Load the animation
    final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce);
    double animationDuration = 2000;
    myAnim.setDuration((long)animationDuration);

    // Use custom animation interpolator to achieve the bounce effect
    MyBounceInterpolator
        interpolator = new MyBounceInterpolator(0.2, 20);

    myAnim.setInterpolator(interpolator);

    // Animate the button
    view.startAnimation(myAnim);

    // Run button animation again after it finished
    myAnim.setAnimationListener(new Animation.AnimationListener(){
      @Override
      public void onAnimationStart(Animation arg0) {}

      @Override
      public void onAnimationRepeat(Animation arg0) {}

      @Override
      public void onAnimationEnd(Animation arg0) {
        new Handler().postDelayed(new Runnable() {
          @Override public void run() {
            animateButton(view);
          }
        },3000);
      }
    });
  }
}
