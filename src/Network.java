import java.util.*;

public class Network {
    private Map<String, Map<String, Integer>> adjacencyMap;

    public Network(String[] nodes, Edge[] edges) {
        adjacencyMap = new HashMap<>();

        for (String node : nodes) {
            adjacencyMap.put(node, new HashMap<>());
        }

        for (Edge edge : edges) {
            adjacencyMap.get(edge.getNode1()).put(edge.getNode2(), edge.getJourneyTimeInMinutes());
            adjacencyMap.get(edge.getNode2()).put(edge.getNode1(), edge.getJourneyTimeInMinutes());
        }
    }

    public int getJourneyTime(String from, String to) {
        if (from.equals(to)) {
            return 0;
        }

        Map<String, Integer> neighbors = adjacencyMap.get(from);
        if (neighbors == null || !neighbors.containsKey(to)) {
            throw new IllegalArgumentException("No direct route from " + from + " to " + to);
        }

        return neighbors.get(to);
    }

    public boolean hasDirectRoute(String from, String to) {
        if (from.equals(to)) {
            return true;
        }
        Map<String, Integer> neighbors = adjacencyMap.get(from);
        return neighbors != null && neighbors.containsKey(to);
    }

    public List<String> findShortestPath(String start, String end) {
        Map<String, Integer> distances = new HashMap<>();
        Map<String, String> previous = new HashMap<>();
        PriorityQueue<String> queue = new PriorityQueue<>(
                (a, b) -> Integer.compare(distances.getOrDefault(a, Integer.MAX_VALUE),
                        distances.getOrDefault(b, Integer.MAX_VALUE)));

        for (String node : adjacencyMap.keySet()) {
            distances.put(node, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            if (current.equals(end)) {
                break;
            }

            int currentDistance = distances.get(current);

            for (Map.Entry<String, Integer> neighbor : adjacencyMap.get(current).entrySet()) {
                String neighborNode = neighbor.getKey();
                int weight = neighbor.getValue();
                int distanceThrough = currentDistance + weight;

                if (distanceThrough < distances.getOrDefault(neighborNode, Integer.MAX_VALUE)) {
                    distances.put(neighborNode, distanceThrough);
                    previous.put(neighborNode, current);

                    queue.remove(neighborNode);
                    queue.add(neighborNode);
                }
            }
        }

        List<String> path = new ArrayList<>();
        String current = end;

        if (!previous.containsKey(end) && !start.equals(end)) {
            return path;
        }

        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }

        return path;
    }
}
