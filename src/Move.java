import java.util.*;

public class Move {
    private int timeInSeconds;
    private String trainName;
    private String startNode;
    private List<String> packagesPickedUp;
    private String endNode;
    private List<String> packagesDroppedOff;

    public Move(int timeInMinutes, String trainName, String startNode,
            List<String> packagesPickedUp, String endNode,
            List<String> packagesDroppedOff) {
        this.timeInSeconds = timeInMinutes * 60; // TODO add *60
        this.trainName = trainName;
        this.startNode = startNode;
        this.packagesPickedUp = packagesPickedUp;
        this.endNode = endNode;
        this.packagesDroppedOff = packagesDroppedOff;
    }

    @Override
    public String toString() {
        return "W=" + timeInSeconds + ", T=" + trainName +
                ", N1=" + startNode + ", P1=" + packagesPickedUp +
                ", N2=" + endNode + ", P2=" + packagesDroppedOff;
    }
}
