package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class Python extends Leviathan {
    public Python() {
        super("python_head.png", 200, 200, 2f, 3, 8);
        System.out.println(DecalHelper.offset(200));
        setPosition(860, getY(860, 440) , 440);
    }

    @Override
    public void update() {
        setDecal();
        setPosition(position);

        Vector3 pastPosition = new Vector3(position);
        Vector3 potentialPosition = new Vector3(position);
        float newHeight = getY(potentialPosition.x, potentialPosition.z);

        stateTime += Gdx.graphics.getDeltaTime();
        if (stateTime > animationSpeed) {
            spriteCtr++;
            if (spriteCtr > 2) {
                spriteCtr = 0;
            }
            stateTime = 0;
        }

        if (newHeight - pastPosition.y > 12) {
            setPosition(pastPosition);
        } else {
            if (potentialPosition.x < 0) potentialPosition.x = 0;
            if (potentialPosition.x > MyGdxGame.terrain.terrainWidth) potentialPosition.x = MyGdxGame.terrain.terrainWidth;
            if (potentialPosition.z < 0) potentialPosition.z = 0;
            if (potentialPosition.z > MyGdxGame.terrain.terrainWidth) potentialPosition.z = MyGdxGame.terrain.terrainWidth;
            potentialPosition.y = newHeight;
            setPosition(potentialPosition);
        }

        setDirection(chase(MyGdxGame.mainPlayer.getPosition()));

        MyGdxGame.decalBatch.add(decal);
        DecalHelper.applyLighting(decal, MyGdxGame.scene.cam);
        DecalHelper.faceCameraPerpendicularToGround(decal, MyGdxGame.scene.cam);
    }
}
