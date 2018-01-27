package com.happycoderz.cryptfolio.detail;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import belka.us.androidtoggleswitch.widgets.ToggleSwitch;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Transaction;
import fr.tvbarthel.lib.blurdialogfragment.SupportBlurDialogFragment;
import io.realm.Realm;
import java.util.Date;

public class AddTransactionFragment extends SupportBlurDialogFragment {

  @BindView(R.id.back_image_view) protected ImageView back;
  @BindView(R.id.amount_et) protected EditText amountEditText;
  @BindView(R.id.buy_price_et) protected EditText buyPriceEditText;
  @BindView(R.id.type_toggle) protected ToggleSwitch toggleSwitch;

  private Coin coin;


  public AddTransactionFragment() {

  }

  public AddTransactionFragment(Coin coin) {
    Bundle bundle = new Bundle();
    bundle.putString("coin", new Gson().toJson(coin));
    setArguments(bundle);
  }

  @Override public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState
  ) {
    coin = new Gson().fromJson(getArguments().getString("coin"), Coin.class);
    getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    setCancelable(false);
    View view = inflater.inflate(R.layout.fragment_add_transaction, container);
    ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onStart() {
    super.onStart();
    Dialog dialog = getDialog();
    if (dialog != null) {
      int width = ViewGroup.LayoutParams.MATCH_PARENT;
      int height = ViewGroup.LayoutParams.MATCH_PARENT;
      dialog.getWindow().setLayout(width, height);
    }
  }

  @OnClick(R.id.back_image_view) protected void onBackClicked() {
    dismiss();
  }

  @OnClick(R.id.confirm) protected void onConfirmClicked() {

    double buyPrice = 0;
    double amount = 0;

    if (TextUtils.isEmpty(amountEditText.getText().toString())) {
      amountEditText.setError(getString(R.string.error_empty));
      return;
    }

    if (TextUtils.isEmpty(buyPriceEditText.getText().toString())) {
      buyPriceEditText.setError(getString(R.string.error_empty));
      return;
    }

    try {
      buyPrice = Double.parseDouble(buyPriceEditText.getText().toString());
    } catch (Exception e) {
      Crashlytics.logException(e);
    }


    try {
      amount = Double.parseDouble(amountEditText.getText().toString());
    } catch (Exception e) {
      Crashlytics.logException(e);
    }

    if (amount == 0 || buyPrice == 0) {
      return;
    }

    Realm realm = Realm.getDefaultInstance();
    Transaction transaction = new Transaction();
    transaction.setAmount(amount);
    transaction.setBuyPrice(buyPrice);
    transaction.setCoin(coin.getSymbol());
    transaction.setPair("usd");
    transaction.setDate(new Date());

    if (toggleSwitch.getCheckedTogglePosition() == 0)
      transaction.setType(Transaction.BUY);
    else
      transaction.setType(Transaction.SELL);

    realm.beginTransaction();
    realm.copyToRealm(transaction);
    realm.commitTransaction();
    dismiss();
  }
}