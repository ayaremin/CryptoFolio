package com.happycoderz.cryptfolio.models;

import java.util.ArrayList;

/**
 * Created by EminAyar on 6.12.2017.
 */

public class Portfolio {

  ArrayList<Coin> coins = new ArrayList<>();
  String currency = "USD";

  public Portfolio() {
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public ArrayList<Coin> getCoins() {
    return coins;
  }

  public void setCoins(ArrayList<Coin> coins) {
    this.coins = coins;
  }
}
