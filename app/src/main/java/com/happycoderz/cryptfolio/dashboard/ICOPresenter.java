package com.happycoderz.cryptfolio.dashboard;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.happycoderz.cryptfolio.BasePresenter;
import com.happycoderz.cryptfolio.api.RestService;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.ICO;
import com.happycoderz.cryptfolio.models.IcoResult;
import com.happycoderz.cryptfolio.models.Portfolio;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EminAyar on 17.10.2017.
 */

@InjectViewState public class ICOPresenter extends BasePresenter<ICOView> {

  Context context;

  @Override public void setContext(Context context) {
    this.context = context;
  }

  public void showViews() {

  }

  @Override public void start() {
    super.start();
    getViewState().setViews();
    fetchICOs();
    if (cacheHelper.isAdFree()) {
      getViewState().hideBanner();
    }
  }


  public void fetchICOs() {

    retrofitICOApi.create(RestService.class)
        .getICOs()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .unsubscribeOn(Schedulers.io())
        .subscribe(new Observer<IcoResult>() {
          @Override public void onSubscribe(@NonNull Disposable d) {
            getViewState().refresh();
          }

          @Override public void onNext(@NonNull IcoResult coins) {
            getViewState().hideLoading();
            ArrayList<ICO> icos = coins.ico.live;
            icos.addAll(coins.ico.upcoming);
            getViewState().setListView(icos);
          }

          @Override public void onError(@NonNull Throwable e) {
            getViewState().hideLoading();
            getViewState().showError(e.getMessage());
          }

          @Override public void onComplete() {

          }
        });
  }
}
