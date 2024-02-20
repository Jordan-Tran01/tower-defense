package WizardTD;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;


public class Board extends PApplet {

    private boolean[][] grid;
    private boolean[][] clickableGrid;
    private PImage grassImg, shrubImg, path0Img, path1Img, path2Img, path3Img, wizardImg;
    private ArrayList<Node> startNodes;
    private Node goalNode;
    private String mapData; // Remove the initialization
    private int GRID_CELL_SIZE = 32;
    int numRows = 20; // Replace with the actual number of rows
    int numCols = 20; // Replace with the actual number of columns
    ArrayList<Float> wizardXPositions = new ArrayList<>();
    ArrayList<Float> wizardYPositions = new ArrayList<>();

    public Board(int width, int height, String layout) {
        this.width = width;
        this.height = height;
        this.grassImg = App.grassImg;
        this.shrubImg = App.shrubImg;
        this.path0Img = App.path0Img;
        this.path1Img = App.path1Img;
        this.path2Img = App.path2Img;
        this.path3Img = App.path3Img;
        this.wizardImg = App.wizardImg;

        grid = new boolean[numRows][numCols];
        clickableGrid = new boolean[numRows][numCols];


        // Read map data from the file
        try (BufferedReader br = new BufferedReader(new FileReader(layout))) {
            mapData = "";
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isEmpty()) {
                    continue;
                }
                mapData += line + "\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Populate the grid during initialization
        String[] lines = mapData.split("\n");
        for (int row = 0; row < numRows; row++) {
            String line = lines[row];
            for (int col = 0; col < numCols; col++) {
                char tile = line.charAt(col);
                if (tile == ' ' || tile == 'S') {
                    grid[row][col] = false;
                    if (tile == ' ' || tile != 'S') {
                       clickableGrid[row][col] = true;
                    } else {
                        clickableGrid[row][col] = false;
                    }
                } else if (tile == 'X' || tile == 'W') {
                    grid[row][col] = true;
                    clickableGrid[row][col] = false;
                }
            }
        }
    }

    public void draw(PApplet app) {
        int numRows = 20;
        int numCols = 20;
        int tileWidth = 32;
        int tileHeight = 32;
        int TOPBAR = 40;

        String[] lines = mapData.split("\n");
        for (int row = 0; row < numRows; row++) {
            String line = lines[row];

            for (int col = 0; col < numCols; col++) {

                float x = col * tileWidth;
                float y = TOPBAR + row * tileHeight;

                char tile = line.charAt(col);
                // Check surrounding characters to determine the path configuration
                boolean left = col > 0 && line.charAt(col - 1) == 'X';
                boolean right = col < numCols - 1 && line.charAt(col + 1) == 'X';
                boolean up = row > 0 && lines[row - 1].charAt(col) == 'X';
                boolean down = row < numRows - 1 && lines[row + 1].charAt(col) == 'X';

                // Map the character to the corresponding image and draw it here.
                if (tile == ' ') {
                    app.image(grassImg, x, y);
                } else if (tile == 'S') {
                    app.image(shrubImg, x, y);
                } else if (tile == 'X') {
                    if (left && right && up && down) {
                        app.image(path3Img, x, y);
                    } else if (left && right  && up) {
                        PImage rotated_image = rotateImageByDegrees(path2Img, 180);
                        app.image(rotated_image, x, y);
                    } else if (left && right && down) {
                        app.image(path2Img, x, y);
                    } else if (left && down && up) {
                        PImage rotated_image = rotateImageByDegrees(path2Img, 90);
                        app.image(rotated_image, x, y);
                    } else if (up && down && right) {
                        PImage rotated_image = rotateImageByDegrees(path2Img, 270);
                        app.image(rotated_image, x, y);
                    } else if (left && right) {
                        app.image(path0Img, x, y);
                    } else if (up && down) {
                        PImage rotated_image = rotateImageByDegrees(path0Img, 90);
                        app.image(rotated_image, x, y);
                    } else if (left && up) {
                        PImage rotated_image = rotateImageByDegrees(path1Img, 90);
                        app.image(rotated_image, x, y);
                    } else if (right && up) {
                        PImage rotated_image = rotateImageByDegrees(path1Img, 180);
                        app.image(rotated_image, x, y);
                    } else if (left && down) {
                        PImage rotated_image = rotateImageByDegrees(path1Img, 0);
                        app.image(rotated_image, x, y);
                    } else if (right && down) {
                        PImage rotated_image = rotateImageByDegrees(path1Img, 270);
                        app.image(rotated_image, x, y);
                    } else if (down) {
                        PImage rotated_image = rotateImageByDegrees(path0Img, 90);
                        app.image(rotated_image, x, y);
                    } else if (up) {
                        PImage rotated_image = rotateImageByDegrees(path0Img, 90);
                        app.image(rotated_image, x, y);
                    } else if (left) {
                        PImage rotated_image = rotateImageByDegrees(path0Img, 0);
                        app.image(rotated_image, x, y);
                    } else if (right) {
                        PImage rotated_image = rotateImageByDegrees(path0Img, 00);
                        app.image(rotated_image, x, y);
                    }
                } else if (tile == 'W') {
                    wizardXPositions.add(x);
                    wizardYPositions.add(y);
                }
                // Add other character-to-image mappings as needed.
            }
        }
    }

    public void drawWizardTower(PApplet app) {
        for (int i = 0; i < wizardXPositions.size(); i++) {
            float wizardX = wizardXPositions.get(i);
            float wizardY = wizardYPositions.get(i);
            app.image(wizardImg, wizardX - 2, wizardY - 10);
        }
    }

    public PImage rotateImageByDegrees(PImage pimg, double angle) {
        BufferedImage img = (BufferedImage) pimg.getNative();
        double rads = Math.toRadians(angle);
        double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
        int w = img.getWidth();
        int h = img.getHeight();
        int newWidth = (int) Math.floor(w * cos + h * sin);
        int newHeight = (int) Math.floor(h * cos + w * sin);

        PImage result = this.createImage(newWidth, newHeight, RGB);
        //BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        BufferedImage rotated = (BufferedImage) result.getNative();
        Graphics2D g2d = rotated.createGraphics();
        AffineTransform at = new AffineTransform();
        at.translate((newWidth - w) / 2, (newHeight - h) / 2);

        int x = w / 2;
        int y = h / 2;

        at.rotate(rads, x, y);
        g2d.setTransform(at);
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        for (int i = 0; i < newWidth; i++) {
            for (int j = 0; j < newHeight; j++) {
                result.set(i, j, rotated.getRGB(i, j));
            }
        }

        return result;
    }

    public boolean[][] getGrid() {
        return grid;
    }

    public boolean[][] getClickableGrid() {
        return clickableGrid;
    }

    public void findStartAndGoalNodes() {
        // Initialize start and goal coordinates based on the map data
        String[] lines = mapData.split("\n");
        int numRows = 20; // Replace with the actual number of rows
        int numCols = 20; // Replace with the actual number of columns

        startNodes = new ArrayList<>(); // Create an ArrayList to store start nodes

        for (int row = 0; row < numRows; row++) {
            String line = lines[row];
            
            // Check the first and last columns for 'X'
            if (line.charAt(0) == 'X') {
                startNodes.add(new Node(row, 0, grid));
            }
            if (line.charAt(line.length() - 1) == 'X') {
                startNodes.add(new Node(row, numCols - 1, grid));
            }
            
            // If it's the first or last row, check for 'X' in all columns
            if (row == 0 || row == numRows - 1) {
                for (int col = 0; col < numCols; col++) {
                    char tile = line.charAt(col);
                    if (tile == 'X') {
                        startNodes.add(new Node(row, col, grid));
                    }
                }
            }
            
            // Check for 'W' and set the goal node
            for (int col = 0; col < numCols; col++) {
                char tile = line.charAt(col);
                if (tile == 'W' && !startNodes.isEmpty()) {
                    goalNode = new Node(row, col, grid);
                }
            }
        }
    }
        
    

    public ArrayList<Node> getStartNodes() {
        return startNodes;
    }

    public Node getGoalNode() {
        return goalNode;
    }
    private int startPixelX; // Store the starting X coordinate in pixels
    private int startPixelY; // Store the starting Y coordinate in pixels

    public void findStartCoordinates() {
        String[] lines = mapData.split("\n");
        int numRows = 20; // Replace with the actual number of rows
        int numCols = 20; // Replace with the actual number of columns
    
        for (int row = 0; row < numRows; row++) {
            String line = lines[row];
            for (int col = 0; col < numCols; col++) {
    
                char tile = line.charAt(col);
    
                // Find the first instance of 'X' and convert its position to pixels
                if (tile == 'X') {
                    startPixelX = col * GRID_CELL_SIZE; // Assuming GRID_CELL_SIZE is 32
                    startPixelY = row * GRID_CELL_SIZE; // Assuming GRID_CELL_SIZE is 32
                    return; // Exit the loop once you've found the start coordinates
                }
            }
        }
    }
    

    public int getStartPixelX() {
        return startPixelX;
    }

    public int getStartPixelY() {
        return startPixelY;
    }

}
