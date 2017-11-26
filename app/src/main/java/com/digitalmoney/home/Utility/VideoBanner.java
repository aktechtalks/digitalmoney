package com.digitalmoney.home.Utility;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.models.TaskCounterModel;
import com.digitalmoney.home.ui.BaseActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.digitalmoney.home.Utility.Utils.PREF_KEY_COIN_COUNT;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_INSTALL;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_SUCCESS_IMPRESSION;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_SUCCESS_INSTALL;
import static com.digitalmoney.home.Utility.Utils.PREF_KEY_TOTAL_IMPRESSION;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;

/**
 * Created by shailesh on 20/11/17.
 */

public class VideoBanner extends BaseActivity implements RewardedVideoAdListener {

    private final String AD_UNIT_ID        = "ca-app-pub-3940256099942544/5224354917";
    private final String APP_ID            = "ca-app-pub-3940256099942544~3347511713";
    private final long COUNTER_TIME        = 10;
    private final int GAME_OVER_REWARD     = 1;

    private int                            mCoinCount;
    private TextView                       mCoinCountText;
    private CountDownTimer                 mCountDownTimer;
    private boolean                        mGameOver;
    private boolean                        mGamePaused;
    private RewardedVideoAd                mRewardedVideoAd;
    private Button                         mRetryButton;
    private Button                         mShowVideoButton;
    private long                           mTimeRemaining;
    private DatabaseReference              mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_view);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        Toolbar toolbar         = (Toolbar) findViewById(R.id.toolbar);
        TextView titleToolbar   = (TextView) toolbar.findViewById(R.id.titleToolbar);
        loadAddView();

        String taskTitle = getIntent().getStringExtra("taskTitle");
        titleToolbar.setText(taskTitle);
        Typeface typefaceBold = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_BOLD);
        titleToolbar.setTypeface(typefaceBold);


        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, APP_ID);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        // Create the "retry" button, which tries to show an interstitial between game plays.
        mRetryButton = findViewById(R.id.retry_button);
        mRetryButton.setVisibility(View.INVISIBLE);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        // Create the "show" button, which shows a rewarded video if one is loaded.
        mShowVideoButton = findViewById(R.id.watch_video);
        mShowVideoButton.setVisibility(View.INVISIBLE);
        mShowVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRewardedVideo();
            }
        });

        showRewardedVideo();

        // Display current coin count to user.
        mCoinCountText = findViewById(R.id.coin_count_text);
        mCoinCount = 0;
        mCoinCountText.setText("Coins: " + mCoinCount);

        startGame();
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseGame();
        mRewardedVideoAd.pause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mGameOver && mGamePaused) {
            resumeGame();
        }
        mRewardedVideoAd.resume(this);
    }

    private void pauseGame() {
        mCountDownTimer.cancel();
        mGamePaused = true;
    }

    private void resumeGame() {
        createTimer(mTimeRemaining);
        mGamePaused = false;
    }

    private void loadRewardedVideoAd() {
        if (!mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.loadAd(AD_UNIT_ID, new AdRequest.Builder().build());
        }
    }

    private void addCoins(int coins) {
        mCoinCount = mCoinCount + coins;
        increaseSuccessImpression(mCoinCount);
        mCoinCountText.setText("Coins: " + mCoinCount);
    }



    private void increaseSuccessImpression(int coinCount) {

        String total_pref = getLocale(getApplicationContext(), PREF_KEY_TOTAL_IMPRESSION);
        int increaseCount = Integer.parseInt(total_pref);
        setLocale(getApplicationContext(),PREF_KEY_TOTAL_IMPRESSION, String.valueOf(increaseCount));
        int totalCoinCount = (increaseCount+coinCount);

        setLocale(this, PREF_KEY_COIN_COUNT, String.valueOf(totalCoinCount));
        setLocale(getApplicationContext(),PREF_KEY_SUCCESS_IMPRESSION,"0");
        setLocale(getApplicationContext(),PREF_KEY_INSTALL,"0");
        setLocale(getApplicationContext(),PREF_KEY_SUCCESS_INSTALL, "");

        saveDataInFirebaseDB(String.valueOf(totalCoinCount),"0","0","0");

    }



    private void startGame() {
        // Hide the retry button, load the ad, and start the timer.
        mRetryButton.setVisibility(View.INVISIBLE);
        mShowVideoButton.setVisibility(View.INVISIBLE);
        loadRewardedVideoAd();
        createTimer(COUNTER_TIME);
        mGamePaused = false;
        mGameOver = false;
    }

    // Create the game timer, which counts down to the end of the level
    // and shows the "retry" button.
    private void createTimer(long time) {
        final TextView textView = findViewById(R.id.timer);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mCountDownTimer = new CountDownTimer(time * 1000, 50) {
            @Override
            public void onTick(long millisUnitFinished) {
                mTimeRemaining = ((millisUnitFinished / 1000) + 1);
                textView.setText("seconds remaining: " + mTimeRemaining);
            }

            @Override
            public void onFinish() {
                if (mRewardedVideoAd.isLoaded()) {
                    mShowVideoButton.setVisibility(View.VISIBLE);
                }
                textView.setText("You Lose!");
                addCoins(GAME_OVER_REWARD);
                mRetryButton.setVisibility(View.VISIBLE);
                mGameOver = true;
            }
        };
        mCountDownTimer.start();
    }

    private void showRewardedVideo() {
        mShowVideoButton.setVisibility(View.INVISIBLE);
        if (mRewardedVideoAd.isLoaded()) {
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
        // Preload the next video ad.
        loadRewardedVideoAd();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem reward) {
        Toast.makeText(this,
                String.format(" onRewarded! currency: %s amount: %d", reward.getType(), reward.getAmount()),
                Toast.LENGTH_SHORT).show();
        addCoins(reward.getAmount());
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
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