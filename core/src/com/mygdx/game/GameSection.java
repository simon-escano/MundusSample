package com.mygdx.game;

public class GameSection {
    private boolean inProgress;
    String title;
    Orb orb;
    Leviathan leviathan;
    public GameSection(String title, float orbX, float orbZ, Leviathan leviathan) {
        inProgress = false;
        this.title = title;
        orb = new Orb(orbX, orbZ);
        this.leviathan = leviathan;
    }

    public GameSection(String title) {
        inProgress = false;
        this.title = title;
    }

    public void start() {
        inProgress = true;
    }

    public void end() {
        inProgress = false;
        if (MyGdxGame.gameSectionIndex < 3) {
            MyGdxGame.gameSectionIndex++;
            MyGdxGame.gameSections.get(MyGdxGame.gameSectionIndex).start();
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
                leviathan.update();
                orb.update();
            }
        }
    }
}
