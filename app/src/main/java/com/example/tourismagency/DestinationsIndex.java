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

public class DestinationsIndex extends Fragment {
     Button bntNewDestination;
     View myView;
     FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
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
        bntNewDestination = (Button) myView.findViewById(R.id.btnNewDestination);
        bntNewDestination.setOnClickListener(this::newDestination);
        getAllDestinations();
        return myView;
    }

    private void newDestination(View view)
    {
        MainMenu mainMenu = (MainMenu) getActivity();
        SharedPreferences sharedPreferences =mainMenu.getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("last destination clicked", "").apply();
        Intent newDestinationIntent = new Intent(getContext(), DestinationDetails.class);
        startActivity(newDestinationIntent);
    }


    public  void getAllDestinations()
    {
        RecyclerView destinationsList = myView.findViewById(R.id.rvDestinations);
        List<Destination> destinations = new ArrayList<>();
        ArrayList<String> destinationNames = new ArrayList<>();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Destinations");

        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                DataSnapshot data = task.getResult();

                if(data.exists()) {
                    for (DataSnapshot ds : data.getChildren()) {
                        Destination destination = new Destination();
                        destination = ds.getValue(Destination.class);
                        destinations.add(destination);
                        destinationNames.add(destination.getTitle());
                    }

                    // De bagat toate alea in array adapter
                    DestinationAdapter adapter = new DestinationAdapter(destinations);
                    destinationsList.setAdapter(adapter);
                }
            }
        });
        destinationsList.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        //De gasit destinatia pe care s-a dat click
        MainMenu mainMenu = (MainMenu) getActivity();
        /*SharedPreferences sharedPreferences =mainMenu.getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        int destinationIndex = sharedPreferences.getInt("last destination clicked", -1);
        Log.i("destinationIndex", Integer.toString(destinationIndex));
        Destination currentDestination = destinations.get(destinationIndex);
        Log.i("current destionation", currentDestination.getId());
        Log.i("current destionation", currentDestination.getTitle());*/

    }
}