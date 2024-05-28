package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class GameState {
    private boolean inProgress;
    String title;
    Orb orb;
    Leviathan leviathan;
    Sound music;
    public GameState(String title, float orbX, float orbZ, Leviathan leviathan) {
        inProgress = false;
        this.title = title;
        orb = new Orb(orbX, orbZ);
        this.leviathan = leviathan;
    }

    public GameState(String title) {
        inProgress = false;
        this.title = title;
    }

    public GameState(String title, float orbX, float orbZ) {
        this(title, orbX, orbZ, null);
    }

    public GameState(String title, Leviathan leviathan) {
        inProgress = false;
        this.title = title;
        this.leviathan = leviathan;
    }

    public void start() {
        inProgress = true;
        setMusic("sounds/chapter_3.mp3");
        music.play(0.5f);
    }

    public void end() {
        inProgress = false;
        music.stop();
        if (title.equals("Chapter 3: The Awakening")) {
            Gdx.app.exit();
        }
        if (Game.gameStateIndex < Game.gameStates.size()) {
            Game.client.sendTCP(Game.gameStateIndex + 1);
        }
    }

    public void update() {
        if (!inProgress) {
            return;
        }
        if (orb != null) {
            if (orb.isCollected) {
                end();
            } else {
                orb.update();
                if (leviathan != null) {
                    leviathan.update();
                }
            }
        } else {
            if (leviathan != null) {
                leviathan.update();
            }
        }
    }

    public void setMusic(String filepath) {
        music = Gdx.audio.newSound(Gdx.files.internal(filepath));
    }
}
