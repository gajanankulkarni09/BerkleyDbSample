import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if (trackList.size() == 0) {
            return new ArrayList<ConnectedTrains>(0);
        }

        List<Train> trains;
        if (trackList.size() == 1) {
            trains = trackList.get(0).getTrainBetween(sourceStation, destinationStation, journeyDate);
            connectedTrainList =
                    trains.stream()
                            .map(train -> {
                                LocalDateTime trainStartDate = train.getTrainStartDateTime(sourceStation, journeyDate);
                                return new ConnectedTrains(new TrainJourney[]{new TrainJourney(sourceStation.getStationName(),
                                        destinationStation.getStationName(),
                                        train.getStopArrivalDateTime(sourceStation, trainStartDate),
                                        train.getStopArrivalDateTime(destinationStation, trainStartDate))});
                            })
                            .toList();
            return connectedTrainList;
        }

        connectedTrainList = new ArrayList<>();
        final Station nextStation = trackList.get(0).getIntesectingStation(trackList.get(1));
        trains = trackList.get(0).getTrainBetween(sourceStation, nextStation, journeyDate);

        for (Train train : trains) {
            final TrainJourney[] trainJourneys = new TrainJourney[trackList.size()];

            LocalDateTime trainStartDate = train.getTrainStartDateTime(sourceStation, journeyDate);
            trainJourneys[0] = new TrainJourney(sourceStation.getStationName(),
                    nextStation.getStationName(),
                    train.getStopArrivalDateTime(sourceStation, trainStartDate),
                    train.getStopArrivalDateTime(nextStation, trainStartDate));

            if (findNextConnectingTrain(nextStation, trainJourneys, 1)) {
                connectedTrainList.add(new ConnectedTrains(trainJourneys));
            }
        }

        return connectedTrainList;
    }

    private boolean findNextConnectingTrain(Station currentSourceStation, TrainJourney[] trainJourneys, int index) {

        if (index >= trackList.size())
            return true;

        final boolean isLastTrackInRoute = index == (trainJourneys.length - 1);
        final Station nextStation = isLastTrackInRoute ? destinationStation : trackList.get(index).getIntesectingStation(trackList.get(index + 1));
        final LocalDateTime journeyDate = trainJourneys[index - 1].getEndTime();

        final List<Train> trains = trackList.get(index).getTrainBetween(currentSourceStation, nextStation, journeyDate);
        if (trains.size() == 0)
            return false;

        //below stream api find the train that start after previous train reaches current source station and reaches first to next station
        // among all available options
        Optional<Train> firstTrainOptional = trains.stream()
                .filter(train ->
                {
                    LocalDateTime trainStartDate = train.getTrainStartDateTime(currentSourceStation, journeyDate);
                    return train.getStopArrivalDateTime(currentSourceStation, trainStartDate).isAfter(journeyDate);
                })
                .sorted((t1, t2) -> {
                    LocalDateTime trainStartDateTime1 = t1.getTrainStartDateTime(currentSourceStation, journeyDate);
                    LocalDateTime trainStartDateTime2 = t2.getTrainStartDateTime(currentSourceStation, journeyDate);

                    LocalDateTime nextStationArrivalTime1 = t1.getStopArrivalDateTime(nextStation, trainStartDateTime1);
                    LocalDateTime nextStationArrivalTime2 = t2.getStopArrivalDateTime(nextStation, trainStartDateTime2);

                    return nextStationArrivalTime1.compareTo(nextStationArrivalTime2);
                })
                .findFirst();

        if (firstTrainOptional.isPresent()) {
            Train firstTrain = firstTrainOptional.get();
            LocalDateTime trainStartDate = firstTrain.getTrainStartDateTime(currentSourceStation, journeyDate);
            trainJourneys[index] = new TrainJourney(currentSourceStation.getStationName(),
                    nextStation.getStationName(),
                    firstTrain.getStopArrivalDateTime(currentSourceStation, trainStartDate),
                    firstTrain.getStopArrivalDateTime(nextStation, trainStartDate));

            return findNextConnectingTrain(nextStation, trainJourneys, index + 1);
        } else {
            return false;
        }
    }
}
