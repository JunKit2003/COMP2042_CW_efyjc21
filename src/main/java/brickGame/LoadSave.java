package brickGame; // Package declaration

import java.io.File; // Importing necessary Java classes for I/O operations
import java.io.FileInputStream; // Importing necessary Java classes for I/O operations
import java.io.IOException; // Importing necessary Java classes for I/O operations
import java.io.ObjectInputStream; // Importing necessary Java classes for I/O operations
import java.util.ArrayList; // Importing necessary Java classes for I/O operations


/**
 * The LoadSave class is responsible for handling the loading and saving of game state.
 * It includes various fields that represent different aspects of the game's current state,
 * which can be serialized for saving or loaded from a saved state.
 */
public class LoadSave { // Class for handling loading and saving game states

    /** Indicates if a heart block exists in the current game state. */
    public boolean isExistHeartBlock; // Boolean flag to indicate existence of a special block
    public boolean isGoldStatus; // Boolean flag for gold status
    public boolean isBigBall; // Boolean flag for gold status
    public boolean isSmallBall; // Boolean flag for gold status
    public boolean goDownBall; // Boolean flag for ball movement direction
    public boolean goRightBall; // Boolean flag for ball movement direction
    public boolean colideToBreak; // Boolean flag for collision detection
    public boolean colideToBreakAndMoveToRight; // Boolean flag for collision detection and movement
    public boolean colideToRightWall; // Boolean flag for collision with right wall
    public boolean colideToLeftWall; // Boolean flag for collision with left wall
    public boolean colideToRightBlock; // Boolean flag for collision with right block
    public boolean colideToBottomBlock; // Boolean flag for collision with bottom block
    public boolean colideToLeftBlock; // Boolean flag for collision with left block
    public boolean colideToTopBlock; // Boolean flag for collision with top block
    public boolean colideToTopLeftCornerBlock; // Boolean flag for collision with top-left corner block
    public boolean colideToBottomLeftCornerBlock; // Boolean flag for collision with bottom-left corner block
    public boolean colideToTopRightCornerBlock; // Boolean flag for collision with top-right corner block
    public boolean colideToBottomRightCornerBlock; // Boolean flag for collision with bottom-right corner block

    /** The current level of the game. */
    public int level; // Integer for current game level

    /** The current score of the player in the game. */
    public int score; // Integer for current score
    public int heart; // Integer for number of hearts or lives
    public int destroyedBlockCount; // Integer for count of destroyed blocks

    /** The x-coordinate of the ball in the current game state. */
    public double xBall; // Double for ball's x-coordinate

    /** The y-coordinate of the ball in the current game state. */
    public double yBall; // Double for ball's y-coordinate
    public double xBreak; // Double for break's x-coordinate
    public double yBreak; // Double for break's y-coordinate
    public double centerBreakX; // Double for center of the break's x-coordinate
    public long time; // Long for tracking time
    public long goldTime; // Long for tracking gold time
    public double vX; // Double for velocity in x-direction
    public ArrayList<BlockSerializable> blocks = new ArrayList<BlockSerializable>(); // ArrayList to store serialized blocks



    /**
     * Reads the saved game state from a file and updates the game state accordingly.
     * This method should handle all necessary deserialization logic to load the game state.
     */
    public void read() {
        // Method to read and load the game state from a file
        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(Main.savePath)));
            // Reading various game state variables from the input stream
            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            destroyedBlockCount = 0;
            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xBreak = inputStream.readDouble();
            yBreak = inputStream.readDouble();
            centerBreakX = inputStream.readDouble();
            time = inputStream.readLong();
            vX = inputStream.readDouble();
            isExistHeartBlock = inputStream.readBoolean();
            isGoldStatus = inputStream.readBoolean();
            isBigBall = inputStream.readBoolean();
            isSmallBall = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            colideToBreak = inputStream.readBoolean();
            colideToBreakAndMoveToRight = inputStream.readBoolean();
            colideToRightWall = inputStream.readBoolean();
            colideToLeftWall = inputStream.readBoolean();
            colideToRightBlock = inputStream.readBoolean(); // Reading the collision state with a right block
            colideToBottomBlock = inputStream.readBoolean(); // Reading the collision state with a bottom block
            colideToLeftBlock = inputStream.readBoolean(); // Reading the collision state with a left block
            colideToTopBlock = inputStream.readBoolean(); // Reading the collision state with a top block
            colideToTopLeftCornerBlock = inputStream.readBoolean(); // Reading the collision state with a top-left corner block
            colideToBottomLeftCornerBlock = inputStream.readBoolean(); // Reading the collision state with a bottom-left corner block
            colideToTopRightCornerBlock = inputStream.readBoolean(); // Reading the collision state with a top-right corner block
            colideToBottomRightCornerBlock = inputStream.readBoolean(); // Reading the collision state with a bottom-right corner block
            

            try {
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject(); // Reading serialized blocks
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
