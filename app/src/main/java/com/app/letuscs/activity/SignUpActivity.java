package com.app.letuscs.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.letuscs.R;
import com.app.letuscs.helper.GetUser;
import com.app.letuscs.helper.ManualSignUp;

public class SignUpActivity extends BaseActivity {

    private static final String TAG = SignUpActivity.class.getName();
    private Context mContext;
    private EditText etName, etEmail, etPhone, etPassword, etConfirmPassword;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_sign_up;
    }

    @Override
    protected void initializeComponents() {
        etName = findViewById(R.id.activity_signUp_etName);
        etEmail = findViewById(R.id.activity_signUp_etEmail);
        etPhone = findViewById(R.id.activity_signUp_etPhone);
        etPassword = findViewById(R.id.activity_signUp_etPassword);
        etConfirmPassword = findViewById(R.id.activity_signUp_etConfirmPassword);
    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = getApplicationContext();
    }

    public void onSave(View v){
        boolean isNotEmpty = validateFields(new EditText[]{etName, etEmail, etPhone, etPassword, etConfirmPassword});
        if (isNotEmpty) {
            if (checkSimilarity() && isValidEmail(etEmail.getText().toString())) {
                GetUser getUser = new ManualSignUp(this
                        , etName.getText().toString()
                        , etEmail.getText().toString()
                        , etPhone.getText().toString()
                        , etPassword.getText().toString());

                getUser.getUserDetails();
            } else {
                if(!checkSimilarity()) {
                    Toast.makeText(mContext, "Passwords are not same", Toast.LENGTH_SHORT).show();
                }else etEmail.setError("Email not valid");
            }

        }
    }

    private boolean checkSimilarity() {
        if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            return true;
        }
        return false;
    }

    private boolean validateFields(EditText[] fields) {
        for (int i = 0; i < fields.length; i++) {
            EditText currentField = fields[i];
            if (currentField.getText().toString().trim().isEmpty()) {
                currentField.setError("Cannot be empty");
                return false;
            }
        }
        return true;
    }
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
