package WizardTD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Iterator; // Import the Iterator class

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.event.MouseEvent;


public class App extends PApplet {

    public static final int CELLSIZE = 32;
    public static final int SIDEBAR = 120;
    public static final int TOPBAR = 40;
    public static final int BOARD_WIDTH = 20;
    public static final int BOARD_HEIGHT = 20;
    public static int WIDTH = CELLSIZE*BOARD_WIDTH+SIDEBAR;
    public static int HEIGHT = BOARD_WIDTH*CELLSIZE+TOPBAR;
    public static int FPS = 60;

    static JSONObject json;
    public static Board board;
    Tile[][] grid;

    public String configPath;

    public Random random = new Random();

    //GUI INITALISATION
    Button fastButton;
    Button pauseButton;
    Button manaButton;

    //TOWER Buttons
    Button towerButton;
    static Button rangeButton;
    static Button speedButton;
    static Button damageButton;
    Button icetowerButton;

    static double tower_cost;

    private float ManaBarWidth = 300;
    private float ManaBarHeight = 25;
    private float ManaBarX = 430;
    private float ManaBarY = 8;

    float mana; // Setting default initial mana here but will change later in setup
    float mana_cap;
    float mana_gained_per_sec;

    int intMana;
    int intManaCap;
    float textX;
    float textY;

    static double mana_pool_spell_initial_cost;
    double mana_pool_spell_cost_increase_per_use;
    double mana_pool_spell_cap_multiplier;
    double mana_pool_spell_mana_gained_multiplier;
    double mana_kill_multiplier;

    private double pauseTimer;
    private int frameCounter;
    private int frameCounter_2;
    private int elapsedTime;
    private int wave_duration; //Define a variable to keep track of wave duration
    private double next_pre_wave_pause; //Keep track of the next pre_wave_pause

    public ArrayList<Button> buttons = new ArrayList<Button>();
    public List<Tower> towers = new ArrayList<>();
    public List<Fireball> activeFireballs = new ArrayList<>();
    public List<Monsters> enemies = new ArrayList<>();
    private Iterator<Monsters> enemyIterator;
    private List<Monsters> respawnedEnemies;
    private Iterator<Monsters> banished_monster_iterator;

    public Monsters banished_monster;
    public int spawnedMonsters; // Keep track of the number of spawned monsters
    public double spawnTime;

    public String waveInfoText; // Text to display wave info
    public double timeUntilNextWave; // Countdown time for the upcoming wave
    public int countdownTimer;
    public int wave_number;
    public boolean timer_call;
    public Boolean lose;
    public boolean win;
    public PImage towerImg;
    public PImage[] towerImgs;
    public PImage[] icetowerImgs;
    public static PImage gremlinImg;
    public static PImage frozengremlinImg;
    public static PImage beetleImg;
    public static PImage wormImg;
    public PImage fireballImg;
    public PImage snowballImg;
    static PImage[] deathFrames; // Array of death animation frames
    public PImage frozen_gremlin;
    public PImage frozen_beetle;
    public PImage frozen_worm;
    public static PImage grassImg;
    public static PImage shrubImg;
    public static PImage path0Img;
    public static PImage path1Img;
    public static PImage path2Img;
    public static PImage path3Img;
    public static PImage wizardImg;

    public Wave wave;
    //Starting from here these are all setup variables
    public static float fast_forward_modifier;
    
    //Font variables
    public PFont endFont;
    public PFont globalFont;


    public App() {
        this.configPath = "config.json";
    }

    /**
     * Initialise the setting of the window size.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
        noSmooth();
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player, enemies and map elements.
     */

	@Override
    public void setup() {
        frameRate(FPS);

        //Game setup variables:
        fast_forward_modifier = 1;
        waveInfoText = "";
        wave_number = 0;
        timer_call = true; //To call time until first wave by default
        spawnedMonsters = 0;
        lose = false;
        win = false;
        pauseTimer = 0; //To track how long game has been paused
        frameCounter = 0; //To count the frames passed
        frameCounter_2 = 0;
        elapsedTime = 0;
        spawnTime = 0;

        mana_kill_multiplier = 1;

        load_json();

        initalise_map();

        generate_buttons();

        initalise_entities();

        wave.tickWave(this); //Populates enemies full of monsters for current wave. Only updates when current wave duration finishes
    }

    public void initalise_map() {
        grid = new Tile[20][20];

        app_loadImages();

        board = new Board(WIDTH, HEIGHT, json.getString("layout"));
        board.findStartCoordinates();
        board.findStartAndGoalNodes();
        boolean[][] clickableGrid = board.getClickableGrid();
        //GRID MAP DETAILS START
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 20; y++) {
                if (clickableGrid[y][x] == true) {
                    grid[x][y] = new Tile(true); // Grass tile (clickable)
                } else {
                    grid[x][y] = new Tile(false); // Non-grass tile (not clickable)
                }
            }
        }
    }

    public void initalise_entities() {
        towers = new ArrayList<>();
        enemies = new ArrayList<>();
        activeFireballs = new ArrayList<>();
        respawnedEnemies = new ArrayList<>();

        wave = new Wave(this, json.getJSONArray("waves"));
    }

    public void load_json() {
        json = loadJSONObject("config.json");
        //All public/config variables here
        mana = json.getFloat("initial_mana");
        mana_cap = json.getFloat("initial_mana_cap");
        mana_gained_per_sec = json.getFloat("initial_mana_gained_per_second");
        tower_cost = json.getFloat("tower_cost");
        mana_pool_spell_initial_cost = json.getDouble("mana_pool_spell_initial_cost");
        mana_pool_spell_cost_increase_per_use = json.getDouble("mana_pool_spell_cost_increase_per_use");
        mana_pool_spell_cap_multiplier = json.getDouble("mana_pool_spell_cap_multiplier");
        mana_pool_spell_mana_gained_multiplier = json.getDouble("mana_pool_spell_mana_gained_multiplier");
    }

    public void generate_buttons() {
        //Generate all board details START
        fastButton = new Button(this, 650, 50, 50, 50, "FF", "2x speed");
        pauseButton = new Button(this, 650, 110, 50, 50, "P", "PAUSE");
        towerButton = new Button(this, 650, 170, 50, 50, "T", "Build tower");
        rangeButton = new Button(this, 650, 230, 50, 50, "U1", "Upgrade range");
        speedButton = new Button(this, 650, 290, 50, 50, "U2", "Upgrade speed");
        damageButton = new Button(this, 650, 350, 50, 50, "U3", "Upgrade damage");
        manaButton = new Button(this, 650, 410, 50, 50, "M", "Mana pool cost: 100");
        icetowerButton = new Button(this, 650, 470, 50, 50, "I", "Build ice tower");

        // Add the buttons to the list 
        buttons = new ArrayList<Button>();
        buttons.add(fastButton);
        buttons.add(pauseButton);
        buttons.add(towerButton);
        buttons.add(rangeButton);
        buttons.add(speedButton);
        buttons.add(damageButton);
        buttons.add(manaButton);
        buttons.add(icetowerButton);
    }
    
    public void app_loadImages() {
        // Load image within the Board class using the provided PApplet instance
        grassImg = loadImage("src/main/resources/WizardTD/grass.png");
        shrubImg = loadImage("src/main/resources/WizardTD/shrub.png");
        path0Img = loadImage("src/main/resources/WizardTD/path0.png");
        path1Img = loadImage("src/main/resources/WizardTD/path1.png");
        path2Img = loadImage("src/main/resources/WizardTD/path2.png");
        path3Img = loadImage("src/main/resources/WizardTD/path3.png");
        wizardImg = loadImage("src/main/resources/WizardTD/wizard_house.png");

        gremlinImg = loadImage("src/main/resources/WizardTD/gremlin.png");
        beetleImg = loadImage("src/main/resources/WizardTD/beetle.png");
        wormImg = loadImage("src/main/resources/WizardTD/worm.png");
        frozengremlinImg = loadImage("src/main/resources/WizardTD/gremlin0.png");
        deathFrames = new PImage[5];
        for (int i = 1; i < deathFrames.length; i++) {
            deathFrames[i] = loadImage("src/main/resources/WizardTD/gremlin" + i + ".png");
        }

        fireballImg = loadImage("src/main/resources/WizardTD/fireball.png");
        snowballImg = loadImage("src/main/resources/WizardTD/snowball.png");

        towerImgs = new PImage[3];

        for (int i = 0; i < towerImgs.length; i++) {
            towerImgs[i] = loadImage("src/main/resources/WizardTD/tower" + i + ".png");
        }

        icetowerImgs = new PImage[1];
        icetowerImgs[0] = loadImage("src/main/resources/WizardTD/tower_ice.png");
    }
    

    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(){
        if (key == 't' || key == 'T') {
            // Your code to handle the 't' key press goes here
            if (towerButton.getPressed() == false) {
                towerButton.setPressed(true);
            } else {
                towerButton.setPressed(false);
            }
        }

        if (key == 'i' || key == 'I') {
            // Your code to handle the 't' key press goes here
            if (icetowerButton.getPressed() == false) {
                icetowerButton.setPressed(true);
            } else {
                icetowerButton.setPressed(false);
            }
        }

        if (key == 'f' || key == 'F') {
            if (!fastButton.getPressed()) {
              fastButton.setPressed(true);
              fast_forward_modifier = 2;
            } else {
              fastButton.setPressed(false);  
              fast_forward_modifier = 1;
            }
        } else if (key == 'p' || key == 'P') {
            if (!pauseButton.getPressed()) {
              pauseButton.setPressed(true);
            } else {
              pauseButton.setPressed(false);
            }
        }

        if (key == '1') {
            if (!rangeButton.getPressed()){
                rangeButton.setPressed(true);
            } else if (rangeButton.getPressed()) {
                rangeButton.setPressed(false);
            }
        }

        if (key == '2') {
            if (!speedButton.getPressed()){
                speedButton.setPressed(true);
            } else if (speedButton.getPressed()) {
                speedButton.setPressed(false);
            }

        } 

        if (key == '3') {
            if (!damageButton.getPressed()){
                damageButton.setPressed(true);
            } else if (damageButton.getPressed()) {
                damageButton.setPressed(false);
            }
        } 

        if ((key == 'm' || key == 'M') && mana > mana_pool_spell_initial_cost) {
            manaButton.setPressed(true);
            mana -= mana_pool_spell_initial_cost;
            mana_pool_spell_initial_cost += mana_pool_spell_cost_increase_per_use;
            mana_cap = mana_cap * (float) mana_pool_spell_cap_multiplier;
            mana_kill_multiplier = mana_kill_multiplier * mana_pool_spell_mana_gained_multiplier;

            manaButton.updateDescription();
        }
        
        if ((key == 'r' || key == 'R') && lose == true) {
            lose = false;
            setup();
        }
    }
    

    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(){
        if (key == 'm' || key == 'M') {
            manaButton.setPressed(false);
        }
    }

    public void mousePressed(MouseEvent e) {
        if (mouseX <= 640 && mouseY >= 40) {
            HandleTowerPlacement(mouseX, mouseY);
        }

        MousePressActionButtons();
    }

    public void MousePressActionButtons() {
        if (!fastButton.getPressed() && fastButton.isMouseOver()) {
            fast_forward_modifier = 2;
        } else if (fastButton.isMouseOver()) {
            fast_forward_modifier = 1;
        }

        if ((!manaButton.getPressed() && manaButton.isMouseOver()) && mana > mana_pool_spell_initial_cost) {
            mana -= mana_pool_spell_initial_cost;
            mana_pool_spell_initial_cost += mana_pool_spell_cost_increase_per_use;
            mana_cap = mana_cap * (float) mana_pool_spell_cap_multiplier;
            mana_kill_multiplier = mana_kill_multiplier * mana_pool_spell_mana_gained_multiplier;
            manaButton.updateDescription();
        }
    }

    public void HandleTowerPlacement(int mouseX, int mouseY) {
        // Check if the click is within the boundaries of the grid and take action accordingly.
        int adjustedMouseX = mouseX;
        int adjustedMouseY = mouseY - 40;
        // Converts mouseX and Y coordinates into their corresponding grid cell
        int gridX = (adjustedMouseX) / 32;
        int gridY = (adjustedMouseY) / 32;
        if (towerButton.getPressed() == true) {
            if (grid[gridX][gridY].isClickable() == true && grid[gridX][gridY].isOccupied() == false && mana > tower_cost) {
                mana -= tower_cost;
                Tower newTower = new Tower(this, gridX, gridY, towerImgs);
                    
                // Add the tower to the list of towers
                towers.add(newTower);
                    
                // Mark the grid cell as occupied
                grid[gridX][gridY].setOccupied(true);
                grid[gridX][gridY].setTower(newTower);
            }
        } else if (icetowerButton.getPressed() == true) {
            if (grid[gridX][gridY].isClickable() == true && grid[gridX][gridY].isOccupied() == false && mana > tower_cost) {
                mana -= tower_cost;
                FreezeTower newTower = new FreezeTower(this, gridX, gridY, icetowerImgs);
                    
                // Add the tower to the list of towers
                towers.add(newTower);
                    
                // Mark the grid cell as occupied
                grid[gridX][gridY].setOccupied(true);
                grid[gridX][gridY].setTower(newTower);
            }
        }

        Tower targetTower = grid[gridX][gridY].getTower();;
        boolean default_tower = true;
        if (grid[gridX][gridY].getTower() instanceof FreezeTower) {
            default_tower = false;
        }

        if (rangeButton.getPressed()&& grid[gridX][gridY].isOccupied() && default_tower) {
            int mana_cost = targetTower.UpgradeRangeCost();
            if (mana_cost < mana) {
                mana -= mana_cost;
                targetTower.upgradeRange();
                targetTower.updateTowerImage();
            }
        } 

        if (speedButton.getPressed() && grid[gridX][gridY].isOccupied() && default_tower) {
            int mana_cost = targetTower.UpgradeSpeedCost();
            if (mana_cost < mana) {
                mana -= mana_cost;
                targetTower.upgradeSpeed();
                targetTower.updateTowerImage();
            }
        }

        if (damageButton.getPressed() && grid[gridX][gridY].isOccupied() && default_tower) {
            int mana_cost = targetTower.UpgradeDamageCost();
            if (mana_cost < mana) {
                mana -= mana_cost;
                targetTower.upgradeDamage();
                targetTower.updateTowerImage();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (manaButton.getPressed()) {
            // Toggle the pressed state when the mouse is released
            manaButton.setPressed(false);
        }
    }

    /*@Override
    public void mouseDragged(MouseEvent e) {

    }*/

    /**
     * Draw all elements in the game by current frame.
     */

	@Override
    public void draw() {
        //Real-Time Loop start (Loop sets the game in motion i.e. pauseable stuff)
        if (pauseButton.getPressed() == false && lose == false) {
            wave.tickWave(this); //Populates enemies full of monsters for current wave. Only updates when current wave duration finishes

            updateWaveInfo();

            HandleWaves();

            if (mana < mana_cap) 
                mana += (mana_gained_per_sec * fast_forward_modifier)/60; //Here so that it pauses mana gain but I can draw the mana bar outside

            iterate_enemies();

            findEnemy();

            iterate_fireballs();

            increment_counter();
        }
        //Real-Time Loop END

        drawObjects();

        for (Button button : buttons) {
            button.handleEvents();
            button.handleDisplay();
            button.display();
        }

        displayManaBar();
        textSize(18); // Adjust text size as needed
        textAlign(CENTER, CENTER);
        text(intMana + " / " + intManaCap, textX, textY); // Display mana / mana_cap as integers

        // Display the countdown timer in the top left corner of the screen
        textSize(ManaBarHeight);
        textAlign(LEFT, TOP);
        text("MANA:", ManaBarX - 85, ManaBarY);

        wave.displayTimer(this, waveInfoText);

        if (mana <= 0) {
            GameLost();
        } else if (win) {
            GameWon();
            noLoop();
        }
    }

    public void iterate_fireballs() {
        Iterator<Fireball> fireballIterator = activeFireballs.iterator();
        while (fireballIterator.hasNext()) {
            Fireball projectile = fireballIterator.next();
            projectile.update();
            
            // Check if the fireball has reached its target
            if (projectile.hasReachedTarget()) {
                // Apply damage to the enemy
                if (projectile instanceof Snowball) {
                    projectile.dealHit(enemies);
                } else {
                    projectile.dealHit(null);
                }
                // Remove the fireball from the list
                fireballIterator.remove();
            }
        }
    }

    public void iterate_enemies() {
        enemyIterator = enemies.iterator();
        respawnedEnemies = new ArrayList<>(); // List to hold enemies to be respawned
            
        while (enemyIterator.hasNext()) {
            Monsters enemy = enemyIterator.next();

            if (!enemy.isDead() && !enemy.hasReachedDestination()) {
                enemy.tick();
            } else {
                if (mana < mana_cap - 50 && enemy.isDead()) {
                    mana += enemy.get_mana_gained_on_killed() * mana_kill_multiplier;
                } else if (mana > (mana_cap - enemy.get_mana_gained_on_killed()) && mana < mana_cap) {
                    mana = mana_cap;
                }
            
                if (enemy.hasReachedDestination()) {
                    if (mana <= enemy.getHp()) {
                        mana = 0;
                    } else {
                        mana -= enemy.getHp();
                    }
                    banished_monster = new Monsters(this, enemy.getstartX(), enemy.getstartY(), enemy.getType(), enemy.getHp(), enemy.getFullHp(), enemy.getSpeed(), enemy.getArmour(), enemy.get_mana_gained_on_killed());
                    respawnedEnemies.add(banished_monster);
                }
            enemyIterator.remove();
            }
        } 
                         
        banished_monster_iterator = respawnedEnemies.iterator();
        //If theres any monsters to be respawned, then add them back into enemies list to be respawned again
        while (banished_monster_iterator.hasNext()) {
            Monsters enemy = banished_monster_iterator.next();
            // Pathfinding logic
            Node goalNode = App.board.getGoalNode();
            Pathfinding pathfinder = new Pathfinding(App.board.getGrid());
                
            // Get a random start node
            ArrayList<Node> startNodes = App.board.getStartNodes();
            if (!startNodes.isEmpty()) {
                int randomIndex = random.nextInt(startNodes.size());
                Node startNode = startNodes.get(randomIndex);
            
                List<Node> path = pathfinder.findPath(startNode, goalNode);
                
            enemy.setPath(path);

            enemies.add(enemy);

            }
        } 
    }

    public void increment_counter() {
        frameCounter += 1 * fast_forward_modifier;
        if (frameCounter > frameRate) {
            elapsedTime++; //Tracks the elapsed time resetting whenever the pre wave pause timer ends
            countdownTimer--; // Decrement the countdown timer by 1 every second (Tracks when the wave spawns)
            frameCounter = 0;
        }
    }

    public void drawObjects() {
        //Draw elements of the game are here because regardless if its paused or not, it should still be drawn on.
        board.draw(this); // Draw the board
        fill(132, 115, 74);
        noStroke();  // Add this line to disable outlines
        rect(0, 0, WIDTH, TOPBAR); //Create topbar background
        rect(WIDTH - SIDEBAR, TOPBAR, SIDEBAR, HEIGHT - TOPBAR); //Create sidebar Background

        for (Tower tower : towers) {
            tower.draw(this);
            if (rangeButton.getPressed()) {
                tower.setRangeToggle(true);
            } else {
                tower.setRangeToggle(false);
            }

            if (speedButton.getPressed()) {
                tower.setSpeedToggle(true);
            } else {
                tower.setSpeedToggle(false);
            }

            if (damageButton.getPressed()) {
                tower.setDamageToggle(true);
            } else {
                tower.setDamageToggle(false);
            }
        }

        for (Monsters enemy : enemies) {
            enemy.draw(this);
        }

        for (Fireball fireball : activeFireballs) {
            fireball.draw(this);
        }

        board.drawWizardTower(this);
    }

    public void HandleWaves() {
            // handle wave info and countdown
            if (wave.getPauseEnded() && timer_call) {
                timeUntilNextWave = wave.getTimeUntilNextWave();
                countdownTimer = (int) timeUntilNextWave;
                timer_call = false;
                wave_number += 1;
                pauseTimer = 0;
                wave_duration = wave.getDuration();
                next_pre_wave_pause = wave.getPreWavePause(1);
            }

            // Check if it's time to start the wave
            if (wave.firstWave()) {
                if (elapsedTime > wave.getPreWavePause(0)) {
                    wave.setStartSpawning(true);
                    wave.setWaveStarted(true);
                    wave.setfirstWave();
                    timer_call = true;
                    elapsedTime = 0;
                }
            } else {
                if ((elapsedTime > next_pre_wave_pause + wave_duration) && !wave.getPauseEnded()) {
                    wave.setStartSpawning(true);
                    wave.pause_ended(true);
                    elapsedTime = 0;
                }
            }

            frameCounter_2 += 1;
            if (frameCounter_2 > frameRate * wave.getSpawnDelay()) {
                spawnTime += (1 * wave.getSpawnDelay());
                frameCounter_2 = 0;
            }
            //Starts spawning the monsters while the wave is active
            if (spawnTime >= wave.getSpawnDelay() && spawnedMonsters < wave.getWaveQuantity() && wave.getstartSpawning()) {
                wave.spawnMonsters(enemies);

                spawnTime = 0;
                spawnedMonsters++; // Increment the count of spawned monsters
            } else if (spawnedMonsters == wave.getWaveQuantity() && wave.MoreWavesLeft()) {
                wave.setWaveStarted(false); //End the previous wave and indicate to start the next
                wave.setStartSpawning(false); //stops the spawning of current wave
                wave.setWaveQuantity(0); //Reset back to currently 0 waves to count again for next
                wave.nextWave(); //Increments wave iteration i.e. goes onto next wave
                wave.pause_ended(false);
                timer_call = true;

                spawnedMonsters = 0;
            } else if (spawnedMonsters == wave.getWaveQuantity() && enemies.size() == 0) {
                win = true;
            }
    }

    public void updateWaveInfo() {
        if (countdownTimer < 0) {
            countdownTimer = 0;
        } 

        if (wave_number < wave.getTotalWaves() + 1) {
            waveInfoText = "Wave " + wave_number + " starts: " + countdownTimer;
        } else {
            waveInfoText = "";
        }
    }

    void GameWon() {
        fill(255, 0, 255);
        
        // Draw the text at the center of the screen
        text("YOU WIN!", width/2 - 150, height/2 - 150);
    }

    void GameLost() {
        lose = true;
        fill(0, 255, 0);
        
        // Draw the text at the center of the screen
        text("YOU LOST", width/2 - 150, height/2 - 150);
        text("Press 'r' to restart", width/2 - 150, height/2 - 100);
    
    }

    void displayManaBar() {
        // Calculate the position for the text (centered within the mana bar)
        textX = ManaBarX + ManaBarWidth / 2;
        textY = ManaBarY + ManaBarHeight / 2;

        float manaPercentage = mana / mana_cap;
        fill(255, 255, 255); //White colour
        rect(ManaBarX, ManaBarY, ManaBarWidth, ManaBarHeight); //draw background of manabar

        fill(0, 200, 255); //Mana Colour
        rect(ManaBarX, ManaBarY, ManaBarWidth * manaPercentage, ManaBarHeight); //Draw the manapool over background

        intMana = (int) mana; // Convert to integers using these variables for display only
        intManaCap = (int) mana_cap; 

        fill(0);
    }
    

    void findEnemy() {
        for (Tower tower : towers) {
            if (tower.getCooldown() > 0) {
                tower.decrementCooldown(); // Decrement the cooldown timer
            }
    
            // Check if the tower is ready to fire (cooldown has expired)
            if (tower.getCooldown() <= 0) {
                boolean hasFired = false; // Flag to track if the tower has fired

                if (tower instanceof FreezeTower) {
                    tower.findEnemy(hasFired, enemies, activeFireballs, snowballImg, false);
                } else {
                    tower.findEnemy(hasFired, enemies, activeFireballs, fireballImg, true);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        PApplet.main("WizardTD.App");
    }

}
