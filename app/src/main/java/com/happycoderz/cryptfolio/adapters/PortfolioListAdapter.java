package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.interfaces.AlertListener;
import com.happycoderz.cryptfolio.models.Alert;
import com.happycoderz.cryptfolio.models.Coin;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PortfolioListAdapter extends ArrayAdapter<Coin> {

  Context context;
  private ArrayList<Coin> coins = new ArrayList<>();

  public PortfolioListAdapter(
      @NonNull Context context, @LayoutRes int resource, @NonNull List<Coin> objects
  ) {
    super(context, 0, new ArrayList<Coin>());
    coins.addAll(objects);
    addAll(objects);
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    Coin alert = getItem(position);

    if (convertView == null) {
      convertView = LayoutInflater.from(parent.getContext())
          .inflate(R.layout.item_portfolio_list, parent, false);
    }

    AlertViewWrapper view = new AlertViewWrapper();
    ButterKnife.bind(view, convertView);
    view.load(alert);

    return convertView;
  }

  class AlertViewWrapper {

    @BindView(R.id.name_text_view) protected TextView nameTextView;
    @BindView(R.id.symbol_text_view) protected TextView symbolTextView;
    @BindView(R.id.price_text_view) protected TextView priceTextView;
    @BindView(R.id.btc_text_view) protected TextView btcTextView;
    @BindView(R.id.holding_text_view) protected TextView holdingsTextView;

    public void load(final Coin coin) {

      nameTextView.setText(coin.getName());
      symbolTextView.setText(coin.getSymbol());

      holdingsTextView.setText(String.valueOf(coin.getMyHoldings()));
      DecimalFormat df = new DecimalFormat("#.##");

      try {
        btcTextView.setText(df.format(coin.getBTCHoldingsAsDouble()) + " BTC");
      } catch (Exception e) {
        Crashlytics.logException(e);
      }

      try {
        priceTextView.setText(df.format(coin.getUSDHoldingsAsDouble()) + " $");
      } catch (Exception e) {
        Crashlytics.logException(e);
      }
    }
  }

  public void sort(String field, final boolean isAsc) {
    if (field.equals("name")) {
      Collections.sort(coins, new Comparator<Coin>() {
        @Override public int compare(Coin o1, Coin o2) {
          if (isAsc) {
            return o1.getName().compareToIgnoreCase(o2.getName());
          } else {
            return o2.getName().compareToIgnoreCase(o1.getName());
          }
        }
      });
    } else if (field.equals("holdings")) {
      Collections.sort(coins, new Comparator<Coin>() {
        @Override public int compare(Coin o1, Coin o2) {
          if (isAsc) {
            return Double.compare(o1.getMyHoldings(), o2.getMyHoldings());
          } else {
            return Double.compare(o2.getMyHoldings(), o1.getMyHoldings());
          }
        }
      });
    } else if (field.equals("value")) {
      Collections.sort(coins, new Comparator<Coin>() {
        @Override public int compare(Coin o1, Coin o2) {
          if (isAsc) {
            return Double.compare(o1.getUSDHoldingsAsDouble(), o2.getUSDHoldingsAsDouble());
          } else {
            return Double.compare(o2.getUSDHoldingsAsDouble(), o1.getUSDHoldingsAsDouble());
          }
        }
      });
    }
    clear();
    addAll(coins);
  }
}