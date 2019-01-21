package com.app.letuscs.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.app.letuscs.activity.PostDetailActivity;
import com.app.letuscs.adapter.AdapterPost;
import com.app.letuscs.models.postsModel.ModelPosts;
import com.app.letuscs.utility.AppController;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.SharedPref;
import com.app.letuscs.web.api.ApiClient;
import com.app.letuscs.web.api.ApiInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends BaseFragment {
    private static final String TAG = HomeFragment.class.getName();
    private RecyclerView rvHome;
    private Context mContext;

    private AdapterPost adapterPost;
    private List<ModelPosts> modelPosts;

    private static final int MAX_LIMIT=10;
    int offset=0,limit=MAX_LIMIT;

    AdapterPost.PostSetOnItemClickListner postSetOnItemClickListner;

    private Parcelable recyclerViewState;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Override
    protected int defineLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initializeComponent(View view) {
       rvHome = view.findViewById(R.id.fragment_home_rvHome);
    }

    @Override
    protected void initializeComponentsBehaviour() {
        postSetOnItemClickListner=new AdapterPost.PostSetOnItemClickListner() {
            @Override
            public void getPostClickPosition(int postId) {
                Intent intent=new Intent(mContext, PostDetailActivity.class);
                                intent.putExtra(Constants.POST_ID,postId);
                startActivity(intent);
            }

            @Override
            public void getLikesView(int position, int postId) {
                MyLikeBehaviourAsyncTask myLikeBehaviourAsyncTask=new MyLikeBehaviourAsyncTask(postId);
                myLikeBehaviourAsyncTask.execute();
            }
        };
        modelPosts=new ArrayList<>();
        adapterPost=new AdapterPost(modelPosts,mContext,postSetOnItemClickListner);
        rvHome.setLayoutManager(new LinearLayoutManager(mContext));
        rvHome.setAdapter(adapterPost);

        showMyLoader(mContext);
        getMyPosts(offset,limit);
        recyclerViewState = rvHome.getLayoutManager().onSaveInstanceState();

        //recyclerViewState = rvHome.getLayoutManager().onSaveInstanceState();
        // Restore state
        //rvHome.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    private void getMyPosts(int offset, int limit) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String auth="bearer "+new SharedPref(mContext).getKey();
        Log.d(TAG,"AUTH: "+auth);
        Call<List<ModelPosts>> call = apiService.getPost(auth,"0","5");
        call.enqueue(new Callback<List<ModelPosts>>() {
            @Override
            public void onResponse(Call<List<ModelPosts>> call, Response<List<ModelPosts>> response) {
                if(response.body()!=null){
                    hideMyLoader();
                    if(response.isSuccessful()) {
                        modelPosts = response.body();
                        adapterPost.setPostList(modelPosts);
                        rvHome.getLayoutManager().onRestoreInstanceState(recyclerViewState);
                        /*Log.d(TAG,"SIZE: "+modelPosts.get(0).getLikes().size()+" "+
                        new SharedPref(mContext).getUserId()+" "+
                                new SharedPref(mContext).getKey());*/

                        //notifyMyAdapter(modelPosts);

                    }else{

                    }
                }
            }

            @Override
            public void onFailure(Call<List<ModelPosts>> call, Throwable t) {
                Log.d(TAG,"Response2: "+t.getMessage());
                hideMyLoader();
            }
        });
    }

    private void notifyMyAdapter(List<ModelPosts> modelPosts) {
    }

    private class MyLikeBehaviourAsyncTask extends AsyncTask{

        int postId;
        public MyLikeBehaviourAsyncTask(int postId) {
            this.postId=postId;
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
                                JSONObject jsonObject=new JSONObject(response);
                                if(jsonObject.getString("status").equals("success")){
                                    getMyPosts(offset,limit);
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
                String auth="bearer "+new SharedPref(mContext).getKey();
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
}
