package com.app.letuscs.web.api.apiPost;

import android.app.Activity;
import android.util.Log;

import com.app.letuscs.models.others.ModelLike;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.web.api.BaseApi;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostLikesApi extends BaseApi {
    private static final String TAG = PostLikesApi.class.getName();
    private Activity activity;
    private PostLikeApiListener listener;

    public PostLikesApi(Activity activity, PostLikeApiListener listener) {
        super(activity);
        this.activity = activity;
        this.listener = listener;
    }

    public void postLike(String auth, int postId) {
        //showMyLoader(activity);
        ///Header for the post req
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", auth);
        ////

        Call<ModelLike> call = service.postLike(header, postId);
        call.enqueue(new Callback<ModelLike>() {
            @Override
            public void onResponse(Call<ModelLike> call, Response<ModelLike> response) {
                hideMyLoader();
                if (response.isSuccessful()) {
                    listener.onPostLikeSuccess(response.body());
                } else {
                    listener.onPostLikeError(myErrorMessage(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ModelLike> call, Throwable t) {
                hideMyLoader();
                if (t instanceof IOException) {
                    listener.onPostLikeError(Constants.API_POOR_NETWORK);
                    Log.d(TAG, "ERROR: Poor Network Connection");
                } else {
                    listener.onPostLikeError(Constants.API_ERROR);
                    Log.d(TAG, "ERROR: Conversion issue");
                }
            }
        });

    }

    public interface PostLikeApiListener {
        void onPostLikeSuccess(ModelLike response);

        void onPostLikeError(String message);
    }
}
