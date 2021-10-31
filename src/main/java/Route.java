import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Route {

    private Station sourceStation;
    private Station destinationStation;
    private List<Track> trackList;
    private List<ConnectedTrains> connectedTrainList;

    public Route(List<Track> trackList, Station sourceStation, Station destinationStation) {
        this.trackList = trackList;
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    public List<ConnectedTrains> findConnectingTrains(LocalDateTime journeyDate) {
        connectedTrainList = new ArrayList<>();

        if (trackList.size() == 0) {
            return connectedTrainList;
        }

        List<Train> trains;
        if (trackList.size() == 1) {
            trains = trackList.get(0).getTrainBetween(sourceStation, destinationStation, journeyDate);
            connectedTrainList =
                    trains.stream()
                            .map(train ->
                                    new ConnectedTrains(new TrainJourney[]{new TrainJourney(sourceStation.getStationName(),
                                            destinationStation.getStationName(),
                                            LocalDateTime.of(journeyDate.toLocalDate(), train.getStopArrivalTime(sourceStation)),
                                            LocalDateTime.of(journeyDate.toLocalDate(), train.getStopArrivalTime(destinationStation)))}))
                            .toList();
            return connectedTrainList;
        }

        Station nextStation = trackList.get(0).getIntesectingStation(trackList.get(1));
        trains = trackList.get(0).getTrainBetween(sourceStation, nextStation, journeyDate);

        for (Train train : trains) {
            final TrainJourney[] trainJourneys = new TrainJourney[trackList.size()];

            trainJourneys[0] = new TrainJourney(sourceStation.getStationName(),
                    nextStation.getStationName(),
                    LocalDateTime.of(journeyDate.toLocalDate(), train.getStopArrivalTime(sourceStation)),
                    LocalDateTime.of(journeyDate.toLocalDate(), train.getStopArrivalTime(nextStation)));

            if (findNextConnectingTrain(nextStation, trainJourneys, 1)) {
                connectedTrainList.add(new ConnectedTrains(trainJourneys));
            }
        }

        return connectedTrainList;
    }

    private boolean findNextConnectingTrain(Station currentSourceStation, TrainJourney[] trainJourneys, int index) {

        if (index >= trackList.size())
            return true;

        boolean isLastTrackInRoute = index == (trainJourneys.length - 1);
        final Station nextStation = isLastTrackInRoute ? destinationStation : trackList.get(index).getIntesectingStation(trackList.get(index + 1));
        final LocalDateTime journeyDate = trainJourneys[index - 1].getEndTime();

        final List<Train> trains = trackList.get(index).getTrainBetween(currentSourceStation, nextStation, journeyDate);
        if (trains.size() == 0)
            return false;

        Optional<Train> firstTrainOptional = trains.stream()
                .filter(train -> train.getStopArrivalTime(nextStation).isAfter(journeyDate.toLocalTime()))
                .sorted((t1, t2) -> {
                    LocalDateTime t1Time = t1.getStopArrivalDateTime(destinationStation, journeyDate.toLocalDate());
                    LocalDateTime t2Time = t2.getStopArrivalDateTime(destinationStation, journeyDate.toLocalDate());
                    return t1Time.compareTo(t2Time);
                }).findFirst();

        if (firstTrainOptional.isPresent()) {
            Train firstTrain = firstTrainOptional.get();
            trainJourneys[index] = new TrainJourney(currentSourceStation.getStationName(),
                    nextStation.getStationName(),
                    firstTrain.getStopArrivalDateTime(currentSourceStation, journeyDate.toLocalDate()),
                    firstTrain.getStopArrivalDateTime(nextStation, journeyDate.toLocalDate()));
            return findNextConnectingTrain(nextStation, trainJourneys, index + 1);
        } else {
            return false;
        }
    }
}
