package brickGame;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;


/**
 * The Sound class is responsible for handling audio functionalities in the game.
 * It includes methods to play background music, sound effects, and to stop music.
 */
public class Sound {
    private MediaPlayer backgroundMusicPlayer;
    private MediaPlayer soundEffectPlayer;


    /**
     * Plays the background music from a specified path.
     *
     * @param backgroundMusicPath The path to the background music file.
     * @param volume The volume at which to play the music (range: 0.0 to 1.0).
     */
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


    /**
     * Plays a sound effect from a specified path.
     *
     * @param soundEffectPath The path to the sound effect file.
     * @param volume The volume at which to play the sound effect (range: 0.0 to 1.0).
     */
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

    /**
     * Stops any currently playing music.
     */
    public void stopMusic(){
        backgroundMusicPlayer.stop();
        soundEffectPlayer.stop();
    }

}
