package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.crashlytics.android.Crashlytics;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdView;
import com.google.android.gms.ads.AdView;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.dashboard.DashboardActivity;
import com.happycoderz.cryptfolio.interfaces.CoinListener;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import java.util.ArrayList;
import java.util.List;

public class CoinAdapter extends ArrayAdapter<Coin> {

  Context context;
  CoinListener listener;
  List<Coin> coins = new ArrayList<>();
  private CoinFilter coinFilter = new CoinFilter();
  private View adView;
  private Ad ad;
  private NativeAd nativeAd;

  public CoinAdapter(Context ctx, List<Coin> coins, CoinListener listener) {
    super(ctx, 0);
    this.coins.addAll(coins);
    addAll(this.coins);
    notifyDataSetChanged();
    this.listener = listener;
    this.context = ctx;
    nativeAd = new NativeAd(context, context.getString(R.string.facebook_native_ad));
    if (!CacheHelper.getInstance(context).isAdFree()) {
      nativeAd.setAdListener(new com.facebook.ads.AdListener() {
        @Override public void onError(Ad ad, AdError adError) {

        }

        @Override public void onAdLoaded(Ad ad) {
          CoinAdapter.this.ad = ad;
          addAdvert();
        }

        @Override public void onAdClicked(Ad ad) {

        }

        @Override public void onLoggingImpression(Ad ad) {

        }
      });

      // Initiate a request to load an ad.
      nativeAd.loadAd();
    }
  }

  public CoinFilter getCoinFilter() {
    return coinFilter;
  }

  public void setCoinFilter(CoinFilter coinFilter) {
    this.coinFilter = coinFilter;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    Coin coin = getItem(position);
    // 0 mean coin 1 will be ad
    int layout;
    layout = R.layout.item_coin_list;
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
    }
    CoinViewWrapper view = new CoinViewWrapper();
    ButterKnife.bind(view, convertView);
    view.load(coin);

    return convertView;
  }

  class CoinViewWrapper {
    @BindView(R.id.ad_container) protected LinearLayout adContainer;
    @BindView(R.id.content) protected LinearLayout content;
    @BindView(R.id.name_text_view) protected TextView nameTextView;
    @BindView(R.id.symbol_text_view) protected TextView symbolTextView;
    @BindView(R.id.add_image_view) protected ImageView addImageView;
    @BindView(R.id.price_text_view) protected TextView priceTextView;
    @BindView(R.id.percentage_text_view) protected TextView percentageTextView;
    @BindView(R.id.market_cap_text_view) protected TextView marketCapTextView;

    public void load(final Coin coin) {
      addImageView.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          listener.onCoinAdded(coin);
        }
      });
      if (coin.isAd()) {
        adContainer.removeAllViews();
        content.setVisibility(View.GONE);
        adContainer.setVisibility(View.VISIBLE);
        adContainer.addView(
            NativeAdView.render(getContext(), nativeAd, NativeAdView.Type.HEIGHT_100));
      } else {
        adContainer.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);

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

        priceTextView.setText(coin.getPriceUSD());
        marketCapTextView.setText(coin.getMarketCap());
      }
    }
  }

  public class CoinFilter extends Filter {
    @Override public FilterResults performFiltering(CharSequence constraint) {

      String filterString = constraint.toString().toLowerCase();

      FilterResults results = new FilterResults();

      final List<Coin> list = coins;

      int count = list.size();
      final ArrayList<Coin> nlist = new ArrayList<Coin>(count);

      String filterableName, filterableShorthand;

      for (int i = 0; i < count; i++) {
        if (!list.get(i).isAd()) {
          filterableName = list.get(i).getName();
          filterableShorthand = list.get(i).getSymbol();
          if (filterableName.toLowerCase().contains(filterString)
              || filterableShorthand.toLowerCase().contains(filterString)) {
            nlist.add(list.get(i));
          }
        }
      }

      results.values = nlist;
      results.count = nlist.size();

      publishResults(constraint, results);
      return results;
    }

    @Override public void publishResults(CharSequence constraint, FilterResults results) {
      clear();
      add(new Coin(true));
      addAll((ArrayList<Coin>) results.values);
      notifyDataSetChanged();
    }
  }

  public void addAdvert() {
    this.coins.add(2, new Coin(true));
    notifyDataSetChanged();
    clear();
    addAll(this.coins);
  }
}