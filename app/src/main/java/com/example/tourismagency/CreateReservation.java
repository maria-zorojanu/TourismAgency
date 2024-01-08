package com.example.tourismagency;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourismagency.FirebaseHelper.Reservations;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateReservation extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reservation);

        sharedPreferences =getApplicationContext().getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
    }

    public void saveReservation(View view) {
        EditText etFromDate = findViewById(R.id.etFromDate);
        EditText etFinalDate = findViewById(R.id.etFinalDate);

        Log.i("From date",etFromDate.getText().toString());
        Log.i("Final date",etFinalDate.getText().toString());

        int fromDate = checkDateFormat(etFromDate.getText().toString());
        int finalDate = checkDateFormat(etFinalDate.getText().toString());
        if(fromDate == 0 || finalDate == 0 || fromDate>finalDate){
            Toast.makeText(getApplicationContext(),"Please enter valid dates", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(),"Reservation saved", Toast.LENGTH_SHORT).show();
        }

        String userId = sharedPreferences.getString("uid", "");
        String destinationId = sharedPreferences.getString("last destination clicked", "");

        Reservations reservation = new Reservations(userId, destinationId, fromDate, finalDate);
        DatabaseReference dataRef = firebaseDatabase.getReference("Reservations").child(destinationId);

        String key = firebaseDatabase.getReference("Reservations").push().getKey();
        dataRef.child(key).setValue(reservation);


    }

    //Return yyyymmdd if a date is valid or 0
    private int checkDateFormat(String date)
    {
        if(date.length()!= 10)
        {
            return 0;
        }
        String ddStr, mmStr, yyyyStr;
        for(int chIndex = 0; chIndex < date.length(); chIndex++)
        {
            if((date.charAt(chIndex) < '0' || date.charAt(chIndex)> '9') && date.charAt(chIndex) != '/' && date.charAt(chIndex) != '.')
            {
                return 0;
            }
        }
        ddStr = date.substring(0,2);
        mmStr = date.substring(3,5);
        yyyyStr = date.substring(6,10);

        int dd, mm, yyyy;
        dd = Integer.valueOf(ddStr);
        mm = Integer.valueOf(mmStr);
        yyyy = Integer.valueOf(yyyyStr);
        if(mm<1 || mm>12){
            return 0;
        }
        if(mm == 2 && yyyy%4 >0 && dd>28){
            return 0;
        }
        if(mm==2 && yyyy%4 == 0 && dd>29){
            return 0;
        }
        if((mm == 4  || mm == 6 || mm == 9 || mm == 11) && dd>30)
        {
            return 0;
        }
        if(dd>31){
            return 0;
        }
        int formatDate = dd + 100 * mm + 10000 * yyyy; //ex: 20240512
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        int currentDate = Integer.valueOf(timeStamp);
        if(formatDate<currentDate){
            return 0;
        }
        return formatDate;
    }


}