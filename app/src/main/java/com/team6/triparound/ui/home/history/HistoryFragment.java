package com.team6.triparound.ui.home.history;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.team6.triparound.R;
import com.team6.triparound.utils.TripModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mTripsRef;
    private List<TripModel> tripDetails = new ArrayList<>();
    RecyclerView rev;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {



        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mTripsRef = FirebaseDatabase.getInstance().getReference().child("triparound")
                .child(currentUser.getUid()).child("historytrips");


        View root = inflater.inflate(R.layout.history_fragment, container, false);

        rev = root.findViewById(R.id.recycler);


        HistoryAdaptor adpater = new HistoryAdaptor(tripDetails, getActivity());

       LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
       linearLayoutManager.setReverseLayout(true);
       linearLayoutManager.setStackFromEnd(true);
       rev.setLayoutManager(linearLayoutManager);


        rev.setAdapter(adpater);

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                tripDetails.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    tripDetails.add(ds.getValue(TripModel.class));
                }
                adpater.notifyDataSetChanged();
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Database", "onCancelled", databaseError.toException());
                // ...
            }
        };
        mTripsRef.addValueEventListener(postListener);

        return root;
    }

    private void recyclerViewInit() {
    }


}
