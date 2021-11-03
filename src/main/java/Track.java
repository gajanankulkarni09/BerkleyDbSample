import java.time.LocalDateTime;
import java.util.*;

public class Track {
    private int tracksId;
    private Station sourceStation;
    private Station destinationStation;
    private List<ConnectedTracks> connectedTracksList;
    private Map<Integer, Station> tracksIntersectionMap;

    private List<Train> trains;

    public Track() {
    }

    Track(int trackId, Station sourceStation, Station destinationStation, List<ConnectedTracks> connectedTracksList, List<Train> trains) {
        this.tracksId = trackId;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.connectedTracksList = connectedTracksList;
        this.trains = trains;
        tracksIntersectionMap = new HashMap<>();
        for (ConnectedTracks connectedTrack : connectedTracksList) {
            tracksIntersectionMap.put(connectedTrack.destinationTrack.getTracksId(), connectedTrack.getIntersectingStation());
        }
    }

    public List<Train> getTrains() {
        return trains;
    }

    public int getTracksId() {
        return tracksId;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public Iterator<ConnectedTracks> getConnectedTracksList() {
        return connectedTracksList.iterator();
    }

    public List<Train> getTrainBetween(Station sourceStation, Station destinationStation, LocalDateTime dateTime) {
        return trains.stream()
                .filter(train -> train.runsOnDay(dateTime.getDayOfWeek(), sourceStation))
                .filter(train -> train.stopsAt(sourceStation))
                .filter(train -> train.stopsAt(destinationStation))
                .sorted((t1, t2) -> {
                    LocalDateTime t1Time = t1.getStopArrivalDateTime(destinationStation, dateTime);
                    LocalDateTime t2Time = t2.getStopArrivalDateTime(destinationStation, dateTime);
                    return t1Time.compareTo(t2Time);
                }).toList();
    }

    public Station getIntesectingStation(Track nextTrack) {
        return tracksIntersectionMap.getOrDefault(nextTrack.getTracksId(), null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track route = (Track) o;
        return tracksId == route.tracksId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tracksId);
    }
}



