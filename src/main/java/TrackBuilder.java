import java.util.ArrayList;
import java.util.List;

public class TrackBuilder {
    private int trackId;
    private Station sourceStation;
    private Station destinationStation;
    private List<Train> trains;
    private List<ConnectedTracks> connectedTracksList;

    public TrackBuilder() {
        trains = new ArrayList<>();
        connectedTracksList = new ArrayList<>();
    }

    public void setTrackId(int trackId){
        this.trackId = trackId;
    }

    public void setSourceStation(Station sourceStation){
        this.sourceStation = sourceStation;
    }

    public void setDestinationStation(Station destinationStation){
        this.destinationStation = destinationStation;
    }

    public void addConnectedRoute(ConnectedTracks connectedRoutes){
        this.connectedTracksList.add(connectedRoutes);
    }

    public void addTrain(Train train){
        this.trains.add(train);
    }

    public Track build(){
        return new Track(trackId,sourceStation,destinationStation, connectedTracksList,trains);
    }
}
