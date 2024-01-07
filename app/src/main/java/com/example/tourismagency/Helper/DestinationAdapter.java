package com.example.tourismagency.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tourismagency.FirebaseHelper.Destination;
import com.example.tourismagency.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {
    private List<Destination> destinationList;
    Context context;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.destination_adapter, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        TextView tvTitle = holder.tvTitle;
        tvTitle.setText(destination.getTitle());
        TextView tvPrice = holder.tvPrice;
        tvPrice.setText(destination.getPrice().toString() + "$");
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements com.example.tourismagency.Helper.ViewHolder, View.OnClickListener {
        public TextView tvTitle;
        public TextView tvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Log.i("click position", Integer.toString(position));

                DatabaseReference dataRef = FirebaseDatabase.getInstance().getReference("Destinations");
                dataRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(Task<DataSnapshot> task) {
                        DataSnapshot data = task.getResult();
                        Destination destination = new Destination();
                        int index = 0;
                        for (DataSnapshot ds : data.getChildren()) {
                            if(index == position) {
                                destination = ds.getValue(Destination.class);
                            }
                            index++;
                        }
                        Log.i("current destionation", destination.getId());
                        Log.i("current destionation", destination.getTitle());
                        //Use shared preferences to store the item last clicked
                        SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
                        sharedPreferences.edit().putString("last destination clicked", destination.getId()).apply();
                    }
                });
            }else{
                SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.tourismagency", Context.MODE_PRIVATE);
                sharedPreferences.edit().putInt("last destination clicked", -1).apply();;
            }
        }
    }

    public DestinationAdapter(List<Destination> destinationList) {
        this.destinationList = destinationList;
    }



}
