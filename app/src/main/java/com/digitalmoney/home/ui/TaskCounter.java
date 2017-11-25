package com.digitalmoney.home.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daasuu.cat.CountAnimationTextView;
import com.digitalmoney.home.R;
import com.digitalmoney.home.models.TaskCounterModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import static com.digitalmoney.home.Utility.Utils.PREF_KEY_INSTALL;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_SUCCESS_IMPRESSION;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_SUCCESS_INSTALL;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_TOTAL_IMPRESSION;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;

public class TaskCounter extends BaseActivity {

    private Typeface typefaceBold;
    private CountAnimationTextView mCountAnimationTextView;
    private ProgressBar count_progress;
    private ImageView finishedView;
    private Button buttonNext;
    private DatabaseReference mDatabase;
    String taskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_counter);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        loadAddView();

        initUI();
    }

    private void initUI(){

        Toolbar toolbar         = (Toolbar) findViewById(R.id.toolbar);
        TextView titleToolbar   = (TextView) toolbar.findViewById(R.id.titleToolbar);


        taskTitle = getIntent().getStringExtra("taskTitle");
        titleToolbar.setText(taskTitle);
        typefaceBold = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_BOLD);
        titleToolbar.setTypeface(typefaceBold);

        titleToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        count_progress = (ProgressBar) findViewById(R.id.count_progress);
        finishedView = (ImageView) findViewById(R.id.finishedView);
        buttonNext = (Button) findViewById(R.id.buttonNext);

        mCountAnimationTextView = (CountAnimationTextView) findViewById(R.id.count_animation_textView);
        mCountAnimationTextView.setAnimationDuration(10000).countAnimation(18, 0);
        mCountAnimationTextView.setCountAnimationListener(new CountAnimationTextView.CountAnimationListener() {
            @Override
            public void onAnimationStart(Object animatedValue) {
                count_progress.setVisibility(View.VISIBLE);
                finishedView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Object animatedValue) {
                count_progress.setVisibility(View.GONE);
                finishedView.setVisibility(View.VISIBLE);
                mCountAnimationTextView.setVisibility(View.GONE);
                buttonNext.setVisibility(View.VISIBLE);
                increaseSuccessImpression();

            }
        });



        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public boolean onNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), TaskReportActivity.class);
        intent.putExtra("taskTitle", taskTitle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void increaseSuccessImpression() {

        String total_pref = getLocale(getApplicationContext(), PREF_KEY_TOTAL_IMPRESSION);
        int pref_count = Integer.parseInt(total_pref);
        ++pref_count;

        setLocale(getApplicationContext(),PREF_KEY_TOTAL_IMPRESSION, String.valueOf(pref_count));
        setLocale(getApplicationContext(),PREF_KEY_SUCCESS_IMPRESSION,"0");
        setLocale(getApplicationContext(),PREF_KEY_INSTALL,"0");
        setLocale(getApplicationContext(),PREF_KEY_SUCCESS_INSTALL, "");
        saveDataInFirebaseDB(String.valueOf(pref_count),"0","0","0");

    }




    private void loadAddView(){

        AdView adView1    = (AdView)findViewById(R.id.adView1);
        AdView adView2    = (AdView)findViewById(R.id.adView2);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView1.loadAd(adRequest);
        adView2.loadAd(adRequest);
    }



    protected void saveDataInFirebaseDB(String totalImpression, String successImpression, String totalInstall, String successInstall){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!=null){

            mDatabase.child("users").child(user.getUid()).child("taskCounter").push()
                    .setValue(new TaskCounterModel(totalImpression, successImpression, totalInstall, successInstall),
                            new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                    if (databaseError == null)
                                    {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.added_successfully), Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.addedion_failed), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

        }


    }
}
