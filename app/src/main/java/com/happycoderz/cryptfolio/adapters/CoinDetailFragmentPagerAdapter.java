package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.detail.AlertFragment;
import com.happycoderz.cryptfolio.detail.GraphFragment;
import com.happycoderz.cryptfolio.detail.TransactionsFragment;

/**
 * Created by EminAyar on 11.12.2017.
 */

public class CoinDetailFragmentPagerAdapter extends FragmentPagerAdapter {

  Context context;

  public CoinDetailFragmentPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    this.context = context;
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0 :
        return new GraphFragment();
      case 1:
        return new AlertFragment();
      case 2:
        return new TransactionsFragment();
      default:
        return new GraphFragment();
    }
  }

  @Override public int getCount() {
    return 2;
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0 :
        return context.getString(R.string.label_coin_detail_graph);
      case 1:
        return context.getString(R.string.label_coin_detail_alarm);
      case 2:
        return context.getString(R.string.label_coin_detail_transactio);
      default:
        return context.getString(R.string.label_coin_detail_graph);
    }
  }
}
