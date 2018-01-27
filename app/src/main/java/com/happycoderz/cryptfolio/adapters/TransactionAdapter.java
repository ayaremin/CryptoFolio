package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.interfaces.AlertListener;
import com.happycoderz.cryptfolio.models.Alert;
import com.happycoderz.cryptfolio.models.Transaction;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

public class TransactionAdapter extends RealmBaseAdapter<Transaction> implements ListAdapter {

  Context context;

  public TransactionAdapter(
      OrderedRealmCollection<Transaction> realmResults, Context context
  ) {
    super(realmResults);
    this.context = context;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    Transaction transaction = getItem(position);

    if (convertView == null) {
      convertView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.card_transaction, parent, false);
    }

    AlertViewWrapper view = new AlertViewWrapper();
    ButterKnife.bind(view, convertView);
    view.load(transaction);

    return convertView;
  }

  class AlertViewWrapper {

    @BindView(R.id.trading_pair) protected TextView tradingPairTextView;
    @BindView(R.id.date) protected TextView dateTextView;
    @BindView(R.id.amount_buy) protected TextView amountBuyTextView;
    @BindView(R.id.transaction_price) protected TextView transactionPriceTextView;
    @BindView(R.id.percentage_text_view) protected TextView percentageTextView;
    @BindView(R.id.type_image_view) protected ImageView typeImageView;

    public void load(final Transaction transaction) {

      tradingPairTextView.setText(transaction.getPair());

      amountBuyTextView.setText(String.valueOf(transaction.getAmount()));

      transactionPriceTextView.setText(String.valueOf(transaction.getTransactionPrice()));

      dateTextView.setText(transaction.getDateFormatted());

      if (transaction.getType() == Transaction.BUY) {
        typeImageView.getBackground()
            .setColorFilter(ContextCompat.getColor(context, R.color.evernote_green),
                PorterDuff.Mode.SRC_IN);
      } else {
        typeImageView.getBackground()
            .setColorFilter(ContextCompat.getColor(context, R.color.google_red),
                PorterDuff.Mode.SRC_IN);
      }

    }
  }
}