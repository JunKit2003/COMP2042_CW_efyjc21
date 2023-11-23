package brickGame; // Package declaration

import java.io.File; // Importing necessary Java classes for I/O operations
import java.io.FileInputStream; // Importing necessary Java classes for I/O operations
import java.io.IOException; // Importing necessary Java classes for I/O operations
import java.io.ObjectInputStream; // Importing necessary Java classes for I/O operations
import java.util.ArrayList; // Importing necessary Java classes for I/O operations

public class LoadSave { // Class for handling loading and saving game states
    public boolean isExistHeartBlock; // Boolean flag to indicate existence of a special block
    public boolean isGoldStauts; // Boolean flag for gold status
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
    public int level; // Integer for current game level
    public int score; // Integer for current score
    public int heart; // Integer for number of hearts or lives
    public int destroyedBlockCount; // Integer for count of destroyed blocks
    public double xBall; // Double for ball's x-coordinate
    public double yBall; // Double for ball's y-coordinate
    public double xBreak; // Double for break's x-coordinate
    public double yBreak; // Double for break's y-coordinate
    public double centerBreakX; // Double for center of the break's x-coordinate
    public long time; // Long for tracking time
    public long goldTime; // Long for tracking gold time
    public double vX; // Double for velocity in x-direction
    public ArrayList<BlockSerializable> blocks = new ArrayList<BlockSerializable>(); // ArrayList to store serialized blocks

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
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();
            isExistHeartBlock = inputStream.readBoolean();
            isGoldStauts = inputStream.readBoolean();
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
