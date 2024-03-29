package com.example.tourismagency;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.example.tourismagency.FirebaseHelper.Destination;
import com.example.tourismagency.Helper.DestinationAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DestinationsIndex extends Fragment {
     Button bntNewDestination;
     Button btnFilterDestination;
     View myView;
     FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    SharedPreferences sharedPreferences;
    public DestinationsIndex() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DestinationsIndex newInstance(String param1, String param2) {
        DestinationsIndex fragment = new DestinationsIndex();
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
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_destinations_index, container, false);
        MainMenu mainMenu = (MainMenu) getActivity();
        sharedPreferences =mainMenu.getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("uid", "-1");
        DatabaseReference databaseReference = firebaseDatabase.getReference("AdminUsers").child(userId);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                if(dataSnapshot.exists())
                {
                    Boolean admin = (Boolean) dataSnapshot.getValue();
                    bntNewDestination = (Button) myView.findViewById(R.id.btnNewDestination);
                    btnFilterDestination = (Button) myView.findViewById(R.id.btnFilterDestination);
                    if(admin){
                        bntNewDestination.setOnClickListener(this::newDestination);
                    }else{
                        bntNewDestination.setVisibility(View.INVISIBLE);
                    }
                    btnFilterDestination.setOnClickListener(this::filterDestination);
                }

            }

            private void filterDestination(View view) {
                Intent filterDestinationIntent = new Intent(getContext(), FilterActivity.class);
                startActivity(filterDestinationIntent);
            }

            private void newDestination(View view)
            {
                sharedPreferences.edit().putString("last destination clicked", "").apply();
                Intent newDestinationIntent = new Intent(getContext(), DestinationDetails.class);
                startActivity(newDestinationIntent);
            }
        });

        getAllDestinations();
        return myView;
    }

    public  void getAllDestinations()
    {
        RecyclerView destinationsList = myView.findViewById(R.id.rvDestinations);
        List<Destination> destinations = new ArrayList<>();
        ArrayList<String> destinationNames = new ArrayList<>();
        String minPriceStr = sharedPreferences.getString("minPrice", "-1");
        String maxPriceStr = sharedPreferences.getString("maxPrice", "-1");
        String search = sharedPreferences.getString("search", "");
        Float minPrice = -1.0f, maxPrice = -1.0f;
        if(minPriceStr.length()>0) {
            minPrice = Float.valueOf(minPriceStr);
        }
        if(maxPriceStr.length()>0) {
            maxPrice = Float.valueOf(maxPriceStr);
        }
        DatabaseReference databaseReference = firebaseDatabase.getReference("Destinations");

        Float finalMinPrice = minPrice;
        Float finalMaxPrice = maxPrice;
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                DataSnapshot data = task.getResult();

                if(data.exists()) {
                    for (DataSnapshot ds : data.getChildren()) {
                        Destination destination = new Destination();
                        destination = ds.getValue(Destination.class);
                        Boolean ok = true;
                        if(search.length()>0 && !(destination.getTitle().toUpperCase(Locale.ROOT).indexOf(search.toUpperCase(Locale.ROOT))>=0))
                        {
                            ok = false;
                        }
                        if(finalMinPrice > -1 && destination.getPrice()< finalMinPrice)
                        {
                            ok = false;
                        }
                        if(finalMaxPrice > -1 && destination.getPrice()> finalMaxPrice)
                        {
                            ok = false;
                        }
                        if(ok) {
                            destinations.add(destination);
                            destinationNames.add(destination.getTitle());
                        }
                    }

                    DestinationAdapter adapter = new DestinationAdapter(destinations);
                    destinationsList.setAdapter(adapter);
                }
            }
        });
        destinationsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }
}