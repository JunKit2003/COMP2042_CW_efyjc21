package brickGame; // Package declaration


// Importing necessary JavaFX classes and Java classes
import javafx.scene.image.Image; 
import javafx.scene.paint.ImagePattern; 
import javafx.scene.shape.Rectangle; 

import java.io.Serializable; 
import java.util.Random; 

public class Bonus implements Serializable { // Class representing a bonus item in the game
    public Rectangle choco; // Rectangle shape for the bonus item

    public double x; // Positional coordinate for the bonus item
    public double y; // Positional coordinate for the bonus item
    public long timeCreated; // Time of creation or status of the bonus item
    public boolean taken = false; // Time of creation or status of the bonus item

    public Bonus(int row, int column) { // Constructor for Bonus class
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15; // Calculate and set the position of the bonus item
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15; // Calculate and set the position of the bonus item

        draw(); // Call the draw method to set up the visual representation
    }

    private void draw() {
        choco = new Rectangle(); // Initialize the Rectangle object for the bonus
        choco.setWidth(30); // Set the width of the Rectangle
        choco.setHeight(30); // Set the height of the Rectangle
        choco.setX(x); // Set the x-coordinate of the Rectangle
        choco.setY(y); // Set the y-coordinate of the Rectangle

        String url; // Variable to store the image URL
        // Randomly select an image for the bonus
        if (new Random().nextInt(20) % 2 == 0) {
            url = "bonus1.png"; // URL for the first bonus image
        } else {
            url = "bonus2.png"; // URL for the second bonus image
        }

        choco.setFill(new ImagePattern(new Image(url))); // Set the image as the fill for the Rectangle
    }

    // ... Remaining methods and class definition ...
}
