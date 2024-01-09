package com.example.tourismagency;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tourismagency.FirebaseHelper.Destination;
import com.example.tourismagency.FirebaseHelper.Reservations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;

public class ReservationsIndex extends Fragment {
    ListView lvReservations;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    SharedPreferences sharedPreferences;
    View myView;
    String userId;
    MainMenu mainMenu;
    ArrayList<String> displayReservations;


    public ReservationsIndex() {
        // Required empty public constructor
    }
    public static ReservationsIndex newInstance(String param1, String param2) {
        ReservationsIndex fragment = new ReservationsIndex();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_reservations_index, container, false);
        return myView;
    }

    private void updateUI(ArrayList<String> reservations)
    {
        ArrayAdapter<String> reviewsArrayAdapter = new ArrayAdapter<String>(myView.getContext(), R.layout.reviews_listview_layout, reservations);
        lvReservations.setAdapter(reviewsArrayAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mainMenu = (MainMenu) getActivity();
        sharedPreferences =mainMenu.getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", "-1");
        lvReservations = view.findViewById(R.id.lvReservations);
        DatabaseReference databaseReference = firebaseDatabase.getReference("AdminUsers").child(userId);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                if(dataSnapshot.exists())
                {
                    Boolean admin = (Boolean) dataSnapshot.getValue();
                    DatabaseReference destinationsDataRef = firebaseDatabase.getReference("Reservations");
                    //all destinations with reservation
                    //ArrayList<String>
                    displayReservations = new ArrayList<String>();
                    destinationsDataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(Task<DataSnapshot> task) {
                            DataSnapshot data = task.getResult();
                            for(DataSnapshot ds: data.getChildren())
                            {
                                String key = ds.getKey();
                                DatabaseReference reservationsDataRef = firebaseDatabase.getReference("Reservations").child(key);
                                reservationsDataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(Task<DataSnapshot> task) {
                                        //All reservations for a certain destination
                                        DataSnapshot reservationData = task.getResult();
                                        for(DataSnapshot rds: reservationData.getChildren())
                                        {
                                            Reservations reservation = new Reservations();
                                            reservation = (Reservations) rds.getValue(Reservations.class);
                                            String fromDateFormat;
                                            String finalDateFormat;
                                            fromDateFormat = Integer.toString(reservation.getStartDate()).substring(6,8)+'/'+
                                                    Integer.toString(reservation.getStartDate()).substring(4,6)+'/'+
                                                    Integer.toString(reservation.getStartDate()).substring(2,4);
                                            finalDateFormat = Integer.toString(reservation.getFinalDate()).substring(6,8)+'/'+
                                                    Integer.toString(reservation.getFinalDate()).substring(4,6)+'/'+
                                                    Integer.toString(reservation.getFinalDate()).substring(0,4);
                                            String interval = fromDateFormat + " - " + finalDateFormat;

                                            DatabaseReference destRef = firebaseDatabase.getReference("Destinations");
                                            Reservations finalReservation = reservation;
                                            destRef.child(reservation.getDestinationId()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                                @Override
                                                public void onComplete(Task<DataSnapshot> task) {
                                                    DataSnapshot ds = task.getResult();
                                                    if(ds.exists()) {
                                                        Destination destination = new Destination();
                                                        destination = (Destination) ds.getValue(Destination.class);
                                                        String destinationName = destination.getTitle();
                                                        String result = interval + ": " + destinationName;
                                                        if(admin){
                                                            displayReservations.add(result);
                                                        }else if(userId.equals(finalReservation.getUserId())){
                                                            displayReservations.add(result);
                                                        }
                                                        Log.i("reservations", displayReservations.toString());
                                                        if(displayReservations.size()>0 && lvReservations!= null) {
                                                            updateUI(displayReservations);
                                                        }
                                                    }

                                                }
                                            });

                                        }
                                    }
                                });
                            }
                            //updateUI(displayReservations);
                            //ArrayAdapter<String> reservationsArrayAdapter = new ArrayAdapter<String>(mainMenu.getApplicationContext(), R.layout.reviews_listview_layout, displayReservations);
                            //lvReservations.setAdapter(reservationsArrayAdapter);
                        }
                    });

                }
            }
        });
        super.onViewCreated(view, savedInstanceState);

    }

}