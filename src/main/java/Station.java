import com.connectingTrain.GeoCoordinate;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Station {
    private String stationCode;
    private String stationName;
    private GeoCoordinate geoCoordinate;

    private Set<Track> tracks;
    private Map<String, Set<Train>> trains;

    public Station() {
    }

    public Station(String stationId, String stationName, GeoCoordinate geoCoordinate, Set<Track> tracks) {
        this.stationCode = stationId;
        this.stationName = stationName;
        this.geoCoordinate = geoCoordinate;
        this.tracks = tracks;
        populateTrains();
    }

    private void populateTrains() {
        trains = new HashMap<>();

        for (Track track : tracks) {
            Set<Train> set = track.getTrains()
                    .stream()
                    .filter(train -> train.stopsAt(this))
                    .collect(Collectors.toSet());
            trains.put(track.getTrackCode(), set);
        }
    }

    public String getStationCode() {
        return stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public GeoCoordinate getGeoCoordinate() {
        return geoCoordinate;
    }

    public Set<Track> getTracks() {
        return Collections.unmodifiableSet(tracks);
    }

    public double getDistance(@NotNull Station station) {
        return geoCoordinate.getDistance(station.getGeoCoordinate());
    }

    public boolean hasTrack(Track route) {
        return tracks.contains(route);
    }

}
