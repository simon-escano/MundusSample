package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;

public class DecalHelper {

    public static void faceCameraPerpendicularToGround(Decal decal, Camera camera) {
        // Get the camera and decal positions
        Vector3 cameraPosition = new Vector3(camera.position);
        Vector3 decalPosition = new Vector3(decal.getPosition());

        // Calculate the direction vector from the decal to the camera
        Vector3 direction = cameraPosition.sub(decalPosition);

        // Zero out the Y component to keep the decal upright
        direction.y = 0;

        // Normalize the direction vector
        direction.nor();

        // Calculate the rotation quaternion to face the camera while staying perpendicular to the ground
        Quaternion rotation = new Quaternion();
        rotation.setFromCross(Vector3.Z, direction);

        // Set the decal's rotation
        decal.setRotation(rotation);
    }
}
