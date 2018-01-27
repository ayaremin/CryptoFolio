package com.happycoderz.cryptfolio.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.happycoderz.cryptfolio.BaseFragment;
import com.happycoderz.cryptfolio.R;
import com.happycoderz.cryptfolio.adapters.TransactionAdapter;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Transaction;
import io.realm.Realm;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by EminAyar on 11.12.2017.
 */

public class TransactionsFragment extends BaseFragment {

  private TransactionAdapter adapter;
  private Coin selectedCoin;

  @BindView(R.id.list_view) ListView listView;
  @BindView(R.id.transparent_view) View shrinkGrowView;

  @Nullable @Override public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState
  ) {
    View view = inflater.inflate(R.layout.fragment_transactions, container, false);
    ButterKnife.bind(this, view);

    Animation sgAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shrink_grow);
    shrinkGrowView.startAnimation(sgAnimation);

    selectedCoin = ((CoinDetailActivity) getActivity()).getSelectedCoin();

    adapter = new TransactionAdapter(Realm.getDefaultInstance().where(Transaction
        .class).equalTo("coin",selectedCoin.getSymbol()).findAll(), getContext());

    listView.setAdapter(adapter);

    return view;
  }

  @OnClick(R.id.fab) protected void onNewTransactionClicked () {
    FragmentManager manager = getFragmentManager();
    Fragment frag = manager.findFragmentByTag("transaction_fragment");
    if (frag != null) {
      manager.beginTransaction().remove(frag).commit();
    }
    AddTransactionFragment cardDialogFragment = new AddTransactionFragment(selectedCoin);
    cardDialogFragment.show(manager, "transaction_fragment");
  }
}
