package com.happycoderz.cryptfolio.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;

/**
 * Created by EminAyar on 25.12.2017.
 */

public class IcoResult {

  @SerializedName("ico") public ICO ico;

  public class ICO {
    @SerializedName("live") public ArrayList<com.happycoderz.cryptfolio.models.ICO> live;
    @SerializedName("upcoming") public ArrayList<com.happycoderz.cryptfolio.models.ICO> upcoming;
    @SerializedName("finished") public ArrayList<com.happycoderz.cryptfolio.models.ICO> finished;
  }
}
