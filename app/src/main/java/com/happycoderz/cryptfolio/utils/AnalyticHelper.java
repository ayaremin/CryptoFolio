package com.happycoderz.cryptfolio.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.ContentViewEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.happycoderz.cryptfolio.BuildConfig;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.Portfolio;

public class AnalyticHelper {

  private static AnalyticHelper analyticHelper;
  private Context context;
  private FirebaseAnalytics mFirebaseAnalytics;

  private AnalyticHelper(Context context) {
    this.context = context;
    mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
  }

  public static AnalyticHelper getInstance(Context context) {
    if (analyticHelper == null) analyticHelper = new AnalyticHelper(context);
    return analyticHelper;
  }

  public void sendEvent (String key, String value) {
    Answers.getInstance().logContentView(new ContentViewEvent().putCustomAttribute(key, value));
    Bundle bundle = new Bundle();
    bundle.putString(key, value);
    mFirebaseAnalytics.logEvent(key, bundle);
  }
}
