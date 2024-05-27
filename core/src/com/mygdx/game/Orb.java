package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector3;

public class Orb {
    Vector3 position;
    public Decal decal;
    boolean isCollected;

    public Orb(float x, float z) {
        position = new Vector3();
        position.set(x, Utils.getHeight(x, z), z);
        decal = Decal.newDecal( 40, 40, new TextureRegion(new Texture("orb.png")), true);
        decal.setPosition(position);
        isCollected = false;
    }

    public void pickup() {
        isCollected = true;
    }

    public void update() {
        if (!isCollected) {
            MyGdxGame.decalBatch.add(decal);
            DecalHelper.faceCameraPerpendicularToGround(decal, MyGdxGame.scene.cam);
        }
    }
}
