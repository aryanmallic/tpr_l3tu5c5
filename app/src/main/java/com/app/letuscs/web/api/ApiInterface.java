package com.app.letuscs.web.api;


import com.app.letuscs.models.homeModel.ModelBanner;
import com.app.letuscs.models.modelLogin.ModelLogin;
import com.app.letuscs.models.modelPostCount.ModelPostCount;
import com.app.letuscs.models.nestedCommentModel.ModelNestedComment;
import com.app.letuscs.models.others.ModelLike;
import com.app.letuscs.models.postDetailModel.ModelPostDetail;
import com.app.letuscs.models.postsModel.ModelPosts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiInterface {

    @GET("allpost")
    Call<List<ModelPosts>> getPost(@Header("Authorization") String auth,
                                   @Query("offset") String offset,
                                   @Query("limit") String limit);

    @GET("postdetail")
    Call<List<ModelPostDetail>> getPostDetail(
            @Header("Authorization") String auth,
            @Query("post_id") int postId);


    @GET("commentdetail")
    Call<List<ModelNestedComment>> getCommentDetail(
            @Query("comment_id") int commentId);


    @GET("postcount")
    Call<ModelPostCount> getTotalPost();

    @GET("banners")
    Call<ArrayList<ModelBanner>> getBannersList();

    //////////////////
    //////////////////
    @POST("like")
    Call<ModelLike> postLike(@HeaderMap Map<String, String> headers,
                             @Query("post_id") int postId);

    @POST("login")
    Call<ModelLogin> postLogin(@QueryMap HashMap<String, String> stringHasMap);

    @POST("register")
    Call<ModelLogin> postRegister(@QueryMap HashMap<String, String> stringHasMap);
}
