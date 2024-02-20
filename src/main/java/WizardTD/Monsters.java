package WizardTD;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.List;

public class Monsters {
    public float x;
    public float y;
    public float startX;
    public float startY;
    public String nameType;
    public PImage type;
    public PImage frozen_type;
    public float hp;
    public float fullhp;
    public float Vel;
    public double armour;
    
    public float Initial_Vel;
    public float Vel_before_freeze; 
    public int mana_gain_on_kill;

    private float freeze_duration;
    private boolean frozen;
    private int freezeTimer;

    private int deathFrameIndex;  // Index of the current death frame
    private boolean dead = false;
    private boolean isDying = false;
    private int deathFrameDuration = 4; // Number of frames per death frame
    private int deathFrameTimer = 0;     // Timer to control frame duration

    private List<Node> path; // Add a member to store the path
    public static final int MONSTER_SIZE = 20;
    public static final int HorizontalTileOffset = 6;
    public static final int VerticalTileOffset = 46;
    int GRID_CELL_SIZE = 32; // Adjust this based on your game's grid cell size

    private Node startNode;


    private PImage[] deathFrames;

    public void setPath(List<Node> path) {
        this.path = path;
        if (path != null) 
            startNode = path.get(0);
    }

    public Monsters(PApplet app, float x, float y, String type, float hp, float fullhp, float speed, double armour, int mana_gain_on_kill) {
        this.nameType = type;
        if (type.equals("gremlin")) {
            this.type = App.gremlinImg;
        } else if (type.equals("beetle")) {
            this.type = App.beetleImg;
        } else if (type.equals("worm")) {
            this.type = App.wormImg;
        } else {
            this.type = App.gremlinImg;
        }

        this.Vel = speed; //Change this through config when passing parameter
        this.Initial_Vel = speed;
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.hp = hp;
        this.fullhp = fullhp;
        this.armour = armour;
        this.mana_gain_on_kill = mana_gain_on_kill;
        this.deathFrames = App.deathFrames; // Store the death frames array
        this.frozen_type = App.frozengremlinImg;
        //Get starting nodes
    }

    public float getstartX() {
        return startX;
    }

    public float getstartY() {
        return startY;
    }

    public float getFullHp() {
        return fullhp;
    }
    
    public Node getStartNode() {
        return startNode;
    }

    public PImage[] getdeathFrames() {
        return deathFrames;
    }

    public String getType() {
        return nameType;
    }

    public float getHp() {
        return hp;
    }

    public float getSpeed() {
        return Initial_Vel;
    }

    public double getArmour() {
        return armour;
    }

    public int get_mana_gained_on_killed() {
        return mana_gain_on_kill;
    }
    

    public void takeDamage(double damage) {
        damage = damage * armour;
        if (hp <= damage) {
            hp = 0;
            Initial_Vel = 0;
            isDying = true;
            return;
        } else {
            hp -= damage;
        }
    }

    public void takeFreeze(float freeze_duration) {
        if (!frozen) {
            Vel_before_freeze = Initial_Vel;
        }
        this.freeze_duration = freeze_duration;
        frozen = true;
        Initial_Vel = 0;
        freezeTimer = 0; //In the scenario the Monsters are hit again to reset freeze duration
    }

    public boolean isFrozen() {
        return frozen;
    }

    public boolean isDead() {
        return dead;
    }

    public void tick() {
        if (path != null && !path.isEmpty()) {
            Vel = Initial_Vel;
            // Get the next node in the path
            Node nextNode = path.get(0);
            Vel = Vel * App.fast_forward_modifier;
            // Calculate the distance between the monster and the next node in pixels
            float dx = nextNode.getCol() * GRID_CELL_SIZE - x;
            float dy = nextNode.getRow() * GRID_CELL_SIZE - y;
            float distance = (float) Math.sqrt(dx * dx + dy * dy);

            // Calculate the velocity in pixels per frame
            float vx = (float) (dx / distance) * Vel;
            float vy = (float) (dy / distance) * Vel;

            // Check if the monster has reached the next node
            if (distance <= Vel) {
                // Remove the reached node from the path
                path.remove(0);

                // Check if there are more nodes in the path
                if (!path.isEmpty()) {
                    // Get the new next node
                    nextNode = path.get(0);
                }
            }

            // Update the monster's position
            x += vx;
            y += vy;

            if (isDying) {
                // Draw death animation frames
                if (deathFrames != null && deathFrameIndex >= 0 && deathFrameIndex < deathFrames.length && deathFrames[deathFrameIndex] != null) {
                    type = deathFrames[deathFrameIndex];
                }

                // Update death frame timer
                deathFrameTimer++;
                if (deathFrameTimer >= deathFrameDuration) {
                    deathFrameIndex++;
                    deathFrameTimer = 0;
                }
                    
                    // Check if the death animation is complete
                if (deathFrameIndex >= deathFrames.length) {
                    dead = true;
                }
            }

            if (frozen) {
                freezeTimer += 1 * App.fast_forward_modifier;
                if (freezeTimer >= freeze_duration * App.FPS) {
                    frozen = false;
                    freezeTimer = 0;
                    Initial_Vel = Vel_before_freeze;
                }
            }
        } 
    }

    public void draw(PApplet app) {
        if (!isDying) {
            // Calculate HP bar dimensions
            float barWidth = 30; // Adjust this as needed
            float barHeight = 5; // Adjust this as needed
            float barX = x - (barWidth / 2) + (MONSTER_SIZE / 2) + HorizontalTileOffset;
            float barY = y - 6 + VerticalTileOffset; // Adjust this as needed
        
            // Calculate the percentage of HP remaining
            float hpPercentage = hp / fullhp;
            float remaining_hp = hpPercentage * barWidth;
            app.noStroke();
            // Draw the HP bar background (red)
            app.fill(255, 0, 0); // Red color
            app.rect(barX, barY, barWidth, barHeight);
        
            // Draw the HP bar (green) based on the HP percentage
            app.fill(0, 255, 0); // Green color
            app.rect(barX, barY, remaining_hp, barHeight);
        }
    
        // Draw the monster image
        if (frozen && !isDying) {
            app.image(frozen_type, (float) x + HorizontalTileOffset, (float) y + VerticalTileOffset);
        } else {
            app.image(type, (float) x + HorizontalTileOffset, (float) y + VerticalTileOffset);
        }
    }

    public boolean hasReachedDestination() {
        // Check if the path list is empty
        return path == null || path.isEmpty();
    }
    
    public boolean getDying() {
        return isDying;
    }

    public float getX() {
        float real_x = x + HorizontalTileOffset;
        return real_x;
    }

    public float getY() {
        float real_y = y + VerticalTileOffset;
        return real_y;
    }
    

    // Implement any additional methods or logic as needed for your monsters
}
