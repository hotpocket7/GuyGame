package game.sound;

import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;

import java.util.ArrayList;

public class Song {

    public static Song hubSong = new Song("/sound/music/hub.ogg", 0.4);

    public static Song jumpAreaSong = new Song("/sound/music/jumpStage.ogg", 0.2);
    public static Song boss1 = new Song("/sound/music/boss1.ogg", 0.5);

    private double maxVolume;
    private Music music;

    private static ArrayList<Song> playingSongs = new ArrayList<Song>();

    public Song(String path, double maxVolume) {
        music = TinySound.loadMusic(path);
        this.maxVolume = maxVolume;
    }

    public static void pauseAll() {
        playingSongs.forEach(Song::pause);
    }

    public double getMaxVolume() {
        return maxVolume;
    }

    public void play(boolean loop, double volume) {
        music.play(loop, volume);
        playingSongs.add(this);
    }

    public void stop() {
        music.stop();
    }

    public void pause() {
        music.pause();
    }

    public void resume() {
        music.resume();
    }

    public void setVolume(double volume) {
        music.setVolume(volume);
    }

    public double getVolume() {
        return music.getVolume();
    }

    public boolean playing() {
        return music.playing();
    }

    public boolean done() {
        return music.done();
    }
}
