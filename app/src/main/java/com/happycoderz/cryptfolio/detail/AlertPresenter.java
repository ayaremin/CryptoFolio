package com.happycoderz.cryptfolio.detail;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import com.arellomobile.mvp.InjectViewState;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.happycoderz.cryptfolio.BasePresenter;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.models.Alert;
import com.happycoderz.cryptfolio.models.Coin;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by EminAyar on 17.10.2017.
 */

@InjectViewState public class AlertPresenter extends BasePresenter<AlertView> {

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

  public void populateListView(Coin coin) {
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Alert> alerts = realm.where(Alert.class)
        .equalTo("symbol", coin.getSymbol())
        .findAll();
    getViewState().setListDate(alerts);
  }

  public void addAlarm(View view, Coin coin) {
    EditText gte = (EditText) view.findViewById(R.id.great_than_edit_text);
    EditText lte = (EditText) view.findViewById(R.id.less_than_edit_text);

    if (TextUtils.isEmpty(gte.getText().toString()) && TextUtils.isEmpty(
        lte.getText().toString())) {
      gte.setError(context.getString(R.string.error_empty));
      lte.setError(context.getString(R.string.error_empty));
      return;
    }
    double lte_d, gte_d;
    try {
      lte_d = Double.parseDouble(lte.getText().toString());
    } catch (Exception e) {
      lte_d = 0;
    }

    try {
      gte_d = Double.parseDouble(gte.getText().toString());
    } catch (Exception e) {
      gte_d = Double.MAX_VALUE;
    }

    Alert alert = new Alert(coin.getName(), lte_d, gte_d, coin.getSymbol());

    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    realm.copyToRealm(alert);
    realm.commitTransaction();

    Answers.getInstance()
        .logContentView(new ContentViewEvent().putContentType("Alarm")
            .putContentType("added")
            .putCustomAttribute("alarmCoin", coin.getName())
            .putCustomAttribute("alarmGT", gte_d)
            .putCustomAttribute("alarmLT", lte_d));
  }
}
