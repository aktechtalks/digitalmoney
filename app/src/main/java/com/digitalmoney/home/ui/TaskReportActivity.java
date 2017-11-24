package com.digitalmoney.home.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;
import com.digitalmoney.home.models.TaskCounterModel;
import com.digitalmoney.home.models.User;
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
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;

public class TaskReportActivity extends BaseActivity {

    private static final String TAG = TaskReportActivity.class.getSimpleName();
    private String                    taskTitle;
    private TextView                  tvTotalCounter;
    private TextView                  tvSuccessImpressionCounter;
    private TextView                  tvInstallCounter;
    private TextView                  tvSuccessInstallCounter;
    private AppCompatButton           btnInstall;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_counter);

        initUI();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TaskCounterModel counter = dataSnapshot.child("users").child(user.getUid()).child("taskCounter").getValue(TaskCounterModel.class);

                if (counter!=null){

          /*        String installCount = counter.getInstallCount();
                    String successImpression = counter.getSuccessInpression();
                    String successInstall = counter.getSuccessInstall();
                    String totalImpression = counter.getTotalInpression();

                    tvTotalCounter.setText(installCount);
                    tvSuccessImpressionCounter.setText(installCount);
                    tvTotalCounter.setText(installCount);
                    tvTotalCounter.setText(installCount);*/

           /*         Log.e("installCount",installCount);
                    Log.e("successImpression",successImpression);
                    Log.e("successInstall",successInstall);
                    Log.e("totalImpression",totalImpression);*/
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });
    }



    private void initUI(){

        Toolbar toolbar                = (Toolbar) findViewById(R.id.toolbar);
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
        getSupportActionBar().setTitle(taskTitle);
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

        String success_impression = getLocale(getApplicationContext(), PREF_KEY_TOTAL_IMPRESSION);
        getLocale(getApplicationContext(), PREF_KEY_SUCCESS_IMPRESSION);
        getLocale(getApplicationContext(), PREF_KEY_INSTALL);
        getLocale(getApplicationContext(), PREF_KEY_SUCCESS_INSTALL);
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
