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

                planRouteToPickUp(nearestTrain, nextPackage);

                planRouteToDeliver(nearestTrain, nextPackage);

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

    private void planRouteToPickUp(Train train, Package pkg) {
        String trainLocation = train.getCurrentNode();
        String packageLocation = pkg.getCurrentNode();

        if (trainLocation.equals(packageLocation)) {

            train.pickUpPackage(pkg);
            return;
        }

        List<String> path = network.findShortestPath(trainLocation, packageLocation);

        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            int journeyTime = network.getJourneyTime(from, to);

            moves.add(new Move(currentTime, train.getName(), from,
                    List.of(), to, List.of()));

            currentTime += journeyTime;
            train.setCurrentNode(to);

            if (to.equals(packageLocation)) {
                train.pickUpPackage(pkg);
            }
        }
    }

    private void planRouteToDeliver(Train train, Package pkg) {
        String trainLocation = train.getCurrentNode();
        String packageDestination = pkg.getDestinationNode();

        if (trainLocation.equals(packageDestination)) {
            train.dropOffPackage(pkg);
            return;
        }

        List<String> path = network.findShortestPath(trainLocation, packageDestination);

        boolean isFirstMove = true;

        for (int i = 0; i < path.size() - 1; i++) {
            String from = path.get(i);
            String to = path.get(i + 1);
            int journeyTime = network.getJourneyTime(from, to);

            List<String> packagesDroppedOff = List.of();
            List<String> packagesPickedUp = List.of();

            if (isFirstMove && from.equals(pkg.getStartingNode())) {
                packagesPickedUp = List.of(pkg.getName());
            }

            if (to.equals(packageDestination)) {

                packagesDroppedOff = List.of(pkg.getName());
            }

            moves.add(new Move(currentTime, train.getName(), from,
                    packagesPickedUp, to, packagesDroppedOff));

            currentTime += journeyTime;
            train.setCurrentNode(to);

            if (to.equals(packageDestination)) {
                train.dropOffPackage(pkg);
            }

            isFirstMove = false;
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