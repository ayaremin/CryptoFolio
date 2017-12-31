package com.happycoderz.cryptfolio.models;

import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;

/**
 * Created by EminAyar on 6.12.2017.
 */

public class ICO {

  @SerializedName("name") private String name;
  @SerializedName("image") private String image;
  @SerializedName("description") private String description;
  @SerializedName("website_link") private String website;
  @SerializedName("start_time") private String startTime;
  @SerializedName("end_time") private String endTime;
  private boolean isAd = false;

  public ICO() {
  }

  public ICO(boolean isAd) {
    this.isAd = isAd;
  }

  public boolean isAd() {
    return isAd;
  }

  public void setAd(boolean ad) {
    isAd = ad;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getWebsite() {
    return website;
  }

  public void setWebsite(String website) {
    this.website = website;
  }

  public String getStartTime() {
    return startTime;
  }

  public void setStartTime(String startTime) {
    this.startTime = startTime;
  }

  public String getEndTime() {
    return endTime;
  }

  public void setEndTime(String endTime) {
    this.endTime = endTime;
  }
}