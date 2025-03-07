package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

public class MapDecals {
    private Array<Decal> mapDecals = new Array<>();
    private final TextureRegion[][] trees;

    public MapDecals() {
        mapDecals = new Array<>();
        Texture spriteSheet = new Texture(Gdx.files.internal("map_decals/trees.png"));
        trees = TextureRegion.split(spriteSheet, spriteSheet.getWidth() / 4, spriteSheet.getHeight() / 5);
        // starting load map...
        MapLoader mapLoader = new MapLoader(mapDecals, trees);
        Thread thread = new Thread(mapLoader);
        thread.start();

        try {
            thread.join(); // waiting for thread map to complete
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        for (Decal decal : mapDecals) {
            Game.decalBatch.add(decal);
            DecalHelper.faceCameraPerpendicularToGround(decal, Game.scene.cam);
            DecalHelper.applyLighting(decal, Game.scene.cam);
        }
    }

    private void loadMap() {
        for (int i = 0; i < 300 ; i++){
            Decal decal = Decal.newDecal(100, 120, trees[(int) (Math.random() * 4)][(int) (Math.random() * 4)],true);
            int randomX = (int) (Math.random() * ((2100 - 150) + 1)) + 150;
            int randomZ = (int) (Math.random() * ((2800 - 2130) + 1)) + 2130;
            float height = Game.terrain.getHeightAtWorldCoord(randomX, randomZ, new Matrix4()) + DecalHelper.offset(120);

            decal.setPosition(randomX,height,randomZ);
            mapDecals.add(decal);
        }
    }
}