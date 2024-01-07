package com.example.tourismagency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tourismagency.FirebaseHelper.Reviews;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Review extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String colorYellow = "#A2FE00";
    String colorGray = "#D2D2D2";
    Integer star = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Log.i("Review", "start");
    }

    public void saveReview(View view) {
        sharedPreferences = sharedPreferences = getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("uid", "");
        String destinationId = sharedPreferences.getString("last destination clicked", "");
        Log.i("uid", userId);
        Log.i("destinationId", destinationId);
        TextInputLayout etReview = findViewById(R.id.etReview);
        String details = etReview.getEditText().getText().toString();
        Reviews review = new Reviews(destinationId, userId, details, star);

        String id = firebaseDatabase.getReference("Reviews").child(destinationId).push().getKey();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Reviews");
        databaseReference.child(destinationId).child(id).setValue(review);

        //Back to index Screen
        sharedPreferences.edit().putString("last destination clicked", "").apply();
        Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(mainMenuIntent);
    }

    public void stars(View view) {
        star = Integer.valueOf(view.getTag().toString());
        Button star1 = findViewById(R.id.star1);
        Button star2 = findViewById(R.id.star2);
        Button star3 = findViewById(R.id.star3);
        Button star4 = findViewById(R.id.star4);
        Button star5 = findViewById(R.id.star5);
        Log.i("stars", star.toString());
        switch (star)
        {
            case 1:
                star1.setBackgroundColor(Color.parseColor(colorYellow));
                star2.setBackgroundColor(Color.parseColor(colorGray));
                star3.setBackgroundColor(Color.parseColor(colorGray));
                star4.setBackgroundColor(Color.parseColor(colorGray));
                star5.setBackgroundColor(Color.parseColor(colorGray));
                break;
            case 2:
                star1.setBackgroundColor(Color.parseColor(colorYellow));
                star2.setBackgroundColor(Color.parseColor(colorYellow));
                star3.setBackgroundColor(Color.parseColor(colorGray));
                star4.setBackgroundColor(Color.parseColor(colorGray));
                star5.setBackgroundColor(Color.parseColor(colorGray));
                break;
            case 3:
                star1.setBackgroundColor(Color.parseColor(colorYellow));
                star2.setBackgroundColor(Color.parseColor(colorYellow));
                star3.setBackgroundColor(Color.parseColor(colorYellow));
                star4.setBackgroundColor(Color.parseColor(colorGray));
                star5.setBackgroundColor(Color.parseColor(colorGray));
                break;
            case 4:
                star1.setBackgroundColor(Color.parseColor(colorYellow));
                star2.setBackgroundColor(Color.parseColor(colorYellow));
                star3.setBackgroundColor(Color.parseColor(colorYellow));
                star4.setBackgroundColor(Color.parseColor(colorYellow));
                star5.setBackgroundColor(Color.parseColor(colorGray));
                break;
            case 5:
                star1.setBackgroundColor(Color.parseColor(colorYellow));
                star2.setBackgroundColor(Color.parseColor(colorYellow));
                star3.setBackgroundColor(Color.parseColor(colorYellow));
                star4.setBackgroundColor(Color.parseColor(colorYellow));
                star5.setBackgroundColor(Color.parseColor(colorYellow));
                break;
        }

    }
}