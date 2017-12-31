package com.happycoderz.cryptfolio.dashboard;

import com.happycoderz.cryptfolio.BaseView;
import com.happycoderz.cryptfolio.models.Coin;
import java.util.List;

/**
 * Created by EminAyar on 17.10.2017.
 */

public interface DashboardView extends BaseView {

  void hidePlaceHolderView ();

  void showPlaceHolderView ();

  void writeTotalAmount (double amount, String currency);

  void showInterstitial ();

  void showCoinAddedAd ();

  void showAmountChangedAd ();

  void showInAppDialog ();

}
