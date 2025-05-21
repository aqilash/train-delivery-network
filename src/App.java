public class App {
    public static void main(String[] args) {
        // Example hardcoded input for testing - would be replaced by actual input
        // parsing
        // 3 stations
        String[] nodes = { "A", "B", "C" };

        // 2 edges
        Edge[] edges = {
                new Edge("E1", "A", "B", 30),
                new Edge("E2", "B", "C", 20),
                // new Edge("E3", "C", "D", 10)
        };

        // 1 package
        Package[] packages = {
                new Package("K1", 5, "A", "C"),
                // new Package("K2", 5, "C", "D")
        };

        // 1 train
        Train[] trains = {
                new Train("Q1", 6, "B"),
                // new Train("Q2", 6, "C")
        };

        // Create the network
        Network network = new Network(nodes, edges);

        // Create and run the solution
        DeliveryScheduler scheduler = new DeliveryScheduler(network, trains, packages);
        Move[] moves = scheduler.generateDeliverySchedule();

        // Print the solution
        for (Move move : moves) {
            System.out.println(move);
        }
    }
}
