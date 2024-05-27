package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class ForestLurker extends Leviathan {
    public ForestLurker() {
        super("forest_lurker.png", 100, 100, 2f, 4, 8);
        setPosition(1500, getY(1500, 2400) , 2400);
    }

    @Override
    public void update() {
        setDecal();
        setPosition(position);

        Vector3 pastPosition = new Vector3(position);
        Vector3 potentialPosition = new Vector3(position);
        float newHeight = getY(potentialPosition.x, potentialPosition.z);

        switch (state) {
            case IDLE:
                spriteCtr = 0;
                break;
            case SPRINTING:
            case WALKING:
                stateTime += Gdx.graphics.getDeltaTime();
                if (stateTime > animationSpeed) {
                    spriteCtr++;
                    if (spriteCtr > 3) {
                        spriteCtr = 0;
                    }
                    stateTime = 0;
                }
                break;
        }

        if (newHeight - pastPosition.y > 12) {
            setPosition(pastPosition);
        } else {
            if (potentialPosition.x < 0) potentialPosition.x = 0;
            if (potentialPosition.x > Game.terrain.terrainWidth) potentialPosition.x = Game.terrain.terrainWidth;
            if (potentialPosition.z < 0) potentialPosition.z = 0;
            if (potentialPosition.z > Game.terrain.terrainWidth) potentialPosition.z = Game.terrain.terrainWidth;
            potentialPosition.y = newHeight;
            setPosition(potentialPosition);
        }

        if (position.dst(Game.mainPlayer.getPosition()) < 500) {
            setDirection(chase(Game.mainPlayer.getPosition()));
            setState(State.WALKING);
        } else {
            setState(State.IDLE);
        }

        Game.decalBatch.add(decal);
        DecalHelper.applyLighting(decal, Game.scene.cam);
        DecalHelper.faceCameraPerpendicularToGround(decal, Game.scene.cam);
    }
}
