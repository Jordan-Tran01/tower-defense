package WizardTD;

import java.util.List;

import processing.core.PImage;


public class Snowball extends Fireball{

    PImage snowballImg;

    private float freeze_duration;
    private float splash_radius;

    public Snowball(float startX, float startY, Monsters enemy, float damage, PImage snowballImg, float splash_radius, float freeze_duration) {
        super(startX, startY, enemy, damage, snowballImg);
        this.snowballImg = snowballImg;
        this.freeze_duration = App.json.getFloat("initial_freeze_tower_freeze_duraton");
        this.splash_radius = App.json.getFloat("initial_freeze_tower_splash_radius");    
    }

    public void dealHit(List<Monsters> enemies) {
        for (Monsters enemy : enemies) {
            float distance = dist((getX() - snowballImg.width/2) + App.CELLSIZE / 2, (getY() - snowballImg.height/2) + App.CELLSIZE / 2,
            enemy.getX() + Monsters.MONSTER_SIZE / 2, enemy.getY() + Monsters.MONSTER_SIZE / 2);
            if (distance <= splash_radius) {
                enemy.takeFreeze(freeze_duration);
            }
        }
    }
}
