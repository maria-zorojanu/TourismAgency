package com.example.tourismagency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class DestinationDetails extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    Button btnSaveDestination;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_destination_details);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", "-1");
        Log.i("userId", userId);
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
                    if(admin)
                    {
                        btnSaveDestination.setOnClickListener(this::saveDestination);
                    }else{
                        btnSaveDestination.setVisibility(View.INVISIBLE);
                    }
                }
            }

            private void saveDestination(View view) {
                Log.i("save destination", "start");
                //get elements from user innput
                EditText titleEditText = findViewById(R.id.etTitle);
                TextInputLayout detailsInput = findViewById(R.id.etDetails);
                EditText priceEditText = findViewById(R.id.etPrice);

                String title = titleEditText.getText().toString();
                String details = detailsInput.getEditText().getText().toString();
                String priceStr = priceEditText.getText().toString();
                Float price = Float.valueOf(priceStr);
                Log.i("save destination", "2");
                //Save destination to DB
                //To do -> check if title is not empty + price > 0
                String id = firebaseDatabase.getReference("Destinations").push().getKey();
                Log.i("Destination id", id);
                Destination destination = new Destination(id,title, details, price, userId);
                DatabaseReference databaseReference = firebaseDatabase.getReference("Destinations");
                databaseReference.child(id).setValue(destination);
                Log.i("save destination", "3");
            }
        });
    }

}