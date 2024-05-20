package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.mbrlabs.mundus.commons.terrain.Terrain;

import java.util.Random;

public class Monster {
    private final Vector3 position;
    private final float velocity;
    private final int width;
    int height;
    public Decal decal;
    public Monster(String path, float velocity, int width, int height) {
        this.velocity = velocity;
        this.width = width;
        this.height = height;
        position = new Vector3();

        setDecal(path).setPosition();
        // 174 x 606
        // 215 x 584
    }

    public Vector3 getPosition() {
        return position;
    }

    public Monster setPosition() {
        Random random = new Random();
        float randomX = random.nextFloat(500) + 1;
        float randomZ = random.nextFloat(500) + 1;
        return this.setPosition(randomX, getY(), randomZ);
    }

    public Monster setPosition(float x, float y, float z) {
        position.set(x, y, z);
        decal.setPosition(position);
        return this;
    }

    public float getY() {
        return MyGdxGame.terrain.getHeightAtWorldCoord(position.x, position.z, new Matrix4()) + DecalHelper.offset(height);
    }

    public void chase(Vector3 player) {
        float x = getPosition().x;
        float z = getPosition().z;

        if (position.dst(player) < 60) {
            setPosition(x, getY(), z);
            return;
        }

        if (x < player.x) {
            x += velocity;
        } else if (x > player.x) {
            x -= velocity;
        }

        if (z < player.z) {
            z += velocity;
        } else if (getPosition().z > player.z) {
            z -= velocity;
        }

        setPosition(x, getY(), z);
    }

    public Monster setDecal(String path) {
        TextureRegion region = new TextureRegion(new Texture(Gdx.files.internal("monsters/" + path)));
        decal = Decal.newDecal(width, height, region, true);
        return this;
    }
}