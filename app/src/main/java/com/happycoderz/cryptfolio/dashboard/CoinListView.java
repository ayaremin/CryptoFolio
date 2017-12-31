package com.happycoderz.cryptfolio.dashboard;

import com.happycoderz.cryptfolio.BaseView;
import com.happycoderz.cryptfolio.models.Coin;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EminAyar on 17.10.2017.
 */

public interface CoinListView extends BaseView {

  void showProgress();

  void hideProgress();

  void noDataFound();

  void hideLoading ();

  void setListView (List<Coin> coins);

  void refresh ();

  void hideBanner ();

  void writeTotalAmount (double amount, String currency);
}
