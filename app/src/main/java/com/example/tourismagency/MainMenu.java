package com.example.tourismagency;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.navigation_destinations);
        sharedPreferences = getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("last destination clicked", "").apply(); //No destination was selected
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

    public void deleteUser(View view)
    {
        userID = sharedPreferences.getString("uid", "");
        Log.i("userID", userID);
        new AlertDialog.Builder(this)
                .setTitle("Delete user")
                .setMessage("Are you sure you want to delete you account?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Log.i("delete", "test");
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //delete user
                        user.delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("User account deleted.", "userName");
                                            // delete data for automatic logIn
                                            sharedPreferences.edit().putString("email", "").apply();
                                            sharedPreferences.edit().putString("password", "").apply();
                                        }
                                    }
                                });

                        Intent MainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(MainActivityIntent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void logOut(View view) {
        Log.i("log out clicked", "ok");
        sharedPreferences.edit().putString("email", "").apply();
        sharedPreferences.edit().putString("password", "").apply();
        sharedPreferences.edit().putString("userName", "").apply();
        sharedPreferences.edit().putString("uid", "").apply();

        Intent logInIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(logInIntent);
    }

}