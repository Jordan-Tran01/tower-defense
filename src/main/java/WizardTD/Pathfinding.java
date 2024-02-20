package WizardTD;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Pathfinding {
    private boolean[][] grid;

    public Pathfinding(boolean[][] grid) {
        this.grid = grid;
    }

    public List<Node> findPath(Node start, Node goal) {
        PriorityQueue<Node> openSet = new PriorityQueue<>();
        List<Node> closedSet = new ArrayList<>();
        openSet.add(start);

        // Create a mapping to store the best-known path to each node
        Map<Node, Node> cameFrom = new HashMap<>();
        // Initialize the cost from start to start to be 0
        start.setG(0);
        
        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.equals(goal)) {
                // Reconstruct the path using the cameFrom mapping
                List<Node> path = reconstructPath(current, cameFrom);

                // Reverse the path to start from the monster's current position
                Collections.reverse(path);
                return path;
            }

            closedSet.add(current);
            for (Node neighbor : getNeighbors(current)) {
                if (closedSet.contains(neighbor) || !neighbor.isWalkable()) {
                    continue;
                }

                int tentativeGScore = current.getG() + 1;

                if (!openSet.contains(neighbor) || tentativeGScore < neighbor.getG()) {
                    cameFrom.put(neighbor, current);
                    neighbor.setG(tentativeGScore);
                    neighbor.setH(heuristic(neighbor, goal));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null; // No path found
    }

    private List<Node> reconstructPath(Node current, Map<Node, Node> cameFrom) {
        List<Node> path = new ArrayList<>();
        while (current != null) {
            path.add(current);
            current = cameFrom.get(current);
        }
        return path;
    }


    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int row = node.getRow();
        int col = node.getCol();
    
        // Example: Add adjacent nodes
        if (isValidLocation(row - 1, col)) {
            neighbors.add(new Node(row - 1, col, grid));
        }
        if (isValidLocation(row + 1, col)) {
            neighbors.add(new Node(row + 1, col, grid));
        }
        if (isValidLocation(row, col - 1)) {
            neighbors.add(new Node(row, col - 1, grid));
        }
        if (isValidLocation(row, col + 1)) {
            neighbors.add(new Node(row, col + 1, grid));
        }
    
        return neighbors;
    }

    private boolean isValidLocation(int row, int col) {
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length;
    }

    private int heuristic(Node a, Node b) {
        // Implement your heuristic function (e.g., Manhattan distance, Euclidean distance) here
        // For example, using Manhattan distance:
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

}
