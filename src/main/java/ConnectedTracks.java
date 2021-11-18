public class ConnectedTracks {
    private Track sourceTrack;
    private Track destinationTrack;
    private Station intersectingStation;
    private int stationNumber;

    public ConnectedTracks() {
    }

    public ConnectedTracks(Track sourceRoute, Track destinationRoute, Station intersectingStation,int stationNumber) {
        this.sourceTrack = sourceRoute;
        this.destinationTrack = destinationRoute;
        this.intersectingStation = intersectingStation;
        this.stationNumber = stationNumber;
    }

    public Track getSourceTrack() {
        return sourceTrack;
    }

    public Track getDestinationTrack() {
        return destinationTrack;
    }

    public Station getIntersectingStation() {
        return intersectingStation;
    }

    public int getStationNumber() {
        return stationNumber;
    }

    public boolean meetsConnectedTrack(ConnectedTracks connectedTracks) {
        return intersectingStation.equals(connectedTracks.intersectingStation);
    }
}
