package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;
import com.mbrlabs.mundus.commons.terrain.Terrain;

import java.util.Random;

public class Map {
    private final Array<Decal> mapDecals = new Array<>();

    public Map() {
        loadMap();
    }

    public void update() {
        for (Decal decal : mapDecals) {
            MyGdxGame.decalBatch.add(decal);
            DecalHelper.faceCameraPerpendicularToGround(decal, MyGdxGame.scene.cam);
            DecalHelper.applyLighting(decal, MyGdxGame.scene.cam);
        }
    }

    private void loadMap() {
        Random random = new Random();
        TextureRegion ground = new TextureRegion(new Texture(Gdx.files.internal("map1/tree.png")));

        for (int i = 0; i < 50 ; i++){
            Decal decal = Decal.newDecal(100, 120, ground,true);
            int randomX = random.nextInt(500) + 1;
            int randomZ = random.nextInt(500) + 1;
            float height = MyGdxGame.terrain.getHeightAtWorldCoord(randomX, randomZ,new Matrix4()) + DecalHelper.offset(120);

            decal.setPosition(randomX,height,randomZ);
            mapDecals.add(decal);
        }
    }
}