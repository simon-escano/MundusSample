package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

public class ServerLeviathan {
    public String name;
    public Vector3 position;
    public Entity.Direction direction;
    public Entity.State state;

    public ServerLeviathan() {}

    public ServerLeviathan(float x, float z) {
        position = new Vector3();
        position.set(x, Utils.getHeight(x, z) + DecalHelper.offset(200), z);
        direction = Entity.Direction.N;
        state = Entity.State.IDLE;
    }
}