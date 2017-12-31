package com.happycoderz.cryptfolio.detail;

import android.content.Context;
import com.arellomobile.mvp.InjectViewState;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.happycoderz.cryptfolio.BasePresenter;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.models.Coin;
import io.realm.Realm;
import io.realm.RealmResults;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by EminAyar on 17.10.2017.
 */

@InjectViewState public class PortfolioOverviewPresenter
    extends BasePresenter<PortfolioOverviewView> {

  Realm realm = Realm.getDefaultInstance();

  Context context;

  public void showViews() {

  }

  public void setContext(Context context) {
    this.context = context;
  }

  @Override public void start() {
    super.start();
    getViewState().setViews();
  }

  public void setUpListView() {
    RealmResults<Coin> realmResults =
        realm.where(Coin.class).notEqualTo("myHoldings", 0.0).findAllSorted("name");
    double btc = 0, usd = 0;
    for (Coin coin : realmResults) {
      btc += coin.getBTCHoldingsAsDouble();
      usd += coin.getUSDHoldingsAsDouble();
    }
    DecimalFormat dfBTC = new DecimalFormat("#.######");
    DecimalFormat dfUSD = new DecimalFormat("#.###");
    getViewState().onListResult(realmResults, dfBTC.format(btc) + " BTC", dfUSD.format(usd) + " $");
  }

  public PieData setPieChartData() {
    RealmResults<Coin> realmResults =
        realm.where(Coin.class).notEqualTo("myHoldings", 0.0).findAll();
    ArrayList<Entry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<>();

    int i = 0;
    for (Coin coin : realmResults) {
      labels.add(coin.getName());
      entries.add(new Entry(Float.parseFloat(coin.getUSDHoldings()), i));
      i++;
    }

    PieDataSet dataset = new PieDataSet(entries, "");
    dataset.setColors(ColorTemplate.COLORFUL_COLORS);
    return new PieData(labels, dataset);
  }
}
