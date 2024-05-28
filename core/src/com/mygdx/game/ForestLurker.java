package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class ForestLurker extends Leviathan {
    public ForestLurker() {
        super("forest_lurker.png", 100, 100, 2f, 4, 8);
        setPosition(1500, getY(1500, 2400) , 2400);
    }
}
