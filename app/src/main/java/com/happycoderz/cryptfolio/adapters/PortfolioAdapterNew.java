package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.interfaces.CoinListener;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Portfolio;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class PortfolioAdapterNew extends RealmRecyclerViewAdapter<Coin, PortfolioAdapterNew
    .ViewHolder> {

  private boolean inDeletionMode = false;
  private Set<Integer> countersToDelete = new HashSet<Integer>();
  private final int NOTIFY_DELAY = 500;
  private List<Coin> mCoins;
  private Portfolio portfolio;
  private CacheHelper cacheHelper;
  private CoinListener listener;


  public PortfolioAdapterNew(OrderedRealmCollection<Coin> data, Context context, CoinListener
      listener) {
    super(data, false);
    cacheHelper = CacheHelper.getInstance(context);
    portfolio = cacheHelper.getPortfolio();
    this.listener = listener;
    setHasStableIds(false);
  }

  @Override public PortfolioAdapterNew.ViewHolder onCreateViewHolder(ViewGroup parent, int
      viewType) {
    View v =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.card_portfolio, parent, false);

    return new PortfolioAdapterNew.ViewHolder(v);
  }

  @Override public void onBindViewHolder(PortfolioAdapterNew.ViewHolder viewHolder, int position) {
    Coin coin = getItem(position);

    viewHolder.load();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.name_text_view) protected TextView nameTextView;
    @BindView(R.id.symbol_text_view) protected TextView symbolTextView;
    @BindView(R.id.delete_image_view) protected ImageView deleteImageView;
    @BindView(R.id.price_text_view) protected TextView priceTextView;
    @BindView(R.id.selected_currency_text_view) protected TextView selectedCurrencyTextView;
    @BindView(R.id.percentage_text_view) protected TextView percentageTextView;
    @BindView(R.id.market_cap_text_view) protected TextView marketCapTextView;
    @BindView(R.id.holding_edit_text) protected EditText holdingEditText;

    public ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
      view.setOnClickListener(this);
    }


    public void load() {
      final Coin coin = getItem(getAdapterPosition());
      if (!coin.isValid())
        return;
      portfolio = cacheHelper.getPortfolio();
      nameTextView.setText(coin.getName());
      symbolTextView.setText(coin.getSymbol());

      try {
        if (Double.valueOf(coin.getPercentChange()) < 0) {
          percentageTextView.setTextColor(Color.parseColor("#ff5640"));
          percentageTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_down, 0, 0, 0);
          percentageTextView.setCompoundDrawablePadding(4);
        } else {
          percentageTextView.setTextColor(Color.parseColor("#6fdc6f"));
          percentageTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_up, 0, 0, 0);
          percentageTextView.setCompoundDrawablePadding(4);
        }

        percentageTextView.setText(coin.getPercentChange() + "%");
      } catch (Exception e) {
        Crashlytics.logException(e);
      }

      if (coin.getSelectedCoin() == Coin.NO_SELECTED_CURRENCY) {
        selectedCurrencyTextView.setVisibility(View.GONE);
      } else {
        selectedCurrencyTextView.setVisibility(View.VISIBLE);
        selectedCurrencyTextView.setText(
            String.valueOf(coin.getSelectedCoin()) + " " + portfolio.getCurrency().toUpperCase());
      }

      holdingEditText.setText(String.valueOf(coin.getMyHoldings()));
      priceTextView.setText(coin.getPriceUSD());
      marketCapTextView.setText(coin.getMarketCap());

      deleteImageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          listener.onCoinDeleted(coin.getName(), getAdapterPosition());
          listener.onCoinHoldingChanged();
        }
      });

      holdingEditText.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        private Timer timer = new Timer();
        private final long DELAY = 1000;

        @Override public void afterTextChanged(Editable s) {
          if (!coin.isValid())
            return;
          double amount = 0;
          try {
            amount = Double.parseDouble(s.toString());
          } catch (Exception e) {
            e.printStackTrace();
            return;
          }
          Realm realm = Realm.getDefaultInstance();
          final double finalAmount = amount;
          realm.executeTransaction(new Realm.Transaction() {
            @Override public void execute(Realm realm) {
              getItem(getAdapterPosition()).setMyHoldings(
                  finalAmount);
            }
          });
          timer.cancel();
          timer = new Timer();
          timer.schedule(new TimerTask() {
            @Override public void run() {
              listener.onCoinHoldingChanged();
            }
          }, DELAY);
        }
      });
    }

    @Override public void onClick(View view) {

    }
  }

  public void removeCoin(final int position) {
    Realm realm = Realm.getDefaultInstance();
    realm.beginTransaction();
    getItem(position).deleteFromRealm();
    realm.commitTransaction();
    notifyDataSetChanged();
  }

  public void addCoin () {
    //notifyDataSetChanged();
  }
}