package WizardTD;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import processing.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;
import processing.core.PFont;
import processing.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class AppTest {
    private Random random = new Random();


    @Test
    public void testSetup() {
        App app = new App();
        app.configPath = "/Users/jordantran/Desktop/java_game/config.json";
        // Set up the Processing sketch and run it
        App.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        app.initalise_map();
        app.generate_buttons();
        app.initalise_entities();
        assert(true); // Replace with your actual assertions
    }

    @Test
    public void testKeyPressed_t() {
        App app = new App();
        
        // Set up the Processing sketch
        App.runSketch(new String[] { "App" }, app); 
        // Initialize towerButton here
        app.towerButton = new Button(app, 650, 170, 50, 50, "T", "Build tower");
        // Manually call the key press event with 't'
        app.key = 't';
        app.keyPressed();
        
        // Check if the towerButton is pressed
        assertTrue(app.towerButton.getPressed());

        app.key = 't';
        app.keyPressed();
        
        assertFalse(app.towerButton.getPressed());

        app.key = 'T';
        app.keyPressed();

        // Check if the towerButton is pressed
        assertTrue(app.towerButton.getPressed());

        app.key = 'T';
        app.keyPressed();

        // Check if the towerButton is pressed
        assertFalse(app.towerButton.getPressed());
    }

    @Test
    public void testKeyPressed_i() {
        App app = new App();
        
        // Set up the Processing sketch
        App.runSketch(new String[] { "App" }, app); 
        // Initialize towerButton here
        app.icetowerButton = new Button(app, 650, 170, 50, 50, "I", "Build ice tower");
        // Manually call the key press event with 't'
        app.key = 'i';
        app.keyPressed();
        
        // Check if the towerButton is pressed
        assertTrue(app.icetowerButton.getPressed());

        app.key = 'i';
        app.keyPressed();
        
        assertFalse(app.icetowerButton.getPressed());

        app.key = 'I';
        app.keyPressed();

        // Check if the towerButton is pressed
        assertTrue(app.icetowerButton.getPressed());

        app.key = 'I';
        app.keyPressed();

        // Check if the towerButton is pressed
        assertFalse(app.icetowerButton.getPressed());
    }

    @Test
    public void testKeyPressed_f() {
        App app = new App();
        
        // Set up the Processing sketch
        App.runSketch(new String[] { "App" }, app); 
        // Initialize towerButton here
        app.fastButton = new Button(app, 650, 170, 50, 50, "FF", "Fast foward");
        // Manually call the key press event with 't'
        app.key = 'f';
        app.keyPressed();
        
        // Check if the towerButton is pressed
        assertTrue(app.fastButton.getPressed());

        app.key = 'f';
        app.keyPressed();
        
        assertFalse(app.fastButton.getPressed());

        app.key = 'F';
        app.keyPressed();

        // Check if the towerButton is pressed
        assertTrue(app.fastButton.getPressed());

        app.key = 'F';
        app.keyPressed();

        // Check if the towerButton is pressed
        assertFalse(app.fastButton.getPressed());
    }

    @Test
    public void testKeyPressed_p() {
        App app = new App();
        
        // Set up the Processing sketch
        App.runSketch(new String[] { "App" }, app); 
        // Initialize pauseButton here
        app.pauseButton = new Button(app, 650, 170, 50, 50, "PP", "Pause");
        // Manually call the key press event with 'p'
        app.key = 'p';
        app.keyPressed();
        
        // Check if the pauseButton is pressed
        assertTrue(app.pauseButton.getPressed());
    
        app.key = 'p';
        app.keyPressed();
        
        assertFalse(app.pauseButton.getPressed());
    
        app.key = 'P';
        app.keyPressed();
    
        // Check if the pauseButton is pressed
        assertTrue(app.pauseButton.getPressed());
    
        app.key = 'P';
        app.keyPressed();
    
        // Check if the pauseButton is pressed
        assertFalse(app.pauseButton.getPressed());
    }

    @Test
    public void testKeyPressed_1() {
        App app = new App();
        
        // Set up the Processing sketch
        App.runSketch(new String[] { "App" }, app); 
        // Initialize pauseButton here
        app.rangeButton = new Button(app, 650, 170, 50, 50, "U1", "Upgrade Range");
        // Manually call the key press event with 'p'
        app.key = '1';
        app.keyPressed();
        
        // Check if the pauseButton is pressed
        assertTrue(app.rangeButton.getPressed());
    
        app.key = '1';
        app.keyPressed();
        
        assertFalse(app.rangeButton.getPressed());
    }
    @Test
    public void testKeyPressed_2() {
        App app = new App();
        
        // Set up the Processing sketch
        App.runSketch(new String[] { "App" }, app); 
        // Initialize speedButton here
        app.speedButton = new Button(app, 650, 170, 50, 50, "U2", "Upgrade Speed");
        // Manually call the key press event with '2'
        app.key = '2';
        app.keyPressed();
        
        // Check if the speedButton is pressed
        assertTrue(app.speedButton.getPressed());
    
        app.key = '2';
        app.keyPressed();
        
        assertFalse(app.speedButton.getPressed());
    }

    @Test
    public void testKeyPressed_3() {
        App app = new App();
        
        App.runSketch(new String[] { "App" }, app); 
        // Initialize damageButton here
        app.damageButton = new Button(app, 650, 170, 50, 50, "U3", "Upgrade Damage");
        // Manually call the key press event with '3'
        app.key = '3';
        app.keyPressed();
        
        // Check if the damageButton is pressed
        assertTrue(app.damageButton.getPressed());
    
        app.key = '3';
        app.keyPressed();
        
        assertFalse(app.damageButton.getPressed());
    }

    @Test
    public void testKeyPressed_M() {
        App app = new App();
        
        // Initialize damageButton here
        app.mana = 200;
        app.mana_pool_spell_initial_cost = 150;
        app.mana_cap = 1000;
        app.mana_pool_spell_cap_multiplier = 1.5;
        app.mana_pool_spell_cost_increase_per_use = 150;
        app.mana_kill_multiplier = 1;
        app.mana_pool_spell_mana_gained_multiplier = 1.1;

        app.manaButton = new Button(app, 650, 170, 50, 50, "M", "Mana Pool cost");
        app.key = 'm';
        app.keyPressed();
        assertTrue(app.manaButton.getPressed());
        assertEquals(50, app.mana);
        app.key = 'M';
        app.keyPressed();
        assertTrue(app.manaButton.getPressed());
        assertEquals(50, app.mana);
    }

    @Test
    void testAppFindEnemy() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        Tower tower = new FreezeTower(app, 0, 0, app.towerImgs);
        Monsters monster = new Monsters(app, 10, 10, "MonsterType", 100, 100, 2.0f, 1, 10);
        List<Tower> towers = new ArrayList<>(); 
        List<Fireball> activeFireballs = new ArrayList<>();
        List<Monsters> enemies = new ArrayList<>();

        enemies.add(monster);

        app.findEnemy();
    }

    @Test 
    public void testTowerToggleUpgrades() {
        App app = new App();
        App.runSketch(new String[] { "App" }, app); 
        // Initialize damageButton here
        app.mana = 1000;
        app.mana_pool_spell_initial_cost = 150;
        app.mana_cap = 1000;
        app.mana_pool_spell_cap_multiplier = 1.5;
        app.mana_pool_spell_cost_increase_per_use = 150;
        app.mana_kill_multiplier = 1;
        app.mana_pool_spell_mana_gained_multiplier = 1.1;
        app.app_loadImages();
        app.generate_buttons();
        app.initalise_map();
        app.rangeButton.setPressed(true);
        app.speedButton.setPressed(true);
        app.damageButton.setPressed(true);
        app.towerButton.setPressed(true);

        app.HandleTowerPlacement(0, 85);
        assert(true);
        app.HandleTowerPlacement(0, 85);

        app.towerButton.setPressed(false);
        app.icetowerButton.setPressed(true);
        app.HandleTowerPlacement(48, 313);
        assert(true);
        app.HandleTowerPlacement(48, 313);
        assert(true);
        app.mana = 90;
        app.towerButton.setPressed(true);
        app.HandleTowerPlacement(0, 125);
        assert(true);
        app.towerButton.setPressed(false);
        app.HandleTowerPlacement(0, 125);
        assert(true);
        }

    @Test 
    public void testNoManaTowerToggleUpgrades() {
        App app = new App();
        App.runSketch(new String[] { "App" }, app); 
        // Initialize damageButton here
        app.mana = 200;
        app.mana_pool_spell_initial_cost = 150;
        app.mana_cap = 1000;
        app.mana_pool_spell_cap_multiplier = 1.5;
        app.mana_pool_spell_cost_increase_per_use = 150;
        app.mana_kill_multiplier = 1;
        app.mana_pool_spell_mana_gained_multiplier = 1.1;
        app.app_loadImages();
        app.generate_buttons();
        app.initalise_map();
        app.rangeButton.setPressed(true);
        app.speedButton.setPressed(true);
        app.damageButton.setPressed(true);
        app.towerButton.setPressed(true);

        app.HandleTowerPlacement(0, 85);
        assert(true);
        app.HandleTowerPlacement(0, 85);

        app.towerButton.setPressed(false);
        app.icetowerButton.setPressed(true);
        app.HandleTowerPlacement(48, 313);
        assert(true);
        app.HandleTowerPlacement(48, 313);
        assert(true);
        app.mana = 90;
        app.towerButton.setPressed(true);
        app.HandleTowerPlacement(0, 125);
        assert(true);
        app.towerButton.setPressed(false);
        app.HandleTowerPlacement(0, 125);
        assert(true);
        }

   @Test 
    public void testActionButtonsMouse() {
        App app = new App();
        App.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.generate_buttons();
        app.mouseX = 650;
        app.mouseY = 50;
        app.MousePressActionButtons();
        app.fastButton.setPressed(true);
        app.MousePressActionButtons();
        app.mouseX = 650;
        app.mouseY = 410;
        app.MousePressActionButtons();
        app.mana = 100;
        app.mana_pool_spell_initial_cost = 150;
        app.MousePressActionButtons();
        app.mouseX = 0;
        app.mouseY = 0;
        app.MousePressActionButtons();

        assert(true);
    }

    @Test
    public void testKeyReleased() {
        App app = new App();
        App.runSketch(new String[] { "App" }, app); 
        app.generate_buttons();
        app.key = 'm';
        app.manaButton.setPressed(true);
        app.keyReleased();
        assertEquals(false, app.manaButton.getPressed());
        app.key = 'M';
        app.manaButton.setPressed(true);
        app.keyReleased();
        assertEquals(false, app.manaButton.getPressed());
        app.key = 's';
        app.manaButton.setPressed(true);
        app.keyReleased();
        assertEquals(true, app.manaButton.getPressed());
    }

    @Test
    public void testDrawObjects() {
        App app = new App();
        App.runSketch(new String[] { "App" }, app); 
        app.generate_buttons();
        app.app_loadImages();
        app.load_json();
        app.initalise_map();
        app.initalise_entities();

        Tower tower = new Tower(app, 5, 0, app.towerImgs);
        app.towers.add(tower);

        Node goalNode = app.board.getGoalNode();
        Pathfinding pathfinder = new Pathfinding(app.board.getGrid());
        
        Monsters monster;
        // Get a random start node
        ArrayList<Node> startNodes = app.board.getStartNodes();
        int randomIndex = app.random.nextInt(startNodes.size());
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
        app.enemies.add(monster);

        Fireball fireball = new Fireball(0, 0, monster, 0, app.fireballImg);
        app.activeFireballs.add(fireball);
        
        app.drawObjects();
        assert(true);
        app.rangeButton.setPressed(true);
        app.speedButton.setPressed(true);
        app.damageButton.setPressed(true);
        app.drawObjects();

        assert(true); //All entities were drawn onto sketch without any problems
    }

    @Test
    public void testIterateEnemies() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        app.app_loadImages();
        app.load_json();
        app.initalise_entities();
        app.initalise_map();

        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level1.txt");
        board.findStartAndGoalNodes();
        Node goalNode = board.getGoalNode();
        Pathfinding pathfinder = new Pathfinding(board.getGrid());
    
        // Get a random start node
        ArrayList<Node> startNodes = board.getStartNodes();
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
            Monsters monster1 = new Monsters(app, startX, startY, "MonsterType", 100, 100, 2.0f, 1, 10);

            monster.setPath(path);
            app.enemies.add(monster);
            app.enemies.add(monster1);
        // Number of ticks to perform
        int numTicks = 100000;

        for (int i = 0; i < numTicks; i++) {
            app.iterate_enemies();
        }
        
        }

        assert(true);
    }

    @Test
    public void testFindEnemy() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        app.app_loadImages();
        app.load_json();
        app.initalise_entities();
        app.initalise_map();

        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level1.txt");
        board.findStartAndGoalNodes();
        Node goalNode = board.getGoalNode();
        Pathfinding pathfinder = new Pathfinding(board.getGrid());
    
        // Get a random start node
        ArrayList<Node> startNodes = board.getStartNodes();
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

            Monsters monster = new Monsters(app, startX, startY, "MonsterType", 1000, 100, 1.0f, 1, 10);
            Monsters monster1 = new Monsters(app, startX, startY, "MonsterType", 1000, 100, 1.0f, 1, 10);

            monster.setPath(path);
            app.enemies.add(monster);
            app.enemies.add(monster1);
        // Number of ticks to perform
        int numTicks = 100000;
        Tower tower = new Tower(app, 3, 4, app.towerImgs);
        Tower freezeTower = new FreezeTower(app, 5, 6, app.icetowerImgs);
        tower.upgradeRange();
        tower.upgradeRange();
        tower.upgradeSpeed();
        tower.upgradeSpeed();
        app.towers.add(tower);
        app.towers.add(freezeTower);
        for (int i = 0; i < numTicks; i++) {
            app.iterate_enemies();
            app.findEnemy();
            app.iterate_fireballs();
        }
        
        }
        assert(true);
    }

    @Test
    public void testIterateFireballs() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        app.app_loadImages();
        app.load_json();
        app.initalise_entities();
        app.initalise_map();

        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level1.txt");
        board.findStartAndGoalNodes();
        Node goalNode = board.getGoalNode();
        Pathfinding pathfinder = new Pathfinding(board.getGrid());
    
        // Get a random start node
        ArrayList<Node> startNodes = board.getStartNodes();
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
            Fireball fireball = new Fireball(999, 999, monster, 1000f, app.fireballImg);
            app.activeFireballs.add(fireball);
            int tick = 1000;
            for (int i = 0; i < tick; i++) {
                monster.tick();
                app.iterate_fireballs();
            }
            assert(true);

            Monsters monster1 = new Monsters(app, startX, startY, "MonsterType", 100, 100, 2.0f, 1, 10);
            monster1.setPath(path);
            app.enemies.add(monster1);
            Monsters monster2 = new Monsters(app, startX, startY, "MonsterType", 100, 100, 2.0f, 1, 10);
            monster2.setPath(path);
            app.enemies.add(monster2);
            Monsters monster3 = new Monsters(app, startX, startY, "MonsterType", 100, 100, 2.0f, 1, 10);
            monster3.setPath(path);
            app.enemies.add(monster3);
            Snowball snowball = new Snowball(999, 999, monster, 0, app.snowballImg, 100, 5);
            app.activeFireballs.add(snowball);
            int tick_2 = 1000;
                for (int i = 0; i < tick; i++) {
                    for (Monsters enemy : app.enemies) {
                        enemy.tick();
                    }
                    app.iterate_fireballs();
                }
            
            assert(true);
        // Number of ticks to perform
    }
    }

    @Test
    public void testHandleEvents() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.generate_buttons();
        // Initially, the button is not pressed
        assertFalse(app.pauseButton.pressed);
        assertFalse(app.pauseButton.wasMousePressed);

        app.mouseX = 650;
        app.mouseY = 110;
        app.pauseButton.handleEvents();
        app.pauseButton.setPressed(true);
        app.pauseButton.handleEvents();

    }

    @Test
    public void testButtonProperties() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();

        app.towerButton.button_properties();
        app.towerButton.setPressed(true);
        app.towerButton.button_properties();
        app.towerButton.setPressed(false);
        app.mouseX = 650;
        app.mouseY = 170;
        app.towerButton.button_properties();
        app.towerButton.setPressed(true);
        app.towerButton.button_properties();
    }

    @Test
    public void testManaBar() {
        App app = new App();
        PApplet.runSketch(new String[] { "App" }, app);
        app.setup();
        app.generate_buttons();
        app.load_json();
        app.displayManaBar();
        // Delay to allow the sketch to run and display its content (adjust as needed)
    }
}
