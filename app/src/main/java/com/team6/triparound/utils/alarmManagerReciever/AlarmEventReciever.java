package com.team6.triparound.utils.alarmManagerReciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.team6.triparound.ui.home.addButtonActivity.AddBtnActivity;
import com.team6.triparound.utils.TripModel;

public class AlarmEventReciever extends BroadcastReceiver {
    public static final String RECEIVED_TRIP = "RECEIVED_TRIP";
    public static final String RECEIVED_TRIP_SEND_SERIAL = "RECEIVED_TRIP_SEND_SERIAL";

    private Context context;


    //recieve from other applications
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i("service ", "serviceAlarmHasFired");
        Bundle b = intent.getBundleExtra(AddBtnActivity.NEW_TRIP_OBJECT);
        TripModel tm = (TripModel) b.getSerializable(AddBtnActivity.NEW_TRIP_OBJ_SERIAL);

        if (tm != null) {
            displayAlert(tm);
            Log.i("OnReceive", "Trip name" + tm.getTripname());
        }

    }

    private void displayAlert(TripModel tm) {
        Intent i = new Intent(context, MyDialogActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(RECEIVED_TRIP_SEND_SERIAL, tm);

        i.putExtra(RECEIVED_TRIP, b);
        i.setClass(context, MyDialogActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
