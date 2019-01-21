package com.app.letuscs.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateUtils;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HelperMethods {

    private static final String TAG=HelperMethods.class.getName();
    Context mContext;

    public HelperMethods(Context mContext) {
        this.mContext = mContext;
    }

    public String getDateStatus(String myDate) throws ParseException {
        Log.d(TAG, "after Current Time - 24hr");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        Log.d(TAG, "CurrTime: " + formattedDate);

        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = sdf2.parse(myDate);
        String niceDateStr = String.valueOf(DateUtils.getRelativeTimeSpanString(date.getTime(), Calendar.getInstance().getTimeInMillis(), DateUtils.SECOND_IN_MILLIS));
        Log.d(TAG, "time: " + niceDateStr);

        if(niceDateStr.equals("0 seconds ago")||niceDateStr.equals("In 0 seconds")){
            niceDateStr="Just Now";
        }

        return niceDateStr;
    }


    public static boolean isOnline(Context mActivity) {
        ConnectivityManager cm = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }
}
