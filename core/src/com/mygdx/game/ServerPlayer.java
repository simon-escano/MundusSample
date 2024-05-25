package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

public class ServerPlayer {
    private Vector3 position;
    private Entity.Direction direction;
    private Entity.State state;
    private final String color;

    public ServerPlayer() {
        direction = Entity.Direction.N;
        state = Entity.State.IDLE;
        position = new Vector3(50, 15, 50);
        color = "Red";
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
        return position.toString() + "\n" + direction.toString() + "\n" + state.toString();
    }
}
