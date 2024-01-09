package com.example.tourismagency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FilterActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
    }

    public void search(View view) {
        EditText searchEt = findViewById(R.id.etSearch);
        EditText minPriceEt = findViewById(R.id.etMinPrice);
        EditText maxPriceEt = findViewById(R.id.etMaxPrice);
        String searchStr = searchEt.getText().toString();
        String minPriceStr = minPriceEt.getText().toString();
        String maxPriceStr = maxPriceEt.getText().toString();

        sharedPreferences.edit().putString("search", searchStr).apply();
        sharedPreferences.edit().putString("minPrice", minPriceStr).apply();
        sharedPreferences.edit().putString("maxPrice", maxPriceStr).apply();

        Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
        startActivity(mainMenuIntent);
    }
}