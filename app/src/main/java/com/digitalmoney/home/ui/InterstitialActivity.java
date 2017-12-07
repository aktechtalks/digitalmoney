package com.digitalmoney.home.ui;


import com.digitalmoney.home.Utility.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.igalata.bubblepicker.BubblePickerListener;
import com.igalata.bubblepicker.adapter.BubblePickerAdapter;
import com.igalata.bubblepicker.model.BubbleGradient;
import com.igalata.bubblepicker.model.PickerItem;
import com.igalata.bubblepicker.rendering.BubblePicker;

import org.jetbrains.annotations.NotNull;

import static com.digitalmoney.home.Utility.Utils.PREF_KEY_INSTALL;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_SUCCESS_INSTALL;

public class InterstitialActivity extends BaseActivity implements RewardedVideoAdListener {

    private Button             mNextLevelButton;
    private AdView             mAdViewTop;
    private AdView             mAdViewBottom;
    private AdRequest          mAdRequest;
    private final String       AD_UNIT_ID = "ca-app-pub-1594372409902675/9435164874";
    private RewardedVideoAd    mRewardedVideoAd;
    private final String VIDEO_TAG = "RewardedVideoAd";
    private DatabaseReference  mDatabase;
    private String             taskTitle;
    private String uid =       FirebaseAuth.getInstance().getCurrentUser().getUid();
    private BubblePicker       picker;
    private RelativeLayout     main_layout;
    private Boolean isAdClosed = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstial);

        Intent intent = getIntent();
        taskTitle = intent.getStringExtra("taskTitle");

        mDatabase            = FirebaseDatabase.getInstance().getReference();
        mAdViewTop           = (AdView) findViewById(R.id.adViewShareTop);
        mAdViewBottom        = (AdView) findViewById(R.id.adViewShareBottom);
        mAdRequest           = new AdRequest.Builder().build();
        mNextLevelButton     = (Button) findViewById(R.id.next_level_button);
        picker               = (BubblePicker) findViewById(R.id.picker);
        main_layout          = (RelativeLayout) findViewById(R.id.main_layout_interstial);


        applyBoldFont(mNextLevelButton);
        mNextLevelButton.setVisibility(View.GONE);

        loadPicker();

        /* Initialisation of rewarded videos*/
        MobileAds.initialize(this, AD_UNIT_ID);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        /* load
           Banner
           View */

        loadNewBannerAd();
        /* load
           Interstitial
           View */

        newInterstitialAd();
    }






    private void loadPicker(){

        final String[] titles = getResources().getStringArray(R.array.countries);
        final TypedArray colors = getResources().obtainTypedArray(R.array.color_picker);
        final TypedArray images = getResources().obtainTypedArray(R.array.images);

        picker.setAdapter(new BubblePickerAdapter() {
            @Override
            public int getTotalCount() {
                return titles.length;
            }

            @NotNull
            @Override
            public PickerItem getItem(int position) {
                PickerItem item = new PickerItem();
                item.setTitle(titles[position]);
                item.setGradient(new BubbleGradient(colors.getColor((position * 2) % 8, 0),
                        colors.getColor((position * 2) % 8 + 1, 0), BubbleGradient.VERTICAL));
                item.setTextColor(ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                item.setBackgroundImage(ContextCompat.getDrawable(getApplicationContext(), images.getResourceId(position, 0)));
                return item;
            }
        });



        picker.setListener(new BubblePickerListener() {
            @Override
            public void onBubbleSelected(@NotNull PickerItem item) {
                Toast.makeText(InterstitialActivity.this, item.getTitle(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onBubbleDeselected(@NotNull PickerItem item) {

            }
        });


    }



    private void loadNewBannerAd() {

        mAdViewBottom.loadAd(mAdRequest);
        mAdViewTop.loadAd(mAdRequest);

        /*mAdViewTop.setAdListener(new AdListener() {
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
*/
    }

    private InterstitialAd newInterstitialAd() {

        InterstitialAd mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {


            @Override
            public void onAdLoaded() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                }, 5000);
                Log.e("onAdLoaded::", "Success");
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.e("onAdFailedToLoad::", "Failed::"+errorCode);
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Log.e("onAdClicked::", "Loading in browser");

                int installCount = Integer.parseInt(getLocale(getApplicationContext(), PREF_KEY_INSTALL+taskTitle));
                ++installCount;
                setLocale(InterstitialActivity.this, Utils.PREF_KEY_INSTALL+taskTitle, String.valueOf(installCount));
                mDatabase.child("users").child(uid).child("install_count").setValue(installCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.e("install_count", "successfully increased install_count");
                        }
                    }
                });

            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.e("onAdImpression::", " onAdImpression Block");

                int successInstallCount = Integer.parseInt(getLocale(getApplicationContext(), PREF_KEY_SUCCESS_INSTALL+taskTitle));
                ++successInstallCount;
                setLocale(InterstitialActivity.this, Utils.PREF_KEY_SUCCESS_INSTALL+taskTitle, String.valueOf(successInstallCount));
                mDatabase.child("users").child(uid).child("successInstallCount").setValue(successInstallCount).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.e("successInstallCount", "successfully increased successInstallCount");
                        }
                    }
                });
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                isAdClosed = false;
                Log.e("onAdOpened::", "Add is opened");
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                Log.e("onAdLeftApplication::", "Left Application");
            }

            @Override
            public void onAdClosed() {

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mNextLevelButton.setVisibility(View.VISIBLE);
                        mNextLevelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                goToBackPage();
                            }
                        });
                    }
                },10000);

                isAdClosed = true;
            }


        });

        return mInterstitialAd;
    }



    private void goToBackPage() {

        if (isAdClosed = false){
            onBackPressed();
        }else {
            increaseRevenue();
            onBackPressed();
            //showRewardedVideo();
        }


    }

    private void increaseRevenue() {

        String getWalletMoney     = getLocale(this, Utils.WALLET_MONEY);
        double wallet_money       = Double.parseDouble(getWalletMoney);
        double current_revenue    = Utils.SUCCESS_IMPRESSION_REVENUE;
        String totalWalletMoney   = String.format("%.2f", (wallet_money+current_revenue));


        setLocale(this, Utils.WALLET_MONEY, totalWalletMoney);
        mDatabase.child("users").child(uid).child("wallet_money").setValue(totalWalletMoney).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    String price_added = "₹ "+Utils.SUCCESS_IMPRESSION_REVENUE+" added in wallet";
                    Toast toast = Toast.makeText(getApplicationContext(), price_added, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });


        setLocale(this, Utils.PREF_KEY_TOTAL_IMPRESSION+taskTitle, Utils.TOTAL_IMPRESSION);


        int success_impression =Integer.parseInt(getLocale(this,  Utils.PREF_KEY_SUCCESS_IMPRESSION+taskTitle));
        ++success_impression;
        setLocale(this, Utils.PREF_KEY_SUCCESS_IMPRESSION+taskTitle, String.valueOf(success_impression));
        mDatabase.child("users").child(uid).child("success_impression").setValue(success_impression).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task){
                if (task.isSuccessful()){
                    Log.e("success_impression", "successfully increased success_impression");
                }
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), TaskReportActivity.class);
        intent.putExtra("taskTitle", taskTitle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded())
        {
            mRewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    private void showRewardedVideo() {

        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewarded(RewardItem reward) {
        Log.i(VIDEO_TAG, "onRewarded! currency: " + reward.getType() + "  amount: " + reward.getAmount());
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Log.i(VIDEO_TAG, "onRewardedVideoAdLeftApplication");
    }

    @Override
    public void onRewardedVideoAdClosed()
    {
        Log.i(VIDEO_TAG, "onRewardedVideoAdClosed");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) { Log.i(VIDEO_TAG, "onRewardedVideoAdFailedToLoad");}

    @Override
    public void onRewardedVideoAdLoaded()
    {
        Log.i(VIDEO_TAG, "onRewardedVideoAdLoaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Log.i(VIDEO_TAG, "onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        Log.i(VIDEO_TAG, "onRewardedVideoStarted");
    }


    @Override
    protected void onResume() {
        super.onResume();
        picker.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        picker.onPause();
    }

}
