package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;

public class Cthulhu extends Leviathan {
    public Cthulhu() {
        super("cthulhu.png", 500, 500, 1f, 1, 8);
        setPosition(2500, getY(2500, 580) , 580);
    }

    @Override
    public void update() {
        setDecal();
        setPosition(position);

        Vector3 pastPosition = new Vector3(position);
        Vector3 potentialPosition = new Vector3(position);
        float newHeight = getY(potentialPosition.x, potentialPosition.z);

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

        setDirection(chase(Game.mainPlayer.getPosition()));
        Game.decalBatch.add(decal);
        DecalHelper.applyLighting(decal, Game.scene.cam);
        DecalHelper.faceCameraPerpendicularToGround(decal, Game.scene.cam);
    }
}
