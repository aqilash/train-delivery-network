public class Package {
    private String name;
    private int weightInKg;
    private String startingNode;
    private String destinationNode;
    private String currentNode;
    private boolean delivered;

    public Package(String name, int weightInKg, String startingNode, String destinationNode) {
        this.name = name;
        this.weightInKg = weightInKg;
        this.startingNode = startingNode;
        this.destinationNode = destinationNode;
        this.currentNode = startingNode;
        this.delivered = false;
    }

    public String getName() {
        return name;
    }

    public int getWeightInKg() {
        return weightInKg;
    }

    public String getStartingNode() {
        return startingNode;
    }

    public String getDestinationNode() {
        return destinationNode;
    }

    public String getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(String location) {
        this.currentNode = location;
        if (location != null && location.equals(destinationNode)) {
            delivered = true;
        }
    }

    public boolean isDelivered() {
        return delivered;
    }
}