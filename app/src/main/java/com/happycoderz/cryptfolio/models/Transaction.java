package com.happycoderz.cryptfolio.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import java.text.DateFormat;
import java.util.Date;

public class Transaction extends RealmObject {
    private String coin;
    private Date date;
    private String market;
    private String pair;
    private double amount;
    private int type;
    private double buyPrice;

    public static int BUY = 0;
    public static int SELL = 1;

    public Transaction() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getPair() {
        return coin.toUpperCase() + '/' + pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(double buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getDateFormatted () {
        return DateFormat.getDateTimeInstance().format(date);
    }

    public String getDateText () {
        return getDateFormatted();
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public double getTransactionPrice () {
        return amount * buyPrice;
    }
}