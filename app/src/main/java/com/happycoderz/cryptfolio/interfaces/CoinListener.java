package com.happycoderz.cryptfolio.interfaces;

import com.happycoderz.cryptfolio.models.Coin;

/**
 * Created by EminAyar on 7.12.2017.
 */

public interface CoinListener {
  void onCoinAdded(Coin coin);

  void onCoinDeleted(String name, int index);

  void onCoinHoldingChanged ();
}
