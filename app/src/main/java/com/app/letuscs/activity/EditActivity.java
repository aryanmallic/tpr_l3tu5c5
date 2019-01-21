package com.app.letuscs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.app.letuscs.utility.AppController;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.SharedPref;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditActivity extends BaseActivity {

    private Context mContext;
    private CircleImageView civCommentProfile;
    private EditText etPostComment;
    private Button btCancel, btUpdate;
    private CoordinatorLayout clParent;

    private int commentId = 0, nestedCommentId = 0;
    private String type;
    private static final String TAG = EditText.class.getName();

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_edit;
    }

    @Override
    protected void initializeComponents() {
        civCommentProfile = findViewById(R.id.activity_edit_civCommentProfile);
        etPostComment = findViewById(R.id.activity_edit_etPostComment);
        btCancel = findViewById(R.id.activity_edit_btCancel);
        btUpdate = findViewById(R.id.activity_edit_btUpdate);
        clParent = findViewById(R.id.activity_edit_clParent);
    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = getApplicationContext();
        Glide.with(mContext)
                .load(new SharedPref(mContext).getUserImage())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(civCommentProfile);

        if (getIntent().getExtras() != null) {
            String comment = getIntent().getExtras().getString("comment");
            etPostComment.setText(comment);
            etPostComment.setSelection(etPostComment.getText().length());

            if (getIntent().getExtras().getString("type").equals("comment")) {
                type = getIntent().getExtras().getString("type");
                commentId = getIntent().getExtras().getInt("commentId");
            }
        }
        btCancel.setOnClickListener(this);
        btUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        /*//To return data to previous activity
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("loginStatus","true");
                            setResult(Activity.RESULT_OK,returnIntent);
                            finish();

                         */
        switch (v.getId()) {
            case R.id.activity_edit_btCancel:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;
            case R.id.activity_edit_btUpdate:
                if (type.equals("comment")) {
                    if (!etPostComment.getText().toString().trim().equals("")) {
                        String comment = etPostComment.getText().toString();
                        MyCommentUpdateAsyncTask myCommentUpdateAsyncTask = new MyCommentUpdateAsyncTask(commentId, comment);
                        myCommentUpdateAsyncTask.execute();
                    } else {
                        Toast.makeText(mContext, "Nothing to update", Toast.LENGTH_SHORT).show();
                    }

                } else {

                }
                break;
            default:
                break;
        }
    }

    private class MyCommentUpdateAsyncTask extends AsyncTask {

        int commentId;
        String comment;

        public MyCommentUpdateAsyncTask(int commentId, String comment) {
            this.commentId = commentId;
            this.comment = comment;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            final int[] statusCode = new int[1];
            String url = Constants.BASE_URL + "updatecomment";
            StringRequest request = new StringRequest(Request.Method.POST, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (statusCode[0] == 200) {
                        if (response != null) {
                            //hideMyLoader();
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.getString("status").equals("success")) {
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("commentUpdateStatus","true");
                                    setResult(Activity.RESULT_OK,returnIntent);
                                    finish();
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
                    param.put("comment", comment);
                    return param;
                }
            };
            AppController.getInstance().addToRequestQueue(request);
            return null;
        }
    }
}
