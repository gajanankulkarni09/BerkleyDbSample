import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Stop {
    private Station station;
    private LocalTime arrivalTime;
    private int stopNumber;
    private int dayNumber;

    public Stop() {
    }

    public Stop(Station station, LocalTime arrivalTime, int stopNumber, int dayNumber) {
        this.station = station;
        this.arrivalTime = arrivalTime;
        this.stopNumber = stopNumber;
        this.dayNumber = dayNumber;
    }

    public Station getStation() {
        return station;
    }

    public String getStopName(){
        return station.getStationName();
    }

    public LocalDateTime getArrivalTime(LocalDate trainStartDate) {
        LocalDate stopDate = trainStartDate.plusDays(dayNumber);
        LocalDateTime localDateTime = LocalDateTime.of(stopDate, arrivalTime);
        return localDateTime;
    }

    public LocalTime getArrivalTime(){
        return this.arrivalTime;
    }

    public int getStopNumber() {
        return stopNumber;
    }

    public int getDayNumber() {
        return dayNumber;
    }
}
