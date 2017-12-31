package com.happycoderz.cryptfolio.models;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import retrofit2.http.Field;

/**
 * Created by EminAyar on 6.12.2017.
 */

public class Coin extends RealmObject {

  public static final int NO_SELECTED_CURRENCY = -10;

  @PrimaryKey @SerializedName("id")private String id;
  @SerializedName("name")private String name;
  @SerializedName("symbol")private String symbol;
  @SerializedName("rank")private String rank;
  @SerializedName("price_usd") private String priceUSD;
  @SerializedName("price_btc") private String priceBTC;
  @SerializedName("market_cap_usd") private String marketCap;
  @SerializedName("available_supply") private String availableSupply;
  @SerializedName("max_supply") private String maxSupply;
  @SerializedName("percent_change_24h") private String percentChange;
  @SerializedName("last_updated") private String lastUpdated;
  @SerializedName("24h_volume_usd") private String dailyVolume;
  @SerializedName("price_try") private String priceTRY;
  @SerializedName("price_aud") private String priceAUD;
  @SerializedName("price_brl") private String priceBRL;
  @SerializedName("price_cad") private String priceCAD;
  @SerializedName("price_chf") private String priceCHF;
  @SerializedName("price_clp") private String priceCLP;
  @SerializedName("price_cny") private String priceCNY;
  @SerializedName("price_czk") private String priceCZK;
  @SerializedName("price_dkk") private String priceDKK;
  @SerializedName("price_eur") private String priceEUR;
  @SerializedName("price_gbp") private String priceGBP;
  @SerializedName("price_hkd") private String priceHKD;
  @SerializedName("price_huf") private String priceHUF;
  @SerializedName("price_idr") private String priceIDR;
  @SerializedName("price_ils") private String priceILS;
  @SerializedName("price_inr") private String priceINR;
  @SerializedName("price_jpy") private String priceJPY;
  @SerializedName("price_krw") private String priceKRW;
  @SerializedName("price_mxn") private String priceMXN;
  @SerializedName("price_myr") private String priceMYR;
  @SerializedName("price_nok") private String priceNOK;
  @SerializedName("price_nzd") private String priceNZD;
  @SerializedName("price_php") private String pricePHP;
  @SerializedName("price_pkr") private String pricePKR;
  @SerializedName("price_pln") private String pricePLN;
  @SerializedName("price_rub") private String priceRUB;
  @SerializedName("price_sek") private String priceSEK;
  @SerializedName("price_sgd") private String priceSGD;
  @SerializedName("price_thb") private String priceTHB;
  @SerializedName("price_twd") private String priceTWD;
  @SerializedName("price_zar") private String priceZAR;
  private boolean ad = false;
  private int portfolio;
  private String account;
  private double myHoldings = 0;

  public Coin() {
  }

  public Coin(boolean ad) {
    this.ad = ad;
  }

  public boolean isAd() {
    return ad;
  }

  public void setAd(boolean ad) {
    this.ad = ad;
  }

  public double getMyHoldings() {
    return myHoldings;
  }

  public void setMyHoldings(double myHoldings) {
    this.myHoldings = myHoldings;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  public String getRank() {
    return rank;
  }

  public void setRank(String rank) {
    this.rank = rank;
  }

  public double getPriceUSDasDouble() {
    return Double.parseDouble(priceUSD);
  }

  public String getUSDHoldings () {
    return String.valueOf(myHoldings * getPriceUSDasDouble());
  }

  public String getBTCHoldings () {
    return String.valueOf(myHoldings * Double.parseDouble(priceBTC));
  }

  public double getUSDHoldingsAsDouble () {
    return myHoldings * getPriceUSDasDouble();
  }

  public double getBTCHoldingsAsDouble () {
    return myHoldings * Double.parseDouble(priceBTC);
  }

  public String getPriceUSD() {
    return '$' + priceUSD;
  }

  public double getPriceUSDAsDouble() {
    try {
      return Double.parseDouble(priceUSD) ;
    } catch (Exception e) {
      return 0 ;
    }
  }

  public void setPriceUSD(String priceUSD) {
    this.priceUSD = priceUSD;
  }

  public String getPriceBTC() {
    return priceBTC;
  }

  public void setPriceBTC(String priceBTC) {
    this.priceBTC = priceBTC;
  }

  public String getMarketCap() {
    return "Market Cap : " + marketCap;
  }

  public void setMarketCap(String marketCap) {
    this.marketCap = marketCap;
  }

  public String getAvailableSupply() {
    return availableSupply;
  }

  public void setAvailableSupply(String availableSupply) {
    this.availableSupply = availableSupply;
  }

  public String getMaxSupply() {
    return maxSupply;
  }

  public void setMaxSupply(String maxSupply) {
    this.maxSupply = maxSupply;
  }

  public String getPercentChange() {
    return percentChange;
  }

  public void setPercentChange(String percentChange) {
    this.percentChange = percentChange;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getDailyVolume() {
    return dailyVolume;
  }

  public void setDailyVolume(String dailyVolume) {
    this.dailyVolume = dailyVolume;
  }

  public String getPriceTRY() {
    return priceTRY;
  }

  public void setPriceTRY(String priceTRY) {
    this.priceTRY = priceTRY;
  }

  public String getPriceAUD() {
    return priceAUD;
  }

  public void setPriceAUD(String priceAUD) {
    this.priceAUD = priceAUD;
  }

  public String getPriceBRL() {
    return priceBRL;
  }

  public void setPriceBRL(String priceBRL) {
    this.priceBRL = priceBRL;
  }

  public String getPriceCAD() {
    return priceCAD;
  }

  public void setPriceCAD(String priceCAD) {
    this.priceCAD = priceCAD;
  }

  public String getPriceCHF() {
    return priceCHF;
  }

  public void setPriceCHF(String priceCHF) {
    this.priceCHF = priceCHF;
  }

  public String getPriceCLP() {
    return priceCLP;
  }

  public void setPriceCLP(String priceCLP) {
    this.priceCLP = priceCLP;
  }

  public String getPriceCNY() {
    return priceCNY;
  }

  public void setPriceCNY(String priceCNY) {
    this.priceCNY = priceCNY;
  }

  public String getPriceCZK() {
    return priceCZK;
  }

  public void setPriceCZK(String priceCZK) {
    this.priceCZK = priceCZK;
  }

  public String getPriceDKK() {
    return priceDKK;
  }

  public void setPriceDKK(String priceDKK) {
    this.priceDKK = priceDKK;
  }

  public String getPriceEUR() {
    return priceEUR;
  }

  public void setPriceEUR(String priceEUR) {
    this.priceEUR = priceEUR;
  }

  public String getPriceGBP() {
    return priceGBP;
  }

  public void setPriceGBP(String priceGBP) {
    this.priceGBP = priceGBP;
  }

  public String getPriceHKD() {
    return priceHKD;
  }

  public void setPriceHKD(String priceHKD) {
    this.priceHKD = priceHKD;
  }

  public String getPriceHUF() {
    return priceHUF;
  }

  public void setPriceHUF(String priceHUF) {
    this.priceHUF = priceHUF;
  }

  public String getPriceIDR() {
    return priceIDR;
  }

  public void setPriceIDR(String priceIDR) {
    this.priceIDR = priceIDR;
  }

  public String getPriceILS() {
    return priceILS;
  }

  public void setPriceILS(String priceILS) {
    this.priceILS = priceILS;
  }

  public String getPriceINR() {
    return priceINR;
  }

  public void setPriceINR(String priceINR) {
    this.priceINR = priceINR;
  }

  public String getPriceJPY() {
    return priceJPY;
  }

  public void setPriceJPY(String priceJPY) {
    this.priceJPY = priceJPY;
  }

  public String getPriceKRW() {
    return priceKRW;
  }

  public void setPriceKRW(String priceKRW) {
    this.priceKRW = priceKRW;
  }

  public String getPriceMXN() {
    return priceMXN;
  }

  public void setPriceMXN(String priceMXN) {
    this.priceMXN = priceMXN;
  }

  public String getPriceMYR() {
    return priceMYR;
  }

  public void setPriceMYR(String priceMYR) {
    this.priceMYR = priceMYR;
  }

  public String getPriceNOK() {
    return priceNOK;
  }

  public void setPriceNOK(String priceNOK) {
    this.priceNOK = priceNOK;
  }

  public String getPriceNZD() {
    return priceNZD;
  }

  public void setPriceNZD(String priceNZD) {
    this.priceNZD = priceNZD;
  }

  public String getPricePHP() {
    return pricePHP;
  }

  public void setPricePHP(String pricePHP) {
    this.pricePHP = pricePHP;
  }

  public String getPricePKR() {
    return pricePKR;
  }

  public void setPricePKR(String pricePKR) {
    this.pricePKR = pricePKR;
  }

  public String getPricePLN() {
    return pricePLN;
  }

  public void setPricePLN(String pricePLN) {
    this.pricePLN = pricePLN;
  }

  public String getPriceRUB() {
    return priceRUB;
  }

  public void setPriceRUB(String priceRUB) {
    this.priceRUB = priceRUB;
  }

  public String getPriceSEK() {
    return priceSEK;
  }

  public void setPriceSEK(String priceSEK) {
    this.priceSEK = priceSEK;
  }

  public String getPriceSGD() {
    return priceSGD;
  }

  public void setPriceSGD(String priceSGD) {
    this.priceSGD = priceSGD;
  }

  public String getPriceTHB() {
    return priceTHB;
  }

  public void setPriceTHB(String priceTHB) {
    this.priceTHB = priceTHB;
  }

  public String getPriceTWD() {
    return priceTWD;
  }

  public void setPriceTWD(String priceTWD) {
    this.priceTWD = priceTWD;
  }

  public String getPriceZAR() {
    return priceZAR;
  }

  public void setPriceZAR(String priceZAR) {
    this.priceZAR = priceZAR;
  }

  public double getSelectedCoin() {
    if (!TextUtils.isEmpty(priceAUD)) {
      return Double.valueOf(priceAUD);
    }
    if (!TextUtils.isEmpty(priceBRL)) {
      return Double.valueOf(priceBRL);
    }
    if (!TextUtils.isEmpty(priceCAD)) {
      return Double.valueOf(priceCAD);
    }
    if (!TextUtils.isEmpty(priceCHF)) {
      return Double.valueOf(priceCHF);
    }
    if (!TextUtils.isEmpty(priceCLP)) {
      return Double.valueOf(priceCLP);
    }
    if (!TextUtils.isEmpty(priceCNY)) {
      return Double.valueOf(priceCNY);
    }
    if (!TextUtils.isEmpty(priceCZK)) {
      return Double.valueOf(priceCZK);
    }
    if (!TextUtils.isEmpty(priceDKK)) {
      return Double.valueOf(priceDKK);
    }
    if (!TextUtils.isEmpty(priceEUR)) {
      return Double.valueOf(priceEUR);
    }
    if (!TextUtils.isEmpty(priceGBP)) {
      return Double.valueOf(priceGBP);
    }
    if (!TextUtils.isEmpty(priceHKD)) {
      return Double.valueOf(priceHKD);
    }
    if (!TextUtils.isEmpty(priceHUF)) {
      return Double.valueOf(priceHUF);
    }
    if (!TextUtils.isEmpty(priceIDR)) {
      return Double.valueOf(priceIDR);
    }
    if (!TextUtils.isEmpty(priceILS)) {
      return Double.valueOf(priceILS);
    }
    if (!TextUtils.isEmpty(priceINR)) {
      return Double.valueOf(priceINR);
    }
    if (!TextUtils.isEmpty(priceJPY)) {
      return Double.valueOf(priceJPY);
    }
    if (!TextUtils.isEmpty(priceKRW)) {
      return Double.valueOf(priceKRW);
    }
    if (!TextUtils.isEmpty(priceMXN)) {
      return Double.valueOf(priceMXN);
    }
    if (!TextUtils.isEmpty(priceMYR)) {
      return Double.valueOf(priceMYR);
    }
    if (!TextUtils.isEmpty(priceNOK)) {
      return Double.valueOf(priceNOK);
    }
    if (!TextUtils.isEmpty(priceNZD)) {
      return Double.valueOf(priceNZD);
    }
    if (!TextUtils.isEmpty(pricePHP)) {
      return Double.valueOf(pricePHP);
    }
    if (!TextUtils.isEmpty(pricePKR)) {
      return Double.valueOf(pricePKR);
    }
    if (!TextUtils.isEmpty(pricePLN)) {
      return Double.valueOf(pricePLN);
    }
    if (!TextUtils.isEmpty(priceRUB)) {
      return Double.valueOf(priceRUB);
    }
    if (!TextUtils.isEmpty(priceSEK)) {
      return Double.valueOf(priceSEK);
    }
    if (!TextUtils.isEmpty(priceSGD)) {
      return Double.valueOf(priceSGD);
    }
    if (!TextUtils.isEmpty(priceTHB)) {
      return Double.valueOf(priceTHB);
    }
    if (!TextUtils.isEmpty(priceTRY)) {
      return Double.valueOf(priceTRY);
    }
    if (!TextUtils.isEmpty(priceTWD)) {
      return Double.valueOf(priceTWD);
    }
    if (!TextUtils.isEmpty(priceZAR)) {
      return Double.valueOf(priceZAR);
    }
    return NO_SELECTED_CURRENCY;
  }

}