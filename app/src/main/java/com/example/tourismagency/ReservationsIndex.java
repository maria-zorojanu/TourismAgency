package com.example.tourismagency;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class ReservationsIndex extends Fragment {
    Button addNewButton;

    public ReservationsIndex() {
        // Required empty public constructor
    }
    public static ReservationsIndex newInstance(String param1, String param2) {
        ReservationsIndex fragment = new ReservationsIndex();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reservations_index, container, false);
    }
}