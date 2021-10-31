import org.jetbrains.annotations.NotNull;
import java.util.*;

public class RouteExploreCommand {

    private Station sourceStation;
    private Station destinationStation;
    private double originalAerialDistance;

    public RouteExploreCommand(Station sourceStation, Station destinationStation) {
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    int min = 4;

    public @NotNull Iterator<Iterator<Track>> getRoute() {
        List<Iterator<Track>> ans = new ArrayList<>();
        List<Track[]> selectedRoutes = new ArrayList<>();
        originalAerialDistance = sourceStation.getDistance(destinationStation);

        Set<Track> routes = sourceStation.getTracks();
        for (Track sourceRoute : routes) {
            Track[] currentRoutes = new Track[6];
            exploreRoutes(sourceRoute, sourceStation, currentRoutes, selectedRoutes, 0);
        }
        for (int i = 0; i < selectedRoutes.size(); i++) {
            if (selectedRoutes.get(i).length == min) {
                ans.add(Arrays.stream(selectedRoutes.get(i)).iterator());
            }
        }
        return ans.iterator();
    }

    private void exploreRoutes(@NotNull Track sourceRoute, @NotNull Station sourceStation, @NotNull Track[] currentRoutes, @NotNull List<Track[]> selectedRoutesList, int index) {
        if (index > min) return;
        double currentAerialDistance = sourceStation.getDistance(destinationStation);
        if (currentAerialDistance > 2 * originalAerialDistance) return;
        currentRoutes[index] = sourceRoute;
        if (destinationStation.hasRoute(sourceRoute)) {
            selectedRoutesList.add(Arrays.copyOf(currentRoutes, index));
            min = Math.min(min, index);
            return;
        }
        Iterator<ConnectedTracks> routeIterator = sourceRoute.getConnectedTracksList();
        while (routeIterator.hasNext()) {
            ConnectedTracks connectedRoute = routeIterator.next();
            exploreRoutes(connectedRoute.destinationTrack, connectedRoute.getIntersectingStation(), currentRoutes, selectedRoutesList, index + 1);
        }
    }
}
