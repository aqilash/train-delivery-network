import java.util.Scanner;

public class App {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            int numNodes = readIntWithPrompt("Please enter number of nodes: ");
            String[] nodes = new String[numNodes];
            for (int i = 0; i < numNodes; i++) {
                nodes[i] = readStringWithPrompt("Enter node name: ");
            }

            int numEdges = readIntWithPrompt("Please enter number of edges: ");
            Edge[] edges = new Edge[numEdges];
            for (int i = 0; i < numEdges; i++) {
                edges[i] = readEdge("Enter edge details (name,node1,node2,journeyTimeInMinutes): ", nodes);
            }

            int numPackages = readIntWithPrompt("Please enter number of packages: ");
            Package[] packages = new Package[numPackages];
            for (int i = 0; i < numPackages; i++) {
                packages[i] = readPackage("Enter package details (name,weightInKg,startingNode,destinationNode): ",
                        nodes);
            }

            int numTrains = readIntWithPrompt("Please enter number of trains: ");
            Train[] trains = new Train[numTrains];
            for (int i = 0; i < numTrains; i++) {
                trains[i] = readTrain("Enter train details (name,capacityInKg,startingNode): ", nodes);
            }

            scanner.close();

            Network network = new Network(nodes, edges);

            System.out.println("\nCalculating delivery schedule...");
            DeliveryScheduler scheduler = new DeliveryScheduler(network, trains, packages);
            Move[] moves = scheduler.generateDeliverySchedule();

            System.out.println("\nDelivery Schedule:");
            for (Move move : moves) {
                System.out.println(move);
            }

        } catch (Exception e) {
            System.err.println("Error in program: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int readIntWithPrompt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value < 0) {
                    System.out.println("Error: Please enter a non-negative number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Error: Please enter a valid number.");
            }
        }
    }

    private static String readStringWithPrompt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (value.isEmpty()) {
                System.out.println("Error: Input cannot be empty.");
                continue;
            }
            return value;
        }
    }

    private static Edge readEdge(String prompt, String[] validNodes) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                String[] parts = input.split(",");

                if (parts.length != 4) {
                    System.out
                            .println("Error: Please enter all 4 values in the correct format (name,node1,node2,time).");
                    continue;
                }

                String name = parts[0];
                String node1 = parts[1];
                String node2 = parts[2];

                if (!isValidNode(node1, validNodes)) {
                    System.out.println("Error: Node '" + node1 + "' is not in the node list.");
                    continue;
                }
                if (!isValidNode(node2, validNodes)) {
                    System.out.println("Error: Node '" + node2 + "' is not in the node list.");
                    continue;
                }

                int journeyTime;
                try {
                    journeyTime = Integer.parseInt(parts[3]);
                    if (journeyTime <= 0) {
                        System.out.println("Error: Journey time must be a positive number.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Journey time must be a valid number.");
                    continue;
                }

                return new Edge(name, node1, node2, journeyTime);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    private static Package readPackage(String prompt, String[] validNodes) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                String[] parts = input.split(",");

                if (parts.length != 4) {
                    System.out.println(
                            "Error: Please enter all 4 values in the correct format (name,weight,startNode,destNode).");
                    continue;
                }

                String name = parts[0];

                int weight;
                try {
                    weight = Integer.parseInt(parts[1]);
                    if (weight <= 0) {
                        System.out.println("Error: Weight must be a positive number.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Weight must be a valid number.");
                    continue;
                }

                String startNode = parts[2];
                String destNode = parts[3];

                if (!isValidNode(startNode, validNodes)) {
                    System.out.println("Error: Node '" + startNode + "' is not in the node list.");
                    continue;
                }
                if (!isValidNode(destNode, validNodes)) {
                    System.out.println("Error: Node '" + destNode + "' is not in the node list.");
                    continue;
                }

                return new Package(name, weight, startNode, destNode);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    private static Train readTrain(String prompt, String[] validNodes) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                String[] parts = input.split(",");

                if (parts.length != 3) {
                    System.out.println(
                            "Error: Please enter all 3 values in the correct format (name,capacity,startNode).");
                    continue;
                }

                String name = parts[0];

                int capacity;
                try {
                    capacity = Integer.parseInt(parts[1]);
                    if (capacity <= 0) {
                        System.out.println("Error: Capacity must be a positive number.");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Error: Capacity must be a valid number.");
                    continue;
                }

                String startNode = parts[2];

                if (!isValidNode(startNode, validNodes)) {
                    System.out.println("Error: Node '" + startNode + "' is not in the node list.");
                    continue;
                }

                return new Train(name, capacity, startNode);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    private static boolean isValidNode(String node, String[] validNodes) {
        for (String validNode : validNodes) {
            if (validNode.equals(node)) {
                return true;
            }
        }
        return false;
    }
}