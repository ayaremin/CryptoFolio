package com.happycoderz.cryptfolio.models;

import android.text.TextUtils;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Alert extends RealmObject {
    private String coinName;
    private double lessThan;
    private double greatThan;
    private String symbol;
    private boolean persistent;
    private boolean stopped;

    public Alert() {
    }

    public Alert(String coinName, double lessThan, double greatThan, String symbol) {
        this.coinName = coinName;
        this.lessThan = lessThan;
        this.greatThan = greatThan;
        this.symbol = symbol;
        this.persistent = false;
        this.stopped = false;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public String getCoinName() {
        return coinName;
    }

    public void setCoinName(String coinName) {
        this.coinName = coinName;
    }

    public double getLessThan() {
        return lessThan;
    }

    public void setLessThan(double lessThan) {
        this.lessThan = lessThan;
    }

    public double getGreatThan() {
        return greatThan;
    }

    public void setGreatThan(double greatThan) {
        this.greatThan = greatThan;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCondition () {
        StringBuilder sb = new StringBuilder();
        if (lessThan > 0 && greatThan == Double.MAX_VALUE) {
            sb.append("≤ " + lessThan);
        }
        if (greatThan > 0  && greatThan != Double.MAX_VALUE && lessThan <= 0) {
            sb.append("≥ " + greatThan);
        }
        if (greatThan > 0 && greatThan != Double.MAX_VALUE && lessThan > 0) {
            sb.append("≤ " + lessThan);
            sb.append("\n");
            sb.append("≥ " + greatThan);
        }
        return sb.toString();
    }
}