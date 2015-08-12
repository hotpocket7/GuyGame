package game.sound;

import kuusisto.tinysound.*;

public final class Sounds {
    public static Sound save = TinySound.loadSound("/sound/save.ogg");

    public static Sound playerJump = TinySound.loadSound("/sound/playerJump.wav");
    public static Sound playerDoubleJump = TinySound.loadSound("/sound/playerDoubleJump.wav");
    public static Sound playerDeath = TinySound.loadSound("/sound/playerDeath.wav");

    public static Sound pickupJumpRefresher = TinySound.loadSound("/sound/pickupJumpRefresher.wav");
}
