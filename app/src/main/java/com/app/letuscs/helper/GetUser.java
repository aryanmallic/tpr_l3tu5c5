package com.app.letuscs.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.app.letuscs.models.modelLogin.ModelLogin;
import com.app.letuscs.utility.SharedPref;

public abstract class GetUser extends AppCompatActivity {

    private SharedPref pref;

    public GetUser() {

    }

    public abstract void getUserDetails();

    public void onSuccess(Activity mActivity, ModelLogin response) {
        pref = new SharedPref(mActivity);

        Log.d("TAG","TOKEN: "+response.getToken());

        String name = response.getData().get(0).getName();
        saveDetails(response.getData().get(0).getId()
                , response.getToken()
                , name
                , response.getData().get(0).getEmail()
                , response.getData().get(0).getContactNo());

        updateUI(mActivity);

    }

    public void saveDetails(int id,String token, String name, String email, String phone) {
        pref.setLoginSetupValues(id,token, name, email, phone);
        pref.setLoginStatus(true);
    }

    public void updateUI(Activity mActivity) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("loginStatus", "true");
        mActivity.setResult(Activity.RESULT_OK, returnIntent);
        mActivity.finish();
    }
}
