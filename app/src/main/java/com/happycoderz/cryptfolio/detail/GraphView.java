package com.happycoderz.cryptfolio.detail;

import com.github.mikephil.charting.data.CandleEntry;
import com.happycoderz.cryptfolio.BaseView;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.GraphData;
import java.util.ArrayList;

/**
 * Created by EminAyar on 17.10.2017.
 */

public interface GraphView extends BaseView {

  void showProgress();

  void hideProgress();

  void noDataFound();

  void showData (ArrayList<ArrayList<Object>> coins, String market, String conversion);
}
