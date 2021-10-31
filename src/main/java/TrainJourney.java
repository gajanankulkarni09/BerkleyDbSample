import java.time.LocalDateTime;

public class TrainJourney {
    private String sourceStation;
    private String destinationStation;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public TrainJourney() {
    }

    public TrainJourney(String sourceStation, String destinationStation, LocalDateTime startTime, LocalDateTime endTime) {
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getDestinationStation() {
        return destinationStation;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }
}
