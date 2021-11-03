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
    private Station sourceStation;
    private Station destinationStation;

    public Train() {
    }

    public Train(String trainId, String trainName, List<Stop> stops, boolean[] runningDays) {
        this.trainId = trainId;
        this.trainName = trainName;
        this.stops = stops;
        this.stopsMap = stops.stream().collect(Collectors.toMap(Stop::getStopName, Function.identity()));
        this.runningDays = runningDays;
        sourceStation = stops.get(0).getStation();
        destinationStation = stops.get(stops.size() - 1).getStation();
    }

    public String getTrainId() {
        return trainId;
    }

    public String getTrainName() {
        return trainName;
    }

    public Station getSourceStation(){
        return this.sourceStation;
    }

    public Station getDestinationStation(){
        return this.destinationStation;
    }

    public boolean stopsAt(Station station) {
        return stopsMap.containsKey(station.getStationName());
    }

    public LocalDateTime getStopArrivalDateTime(Station station, LocalDateTime trainStartDateTime) {
      
        Stop stop = stopsMap.get(station.getStationName());
        return LocalDateTime.of(trainStartDateTime.plusDays(stop.getDayNumber()).toLocalDate(), stop.getArrivalTime());
    }

    public LocalDateTime getTrainStartDateTime(Station station, LocalDateTime stationArrivalDateTime) {
      
        Stop stop = stopsMap.get(station.getStationName());
        Stop firstStop = stops.get(0);
        return LocalDateTime.of(stationArrivalDateTime.minusDays(stop.getDayNumber()).toLocalDate(), firstStop.getArrivalTime());
    }

    public boolean runsOnDay(DayOfWeek dayOfWeek, Station station) {
        int index = dayOfWeek.getValue();
        Stop stop = stopsMap.get(station.getStationName());
        index = (7 + (index - stop.getDayNumber())) % 7;
        return runningDays[index];
    }
}