package com.happycoderz.cryptfolio.detail;

import android.content.Context;
import android.graphics.Color;
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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;

/**
 * Created by EminAyar on 17.10.2017.
 */

@InjectViewState public class GraphPresenter extends BasePresenter<GraphView> {

  Context context;

  @Override public void setContext(Context context) {
    this.context = context;
  }

  public void showViews() {

  }

  @Override public void start() {
    super.start();
    getViewState().setViews();
  }

  public int mIndex = 0;
  public int cIndex = 0;
  public void getGraph(final String pair, final int marketIndex, final int coinIndex, final String
      time, final String res) {
    final String[] markets = { "bitfinex", "bitstamp", "coinone", "coinbase", "liqui", "poloniex" };
    final String[] converts = { "usd", "btc" };
    retrofitGraphApi.create(RestService.class)
        .getGraphForCurrency(pair+ "-"+converts[cIndex], markets[mIndex], time, res)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .unsubscribeOn(Schedulers.io())
        .subscribe(new Observer<ArrayList<ArrayList<Object>>>() {
          @Override public void onSubscribe(@NonNull Disposable d) {
            getViewState().showProgress();
          }

          @Override public void onNext(@NonNull ArrayList<ArrayList<Object>> coins) {
            if (coins.isEmpty() && mIndex == markets.length - 1 && cIndex  == converts
                .length - 1) {
              getViewState().hideProgress();
              getViewState().noDataFound();
              return;
            }
            if (coins.isEmpty() && mIndex == markets.length - 1 && cIndex != converts
                .length - 1) {
              cIndex++;
              mIndex = 0;
              getGraph(pair, mIndex, cIndex, time, res);
              return;
            }
            else if (coins.isEmpty()){
              getGraph(pair, mIndex++, cIndex, time, res);
              return;
            }
            getViewState().hideProgress();
            getViewState().showData(coins, markets[mIndex], converts[cIndex]);
          }

          @Override public void onError(@NonNull Throwable e) {
            getViewState().hideProgress();
            getViewState().noDataFound();
          }

          @Override public void onComplete() {

          }
        });
  }


  public LineData generateLineData(ArrayList<ArrayList<Object>> data) {

    final ArrayList<String > labels = new ArrayList<>();
    for (ArrayList array : data) {
      labels.add((String)array.get(0));
    }


    ArrayList<Entry> entries = new ArrayList<Entry>();

    for (int index = 0; index < data.size(); index++)
      entries.add(new Entry(Float.valueOf(String.valueOf(data.get(index).get(6))), index));

    LineDataSet set = new LineDataSet(entries, context.getString(R.string.label_orders));
    set.setColor(Color.parseColor("#cccccc"));
    set.setLineWidth(0.5f);
    set.setDrawCubic(true);
    set.setFillColor(Color.parseColor("#cccccc"));
    set.setDrawFilled(true);
    set.setDrawValues(false);
    set.setCircleSize(0f);

    set.setAxisDependency(YAxis.AxisDependency.LEFT);
    LineData d = new LineData(labels, set);

    return d;
  }

  protected CandleData generateCandleData(ArrayList<ArrayList<Object>> data) {

    final ArrayList<String > labels = new ArrayList<>();
    for (ArrayList array : data) {
      labels.add((String)array.get(0));
    }


    ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();

    int i = 0;
    for (ArrayList arrayList : data) {
      entries.add(new CandleEntry(i, Float.valueOf(String.valueOf(arrayList.get(4))),
          Float.valueOf(String.valueOf(arrayList.get(1))),
          Float.valueOf(String.valueOf(arrayList.get(2))),
          Float.valueOf(String.valueOf(arrayList.get(3))), (String) arrayList.get(0)));
      i++;
    }

    CandleDataSet candleDataSet = new CandleDataSet(entries, context.getString(R.string.label_price_changes));
    candleDataSet.setDecreasingColor(Color.rgb(211, 72, 54));
    candleDataSet.setIncreasingColor(Color.parseColor("#5ba525"));
    candleDataSet.setShadowColor(Color.DKGRAY);
    candleDataSet.setBodySpace(0.3f);
    //set.setBarSpace(0.3f);
    candleDataSet.setValueTextSize(10f);
    candleDataSet.setDrawValues(false);
    candleDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
    CandleData d = new CandleData(labels, candleDataSet);

    return d;
  }
}
