package com.mygdx.game;

import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MapLoader implements Runnable {
    private final Array<Decal> mapDecals;
    private final TextureRegion[][] trees;

    public MapLoader(Array<Decal> mapDecals, TextureRegion[][] trees) {
        this.mapDecals = mapDecals;
        this.trees = trees;
    }

    @Override
    public void run() {
        for (int i = 0; i < 300; i++) {
            Decal decal = Decal.newDecal(100, 120, trees[(int) (Math.random() * 4)][(int) (Math.random() * 4)], true);
            int randomX = (int) (Math.random() * ((2100 - 150) + 1)) + 150;
            int randomZ = (int) (Math.random() * ((2800 - 2130) + 1)) + 2130;
            float height = Game.terrain.getHeightAtWorldCoord(randomX, randomZ, new Matrix4()) + DecalHelper.offset(120);

            decal.setPosition(randomX, height, randomZ);
            mapDecals.add(decal);
        }
    }
}
