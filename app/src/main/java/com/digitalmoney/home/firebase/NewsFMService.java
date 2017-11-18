package com.digitalmoney.home.firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.digitalmoney.home.R;
import com.digitalmoney.home.ui.WebViewActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by shailesh on 13/11/17.
 */

public class NewsFMService extends FirebaseMessagingService {

    private String TAG = NewsFMService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        onMessageReceivedSendToDetailedActivity(remoteMessage);
    }

    public void onMessageReceivedSendToDetailedActivity(RemoteMessage remoteMessage){
        String title=remoteMessage.getNotification().getTitle();
        String message=remoteMessage.getNotification().getBody();

        Intent intent=new Intent(getApplicationContext(), WebViewActivity.class);
        if (remoteMessage.getData().size() > 0) {
            String getUid = remoteMessage.getData().get("uid");
            if (!getUid.equalsIgnoreCase("")){
                intent.putExtra("uid", getUid);
            }
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationCompat.Builder notificationBuilder=new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.ic_wallet);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }


}
