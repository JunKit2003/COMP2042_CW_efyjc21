package brickGame; // Declares the package name as 'brickGame'

// Import statements for various JavaFX classes and interfaces
import javafx.application.Application; // Base class for JavaFX applications
import javafx.application.Platform; // Provides methods for interacting with the JavaFX application platform
import javafx.event.ActionEvent; // Represents an event related to an action (like button click)
import javafx.event.EventHandler; // Interface for event handling
import javafx.scene.Scene; // Represents the physical contents of a JavaFX application
import javafx.scene.control.Button; // UI control for buttons
import javafx.scene.control.Label; // UI control for displaying text
import javafx.scene.image.Image; // Class for handling images
import javafx.scene.input.KeyEvent; // Represents an event related to keyboard input
import javafx.scene.layout.Pane; // Base class for layout panes in JavaFX
import javafx.scene.paint.Color; // Class for colors
import javafx.scene.paint.ImagePattern; // Class for filling shapes with an image
import javafx.scene.shape.Circle; // Shape class for circles
import javafx.scene.shape.Rectangle; // Shape class for rectangles
import javafx.stage.Stage; // Top-level container for JavaFX application

// Java core imports
import java.io.*; // Imports classes for input and output
import java.util.ArrayList; // Imports the ArrayList class for dynamic arrays

import java.util.Random; // Imports the Random class for generating random numbers

// Declaration of the Main class which extends the Application class from JavaFX and implements event handlers
public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    // Instance variables for the Main class
    private int level = 0; // Variable to keep track of the game level

    private double xBreak = 0.0f; // Variable for the X coordinate (horizontal position) of a game element, likely a 'break'
    private double centerBreakX; // Variable for the central X coordinate of the 'break'
    private double yBreak = 640.0f; // Variable for the Y coordinate (vertical position) of the 'break'

    private int breakWidth = 130; // Width of the 'break' element
    private int breakHeight = 30; // Height of the 'break' element
    private int halfBreakWidth = breakWidth / 2; // Half the width of the 'break', useful for calculations

    private int sceneWidth = 500; // Width of the scene (game window or canvas)
    private int sceneHeigt = 700; // Height of the scene (game window or canvas)

    private static int LEFT = 1; // Static variable representing the left direction, possibly for movement
    private static int RIGHT = 2; // Static variable representing the right direction
// Definition of additional instance variables for the Main class

private Circle ball; // Variable representing the ball as a Circle object (JavaFX)
private double xBall; // X coordinate (horizontal position) of the ball
private double yBall; // Y coordinate (vertical position) of the ball

private boolean isGoldStauts = false; // Boolean flag to track if 'Gold Status' is active
private boolean isExistHeartBlock = false; // Boolean flag to check the existence of a 'Heart Block'

private Rectangle rect; // Variable representing a rectangle, possibly the paddle in the game
private int ballRadius = 10; // Radius of the ball

private int destroyedBlockCount = 0; // Counter for the number of blocks destroyed

private double v = 1.000; // Possibly a velocity or speed variable for the ball

private int heart = -1; // Variable representing the number of lives or 'hearts' the player has
private int score = 0; // Variable for keeping the player's score
private long time = 0; // Variable to track time, possibly for game duration or events
private long hitTime = 0; // Variable to track the time of the last hit
private long goldTime = 0; // Variable to track the duration of the 'Gold Status'

private GameEngine engine; // Variable for a GameEngine object, likely handling game logic

// Static variables for file paths used in saving game data
public static String savePath = "C:\\Developing Maintainable Software\\COMP2042_CW_efyjc21\\src\\main\\save\\save.mdds"; // Path for the save file
public static String savePathDir = "C:\\Developing Maintainable Software\\COMP2042_CW_efyjc21\\src\\main\\save"; // Directory path for save files

// ArrayLists to store game elements
private ArrayList<Block> blocks = new ArrayList<Block>(); // List of 'Block' objects in the game
private ArrayList<Bonus> chocos = new ArrayList<Bonus>(); // List of 'Bonus' objects, named 'chocos'

// Array of colors, possibly for rendering different types of blocks or game elements
private Color[] colors = new Color[]{
    Color.MAGENTA,
    Color.RED,
    Color.GOLD,
    Color.CORAL,
    Color.AQUA,
    Color.VIOLET,
    Color.GREENYELLOW,
    Color.ORANGE,
    Color.PINK,
    Color.SLATEGREY,
    Color.YELLOW,
    Color.TOMATO, 
    Color.TAN,
}; // Completion of the colors array

// UI elements and a flag for game state management
public Pane root; // Pane object that serves as the root container for the UI elements
private Label scoreLabel; // Label for displaying the score
private Label heartLabel; // Label for displaying the number of hearts (lives)
private Label levelLabel; // Label for displaying the current level

private boolean loadFromSave = false; // Boolean flag to determine if the game should load from a saved state

// Primary stage and UI controls for game management
Stage primaryStage; // Reference to the primary stage of the JavaFX application
Button load = null; // Button for loading a saved game
Button newGame = null; // Button for starting a new game

@Override
// Method override for the start method of the Application class
public void start(Stage primaryStage) throws Exception {
    this.primaryStage = primaryStage; // Setting the primary stage reference

    // Conditional logic for game initialization
    if (loadFromSave == false) {
        level++; // Incrementing the level if not loading from save

             // Conditional statements for level progression and game status
             if (level > 1) {
                new Score().showMessage("Level Up :)", this); // Display a level-up message when the level is greater than 1
            }
            if (level == 18) {
                new Score().showWin(this); // Display a win message and return from the method if the level is 18
                return;
            }

            // Initialization methods for game elements
            initBall();   // Initializes the ball
            initBreak();  // Initializes the 'break' element
            initBoard();  // Initializes the game board

            // Creating and configuring buttons for loading and starting a new game
            load = new Button("Load Game");          // Button to load a saved game
            newGame = new Button("Start New Game");  // Button to start a new game
            load.setTranslateX(220);                 // Setting the horizontal position of the 'load' button
            load.setTranslateY(300);                 // Setting the vertical position of the 'load' button
            newGame.setTranslateX(220);              // Setting the horizontal position of the 'newGame' button
            newGame.setTranslateY(340);              // Setting the vertical position of the 'newGame' button

        } // End of the conditional block for loading game state

        

        // UI and scene setup for the game
        root = new Pane(); // Creating a new Pane object to serve as the container for game elements

        // Creating and configuring UI labels for score, level, and hearts
        scoreLabel = new Label("Score: " + score); // Label for displaying the current score
        levelLabel = new Label("Level: " + level); // Label for displaying the current level
        levelLabel.setTranslateY(20);              // Positioning the level label vertically
        heartLabel = new Label("Heart : " + heart); // Label for displaying the number of hearts (lives)
        heartLabel.setTranslateX(sceneWidth - 70);  // Positioning the heart label horizontally

        // Conditional logic for adding elements to the root pane based on game state
        if (loadFromSave == false) {
            // Adding UI elements and buttons to the root pane when not loading from a save
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, newGame);
        } else {
            // Adding only the essential UI elements when loading from a save
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        }

        // Adding each block from the blocks list to the root pane
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }

        // Setting up the scene with the root pane and its dimensions
        Scene scene = new Scene(root, sceneWidth, sceneHeigt); // Creating the scene with specified width and height
        scene.getStylesheets().add("style.css"); // Adding a stylesheet for styling the UI elements
        scene.setOnKeyPressed(this); // Setting an event handler for key presses on the scene

        // Setting up the primary stage of the JavaFX application
        primaryStage.setTitle("Game"); // Setting the title of the window to "Game"
        primaryStage.setScene(scene);  // Assigning the previously created scene to the primary stage
        primaryStage.show(); // Making the primary stage visible

        // Conditional logic for game state and UI visibility
        if (loadFromSave == false) {
            // Logic for handling game progress when not loading from a save
            if (level > 1 && level < 18) {
                load.setVisible(false); // Hiding the load button
                newGame.setVisible(false); // Hiding the new game button

                // Initializing and configuring the game engine
                engine = new GameEngine(); // Creating a new GameEngine object
                engine.setOnAction(this); // Setting the current class as the action handler for the engine
                engine.setFps(120); // Setting the frames per second for the game engine
                engine.start(); // Starting the game engine
            }

            // Setting the action for the load button
            load.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    loadGame(); // Calling the loadGame method when the load button is clicked
                    load.setVisible(false); // Hiding the load button after the game is loaded
                    newGame.setVisible(false); // Hiding the new game button after the game is loaded
                }
            }); // End of the load button action event

            // Setting the action for the new game button
            newGame.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    // Initializing and configuring the game engine for a new game
                    engine = new GameEngine(); // Creating a new GameEngine object
                    engine.setOnAction(Main.this); // Setting the current class as the action handler for the engine
                    engine.setFps(120); // Setting the frames per second for the game engine
                    engine.start(); // Starting the game engine

                    // Hiding the UI buttons once the game starts
                    load.setVisible(false); // Hiding the load button
                    newGame.setVisible(false); // Hiding the new game button
                }
            }); // End of the new game button action event

        } else {
            // Initializing and configuring the game engine when loading from a save
            engine = new GameEngine(); // Creating a new GameEngine object
            engine.setOnAction(this); // Setting the current class as the action handler for the engine
            engine.setFps(120); // Setting the frames per second for the game engine
            engine.start(); // Starting the game engine
            loadFromSave = false; // Resetting the loadFromSave flag
        }

    } // End of the start method

    // Method to initialize the game board
    private void initBoard() {
        // Nested loops to create game elements based on the level
        for (int i = 0; i < 4; i++) { // Outer loop, possibly for rows of elements
            for (int j = 0; j < level + 1; j++) { // Inner loop, for columns of elements, increasing with level
                int r = new Random().nextInt(500); // Generating a random number for element creation logic
                if (r % 5 == 0) {
                    continue; // Skipping the current iteration if the random number is divisible by 5
                }
                int type; // Variable to hold the type of block to be created
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO; // Assigning a 'choco' block type
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) { // Checking if a 'heart' block exists
                        type = Block.BLOCK_HEART; // Assigning a 'heart' block type
                        isExistHeartBlock = true; // Flagging that a heart block exists
                    } else {
                        type = Block.BLOCK_NORMAL; // Assigning a normal block type
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR; // Assigning a star block type
                    // type = Block.BLOCK_ADDBALL; // Assigning a addball block type
                } else {
                    type = Block.BLOCK_NORMAL; // Defaulting to a normal block type
                }
                // Adding a new block to the blocks list with determined characteristics
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                // System.out.println("colors " + r % (colors.length)); // Debugging line (commented out)
            }
        }
    } // End of the initBoard method

    // Main method of the application
    public static void main(String[] args) {
        launch(args); // Launching the JavaFX application
    }

    @Override
    // Method override to handle key events
    public void handle(KeyEvent event) {
        // Switch statement to process different key events
        switch (event.getCode()) {
            case LEFT:
                move(LEFT); // Calls the move method with LEFT as parameter when left key is pressed
                break; // Breaks out of the switch statement
            case RIGHT:
                move(RIGHT); // Calls the move method with RIGHT as parameter when right key is pressed
                break; // Breaks out of the switch statement
            case DOWN:
                // setPhysicsToBall(); // A commented out method call, possibly to apply physics to the ball
                break; // Breaks out of the switch statement
            case S:
                saveGame(); // Calls the saveGame method when the 'S' key is pressed
                break; // Breaks out of the switch statement
        }
    } // End of the key event handling method

    float oldXBreak; // Declaration of a float variable, oldXBreak, likely used to track previous positions

     // Method for handling movement based on direction
     private void move(final int direction) {
        // Creating a new thread to handle the movement
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4; // Time in milliseconds to pause between movement steps

                // Loop to incrementally move the game element
                for (int i = 0; i < 30; i++) {
                    // Conditional checks to prevent moving beyond the scene's width
                    if (xBreak == (sceneWidth - breakWidth) && direction == RIGHT) {
                        return; // Stops the movement if at the right edge and moving right
                    }
                    if (xBreak == 0 && direction == LEFT) {
                        return; // Stops the movement if at the left edge and moving left
                    }

                    // Adjusting the position based on the direction
                    if (direction == RIGHT) {
                        xBreak++; // Moves the element to the right
                    } else {
                        xBreak--; // Moves the element to the left
                    }

                    centerBreakX = xBreak + halfBreakWidth; // Updating the center position of the break element

                    try {
                        Thread.sleep(sleepTime); // Pausing the thread for a short duration to control movement speed
                    } catch (InterruptedException e) {
                        e.printStackTrace(); // Printing the stack trace in case of an InterruptedException
                    }

                    // Adjusting the sleep time after a certain number of iterations
                    if (i >= 20) {
                        sleepTime = i; // Increasing the sleep time, potentially to slow down movement
                    }
                }
            }
        }).start(); // Starting the thread for the movement logic

    } // End of the move method

    // Method for initializing the ball
    private void initBall() {
        Random random = new Random(); // Creating a Random object for generating random positions
        xBall = random.nextInt(sceneWidth) + 1; // Generating a random X position for the ball within the scene width
        yBall = random.nextInt(sceneHeigt - 200) + ((level + 1) * Block.getHeight()) + 15; // Generating a random Y position for the ball
        ball = new Circle(); // Creating a new Circle object for the ball

        ball.setRadius(ballRadius); // Setting the radius of the ball
        ball.setFill(new ImagePattern(new Image("ball.png"))); // Setting the fill of the ball with an image (texture)

    } // End of the initBall method

    // Method for initializing the break (likely the paddle in the game)
    private void initBreak() {
        rect = new Rectangle(); // Creating a new Rectangle object for the break
        rect.setWidth(breakWidth); // Setting the width of the break
        rect.setHeight(breakHeight); // Setting the height of the break
        rect.setX(xBreak); // Setting the X position of the break
        rect.setY(yBreak); // Setting the Y position of the break

        ImagePattern pattern = new ImagePattern(new Image("block.jpg")); // Creating an image pattern for the break's texture

        rect.setFill(pattern); // Setting the fill of the rectangle with the created image pattern

    } // End of the initBreak method

    // Declaration of boolean variables for ball movement and collision
    private boolean goDownBall = true; // Flag to control if the ball should move down
    private boolean goRightBall = true; // Flag to control if the ball should move right
    private boolean colideToBreak = false; // Flag to track if the ball has collided with the break (paddle)
    private boolean colideToBreakAndMoveToRight = true; // Flag for collision with the break and moving to the right
    private boolean colideToRightWall = false; // Flag for collision with the right wall
    private boolean colideToLeftWall = false; // Flag for collision with the left wall
    private boolean colideToRightBlock = false; // Flag for collision with a block on the right
    private boolean colideToBottomBlock = false; // Flag for collision with a block on the bottom
    private boolean colideToLeftBlock = false; // Flag for collision with a block on the left
    private boolean colideToTopBlock = false; // Flag for collision with a block on the top
    private boolean colideToTopRightCornerBlock = false; // Flag for collision with a block on the top right corner
    private boolean colideToTopLeftCornerBlock = false; // Flag for collision with a block on the top left corner
    private boolean colideToBottomLeftCornerBlock = false; // Flag for collision with a block on the bottom left corner
    private boolean colideToBottomRightCornerBlock= false; // Flag for collision with a block on the bottom right corner
    
    // Variables for the velocity of the ball
    private double vX = 1.000; // Horizontal velocity of the ball
    private double vY = 1.000; // Vertical velocity of the ball
    
    // Method to reset collision flags
    private void resetColideFlags() {
        // Resetting all collision flags to false
        colideToBreak = false;
        colideToBreakAndMoveToRight = false;
        colideToRightWall = false;
        colideToLeftWall = false;
    
        colideToRightBlock = false;
        colideToBottomBlock = false; // Resetting the flag for collision with a bottom block
        colideToLeftBlock = false; // Resetting the flag for collision with a left block
        colideToTopBlock = false; // Resetting the flag for collision with a top block
        colideToBottomLeftCornerBlock = false; // Resetting the flag for collision with bottom left corner block
        colideToBottomRightCornerBlock = false; // Resetting the flag for collision with a bottom right corner block
        colideToTopLeftCornerBlock = false; // Resetting the flag for collision with a top left corner block
        colideToTopRightCornerBlock = false; // Resetting the flag for collision with a top right corner block
    } // End of the resetColideFlags method

    // Method to set the physics for the ball's movement
    private void setPhysicsToBall() {
        // This commented line suggests a calculation for velocity v based on time and hitTime, but it's currently not in use
        //v = ((time - hitTime) / 1000.000) + 1.000;

        // Conditional logic for vertical movement of the ball
        if (goDownBall) {
            yBall += vY; // Moving the ball down by increasing its Y coordinate
        } else {
            yBall -= vY; // Moving the ball up by decreasing its Y coordinate
        }

        // Conditional logic for horizontal movement of the ball
        if (goRightBall) {
            xBall += vX; // Moving the ball right by increasing its X coordinate
        } else {
            xBall -= vX; // Moving the ball left by decreasing its X coordinate
        }
        // Conditional checks and actions for the ball's vertical position
        if (yBall <= 0) {
            // The commented line suggests a possible reset or adjustment of horizontal velocity, but it's not active
            //vX = 1.000;
            resetColideFlags(); // Resetting all collision flags
            goDownBall = true; // Changing the ball's vertical direction to downward
            return; // Exiting the method
        }
        if (yBall >= sceneHeigt) {
            goDownBall = false; // Changing the ball's vertical direction to upward
            if (!isGoldStauts) {
                // Handling the game-over scenario or life reduction when the ball reaches the bottom of the scene
                heart--; // Decreasing the number of hearts (lives)
                new Score().show(sceneWidth / 2, sceneHeigt / 2, -1, this); // Displaying a score message

                if (heart == 0) {
                    new Score().showGameOver(this); // Displaying the game over message if all hearts are lost
                    engine.stop(); // Stopping the game engine
                }

                // The commented 'return' suggests an early exit from the method in certain conditions but is currently not active
                //return;
            }
        }


        // Collision detection with the break (paddle)
        if (yBall + ballRadius >= yBreak && yBall - ballRadius <= yBreak) { // Checking for collision with the top of the break
            if (xBall + ballRadius >= xBreak && xBall - ballRadius <= xBreak + breakWidth) { 
                hitTime = time; // Updating the hit time with the current time
                resetColideFlags(); // Resetting all collision flags
                colideToBreak = true; // Setting the flag for collision with the break
                goDownBall = false; // Changing the ball's vertical direction to upward

                // Calculating the collision position relative to the break's center
                double relation = (xBall - centerBreakX) / (breakWidth / 2);

                // Adjusting the horizontal velocity based on where the ball hits the break
                if (Math.abs(relation) <= 0.3) {
                    vX = Math.abs(relation); // Smaller adjustment for central collisions
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500); // Moderate adjustment for off-center collisions
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500); // Larger adjustment for edge collisions
                }

                // Setting the direction to right or left based on the collision relation
                colideToBreakAndMoveToRight = relation > 0; // True if collision is on the right side of the center

            }
        }  

        // Additional collision detection for the ball with the scene's walls
        if (xBall >= sceneWidth) {
            resetColideFlags(); // Resetting all collision flags
            // The commented line suggests a possible reset or adjustment of horizontal velocity, but it's not active
            //vX = 1.000;
            colideToRightWall = true; // Setting the flag for collision with the right wall
        }

        if (xBall <= 0) {
            resetColideFlags(); // Resetting all collision flags
            // The commented line suggests a possible reset or adjustment of horizontal velocity, but it's not active
            //vX = 1.000;
            colideToLeftWall = true; // Setting the flag for collision with the left wall
        }

        // Handling the ball's movement direction after collision with the break (paddle)
        if (colideToBreak) {
            if (colideToBreakAndMoveToRight) {
                goRightBall = true; // Setting the ball to move right if it collided on the right side of the break
            } else {
                goRightBall = false; // Setting the ball to move left if it collided on the left side of the break
            }

        }

        //Wall Colide
        // Handling the ball's movement direction after collision with walls

        // Collision with the right wall
        if (colideToRightWall) {
            goRightBall = false; // Setting the ball to move left if it collided with the right wall
        }

        // Collision with the left wall
        if (colideToLeftWall) {
            goRightBall = true; // Setting the ball to move right if it collided with the left wall
        }

        // Handling the ball's movement direction after collision with blocks
        // Collision with a block on the right
        if (colideToRightBlock) {
            goRightBall = true; // Setting the ball to move right if it collided with a block on the right
        }

        // Collision with a block on the left
        if (colideToLeftBlock) {
            goRightBall = true; // Setting the ball to move right if it collided with a block on the left

        }
        // Handling the ball's movement direction after collision with blocks on top or bottom
        if (colideToTopBlock) {
            goDownBall = false; // Setting the ball to move up if it collided with a block on the top
        }

        if (colideToBottomBlock) {
            goDownBall = true; // Setting the ball to move down if it collided with a block on the bottom
        }

        if (colideToBottomRightCornerBlock) {
            goDownBall = true; // Setting the ball to move down if it collided with a block on the bottom
            goRightBall = true ; 
        }

        if (colideToBottomLeftCornerBlock) {
            goDownBall = true; // Setting the ball to move down if it collided with a block on the bottom
            goRightBall = false ; 
        }

        if (colideToTopRightCornerBlock) {
            goDownBall = false; // Setting the ball to move down if it collided with a block on the bottom
            goRightBall = true ; 
        }

        if (colideToTopLeftCornerBlock) {
            goDownBall = false; // Setting the ball to move down if it collided with a block on the bottom
            goRightBall = false ; 
        }


    } // End of the setPhysicsToBall method

    // Method to check the count of destroyed blocks
    private void checkDestroyedCount() {
        // Checking if all blocks have been destroyed
        if (destroyedBlockCount == blocks.size()) {
            // The commented out lines suggest a placeholder for winning a level
            //TODO win level todo...
            //System.out.println("You Win");

            nextLevel(); // Calling the nextLevel method to progress to the next level

        }
    }
        // End of checkDestroyedCount method

    // Method for saving the game state
    private void saveGame() {
        // Creating a new thread to handle the save process
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Creating the directory for saving if it does not exist
                new File(savePathDir).mkdirs(); // Ensuring the save directory exists
                File file = new File(savePath); // Creating a file object for the save file
                ObjectOutputStream outputStream = null; // Declaring an ObjectOutputStream for writing data

                try {
                    // Initializing the output stream for the save file
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));

                    // Writing game state data to the save file
                    outputStream.writeInt(level); // Saving the current level
                    outputStream.writeInt(score); // Saving the current score
                    outputStream.writeInt(heart); // Saving the current number of hearts
                    outputStream.writeInt(destroyedBlockCount); // Saving the count of destroyed blocks
                    outputStream.writeDouble(xBall); // Saving the X position of the ball
                    outputStream.writeDouble(yBall); // Saving the Y position of the ball
                    outputStream.writeDouble(xBreak); // Saving the X position of the break (paddle)
                    outputStream.writeDouble(yBreak); // Saving the Y position of the break
                    outputStream.writeDouble(centerBreakX); // Saving the center X position of the break
                    outputStream.writeLong(time); // Saving the current game time
                    outputStream.writeLong(goldTime); // Saving the time related to 'Gold Status'
                    outputStream.writeDouble(vX); // Saving the horizontal velocity of the ball

                    // Writing boolean flags to the save file
                    outputStream.writeBoolean(isExistHeartBlock); // Saving the state of heart block existence
                    outputStream.writeBoolean(isGoldStauts); // Saving the state of 'Gold Status'
                    outputStream.writeBoolean(goDownBall); // Saving the vertical movement direction of the ball
                    outputStream.writeBoolean(goRightBall); // Saving the horizontal movement direction of the ball
                    outputStream.writeBoolean(colideToBreak); // Saving the collision state with the break
                    outputStream.writeBoolean(colideToBreakAndMoveToRight); // Saving the collision direction with the break
                    outputStream.writeBoolean(colideToRightWall); // Saving the collision state with the right wall
                    outputStream.writeBoolean(colideToLeftWall); // Saving the collision state with the left wall
                    outputStream.writeBoolean(colideToRightBlock); // Saving the collision state with a right block
                    outputStream.writeBoolean(colideToBottomBlock); // Saving the collision state with a bottom block
                    outputStream.writeBoolean(colideToLeftBlock); // Saving the collision state with a left block
                    outputStream.writeBoolean(colideToTopBlock); // Saving the collision state with a top block
                    outputStream.writeBoolean(colideToTopLeftCornerBlock); // Saving the collision state with a top-left corner block
                    outputStream.writeBoolean(colideToBottomLeftCornerBlock); // Saving the collision state with a bottom-left corner block
                    outputStream.writeBoolean(colideToTopRightCornerBlock); // Saving the collision state with a top-right corner block
                    outputStream.writeBoolean(colideToBottomRightCornerBlock); // Saving the collision state with a bottom-right corner block

                    // Preparing to save the state of game blocks
                    ArrayList<BlockSerializable> blockSerializables = new ArrayList<BlockSerializable>(); // Creating a list to hold serializable block data
                    for (Block block : blocks) { // Iterating through each block in the game
                        if (block.isDestroyed) {
                            continue; // Skipping destroyed blocks
                        }
                        // Adding a serializable representation of the block to the list
                        blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                    }

                    // Writing the serializable block data to the save file
                    outputStream.writeObject(blockSerializables);

                    // Displaying a message to indicate that the game state has been saved
                    new Score().showMessage("Game Saved", Main.this);

                    // Handling potential exceptions during the save process
                } catch (FileNotFoundException e) {
                    e.printStackTrace(); // Printing stack trace for a FileNotFoundException
                } catch (IOException e) {
                    e.printStackTrace(); // Printing stack trace for an IOException
                } finally {
                    // The finally block continues in the next lines

                    // Finalizing the save process by flushing and closing the output stream
                    try {
                        outputStream.flush(); // Ensuring all data is written to the file
                        outputStream.close(); // Closing the output stream
                    } catch (IOException e) {
                        e.printStackTrace(); // Printing stack trace for any IOException during stream closure
                    }
                }
            }
        }).start(); // Starting the thread to execute the save operation

    } // End of the saveGame method

    // Method for loading the game state
    private void loadGame() {
        // Creating an instance of LoadSave to handle the loading process
        LoadSave loadSave = new LoadSave(); // Instantiating the LoadSave class
        loadSave.read(); // Calling the read method to load the saved game data

        // Setting game state variables based on loaded data
        isExistHeartBlock = loadSave.isExistHeartBlock; // Setting the heart block existence flag
        isGoldStauts = loadSave.isGoldStauts; // Setting the gold status flag
        // Continuing to set game state variables based on loaded data
        goDownBall = loadSave.goDownBall; // Setting the vertical movement direction of the ball
        goRightBall = loadSave.goRightBall; // Setting the horizontal movement direction of the ball
        colideToBreak = loadSave.colideToBreak; // Setting the collision state with the break (paddle)
        colideToBreakAndMoveToRight = loadSave.colideToBreakAndMoveToRight; // Setting the direction of movement after colliding with the break
        colideToRightWall = loadSave.colideToRightWall; // Setting the collision state with the right wall
        colideToLeftWall = loadSave.colideToLeftWall; // Setting the collision state with the left wall
        colideToRightBlock = loadSave.colideToRightBlock; // Setting the collision state with a right block
        colideToBottomBlock = loadSave.colideToBottomBlock; // Setting the collision state with a bottom block
        colideToLeftBlock = loadSave.colideToLeftBlock; // Setting the collision state with a left block
        colideToTopBlock = loadSave.colideToTopBlock; // Setting the collision state with a top block
        colideToTopLeftCornerBlock = loadSave.colideToTopLeftCornerBlock; // Setting the collision state with a top-left corner block
        colideToBottomLeftCornerBlock = loadSave.colideToBottomLeftCornerBlock; // Setting the collision state with a bottom-left corner block
        colideToTopRightCornerBlock = loadSave.colideToTopRightCornerBlock; // Setting the collision state with a top-right corner block
        colideToBottomRightCornerBlock = loadSave.colideToBottomRightCornerBlock; // Setting the collision state with a bottom-right corner block
        level = loadSave.level; // Setting the current game level
        score = loadSave.score; // Setting the current score
        heart = loadSave.heart; // Setting the current number of hearts (lives)
        destroyedBlockCount = loadSave.destroyedBlockCount; // Setting the count of destroyed blocks
        xBall = loadSave.xBall; // Setting the X position of the ball
        yBall = loadSave.yBall; // Setting the Y position of the ball
        xBreak = loadSave.xBreak; // Setting the X position of the break
        yBreak = loadSave.yBreak; // Setting the Y position of the break
        centerBreakX = loadSave.centerBreakX; // Setting the center X position of the break
        time = loadSave.time; // Setting the current game time
        goldTime = loadSave.goldTime; // Setting the time related to 'Gold Status'
        vX = loadSave.vX; // Setting the horizontal velocity of the ball

        // Clearing current blocks and bonus items from the game
        blocks.clear(); // Clearing the list of blocks
        chocos.clear(); // Clearing the list of bonus items (chocos)

        // Re-creating blocks from the saved game state
        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200); // Generating a random number for color assignment
            // Adding each block with properties from the saved state
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }

        // Attempting to restart the game from the loaded state
        try {
            loadFromSave = true; // Indicating that the game is being loaded from a saved state
            start(primaryStage); // Restarting the game using the primary stage
        } catch (Exception e) {
            e.printStackTrace(); // Printing stack trace for any exceptions during game restart
        }


    } // End of the loadGame method

    // Method for progressing to the next level in the game
    private void nextLevel() {
        // Ensuring the UI updates are done on the JavaFX Application Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Resetting certain game variables for the new level
                    vX = 1.000; // Resetting the horizontal velocity of the ball

                    engine.stop(); // Stopping the current game engine
                    resetColideFlags(); // Resetting all collision flags
                    goDownBall = true; // Setting the initial vertical movement direction of the ball

                    // Resetting special game states for the new level
                    isGoldStauts = false; // Resetting the 'Gold Status'
                    isExistHeartBlock = false; // Resetting the existence of heart block

                    // Resetting time-related variables
                    hitTime = 0; // Resetting the time of the last hit
                    time = 0; // Resetting the game time
                    goldTime = 0; // Resetting the time related to 'Gold Status'
                    // Continuing the preparation for the next level
                    engine.stop(); // Stopping the game engine again for safety
                    blocks.clear(); // Clearing the list of blocks for the new level
                    chocos.clear(); // Clearing the list of bonus items (chocos)
                    destroyedBlockCount = 0; // Resetting the count of destroyed blocks

                    // Restarting the game with the new level setup
                    start(primaryStage); // Restarting the game using the primary stage

                } catch (Exception e) {
                    e.printStackTrace(); // Printing stack trace for any exceptions during the level transition
                }
            }
        }); // End of the runLater block

    } // End of the nextLevel method

    // Method for restarting the game from the beginning
    public void restartGame() {
        try {
            level = 0; // Resetting to the first level
            heart = 3; // Resetting the number of hearts (lives) to the initial count
            score = 0; // Resetting the score to zero

            vX = 1.000; // Resetting the horizontal velocity of the ball
            destroyedBlockCount = 0; // Resetting the count of destroyed blocks
            resetColideFlags(); // Resetting all collision flags
            goDownBall = true; // Setting the initial vertical movement direction of the ball

            // Resetting special game states for the new game start
            isGoldStauts = false; // Resetting the 'Gold Status'
            isExistHeartBlock = false; // Resetting the existence of heart block
            hitTime = 0; // Resetting the time of the last hit
            time = 0; // Resetting the game time
            goldTime = 0; // Resetting the time related to 'Gold Status'

            // Clearing current blocks and bonus items from the game
            blocks.clear(); // Clearing the list of blocks
            chocos.clear(); // Clearing the list of bonus items (chocos)

            // Restarting the game with the initial setup
            start(primaryStage); // Restarting the game using the primary stage

        } catch (Exception e) {
            e.printStackTrace(); // Printing stack trace for any exceptions during game restart
        }
    } // End of the restartGame method


    // Overridden onUpdate method from a possible interface or superclass
    @Override
    public void onUpdate() {
        // Ensuring UI updates are executed on the JavaFX Application Thread
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                // Updating the score and heart labels with current game data
                scoreLabel.setText("Score: " + score); // Updating the score display
                heartLabel.setText("Heart : " + heart); // Updating the hearts (lives) display

                // Updating the position of the break (paddle) and ball based on their current coordinates
                rect.setX(xBreak); // Setting the X position of the break
                rect.setY(yBreak); // Setting the Y position of the break
                ball.setCenterX(xBall); // Setting the center X position of the ball
                ball.setCenterY(yBall); // Setting the center Y position of the ball

                // Updating the positions of bonus items (chocos) in the game
                for (Bonus choco : chocos) {
                    choco.choco.setY(choco.y); // Setting the Y position of each bonus item
                }
            }
        }); // End of the runLater block

        // Collision detection and handling for the ball with game blocks
        if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
            // Iterating over each block to check for collisions
            for (final Block block : blocks) {
                // Checking if the ball has hit the block
                int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                if (hitCode != Block.NO_HIT) {
                    // Updating score and displaying a score message when a block is hit
                    score += 1; // Incrementing the score

                    // Displaying a score message at the location of the hit block
                    new Score().show(block.x, block.y, 1, this);

                    // Setting the block as invisible and marking it as destroyed
                    block.rect.setVisible(false); // Hiding the block
                    block.isDestroyed = true; // Marking the block as destroyed
                    destroyedBlockCount++; // Incrementing the count of destroyed blocks

                    // Resetting collision flags after a block is hit
                    resetColideFlags();

                    // Handling special cases for different types of blocks
                    if (block.type == Block.BLOCK_CHOCO) {
                        // Creating a new bonus item (choco) when a 'choco' block is hit
                        final Bonus choco = new Bonus(block.row, block.column); // Creating a new bonus item
                        choco.timeCreated = time; // Setting the creation time for the bonus item

                        // Updating the UI on the JavaFX Application Thread
                        Platform.runLater(new Runnable() {
                             // Executing UI update for the new bonus item
                             @Override
                             public void run() {
                                 // Adding the new bonus item to the game scene
                                 root.getChildren().add(choco.choco);
                             }
                         });
                         // Adding the new bonus item to the list of bonus items
                         chocos.add(choco);
                     }
 
                     // Handling special effects for a 'star' type block
                     if (block.type == Block.BLOCK_STAR) {
                         // Setting up the 'Gold Status' when a 'star' block is hit
                         goldTime = time; // Recording the time of hitting the 'star' block
                         // Changing the ball's appearance to indicate 'Gold Status'
                         ball.setFill(new ImagePattern(new Image("goldball.png")));
                         System.out.println("gold ball"); // Logging a message for debugging
                         // Adding a special style class to the root for 'Gold Status'
                         root.getStyleClass().add("goldRoot");
                         isGoldStauts = true; // Setting the 'Gold Status' flag
                     }

                    //  if (block.type == Block.BLOCK_ADDBALL) {
                    //     initBall();
                    //     System.out.println("extra ball for you");
                    //  }
 
                     // Handling the effect of hitting a 'heart' type block
                     if (block.type == Block.BLOCK_HEART) {
                         heart++; // Incrementing the player's hearts (lives)
                     }
 
                    // Handling the direction of the collision based on the hitCode
                    if (hitCode == Block.HIT_RIGHT) {
                        colideToRightBlock = true; // Setting the flag for collision with a block on the right
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        colideToBottomBlock = true; // Setting the flag for collision with a block on the bottom
                    } else if (hitCode == Block.HIT_LEFT) {
                        colideToLeftBlock = true; // Setting the flag for collision with a block on the left
                    } else if (hitCode == Block.HIT_TOP) {
                        colideToTopBlock = true; // Setting the flag for collision with a block on the top
                    } else if (hitCode == Block.HIT_TOP_LEFT) {
                        colideToTopLeftCornerBlock = true; // Setting the flag for collision with the top-left corner
                    } else if (hitCode == Block.HIT_BOTTOM_LEFT) {
                        colideToBottomLeftCornerBlock = true; // Setting the flag for collision with the bottom-left corner
                    } else if (hitCode == Block.HIT_TOP_RIGHT) {
                        colideToTopRightCornerBlock = true; // Setting the flag for collision with the top-right corner
                    } else if (hitCode == Block.HIT_BOTTOM_RIGHT) {
                        colideToBottomRightCornerBlock = true; // Setting the flag for collision with the bottom-right corner
                    }



                }
                    // Additional processing or TODO items related to collisions may be added here
                    // The commented out lines suggest placeholders for additional logic or debugging
                    // TODO hit to break and some work here....
                    // System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
                }
                // End of the block collision detection loop

            } // End of the condition for ball and block collision range
        } // End of the onUpdate method



    @Override
    // Method placeholder for initialization logic
    public void onInit() {
        // Currently, there is no implementation inside this method
    }

    // Overridden method for handling physics updates
    @Override
    public void onPhysicsUpdate() {
        // Checking if all blocks are destroyed and moving to the next level if so
        checkDestroyedCount(); // Checking the count of destroyed blocks

        // Applying physics to the ball's movement
        setPhysicsToBall(); // Setting the ball's movement based on physics calculations

        // Handling the end of 'Gold Status' after a certain time period
        if (time - goldTime > 5000) { // Checking if 'Gold Status' duration has elapsed
            // Resetting the ball's appearance to normal
            ball.setFill(new ImagePattern(new Image("ball.png")));
            // Removing the special style class associated with 'Gold Status'
            root.getStyleClass().remove("goldRoot");
            // Resetting the 'Gold Status' flag
            isGoldStauts = false;
        }

        // Iterating over bonus items (chocos) to handle their state
        for (Bonus choco : chocos) {
            // Skipping the bonus item if it's out of the scene or already taken
            if (choco.y > sceneHeigt || choco.taken) {
                continue;
            }

            // Checking if a bonus item (choco) is collected by the player
            if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                // Logging the event of collecting the bonus item
                System.out.println("You Got it and +3 score for you");
                // Marking the bonus item as taken and making it invisible
                choco.taken = true;
                choco.choco.setVisible(false);
                // Increasing the player's score upon collecting the bonus item
                score += 3;
                // Displaying a score message at the location of the bonus item
                new Score().show(choco.x, choco.y, 3, this);
            }
            // Updating the Y position of the bonus item based on time elapsed since creation
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

        // The commented line could be for debugging purposes to monitor time and goldTime
        // System.out.println("time is:" + time + " goldTime is " + goldTime);

    } // End of the onPhysicsUpdate method

    // Overridden method to handle time updates
    @Override
    public void onTime(long time) {
        // Setting the current time of the game
        this.time = time;
    } // End of the onTime method
}
