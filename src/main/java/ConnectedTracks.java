public class ConnectedTracks {
    Track sourceTrack;
    Track destinationTrack;
    Station intersectingStation;

    public ConnectedTracks() {
    }

    public ConnectedTracks(Track sourceRoute, Track destinationRoute, Station intersectingStation) {
        this.sourceTrack = sourceRoute;
        this.destinationTrack = destinationRoute;
        this.intersectingStation = intersectingStation;
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
}
