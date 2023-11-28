package brickGame; // Package Declaration


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;    // Importing necessary JavaFX and Java classes

public class Block implements Serializable {
    // Static instance for a default block
    private static Block block = new Block(-1, -1, Color.TRANSPARENT, 99);


    // Row and column positions of the block
    public int row;
    public int column;

    
    // Flag to check if the block is destroyed
    public boolean isDestroyed = false;

    // Color of the block
    private Color color;
    // Type of the block (e.g., for different block types in the game)
    public int type;

    // X and Y coordinates for positioning the block
    public int x;
    public int y;

    private int width = 100; // Default width of the block
    private int height = 30; // Default height of the block
    private int paddingTop = height * 2; // Padding at the top of the block
    private int paddingH = 50; // Horizontal padding
    public Rectangle rect; // Rectangle shape representing the block in the UI

    public static int NO_HIT = -1; // Constant representing no hit
    public static int HIT_RIGHT = 0; // Constant representing a hit on the right
    public static int HIT_BOTTOM = 1; // Constant representing a hit on the bottom
    public static int HIT_LEFT = 2; // Constant representing a hit on the left
    public static int HIT_TOP = 3; // Constant representing a hit on the top
    public static final int HIT_BOTTOM_RIGHT = 4; // Constant representing a hit on the bottom-right corner
    public static final int HIT_BOTTOM_LEFT = 5;  // Constant representing a hit on the bottom-left corner
    public static final int HIT_TOP_RIGHT = 6;    // Constant representing a hit on the top-right corner
    public static final int HIT_TOP_LEFT = 7;     // Constant representing a hit on the top-left corner


    public static int BLOCK_NORMAL = 99; // Constant for normal block type
    public static int BLOCK_QUESTION = 100; // Constant for chocolate block type
    public static int BLOCK_STAR = 101; // Constant for star block type
    public static int BLOCK_HEART = 102; // Constant for heart block type
    public static int BLOCK_GIANT = 103; // Constant for Giant block type
    public static int BLOCK_MINI = 104; // Constant for Giant block type

    // Constructor for the Block class
    public Block(int row, int column, Color color, int type) { // Constructor for Block class, initializes the block with specified properties
        this.row = row; // Setting the block's row and column position
        this.column = column; // Setting the block's row and column position
        this.color = color; // Assign the specified color to the block
        this.type = type; // Set the type of the block

        draw(); // Call the draw method to set up the block's visual representation
    }

    private void draw() {
        x = (column * width) + paddingH; // Calculate the x-coordinate
        y = (row * height) + paddingTop; // Calculate the y-coordinate

        rect = new Rectangle(); // Initialize the Rectangle object
        rect.setWidth(width); // Set the width of the Rectangle
        rect.setHeight(height); // Set the height of the Rectangle
        rect.setX(x); // Set the x-coordinate of the Rectangle
        rect.setY(y); // Set the y-coordinate of the Rectangle

        // Conditional logic for setting different fill patterns based on block type
        if (type == BLOCK_QUESTION) {
            Image image = new Image("question.png"); // Load choco image
            ImagePattern pattern = new ImagePattern(image); // Create an image pattern
            rect.setFill(pattern); // Set the pattern as the fill for the Rectangle
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.png"); // Load heart image
            ImagePattern pattern = new ImagePattern(image); // Create an image pattern
            rect.setFill(pattern); // Set the pattern as the fill for the Rectangle

        } else if (type == BLOCK_GIANT) {
            Image image = new Image("giant.png"); // Load giant image
            ImagePattern pattern = new ImagePattern(image); // Create an image pattern
            rect.setFill(pattern); // Set the pattern as the fill for the Rectangle
        } else if (type == BLOCK_MINI) {
            Image image = new Image("mini.png"); // Load mini image
            ImagePattern pattern = new ImagePattern(image); // Create an image pattern
            rect.setFill(pattern); // Set the pattern as the fill for the Rectangle
        } else if (type == BLOCK_STAR) {
            Image image = new Image("gold.png"); // Load star image
            ImagePattern pattern = new ImagePattern(image); // Create an image pattern
            rect.setFill(pattern); // Set the pattern as the fill for the Rectangle
        }
        else {
            rect.setFill(color); // Set the color as the fill for the Rectangle if no special type
        }

    }

   // Added ballRadius parameter for accurate collision detection with blocks
   public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
    if (isDestroyed) {
        return NO_HIT; // Return NO_HIT if there is no collision
    }

       // Check collision with the bottom-right corner
       if (xBall - ballRadius <= x + width && xBall + ballRadius > x + width
               && yBall - ballRadius <= y + height && yBall + ballRadius > y + height) {
           return HIT_BOTTOM_RIGHT; // Return the direction of the hit (bottom-right)
       }

       // Check collision with the bottom-left corner
       if (xBall + ballRadius >= x && xBall - ballRadius < x
               && yBall - ballRadius <= y + height && yBall + ballRadius > y + height) {
           return HIT_BOTTOM_LEFT; // Return the direction of the hit (bottom-left)
       }

       // Check collision with the top-right corner
       if (xBall - ballRadius <= x + width && xBall + ballRadius > x + width
               && yBall + ballRadius >= y && yBall - ballRadius < y) {
           return HIT_TOP_RIGHT; // Return the direction of the hit (top-right)
       }

       // Check collision with the top-left corner
       if (xBall + ballRadius >= x && xBall - ballRadius < x
               && yBall + ballRadius >= y && yBall - ballRadius < y) {
           return HIT_TOP_LEFT; // Return the direction of the hit (top-left)
       }


    // Check collision with the bottom of the block
    if (xBall >= x && xBall <= x + width && yBall - ballRadius <= y + height && yBall + ballRadius > y + height) {
        return HIT_BOTTOM; // Return the direction of the hit (bottom)
    }

    // Check collision with the top of the block
    if (xBall >= x && xBall <= x + width && yBall + ballRadius >= y && yBall - ballRadius < y) {
        return HIT_TOP; // Return the direction of the hit (top)
    }

    // Check collision with the right side of the block
    if (yBall >= y && yBall <= y + height && xBall - ballRadius <= x + width && xBall + ballRadius > x + width) {
        return HIT_RIGHT; // Return the direction of the hit (right)
    }

    // Check collision with the left side of the block
    if (yBall >= y && yBall <= y + height && xBall + ballRadius >= x && xBall - ballRadius < x) {
        return HIT_LEFT; // Return the direction of the hit (left)
    }



    return NO_HIT; // Return NO_HIT if there is no collision detected
}


    public static int getPaddingTop() {
        return block.paddingTop;
    }

    public static int getPaddingH() {
        return block.paddingH;
    }

    public static int getHeight() {
        return block.height;
    }

    public static int getWidth() {
        return block.width;
    }

}
