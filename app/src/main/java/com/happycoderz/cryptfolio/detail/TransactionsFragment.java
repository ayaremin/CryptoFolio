package com.happycoderz.cryptfolio.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.happycoderz.cryptfolio.BaseFragment;
import com.happycoderz.cryptfolio.R;

/**
 * Created by EminAyar on 11.12.2017.
 */

public class TransactionsFragment extends BaseFragment {

  @Nullable @Override public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
  ) {
    View view = inflater.inflate(R.layout.fragment_transactions, container, false);
    ButterKnife.bind(this, view);
    

    return view;
  }
}
