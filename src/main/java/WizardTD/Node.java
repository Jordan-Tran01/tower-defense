package WizardTD;

public class Node implements Comparable<Node> {
        private int row;
        private int col;
        private int g;
        private int h;
        private Node parent;
        private boolean[][] grid; // Add grid reference
    
        public Node(int row, int col, boolean[][] grid) {
            this.row = row;
            this.col = col;
            this.g = 0;
            this.h = 0;
            this.parent = null;
            this.grid = grid; // Initialize grid reference
        }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
    
    @Override
    public int compareTo(Node other) {
        // Compare nodes based on their F values (total cost)
        int compare = Integer.compare(this.getF(), other.getF());

        // If F values are equal, compare based on H values (heuristic)
        if (compare == 0) {
            compare = Integer.compare(this.getH(), other.getH());
        }

        return compare;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public boolean isWalkable() {
        return grid[row][col];
    }

    public int getF() {
        return g + h;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        return row == other.row && col == other.col;
    }
}
