package WizardTD;

public class Tile {
    private boolean isClickable;
    private boolean isOccupied;
    private Tower tower;

    public Tile(boolean isClickable) {
        this.isClickable = isClickable;
        this.isOccupied = false; // By default, the tile is not occupied
    }

    public boolean isClickable() {
        return isClickable;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public Tower getTower() {
        return tower;
    }

    // You can add more methods as needed for your game logic
}
