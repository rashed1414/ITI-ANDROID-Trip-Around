package com.team6.triparound.ui.home.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.team6.triparound.R;
import com.team6.triparound.data.remote.network.FirebaseDB;
import com.team6.triparound.utils.TripModel;

import java.util.ArrayList;
import java.util.List;

public class HomeAdaptor extends RecyclerView.Adapter<HomeAdaptor.ViewHolder> {

    List<TripModel> list = new ArrayList<>();
    List<TripModel> canceledlist = new ArrayList<>();
    Context cntxt;
    FirebaseDB mFirebaseDB;
    AlertDialog alertDialog;


    public HomeAdaptor(List<TripModel> list, Context cntxt) {
        this.list = list;
        this.cntxt = cntxt;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(parent.getContext());
        View v = inflate.inflate(R.layout.custom_card_row, parent, false);
        mFirebaseDB = FirebaseDB.getInstance();
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.start.setText(list.get(position).startloc);
        holder.name.setText(list.get(position).tripname);
        holder.date.setText(list.get(position).date);
        holder.trip_Type.setText(list.get(position).tripType);
        holder.trip_Reptition.setText(list.get(position).tripRepition);
        holder.time.setText(list.get(position).time);
        holder.end.setText(list.get(position).endloc);
        holder.startnowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(position).setStatus("Done!");
                mFirebaseDB.addTripToHistory(list.get(position));
                mFirebaseDB.removeFromUpcoming(list.get(position));

                Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?saddr="
                        + list.get(position).getStartloc()+"&daddr="+list.get(position).getEndloc());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                cntxt.startActivity(mapIntent);
            }
        });
        holder.popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(cntxt, holder.popup);
                popupMenu.inflate(R.menu.menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //handle item selection from the card pop menu
                        if (item.getItemId() == R.id.editnote) {
                            //Toast.makeText(cntxt, "View note", Toast.LENGTH_LONG).show();


                            PopupMenu pop = new PopupMenu(cntxt, v);
                            pop.inflate(R.menu.notes_menu);
                            for (String n : list.get(position).getNotes()) {

                                pop.getMenu().add(n);
                                Log.i("test",n);

                            }
                            pop.show();

                        }
                        if (item.getItemId() == R.id.cancel) {

                            androidx.appcompat.app.AlertDialog.Builder Builder = new androidx.appcompat.app.AlertDialog.Builder(cntxt)
                                    .setMessage("Are you Sure you want to Cancel?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(cntxt, "Canceled Trip!", Toast.LENGTH_LONG).show();
                                            list.get(position).setStatus("Canceled!");
                                            mFirebaseDB.addTripToHistory(list.get(position));
                                            mFirebaseDB.removeFromUpcoming(list.get(position));
                                            notifyDataSetChanged();





                                        }
                                    }).setNeutralButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            notifyDataSetChanged();
                                            alertDialog.dismiss();


                                        }
                                    });
                            alertDialog = Builder.create();
                            alertDialog.show();

                        }
                        return false;
                    }
                });

                popupMenu.show();

            }
        });





    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView start, end, date, time, name,trip_Type,trip_Reptition;
        Button popup, startnowBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            startnowBtn = itemView.findViewById(R.id.startnow);
            start = itemView.findViewById(R.id.start_loc_id);
            end = itemView.findViewById(R.id.end_loc_id);
            time = itemView.findViewById(R.id.Time_id);
            name = itemView.findViewById(R.id.trip_name_id);
            date = itemView.findViewById(R.id.Date_id);
            popup = itemView.findViewById(R.id.pop_menu_id);
            trip_Type=itemView.findViewById(R.id.Trip_type);
            trip_Reptition=itemView.findViewById(R.id.Trip_repition);
        }
    }
}
