package com.happycoderz.cryptfolio.detail;

import com.happycoderz.cryptfolio.BaseView;
import com.happycoderz.cryptfolio.models.Coin;
import io.realm.RealmResults;

/**
 * Created by EminAyar on 17.10.2017.
 */

public interface PortfolioOverviewView extends BaseView {

  void onListResult (RealmResults<Coin> coins, String btcHold, String usdHold);

}
