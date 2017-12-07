package com.digitalmoney.home.ui;

import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;
import com.digitalmoney.home.models.User;
import com.github.oliveiradev.lib.RxPhoto;
import com.github.oliveiradev.lib.shared.TypeRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingActivity extends AppCompatActivity {

    private ImageView       pickProfilePic;
    private TextView        tvEmail;
    private TextView        tvMobNumber;
    private TextView        tvName;
    private CircleImageView circleImageView;
    private Toolbar         toolbar;
    private DatabaseReference mDatabase;
    private TextView        tvReferralCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        mDatabase         = FirebaseDatabase.getInstance().getReference();
        pickProfilePic    = (ImageView) findViewById(R.id.pickProfilePhoto);
        toolbar           = (Toolbar) findViewById(R.id.toolbar);
        circleImageView   = (CircleImageView) findViewById(R.id.profile_image);
        tvName            = (TextView) findViewById(R.id.tvName);
        tvMobNumber       = (TextView) findViewById(R.id.tvMobNumber);
        tvEmail           = (TextView) findViewById(R.id.tvEmail);
        tvReferralCode    = (TextView) findViewById(R.id.tvReferralCode);


        SpannableString fragTitle = Utils.setSpannableString(getApplicationContext(), getResources().getString(R.string.action_settings), Utils.TYPEFACE_LARGE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(fragTitle);
        getSupportActionBar().setHomeAsUpIndicator(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_arrow_back, null));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        clickHandler();
    }



    private void clickHandler(){




        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User profile = dataSnapshot.child("users").child(user.getUid()).child("profile").getValue(User.class);

                if (profile!=null){

                    String referralCode = profile.getReferral_code();
                    tvReferralCode.setText("Referral Code : "+referralCode);
                    String userName = profile.getUserName();
                    tvName.setText("Name : "+userName);
                    String userEmail = profile.getUserEmail();
                    tvEmail.setText("Email : "+userEmail);
                    String userPhoneNumber = profile.getUserMobileno();
                    tvMobNumber.setText("Mobile : "+userPhoneNumber);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Error:-->", "loadPost:onCancelled", databaseError.toException());
                Toast.makeText(getApplicationContext(), databaseError.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });




        pickProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pickImageFromGallery();
            }
        });
    }




    private void pickImageFromGallery()
    {
        RxPhoto.requestBitmap(getApplicationContext(),
                TypeRequest.GALLERY).doOnNext((bitmap) -> {
            circleImageView.setImageBitmap(bitmap);
            circleImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }).subscribe();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }



}
