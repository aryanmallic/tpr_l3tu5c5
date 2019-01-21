package com.app.letuscs.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPref {
    private SharedPreferences sharedPreferences;
    private static final String TAG = SharedPref.class.getSimpleName();

    Context mContext;


    public SharedPref() {
    }

    public SharedPref(Context mContext) {
        this.mContext = mContext;
        sharedPreferences = mContext.getSharedPreferences("LETUSCS", Context.MODE_PRIVATE);
    }

    public void setLoginStatus(boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("loginStatus", value);
        editor.apply();
        Log.d(TAG, "setLoginStatus " + value);
    }

    public boolean getLoginStatus() {
        Log.d(TAG, "setLoginStatus " + sharedPreferences.getBoolean("loginStatus", false));
        if (sharedPreferences.getBoolean("loginStatus", false)) {
            return true;
        }
        return false;
    }

    public void setKen(String apiKey) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("apiKey", apiKey);
        editor.apply();
    }

    public String getKey() {
        Log.d(TAG, "KEY: " + sharedPreferences.getString("apiKey", ""));
        return sharedPreferences.getString("apiKey", "");
    }

    /*public void setLoginSetupValues(String apiKey, String name, String profilePic, String clientId, String docId) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("apiKey", apiKey);
        editor.putString("name", name);
        editor.putString("profilePic", profilePic);
        editor.putString("clientId", clientId);
        editor.putString("docId", docId);

        editor.apply();
    }*/

    public void setLoginSetupValues(int id,String token, String name, String email, String phone){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.USER_ID, id);
        editor.putString("apiKey", token);
        editor.putString(Constants.USER_NAME, name);
        editor.putString(Constants.USER_EMAIL, email);
        editor.putString(Constants.USER_CONTACT_NO, phone);
        editor.apply();
    }

    public void setLoginSetupValues(int id, String name, String type, String contact_no,
                                    String email, String address, String country,
                                    String state, String is_verified_email,
                                    String is_verified_contact_no, String img_url,
                                    String referral_code, String is_password_change_required,
                                    String facebook_id, String google_id, String continuous_claim_unit,
                                    String last_claim, String status, String created_at) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(Constants.USER_ID, id);
        editor.putString(Constants.USER_NAME, name);
        editor.putString(Constants.USER_TYPE, type);
        editor.putString(Constants.USER_CONTACT_NO, contact_no);
        editor.putString(Constants.USER_EMAIL, email);
        editor.putString(Constants.USER_ADDRESS, address);
        editor.putString(Constants.USER_COUNTRY, country);
        editor.putString(Constants.USER_STATE, state);
        editor.putString(Constants.USER_IS_VERIFIED_EMAIL, is_verified_email);
        editor.putString(Constants.USER_IS_VERIFIED_CONTACT_NO, is_verified_contact_no);
        editor.putString(Constants.USER_IMG_URL, img_url);
        editor.putString(Constants.USER_REFERRAL, referral_code);
        editor.putString(Constants.USER_IS_PASSWORD_CHANGE_REQUIRED, is_password_change_required);
        editor.putString(Constants.USER_FACEBOOK_ID, facebook_id);
        editor.putString(Constants.USER_GOOGLE_ID, google_id);
        editor.putString(Constants.USER_CONTINUOUS_CLAIM_UNIT, continuous_claim_unit);
        editor.putString(Constants.USER_LAST_CLAIM, last_claim);
        editor.putString(Constants.USER_STATUS, status);
        editor.putString(Constants.USER_CREATED_AT, created_at);

        editor.apply();
    }

    public String getUserName() {
        return sharedPreferences.getString(Constants.USER_NAME, "");
    }

    public int getUserId() {
        return sharedPreferences.getInt(Constants.USER_ID, 0);
    }

    public String getUserImage() {
        return sharedPreferences.getString(Constants.USER_IMG_URL, "");
    }

    public void setPostCount(int noOfPost) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("postCount", noOfPost);
        editor.apply();
    }

    public int getPostCount() {
        return sharedPreferences.getInt("postCount", 0);
    }
}
