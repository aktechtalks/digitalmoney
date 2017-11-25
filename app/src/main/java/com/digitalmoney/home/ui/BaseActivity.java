package com.digitalmoney.home.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;
import com.digitalmoney.home.admin.AdminNotification;
import com.digitalmoney.home.models.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.digitalmoney.home.Utility.Utils.SHARED_PREF;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_BOLD;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_LARGE;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_MEDIUM;
import static com.digitalmoney.home.Utility.Utils.TYPEFACE_PATH_REGULAR;

/**
 * Created by shailesh on 11/11/17.
 */

public class BaseActivity extends AppCompatActivity{

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }


    /* Set Preferences */
    public void setLocale(Context context, String pref_key, String pref_value) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pref_key, pref_value);
        editor.apply();
    }

    /* Get Preferences */
    public  String getLocale(Context context, String pref_key) {
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREF, 0);
        return prefs.getString(pref_key, "0");
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public void applyBoldFont(TextView tv) {
        tv.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), TYPEFACE_PATH_BOLD));
    }

    public void applyLargeFont(TextView tv) {
        tv.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), TYPEFACE_PATH_LARGE));
    }

    public void applyMediumFont(TextView tv) {
        tv.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), TYPEFACE_PATH_MEDIUM));
    }

    public void applyRegularFont(TextView tv) {
        tv.setTypeface(Typeface.createFromAsset(getApplicationContext().getAssets(), TYPEFACE_PATH_REGULAR));
    }



}
