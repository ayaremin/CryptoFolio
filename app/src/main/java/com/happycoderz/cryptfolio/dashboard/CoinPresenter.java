package com.happycoderz.cryptfolio.dashboard;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import com.arellomobile.mvp.InjectViewState;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.happycoderz.cryptfolio.BasePresenter;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.api.RestService;
import com.happycoderz.cryptfolio.detail.GraphView;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Portfolio;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EminAyar on 17.10.2017.
 */

@InjectViewState public class CoinPresenter extends BasePresenter<CoinListView> {

  Context context;

  @Override public void setContext(Context context) {
    this.context = context;
  }

  public void showViews() {

  }

  @Override public void start() {
    super.start();
    getViewState().setViews();
    fetchCoins();
    if (cacheHelper.isAdFree()) {
      getViewState().hideBanner();
    }
  }

  private void updateMyPortfolio(final List<Coin> coins) {
    Realm realm = Realm.getDefaultInstance();
    if (!realm.where(Coin.class).findAll().isEmpty()) {
      realm.executeTransactionAsync(new Realm.Transaction() {
        @Override public void execute(Realm realm) {
          for (final Coin coin : coins) {
            for (int i = 0; i < realm.where(Coin.class).findAll().size(); i++) {
              if (coin.getId().equals(realm.where(Coin.class).findAll().get(i).getId())) {
                Coin coinTemp = realm.where(Coin.class).equalTo("id", coin.getId()).findFirst();
                final double amount = coinTemp.getMyHoldings();
                coinTemp = coin;
                coinTemp.setMyHoldings(amount);
                realm.copyToRealmOrUpdate(coinTemp);
              }
            }
          }
        }
      }, new Realm.Transaction.OnSuccess() {
        @Override public void onSuccess() {
          getViewState().hideLoading();
          getViewState().setListView(coins);
        }
      });
    } else {
      getViewState().hideLoading();
      getViewState().setListView(coins);
    }
  }

  public void fetchCoins() {

    Portfolio portfolio = cacheHelper.getPortfolio();

    retrofit.create(RestService.class)
        .getCoins(portfolio.getCurrency())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .unsubscribeOn(Schedulers.io())
        .subscribe(new Observer<List<Coin>>() {
          @Override public void onSubscribe(@NonNull Disposable d) {
            getViewState().refresh();
          }

          @Override public void onNext(@NonNull List<Coin> coins) {
            updateMyPortfolio(coins);
          }

          @Override public void onError(@NonNull Throwable e) {
            getViewState().hideLoading();
            getViewState().showError(e.getMessage());
          }

          @Override public void onComplete() {

          }
        });
  }

  public void calculateTotalAmount() {
    Portfolio portfolio = cacheHelper.getPortfolio();
    RealmResults<Coin> coins = Realm.getDefaultInstance().where(Coin.class).findAll();
    double price = 0;

    for (Coin coin : portfolio.getCoins()) {
      double coinMultiplier =
          (coin.getSelectedCoin() == Coin.NO_SELECTED_CURRENCY) ? coin.getPriceUSDasDouble()
              : coin.getSelectedCoin();

      price += (coinMultiplier * coin.getMyHoldings());
    }

    getViewState().writeTotalAmount(price, portfolio.getCurrency());
  }
}
