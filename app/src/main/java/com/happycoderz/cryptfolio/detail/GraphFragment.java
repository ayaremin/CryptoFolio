package com.happycoderz.cryptfolio.detail;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
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
import info.hoang8f.android.segmented.SegmentedGroup;
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
  @BindView(R.id.segmented) protected SegmentedGroup timeRangeGroup;

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
    presenter.getGraph(symbol, 0, 0, "3d", "1h");
    timeRangeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        presenter.cIndex = 0;
        presenter.mIndex = 0;
        switch (checkedId) {
          case R.id.one_year:
            presenter.getGraph(symbol, 0, 0, "1y", "6d");
            break;

          case R.id.six_month:
            presenter.getGraph(symbol, 0, 0, "6m", "3d");
            break;

          case R.id.three_month:
            presenter.getGraph(symbol, 0, 0, "3m", "1d");
            break;

          case R.id.one_month:
            presenter.getGraph(symbol, 0, 0, "1m", "4h");
            break;

          case R.id.one_week:
            presenter.getGraph(symbol, 0, 0, "7d", "2h");
            break;

          case R.id.three_day:
            presenter.getGraph(symbol, 0, 0, "3d", "1h");
            break;

          case R.id.one_day:
            presenter.getGraph(symbol, 0, 0, "1d", "1h");
            break;
        }
      }
    });
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

    mChart.invalidate();
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
