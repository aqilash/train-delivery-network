import java.util.*;

public class DeliveryScheduler {
    private Network network;
    private Train[] trains;
    private Package[] packages;
    private int currentTime;
    private List<Move> moves;

    public DeliveryScheduler(Network network, Train[] trains, Package[] packages) {
        this.network = network;
        this.trains = trains;
        this.packages = packages;
        this.currentTime = 0;
        this.moves = new ArrayList<>();
    }

    public Move[] generateDeliverySchedule() {

        List<Package> undeliveredPackages = new ArrayList<>();
        for (Package pkg : packages) {
            undeliveredPackages.add(pkg);
        }

        while (!undeliveredPackages.isEmpty()) {

            Package nextPackage = undeliveredPackages.get(0);
            Train nearestTrain = findNearestTrainToPackage(nextPackage);

            if (nearestTrain != null) {
                planCompleteRoute(nearestTrain, nextPackage);
                undeliveredPackages.remove(nextPackage);
            } else {
                throw new IllegalStateException("Error: Package " + nextPackage.getName() +
                        " cannot be delivered due to capacity limit");
            }
        }

        addFinalPositionMoves();

        return moves.toArray(new Move[0]);
    }

    private Train findNearestTrainToPackage(Package pkg) {
        Train nearestTrain = null;
        int shortestPathTime = Integer.MAX_VALUE;

        for (Train train : trains) {

            if (pkg.getWeightInKg() > train.getCapacityInKg()) {
                continue;
            }

            List<String> path = network.findShortestPath(
                    train.getCurrentNode(), pkg.getCurrentNode());

            int pathTime = calculatePathTime(path);

            if (pathTime < shortestPathTime) {
                shortestPathTime = pathTime;
                nearestTrain = train;
            }
        }

        return nearestTrain;
    }

    private int calculatePathTime(List<String> path) {
        int totalTime = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalTime += network.getJourneyTime(path.get(i), path.get(i + 1));
        }
        return totalTime;
    }

    private void planCompleteRoute(Train train, Package pkg) {
        String trainLocation = train.getCurrentNode();
        String packageLocation = pkg.getCurrentNode();
        String packageDestination = pkg.getDestinationNode();

        // special case
        if (trainLocation.equals(packageLocation) && packageLocation.equals(packageDestination)) {
            train.pickUpPackage(pkg);
            List<String> packageNames = train.getPackageNamesOnBoard();
            train.dropOffPackage(pkg);
            moves.add(new Move(currentTime, train.getName(), packageLocation,
                    packageNames, packageDestination, List.of(pkg.getName())));
            return;
        }

        List<String> pathToPackage = trainLocation.equals(packageLocation) ? List.of(packageLocation)
                : network.findShortestPath(trainLocation, packageLocation);

        List<String> pathToDestination = packageLocation.equals(packageDestination) ? List.of(packageDestination)
                : network.findShortestPath(packageLocation, packageDestination);

        List<String> fullPath = new ArrayList<>(pathToPackage);
        if (fullPath.size() > 0 && pathToDestination.size() > 0 &&
                fullPath.get(fullPath.size() - 1).equals(pathToDestination.get(0))) {
            fullPath.addAll(pathToDestination.subList(1, pathToDestination.size()));
        } else {
            fullPath.addAll(pathToDestination);
        }

        boolean packagePickedUp = false;

        for (int i = 0; i < fullPath.size() - 1; i++) {
            String from = fullPath.get(i);
            String to = fullPath.get(i + 1);
            int journeyTime = network.getJourneyTime(from, to);

            List<String> packagesPickedUp = List.of();
            List<String> packagesDroppedOff = List.of();

            if (from.equals(packageLocation) && !packagePickedUp) {
                train.pickUpPackage(pkg);
                packagePickedUp = true;
                packagesPickedUp = train.getPackageNamesOnBoard();
            }

            if (to.equals(packageDestination) && packagePickedUp) {
                train.dropOffPackage(pkg);
                packagesDroppedOff = List.of(pkg.getName());
            }

            moves.add(new Move(currentTime, train.getName(), from,
                    packagesPickedUp, to, packagesDroppedOff));

            currentTime += journeyTime;
            train.setCurrentNode(to);
        }
    }

    private void addFinalPositionMoves() {
        for (Train train : trains) {
            String finalLocation = train.getCurrentNode();
            moves.add(new Move(
                    currentTime,
                    train.getName(),
                    finalLocation,
                    List.of(),
                    finalLocation,
                    List.of()));
        }
    }
}