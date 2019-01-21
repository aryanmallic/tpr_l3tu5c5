package com.app.letuscs.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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
import com.app.letuscs.adapter.AdapterNestedComments;
import com.app.letuscs.adapter.AdapterPostDetails;
import com.app.letuscs.models.nestedCommentModel.ModelNestedComment;
import com.app.letuscs.models.nestedCommentModel.ModelNestedCommentNestReply;
import com.app.letuscs.models.postDetailModel.ModelPostDetail;
import com.app.letuscs.utility.AppController;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.HelperMethods;
import com.app.letuscs.utility.SharedPref;
import com.app.letuscs.web.api.ApiClient;
import com.app.letuscs.web.api.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NestedCommentActivity extends BaseActivity {

    private static final String TAG=NestedCommentActivity.class.getName();
    private Context mContext;
    private CoordinatorLayout clParent;
    private RecyclerView rvNestedComment;
    private CircleImageView civProfile,civCommentProfile;
    private TextView tvComment, tvTime, tvReply, tvName;
    private EditText etPostComment;
    private ImageButton ibSend;

    private int commentId;


    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_nested_comment;
    }

    @Override
    protected void initializeComponents() {
        clParent = findViewById(R.id.activity_nested_comment_clParent);
        rvNestedComment = findViewById(R.id.activity_nested_comment_rvNestedComment);
        civProfile = findViewById(R.id.activity_nested_comment_civProfile);
        civCommentProfile=findViewById(R.id.activity_nested_comment_civCommentProfile);
        tvComment = findViewById(R.id.activity_nested_comment_tvComment);
        tvTime = findViewById(R.id.activity_nested_comment_tvTime);
        tvReply = findViewById(R.id.activity_nested_comment_tvReply);
        tvName = findViewById(R.id.activity_nested_comment_tvName);

        etPostComment = findViewById(R.id.activity_nested_comment_etPostComment);
        ibSend = findViewById(R.id.activity_nested_comment_ibSend);
    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = getApplicationContext();
        if (getIntent().getExtras() != null) {
            commentId = getIntent().getExtras().getInt(Constants.COMMENT_ID);
            getNestedComment(commentId);

            ibSend.setOnClickListener(this);
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(new SharedPref(mContext).getLoginStatus()){
            Glide.with(mContext)
                    .load(new SharedPref(mContext).getUserImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(civCommentProfile);
            civCommentProfile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_nested_comment_ibSend:
                if (new SharedPref(mContext).getLoginStatus()) {
                    if (!etPostComment.getText().toString().trim().equals("")) {
                        String nestedComment = etPostComment.getText().toString();
                        MyCommentOnPostCommentAsyncTask myCommentOnPostCommentAsyncTask = new MyCommentOnPostCommentAsyncTask(commentId, nestedComment);
                        myCommentOnPostCommentAsyncTask.execute();
                    } else {
                        Toast.makeText(mContext, "Nothing to comment", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivityForResult(i, 1);
                    //startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            default:
                break;
        }
    }

    private void getNestedComment(int commentId) {
        showMyLoader(NestedCommentActivity.this);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<ModelNestedComment>> call = apiService.getCommentDetail(commentId);
        call.enqueue(new Callback<List<ModelNestedComment>>() {
            @Override
            public void onResponse(Call<List<ModelNestedComment>> call, Response<List<ModelNestedComment>> response) {
                if (response.body() != null) {
                    hideMyLoader();
                    if (response.isSuccessful()) {
                        List<ModelNestedComment> modelNestedComments = response.body();

                        Glide.with(mContext)
                                .load(modelNestedComments.get(0).getUser().getImgUrl())
                                .centerCrop()
                                .into(civProfile);

                        tvComment.setText(modelNestedComments.get(0).getContent());
                        tvName.setText(modelNestedComments.get(0).getUser().getName());

                        String timeAgo = "";
                        try {
                            timeAgo = new HelperMethods(mContext).getDateStatus(modelNestedComments.get(0).getCreatedAt());
                            //timeAgo=getDateStatus(modelNestComments.get(position).getCreatedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tvTime.setText(timeAgo);

                        List<ModelNestedCommentNestReply> modelNestedCommentNestReplies = modelNestedComments.get(0).getNestComment();
                        notifyMyAdapter(modelNestedCommentNestReplies);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ModelNestedComment>> call, Throwable t) {
                hideMyLoader();
            }
        });
    }

    private void notifyMyAdapter(List<ModelNestedCommentNestReply> modelNestedCommentNestReplies) {
        rvNestedComment.setNestedScrollingEnabled(false);
        AdapterNestedComments adapterNestedComments = new AdapterNestedComments(modelNestedCommentNestReplies, mContext);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rvNestedComment.setLayoutManager(manager);
        rvNestedComment.setAdapter(adapterNestedComments);
    }

    private class MyCommentOnPostCommentAsyncTask extends AsyncTask {

        int commentId;
        String nestedComment;
        public MyCommentOnPostCommentAsyncTask(int commentId, String nestedComment) {
            this.commentId=commentId;
            this.nestedComment=nestedComment;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showMyLoader(mContext);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            final int[] statusCode = new int[1];
            String url = Constants.BASE_URL + "nestedcomments";
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (statusCode[0] == 200) {
                        if (response != null) {
                            hideMyLoader();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("success")) {
                                    etPostComment.setText("");
                                    getNestedComment(commentId);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (statusCode[0] == 204) {
                        hideMyLoader();
                        showSnack("No Content", clParent);
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "responseCode:" + statusCode[0]);

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        hideMyLoader();
                        showSnack("No Connectivity", clParent);
                    } else if (error instanceof AuthFailureError) {
                        hideMyLoader();
                        showSnack("Authentication Failed", clParent);
                    } else if (error instanceof ServerError) {
                        hideMyLoader();
                        showSnack("Server Error", clParent);
                    } else if (error instanceof NetworkError) {
                        hideMyLoader();
                        showSnack("Network Connectivity Error", clParent);
                    } else if (error instanceof ParseError) {
                        hideMyLoader();
                        showSnack("Try Again", clParent);
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
                    param.put("comment_id", String.valueOf(commentId));
                    param.put("comment", nestedComment);
                    return param;
                }
            };
            AppController.getInstance().addToRequestQueue(request);
            return null;
        }
    }
}
