package brickGame; // Package declaration

public class GameEngine { // Class responsible for managing the game's core logic

    private OnAction onAction; // Interface for handling actions within the game
    private int fps = 15; // Frames per second for the game's update rate
    private Thread updateThread; // Threads for handling game updates and physics
    private Thread physicsThread; // Threads for handling game updates and physics
    public boolean isStopped = true; // Flag to check if the game is stopped

    public void setOnAction(OnAction onAction) { // Method to set the action handler
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and we convert it to millisecond
     */
    public void setFps(int fps) { // Method to set the game's FPS (frames per second)
        this.fps = (int) 1000 / fps;
    }

    private synchronized void Update() { // Synchronized method for updating game state
        // Thread for updating the game state
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!updateThread.isInterrupted()) {
                    try {
                        onAction.onUpdate(); // Call the update action
                        Thread.sleep(fps); // Sleep based on FPS setting
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        updateThread.start(); // Start the update thread
    }

    private void Initialize() {
        onAction.onInit(); // Initialize the game state
    }

    private synchronized void PhysicsCalculation() {
        // Thread for physics calculations
        physicsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!physicsThread.isInterrupted()) {
                    try {
                        onAction.onPhysicsUpdate(); // Perform physics calculations
                        Thread.sleep(fps); // Sleep based on FPS setting
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        physicsThread.start(); // Start the physics thread
    }

    public void start() {
        time = 0;
        Initialize(); // Initialize the game
        Update(); // Start updating the game
        PhysicsCalculation(); // Start physics calculations
        TimeStart(); // Start the time thread
        isStopped = false; // Mark the game as started
    }

    public void stop() {
        // Stop the game if it's running
        if (!isStopped) {
            isStopped = true;
            updateThread.stop(); // Stop the update thread
            physicsThread.stop(); // Stop the physics thread
            timeThread.stop(); // Stop the time thread
        }
    }

    private long time = 0; // Variable for tracking time

    private Thread timeThread; // Thread for handling time updates

    private void TimeStart() {
        // Thread for tracking time
        timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        time++; // Increment time
                        onAction.onTime(time); // Update time in the game
                        Thread.sleep(1); // Sleep for a millisecond
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        timeThread.start(); // Start the time thread
    }

    // Interface definition for actions in the game
    public interface OnAction {
        void onUpdate(); // Method for update actions
        void onInit(); // Method for initialization actions
        void onPhysicsUpdate(); // Method for physics update actions
        void onTime(long time); // Method for time update actions
    }

}
