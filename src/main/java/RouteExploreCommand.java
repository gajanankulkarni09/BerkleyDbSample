import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RouteExploreCommand {

    private Station sourceStation;
    private Station destinationStation;
    private double originalAerialDistance;
    private List<Track[]> selectedRoutesList = new ArrayList<>();
    private Map<String, Integer[]> stationResultMap = new HashMap<>();
    private Map<String, Boolean> stationsExistInPath = new HashMap<>();
    private Set<String> trackCodesInResult = new HashSet<>();

    public RouteExploreCommand(Station sourceStation, Station destinationStation) {
        this.sourceStation = sourceStation;
        this.destinationStation = destinationStation;
    }

    int max = 3;


    public @NotNull Iterator<Iterator<Track>> getRoute() {

        List<Iterator<Track>> ans = new ArrayList<>();
        originalAerialDistance = sourceStation.getDistance(destinationStation);

        Set<Track> tracks = sourceStation.getTracks();

        for (Track sourceTrack : tracks) {
            Track[] currentRoutes = new Track[6];
            exploreRoutes(sourceTrack, sourceStation, currentRoutes, 0);
        }

        for (int i = 0; i < selectedRoutesList.size(); i++) {
            if (selectedRoutesList.get(i).length == max) {
                ans.add(Arrays.stream(selectedRoutesList.get(i)).iterator());
            }
        }
        return ans.iterator();
    }

    private boolean exploreRoutes(@NotNull Track sourceTrack, @NotNull Station sourceStation, @NotNull Track[] currentTracks, int index) {
        if (index > max) return false;

        String oppositeTrackCode = sourceTrack.getOppositeTrackCode();
        if (trackCodesInResult.contains(oppositeTrackCode)) return false;

        String stationCode = sourceStation.getStationCode();
        String trackCode = sourceTrack.getTrackCode();
        String stationCodeTrackIdKey = stationCode + "#" + trackCode;

        //if this station is already explored as a part of another Track,
        //that result is copied. Dynamic programming Memoization technique
        //this will save lot of computations , because there are lots of tracks with common stations.
        if (stationsExistInPath.containsKey(stationCodeTrackIdKey)) {
            if (!stationsExistInPath.get(stationCodeTrackIdKey)) return false;
            for (Track t : currentTracks) {
                trackCodesInResult.add(t.getTrackCode());
            }
            copyResultFromStationMap(sourceTrack, currentTracks, index, stationCodeTrackIdKey);
            return true;
        }

        ///there is no point in exploring this path, if distance of current station from distance is too much greater than original distance
        double currentAerialDistance = sourceStation.getDistance(destinationStation);
        if (currentAerialDistance > 1.3 * originalAerialDistance) return false;

        currentTracks[index] = sourceTrack;

        //if destinationStation is on sourceTrack, we found result
        if (destinationStation.hasTrack(sourceTrack)) {
            selectedRoutesList.add(Arrays.copyOf(currentTracks, index));
            for (Track t : currentTracks) {
                trackCodesInResult.add(t.getTrackCode());
            }
            max = Math.min(max, index);
            return true;
        }

        int beforeSelectedRoutesListSize = selectedRoutesList.size();

        Iterator<ConnectedTracks> trackIterator = sourceTrack.getConnectedTracksList();
        while (trackIterator.hasNext()) {
            ConnectedTracks connectedTrack = trackIterator.next();
            //below line of code prevent loops
            if (sourceTrack.isStation1BeforeStation2(connectedTrack.getIntersectingStation(), sourceStation))
                continue;
            Track destinationTrack = connectedTrack.getDestinationTrack();
            if (sourceTrack.isOppositeTrack(destinationTrack)) continue;
            exploreRoutes(destinationTrack, connectedTrack.getIntersectingStation(),currentTracks, index + 1);
        }

        if (beforeSelectedRoutesListSize < selectedRoutesList.size()) {
            ///if there is route found from current station and current route ,
            ///save starting and end index of resultant routes in selectedRouteList
            ///also save result in Map telling there is route through this station and track
            /// below 2 lines saves this data so that we don't have to calculate same result if this station comes in another path
            /// This is memoization technique of Dynamic programming algorithm.
            stationResultMap.put(stationCodeTrackIdKey, new Integer[]{beforeSelectedRoutesListSize, selectedRoutesList.size() - 1, index});
            stationsExistInPath.put(sourceStation.getStationCode(), true);
            return true;
        }

        stationsExistInPath.put(stationCodeTrackIdKey, false);
        return false;
    }

    private void copyResultFromStationMap(@NotNull Track sourceRoute, @NotNull Track[] currentRoutes, int index, String stationIdTrackIdKey) {
        Integer[] indexes = stationResultMap.get(stationIdTrackIdKey);
        int startIndex = indexes[0];
        int endIndex = indexes[1];
        int trackIndex = indexes[2];

        for (int i = startIndex; i <= endIndex; i++) {
            Track[] tracks = Arrays.copyOf(currentRoutes, 6);
            int temp = index;
            Track[] selectedRoute = selectedRoutesList.get(i);
            boolean flag = false;
            for (int j = trackIndex; j < 6; j++) {
                if (selectedRoute[j] == null)
                    break;
                if (temp > max) {
                    flag = true;
                    break;
                }
                tracks[temp++] = selectedRoute[j];
            }
            if (flag) continue;
            max = Math.min(temp - 1, max);
            selectedRoutesList.add(tracks);
        }
    }
}
