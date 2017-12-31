package com.happycoderz.cryptfolio;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.crashlytics.android.Crashlytics;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by EminAyar on 17.10.2017.
 */

public class BaseActivity extends MvpAppCompatActivity implements BaseView {

  MaterialDialog progress;

  @Override public void start() {

  }

  @Override public void hideProgress() {
    try {
      progress.dismiss();
    } catch (Exception e) {
      Crashlytics.logException(e);
    }
  }

  @Override public void showProgress(String message) {
    progress = new MaterialDialog.Builder(this).cancelable(false)
        .title(getString(R.string.dialog_loading))
        .content(message)
        .progress(true, 0)
        .show();
  }

  @Override public void showProgress() {
    progress = new MaterialDialog.Builder(this).cancelable(false)
        .content(getString(R.string.dialog_loading))
        .progress(true, 0)
        .show();
  }

  @Override public void showError(String message) {
    new MaterialDialog.Builder(this).cancelable(false)
        .title(getString(R.string.dialog_title_error))
        .content(message)
        .positiveText("OK")
        .show();
  }

  @Override public void showError(int message) {
    new MaterialDialog.Builder(this).cancelable(false)
        .title(getString(R.string.dialog_title_error))
        .content(message)
        .positiveText("OK")
        .show();
  }

  @Override public void setViews() {

  }

  @Override
  protected void attachBaseContext(Context newBase) {
    super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
  }

  public void hideSoftKeyboard() {
    if(getCurrentFocus()!=null) {
      InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }

  /**
   * Shows the soft keyboard
   */
  public void showSoftKeyboard(View view) {
    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    view.requestFocus();
    inputMethodManager.showSoftInput(view, 0);
  }
}
