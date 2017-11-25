package com.digitalmoney.home.ui;

import com.bumptech.glide.util.Util;
import com.digitalmoney.home.Utility.Utils;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class InterstitialActivity extends BaseActivity implements RewardedVideoAdListener {

    private Button             mNextLevelButton;
    private AdView             mAdViewTop;
    private AdView             mAdViewBottom;
    private AdRequest          mAdRequest;
    private final String       AD_UNIT_ID = "ca-app-pub-1594372409902675/9435164874";
    private RewardedVideoAd    mRewardedVideoAd;
    final String VIDEO_TAG  =  "RewardedVideoAd";
    private DatabaseReference  mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interstial);

        mDatabase            = FirebaseDatabase.getInstance().getReference();
        mAdViewTop           = (AdView) findViewById(R.id.adViewShareTop);
        mAdViewBottom        = (AdView) findViewById(R.id.adViewShareBottom);
        mAdRequest           = new AdRequest.Builder().build();
        mNextLevelButton     = ((Button) findViewById(R.id.next_level_button));
        applyBoldFont(mNextLevelButton);
        mNextLevelButton.setVisibility(View.GONE);

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
                }, 10000);
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
            }


            @Override
            public void onAdImpression() {
                super.onAdImpression();
                Log.e("onAdImpression::", "add impression");
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
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

                        Toast.makeText(InterstitialActivity.this, "Price added to your wallet", Toast.LENGTH_SHORT).show();

                        mNextLevelButton.setVisibility(View.VISIBLE);
                        mNextLevelButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                increaseRevenue();
                                //showRewardedVideo();
                                onBackPressed();
                            }
                        });

                        Toast.makeText(InterstitialActivity.this,
                                "FINISH", Toast.LENGTH_LONG).show();
                    }
                },10000);
            }


        });

        return mInterstitialAd;
    }



    private void increaseRevenue() {

        String getWalletMoney = getLocale(this, Utils.WALLET_MONEY);
        double wallet_money   = Double.parseDouble(getWalletMoney);
        double current_revenue = Utils.SUCCESS_IMPRESSION_REVENUE;
        String totalWalletMoney   = String.valueOf(wallet_money+current_revenue);
        setLocale(this, Utils.WALLET_MONEY, totalWalletMoney);

        /*storeWalletMoneyToFirebase*/
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabase.child("users").child(uid).child("wallet_money").setValue(totalWalletMoney).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(InterstitialActivity.this, "Price added to your wallet", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    @Override
    public void onBackPressed() {

        Intent intent = new Intent(getApplicationContext(), TaskReportActivity.class);
        intent.putExtra("", )
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void loadRewardedVideoAd()
    {
        if (!mRewardedVideoAd.isLoaded()) {
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
    public void onRewardedVideoAdClosed() {
        Log.i(VIDEO_TAG, "onRewardedVideoAdClosed");
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Log.i(VIDEO_TAG, "onRewardedVideoAdFailedToLoad");
    }

    @Override
    public void onRewardedVideoAdLoaded() {
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




}
