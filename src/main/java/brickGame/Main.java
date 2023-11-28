package brickGame;

import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

    private final Object lock = new Object();
    private int level = 0;
    private BackgroundMusicPlayer musicPlayer;
    private double xBreak = 0.0f;
    private double centerBreakX;
    private double yBreak = 880.0f;

    private int breakWidth     = 130;
    private int breakHeight    = 30;
    private int halfBreakWidth = breakWidth / 2;

    private int sceneWidth = 540;
    private int sceneHeight = 960;

    private static int LEFT  = 1;
    private static int RIGHT = 2;

    private Circle ball;
    private double xBall;
    private double yBall;

    private boolean isGoldStatus = false;
    private boolean isExistHeartBlock = false;

    private boolean isBigBall = false;

    private boolean isSmallBall = false;

    private Rectangle rect;
    private int       ballRadius = 20;


    private int destroyedBlockCount = 0;

    private double v = 1.000;

    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;

    private long goldTime = 0;

    private GameEngine engine;
    public static String savePath    = "COMP2042_CW_efyjc21\\src\\main\\save\\save.mdds";
    public static String savePathDir = "COMP2042_CW_efyjc21\\src\\main\\save";


    private ArrayList<Block> blocks = new ArrayList<Block>();
    private ArrayList<Bonus> chocos = new ArrayList<Bonus>();
    private Color[]          colors = new Color[]{
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
    };
    public  Pane             root;
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;

    private boolean loadFromSave = false;

    Stage  primaryStage;
    Button load    = null;
    Button newGame = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        BackgroundMusicPlayer.getInstance().playMusic("src/main/resources/background.wav");

        if (loadFromSave == false) {
            level++;
            if (level >1){
                new Score().showMessage("Level Up :)", this);
            }
            if (level == 20) {
                new Score().showWin(this);
                return;
            }

            initBall();
            initBreak();
            initBoard();

            load = new Button("Load Game");
            newGame = new Button("Start New Game");
            load.setTranslateX(220);
            load.setTranslateY(300);
            newGame.setTranslateX(220);
            newGame.setTranslateY(340);

        }


        root = new Pane();
        scoreLabel = new Label("Score: " + score);
        levelLabel = new Label("Level: " + level);
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + heart);
        heartLabel.setTranslateX(sceneWidth - 80);
        if (loadFromSave == false) {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel, load, newGame);
        } else {
            root.getChildren().addAll(rect, ball, scoreLabel, heartLabel, levelLabel);
        }
        for (Block block : blocks) {
            root.getChildren().add(block.rect);
        }
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(this);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        if (loadFromSave == false) {
            if (level > 1 && level < 20) {
                load.setVisible(false);
                newGame.setVisible(false);
                engine = new GameEngine();
                engine.setOnAction(this);
                engine.setFps(120);
                engine.start();
            }
            synchronized (lock) {
                load.setOnAction(event -> {
                    loadGame();

                    load.setVisible(false);
                    newGame.setVisible(false);
                });
            }


            newGame.setOnAction(event -> {
                engine = new GameEngine();
                engine.setOnAction(Main.this);
                engine.setFps(120);
                engine.start();

                synchronized (lock) {
                    load.setVisible(false);
                    newGame.setVisible(false);
                }

            });
        } else {
            engine = new GameEngine();
            engine.setOnAction(this);
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }


    }

    public void stop() throws Exception {
        super.stop();
        if (musicPlayer != null) {
            musicPlayer.stopMusic();
        }
    }



    private void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_QUESTION;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else if (r % 10 == 4) {
                        if (Math.random() < 0.5) {  // 50% chance for each type
                            type = Block.BLOCK_MINI;
                        } else {
                            type = Block.BLOCK_GIANT;
                        }

                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                //System.out.println("colors " + r % (colors.length));
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:

                move(RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
        }
    }

    float oldXBreak;

    private void move(final int direction) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(30); // Run for 30 cycles

        KeyFrame moveFrame = new KeyFrame(Duration.millis(4), event -> {
            if ((xBreak == (sceneWidth - breakWidth) && direction == RIGHT) || (xBreak == 0 && direction == LEFT)) {
                timeline.stop();
            } else {
                if (direction == RIGHT) {
                    xBreak++;
                } else {
                    xBreak--;
                }
                centerBreakX = xBreak + halfBreakWidth;
            }
        });

        timeline.getKeyFrames().add(moveFrame);
        timeline.play();

    }


    private void initBall() {
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;
        yBall = random.nextInt(sceneHeight - 150 - ((level + 1) * Block.getHeight())) + ((level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("mario.png")));
    }

    private void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        rect.setFill(pattern);
    }

    private boolean goDownBall                   = true;
    private boolean goRightBall                  = true;
    private boolean collideToBreak               = false;
    private boolean collideToBreakAndMoveToRight = true;
    private boolean collideToRightWall           = false;
    private boolean collideToLeftWall            = false;
    private boolean collideToRightBlock          = false;
    private boolean collideToBottomBlock         = false;
    private boolean collideToLeftBlock           = false;
    private boolean collideToTopBlock            = false;
    private boolean colideToTopRightCornerBlock = false; // Flag for collision with a block on the top right corner
    private boolean colideToTopLeftCornerBlock = false; // Flag for collision with a block on the top left corner
    private boolean colideToBottomLeftCornerBlock = false; // Flag for collision with a block on the bottom left corner
    private boolean colideToBottomRightCornerBlock= false; // Flag for collision with a block on the bottom right corner

    private double vX = 3.000;
    private double vY = 3.000;




    private void resetCollideFlags() {

        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;

        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
        colideToBottomLeftCornerBlock = false; // Resetting the flag for collision with bottom left corner block
        colideToBottomRightCornerBlock = false; // Resetting the flag for collision with a bottom right corner block
        colideToTopLeftCornerBlock = false; // Resetting the flag for collision with a top left corner block
        colideToTopRightCornerBlock = false; // Resetting the flag for collision with a top right corner block
    }

    private void setPhysicsToBall() {

        if (goDownBall) {
            yBall += vY;
        } else {
            yBall -= vY;
        }

        if (goRightBall) {
            xBall += vX;
        } else {
            xBall -= vX;
        }

        if (yBall - ballRadius <= 0) {
            //vX = 1.000;
            resetCollideFlags();
            goDownBall = true;
            return;
        }
        if (yBall + ballRadius >= sceneHeight) {
            resetCollideFlags();
            goDownBall = false;
            if (!isGoldStatus) {
                heart--;
                new Score().show(sceneWidth / 2, sceneHeight / 2, -1, this);

                if (heart == 0) {
                    Platform.runLater(() -> {
                        heartLabel.setText("Heart : " + heart);
                        new Score().showGameOver(this);
                        engine.stop();
                    });
                }

            }
            return;
        }

        if (yBall >= yBreak - ballRadius) {
            //System.out.println("Colide1");
            if (xBall >= xBreak && xBall <= xBreak + breakWidth) {
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (xBall - centerBreakX) / (breakWidth / 2);

                if (Math.abs(relation) <= 0.3) {
                    //vX = 0;
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (level / 3.500);
                    //System.out.println("vX " + vX);
                } else {
                    vX = (Math.abs(relation) * 2) + (level / 3.500);
                    //System.out.println("vX " + vX);
                }

                if (xBall - centerBreakX > 0) {
                    collideToBreakAndMoveToRight = true;
                } else {
                    collideToBreakAndMoveToRight = false;
                }
                //System.out.println("Colide2");
            }
        }

        if (xBall + ballRadius >= sceneWidth) {
            resetCollideFlags();
            //vX = 1.000;
            collideToRightWall = true;
        }

        if (xBall - ballRadius <= 0) {
            resetCollideFlags();
            //vX = 1.000;
            collideToLeftWall = true;
        }

        if (collideToBreak) {
            if (collideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
        }

        //Wall Colide

        if (collideToRightWall) {
            goRightBall = false;
        }

        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block Colide

        if (collideToRightBlock) {
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            goRightBall = false;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            goDownBall = true;
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


    }


    private void checkDestroyedCount() {
        if (destroyedBlockCount == blocks.size()) {
            //TODO win level todo...
            //System.out.println("You Win");

            nextLevel();
        }
    }

    private void saveGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new File(savePathDir).mkdirs();
                File file = new File(savePath);
                ObjectOutputStream outputStream = null;
                try {
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));

                    outputStream.writeInt(level);
                    outputStream.writeInt(score);
                    outputStream.writeInt(heart);


                    outputStream.writeDouble(xBall);
                    outputStream.writeDouble(yBall);
                    outputStream.writeDouble(xBreak);
                    outputStream.writeDouble(yBreak);
                    outputStream.writeDouble(centerBreakX);
                    outputStream.writeLong(time);
                    outputStream.writeLong(goldTime);
                    outputStream.writeDouble(vX);


                    outputStream.writeBoolean(isExistHeartBlock);
                    outputStream.writeBoolean(isGoldStatus);
                    outputStream.writeBoolean(goDownBall);
                    outputStream.writeBoolean(goRightBall);
                    outputStream.writeBoolean(collideToBreak);
                    outputStream.writeBoolean(collideToBreakAndMoveToRight);
                    outputStream.writeBoolean(collideToRightWall);
                    outputStream.writeBoolean(collideToLeftWall);
                    outputStream.writeBoolean(collideToRightBlock);
                    outputStream.writeBoolean(collideToBottomBlock);
                    outputStream.writeBoolean(collideToLeftBlock);
                    outputStream.writeBoolean(collideToTopBlock);
                    outputStream.writeBoolean(colideToTopLeftCornerBlock); // Saving the collision state with a top-left corner block
                    outputStream.writeBoolean(colideToBottomLeftCornerBlock); // Saving the collision state with a bottom-left corner block
                    outputStream.writeBoolean(colideToTopRightCornerBlock); // Saving the collision state with a top-right corner block
                    outputStream.writeBoolean(colideToBottomRightCornerBlock); // Saving the collision state with a bottom-right corner block

                    ArrayList<BlockSerializable> blockSerializables = new ArrayList<BlockSerializable>();
                    for (Block block : blocks) {
                        if (block.isDestroyed) {
                            continue;
                        }
                        blockSerializables.add(new BlockSerializable(block.row, block.column, block.type));
                    }

                    outputStream.writeObject(blockSerializables);

                    new Score().showMessage("Game Saved", Main.this);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStauts;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToBreak = loadSave.colideToBreak;
        collideToBreakAndMoveToRight = loadSave.colideToBreakAndMoveToRight;
        collideToRightWall = loadSave.colideToRightWall;
        collideToLeftWall = loadSave.colideToLeftWall;
        collideToRightBlock = loadSave.colideToRightBlock;
        collideToBottomBlock = loadSave.colideToBottomBlock;
        collideToLeftBlock = loadSave.colideToLeftBlock;
        collideToTopBlock = loadSave.colideToTopBlock;
        colideToTopLeftCornerBlock = loadSave.colideToTopLeftCornerBlock; // Setting the collision state with a top-left corner block
        colideToBottomLeftCornerBlock = loadSave.colideToBottomLeftCornerBlock; // Setting the collision state with a bottom-left corner block
        colideToTopRightCornerBlock = loadSave.colideToTopRightCornerBlock; // Setting the collision state with a top-right corner block
        colideToBottomRightCornerBlock = loadSave.colideToBottomRightCornerBlock; // Setting the collision state with a bottom-right corner block
        level = loadSave.level;
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        xBall = loadSave.xBall;
        yBall = loadSave.yBall;
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        blocks.clear();
        chocos.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            blocks.add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


        try {
            loadFromSave = true;
            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void nextLevel() {
        vX = 3.000;
        vY = 3.000;
        ballRadius = 20;
        isGoldStatus = false;
        Platform.runLater(() -> {
            try {

                resetCollideFlags();
                goDownBall = true;


                isExistHeartBlock = false;


                time = 0;
                goldTime = 0;

                engine.stop();
                blocks.clear();
                chocos.clear();
                destroyedBlockCount = 0;
                start(primaryStage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void restartGame() {
        try {
            level = 0;
            heart = 3;
            score = 0;
            vX = 3.000;
            destroyedBlockCount = 0;
            resetCollideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            time = 0;
            goldTime = 0;

            blocks.clear();
            chocos.clear();

            start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeBallSize(int newRadius) {
        ballRadius = newRadius;
        // Assuming 'ball' is your Circle object for the ball
        ball.setRadius(ballRadius);
        // Update any other necessary attributes related to ball size
    }



    @Override
    public void onUpdate() {
        Platform.runLater(() -> {
            synchronized (lock) {
            scoreLabel.setText("Score: " + score);
            heartLabel.setText("Heart : " + heart);

            rect.setX(xBreak);
            rect.setY(yBreak);
            ball.setCenterX(xBall);
            ball.setCenterY(yBall);

            for (Bonus choco : chocos) {
                choco.choco.setY(choco.y);
            }
            }
        });

        synchronized (lock) {
            if (yBall >= Block.getPaddingTop() && yBall <= (Block.getHeight() * (level + 1)) + Block.getPaddingTop()) {
                for (final Block block : blocks) {
                    int hitCode = block.checkHitToBlock(xBall, yBall, ballRadius);
                    if (hitCode != Block.NO_HIT) {
                        score += 1;

                        new Score().show(block.x, block.y, 1, this);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        destroyedBlockCount++;
                        //System.out.println("size is " + blocks.size());
                        resetCollideFlags();

                        if (block.type == Block.BLOCK_QUESTION) {
                            final Bonus choco = new Bonus(block.row, block.column);
                            choco.timeCreated = time;
                            Platform.runLater(() -> root.getChildren().add(choco.choco));
                            chocos.add(choco);
                        }

                        if (block.type == Block.BLOCK_STAR) {
                            if (isGoldStatus == true){

                            }
                                else {
                                ball.setFill(new ImagePattern(new Image("starmario.png")));
                                BackgroundMusicPlayer.getInstance().playMusic("src/main/resources/golden.wav");
                                isGoldStatus = true;
                                System.out.println("gold ball");
                            }
                        }


                        if (block.type == Block.BLOCK_GIANT) {
                            if (isSmallBall == true){

                            }
                            else {
                                changeBallSize(100);  // Increase ball size
                                isBigBall = true;
                                // Start a timer to reset the size after a delay
                                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                                delay.setOnFinished(event -> {
                                    isBigBall = false;
                                    changeBallSize(20); // Reset to original size
                                });  // Reset ball size
                                delay.play();
                            }

                        }

                        if (block.type == Block.BLOCK_MINI) {
                            if (isBigBall == true){

                            }
                            else{
                                changeBallSize(5);  // decrease ball size
                                isSmallBall = true;
                                // Start a timer to reset the size after a delay
                                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                                delay.setOnFinished(event -> {
                                    isSmallBall = false;
                                    changeBallSize(20); // Reset to original size
                                });  // Reset ball size
                                delay.play();
                            }

                        }







                        if (block.type == Block.BLOCK_HEART) {
                            heart++;
                        }

                        if (hitCode == Block.HIT_RIGHT) {
                            collideToRightBlock = true; // Setting the flag for collision with a block on the right
                        } else if (hitCode == Block.HIT_BOTTOM) {
                            collideToBottomBlock = true; // Setting the flag for collision with a block on the bottom
                        } else if (hitCode == Block.HIT_LEFT) {
                            collideToLeftBlock = true; // Setting the flag for collision with a block on the left
                        } else if (hitCode == Block.HIT_TOP) {
                            collideToTopBlock = true; // Setting the flag for collision with a block on the top
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


                }

                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }



    @Override
    public void onInit() {

    }

//    private boolean isSpeedEffectActive = false;

//
//    private void resetSpeedEffect() {
//            isSpeedEffectActive = false;
//            vX = 3;
//            vY = 3;
//            ball.setFill(new ImagePattern(new Image("mario.png")));
//            System.out.println("normal ball");
//            BackgroundMusicPlayer.getInstance().playMusic("src/main/resources/background.wav");

 //   }

//    private void applySpeedEffect() {
//            isSpeedEffectActive = true;
//            vX = 10;
//            vY = 10;
//            ball.setFill(new ImagePattern(new Image("speedball.png")));
//            System.out.println("Speed ball");
//            BackgroundMusicPlayer.getInstance().playMusic("src/main/resources/speed.wav");
//
//
//    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();


        if (isGoldStatus == true) {
            Platform.runLater(() -> {
                PauseTransition delay = new PauseTransition(Duration.seconds(10));
                delay.setOnFinished(event -> {
                    isGoldStatus = false;
                    BackgroundMusicPlayer.getInstance().playMusic("src/main/resources/background.wav");
                    ball.setFill(new ImagePattern(new Image("mario.png")));
                });
                delay.play();
            });
        }

        for (Bonus choco : chocos) {

            if (choco.y > sceneHeight || choco.taken) {
                continue;
            }
            if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= xBreak && choco.x <= xBreak + breakWidth) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                Platform.runLater(() -> {
                            choco.choco.setVisible(false);
                        });
                score += 3;
                new Score().show(choco.x, choco.y, 3, this);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }



    }


    @Override
    public void onTime(long time) {
        this.time = time;
    }
}
