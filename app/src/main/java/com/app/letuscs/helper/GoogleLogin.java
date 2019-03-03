package com.app.letuscs.helper;

import android.app.Activity;
import android.widget.Toast;

import com.app.letuscs.models.modelLogin.ModelLogin;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.SharedPref;
import com.app.letuscs.web.api.apiPost.PostSignUpApi;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class GoogleLogin extends GetUser implements PostSignUpApi.PostSignUpApiListener {

    private Activity mActivity;
    private String personName,personEmail,personId;
    private PostSignUpApi postSignUpApi;


    public GoogleLogin(Activity mActivity, String personName, String personEmail, String personId) {
        this.mActivity = mActivity;
        this.personName = personName;
        this.personEmail = personEmail;
        this.personId = personId;
    }

    @Override
    public void getUserDetails() {
        HashMap hashMap = new HashMap();
                                hashMap.put(Constants.PARAM_TYPE, "google");
                                hashMap.put(Constants.PARAM_NAME,personName);
                                hashMap.put(Constants.PARAM_EMAIL, personEmail);
                                hashMap.put(Constants.PARAM_CONTACT_NO,"123456789");
                                hashMap.put(Constants.PARAM_USER_ID, personId);
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
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            new SharedPref(mActivity).clearPreferences();
            //Firebase Logout/Gmail Logout
            mAuth.signOut();
        }
    }
}
