package com.martdev.android.currencyconverter.loader;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.martdev.android.currencyconverter.model.CurrencyConverter;
import com.martdev.android.currencyconverter.utils.QueryNetwork;

public class CurrencyConverterLoader extends AsyncTaskLoader<CurrencyConverter> {

    private static final String TAG = CurrencyConverterLoader.class.getSimpleName();
    private String mURL;

    public CurrencyConverterLoader(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    protected void onStartLoading() {
        Log.d(TAG, "onStartLoading: called");
        forceLoad();
    }

    @Nullable
    @Override
    public CurrencyConverter loadInBackground() {
        Log.d(TAG, "loadInBackground: called");
        if (mURL == null) {
            return null;
        }

        return QueryNetwork.getConvertedCurrency(mURL);
    }
}
