package com.app.letuscs.web.api.apiPost;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.app.letuscs.models.modelLogin.ModelLogin;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.web.api.BaseApi;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostSignUpApi extends BaseApi {

    private static final String TAG = PostSignUpApi.class.getName();
    private Activity mActivity;
    private PostSignUpApiListener mListener;

    public PostSignUpApi(Activity mActivity, PostSignUpApiListener mListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.mListener = mListener;
    }

    public void postSignUp(HashMap param) {
        showMyLoader(mActivity);
        Call<ModelLogin> call = service.postRegister(param);
        call.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                hideMyLoader();
                if (response.isSuccessful()) {
                    mListener.onPosSignUpSuccess(response.body());
                } else {
                    mListener.onPostSignUpError(myErrorMessage(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                hideMyLoader();
                if (t instanceof IOException) {
                    mListener.onPostSignUpError(Constants.API_POOR_NETWORK);
                    Log.d(TAG, "ERROR: Poor Network Connection");
                } else {
                    mListener.onPostSignUpError(Constants.API_ERROR);
                    Log.d(TAG, "ERROR: Conversion issue");
                }
            }
        });
    }

    public interface PostSignUpApiListener {
        void onPosSignUpSuccess(ModelLogin response);

        void onPostSignUpError(String message);
    }
}
