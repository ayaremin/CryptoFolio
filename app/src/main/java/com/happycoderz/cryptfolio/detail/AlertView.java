package com.happycoderz.cryptfolio.detail;

import com.happycoderz.cryptfolio.BaseView;
import com.happycoderz.cryptfolio.models.Alert;
import io.realm.RealmResults;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by EminAyar on 17.10.2017.
 */

public interface AlertView extends BaseView {

  void setListDate(RealmResults<Alert> alertArrayList);
}
