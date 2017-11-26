package com.digitalmoney.home.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;
import com.digitalmoney.home.Utility.VideoBanner;
import com.digitalmoney.home.models.TaskCounterModel;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.digitalmoney.home.Utility.Utils.PREF_KEY_INSTALL;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_SUCCESS_IMPRESSION;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_SUCCESS_INSTALL;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_TOTAL_IMPRESSION;

public class TaskReportActivity extends BaseActivity {

    private final String TAG   = TaskReportActivity.class.getSimpleName();
    private String             taskTitle;
    private TextView           tvTotalCounter;
    private TextView           tvSuccessImpressionCounter;
    private TextView           tvInstallCounter;
    private TextView           tvSuccessInstallCounter;
    private AppCompatButton    btnInstall;
    private DatabaseReference  mDatabase;
    private AdView             mAdViewTop;
    private AdRequest          mAdRequest;
    private AdView             mAdViewBottom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_counter);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initUI();
        loadNewBannerAd();

    }



    private void loadNewBannerAd() {

        mAdViewBottom.loadAd(mAdRequest);
        mAdViewTop.loadAd(mAdRequest);

        mAdViewTop.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        mAdViewBottom.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });

    }


    private void initUI(){

        mAdViewTop                     = (AdView)   findViewById(R.id.adViewShareTop);
        mAdViewBottom                  = (AdView)   findViewById(R.id.adViewShareBottom);
        mAdRequest                     = new AdRequest.Builder().build();
        Toolbar toolbar                = (Toolbar)  findViewById(R.id.toolbar);
        TextView tvTotalImpression     = (TextView) findViewById(R.id.tvTotalImpression);
        tvTotalCounter                 = (TextView) findViewById(R.id.tvTotalCounter);
        TextView tvSuccessImpression   = (TextView) findViewById(R.id.tvSuccessImpression);
        tvSuccessImpressionCounter     = (TextView) findViewById(R.id.tvSuccessImpressionCounter);
        TextView tvInstall             = (TextView) findViewById(R.id.tvInstall);
        tvInstallCounter               = (TextView) findViewById(R.id.tvInstallCounter);
        TextView tvSuccessInstall      = (TextView) findViewById(R.id.tvSuccessInstall);
        tvSuccessInstallCounter        = (TextView) findViewById(R.id.tvSuccessInstallCounter);
        btnInstall                     = (AppCompatButton) findViewById(R.id.btnInstall);
        taskTitle                      = getIntent().getStringExtra("taskTitle");

        setSupportActionBar(toolbar);
        SpannableString fragTitle = Utils.setSpannableString(getApplicationContext(), taskTitle, Utils.TYPEFACE_LARGE);
        getSupportActionBar().setTitle(fragTitle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        applyBoldFont(tvSuccessImpression);
        applyBoldFont(tvSuccessImpressionCounter);
        applyBoldFont(tvTotalImpression);
        applyBoldFont(tvTotalCounter);
        applyBoldFont(tvInstall);
        applyBoldFont(tvInstallCounter);
        applyBoldFont(tvSuccessInstall);
        applyBoldFont(tvSuccessInstallCounter);
        applyBoldFont(btnInstall);

        setInstallCounter();

        btnClickHandler();
    }


    private void setInstallCounter() {

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String totalImpression = getLocale(getApplicationContext(), PREF_KEY_TOTAL_IMPRESSION+taskTitle);
        String success_impression = getLocale(getApplicationContext(), PREF_KEY_SUCCESS_IMPRESSION+taskTitle);
        String installCount = getLocale(getApplicationContext(), PREF_KEY_INSTALL+taskTitle);
        String successInstallCount = getLocale(getApplicationContext(), PREF_KEY_SUCCESS_INSTALL+taskTitle);

        tvTotalCounter.setText(Utils.TOTAL_IMPRESSION);

        tvSuccessImpressionCounter.setText(success_impression);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object impressionCount = dataSnapshot.child("users").child(uid).child("success_impression"+taskTitle).getValue();

                if (impressionCount!=null){
                    Log.e("success_impression:::",impressionCount.toString());
                    tvSuccessImpressionCounter.setText(impressionCount.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->",
                        "success impression could not update", databaseError.toException());
            }
        });





        //add from local storage
        tvInstallCounter.setText(installCount);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object installCount = dataSnapshot.child("users").child(uid).child("install_count"+taskTitle).getValue();

                if (installCount!=null){
                    Log.e("success_impression:::",installCount.toString());
                    tvInstallCounter.setText(installCount.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->",
                        "success impression could not update", databaseError.toException());
            }
        });




        //add from local storage
        tvSuccessInstallCounter.setText(successInstallCount);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Object success_install_count = dataSnapshot.child("users").child(uid).child("successInstallCount"+taskTitle).getValue();

                if (success_install_count!=null){
                    Log.e("success_impression:::",success_install_count.toString());
                    tvSuccessInstallCounter.setText(success_install_count.toString());
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->",
                        "success impression could not update", databaseError.toException());
            }
        });




    }



    private void btnClickHandler(){

        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentVideoBanner =
                        new Intent(TaskReportActivity.this,
                        InterstitialActivity.class);
                intentVideoBanner.putExtra("taskTitle", taskTitle);
                startActivity(intentVideoBanner);


                /*Intent intent = new Intent(getApplicationContext(), TaskCounter.class);
                intent.putExtra("taskTitle", taskTitle);
                startActivity(intent);*/
            }
        });
    }



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
