package com.happycoderz.cryptfolio.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.happycoderz.cryptfolio.BaseFragment;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.adapters.AlertAdapter;
import com.happycoderz.cryptfolio.interfaces.AlertListener;
import com.happycoderz.cryptfolio.models.Alert;
import com.happycoderz.cryptfolio.models.Coin;
import io.realm.RealmResults;

/**
 * Created by EminAyar on 11.12.2017.
 */

public class AlertFragment extends BaseFragment implements AlertView, AlertListener {

  @BindView(R.id.list_view) protected ListView listView;
  @BindView(R.id.fab) protected FloatingActionButton fab;

  AlertAdapter adapter;
  Coin selectedCoin;

  @InjectPresenter AlertPresenter presenter;

  @ProvidePresenter(type = PresenterType.GLOBAL) AlertPresenter provideContext() {
    AlertPresenter presenter = new AlertPresenter();
    presenter.setContext(getContext());
    return presenter;
  }

  @Nullable @Override public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
  ) {
    View view = inflater.inflate(R.layout.fragment_alarm, container, false);
    ButterKnife.bind(view);
    presenter.setContext(getContext());
    presenter.start();
    ButterKnife.bind(this, view);

    selectedCoin = ((CoinDetailActivity) getActivity()).getSelectedCoin();
    presenter.populateListView(selectedCoin);

    return view;
  }

  @Override public void setListDate(RealmResults<Alert> alertArrayList) {
    adapter = new AlertAdapter(alertArrayList, this, getContext());
    listView.setAdapter(adapter);
  }

  @OnClick(R.id.fab) public void onFabClicked() {
    new MaterialDialog.Builder(getContext()).title(R.string.label_alert_dialog_title)
        .customView(R.layout.alert_custom_view, true)
        .positiveText(R.string.label_set)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(
              @NonNull MaterialDialog dialog, @NonNull DialogAction which
          ) {
            presenter.addAlarm(dialog.getCustomView(), selectedCoin);
          }
        })
        .show();
  }
}
