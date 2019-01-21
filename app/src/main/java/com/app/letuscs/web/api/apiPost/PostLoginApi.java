package com.app.letuscs.web.api.apiPost;

import android.app.Activity;
import android.util.Log;
import com.app.letuscs.models.modelLogin.ModelLogin;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.web.api.BaseApi;
import java.io.IOException;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLoginApi extends BaseApi {

    private static final String TAG = PostLikesApi.class.getName();
    private Activity activity;
    private PostLogonApiListener listener;

    public PostLoginApi(Activity activity, PostLogonApiListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
    }

    public void postLogin(HashMap param){
        showMyLoader(activity);
        Call<ModelLogin> call=service.postLogin(param);
        call.enqueue(new Callback<ModelLogin>() {
            @Override
            public void onResponse(Call<ModelLogin> call, Response<ModelLogin> response) {
                hideMyLoader();
                if (response.isSuccessful()) {
                    listener.onPostLogonApiSuccess(response.body());
                }   else{
                    listener.onPostLogonApiError(myErrorMessage(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ModelLogin> call, Throwable t) {
                hideMyLoader();
                if (t instanceof IOException) {
                    listener.onPostLogonApiError(Constants.API_POOR_NETWORK);
                    Log.d(TAG, "ERROR: Poor Network Connection");
                } else {
                    listener.onPostLogonApiError(Constants.API_ERROR);
                    Log.d(TAG, "ERROR: Conversion issue");
                }
            }
        });
    }

    public interface PostLogonApiListener{
        void onPostLogonApiSuccess(ModelLogin response);

        void onPostLogonApiError(String message);
    }
}
