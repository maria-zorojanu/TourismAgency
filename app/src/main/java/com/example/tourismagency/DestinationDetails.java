package com.example.tourismagency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.tourismagency.FirebaseHelper.Destination;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.Inet4Address;

public class DestinationDetails extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Button btnSaveDestination;
    Button btnDeleteDestination;
    Button btnRating;
    Button btnNewReservation;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_details);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", "-1");
        Log.i("userId", userId);
        String destinationId = sharedPreferences.getString("last destination clicked", "");
        if(destinationId.length()>0)
        {
            updateDetails(destinationId);
        }
        DatabaseReference databaseReference = firebaseDatabase.getReference("AdminUsers").child(userId);
        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                DataSnapshot dataSnapshot = task.getResult();
                if(dataSnapshot.exists())
                {
                    Boolean admin = (Boolean) dataSnapshot.getValue();
                    Log.i("admin", admin.toString());
                    //Check if the user can save or modify a destination
                    btnSaveDestination = findViewById(R.id.btnSaveDestination);
                    btnDeleteDestination = findViewById(R.id.btnDeleteDestination);
                    btnRating = findViewById(R.id.btnDeleteDestination);
                    btnNewReservation = findViewById(R.id.btnDeleteDestination);
                    if(admin)
                    {
                        btnSaveDestination.setOnClickListener(this::saveDestination);
                        btnDeleteDestination.setOnClickListener(this::deleteDestination);
                        btnNewReservation.setVisibility(View.INVISIBLE);
                        btnRating.setVisibility(View.INVISIBLE);

                    }else{
                        btnNewReservation.setOnClickListener(this::Reservation);
                        btnRating.setOnClickListener(this::Rating);

                        btnSaveDestination.setVisibility(View.INVISIBLE);
                        btnDeleteDestination.setVisibility(View.INVISIBLE);

                        EditText titleEditText = findViewById(R.id.etTitle);
                        TextInputLayout detailsInput = findViewById(R.id.etDetails);
                        EditText priceEditText = findViewById(R.id.etPrice);
                        //set editTexts disabled
                        titleEditText.setEnabled(false);
                        detailsInput.getEditText().setEnabled(false);
                        priceEditText.setEnabled(false);
                    }
                }
            }

            private void saveDestination(View view) {
                //get elements from user input
                EditText titleEditText = findViewById(R.id.etTitle);
                TextInputLayout detailsInput = findViewById(R.id.etDetails);
                EditText priceEditText = findViewById(R.id.etPrice);

                String title = titleEditText.getText().toString();
                String details = detailsInput.getEditText().getText().toString();
                String priceStr = priceEditText.getText().toString();
                Float price = Float.valueOf(priceStr);
                //Save destination to DB
                //To do -> check if title is not empty + price > 0

                //Check if the destination already exists
                String id = sharedPreferences.getString("last destination clicked", "");
                Log.i("destination Id",id);
                if(id.length()==0) {
                    //Generate a new record
                    id = firebaseDatabase.getReference("Destinations").push().getKey();
                }
                Destination destination = new Destination(id,title, details, price, userId);
                DatabaseReference databaseReference = firebaseDatabase.getReference("Destinations");
                databaseReference.child(id).setValue(destination);

                //No destination was selected for edit/delete
                sharedPreferences.edit().putString("last destination clicked", "").apply();
                //Go back to index
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);

            }

            private void deleteDestination(View view)
            {
                //id of the destination to delete
                String destinationId = sharedPreferences.getString("last destination clicked", "");
                Log.i("destination Id",destinationId);

                //Delete destination from firebase database
                if(destinationId.length()>0) {
                    DatabaseReference deleteRef = firebaseDatabase.getReference("Destinations");
                    deleteRef.child(destinationId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("Firebase", "Destination deleted successfully");
                            } else {
                                Log.i("Firebase", "Error deleting destination: " + task.getException().getMessage());
                                task.getException().printStackTrace();
                            }
                        }
                    });
                }

                sharedPreferences.edit().putString("last destination clicked", "").apply();

                //Go back to index
                Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                startActivity(mainMenuIntent);
            }

            private void Reservation(View view)
            {

            }
            private void Rating(View view)
            {

            }

        });
    }

    private void updateDetails(String destinationId) {
        DatabaseReference dataRef = firebaseDatabase.getReference("Destinations").child(destinationId);
        dataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                DataSnapshot ds = task.getResult();
                if(ds.exists())
                {
                    Destination currentDestination = new Destination();
                    currentDestination = ds.getValue(Destination.class);

                    //Get UI elements
                    EditText titleEditText = findViewById(R.id.etTitle);
                    TextInputLayout detailsInput = findViewById(R.id.etDetails);
                    EditText priceEditText = findViewById(R.id.etPrice);

                    //Update UI elements
                    titleEditText.setText(currentDestination.getTitle());
                    detailsInput.getEditText().setText(currentDestination.getDescription());
                    priceEditText.setText(currentDestination.getPrice().toString());
                }
            }
        });
    }

}