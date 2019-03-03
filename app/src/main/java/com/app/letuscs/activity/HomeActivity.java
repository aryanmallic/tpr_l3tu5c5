package com.app.letuscs.activity;

import android.content.Context;

import com.app.letuscs.adapter.AdapterBanner;
import com.app.letuscs.adapter.AdapterHome;
import com.app.letuscs.adapter.InfiniteAdapter;
import com.app.letuscs.models.homeModel.ModelBanner;
import com.app.letuscs.models.homeModel.ModelHomeGrids;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.app.letuscs.R;
import com.app.letuscs.utility.Constants;
import com.app.letuscs.utility.HelperMethods;
import com.app.letuscs.utility.SharedPref;
import com.app.letuscs.web.api.ApiClient;
import com.app.letuscs.web.api.ApiInterface;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getName();
    private Context mContext;


    private int[] image = {R.drawable.ic_recent_post, R.drawable.ic_ask_question
            , R.drawable.ic_contest, R.drawable.ic_rank};


    private int[] imageRes = {R.drawable.ic_recent_post, R.drawable.ic_ask_question
            ,R.drawable.ic_account
            , R.drawable.ic_contest, R.drawable.ic_rank
            , R.drawable.ic_archive, R.drawable.ic_course
            , R.drawable.ic_contacts, R.drawable.ic_google_play};

    private String[] imageName = {"Recent Posts", "Ask Questions","My Account"
            , "My Contest", "My Rank"
            , "Archive", "Courses Offered"
            , "Contact Us", "Rate Us"};

    private ViewPager vpBanner;
    private me.relex.circleindicator.CircleIndicator indicator;
    private RecyclerView rvHome;
    private CoordinatorLayout clParent;
    private List<ModelHomeGrids> itemData;
    private AdapterHome adapterHome;
    private AdapterHome.MyOnItemClickListner myOnItemClickListner;

    //Firebase
    private AdView mAdView;


    @Override
    protected int defineLayoutResource() {
        return R.layout.activity_home;
    }

    @Override
    protected void initializeComponents() {
        rvHome = findViewById(R.id.activity_home_rvHome);
        clParent = findViewById(R.id.activity_home_clParent);
        vpBanner = findViewById(R.id.activity_home_vpBanner);
        indicator=findViewById(R.id.indicator);
        mAdView=findViewById(R.id.adView);
    }

    @Override
    protected void initializeComponentsBehaviour() {
        mContext = getApplicationContext();

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        /*Bundle bundle = new Bundle();
        //bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "screen");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home Screen");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "grids");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //Sets whether analytics collection is enabled for this app on this device.
        mFirebaseAnalytics.setAnalyticsCollectionEnabled(true);
        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        mFirebaseAnalytics.setMinimumSessionDuration(20000);
        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        mFirebaseAnalytics.setSessionTimeoutDuration(500);*/

        getBannerFromServer();
        indicator.setViewPager(vpBanner);

        itemData = new ArrayList<>();
        myOnItemClickListner = new AdapterHome.MyOnItemClickListner() {
            @Override
            public void myViews(int position) {
                switch (position) {
                    case 0://RecentPost
                        startActivity(new Intent(mContext, PostActivity.class));
                        break;
                    case 1:
                        //Ask Question
                        if (new SharedPref(mContext).getLoginStatus()) {
                            if (HelperMethods.isOnline(mContext)) {
                                startActivity(new Intent(mContext, WritePostActivity.class));
                            } else {
                                String message = Constants.NO_INTERNET;
                                showSnack(message, clParent);
                            }
                        } else {
                            startActivity(new Intent(mContext, LoginActivity.class));
                        }
                        break;
                    case 2:
                        //My Account
                        Toast.makeText(mContext, "Coming Soon", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        //My Contest
                        String urlContest ="http://atechnologies.co.in/app/contest";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(urlContest));
                        startActivity(i);
                        break;
                    case 4:
                        //My Rank
                        String urlRank="http://atechnologies.co.in/app/rank";
                        Intent i4 = new Intent(Intent.ACTION_VIEW);
                        i4.setData(Uri.parse(urlRank));
                        startActivity(i4);
                        break;
                    case 5:
                        //My Archive
                        String urlArchive="http://atechnologies.co.in/app/archive";
                        Intent i5 = new Intent(Intent.ACTION_VIEW);
                        i5.setData(Uri.parse(urlArchive));
                        startActivity(i5);
                        break;
                    case 6:
                        //my Courses
                        String urlCouse="http://atechnologies.co.in/app/courses";
                        Intent i6 = new Intent(Intent.ACTION_VIEW);
                        i6.setData(Uri.parse(urlCouse));
                        startActivity(i6);
                        break;
                    case 7:
                        //contact
                        String url = "http://atechnologies.co.in/app/contact";
                        Intent i7 = new Intent(Intent.ACTION_VIEW);
                        i7.setData(Uri.parse(url));
                        startActivity(i7);
                        break;
                    case 8:
                        //Rate
                        onUpdate();
                        break;
                    default:
                        break;

                }
            }
        };

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                Log.d(TAG,"VAL: 1");
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d(TAG,"VAL: 2");
                Log.d(TAG,"VAL: "+errorCode);
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                Log.d(TAG,"VAL: 3");
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                Log.d(TAG,"VAL: 4");
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                Log.d(TAG,"VAL: 5");
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        rvHome.setLayoutManager(manager);
        for (int i = 0; i < imageName.length; i++) {
            itemData.add(new ModelHomeGrids(imageName[i], imageRes[i]));
        }
        adapterHome = new AdapterHome(itemData, mContext, myOnItemClickListner);
        rvHome.setAdapter(adapterHome);
    }

    public void onUpdate(){
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void getBannerFromServer() {
        showMyLoader(this);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<ModelBanner>> call = apiInterface.getBannersList();
        call.enqueue(new Callback<ArrayList<ModelBanner>>() {
            @Override
            public void onResponse(Call<ArrayList<ModelBanner>> call, Response<ArrayList<ModelBanner>> response) {
                hideMyLoader();
                if (response.isSuccessful()) {
                    ArrayList<ModelBanner> bannersList=response.body();

                    setBanners(bannersList);
                } else {
                    Log.d(TAG, "RESPONSE CODE" + response.code());
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
            public void onFailure(Call<ArrayList<ModelBanner>> call, Throwable t) {
                hideMyLoader();
                Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setBanners(ArrayList<ModelBanner> bannersList) {
        vpBanner.setOffscreenPageLimit(1);
        vpBanner.setAdapter(new AdapterBanner(this, bannersList));
        indicator.setViewPager(vpBanner);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("Pager", String.valueOf(vpBanner.getCurrentItem() + 1));
                vpBanner.setCurrentItem(vpBanner.getCurrentItem() + 1);
                handler.postDelayed(this, 10000);

            }
        }, 10000);
    }
}
