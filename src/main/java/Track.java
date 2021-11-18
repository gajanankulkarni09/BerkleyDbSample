import java.time.LocalDateTime;
import java.util.*;

public class Track {
    private String trackCode;
    private Station sourceStation;
    private Station destinationStation;
    private List<ConnectedTracks> connectedTracksList;
    private Map<String, Station> tracksIntersectionMap;
    private Map<String, Integer> trackStationIndexes;

    private List<Train> trains;

    public Track() {

    }

    Track(String trackCode, Station sourceStation, Station destinationStation, List<ConnectedTracks> connectedTracksList, List<Train> trains) {
        this.trackCode = trackCode;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.connectedTracksList = connectedTracksList;
        this.trains = trains;
        tracksIntersectionMap = new HashMap<>();
        for (ConnectedTracks connectedTrack : connectedTracksList) {
            tracksIntersectionMap.put(connectedTrack.getDestinationTrack().getTrackCode(), connectedTrack.getIntersectingStation());
            trackStationIndexes.put(connectedTrack.getIntersectingStation().getStationCode(), connectedTrack.getStationNumber());
        }
    }

    public List<Train> getTrains() {
        return trains;
    }

    public String getTrackCode() {
        return trackCode;
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
        return tracksIntersectionMap.getOrDefault(nextTrack.getTrackCode(), null);
    }

    public int getStationNumberOnTrack(String stationCode) {
        return trackStationIndexes.get(stationCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track route = (Track) o;
        return trackCode == route.trackCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(trackCode);
    }

    public boolean isStation1BeforeStation2(Station station1, Station station2) {
        int station1Number = trackStationIndexes.get(station1.getStationCode());
        int station2Number = trackStationIndexes.get(station2.getStationCode());
        return station1Number <= station2Number;
    }

    public boolean isOppositeTrack(Track destinationTrack) {

    }

    public String getOppositeTrackCode(){
        String[] segments = trackCode.split("#");
        return segments[1] + "#" + segments[0] + "#" + segments[2];
    }
}



