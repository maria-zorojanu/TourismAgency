package com.example.tourismagency;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourismagency.FirebaseHelper.Reservations;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
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
        Boolean ok = true;
        if(fromDate == 0 || finalDate == 0 || fromDate>finalDate){
            ok = false;
            Toast.makeText(getApplicationContext(),"Please enter valid dates", Toast.LENGTH_SHORT).show();
        }

        if(ok) {
            String userId = sharedPreferences.getString("uid", "");
            String destinationId = sharedPreferences.getString("last destination clicked", "");

            DatabaseReference reservationRef = firebaseDatabase.getReference("Reservations").child(destinationId);
            reservationRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @TargetApi(Build.VERSION_CODES.O)
                @Override
                public void onComplete(Task<DataSnapshot> task) {
                    Log.i("reservations onComplete", "ok");
                    DataSnapshot data = task.getResult();
                    Boolean ok = true;
                    if (data.exists()) {
                        Log.i("data exists", "ok");
                        for (DataSnapshot ds : data.getChildren()) {
                            Reservations reservation = new Reservations();
                            reservation = (Reservations) ds.getValue(Reservations.class);

                            if ((fromDate >= reservation.getStartDate() && fromDate <= reservation.getFinalDate()) ||
                                    finalDate >= reservation.getStartDate() && finalDate <= reservation.getFinalDate()
                                    || (fromDate <= reservation.getStartDate() && finalDate >= reservation.getFinalDate())) {
                                Toast.makeText(getApplicationContext(), "The interval is not available", Toast.LENGTH_SHORT).show();
                                ok = false;
                            }


                        }
                    }
                    Log.i("reservations status", ok.toString());
                    if (ok) {
                        Reservations reservation = new Reservations(userId, destinationId, fromDate, finalDate);
                        DatabaseReference dataRef = firebaseDatabase.getReference("Reservations").child(destinationId);

                        String key = firebaseDatabase.getReference("Reservations").push().getKey();
                        dataRef.child(key).setValue(reservation);
                        Log.i("reservations id", key);
                        Toast.makeText(getApplicationContext(), "Reservation saved", Toast.LENGTH_SHORT).show();

                        //Go back to index
                        sharedPreferences.edit().putString("last destination clicked", "").apply();
                        Intent mainMenuIntent = new Intent(getApplicationContext(), MainMenu.class);
                        startActivity(mainMenuIntent);

                        //Notification
                        String messagesChannelId = "Reservations";
                        NotificationChannel messagesChannel = new NotificationChannel(messagesChannelId, "reservation notifications", NotificationManager.IMPORTANCE_HIGH);

                        NotificationManager notificationManager = getSystemService(NotificationManager.class);
                        notificationManager.createNotificationChannel(messagesChannel);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), messagesChannelId)
                                .setSmallIcon(R.drawable.ic_launcher_foreground)
                                .setContentTitle("New reservation")
                                .setContentText("You have a new reservation")
                                .setPriority(NotificationCompat.PRIORITY_HIGH);
                        // Show the notification
                        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        notificationManager.notify(1, builder.build());
                    }
                }
            });
        }
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