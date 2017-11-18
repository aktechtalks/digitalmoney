package com.digitalmoney.home.firebase;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import static com.digitalmoney.home.Utility.Utils.FIREBASE_TOKEN;

/**
 * Created by shailesh on 13/11/17.
 */

public class NewsFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private String TAG = NewsFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        if (refreshedToken.length()!=0 && !refreshedToken.equalsIgnoreCase("")){
            SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(FIREBASE_TOKEN, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("NOTIFICATION_API", refreshedToken);
            editor.commit();

        }
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {


    }
}
