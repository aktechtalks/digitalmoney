package com.digitalmoney.home.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Spannable;
import android.text.SpannableString;

public class Utils {

    public static double SUCCESS_IMPRESSION_REVENUE = 0.40;

    /* Selected fonts for the views*/
    public static final String TYPEFACE_PATH_MEDIUM         = "fonts/Ubuntu-M.ttf" ;
    public static final String TYPEFACE_PATH_BOLD           = "fonts/Ubuntu-B.ttf" ;
    public static final String TYPEFACE_PATH_LARGE          = "fonts/Ubuntu-L.ttf" ;
    public static final String TYPEFACE_PATH_REGULAR        = "fonts/Ubuntu-R.ttf" ;


    /* Selected fonts for the views*/
    public static final String TYPEFACE_MEDIUM         = "Ubuntu-M.ttf" ;
    public static final String TYPEFACE_BOLD           = "Ubuntu-B.ttf" ;
    public static final String TYPEFACE_LARGE          = "Ubuntu-L.ttf" ;
    public static final String TYPEFACE_REGULAR        = "Ubuntu-R.ttf" ;


    /* Shared pref Keys*/
    public static final String SHARED_PREF                  = "DIGITAL_MONEY";
    public static final String PREF_KEY_TOTAL_IMPRESSION    = "TOTAL_IMPRESSION";
    public static final String PREF_KEY_SUCCESS_IMPRESSION  = "SUCCESS_IMPRESSION";
    public static final String PREF_KEY_INSTALL             = "INSTALL";
    public static final String PREF_KEY_SUCCESS_INSTALL     = "SUCCESS_INSTALL";
    public static final String FIREBASE_TOKEN               = "FIREBASE_TOCKEN";
    public static final String PREF_KEY_COIN_COUNT          = "COIN_COUNT";
    public static final String WALLET_MONEY                 = "WALLET_MONEY";

    public static final String TOTAL_IMPRESSION             = "15";



    /* Set Preferences */
    public static void setLocale(Context context, String pref_key, String pref_value) {
        SharedPreferences prefs = context.getSharedPreferences("DIGITAL_APP", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(pref_key, pref_value);
        editor.apply();
    }

    /* Get Preferences */
    public static String getLocale(Context context, String pref_key) {
        SharedPreferences prefs = context.getSharedPreferences("DIGITAL_APP", 0);
        return prefs.getString(pref_key, null);
    }



    public static SpannableString setSpannableString(Context mContext, String titleString, String typefaceName){
        SpannableString spannableText = new SpannableString(titleString);
        spannableText.setSpan(new TypefaceSpan(mContext, typefaceName), 0, spannableText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableText;
    }
}