package WizardTD;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class Tower extends PApplet {

    PApplet parent; // Reference to the main PApplet
    private List<PShape> rangeIndicatorShapes;
    private ArrayList<PShape> damageIndicatorShapes = new ArrayList<>();


    //Coordinates of tower
    private int xCoords;
    private int yCoords;
    private int realCentreX;
    private int realCentreY;
    //PImage references and functionalities
    private PImage[] towerImgs;
    private int towerImgIteration;
    private int RangeIndicator;
    private int SpeedIndicator;
    private int DamageIndicator;
    //Interactable stats for tower gameplay
    public int towerRange;
    private float towerDamage = App.json.getInt("initial_tower_damage");
    double fireRate = App.json.getInt("initial_tower_firing_speed"); // Fire rate in fireballs per second
    private double cooldown; // Variable to track time since last shot
    private float splash_radius;
    private int freeze_duration;

    //STAT LEVELS 
    private int rangeLevel = 0;
    private int speedLevel = 0;
    private int damageLevel = 0;
    private boolean level_1 = false;
    private boolean level_2 = false;
    private boolean rangeToggle = false;
    private boolean speedToggle = false;
    private  boolean damageToggle = false;

    //GUI
    // Define the dimensions and position of the white box
    float boxWidth;
    float boxHeight;
    float boxX;
    float boxY;
    int total;

    ArrayList<PShape> oShapes;

    public Tower(PApplet parent, int gridX, int gridY, PImage[] towerImgs) {
        this.parent = parent;
        this.towerImgs = towerImgs;

        towerImgIteration = 0;
        
        xCoords = (gridX * App.CELLSIZE);
        yCoords = (gridY * App.CELLSIZE) + App.TOPBAR;

        realCentreX = xCoords + (App.CELLSIZE / 2);
        realCentreY = yCoords + (App.CELLSIZE / 2);

        this.towerRange = App.json.getInt("initial_tower_range");

        rangeIndicatorShapes = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            PShape rangeIndicatorShape = parent.createShape(parent.ELLIPSE, realCentreX - App.CELLSIZE/2 + i*6, yCoords + 4, 6, 6);
            rangeIndicatorShape.setFill(false); // No fill
            rangeIndicatorShape.setStroke(parent.color(255, 0, 255)); // Red outline color
            rangeIndicatorShapes.add(rangeIndicatorShape);
        }

        for (int i = 0; i < 300; i++) {
            PShape xShape = createXShape(); // Create a custom "X" shape
            xShape.setFill(parent.color(255, 0, 255));
            xShape.setStroke(false);
            xShape.setStrokeWeight(1);
            damageIndicatorShapes.add(xShape);
        }
    }

    private PShape createXShape() {
        PShape xShape = parent.createShape();
        xShape.beginShape();

        xShape.setStrokeWeight(1);
        
        float halfWidth = 3; // Half the width of the "X"
        float halfHeight = 5; // Half the height of the "X"
        
        // Define the first line of the "X" from top-left to bottom-right
        xShape.vertex(-halfWidth, -halfHeight);
        xShape.vertex(halfWidth, halfHeight);
        
        // Define the second line of the "X" from top-right to bottom-left
        xShape.vertex(halfWidth, -halfHeight);
        xShape.vertex(-halfWidth, halfHeight);
        
        xShape.endShape();
        return xShape;
    }

        // Initialize the attack range PShape
    public int getImgIteration() {
        return towerImgIteration;
    }

    public void setLevel_1() {
        level_1 = true;
    }

    public void setLevel_2() {
        level_2 = true;
    }

    public void setRangeToggle(boolean toggle) {
        rangeToggle = toggle;
    }

    public void setSpeedToggle(boolean toggle) {
        speedToggle = toggle;
    }

    public void setDamageToggle(boolean toggle) {
        damageToggle = toggle;
    }

    public float getXCoords() {
        return xCoords;
    }
    
    public float getYCoords() {
        return yCoords;
    }
    
    // Method to check if the mouse is over the button
    boolean isMouseOver() {
        return parent.mouseX >= xCoords && parent.mouseX <= xCoords + 32 && parent.mouseY >= yCoords && parent.mouseY <= yCoords + 32;
    }

    public int UpgradeRangeCost() {
        int mana_cost = 20 + rangeLevel * 10;
        return mana_cost;
    }

    public int UpgradeSpeedCost() {
        int mana_cost = 20 + speedLevel * 10;
        return mana_cost;
    }

    public int UpgradeDamageCost() {
        int mana_cost = 20 + damageLevel * 10;
        return mana_cost;
    }

    // Method to decrement the cooldown timer
    public void decrementCooldown() {
        if (cooldown > 0) {
            cooldown -= frameRate * App.fast_forward_modifier; // Decrement by the frame rate (milliseconds per frame)
            if (cooldown < 0) {
                cooldown = 0; // Ensure cooldown doesn't go negative
            }
        }
    }

    public double getCooldown() {
        return cooldown;
    }

    public double getFireRate() {
        return fireRate;
    }

    public void setCooldown(double timeBetweenShots) {
        cooldown = timeBetweenShots;
    }

    public void findEnemy(boolean hasFired, List<Monsters> enemies, List<Fireball> activeFireballs, PImage projectileImg, boolean default_tower) {
        // Iterate through all enemies to find targets
        for (Monsters enemy : enemies) {
            // Calculate the distance between the tower and enemy
            float distance = dist(getXCoords() + App.CELLSIZE / 2, getYCoords() + App.CELLSIZE / 2,
                    enemy.getX() + Monsters.MONSTER_SIZE / 2, enemy.getY() + Monsters.MONSTER_SIZE / 2);
            
            // If the enemy is within the tower's range and not dying, attack it
            if (!default_tower) { // So freeze tower does not attack frozen enemy again
                if (distance <= getAttackRange() && !enemy.getDying() && !enemy.isFrozen()) { 
                    attackEnemy(enemy, activeFireballs, projectileImg);
                    hasFired = true; // Tower has fired
                    break; // Exit the loop since the tower has fired
                }
            } else {
                if (distance <= getAttackRange() && !enemy.getDying()) {
                    attackEnemy(enemy, activeFireballs, projectileImg);
                    hasFired = true; // Tower has fired
                    break; // Exit the loop since the tower has fired
                }
            }
        }

            
            if (hasFired) {
                double timeBetweenShots = 1000.0 / getFireRate(); // Convert fire rate from fireballs per second to milliseconds
                setCooldown(timeBetweenShots);
        }
    }

    public void attackEnemy(Monsters enemy, List<Fireball> activeFireballs, PImage projectileImg) {
        if (cooldown <= 0) { // Check if the tower is ready to fire (cooldown has expired)
            Fireball projectile;
            if (this instanceof FreezeTower) {
                projectile = new Snowball(realCentreX, realCentreY, enemy, towerDamage, projectileImg, splash_radius, freeze_duration); // Create a new Fireball targeting the specified enemy
            } else {
                projectile = new Fireball(realCentreX, realCentreY, enemy, towerDamage, projectileImg);
            }
            activeFireballs.add(projectile); // Add the fireball to the list of active fireballs

            cooldown = (int)(1000.0 / fireRate); // Reset the cooldown in milliseconds based on fireRate

        }
    }

    public void upgradeRange() {
        rangeLevel += 1;
        RangeIndicator += 1;

        towerRange += 32;
    }

    public void upgradeSpeed() {
        speedLevel += 1;
        SpeedIndicator += 1;

        fireRate += 0.5;
    }

    public void upgradeDamage() {
        damageLevel += 1;
        DamageIndicator += 1;

        towerDamage += App.json.getInt("initial_tower_damage")/2;
    }

    public void updateTowerImage() {
        if ((rangeLevel >= 1 && speedLevel >= 1 && damageLevel >= 1) &&
        (rangeLevel < 2 || speedLevel < 2 || damageLevel < 2) && level_1 == false) {
            towerImgIteration = 1;
            RangeIndicator -= 1;
            SpeedIndicator -= 1;
            DamageIndicator -= 1;
            level_1 = true; //So that it can't be called twice
        }
        if ((rangeLevel >= 2 && speedLevel >= 2 && damageLevel >= 2)  &&
        (rangeLevel < 3 || speedLevel < 3 || damageLevel < 3) && level_2 == false) {
            towerImgIteration = 2;
            RangeIndicator -= 1;
            SpeedIndicator -= 1;
            DamageIndicator -= 1;
            level_2 = true; //So that it can't be called twice
        }
    }

    public void defineUpgradeBox(int dimension_increment) {
        // Define the dimensions and position of the white box
        boxWidth = 90;
        boxHeight = 50 + 20 * dimension_increment;
        boxX = 650;
        boxY = 555;
                
        // Draw white box in the bottom right corner
        parent.fill(255);
        parent.rect(boxX, boxY, boxWidth, boxHeight);

        // Draw black lines to split rows
                
        parent.stroke(0);
        parent.strokeWeight(2);
        parent.line(boxX, boxY + 25, boxX + boxWidth, boxY + 25);
        parent.line(boxX, boxY + 25 + (20 * dimension_increment) , boxX + boxWidth, boxY + 25 + (20 * dimension_increment));
    }

    public int checkToggles() {
        int dimension_increment = 0;
        if (rangeToggle || speedToggle || damageToggle) {
            if (rangeToggle) {
                dimension_increment += 1;
            }
            if (speedToggle) {
                dimension_increment += 1;
            }
            if (damageToggle) {
                dimension_increment += 1;
            }
        }

        return dimension_increment;
    }

    public void drawInfoBox() {
        int speed_cost = 20 + speedLevel * 10;
        int range_cost = 20 + rangeLevel * 10;      
        int damage_cost = 20 + damageLevel * 10;       

        int dimension_increment = checkToggles();
        
        if (dimension_increment != 0) {
            defineUpgradeBox(dimension_increment);

            // Set text properties
            parent.fill(0);
            parent.textSize(12);

            // Draw "Upgrade cost:" in the first row
            parent.text("Upgrade cost", boxX + 10, boxY + 5);

            // Draw variables/stats in the second row
            int textAlignModifier = 0;
            if (rangeToggle) {
                textAlignModifier += 1;
                parent.text("range:      " + range_cost, boxX + 10, boxY + 10 + 20 * textAlignModifier);
            } else {
                range_cost = 0;
            }
            if (speedToggle) {
                textAlignModifier += 1;
                parent.text("speed:      " + speed_cost, boxX + 10, boxY + 10 + 20 * textAlignModifier);
            } else {
                speed_cost = 0;
            }
            if (damageToggle) {
                textAlignModifier += 1;
                parent.text("damage:   " + damage_cost, boxX + 10, boxY + 10 + 20 * textAlignModifier);
            } else {
                damage_cost = 0;
            }

            parent.text("Total:      " + (range_cost + speed_cost + damage_cost), boxX + 10, boxY + 30 + 20 * textAlignModifier);
        }
    }

    public void drawAttackRange(PApplet app) {
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

    public void drawRangeIndicator(PApplet app) {
        for (int i = 0; i < rangeIndicatorShapes.size() && i < RangeIndicator; i++) {
            PShape shape = rangeIndicatorShapes.get(i); // Get the shape at index i
            shape.setFill(false); // No fill
            shape.setStroke(app.color(255, 0, 255)); // Red outline color
            app.shape(shape); // Draw each range indicator shape
        }
    }

    public void drawDamageIndicators(PApplet app) {
        for (int i = 0; i < damageIndicatorShapes.size() && i < DamageIndicator; i++) {
            PShape xShape = damageIndicatorShapes.get(i);
            xShape.resetMatrix();
            xShape.translate(realCentreX - App.CELLSIZE / 2 + i * 7, yCoords + App.CELLSIZE / 2 + 12);
            app.shape(xShape);
        }
    }

    public void draw(PApplet app) {
        app.image(towerImgs[towerImgIteration], xCoords, yCoords);

        if (isMouseOver()) {
            drawAttackRange(app);

            drawInfoBox();
        } 

        for (int i = 0; i < RangeIndicator; i++) {
            drawRangeIndicator(app);
         }

        for (int i = 0; i < SpeedIndicator; i++) {
            // Set the fill color to transparent (no fill)
            app.noFill();
            
            // Set the stroke color to blue
            app.stroke(0, 100, 200);
            
            // Set the stroke weight (line thickness) to a constant value
            app.strokeWeight(2 + i/2);
            
            // Calculate the position of the square
            float squareX = realCentreX - App.CELLSIZE / 4;
            float squareY = realCentreY - App.CELLSIZE / 4;
            
            // Draw the square with a blue outline
            app.rect(squareX, squareY, App.CELLSIZE / 2, App.CELLSIZE / 2);
            // Reset stroke settings
            app.stroke(0); // Set stroke color to transparent (no stroke)
            app.strokeWeight(1); // Reset stroke weight to its default value
        }
        
        for (int i = 0; i < DamageIndicator; i++) {
            drawDamageIndicators(app);
        }
    }

    public float getAttackRange() {
        return towerRange;
    }

}
