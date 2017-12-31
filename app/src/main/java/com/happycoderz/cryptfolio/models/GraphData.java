package com.happycoderz.cryptfolio.models;

/**
 * Created by EminAyar on 18.12.2017.
 */

public class GraphData {
  private String time;
  private double amount;
  private double price;

  public GraphData(String time, String amount, String price) {
    this.time = time;
    this.amount = Double.parseDouble(amount);
    this.price = Double.parseDouble(price);
  }


  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public double getAmount() {
    return amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }
}
