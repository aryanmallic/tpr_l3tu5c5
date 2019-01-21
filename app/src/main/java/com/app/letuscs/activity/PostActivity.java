package com.app.letuscs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.letuscs.R;
import com.app.letuscs.adapter.AdapterPost;
import com.app.letuscs.localstorage.LocalConstants;
import com.app.letuscs.localstorage.SharedPreferenceUtil;
import com.app.letuscs.models.modelPostCount.ModelPostCount;
import com.app.letuscs.models.others.ModelLike;
import com.app.letuscs.models.postsModel.ModelPosts;
import com.app.letuscs.utility.AppController;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.HelperMethods;
import com.app.letuscs.utility.SharedPref;
import com.app.letuscs.web.api.ApiClient;
import com.app.letuscs.web.api.ApiInterface;
import com.app.letuscs.web.api.apiPost.PostLikesApi;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends BaseActivity implements PostLikesApi.PostLikeApiListener {
    private static final String TAG = PostActivity.class.getName();
    private RecyclerView rvPost;
    private ProgressBar pbList;
    private SwipeRefreshLayout srlPost;
    private Context mContext;
    private CoordinatorLayout clParent;

    private AdapterPost adapterPost;
    private List<ModelPosts> modelPosts;
    private LinearLayoutManager linearLayoutManager;

    private static final int MAX_LIMIT = 5;
    private Call<List<ModelPosts>> callPost;

    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, lastVisiblePos;
    private int start = 0;
    private int end = 0;
    private boolean isSuccessLoad = false;
    private AdapterPost.PostSetOnItemClickListner postSetOnItemClickListner;
    private PostLikesApi postLikesApi;
    //Firebase
    //private com.google.firebase.analytics.FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_post;
    }

    @Override
    protected void initializeComponents() {
        rvPost = findViewById(R.id.activity_post_rvPost);
        srlPost = findViewById(R.id.activity_post_srlPost);
        pbList = findViewById(R.id.activity_post_pbList);
        clParent = findViewById(R.id.activity_post_clParent);
    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = this;

        postLikesApi = new PostLikesApi(this, this);

        postSetOnItemClickListner = new AdapterPost.PostSetOnItemClickListner() {
            @Override
            public void getPostClickPosition(int postId) {
                if (HelperMethods.isOnline(mContext)) {
                    Intent intent = new Intent(mContext, PostDetailActivity.class);
                    intent.putExtra(Constants.POST_ID, postId);
                    startActivityForResult(intent, 1);
                } else {
                    Toast.makeText(mContext, "Oops No Internet", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void getLikesView(int position, int postId) {
                if (new SharedPref(mContext).getLoginStatus()) {
                    if (HelperMethods.isOnline(mContext)) {
                        String auth = "Bearer " + new SharedPref(mContext).getKey();
                        postLikesApi.postLike(auth, postId);
                        //MyLikeBehaviourAsyncTask myLikeBehaviourAsyncTask = new MyLikeBehaviourAsyncTask(postId);
                        //myLikeBehaviourAsyncTask.execute();
                    } else {
                        Toast.makeText(mContext, "No Internet", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent i = new Intent(PostActivity.this, LoginActivity.class);
                    startActivityForResult(i, 2);
                }
            }
        };
        modelPosts = new ArrayList<>();
        adapterPost = new AdapterPost(modelPosts, mContext, postSetOnItemClickListner);
        linearLayoutManager = new LinearLayoutManager(mContext);
        rvPost.setLayoutManager(linearLayoutManager);
        rvPost.setAdapter(adapterPost);


        //////////////////////////////
        if (HelperMethods.isOnline(mContext)) {
            showMyLoader(this);
            getPostCount();
            end = MAX_LIMIT;
            getMyPosts("load");
        } else {
            showLocalData();
        }
        //////////////////////////////


        rvPost.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();
                lastVisiblePos = linearLayoutManager.findLastVisibleItemPosition();

                int postCount = new SharedPref(mContext).getPostCount();

                if (isScrolling &&
                        (currentItems + scrollOutItems == totalItems) &&
                        (totalItems < postCount)
                        && isSuccessLoad) {

                    isSuccessLoad = false;
                    isScrolling = false;
                    pbList.setVisibility(View.VISIBLE);
                    start = totalItems;
                    end = MAX_LIMIT;
                    getMyPosts("scroll");
                    Log.d(TAG, "POSTCOUNT: " + postCount
                            + "\nlastVisiblePos + 1: "
                            + (lastVisiblePos + 1)
                            + "\nend: " + end);
                }

                //////////////////////////////////
                //if ((lastVisiblePos + 1) == offset) {
                //Log.d(TAG, "EQUAL");

                    /*offset = modelPosts.size();
                    limit = offset + MAX_LIMIT;*/
                //getMyPosts(offset, limit);

                //}
                /////////////////////////////////////
            }
        });

        srlPost.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //showMyLoader(PostActivity.this);
                srlPost.setRefreshing(true);
                start = 0;
                end = MAX_LIMIT;
                getMyPosts("refresh");
            }
        });




        /*showMyLoader(this);
        getMyPosts(offset, limit);*/
        //recyclerViewState = rvPost.getLayoutManager().onSaveInstanceState();

        //recyclerViewState = rvHome.getLayoutManager().onSaveInstanceState();
        // Restore state
        //rvHome.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    private void showLocalData() {
        modelPosts.clear();
        modelPosts.addAll(LocalConstants.getPost());
        adapterPost.setPostList(modelPosts);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String changeStatus = data.getStringExtra("changeStatus");
                Log.d(TAG, "changeStatus: " + changeStatus);
                if (changeStatus.equals("true")) {
                    getMyPosts("load");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {
                String loginStatus = data.getStringExtra("loginStatus");
                if (loginStatus.equals("true")) {
                    start = 0;
                    end = MAX_LIMIT;
                    getMyPosts("load");
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void getMyPosts(final String type) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String auth = "bearer " + new SharedPref(mContext).getKey();

        callPost = apiService.getPost(auth, String.valueOf(start), String.valueOf(end));
        callPost.enqueue(new Callback<List<ModelPosts>>() {
            @Override
            public void onResponse(Call<List<ModelPosts>> call, Response<List<ModelPosts>> response) {
                if (response.body() != null) {
                    hideMyLoader();
                    pbList.setVisibility(View.GONE);
                    srlPost.setRefreshing(false);
                    if (response.isSuccessful()) {
                        isSuccessLoad = true;
                        if (new SharedPref(mContext).getPostCount() > end) {
                            start = end;
                            end = MAX_LIMIT;
                        } else {
                            start = new SharedPref(mContext).getPostCount();
                            end = MAX_LIMIT;
                        }

                        //////////////////////
                        SharedPreferenceUtil.putValue(LocalConstants.POST, new Gson().toJson(response.body()));
                        //////////////////////

                        modelPosts = response.body();
                        Log.d(TAG, "listsize: " + modelPosts.size());
                        if (type.equals("load") || type.equals("refresh")) {
                            adapterPost.setPostList(modelPosts);
                        }
                        if (type.equals("scroll")) {
                            adapterPost.loadMoreList(modelPosts);
                        }
                        //rvPost.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<List<ModelPosts>> call, Throwable t) {
                Log.d(TAG, "Response2: " + t.getMessage());
                hideMyLoader();
                if (!callPost.isCanceled()) {
                    Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPostLikeSuccess(ModelLike response) {

    }

    @Override
    public void onPostLikeError(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    //Not Required
    private class MyLikeBehaviourAsyncTask extends AsyncTask {

        int postId;

        public MyLikeBehaviourAsyncTask(int postId) {
            this.postId = postId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showMyLoader(mContext);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            final int[] statusCode = new int[1];
            String url = Constants.BASE_URL + "like";
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (statusCode[0] == 200) {
                        if (response != null) {
                            //hideMyLoader();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("success")) {
                                    getMyPosts("load");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (statusCode[0] == 204) {
                        //hideMyLoader();
                        //showSnack("No Content", clParent);
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "responseCode:" + statusCode[0]);

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        //hideMyLoader();
                        //showSnack("No Connectivity", clParent);
                    } else if (error instanceof AuthFailureError) {
                        //hideMyLoader();
                        //showSnack("Authentication Failed", clParent);
                    } else if (error instanceof ServerError) {
                        //hideMyLoader(alertDialog);
                        //showSnack("Server Error", clParent);
                    } else if (error instanceof NetworkError) {
                        //hideMyLoader();
                        //showSnack("Network Connectivity Error", clParent);
                    } else if (error instanceof ParseError) {
                        //hideMyLoader();
                        //showSnack("Try Again", clParent);
                    }
                }
            }) {
                @Override
                protected com.android.volley.Response<String> parseNetworkResponse(NetworkResponse response) {
                    statusCode[0] = response.statusCode;
                    return super.parseNetworkResponse(response);
                }

                @Override
                protected VolleyError parseNetworkError(VolleyError volleyError) {
                    NetworkResponse networkResponse = volleyError.networkResponse;
                    if (networkResponse != null) {
                        statusCode[0] = volleyError.networkResponse.statusCode;
                        Log.d(TAG, "networkResponse:" + networkResponse.toString());
                    }
                    return super.parseNetworkError(volleyError);
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    String auth = "bearer " + new SharedPref(mContext).getKey();
                    map.put("Authorization", auth);
                    return map;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> param = new HashMap<>();
                    param.put("post_id", String.valueOf(postId));
                    return param;
                }
            };
            AppController.getInstance().addToRequestQueue(request);
            return null;
        }
    }

    private void getPostCount() {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Call<ModelPostCount> call = apiInterface.getTotalPost();
        call.enqueue(new Callback<ModelPostCount>() {
            @Override
            public void onResponse(Call<ModelPostCount> call, Response<ModelPostCount> response) {
                if (response.isSuccessful()) {
                    hideMyLoader();
                    ModelPostCount modelPostCount = response.body();
                    if (modelPostCount.getStatus().equals("success")) {
                        new SharedPref(mContext).setPostCount(modelPostCount.getNoOfPost());
                        getMyPosts("load");
                    } else {

                    }

                } else {
                    hideMyLoader();
                    switch (response.code()) {
                        case 401:
                            showSnack(getString(R.string.network_authentication_failed), clParent);
                            break;
                        case 400:
                            showSnack(getString(R.string.network_bad_request), clParent);
                            break;
                        case 404:
                            showSnack(getString(R.string.network_error_try_again), clParent);
                            break;
                        case 500:
                            showSnack(getString(R.string.network_server_Error), clParent);
                            break;
                        default:
                            showSnack(getString(R.string.network_error_try_again), clParent);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<ModelPostCount> call, Throwable t) {
                hideMyLoader();
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
