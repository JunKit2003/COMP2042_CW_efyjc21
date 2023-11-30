package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
public class Sound {
    private MediaPlayer backgroundMusicPlayer;
    private MediaPlayer soundEffectPlayer;


    public void playBackgroundMusic(String backgroundMusicPath, double volume) {
        // If there's already music playing, stop it
        if (backgroundMusicPlayer != null) {
            backgroundMusicPlayer.stop();
        }

        try {
            URL resource = getClass().getResource("/" + backgroundMusicPath);
            if (resource == null) {
                throw new IllegalArgumentException("Cannot find file: " + backgroundMusicPath);
            }

            Media backgroundMusic = new Media(resource.toExternalForm());
            backgroundMusicPlayer = new MediaPlayer(backgroundMusic);
            backgroundMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            backgroundMusicPlayer.setVolume(volume);
            backgroundMusicPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing background music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void playSoundEffect(String soundEffectPath, double volume) {
        try {
            URL resource = getClass().getResource("/" + soundEffectPath);
            if (resource == null) {
                throw new IllegalArgumentException("Cannot find file: " + soundEffectPath);
            }

            Media soundEffect = new Media(resource.toExternalForm());
            soundEffectPlayer = new MediaPlayer(soundEffect);
            soundEffectPlayer.setVolume(volume);
            soundEffectPlayer.play();
        } catch (Exception e) {
            System.err.println("Error playing sound effect: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stopMusic(){
        backgroundMusicPlayer.stop();
        soundEffectPlayer.stop();
    }

}
