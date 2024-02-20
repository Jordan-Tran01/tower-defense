package WizardTD;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class Fireball extends PApplet{
    private float x;
    private float y;
    private float initial_speed;
    private float speed;
    private float damage;
    private Monsters enemy;
    PImage fireballImg;
    private float targetCenterX;
    private float targetCenterY;

    public Fireball(float startX, float startY, Monsters enemy, float damage, PImage fireballImg) {
        this.x = startX;
        this.y = startY;
        this.initial_speed = 5;
        this.speed = 5; // Adjust the speed as needed
        this.damage = damage; // Set the damage value
        this.enemy = enemy;
        this.fireballImg = fireballImg;
    }

    public void update() {
        if (enemy != null) {
            speed = initial_speed * App.fast_forward_modifier;
            targetCenterX = enemy.getX() + Monsters.MONSTER_SIZE / 2;
            targetCenterY = enemy.getY() + Monsters.MONSTER_SIZE / 2;

            // Calculate the direction vector from the fireball to the target center
            float dx = targetCenterX - x;
            float dy = targetCenterY - y;

            // Normalize the direction vector (Straightens the trajectory)
            float magnitude = sqrt(dx * dx + dy * dy);
            dx /= magnitude;
            dy /= magnitude;

            // Move the fireball towards the target center
            x += dx * speed;
            y += dy * speed;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void dealHit(List<Monsters> enemies) {
        enemy.takeDamage(damage);
    }

    public void draw(PApplet app) {
        app.image(fireballImg, x, y);
    }

    public boolean hasReachedTarget() {
        float distance = PApplet.dist(x, y, targetCenterX, targetCenterY); // Check if the fireball has reached its target (enemy center)
        return distance <= speed; // Fireball is considered to have reached the target when it's very close (Basically the centre of the monster)
    }

}
