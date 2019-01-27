package com.app.letuscs.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import com.app.letuscs.helper.GoogleLogin;
import com.app.letuscs.helper.ManualSignIn;
import com.app.letuscs.localstorage.LocalConstants;
import com.app.letuscs.localstorage.SharedPreferenceUtil;
import com.app.letuscs.utility.AppController;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.SharedPref;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    private LinearLayout llGoogle;
    private CoordinatorLayout clParent;
    private static final int RC_SIGN_IN = 2;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;


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
        llGoogle = findViewById(R.id.activity_login_llGoogle);

    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = getApplicationContext();
        mAuth = FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        //error
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btLogin.setOnClickListener(this);
        llGoogle.setOnClickListener(this);
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
            case R.id.activity_login_llGoogle:

                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                showMyLoader(LoginActivity.this);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.d("Google SignIn: ", "Google sign in failed", e);


                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                int statusCode = result.getStatus().getStatusCode();
                Toast.makeText(mContext, ""+statusCode+": "+result.getStatus().getStatusMessage(), Toast.LENGTH_SHORT).show();
                //ibGoogle.setEnabled(true);
                // ...
            }
        } else {
            // Pass the activity result back to the Facebook SDK
            // mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("TAG", "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideMyLoader();
                        if (task.isSuccessful()) {
                            //ibGoogle.setEnabled(true);
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
                            String personName = "", personEmail = "", personId = "";
                            if (acct != null) {
                                personName = acct.getDisplayName();
                                String personGivenName = acct.getGivenName();
                                String personFamilyName = acct.getFamilyName();
                                personEmail = acct.getEmail();
                                personId = acct.getId();
                                Uri personPhoto = acct.getPhotoUrl();
                                //updateUI();

                                ////////////
                                GetUser getUser = new GoogleLogin(LoginActivity.this, personName, personEmail, personId);
                                getUser.getUserDetails();
                                ////////////

                            }
                            //api.postSocial(personName, personEmail, personId);

                        } else {
                            //ibGoogle.setEnabled(true);
                            // If sign in fails, display a message to the user.
                            Log.d("TAG", "signInWithCredential:failure", task.getException());
                            //updateUI();
                        }
                    }
                });
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

    public void onSignUp(View view) {
        startActivity(new Intent(mContext, SignUpActivity.class));
    }
}
