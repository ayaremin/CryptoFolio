package com.happycoderz.cryptfolio;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.afollestad.materialdialogs.MaterialDialog;
import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.MvpAppCompatFragment;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by EminAyar on 17.10.2017.
 */

public class BaseFragment extends MvpAppCompatFragment implements BaseView {

  MaterialDialog progress;

  @Override public void start() {

  }

  @Override public void hideProgress() {
    progress.dismiss();
  }

  @Override public void showProgress(String message) {
    progress = new MaterialDialog.Builder(getActivity()).cancelable(false)
        .title(getString(R.string.dialog_loading))
        .content(message)
        .progress(true, 0)
        .show();
  }

  @Override public void showProgress() {
    progress = new MaterialDialog.Builder(getActivity()).cancelable(false)
        .content(getString(R.string.dialog_loading))
        .progress(true, 0)
        .show();
  }

  @Override public void showError(String message) {
    new MaterialDialog.Builder(getActivity()).cancelable(false)
        .title(getString(R.string.dialog_title_error))
        .content(message)
        .positiveText("OK")
        .show();
  }

  @Override public void showError(int message) {
    new MaterialDialog.Builder(getActivity()).cancelable(false)
        .title(getString(R.string.dialog_title_error))
        .content(message)
        .positiveText("OK")
        .show();
  }

  @Override public void setViews() {

  }

  public void hideSoftKeyboard() {
    if (getActivity().getCurrentFocus() != null) {
      InputMethodManager inputMethodManager =
          (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
      inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
          0);
    }
  }

  /**
   * Shows the soft keyboard
   */
  public void showSoftKeyboard(View view) {
    InputMethodManager inputMethodManager =
        (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
    view.requestFocus();
    inputMethodManager.showSoftInput(view, 0);
  }
}
