package com.happycoderz.cryptfolio.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.happycoderz.cryptfolio.BaseFragment;
import com.happycoderz.cryptfolio.R;
import java.util.ArrayList;
import java.util.Date;

import static android.R.attr.entries;

/**
 * Created by EminAyar on 11.12.2017.
 */

public class GraphFragment extends BaseFragment implements GraphView {

  @InjectPresenter GraphPresenter presenter;

  @ProvidePresenter(type = PresenterType.GLOBAL) GraphPresenter provideContext() {
    GraphPresenter presenter = new GraphPresenter();
    presenter.setContext(getContext());
    return presenter;
  }

  String symbol;

  @BindView(R.id.graph) protected CombinedChart mChart;
  @BindView(R.id.progress_bar) protected ProgressBar progressBar;
  @BindView(R.id.pair_name_text_view) protected TextView pairNameTextView;
  @BindView(R.id.market_name_text_view) protected TextView marketNameTextView;

  @Nullable @Override public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
  ) {
    View view = inflater.inflate(R.layout.fragment_graph, container, false);
    ButterKnife.bind(this, view);
    presenter.setContext(getContext());
    presenter.start();
    return view;
  }

  @Override public void setViews() {
    super.setViews();
    symbol = ((CoinDetailActivity) getActivity()).getCurrency();
    presenter.getGraph(symbol, 0, 0);
  }

  @Override public void hideProgress() {
    progressBar.setVisibility(View.GONE);
  }

  @Override public void showProgress() {

  }

  @Override public void noDataFound() {

  }

  @Override public void showData(
      ArrayList<ArrayList<Object>> coins, String market, String conversion
  ) {

    pairNameTextView.setVisibility(View.VISIBLE);
    marketNameTextView.setVisibility(View.VISIBLE);
    pairNameTextView.setText(symbol + '-' + conversion.toUpperCase());
    marketNameTextView.setText(market.toUpperCase());
    mChart.setBackgroundColor(Color.WHITE);
    mChart.setDrawGridBackground(false);
    mChart.setDrawBarShadow(false);

    // draw bars behind lines
    mChart.setDrawOrder(new CombinedChart.DrawOrder[] {
        CombinedChart.DrawOrder.CANDLE, CombinedChart.DrawOrder.LINE
    });

    Legend l = mChart.getLegend();
    l.setWordWrapEnabled(false);
    l.setTextSize(0f);

    YAxis rightAxis = mChart.getAxisRight();
    rightAxis.setDrawGridLines(false);
    rightAxis.setStartAtZero(false); // this replaces setStartAtZero(true)

    YAxis leftAxis = mChart.getAxisLeft();
    leftAxis.setDrawGridLines(false);
    leftAxis.setStartAtZero(false); // this replaces setStartAtZero(true)

    XAxis xAxis = mChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    final ArrayList<String> labels = new ArrayList<>();
    for (ArrayList array : coins) {
      labels.add((String) array.get(0) + ":00");
    }

    CombinedData datas = new CombinedData(labels);

    datas.setData(presenter.generateCandleData(coins));
    datas.setData(presenter.generateLineData(coins));

    mChart.setData(datas);
    mChart.invalidate();
  }
}
