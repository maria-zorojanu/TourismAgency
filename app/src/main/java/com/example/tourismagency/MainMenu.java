package com.example.tourismagency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.checkerframework.checker.nullness.qual.NonNull;

public class MainMenu extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_destinations);

        replaceFragment(new DestinationsIndex());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.navigation_destinations:
                        //startActivity(new Intent(getApplicationContext(), LearnFragment.class));
                        //overridePendingTransition(0,0);
                        replaceFragment(new DestinationsIndex());
                        return true;
                    case R.id.navigation_reservations:
                        //startActivity(new Intent(getApplicationContext(), ComunityFragment.class));
                        //overridePendingTransition(0,0);
                        replaceFragment(new ReservationsIndex());
                        return true;
                }

                return false;
            }
        });

    }

    public void replaceFragment(Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.i("change", "ok");
    }

}