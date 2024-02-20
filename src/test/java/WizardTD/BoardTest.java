package WizardTD;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class BoardTest {

    @Test
    public void TestConstructor() {
        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level1.txt");
        assertNotNull(board);
    }

    @Test
    public void TestfindNodes() {
        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level1.txt");
        board.findStartAndGoalNodes();
        ArrayList<Node> startNodes = board.getStartNodes();
        Node goalNode = board.getGoalNode();
        assertNotNull(startNodes);
        assertNotNull(goalNode);
    }

    @Test
    public void TestDrawBoard1() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level1.txt");
        board.draw(app);
        board.drawWizardTower(app);
        assert(true);
    }
    @Test
    public void TestDrawBoard2() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level2.txt");
        board.draw(app);
        board.drawWizardTower(app);
        assert(true);
    }

    @Test
    public void TestDrawBoard3() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level3.txt");
        board.draw(app);
        board.drawWizardTower(app);
        assert(true);
    }

    @Test
    public void TestDrawBoard4() {
        App app = new App();

        PApplet.runSketch(new String[] { "App" }, app);
        Board board = new Board(760, 680, "/Users/jordantran/Desktop/java_game/level4.txt");
        board.draw(app);
        board.drawWizardTower(app);
        assert(true);
    }

}
