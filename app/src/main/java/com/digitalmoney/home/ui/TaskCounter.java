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

import com.daasuu.cat.CountAnimationTextView;
import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;

import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;

public class TaskCounter extends BaseActivity {

    private Typeface typefaceBold;
    private CountAnimationTextView mCountAnimationTextView;
    private ProgressBar count_progress;
    private ImageView finishedView;
    private Button buttonNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_counter);
        initUI();
    }

    private void initUI(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleToolbar = (TextView) toolbar.findViewById(R.id.titleToolbar);
        final String taskTitle = getIntent().getStringExtra("taskTitle");
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

                Snackbar.make(mCountAnimationTextView, "Finished",Snackbar.LENGTH_LONG).show();
            }
        });



        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TaskReportActivity.class);
                intent.putExtra("taskTitle",taskTitle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }


    private void increaseSuccessImpression() {

        String total_pref = getPreferences(Utils.PREF_KEY_TOTAL_IMPRESSION);
        int pref_count = Integer.parseInt(total_pref);
        ++pref_count;

        savePreferences(Utils.PREF_KEY_TOTAL_IMPRESSION, String.valueOf(pref_count));
        savePreferences(Utils.PREF_KEY_SUCCESS_IMPRESSION,"");
        savePreferences(Utils.PREF_KEY_INSTALL,"");
        savePreferences(Utils.PREF_KEY_SUCCESS_INSTALL, "");
    }

}
