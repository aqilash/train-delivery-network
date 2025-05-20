public class Edge {
    private String name;
    private String node1;
    private String node2;
    private int journeyTimeInMinutes;

    public Edge(String name, String node1, String node2, int journeyTimeInMinutes) {
        this.name = name;
        this.node1 = node1;
        this.node2 = node2;
        this.journeyTimeInMinutes = journeyTimeInMinutes;
    }

    public String getName() {
        return name;
    }

    public String getNode1() {
        return node1;
    }

    public String getNode2() {
        return node2;
    }

    public int getJourneyTimeInMinutes() {
        return journeyTimeInMinutes;
    }
}
