package com.happycoderz.cryptfolio;

import com.arellomobile.mvp.MvpView;

/**
 * Created by EminAyar on 17.10.2017.
 */

public interface BaseView extends MvpView {
    void start();

    void hideProgress();

    void showProgress(String message);

    void showProgress();

    void showError(String message);

    void showError(int message);

    void setViews();
}
