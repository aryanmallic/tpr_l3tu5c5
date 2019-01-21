package com.app.letuscs.helper;

import android.app.Activity;
import android.widget.Toast;

import com.app.letuscs.models.modelLogin.ModelLogin;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.web.api.apiPost.PostSignUpApi;

import java.util.HashMap;

public class ManualSignUp extends GetUser implements PostSignUpApi.PostSignUpApiListener {

    private Activity mActivity;
    private String name, email, phone, password;
    private PostSignUpApi postSignUpApi;

    public ManualSignUp(Activity mActivity, String name, String email, String phone, String password) {
        this.mActivity = mActivity;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    @Override
    public void getUserDetails() {
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.PARAM_TYPE, "manual");
        hashMap.put(Constants.PARAM_NAME, name);
        hashMap.put(Constants.PARAM_EMAIL, email);
        hashMap.put(Constants.PARAM_CONTACT_NO, phone);
        hashMap.put(Constants.PARAM_PASSWORD, password);

        postSignUpApi = new PostSignUpApi(mActivity, this);
        postSignUpApi.postSignUp(hashMap);
    }

    @Override
    public void onPosSignUpSuccess(ModelLogin response) {
        onSuccess(mActivity,response);

    }

    @Override
    public void onPostSignUpError(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }
}
