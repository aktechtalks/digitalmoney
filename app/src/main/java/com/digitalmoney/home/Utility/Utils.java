package com.digitalmoney.home.Utility;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public static final String TYPEFACE_PATH_MEDIUM = "fonts/Ubuntu-M.ttf" ;
    public static final String TYPEFACE_PATH_BOLD = "fonts/Ubuntu-B.ttf" ;
    public static final String TYPEFACE_PATH_LARGE = "fonts/Ubuntu-L.ttf" ;
    public static final String TYPEFACE_PATH_REGULAR= "fonts/Ubuntu-R.ttf" ;

    public static final String SHARED_PREF = "DIGITAL_MONEY";
    public static final String PREF_KEY_TOTAL_IMPRESSION = "TOTAL_IMPRESSION";
    public static final String PREF_KEY_SUCCESS_IMPRESSION = "SUCCESS_IMPRESSION";
    public static final String PREF_KEY_INSTALL = "INSTALL";
    public static final String PREF_KEY_SUCCESS_INSTALL = "SUCCESS_INSTALL";


    public static void setLocale(Context context, String locale) {
        SharedPreferences prefs = context.getSharedPreferences("NEWS_APP", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("locale", locale);
        editor.apply();
    }

    public static String getLocale(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("NEWS_APP", 0);
        return prefs.getString("locale", null);
    }

    public static final String FIREBASE_TOKEN = "FIREBASE_TOCKEN";

}