package com.app.letuscs.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.letuscs.R;
import com.app.letuscs.helper.GetUser;
import com.app.letuscs.helper.ManualSignIn;
import com.app.letuscs.localstorage.LocalConstants;
import com.app.letuscs.localstorage.SharedPreferenceUtil;
import com.app.letuscs.utility.AppController;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.SharedPref;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {


    private static final String TAG = LoginActivity.class.getName();
    private Context mContext;
    private Button btLogin;
    private EditText etUsername, etPassword;
    private CoordinatorLayout clParent;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_login;
    }

    @Override
    protected void initializeComponents() {
        btLogin = findViewById(R.id.activity_login_btLogin);
        etUsername = findViewById(R.id.activity_login_etUsername);
        etPassword = findViewById(R.id.activity_login_etPassword);
        clParent = findViewById(R.id.activity_login_clParent);
    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = getApplicationContext();
        btLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.activity_login_btLogin:
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (username.equals("") || password.equals("")) {
                    if (username.equals("")) {
                        showSnack("Username is empty", clParent);
                        return;
                    }
                    if (password.equals("")) {
                        showSnack("Password is empty", clParent);
                        return;
                    }

                } else {
                    GetUser getUser = new ManualSignIn(this, username, password);
                    getUser.getUserDetails();
                    //fetchLoginStatus(username, password);
                }
                break;
            default:
                break;
        }
    }

    public void onSignUp(View view){
        startActivity(new Intent(mContext,SignUpActivity.class));
    }
    /**
     * Fetch Login Status From Server Here
     *
     * @param username
     * @param password
     */
    private void fetchLoginStatus(final String username, final String password) {
        showMyLoader(LoginActivity.this);
        final int[] statusCode = new int[1];
        String url = Constants.BASE_URL + "login";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (statusCode[0] == 200) {
                    if (response != null) {
                        hideMyLoader();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String token = jsonObject.getString("token");
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                String name = object.getString("name");
                                String type = object.getString("type");
                                String contact_no = object.getString("contact_no");
                                String email = object.getString("email");
                                String address = object.getString("address");
                                String country = object.getString("country");
                                String state = object.getString("state");
                                String is_verified_email = object.getString("is_verified_email");
                                String is_verified_contact_no = object.getString("is_verified_contact_no");
                                String img_url = object.getString("img_url");
                                String referral_code = object.getString("referral_code");
                                String is_password_change_required = object.getString("is_password_change_required");
                                String facebook_id = object.getString("facebook_id");
                                String google_id = object.getString("google_id");
                                String continuous_claim_unit = object.getString("continuous_claim_unit");
                                String last_claim = object.getString("last_claim");
                                String status = object.getString("status");
                                String created_at = object.getString("created_at");
                                String updated_at = object.getString("updated_at");
                                String deleted_at = object.getString("deleted_at");

                                new SharedPref(mContext).setLoginSetupValues(id, name, type,
                                        contact_no, email, address, country, state, is_verified_email,
                                        is_verified_contact_no, img_url, referral_code,
                                        is_password_change_required, facebook_id, google_id,
                                        continuous_claim_unit, last_claim, status, created_at);

                                SharedPreferenceUtil.putValue(LocalConstants.USER_IMAGE, new Gson().toJson(img_url));
                            }
                            Log.d(TAG, "token: " + token);

                            new SharedPref(mContext).setLoginStatus(true);
                            new SharedPref(mContext).setKen(token);
                            //startActivity(new Intent(mContext,MainActivity.class));

                            //To return data to previous activity
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("loginStatus", "true");
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();

                            ///To return nothing to previous activity
                           /* Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_CANCELED, returnIntent);
                            finish();*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /*try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String name = object.getString("name");
                                String profileUrl = object.getString("profile_pic");
                                String apiKey = object.getString("API-KEY");
                                String clientId = object.getString("client_id");
                                String docId = object.getString("id");

                                new SharedPref(mContext).setLoginSetupValues(apiKey, name, profileUrl, clientId, docId);
                                new SharedPref(mContext).setLoginStatus(true);

                                startActivity(new Intent(mContext, MainActivity.class));
                                finish();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                    }
                }
                if (statusCode[0] == 204) {
                    hideMyLoader();
                    showSnack("No Content", clParent);
                }
            }
        }, new Response.ErrorListener() {
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
                    //hideMyLoader(alertDialog);
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
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
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

            /*@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("API-KEY", mContext.getResources().getString(R.string.api_key));
                return map;
            }*/

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("type", "manual");
                param.put("email", username);
                param.put("password", password);
                return param;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPref pref = new SharedPref(LoginActivity.this);
        if (pref.getLoginStatus()) {
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            finish();
        }
    }
}
