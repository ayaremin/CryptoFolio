package com.happycoderz.cryptfolio.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.dashboard.CoinFragment;
import com.happycoderz.cryptfolio.dashboard.ICOFragment;
import com.happycoderz.cryptfolio.detail.AlertFragment;
import com.happycoderz.cryptfolio.detail.GraphFragment;
import com.happycoderz.cryptfolio.detail.TransactionsFragment;

/**
 * Created by EminAyar on 11.12.2017.
 */

public class DashboardFragmentPagerAdapter extends FragmentPagerAdapter {

  Context context;

  public DashboardFragmentPagerAdapter(Context context, FragmentManager fm) {
    super(fm);
    this.context = context;
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0 :
        return new CoinFragment();
      case 1:
        return new ICOFragment();
      default:
        return new CoinFragment();
    }
  }

  @Override public int getCount() {
    return 2;
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0 :
        return context.getString(R.string.label_coin_list);
      case 1:
        return context.getString(R.string.label_ico_list);
      default:
        return context.getString(R.string.label_coin_list);
    }
  }
}
