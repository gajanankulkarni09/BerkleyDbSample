import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class SearchTrainCommand {
    private Station sourceStation;
    private Station destinationStation;


    public SearchTrainCommand(Station sourceStation, Station destinationStation) {
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    public Station getSourceStation() {
        return sourceStation;
    }

    public Station getDestinationStation() {
        return destinationStation;
    }

    public List<ConnectedTrains> searchTrains(LocalDateTime journeyDate) {
        RouteExploreCommand routeExploreCommand;
        routeExploreCommand = new RouteExploreCommand(sourceStation, destinationStation);
        Iterator<Iterator<Track>> routesList = routeExploreCommand.getRoute();
        List<ConnectedTrains> connectedTrains = new ArrayList<>();

        while (routesList.hasNext()) {
            Iterator<Track> trackIterator = routesList.next();
            Iterable<Track> trackIterable = (() -> trackIterator);
            List<Track> trackList = StreamSupport
                    .stream(trackIterable.spliterator(), false)
                    .collect(Collectors.toList());
            Route route = new Route(trackList, sourceStation, destinationStation);
            connectedTrains.addAll(route.findConnectingTrains(journeyDate));
        }

        return connectedTrains.stream().sorted(Comparator.comparing(ConnectedTrains::getTotalJourneyTime)).limit(15).toList();
    }
}
