import com.connectingTrain.LatLong;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class Station {
    private String stationId;
    private String stationName;
    private LatLong latLong;

    private Set<Track> tracks;
    private Map<Integer, Set<Train>> trains;

    public Station() {
    }

    public Station(String stationId, String stationName, LatLong latLong, Set<Track> tracks) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.latLong = latLong;
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
            trains.put(track.getTracksId(), set);
        }
    }

    public String getStationId() {
        return stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public LatLong getLatLong() {
        return latLong;
    }

    public Set<Track> getTracks() {
        return Collections.unmodifiableSet(tracks);
    }

    public double getDistance(@NotNull Station station) {
        return latLong.getDistance(station.getLatLong());
    }

    public boolean hasRoute(Track route) {
        return tracks.contains(route);
    }


}
