package com.app.letuscs.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.letuscs.R;
import com.app.letuscs.fragment.AccountFragment;
import com.app.letuscs.fragment.HomeFragment;
import com.app.letuscs.fragment.ProfileFragment;
import com.app.letuscs.utility.SharedPref;

public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout flContainer;

    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initializeComponents() {
        bottomNavigationView=findViewById(R.id.activity_main_bottomNavigationView);
        flContainer=findViewById(R.id.activity_main_flContainer);
    }

    @Override
    protected void initializeComponentsBehaviour() {

        addMyFragment(new HomeFragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        addMyFragment(new HomeFragment());
                        return true;
                    case R.id.navigation_account:
                        addMyFragment(new AccountFragment());
                        return true;
                    case R.id.navigation_profile:
                        addMyFragment(new ProfileFragment());
                        return true;
                }
                return false;
            }
        });
    }

    private void addMyFragment(android.support.v4.app.Fragment mFragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.activity_main_flContainer,mFragment);
        fragmentTransaction.commit();
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        SharedPref pref = new SharedPref(MainActivity.this);
        if (!pref.getLoginStatus()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }
    }*/

}
