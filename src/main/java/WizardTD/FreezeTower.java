package WizardTD;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class FreezeTower extends Tower {
    PApplet parent; // Reference to the main PApplet

    //Coordinates of tower
    private int xCoords;
    private int yCoords;
    private int realCentreX;
    private int realCentreY;
    //PImage references and functionalities
    private PImage[] towerImgs;

    public int towerRange;
    double fireRate; // Fire rate in fireballs per second

    public FreezeTower(PApplet parent, int gridX, int gridY, PImage[] towerImgs) {
        super(parent, gridX, gridY, towerImgs);
        this.parent = parent;
        this.towerImgs = towerImgs;

        xCoords = (gridX * App.CELLSIZE);
        yCoords = (gridY * App.CELLSIZE) + App.TOPBAR;

        realCentreX = xCoords + (App.CELLSIZE / 2);
        realCentreY = yCoords + (App.CELLSIZE / 2);

        this.towerRange = App.json.getInt("initial_tower_range")/2;
        this.fireRate = App.json.getFloat("initial_freeze_tower_firing_speed");

    }

    public void findEnemy(boolean hasFired, List<Monsters> enemies, List<Fireball> activeFireballs, PImage fireballImg) {
        // Iterate through all enemies to find targets
        for (Monsters enemy : enemies) {
            // Calculate the distance between the tower and enemy
            float distance = dist(getXCoords() + App.CELLSIZE / 2, getYCoords() + App.CELLSIZE / 2,
                    enemy.getX() + Monsters.MONSTER_SIZE / 2, enemy.getY() + Monsters.MONSTER_SIZE / 2);
    
            // If the enemy is within the tower's range and not dying, attack it
            if (distance <= towerRange && !enemy.getDying() && !enemy.isFrozen()) {
                attackEnemy(enemy, activeFireballs, fireballImg);
                hasFired = true; // Tower has fired
                break; // Exit the loop since the tower has fired
                }
            }

            if (hasFired) {
                double timeBetweenShots = 1000.0 / fireRate; // Convert fire rate from fireballs per second to milliseconds
                setCooldown(timeBetweenShots);
        }
    }

    public void draw(PApplet app) {
        app.image(towerImgs[0], xCoords, yCoords);

        if (isMouseOver()) {
            // Set the drawing properties for the attack range circle
            parent.pushStyle(); // Save the current drawing state
            parent.noFill(); // No fill
            parent.stroke(255, 255, 0); // Yellow outline color
            parent.strokeWeight(2); // Adjust line thickness for the outline

            // Draw the attack range circle
            parent.ellipse(realCentreX, realCentreY, towerRange * 2, towerRange * 2);

            // Restore the previous drawing state to avoid affecting other shapes
            parent.popStyle();
        } 
    }

}
