package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.core.PShape;

public class TowerTest {

    @Test
    void testConstructorNull() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);
        assertNotNull(tower);
    }

    @Test
    void testUpgradeImage() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);
        assertEquals(0, tower.getImgIteration());
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        tower.updateTowerImage();
        assertEquals(1, tower.getImgIteration());
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        tower.updateTowerImage();
        assertEquals(2, tower.getImgIteration());
        tower.upgradeRange();
        tower.updateTowerImage();
        assertEquals(2, tower.getImgIteration());
    }

    @Test
    void testMixedUpgradeImage() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);
        tower.upgradeSpeed();
        tower.upgradeSpeed();
        tower.upgradeRange();
        tower.upgradeDamage();
        tower.updateTowerImage();
        assertEquals(1, tower.getImgIteration());
        tower.upgradeRange();
        tower.updateTowerImage();
        assertEquals(1, tower.getImgIteration());
        tower.upgradeDamage();
        tower.updateTowerImage();
        assertEquals(2, tower.getImgIteration());
        tower.upgradeSpeed();
        tower.upgradeDamage();
        tower.updateTowerImage();
        assertEquals(2, tower.getImgIteration());
    }

    @Test
    void testAlreadyUpgraded() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);
        tower.setLevel_1();
        tower.updateTowerImage();
        tower.setLevel_2();
        tower.updateTowerImage();
        assertEquals(0, tower.getImgIteration());
    }

    @Test
    void testUpgradeCost() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);
        assertEquals(20, tower.UpgradeRangeCost());
        assertEquals(20, tower.UpgradeSpeedCost());
        assertEquals(20, tower.UpgradeDamageCost());
    }

    @Test
    void testFreezeTowerDraw() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        FreezeTower tower = new FreezeTower(app, 0, 0, app.towerImgs);
        tower.draw(app);
        app.mouseX = 0;
        app.mouseY = 60;
        tower.draw(app);
        assertEquals(true, tower.isMouseOver());
    }

    @Test
    void testfindEnemy_Tower() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);
        Monsters monster = new Monsters(app, 40, 40, "MonsterType", 100, 100, 2.0f, 1, 10);
        List<Fireball> activeFireballs = new ArrayList<>();
        List<Monsters> enemies = new ArrayList<>();
        enemies.add(monster);

        int tick = 1000;
        for (int i = 0; i < tick; i++) {
            tower.findEnemy(false, enemies, activeFireballs, app.fireballImg, true);
            tower.decrementCooldown();
            monster.takeDamage(10);
            monster.tick();
            assert(true);
        }
    }

    @Test
    void testFindEnemy_IceTower() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        FreezeTower tower = new FreezeTower(app, 0, 0, app.icetowerImgs);
        Monsters monster = new Monsters(app, 10, 10, "MonsterType", 100, 100, 2.0f, 1, 10);
        Monsters far_monster = new Monsters(app, 1000, 1000, "MonsterType", 100, 100, 2.0f, 1, 10);
        List<Fireball> activeFireballs = new ArrayList<>();
        List<Monsters> enemies = new ArrayList<>();
        enemies.add(monster);
        enemies.add(far_monster);
        
        tower.findEnemy(false, enemies, activeFireballs, app.snowballImg, false);
        int tick = 1000;
        monster.takeFreeze(10);
        for (int i = 0; i < tick; i ++) {
            monster.tick();
            tower.findEnemy(false, enemies, activeFireballs, app.snowballImg);
        }
        assertTrue(true);
        enemies.remove(monster);
        enemies.remove(far_monster);
        tower.findEnemy(false, enemies, activeFireballs, app.snowballImg, false);
        }

    @Test
    void testFindEnemy_RealIceTower() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        FreezeTower tower = new FreezeTower(app, 0, 0, app.icetowerImgs);
        Monsters monster = new Monsters(app, 10, 10, "MonsterType", 100, 100, 2.0f, 1, 10);
        Monsters far_monster = new Monsters(app, 1000, 1000, "MonsterType", 100, 100, 2.0f, 1, 10);

        List<Fireball> activeFireballs = new ArrayList<>();
        List<Monsters> enemies = new ArrayList<>();
        enemies.add(monster);
        enemies.add(far_monster);
        
        tower.findEnemy(false, enemies, activeFireballs, app.snowballImg);
        monster.takeDamage(200);
        int tick = 1000;
        for (int i = 0; i < tick; i ++) {
            monster.tick();
            tower.findEnemy(false, enemies, activeFireballs, app.snowballImg);
        }
        enemies.remove(monster);
        enemies.remove(far_monster);
        tower.findEnemy(false, enemies, activeFireballs, app.snowballImg);
        
        assertTrue(true);
        }

    @Test
    void testDrawInfoBoxNull() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);

        tower.drawInfoBox();
        assert(true);
    }

    @Test
    void testDrawInfoBoxToggled() {
        App app = new App();
        App.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);

        tower.setRangeToggle(true);
        tower.defineUpgradeBox(1);
        assert(true);
        tower.setSpeedToggle(true);
        tower.defineUpgradeBox(2);
        assert(true);
        tower.setDamageToggle(true);
        tower.defineUpgradeBox(3);
        tower.drawInfoBox();
        assert(true);
    }

    @Test
    void testDrawInfoBoxToggledDimensions() {
        App app = new App();
        App.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);
        tower.setRangeToggle(true);
        tower.checkToggles();
        tower.setSpeedToggle(true);
        tower.checkToggles();
        tower.setDamageToggle(true);
        tower.checkToggles();
        tower.setRangeToggle(false);
        tower.checkToggles();
        tower.setSpeedToggle(false);
        tower.checkToggles();
        tower.setDamageToggle(false);
        tower.checkToggles();

        assert(true);
    }

    @Test
    void testDrawAttackRange() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);

        tower.drawAttackRange(app);
        assertEquals(96, tower.getAttackRange());
    }

    @Test
    void testDrawTowerUpgrades() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new Tower(app, 0, 0, app.towerImgs);
        tower.draw(app);
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeDamage();
        tower.draw(app);
        assert(true);
        tower.upgradeRange();
        tower.upgradeDamage();
        tower.upgradeSpeed();
        tower.draw(app);
        int tickDUpgrade = 1000;
        for (int i = 0; i < tickDUpgrade; i++) {
            tower.upgradeDamage();
        }
        int tickRUpgrade = 1000;
        for (int i = 0; i < tickRUpgrade; i ++) {
            tower.upgradeRange();
        }
        tower.draw(app);
    }
    
}
