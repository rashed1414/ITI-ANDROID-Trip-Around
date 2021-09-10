package com.team6.triparound.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.team6.triparound.R;
import com.team6.triparound.ui.home.UpcomingTripsActivity;
import com.team6.triparound.ui.home.home.HomeFragment;
import com.team6.triparound.utils.TripModel;
import com.team6.triparound.utils.alarmManagerReciever.MyDialogActivity;

import static com.team6.triparound.utils.alarmManagerReciever.AlarmEventReciever.RECEIVED_TRIP_SEND_SERIAL;

public class DialognotificationService extends Service {
    private static final String CHANNEL_ID = "MyDialogService";
    private TripModel tm;
    private final IBinder localBinder = new MyBinder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Create Notification Here !
        // TODO Try to remove createNotificationChannel(); call and check the notification Builder

        createNotificationChannel();
        createNotification();

        tm = (TripModel) intent.getSerializableExtra(RECEIVED_TRIP_SEND_SERIAL);
        if (tm != null) {
            Log.i("MyService: ", "Object Received " + tm.getTripname());
        }

        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

//        Bundle b = intent.getBundleExtra(RECEIVED_TRIP);
//        tm = (TripModel) b.getSerializable(RECEIVED_TRIP_SEND_SERIAL);

        return localBinder;  // ref from inerclass MyBinder
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(
                    CHANNEL_ID,
                    "Trip Around",
                    NotificationManager.IMPORTANCE_HIGH);
            NotificationManager man = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);

            man.createNotificationChannel(nc);
        }

    }

    private void createNotification() {

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, UpcomingTripsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_date_range_24px)
                .setContentTitle("Trip Around")
                .setContentText("You have an upcoming trip")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

    }


    public class MyBinder extends Binder {
        public DialognotificationService getService() {  // ref ml service
            return DialognotificationService.this;
        }

    }

}
