package com.martdev.android.currencyconverter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.martdev.android.currencyconverter.loader.CurrencyConverterLoader;
import com.martdev.android.currencyconverter.model.CurrencyConverter;

import java.text.DateFormat;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<CurrencyConverter> {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final String FORGE_URL = "https://forex.1forge.com/1.0.3/convert?";
    private static final String API_KEY = "jdjWCXdLAQUuBpZVjmO6t9yTdlKO8qvm";

    private static final int CONVERSION_LOADER_ID = 0;

    private EditText mAmount;
    private TextView mConversionResult;
    private Spinner mSpinner1, mSpinner2;
    private Button mConvertButton;

    private String amount, convertFrom, convertTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAmount = findViewById(R.id.amount);

        mConversionResult = findViewById(R.id.result);

        mSpinner1 = findViewById(R.id.spinner1);
        mSpinner2 = findViewById(R.id.spinner2);

        mConvertButton = findViewById(R.id.button);
        mConvertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = mAmount.getText().toString();
                convertFrom = String.valueOf(mSpinner1.getSelectedItem());
                convertTo = String.valueOf(mSpinner2.getSelectedItem());
                checkNetwork();
            }
        });
    }

    private void checkNetwork() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            Toast.makeText(this, "Converting...", Toast.LENGTH_LONG).show();

            if (loaderManager.getLoader(CONVERSION_LOADER_ID) == null) {
                loaderManager.initLoader(CONVERSION_LOADER_ID, null, MainActivity.this);
                Log.d(TAG, "checkNetwork: initLoader called");
            } else {
                loaderManager.restartLoader(CONVERSION_LOADER_ID, null, MainActivity.this);
                Log.d(TAG, "checkNetwork: restartLoader called");
            }
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @NonNull
    @Override
    public Loader<CurrencyConverter> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d(TAG, "onCreateLoader: ");
        Uri baseUri = Uri.parse(FORGE_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon()
                .appendQueryParameter("from", convertFrom)
                .appendQueryParameter("to", convertTo)
                .appendQueryParameter("quantity", amount)
                .appendQueryParameter("api_key", API_KEY);

        return new CurrencyConverterLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<CurrencyConverter> loader, CurrencyConverter data) {
        Log.d(TAG, "onLoadFinished: called");

        if (data != null) {
            String value = String.valueOf(data.getAmount());
            String response = data.getResponse();
            String date = DateFormat.getDateTimeInstance().format(data.getDate());
            mConversionResult.setText("Value: " + value);
            mConversionResult.append("\n" + response);
            mConversionResult.append("\n" + "Date: " + date);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<CurrencyConverter> loader) {

    }
}
