package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.interfaces.CoinListener;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.ICO;
import com.happycoderz.cryptfolio.utils.CacheHelper;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;

public class ICOAdapter extends ArrayAdapter<ICO> {

  Context context;
  List<ICO> icos = new ArrayList<>();
  private NativeAd nativeAd;

  public ICOAdapter(Context ctx, ArrayList<ICO> icos) {
    super(ctx, 0);
    this.icos.addAll(icos);
    addAll(this.icos);
    notifyDataSetChanged();
    this.context = ctx;
    nativeAd = new NativeAd(context, context.getString(R.string.facebook_native_ad_ico));
    if (!CacheHelper.getInstance(context).isAdFree()) {
      nativeAd.setAdListener(new com.facebook.ads.AdListener() {
        @Override public void onError(Ad ad, AdError adError) {

        }

        @Override public void onAdLoaded(Ad ad) {
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

  @Override public View getView(int position, View convertView, ViewGroup parent) {

    ICO ico = getItem(position);
    int layout;
    layout = R.layout.item_ico_list;
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(layout, parent, false);
    }
    CoinViewWrapper view = new CoinViewWrapper();
    ButterKnife.bind(view, convertView);
    view.load(ico);

    return convertView;
  }

  class CoinViewWrapper {
    @BindView(R.id.icon) protected ImageView icon;
    @BindView(R.id.name_text_view) protected TextView nameTextView;
    @BindView(R.id.desc_text_view) protected TextView descTextView;
    @BindView(R.id.start_text_view) protected TextView startTextView;
    @BindView(R.id.end_text_view) protected TextView endTextView;
    @BindView(R.id.ad_container) protected LinearLayout adContainer;
    @BindView(R.id.content) protected LinearLayout content;

    public void load(final ICO ico) {
      if (ico.isAd()) {
        adContainer.removeAllViews();
        content.setVisibility(View.GONE);
        adContainer.setVisibility(View.VISIBLE);
        adContainer.addView(
            NativeAdView.render(getContext(), nativeAd, NativeAdView.Type.HEIGHT_300));
      } else {
        adContainer.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        Picasso.with(getContext()).load(ico.getImage()).into(icon);
        nameTextView.setText(ico.getName());
        descTextView.setText(ico.getDescription());
        startTextView.setText(ico.getStartTime());
        endTextView.setText(ico.getEndTime());
      }
    }
  }

  public void addAdvert() {
    this.icos.add(2, new ICO(true));
    notifyDataSetChanged();
    clear();
    addAll(this.icos);
  }
}