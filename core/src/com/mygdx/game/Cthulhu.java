package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

public class Cthulhu extends Leviathan {
    public Cthulhu() {
        super("cthulhu.png", 500, 500, 2f, 1, 8);
        setPosition(1441, getY(1441, 566) , 566);
    }
}
