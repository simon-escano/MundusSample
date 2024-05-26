package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;

public class DecalHelper {

    public static void faceCameraPerpendicularToGround(Decal decal, Camera camera) {
        Vector3 cameraPosition = new Vector3(camera.position);
        Vector3 decalPosition = new Vector3(decal.getPosition());
        Vector3 direction = cameraPosition.sub(decalPosition);
        direction.y = 0;
        direction.nor();
        Quaternion rotation = new Quaternion();
        rotation.setFromCross(Vector3.Z, direction);
        decal.setRotation(rotation);
    }

    public static void applyLighting(Decal decal, Camera camera) {
        float renderDistance = 700f;
        float distance = camera.position.dst(decal.getPosition());
        float darkness = 0.625f - (distance / renderDistance);
        darkness = Math.max(darkness, 0.1f);
        decal.setColor(new Color(darkness, darkness, darkness, 1f));
    }

    public static float offset(int height) {
        return ((float) height / 2) - 58;
    }
}