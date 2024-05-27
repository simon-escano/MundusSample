package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

public abstract class Leviathan extends Entity {
    private float detectionThreshold = 12.0f;
    private float avoidanceDistance = 5.0f;
    public Leviathan(String spriteSheetPath, int spriteWidth, int spriteHeight, float velocity) {
        super("leviathans/" + spriteSheetPath, spriteWidth, spriteHeight, velocity);
    }

    public void update() {
    }

    public Leviathan(String spriteSheetPath, int spriteWidth, int spriteHeight, float velocity, int FRAME_ROWS, int FRAME_COLS) {
        super("leviathans/" + spriteSheetPath, spriteWidth, spriteHeight, velocity, FRAME_ROWS, FRAME_COLS);
    }

    public Direction chase(Vector3 player) {
        Vector3 currentPosition = new Vector3(getPosition());
        Vector3 direction = new Vector3(player).sub(currentPosition).nor();
        Vector3 potentialPosition = new Vector3(currentPosition).add(direction.scl(getVelocity()));

        float currentHeight = Utils.getHeight(currentPosition.x, currentPosition.z);
        float newHeight = Utils.getHeight(potentialPosition.x, potentialPosition.z);

        if (newHeight - currentHeight > detectionThreshold) {
            avoidBump(direction);
        } else {
            setPosition(potentialPosition);
        }

        return Utils.vectorToDirection(direction.x, direction.z);
    }

    private void avoidBump(Vector3 direction) {
        Vector3 leftDirection = new Vector3(-direction.z, direction.y, direction.x);
        Vector3 leftPosition = new Vector3(getPosition()).add(leftDirection.scl(avoidanceDistance));
        if (isValidPosition(leftPosition)) {
            setPosition(leftPosition);
            return;
        }

        Vector3 rightDirection = new Vector3(direction.z, direction.y, -direction.x);
        Vector3 rightPosition = new Vector3(getPosition()).add(rightDirection.scl(avoidanceDistance));
        if (isValidPosition(rightPosition)) {
            setPosition(rightPosition);
        }
    }

    private boolean isValidPosition(Vector3 position) {
        float newHeight = Utils.getHeight(position.x, position.z);
        float currentHeight = Utils.getHeight(getPosition().x, getPosition().z);
        return Math.abs(newHeight - currentHeight) <= detectionThreshold;
    }
}