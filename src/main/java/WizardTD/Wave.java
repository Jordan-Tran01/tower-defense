package WizardTD;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class Wave {
    JSONArray wavesArray;
    JSONArray monstersArray;

    private int duration;
    private boolean start_spawning = false; //By default
    private boolean pause_ended = true;

    private ArrayList<Monsters> monsters;

    private Random random = new Random();

    private int currentWaveIndex = 0; // Start with the first wave
    private int overall_wave_quantity = 0; //Equals 0 by default if nothing to be spawned in config
    private Boolean wave_started = false; //Checks if wave started
    private Boolean first_wave = true; //To tell the program that the first wave hasn't started yet, set false when that happens

    List<Node> path;

    public Wave(PApplet app, JSONArray wavesArray) {
        this.wavesArray = wavesArray;
        monsters = new ArrayList<Monsters>();

    }
    
    public int getTotalWaves() {
        return wavesArray.size();
    }

    public void tickWave(PApplet app) { //So add an implementation in App class so that it doesn't call this twice unless wave ended.
        if (!wave_started) {
            wave_started = true;
            
            JSONObject wave = wavesArray.getJSONObject(currentWaveIndex);
            JSONArray monstersArray = wave.getJSONArray("monsters");
            
            for (int j = 0; j < monstersArray.size(); j++) {
                JSONObject monsterData = monstersArray.getJSONObject(j);
                duration = wave.getInt("duration");
                String type = monsterData.getString("type");
                int hp = monsterData.getInt("hp");
                float speed = monsterData.getFloat("speed");
                double armour = monsterData.getDouble("armour");
                int manaGainedOnKill = monsterData.getInt("mana_gained_on_kill");
                int quantity = monsterData.getInt("quantity");

                overall_wave_quantity += quantity; //Add to amount 

                for (int k = 0; k < quantity; k++) {
                    // Pathfinding logic
                    Node goalNode = App.board.getGoalNode();
                    Pathfinding pathfinder = new Pathfinding(App.board.getGrid());
                    // Get a random start node
                    ArrayList<Node> startNodes = App.board.getStartNodes();
                    if (!startNodes.isEmpty()) {
                        int randomIndex = random.nextInt(startNodes.size());
                        Node startNode = startNodes.get(randomIndex);
                
                        int startX = startNode.getCol() * App.CELLSIZE;
                        int startY = startNode.getRow() * App.CELLSIZE;
                
                        List<Node> path = pathfinder.findPath(startNode, goalNode);
                        if (startNode.getCol() == 0) { //Spawn before appearing on path
                            startX -= App.CELLSIZE;
                        } else if (startNode.getCol() == 19) {
                            startX += App.CELLSIZE;
                        } else if (startNode.getRow() == 0) {
                            startY -= App.CELLSIZE;
                        } else if (startNode.getRow() == 19) {
                            startY += App.CELLSIZE;
                        }

                    Monsters monster = new Monsters(app, startX, startY, type, hp, hp, speed, armour, manaGainedOnKill);
                    monster.setPath(path);
                    monsters.add(monster);
                }
            }

        }
        }
    }

    public boolean MoreWavesLeft() {
        if (currentWaveIndex != wavesArray.size() - 1) {
            return true;
        } else {
            return false;
        }
    }

    public void nextWave() {
        currentWaveIndex += 1;
    }

    public boolean firstWave() {
        return first_wave;
    }

    public int getWaveIndex() {
        return currentWaveIndex;
    }

    public void setfirstWave() {
        first_wave = false;
    }

    public int getDuration() {
        return duration;
    }

    public void pause_ended(boolean flag) {
        pause_ended = flag;
    }

    public boolean getPauseEnded() {
        return pause_ended;
    }

    public double getPreWavePause(int increment) { //Would pass 1 unless its the first wave
        try {
            JSONObject wave = wavesArray.getJSONObject(currentWaveIndex + increment);
            int pre_wave_pause_test = wave.getInt("pre_wave_pause");
            return pre_wave_pause_test;
        } catch (RuntimeException e) {
            JSONObject wave = wavesArray.getJSONObject(currentWaveIndex);
            int pre_wave_pause_test = wave.getInt("pre_wave_pause");
            return pre_wave_pause_test;
        }
    }

    public double getTimeUntilNextWave() {
        if (first_wave) {
            double time_until_next_wave = getPreWavePause(0);
            return time_until_next_wave;
        } else if (MoreWavesLeft()) {
            double time_until_next_wave = getPreWavePause(1) + getDuration();
            return time_until_next_wave;
        } else {
            return 0;
        }
    }

    public void displayTimer(PApplet app, String waveInfoText) {
            // Display the countdown timer in the top left corner of the screen
            app.textSize(28);
            app.fill(0); // Set text color to white
            app.textAlign(app.LEFT, app.TOP);
            app.text(waveInfoText, 15, 4);
    }

    public int getWaveQuantity() {
        return overall_wave_quantity;
    }

    public void setWaveQuantity(int reset) {
        overall_wave_quantity = 0;
    }

    public boolean getWaveStarted() {
        return wave_started;
    }

    public void setWaveStarted(boolean reset) { //Only call when all the monsters has spawned and duration has finished
        wave_started = reset;
    }

    public void setStartSpawning(boolean flag) {
        start_spawning = flag;
    }

    public boolean getstartSpawning() {
        return start_spawning;
    }

    public double getSpawnDelay() {
        double spawn_delay = (double) duration / overall_wave_quantity;
        return spawn_delay;
    }

    public void spawnMonsters(List<Monsters> enemies) {
        if ( monsters.size() > 0) { //Checks if theres still monsters left to print out for the wave
            enemies.add(monsters.get(0));
            monsters.remove(0);
        }      
    }
}


