package com.app.letuscs.web.api;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.app.letuscs.R;
import com.app.letuscs.utility.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BaseApi {
    private Retrofit retrofit = null;
    private Context context;
    protected ApiInterface service;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog alertDialog;


    public BaseApi(Context context) {
        this.context = context;
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(Constants.WS_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(Constants.WS_READ_TIMEOUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        service = retrofit.create(ApiInterface.class);
    }

    protected String myErrorMessage(int responseCode) {
        String message;
        switch (responseCode) {
            case 401:
                message = Constants.API_AUTHENTICATION_FAILED;
                break;
            case 400:
                message = Constants.API_BAD_REQUEST;
                break;
            case 404:
                message = Constants.API_TRY_AGAIN;
                break;
            case 500:
                message = Constants.API_SERVER_ERROR;
                break;
            default:
                message = Constants.API_TRY_AGAIN;
                break;
        }
        return message;
    }

    public void showMyLoader(Activity activity) {
        if (dialogBuilder == null) {
            dialogBuilder = new AlertDialog.Builder(activity);
        }
        final LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.design_loader, null);
        dialogBuilder.setView(dialogView);
        if (alertDialog == null) {
            alertDialog = dialogBuilder.create();
            alertDialog.setCancelable(false);
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        } else {
            if (!alertDialog.isShowing()) {
                alertDialog.show();
            }
        }
    }

    /**
     * to dismiss the loader
     */
    public void hideMyLoader() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
    }
}
