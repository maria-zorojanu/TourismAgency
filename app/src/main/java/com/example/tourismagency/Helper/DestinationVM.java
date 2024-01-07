package com.example.tourismagency.Helper;

import com.example.tourismagency.FirebaseHelper.Destination;

import java.util.ArrayList;

public class DestinationVM {
    private String Name;
    private Float Price;
    private String Id;

    public DestinationVM() {
    }

    public DestinationVM(String name, Float price, String id) {
        Name = name;
        Price = price;
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Float getPrice() {
        return Price;
    }

    public void setPrice(Float price) {
        Price = price;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public static ArrayList<DestinationVM> createDestinationList(ArrayList<Destination> destinations)
    {
        ArrayList<DestinationVM> destinationAdapter = new ArrayList<DestinationVM>();
        for(int index = 0; index<destinations.size(); index++)
        {
            DestinationVM da = new DestinationVM(destinations.get(index).getTitle(),destinations.get(index).getPrice(),destinations.get(index).getId());
            destinationAdapter.add(da);
        }
        return destinationAdapter;
    }
}
