package com.example.tourismagency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tourismagency.FirebaseHelper.Reviews;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReviewsIndex extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    SharedPreferences sharedPreferences;
    String destinationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews_index);

        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        destinationId = sharedPreferences.getString("last destination clicked", "");

        generateList();
    }

    public void generateList()
    {
        Log.i("destinationId", destinationId);
        DatabaseReference dataRef = firebaseDatabase.getReference("Reviews").child(destinationId);
        dataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(Task<DataSnapshot> task) {
                DataSnapshot data = task.getResult();
                ArrayList<String> dispalyReviews = new ArrayList<>();
                Float totalStars = 0.0f;
                float stars;
                if(data.exists())
                {

                    for(DataSnapshot ds: data.getChildren())
                    {
                        Reviews r = new Reviews();
                        r = (Reviews) ds.getValue(Reviews.class);
                        String displayReview = Integer.toString(r.getStarts()) + "* -> " + r.getDetails();
                        dispalyReviews.add(displayReview);
                        totalStars += r.getStarts();
                    }
                    if(dispalyReviews.size()>0)
                    {
                        stars = totalStars/dispalyReviews.size();
                    }else{
                        stars = 0;
                        dispalyReviews.add("No reviews yet");
                    }
                    updateUI(dispalyReviews, stars);
                }else{
                    stars = 0;
                    dispalyReviews.add("No reviews yet");
                    updateUI(dispalyReviews, stars);
                }

            }
        });
    }

    private void updateUI(ArrayList<String> reviews, Float stars)
    {
        TextView tvStars = findViewById(R.id.tvAvgRating);
        ListView lvReviews = findViewById(R.id.lvReviews);

        if(stars > 0) {
            tvStars.setText("Avg rating: " + stars.toString());
        }else{
            tvStars.setText("Avg rating: -");
        }
        ArrayAdapter<String> reviewsArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.reviews_listview_layout, reviews);
        lvReviews.setAdapter(reviewsArrayAdapter);
    }
}