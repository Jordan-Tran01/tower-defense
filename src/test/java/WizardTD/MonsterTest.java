package WizardTD;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MonsterTest {

    private Random random = new Random();

    @Test
    void testConstructorNull() {
        App app = new App();
        Monsters monster = new Monsters(app, 0, 0, "null", 100, 100, 1, 1, 20);
      
        assertNotNull(monster);
    }
    @Test
    void testConstructorBeetle() {
        App app = new App();
        Monsters monster = new Monsters(app, 0, 0, "beetle", 100, 100, 1, 1, 20);
        assertNotNull(monster);
    }

    @Test
    void testConstructorWorm() {
        App app = new App();
        Monsters monster = new Monsters(app, 0, 0, "worm", 100, 100, 1, 1, 20);
        assertNotNull(monster);
    }

    @Test
    public void testConstructor() { //Tests if the constructor worked
        App app = new App();
        Monsters monster = new Monsters(app, 100, 200, "MonsterType", 100, 100, 2.0f, 0.5, 10);
        
        assertEquals(100f, monster.getHp());
        assertEquals(100, monster.getFullHp());
        assertEquals("MonsterType", monster.getType());
        assertEquals(2.0f, monster.getSpeed());
        assertEquals(0.5, monster.getArmour());
        assertEquals(10, monster.get_mana_gained_on_killed());
    }
    
    @Test
    public void testTickDamageMonster() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        Node goalNode = app.board.getGoalNode();
        Pathfinding pathfinder = new Pathfinding(app.board.getGrid());
    
        // Get a random start node
        ArrayList<Node> startNodes = app.board.getStartNodes();
        if (!startNodes.isEmpty()) {
            int randomIndex = random.nextInt(startNodes.size());
            Node startNode = startNodes.get(randomIndex);

            int startX = startNode.getCol() * app.CELLSIZE;
            int startY = startNode.getRow() * app.CELLSIZE;
                
            List<Node> path = pathfinder.findPath(startNode, goalNode);
            if (startNode.getCol() == 0) { //Spawn before appearing on path
                startX -= app.CELLSIZE;
            } else if (startNode.getCol() == 19) {
                startX += app.CELLSIZE;
            } else if (startNode.getRow() == 0) {
                startY -= app.CELLSIZE;
            } else if (startNode.getRow() == 19) {
                startY += app.CELLSIZE;
            }

            Monsters monster = new Monsters(app, startX, startY, "MonsterType", 100, 100, 2.0f, 1, 10);
            monster.setPath(path);
        // Number of ticks to perform
        int numTicks = 1000;

        for (int i = 0; i < numTicks; i++) {
            monster.tick();
            monster.draw(app);
            monster.takeDamage(20);
        }
        assertEquals(true, monster.isDead());
        }
    }

    @Test
    public void testTickMoveMonster() {
        App app = new App();

        App.runSketch(new String[] { "App" }, app);
        Node goalNode = app.board.getGoalNode();
        Pathfinding pathfinder = new Pathfinding(app.board.getGrid());
        Monsters monster;
        // Get a random start node
        ArrayList<Node> startNodes = app.board.getStartNodes();

        if (!startNodes.isEmpty()) {
            int randomIndex = random.nextInt(startNodes.size());
            Node startNode = startNodes.get(randomIndex);

            int startX = startNode.getCol() * app.CELLSIZE;
            int startY = startNode.getRow() * app.CELLSIZE;
                
            List<Node> path = pathfinder.findPath(startNode, goalNode);
            if (startNode.getCol() == 0) { //Spawn before appearing on path
                startX -= app.CELLSIZE;
            } else if (startNode.getCol() == 19) {
                startX += app.CELLSIZE;
            } else if (startNode.getRow() == 0) {
                startY -= app.CELLSIZE;
            } else if (startNode.getRow() == 19) {
                startY += app.CELLSIZE;
            }

            monster = new Monsters(app, startX, startY, "MonsterType", 100, 100, 2.0f, 1, 10);
            monster.setPath(path);
            int numTicks = 1000;
            for (int i = 0; i < numTicks; i++) {
                monster.tick();
                monster.draw(app);
            }

            assertEquals(true, monster.hasReachedDestination());
        } //Monster has sucessfully reached destination
    }

    @Test
    public void testTickFreezeMonster() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        Node goalNode = app.board.getGoalNode();
        Pathfinding pathfinder = new Pathfinding(app.board.getGrid());
    
        // Get a random start node
        ArrayList<Node> startNodes = app.board.getStartNodes();
        if (!startNodes.isEmpty()) {
            int randomIndex = random.nextInt(startNodes.size());
            Node startNode = startNodes.get(randomIndex);

            int startX = startNode.getCol() * app.CELLSIZE;
            int startY = startNode.getRow() * app.CELLSIZE;
                
            List<Node> path = pathfinder.findPath(startNode, goalNode);
            if (startNode.getCol() == 0) { //Spawn before appearing on path
                startX -= app.CELLSIZE;
            } else if (startNode.getCol() == 19) {
                startX += app.CELLSIZE;
            } else if (startNode.getRow() == 0) {
                startY -= app.CELLSIZE;
            } else if (startNode.getRow() == 19) {
                startY += app.CELLSIZE;
            }

            Monsters monster = new Monsters(app, startX, startY, "MonsterType", 100, 100, 2.0f, 1, 10);
            monster.setPath(path);
            // Number of ticks to perform
            int numTicks = 10000;
            monster.takeFreeze(5);
            assertEquals(true, monster.isFrozen()); //Monster is frozen
            for (int i = 0; i < numTicks; i++) {
                monster.tick();
                monster.draw(app);
            }
            assertEquals(false, monster.isFrozen()); //Monster is no longer frozen
        }
    }

    @Test
    public void testStartCoordinates() {
        App app = new App();
        Monsters monster = new Monsters(app, 100, 200, "MonsterType", 100, 100, 2.0f, 1, 10);

        assertEquals(100, monster.getstartX());
        assertEquals(200, monster.getstartY());
    }

    @Test
    public void testTileOffset() {
        App app = new App();
        Monsters monster = new Monsters(app, 100, 200, "MonsterType", 100, 100, 2.0f, 1, 10);

        float x_offset = 100 + 6;
        float y_offset = 200 + 46;

        assertEquals(x_offset, monster.getX());
        assertEquals(y_offset, monster.getY());
    }

    @Test
    public void testTakeDamage() {
        App app = new App();
        Monsters monster = new Monsters(app, 100, 200, "MonsterType", 100, 100, 2.0f, 1, 10);
        
        monster.takeDamage(20);
        assertEquals(80.0, monster.getHp());
        monster.takeDamage(20);
        assertEquals(60.0, monster.getHp());
        monster.takeDamage(20);
        assertEquals(40.0, monster.getHp());
        monster.takeDamage(20);
        assertEquals(20.0, monster.getHp());
        monster.takeDamage(20);
        assertEquals(true, monster.getDying());
    }

    @Test
    public void testTakeFreeze() {
        App app = new App();
        Monsters monster = new Monsters(app, 100, 200, "gremlin", 100, 100, 2.0f, 0.5, 10);
        
        assertFalse(monster.isFrozen());
        monster.takeFreeze(3.0f);
        monster.takeFreeze(3.0f);
        assertTrue(monster.isFrozen());
    }

    @Test
    public void testMonsterDie() {
        App app = new App();
        Monsters monster = new Monsters(app, 100, 200, "MonsterType", 100, 100, 2.0f, 1, 10);

        monster.takeDamage(100); // Damage greater than remaining HP
        assertEquals(0, monster.getHp());
        assertTrue(monster.getDying());
        assertEquals(0.0f, monster.getSpeed());
    }

    @Test
    public void testMonsterDefense() {
        App app = new App();
        Monsters monster = new Monsters(app, 100, 200, "MonsterType", 100, 100, 2.0f, 0.5, 10);

        monster.takeDamage(100); //Damage does same as hp, so expected to die however armour should save it
        assertEquals(50, monster.getHp());
        assertFalse(monster.getDying());
    }

    @Test
    public void testEdgeReachedDestination() { 
        App app = new App();
        Monsters monster = new Monsters(app, 100, 200, "MonsterType", 100, 100, 2.0f, 0.5, 10);
        monster.setPath(null);

        assertEquals(true, monster.hasReachedDestination());
    }

}


