package com.martdev.android.currencyconverter.model;

import java.util.Date;

public class CurrencyConverter {
    private double mAmount;
    private String mResponse;
    private Date mDate;

    public CurrencyConverter(double amount, String response, Date date) {
        mAmount = amount;
        mResponse = response;
        mDate = date;
    }

    public double getAmount() {
        return mAmount;
    }

    public String getResponse() {
        return mResponse;
    }

    public Date getDate() {
        return mDate;
    }
}
