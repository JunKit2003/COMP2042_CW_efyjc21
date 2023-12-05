package brickGame;

/**
 * The GameEngine class is responsible for managing the game's main loop and physics updates.
 * It controls the game flow, updating game states, and rendering frames at a set frames per second (fps).
 */

    public class GameEngine {

        /**
         * The action to be executed on each frame update.
         */
        private OnAction onAction;
        private int fps = 15;

        /**
         * The thread responsible for updating game states.
         */
        private Thread updateThread;

        /**
         * The thread responsible for managing game physics.
         */
        private Thread physicsThread;
        public volatile boolean isStopped = true;


        /**
         * Sets the action to be executed on each frame update.
         *
         * @param onAction The action to be executed.
         */
        public void setOnAction(OnAction onAction) {
            this.onAction = onAction;
        }

        /**
         * Sets the frames per second for the game updates.
         *
         * @param fps The desired frames per second.
         */
        public void setFps(int fps) {
            this.fps = (int) 1000 / fps;
        }

        private synchronized void Update() {
            updateThread = new Thread(() -> {
                while (!updateThread.isInterrupted()) {
                    try {
                        onAction.onUpdate();
                        Thread.sleep(fps);
                    } catch (InterruptedException e) {
                        System.out.println("onUpdate thread interrupted.");
                        Thread.currentThread().interrupt();
                    }
                }
            });
            updateThread.start();
        }

        private void Initialize() {
            onAction.onInit();
        }

        private synchronized void PhysicsCalculation() {
            physicsThread = new Thread(() -> {
                while (!physicsThread.isInterrupted()) {
                    try {
                        onAction.onPhysicsUpdate();
                        Thread.sleep(fps);
                    } catch (InterruptedException e) {
                        System.out.println("onPhysics thread interrupted.");
                        Thread.currentThread().interrupt();
                    }
                }
            });

            physicsThread.start();

        }

        /**
         * Starts the game loop.
         */
        public void start() {
            time = 0;
            Initialize();
            Update();
            PhysicsCalculation();
            TimeStart();
            isStopped = false;
        }

        /**
         * Starts the game loop with a specified delay.
         *
         * @param t The delay time in milliseconds before starting the game loop.
         */
        public void start(long t) {
            time = t;
            Initialize();
            Update();
            PhysicsCalculation();
            TimeStart();
            isStopped = false;
        }

        /**
         * Stops the game loop and terminates all running threads.
         */
        public void stop() {
            if (!isStopped) {
                isStopped = true;
                updateThread.interrupt();
                physicsThread.interrupt();
                timeThread.interrupt();
            }
        }

        private long time = 0;

        /**
         * The thread responsible for time-based events.
         */
        private Thread timeThread;

        private synchronized void TimeStart() {
            timeThread = new Thread(() -> {
                try {
                    while (true) {
                        time++;
                        onAction.onTime(time);
                        Thread.sleep(1);
                    }
                } catch (InterruptedException e) {
                    System.out.println("TimeStart thread interrupted.");
                    Thread.currentThread().interrupt();
                }
            });
            timeThread.start();
        }


        public interface OnAction {
            void onUpdate();

            void onInit();

            void onPhysicsUpdate();

            void onTime(long time);
        }

    }
