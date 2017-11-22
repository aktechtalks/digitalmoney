package com.digitalmoney.home.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.digitalmoney.home.R;
import com.digitalmoney.home.Utility.Utils;

/**
 * Created by shailesh on 11/11/17.
 */

public class BaseActivity extends AppCompatActivity{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    public String getPreferences(String sharedPrefKey){

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Utils.SHARED_PREF, Context.MODE_PRIVATE);
        String defaultValue = getApplicationContext().getResources().getString(R.string.default_value);
        String resultModule = sharedPref.getString( sharedPrefKey, defaultValue);
        return resultModule;
    }

    protected void savePreferences(String sharedPrefKey, String sharedPrefValue) {

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(Utils.SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(sharedPrefKey, sharedPrefValue);
        editor.commit();
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

}
