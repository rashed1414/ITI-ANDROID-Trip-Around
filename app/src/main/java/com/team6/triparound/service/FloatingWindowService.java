package com.team6.triparound.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.team6.triparound.R;
import com.team6.triparound.ui.home.UpcomingTripsActivity;
import com.team6.triparound.ui.home.addButtonActivity.AddBtnActivity;
import com.team6.triparound.ui.home.home.HomeAdaptor;
import com.team6.triparound.utils.TripModel;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloatingWindowService extends Service {
    private final IBinder localBinder = new MyBinder();

    TripModel mTripModel;
    WindowManager wm;
    LinearLayout ll;
    private boolean listOn = false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {


        return localBinder;
    }


        @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);
        ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.TRANSPARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.
                LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(layoutParams);

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = 0;

        ImageView openapp = new ImageView(this);
        openapp.setImageResource(R.drawable.triplogofloating);
        ViewGroup.LayoutParams butnparams = new ViewGroup.LayoutParams(
                200, 200);
        openapp.setLayoutParams(butnparams);

        ll.addView(openapp);
        wm.addView(ll, params);

        openapp.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatepar = params;
            double x;
            double y;
            double px;
            double py;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatepar.x;
                        y = updatepar.y;

                        px = motionEvent.getRawX();
                        py = motionEvent.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE:

                        updatepar.x = (int) (x + (motionEvent.getRawX() - px));
                        updatepar.y = (int) (y + (motionEvent.getRawY() - py));

                        wm.updateViewLayout(ll, updatepar);

                    default:
                        break;
                }

                return false;

            }
        });

        openapp.setOnClickListener(view -> {
            ListView listview = new ListView(FloatingWindowService.this);
            ArrayList<String>notey= (ArrayList<String>) mTripModel.getNotes();
            ArrayList<String> arr = new ArrayList<>();
            arr.add("test");

            arr.toArray();


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(FloatingWindowService.this
                   , android.R.layout.simple_list_item_1, arr);
           listview.setAdapter(adapter);

            if (!listOn) {
                ll.addView(listview);
            } else {
                ll.removeView(listview);
            }

        });
        openapp.setOnLongClickListener(view -> {
            Intent i = new Intent(FloatingWindowService.this, UpcomingTripsActivity.class);
            i.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            wm.removeView(ll);
            return false;
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stopSelf();
        wm.removeView(ll);
    }

    public class MyBinder extends Binder {
        public FloatingWindowService getService() {
            return FloatingWindowService.this;
        }

    }

    public void setTripModel(TripModel tm) {
        this.mTripModel = tm;
    }

}

