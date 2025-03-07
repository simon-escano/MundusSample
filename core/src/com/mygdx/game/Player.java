package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;

public class Player extends Entity {
    public ServerPlayer serverPlayer;
    private boolean needsSpriteUpdate;

    public Player(ServerPlayer serverPlayer) {
        super("astronauts/astronaut" + serverPlayer.getColor() + ".png", 80, 80, 0.5f);
        this.serverPlayer = serverPlayer;
        this.position = serverPlayer.getPosition();
        this.direction = serverPlayer.getDirection();
        this.state = serverPlayer.getState();
        this.needsSpriteUpdate = true;
    }

    public Player() {
        super("", 0, 0, 0);
    }

    public void update() {
        setPosition(serverPlayer.getPosition());
        setDirection(serverPlayer.getDirection());
        setState(serverPlayer.getState());

        switch (state) {
            case IDLE:
                spriteCtr = 0;
                break;
            case SPRINTING:
            case WALKING:
                stateTime += Gdx.graphics.getDeltaTime();
                if (stateTime > animationSpeed) {
                    spriteCtr++;
                    if (spriteCtr > 4) {
                        spriteCtr = 1;
                    }
                    stateTime = 0;
                }
                break;
        }

        if (needsSpriteUpdate) {
            setDecal();
            needsSpriteUpdate = false;
        }

        setPosition(position.x, getY(), position.z);
        Game.decalBatch.add(decal);
        DecalHelper.applyLighting(decal, Game.scene.cam);
        DecalHelper.faceCameraPerpendicularToGround(decal, Game.scene.cam);
    }

    @Override
    public void setPosition(Vector3 position) {
        super.setPosition(position);
        if (serverPlayer != null) {
            serverPlayer.setPosition(position);
        }
        needsSpriteUpdate = true;
    }

    public void setPosition(float x, float y, float z) {
        super.setPosition(x, y, z);
        if (serverPlayer != null) {
            serverPlayer.setPosition(position);
        }
        needsSpriteUpdate = true;
    }

    @Override
    public void setDirection(Direction direction) {
        super.setDirection(direction);
        if (serverPlayer != null) {
            serverPlayer.setDirection(direction);
        }
        needsSpriteUpdate = true;
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        if (serverPlayer != null) {
            serverPlayer.setState(state);
        }
        needsSpriteUpdate = true;
    }
}