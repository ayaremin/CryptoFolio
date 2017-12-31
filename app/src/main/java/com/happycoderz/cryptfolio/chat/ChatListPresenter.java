package com.happycoderz.cryptfolio.chat;

import android.os.Handler;
import com.arellomobile.mvp.InjectViewState;
import com.happycoderz.cryptfolio.BasePresenter;
import com.happycoderz.cryptfolio.api.RestService;
import com.happycoderz.cryptfolio.dashboard.DashboardView;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Portfolio;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

/**
 * Created by EminAyar on 17.10.2017.
 */

@InjectViewState public class ChatListPresenter extends BasePresenter<ChatListView> {

  public void showViews() {

  }

  @Override public void start() {
    super.start();
    getViewState().setViews();
  }
}
