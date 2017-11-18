package com.digitalmoney.home.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;

import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;

public class TaskReportActivity extends BaseActivity {

    private static final String TAG = "TaskCounter";
    private String taskTitle;

    private Typeface typefaceBold;
    private TextView tvTotalCounter;
    private TextView tvSuccessImpressionCounter;
    private TextView tvInstallCounter;
    private TextView tvSuccessInstallCounter;
    private AppCompatButton btnInstall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_counter);

        initUI();
    }



    private void initUI(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleToolbar = (TextView) toolbar.findViewById(R.id.titleToolbar);
        taskTitle = getIntent().getStringExtra("taskTitle");
        titleToolbar.setText(taskTitle);
        typefaceBold = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_BOLD);
        //typefaceBold = Typeface.createFromAsset(getAssets(), TYPEFACE_PATH_BOLD);
        titleToolbar.setTypeface(typefaceBold);

        titleToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        TextView tvTotalImpression = (TextView) findViewById(R.id.tvTotalImpression);
        tvTotalImpression.setTypeface(typefaceBold);
        tvTotalCounter = (TextView) findViewById(R.id.tvTotalCounter);
        tvTotalCounter.setTypeface(typefaceBold);

        /* Textview for Success impression*/
        TextView tvSuccessImpression = (TextView) findViewById(R.id.tvSuccessImpression);
        tvSuccessImpression.setTypeface(typefaceBold);
        tvSuccessImpressionCounter = (TextView) findViewById(R.id.tvSuccessImpressionCounter);
        tvSuccessImpressionCounter.setTypeface(typefaceBold);

        /* Textview fot install*/
        TextView tvInstall = (TextView) findViewById(R.id.tvInstall);
        tvInstall.setTypeface(typefaceBold);
        tvInstallCounter = (TextView) findViewById(R.id.tvInstallCounter);
        tvInstallCounter.setTypeface(typefaceBold);

        /* Textview for Success install*/
        TextView tvSuccessInstall = (TextView) findViewById(R.id.tvSuccessInstall);
        tvSuccessInstall.setTypeface(typefaceBold);
        tvSuccessInstallCounter = (TextView) findViewById(R.id.tvSuccessInstallCounter);
        tvSuccessInstallCounter.setTypeface(typefaceBold);

        /* Button for install*/
        btnInstall = (AppCompatButton) findViewById(R.id.btnInstall);
        btnInstall.setTypeface(typefaceBold);

        setInstallCounter();

        btnClickHandler();
    }


    private void setInstallCounter() {

        String success_impression = getPreferences(Utils.PREF_KEY_TOTAL_IMPRESSION);
        getPreferences(Utils.PREF_KEY_SUCCESS_IMPRESSION);
        getPreferences(Utils.PREF_KEY_INSTALL);
        getPreferences(Utils.PREF_KEY_SUCCESS_INSTALL);

        tvTotalCounter.setText("15");
        tvSuccessImpressionCounter.setText(success_impression);
    }



    private void btnClickHandler(){

        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TaskCounter.class);
                intent.putExtra("taskTitle", taskTitle);
                startActivity(intent);
            }
        });
    }
}
