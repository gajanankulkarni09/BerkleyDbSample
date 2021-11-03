package com.connectingTrain;

public class GeoCoordinate {
    double latitude;
    double longitude;

    public GeoCoordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance(GeoCoordinate geoCoordinate){
        double lon1 = Math.toRadians(this.longitude);
        double lon2 = Math.toRadians(geoCoordinate.longitude);
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(geoCoordinate.latitude);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }
}
