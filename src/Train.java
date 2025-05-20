import java.util.*;

public class Train {
    private String name;
    private int capacityInKg;
    private String currentNode;
    private int availableCapacity;
    private List<Package> packagesOnBoard;

    public Train(String name, int capacityInKg, String startingNode) {
        this.name = name;
        this.capacityInKg = capacityInKg;
        this.currentNode = startingNode;
        this.availableCapacity = capacityInKg;
        this.packagesOnBoard = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getCapacityInKg() {
        return capacityInKg;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(String node) {
        this.currentNode = node;
    }

    public int getAvailableCapacity() {
        return availableCapacity;
    }

    public List<Package> getPackagesOnBoard() {
        return new ArrayList<>(packagesOnBoard);
    }

    public void pickUpPackage(Package pkg) {
        if (pkg.getWeightInKg() <= availableCapacity) {
            packagesOnBoard.add(pkg);
            availableCapacity -= pkg.getWeightInKg();
            pkg.setCurrentNode(null);
        } else {
            throw new IllegalStateException("Error: Unable to pickup package " + pkg.getName() +
                    " as it exceeds available capacity");
        }
    }

    public void dropOffPackage(Package pkg) {
        if (packagesOnBoard.remove(pkg)) {
            availableCapacity += pkg.getWeightInKg();
            pkg.setCurrentNode(currentNode);
        } else {
            throw new IllegalStateException("Error: Package " + pkg.getName() +
                    " is not on train " + name);
        }
    }

    public List<String> getPackageNames() {
        List<String> names = new ArrayList<>();
        for (Package pkg : packagesOnBoard) {
            names.add(pkg.getName());
        }
        return names;
    }
}