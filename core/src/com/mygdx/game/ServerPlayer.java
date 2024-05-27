package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;
import java.util.Random;

public class ServerPlayer implements Serializable {
    private int id;
    private Vector3 position;
    private Entity.Direction direction;
    private Entity.State state;
    private String color;

    public ServerPlayer() {}

    public ServerPlayer(int id, String color) {
        this.id = id;
        direction = Entity.Direction.N;
        state = Entity.State.IDLE;
        position = new Vector3();
        position.set(260, Utils.getHeight(260, 230), 230);
        this.color = color;
    }

    public int getID() {
        return id;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setDirection(Entity.Direction direction) {
        this.direction = direction;
    }

    public Entity.Direction getDirection() {
        return direction;
    }

    public void setState(Entity.State state) {
        this.state = state;
    }

    public Entity.State getState() {
        return state;
    }


    public String getColor() {
        return color;
    }

    public String toString() {
        return "Position: " + position.toString() + "\nDirection: " + direction.toString() + "\nState: " + state.toString();
    }
}
