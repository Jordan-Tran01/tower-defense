package WizardTD;

import processing.core.*;

public class Button {
    PApplet parent; // Reference to the main PApplet
    float x, y; // Position of the button
    float w, h; // Width and height of the button
    String label; // Text label on the button
    String description; // Description text
    boolean pressed; // Button state
    boolean wasMousePressed; // Flag to track if the mouse was previously pressed

    Button(PApplet parent, float x, float y, float w, float h, String label, String description) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.label = label;
        this.description = description;
        this.pressed = false;
        this.wasMousePressed = false;
    }

    boolean getPressed() {
        return pressed;
    }

    boolean setPressed(boolean pressed) {
        this.pressed = pressed;
        return pressed;
    }

    // Method to check if the mouse is over the button
    boolean isMouseOver() {
        return parent.mouseX >= x && parent.mouseX <= x + w && parent.mouseY >= y && parent.mouseY <= y + h;
    }

    // Method to handle button events
    void handleEvents() {
        boolean isMousePressed = parent.mousePressed;

        if (isMouseOver() && isMousePressed && !wasMousePressed) {
            pressed = !pressed; // Toggle the pressed state
            // Add any actions you want to perform when the button is clicked here
        }
        wasMousePressed = isMousePressed;
    }

    void handleDisplay() {
        int toggleIncrement = 0;
        if (App.rangeButton.getPressed()) {
            toggleIncrement += 1;
        }
        if (App.speedButton.getPressed()) {
            toggleIncrement += 1;
        }
        if (App.damageButton.getPressed()) {
            toggleIncrement += 1;
        }

        if (isMouseOver() && (label == "T" || label == "I")) {
            parent.textSize(18);
            int cost = (int) App.tower_cost + toggleIncrement * 20;
            String tooltipText = "Cost: " + cost;
            // Display the tooltip
            parent.fill(255, 255, 255);
            parent.rect(x - 100, y, 85, 25); // Tooltip box with 5px rounded corners
            parent.fill(0); // Black text
            parent.text(tooltipText, x - 57, y + 10);
        } else if (isMouseOver() && (label == "M")) {
            parent.textSize(18);
            int cost = (int) App.mana_pool_spell_initial_cost;
            String tooltipText = "Cost: " + cost;
            // Display the tooltip
            parent.fill(255, 255, 255);
            parent.rect(x - 100, y, 85, 25); // Tooltip box with 5px rounded corners
            parent.fill(0); // Black text
            parent.text(tooltipText, x - 57, y + 10);
        }
    }

    void updateDescription() {
        description = "Mana pool cost: " + (int) App.mana_pool_spell_initial_cost;
    }

    void button_properties() {
        parent.stroke(0); // Button border color
        parent.strokeWeight(2);
        if (isMouseOver() && pressed == false) {
            parent.fill(parent.color(192, 192, 192)); // Grey when the mouse is over the button
        } else if (pressed) {
            parent.fill(parent.color(255, 255, 0)); // Yellow when pressed
        } else {
            parent.fill(parent.color(132, 115, 74)); // Default color (black)
        }
        
        parent.rect(x, y, w, h);
        parent.fill(0); // Text color
    }

    // Method to draw the button and description with text wrapping
    void display() {
        button_properties();

        parent.textAlign(parent.CENTER, parent.CENTER);
        parent.textSize(32);

        // Display the label with text wrapping to the right of the button
        String[] labelLines = wrapText(label, w - 10); // Adjust the margin as needed
        float labelY = y + h / 2 - (labelLines.length - 1) * 8; // Adjust vertical alignment
        for (String line : labelLines) {
            parent.text(line, x + w / 2, labelY);
            labelY += 20; // Adjust line spacing
        }

        // Display the description with text wrapping to the right of the label
        parent.textSize(12);
        String[] descLines = wrapText(description, w - 5); // Adjust the margin as needed
        float descX = x + w + 30; // Adjust horizontal alignment to the right of the button
        float descY = y + h / 2 - ((descLines.length - 2) * 9 + 10); // Adjust vertical alignment
        
        for (String line : descLines) {
            parent.text(line, descX, descY);
            descY += 16; // Adjust line spacing
        }
    }

    // Method to wrap text within a specified width
    String[] wrapText(String text, float maxWidth) {
        String[] words = parent.split(text, ' ');
        String currentLine = "";
        java.util.ArrayList<String> result = new java.util.ArrayList<String>();

        for (String word : words) {
            String potentialLine = currentLine.isEmpty() ? word : currentLine + " " + word;
            float lineW = parent.textWidth(potentialLine);

            if (lineW <= maxWidth + 10) {
                currentLine = potentialLine;
            } else {
                result.add(currentLine);
                currentLine = word;
            }
        }

        if (!currentLine.isEmpty()) {
            result.add(currentLine);
        }

        return result.toArray(new String[0]);
    }
}
