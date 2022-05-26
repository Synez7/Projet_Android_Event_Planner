package rpr.events;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;



// Service FirebaseMessaging pour l'envoi de Notifications Push
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgService";

    UserSessionManager session;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        session = new UserSessionManager(getApplicationContext());

        if(session.isUserLoggedIn()){
            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
                Intent intent = new Intent(this, Navigation.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (remoteMessage.getData().get("event_id") != null && remoteMessage.getData().get("name") != null && remoteMessage.getData().get("time") != null && remoteMessage.getData().get("time_end") != null && remoteMessage.getData().get("venue") != null && remoteMessage.getData().get("details") != null && remoteMessage.getData().get("creator_id") != null && remoteMessage.getData().get("creator") != null && remoteMessage.getData().get("category_id") != null && remoteMessage.getData().get("category") != null){
                    intent = new Intent(this, EventDisplayUser.class);

                    intent.putExtra("event_id", Integer.parseInt(remoteMessage.getData().get("event_id")));
                    intent.putExtra("name", remoteMessage.getData().get("name"));
                    intent.putExtra("time", remoteMessage.getData().get("time"));
                    intent.putExtra("time_end", remoteMessage.getData().get("time_end"));
                    intent.putExtra("venue", remoteMessage.getData().get("venue"));
                    intent.putExtra("details", remoteMessage.getData().get("details"));
                   // intent.putExtra("usertype_id", Integer.parseInt(remoteMessage.getData().get("usertype_id")));
                    //intent.putExtra("usertype", remoteMessage.getData().get("usertype"));
                    intent.putExtra("creator_id", Integer.parseInt(remoteMessage.getData().get("creator_id")));
                    intent.putExtra("creator", remoteMessage.getData().get("creator"));
                    intent.putExtra("category_id", Integer.parseInt(remoteMessage.getData().get("category_id")));
                    intent.putExtra("category", remoteMessage.getData().get("category"));

                }
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.wave)
                        .setContentTitle(remoteMessage.getData().get("title"))
                        .setContentText(remoteMessage.getData().get("body"))
                        .setAutoCancel(true)
                        .setColor(ContextCompat.getColor(this,R.color.colorPrimary))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(Integer.parseInt(remoteMessage.getData().get("event_id")), notificationBuilder.build());


            }
        }



        if (remoteMessage.getNotification() != null) {
            Intent intent = new Intent(this, Navigation.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder;
            if (remoteMessage.getNotification().getTitle() != null){
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.wave)
                        .setContentTitle(remoteMessage.getNotification().getTitle())
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setColor(ContextCompat.getColor(this,R.color.colorPrimary))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
            }
            else{
                notificationBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.wave)
                        .setContentTitle("Event Planner")
                        .setContentText(remoteMessage.getNotification().getBody())
                        .setAutoCancel(true)
                        .setColor(ContextCompat.getColor(this,R.color.colorPrimary))
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
            }


            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }

    }
}