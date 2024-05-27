package com.mygdx.game;

import com.badlogic.gdx.math.Matrix4;

public class Utils {
    public static Entity.Direction vectorToDirection(float x, float z) {
        float angle = (float) Math.toDegrees(Math.atan2(x, z));
        return angleToDirection(angle);
    }

    public static Entity.Direction angleToDirection(float angle) {
        if (angle < 0) {
            angle += 360;
        }

        if (angle >= 337.5 || angle < 22.5) {
            return Entity.Direction.E;
        } else if (angle >= 22.5 && angle < 67.5) {
            return Entity.Direction.NE;
        } else if (angle >= 67.5 && angle < 112.5) {
            return Entity.Direction.N;
        } else if (angle >= 112.5 && angle < 157.5) {
            return Entity.Direction.NW;
        } else if (angle >= 157.5 && angle < 202.5) {
            return Entity.Direction.W;
        } else if (angle >= 202.5 && angle < 247.5) {
            return Entity.Direction.SW;
        } else if (angle >= 247.5 && angle < 292.5) {
            return Entity.Direction.S;
        } else if (angle >= 292.5 && angle < 337.5) {
            return Entity.Direction.SE;
        }
        return null;
    }

    public static float getHeight(float x, float z) {
        return Game.terrain.getHeightAtWorldCoord(x, z, new Matrix4());
    }
}
