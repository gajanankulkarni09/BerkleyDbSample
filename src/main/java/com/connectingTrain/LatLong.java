package com.connectingTrain;

public class LatLong {
    double latitude;
    double longitude;

    public LatLong(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance(LatLong latLong){
        return Math.sqrt( Math.pow((latLong.getLatitude() - getLatitude()),2)
                + Math.pow((latLong.getLongitude()-getLongitude()),2));
    }
}
