package com.happycoderz.cryptfolio.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.happycoderz.cryptfolio.BuildConfig;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Portfolio;
import io.realm.Realm;

public class CacheHelper {

  private static CacheHelper objectCache;
  private Context context;
  private SharedPreferences preferences;
  private SharedPreferences.Editor editor;
  private static Gson GSON = new Gson();
  private static final String LOG_TAG = "CacheHelper";
  private static final String PREFERENCES_NAME = BuildConfig.APPLICATION_ID;
  private static final int PREFERENCES_MODE = Activity.MODE_PRIVATE;
  public static final String COIN_ADD = "coin_add";
  public static final String DETAIL_SCREEN_AD = "detail_ad";
  public static final String AMOUNT_CHANGED = "amount_changed";
  public static final String REMOVE_ADS = "remove_ads";
  public static final String DONATE_US = "donate_us";
  private static final int COIN_ADD_FREQ = 10;
  private static final int DETAIL_AD_FREQ = 2;
  private static final int AMOUNT_CHANGE_FREQ = 5;

  private CacheHelper(Context context) {
    this.context = context;
    preferences = context.getSharedPreferences(PREFERENCES_NAME, PREFERENCES_MODE);
    editor = preferences.edit();
  }

  public static CacheHelper getInstance(Context context) {
    if (objectCache == null) objectCache = new CacheHelper(context);
    return objectCache;
  }

  public void putObject(String key, Object object) {
    editor.putString(key, GSON.toJson(object));
    editor.commit();
  }

  public void putObject(String key, boolean object) {
    editor.putBoolean(key, object);
    editor.commit();
  }

  public void commit() {
    editor.commit();
  }

  public <T> T getObject(String key, Class<T> a) {
    String gson = preferences.getString(key, null);
    if (gson == null) {
      return null;
    } else {
      try {
        return GSON.fromJson(gson, a);
      } catch (Exception e) {
        throw e;
      }
    }
  }

  public boolean getBool(String key) {
    return preferences.getBoolean(key, false);
  }

  public void removeObject(String key) {
    editor.remove(key);
    editor.commit();
  }

  public Portfolio getPortfolio() {
    if (getObject("portfolio", Portfolio.class) == null) {
      return new Portfolio();
    } else {
      return getObject("portfolio", Portfolio.class);
    }
  }

  public void updateCurrency(String currency) {
    Portfolio folio;
    if (getObject("portfolio", Portfolio.class) == null) {
      folio = new Portfolio();
    } else {
      folio = getObject("portfolio", Portfolio.class);
    }

    folio.setCurrency(currency);
    putObject("portfolio", folio);
  }

  public void updateCoin(int index, Coin coin) {
    Portfolio folio;
    if (getObject("portfolio", Portfolio.class) == null) {
      folio = new Portfolio();
    } else {
      folio = getObject("portfolio", Portfolio.class);
    }
    double amount = folio.getCoins().get(index).getMyHoldings();
    coin.setMyHoldings(amount);
    folio.getCoins().set(index, coin);
    putObject("portfolio", folio);
  }

  public void updateHoldings(int index, double amount) {
    Portfolio folio;
    if (getObject("portfolio", Portfolio.class) == null) {
      folio = new Portfolio();
    } else {
      folio = getObject("portfolio", Portfolio.class);
    }
    folio.getCoins().get(index).setMyHoldings(amount);
    putObject("portfolio", folio);
    Log.d("portfolio",new Gson().toJson(folio.getCoins().get(index)));
  }

  public void updateHoldings (Coin coin, double amount) {

  }

  public void removeCoin(int index) {
    Portfolio folio;
    if (getObject("portfolio", Portfolio.class) == null) {
      folio = new Portfolio();
    } else {
      folio = getObject("portfolio", Portfolio.class);
    }

    folio.getCoins().remove(index);
    putObject("portfolio", folio);
  }

  public void addCoin(Coin coin) {
    Portfolio folio;
    if (getObject("portfolio", Portfolio.class) == null) {
      folio = new Portfolio();
    } else {
      folio = getObject("portfolio", Portfolio.class);
    }

    folio.getCoins().add(coin);
    putObject("portfolio", folio);
  }

  public boolean isExist (String name) {
    Portfolio folio;
    if (getObject("portfolio", Portfolio.class) == null) {
      folio = new Portfolio();
    } else {
      folio = getObject("portfolio", Portfolio.class);
    }

    for (Coin coin : folio.getCoins()) {
      if (name.equalsIgnoreCase(coin.getName())) {
        return true;
      }
    }
    return false;
  }

  public boolean willAdShown(String key) {
    boolean result = false;
    int amount;
    try {
      amount = getObject(key, int.class);
    } catch (Exception e) {
      amount = 0;
    }

    if (key.equals(COIN_ADD)) {
      if (amount != 0 && amount % COIN_ADD_FREQ == 0) {
        result = true;
      } else {
        result = false;
      }
    } else if (key.equals(DETAIL_SCREEN_AD)) {
      if (amount != 0 && amount % DETAIL_AD_FREQ == 1) {
        result = true;
      } else {
        result = false;
      }
    } else {
      if (amount != 0 && amount % AMOUNT_CHANGE_FREQ == 0) {
        result = true;
      } else {
        result = false;
      }
    }
    putObject(key, amount + 1);
    return result;
  }

  public void removeAds () {
    putObject(REMOVE_ADS, true);
  }

  public void showAds () {
    putObject(REMOVE_ADS, false);
  }

  public boolean isAdFree () {
    boolean isAdFree = getBool(REMOVE_ADS);
    return isAdFree;
  }

  public boolean isLoggedIn () {
   return getBool("loggedin");
  }

  public String getUserNickName () {
    return getObject("username", String.class);
  }
}
