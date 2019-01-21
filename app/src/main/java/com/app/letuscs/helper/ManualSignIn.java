package com.app.letuscs.helper;

import android.app.Activity;
import android.widget.Toast;

import com.app.letuscs.models.modelLogin.ModelLogin;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.web.api.apiPost.PostLoginApi;
import com.google.gson.Gson;

import java.util.HashMap;

public class ManualSignIn extends GetUser implements PostLoginApi.PostLogonApiListener {

    private static final String TAG = ManualSignIn.class.getName();
    private PostLoginApi postLoginApi;
    private Activity mActivity;
    private String username, password;


    public ManualSignIn(Activity mActivity, String username, String password) {
        this.mActivity = mActivity;
        this.username = username;
        this.password = password;
    }

    @Override
    public void getUserDetails() {
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.PARAM_TYPE, "manual");
        hashMap.put(Constants.PARAM_EMAIL, username);
        hashMap.put(Constants.PARAM_PASSWORD, password);
        postLoginApi=new PostLoginApi(mActivity,this);
        postLoginApi.postLogin(hashMap);

    }

    @Override
    public void onPostLogonApiSuccess(ModelLogin response) {
        onSuccess(mActivity,response);
    }

    @Override
    public void onPostLogonApiError(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();

    }
}
