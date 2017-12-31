package com.happycoderz.cryptfolio.dashboard;

import com.happycoderz.cryptfolio.BaseView;
import com.happycoderz.cryptfolio.models.Coin;
import com.happycoderz.cryptfolio.models.ICO;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EminAyar on 17.10.2017.
 */

public interface ICOView extends BaseView {

  void showProgress();

  void hideProgress();

  void hideLoading();

  void setListView(ArrayList<ICO> icos);

  void refresh();

  void hideBanner();

}
