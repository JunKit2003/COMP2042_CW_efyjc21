package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
//import sun.plugin2.message.Message;

public class Score {

    public void show(final double x, final double y, int score, final Main main) {
        String sign = (score >= 0) ? "+" : "";
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> main.root.getChildren().add(label));

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    final int setI = i;
                    Platform.runLater(() -> {
                        label.setScaleX(setI);
                        label.setScaleY(setI);
                        label.setOpacity((20 - setI) / 20.0);
                    });
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showMessage(String message, final Main main) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> main.root.getChildren().add(label));

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    final int setI = i;
                    Platform.runLater(() -> {
                        label.setScaleX(Math.abs(setI - 10));
                        label.setScaleY(Math.abs(setI - 10));
                        label.setOpacity((20 - setI) / 20.0);
                    });
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void showGameOver(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("Game Over :(");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> main.restartGame());

            main.root.getChildren().addAll(label, restart);
        });
    }

    public void showWin(final Main main) {
        Platform.runLater(() -> {
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Start New Game");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> main.restartGame());

            main.root.getChildren().addAll(label, restart);
        });
    }
}

