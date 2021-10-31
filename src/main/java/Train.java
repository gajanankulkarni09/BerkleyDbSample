import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Train {
    private String trainId;
    private String trainName;
    private List<Stop> stops;
    private Map<String, Stop> stopsMap;
    private boolean[] runningDays;

    public Train() {
    }

    public Train(String trainId, String trainName, List<Stop> stops, boolean[] runningDays) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.stops = stops;
        this.stopsMap = stops.stream().collect(Collectors.toMap(Stop::getStopName, Function.identity()));
        this.runningDays = runningDays;
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public boolean stopsAt(Station station) {
        return stopsMap.containsKey(station.getStationName());
    }

    public LocalDateTime getStopArrivalDateTime(Station station, LocalDate startDate) {
        if (stopsMap.containsKey(station.getStationName())) {
            return stopsMap.get(station.getStationName()).getArrivalTime(startDate);
        }
        return null;
    }

    public LocalTime getStopArrivalTime(Station station) {
        if (stopsMap.containsKey(station.getStationName())) {
            return stopsMap.get(station.getStationName()).getArrivalTime();
        }
        return null;
    }

    public boolean runsOnDay(DayOfWeek dayOfWeek, Station station) {
        int index = dayOfWeek.getValue();
        Stop stop = stopsMap.get(station.getStationName());
        index = (7 + (index - stop.getDayNumber())) % 7;
        return runningDays[index];
    }
}