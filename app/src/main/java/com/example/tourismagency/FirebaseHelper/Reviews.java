package com.example.tourismagency.FirebaseHelper;

public class Reviews {
    String destinationId;
    String userId;
    String details;
    int Starts;

    public String getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getStarts() {
        return Starts;
    }

    public void setStarts(int starts) {
        Starts = starts;
    }

    public Reviews(String destinationId, String userId, String details, int starts) {
        this.destinationId = destinationId;
        this.userId = userId;
        this.details = details;
        Starts = starts;
    }

    public Reviews() {
    }
}
