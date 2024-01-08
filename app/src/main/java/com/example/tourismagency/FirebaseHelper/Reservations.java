package com.example.tourismagency.FirebaseHelper;

public class Reservations {
    String userId;
    String destinationId;
    int  finalDate;
    int  startDate;

    public Reservations() {
    }

    public String getUserId() {

        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public int getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(int finalDate) {
        this.finalDate = finalDate;
    }

    public int getStartDate() {
        return startDate;
    }

    public void setStartDate(int startDate) {
        this.startDate = startDate;
    }

    public Reservations(String userId, String destinationId, int startDate, int finalDate) {
        this.userId = userId;
        this.destinationId = destinationId;
        this.finalDate = finalDate;
        this.startDate = startDate;
    }
}
