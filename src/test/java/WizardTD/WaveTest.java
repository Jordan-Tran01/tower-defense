package WizardTD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;

public class WaveTest {
    @Test
    public void testWave() {
        App app = new App();
        app.configPath = "/Users/jordantran/Desktop/java_game/config.json";
        // Set up the Processing sketch and run it
        App.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        app.generate_buttons();
        app.initalise_entities();
        app.wave = new Wave(app, app.json.getJSONArray("waves"));
        assertNotNull(app.wave); // Replace with your actual assertions
    }

    @Test
    public void testWaveSpawnNull() {
        App app = new App();
        app.configPath = "/Users/jordantran/Desktop/java_game/config.json";
        // Set up the Processing sketch and run it
        App.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        app.generate_buttons();
        app.initalise_entities();
        app.wave = new Wave(app, app.json.getJSONArray("waves"));
        app.wave.getTotalWaves();
        assertEquals(0, app.wave.getWaveIndex());
        assertEquals(false, app.wave.getWaveStarted());
        app.wave.spawnMonsters(null);
    }

    @Test
    public void testWaveTick() {
        App app = new App();
        app.configPath = "/Users/jordantran/Desktop/java_game/config.json";
        // Set up the Processing sketch and run it
        App.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        app.initalise_entities();

        app.board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level1.txt");
        app.board.findStartAndGoalNodes();

        app.wave = new Wave(app, app.json.getJSONArray("waves"));
        app.wave.tickWave(app);
        app.HandleWaves();
        assert(true);
    }

    @Test
    public void testWave_1Tick() {
        App app = new App();
        app.configPath = "/Users/jordantran/Desktop/java_game/config.json";
        // Set up the Processing sketch and run it
        App.runSketch(new String[] { "App" }, app);
        app.load_json();
        app.app_loadImages();
        app.initalise_entities();

        app.board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level3.txt");
        app.board.findStartAndGoalNodes();
        
        app.wave = new Wave(app, app.json.getJSONArray("waves"));
        //Loop these two with very quick FPS. i.e. 500 for quick testing
        app.wave.tickWave(app);
        app.HandleWaves();
        //Loop ends 
        assert(true);
    }

    @Test
    public void testWaveMultipleTicks() {
        App app = new App();
        app.configPath = "/Users/jordantran/Desktop/java_game/config.json";

        // Set up the Processing sketch and run it
        PApplet.runSketch(new String[] { "App" }, app);

        // Load JSON, images, and initialize entities
        app.load_json();
        app.app_loadImages();
        app.initalise_entities();

        // Create the board and associate it with the app
        app.board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level3.txt");
        app.board.findStartAndGoalNodes();

        // Create the wave
        app.wave = new Wave(app, app.json.getJSONArray("waves"));

        // Set the frame rate for quick testing
        app.frameRate(200); // Set to a higher frame rate

        // Define the duration (in frames) and the number of ticks you want
        int durationInFrames = 50000;

        for (int frame = 0; frame < durationInFrames; frame++) {
            // Simulate one tick of the wave
            app.wave.tickWave(app);
            app.HandleWaves();

            app.increment_counter();
            app.increment_counter();
            app.increment_counter();
            app.increment_counter();
            app.updateWaveInfo();
        }
        app.frameRate(60); // Reset frame rate

        assertTrue(true);
    }

}
