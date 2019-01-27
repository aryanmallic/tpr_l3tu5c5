package com.app.letuscs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.app.letuscs.adapter.AdapterImageSlider;
import com.app.letuscs.adapter.AdapterImageSliderPostDetail;
import com.app.letuscs.adapter.AdapterPostDetails;
import com.app.letuscs.models.postDetailModel.ModelPostDetail;
import com.app.letuscs.models.postDetailModel.ModelPostDetailComment;
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

import javax.xml.transform.Source;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends BaseActivity {

    private static final String TAG = PostDetailActivity.class.getName();
    private Context mContext;
    private RecyclerView rvPostDetail;
    private CircleImageView civProfile, civCommentProfile;
    private me.relex.circleindicator.CircleIndicator indicator;
    private TextView tvName, tvDate, tvPost, tvCountLikesComment;
    private ImageView ivIconLike;
    private TextView tvIconLike;
    private LinearLayout llLike;
    private EditText etPostComment;
    private ImageButton ibSend;
    private CoordinatorLayout clParent;
    //private LinearLayout llImageCount;
    private RelativeLayout rlImage;
    private ViewPager vpImage;
    //private TextView tvImageCount;

    private int postId;
    private boolean ischange;

    private Parcelable recyclerViewState;

    AdapterPostDetails.SetOnAdapterPostDetailsClickListner setOnAdapterPostDetailsClickListner;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_post_detail;
    }

    @Override
    protected void initializeComponents() {

        clParent = findViewById(R.id.activity_post_detail_clParent);
        rvPostDetail = findViewById(R.id.activity_post_detail_rvPostDetail);
        civProfile = findViewById(R.id.activity_post_detail_civProfile);
        civCommentProfile = findViewById(R.id.activity_post_detail_civCommentProfile);
        tvName = findViewById(R.id.activity_post_detail_tvName);
        tvDate = findViewById(R.id.activity_post_detail_tvDate);
        tvPost = findViewById(R.id.activity_post_detail_tvPost);
        tvCountLikesComment = findViewById(R.id.activity_post_detail_tvCountLikesComment);


        llLike = findViewById(R.id.activity_post_detail_llLike);
        ivIconLike = findViewById(R.id.activity_post_detail_ivIconLike);
        tvIconLike = findViewById(R.id.activity_post_detail_tvIconLike);

        etPostComment = findViewById(R.id.activity_post_detail_etPostComment);
        ibSend = findViewById(R.id.activity_post_detail_ibSend);

        rlImage = findViewById(R.id.activity_post_detail_rlImage);
        //llImageCount = findViewById(R.id.activity_post_detail_llImageCount);
        //tvImageCount = findViewById(R.id.activity_post_detail_tvImageCount);
        vpImage = findViewById(R.id.activity_post_detail_vpImage);

         indicator =findViewById(R.id.indicator);

    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = getApplicationContext();
        rvPostDetail.setNestedScrollingEnabled(false);
        setOnAdapterPostDetailsClickListner = new AdapterPostDetails.SetOnAdapterPostDetailsClickListner() {
            @Override
            public void getCommentId(int commentId) {
                Intent intent = new Intent(mContext, NestedCommentActivity.class);
                intent.putExtra(Constants.COMMENT_ID, commentId);
                startActivity(intent);
            }

            @Override
            public void getCommentIdForLongClick(final int commentId, View v, int userId, final String comment) {
                int currentUserId = new SharedPref(mContext).getUserId();
                if (new SharedPref(mContext).getLoginStatus()) {
                    if (currentUserId == userId) {
                        PopupMenu popup = new PopupMenu(PostDetailActivity.this, v);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.list_popup_menu_edit:
                                        Intent i = new Intent(mContext, EditActivity.class);
                                        i.putExtra("commentId", commentId);
                                        i.putExtra("comment", comment);
                                        i.putExtra("type", "comment");
                                        startActivityForResult(i, 3);
                                        return true;
                                    case R.id.list_popup_menu_delete:
                                        MyCommentDeleteAsyncTask myCommentDeleteAsyncTask = new MyCommentDeleteAsyncTask(commentId);
                                        myCommentDeleteAsyncTask.execute();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popup.inflate(R.menu.list_popup_menu);
                        popup.show();
                    }else{
                        PopupMenu popup2 = new PopupMenu(PostDetailActivity.this, v);
                        popup2.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.list_popup_report_menu_report:
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        popup2.inflate(R.menu.list_popup_report_menu);
                        popup2.show();
                    }
                } else {
                    //Intent i = new Intent(mContext, LoginActivity.class);
                    //startActivityForResult(i, 1);
                }
            }


        };

        if (getIntent().getExtras() != null) {
            postId = getIntent().getExtras().getInt(Constants.POST_ID);
            showMyLoader(PostDetailActivity.this);
            getMyPostDetails(postId);
            llLike.setOnClickListener(this);
            ibSend.setOnClickListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (new SharedPref(mContext).getLoginStatus()) {
            //Picasso.with(mContext).load(new SharedPref(mContext).getUserImage()).into(civCommentProfile);
            Glide.with(mContext)
                    .load(new SharedPref(mContext).getUserImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_student)
                    .into(civCommentProfile);
            civCommentProfile.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_post_detail_llLike:
                if (new SharedPref(mContext).getLoginStatus()) {
                    MyLikeBehaviourAsyncTask myLikeBehaviourAsyncTask = new MyLikeBehaviourAsyncTask(postId);
                    myLikeBehaviourAsyncTask.execute();
                } else {
                    Intent i = new Intent(this, LoginActivity.class);
                    startActivityForResult(i, 1);
                    //startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;

            case R.id.activity_post_detail_ibSend:
                if (new SharedPref(mContext).getLoginStatus()) {
                    if (!etPostComment.getText().toString().trim().equals("")) {
                        String comment = etPostComment.getText().toString();
                        MyCommentOnPostAsyncTask myCommentOnPostAsyncTask = new MyCommentOnPostAsyncTask(postId, comment);
                        myCommentOnPostAsyncTask.execute();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String loginStatus = data.getStringExtra("loginStatus");
                if (loginStatus.equals("true")) {
                    getMyPostDetails(postId);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
        if (requestCode == 3) {
            if (resultCode == Activity.RESULT_OK) {
                String commentUpdateStatus = data.getStringExtra("commentUpdateStatus");
                if (commentUpdateStatus.equals("true")) {
                    getMyPostDetails(postId);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }

    private void getMyPostDetails(int postId) {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        String auth = "bearer " + new SharedPref(mContext).getKey();
        Call<List<ModelPostDetail>> call = apiService.getPostDetail(auth, postId);
        call.enqueue(new Callback<List<ModelPostDetail>>() {
            @Override
            public void onResponse(Call<List<ModelPostDetail>> call, Response<List<ModelPostDetail>> response) {
                if (response.body() != null) {
                    hideMyLoader();
                    if (response.isSuccessful()) {
                        List<ModelPostDetail> modelPostDetails = response.body();

                        Glide.with(mContext)
                                .load(modelPostDetails.get(0).getUser().getImgUrl())
                                .centerCrop()
                                .into(civProfile);
                        tvName.setText(modelPostDetails.get(0).getUser().getName());
                        if (modelPostDetails.get(0).getContent() != null &&
                                !modelPostDetails.get(0).getContent().equals("")) {
                            //rlImage.setVisibility(View.GONE);
                            tvPost.setVisibility(View.VISIBLE);
                            tvPost.setText(modelPostDetails.get(0).getContent());
                        } else {
                            tvPost.setVisibility(View.GONE);
                        }
                        if (modelPostDetails.get(0).getImage() != null &&
                                modelPostDetails.get(0).getImage().size() > 0) {
                            rlImage.setVisibility(View.VISIBLE);
                            //tvPost.setVisibility(View.GONE);
                            AdapterImageSliderPostDetail adapterImageSlider = new AdapterImageSliderPostDetail(mContext, modelPostDetails.get(0).getImage());
                            vpImage.setAdapter(adapterImageSlider);
                            indicator.setViewPager(vpImage);

                            /*if (modelPostDetails.get(0).getImage().size() >= 2) {
                                tvImageCount.setVisibility(View.VISIBLE);

                                String text = "+" + modelPostDetails.get(0).getImage().size();
                                tvImageCount.setText(text);
                            } else {
                                tvImageCount.setVisibility(View.GONE);
                            }*/
                        } else {
                            rlImage.setVisibility(View.GONE);
                        }


                        String timeAgo = "";
                        try {
                            timeAgo = new HelperMethods(mContext).getDateStatus(modelPostDetails.get(0).getCreatedAt());
                            //timeAgo=getDateStatus(modelNestComments.get(position).getCreatedAt());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        tvDate.setText(timeAgo);

                        StringBuilder builder = new StringBuilder();
                        if (!modelPostDetails.get(0).getLikesCount().equals("0")) {
                            builder.append(modelPostDetails.get(0).getLikesCount());
                            builder.append(" Likes  ");
                        }
                        if (!modelPostDetails.get(0).getCommentCount().equals("0")) {
                            builder.append(modelPostDetails.get(0).getCommentCount());
                            builder.append(" Comments");
                        }
                        tvCountLikesComment.setText(builder.toString());

                        if (modelPostDetails.get(0).getLikes().size() > 0) {
                            tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
                            ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorPrimary));
                        } else {
                            tvIconLike.setTextColor(mContext.getResources().getColor(R.color.colorText));
                            ivIconLike.setBackgroundTintList(mContext.getResources().getColorStateList(R.color.colorText));
                        }

                        List<ModelPostDetailComment> modelPostDetailComments = modelPostDetails.get(0).getModelPostDetailComment();
                        notifyMyAdapter(modelPostDetailComments);
                        recyclerViewState = rvPostDetail.getLayoutManager().onSaveInstanceState();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ModelPostDetail>> call, Throwable t) {
                hideMyLoader();
            }
        });
    }

    private void notifyMyAdapter(List<ModelPostDetailComment> modelPostDetailComments) {
        AdapterPostDetails adapterPostDetails = new AdapterPostDetails(modelPostDetailComments, mContext, setOnAdapterPostDetailsClickListner);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        rvPostDetail.setLayoutManager(manager);
        rvPostDetail.setAdapter(adapterPostDetails);
        rvPostDetail.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    private class MyCommentDeleteAsyncTask extends AsyncTask {

        int commentId;

        public MyCommentDeleteAsyncTask(int commentId) {
            this.commentId = commentId;
        }


        @Override
        protected Object doInBackground(Object[] objects) {
            final int[] statusCode = new int[1];
            String url = Constants.BASE_URL + "deletecomment";
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (statusCode[0] == 200) {
                        if (response != null) {
                            //hideMyLoader();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("success")) {
                                    getMyPostDetails(postId);

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("changeStatus", "true");
                                    setResult(Activity.RESULT_OK, returnIntent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (statusCode[0] == 204) {
                        //hideMyLoader();
                        showSnack("No Content", clParent);
                    }
                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG, "responseCode:" + statusCode[0]);

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        //hideMyLoader();
                        showSnack("No Connectivity", clParent);
                    } else if (error instanceof AuthFailureError) {
                        //hideMyLoader();
                        showSnack("Authentication Failed", clParent);
                    } else if (error instanceof ServerError) {
                        //hideMyLoader(alertDialog);
                        showSnack("Server Error", clParent);
                    } else if (error instanceof NetworkError) {
                        //hideMyLoader();
                        showSnack("Network Connectivity Error", clParent);
                    } else if (error instanceof ParseError) {
                        //hideMyLoader();
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
                    param.put("id", String.valueOf(commentId));
                    return param;
                }
            };
            AppController.getInstance().addToRequestQueue(request);
            return null;
        }
    }

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
                                    getMyPostDetails(postId);

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("changeStatus", "true");
                                    setResult(Activity.RESULT_OK, returnIntent);
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

    private class MyCommentOnPostAsyncTask extends AsyncTask {

        int postId;
        String comment;

        public MyCommentOnPostAsyncTask(int postId, String comment) {
            this.postId = postId;
            this.comment = comment;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showMyLoader(mContext);
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            final int[] statusCode = new int[1];
            String url = Constants.BASE_URL + "comment";
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
                                    getMyPostDetails(postId);

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("changeStatus", "true");
                                    setResult(Activity.RESULT_OK, returnIntent);
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
                    param.put("post_id", String.valueOf(postId));
                    param.put("comment", comment);
                    return param;
                }
            };
            AppController.getInstance().addToRequestQueue(request);
            return null;
        }
    }
}
