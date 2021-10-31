import java.time.Duration;

public class ConnectedTrains {
    private TrainJourney[] trainJourneys;

    public ConnectedTrains() {
    }

    public ConnectedTrains(TrainJourney[] trainJourneys) {
        this.trainJourneys = trainJourneys;
    }


    public Duration getTotalJourneyTime() {
        int size = trainJourneys.length;
        if (size == 1)
            return Duration.between(trainJourneys[0].getStartTime(), trainJourneys[1].getEndTime());

        return Duration.between(trainJourneys[0].getStartTime(), trainJourneys[size - 1].getEndTime());
    }
}

