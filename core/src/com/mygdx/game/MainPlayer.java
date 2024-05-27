package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;

import java.io.Serializable;

public class MainPlayer extends Player implements Serializable {
    private final FirstPersonCameraController controller;
    private final Camera camera;

    public MainPlayer(int id, String color, float velocity) {
        super();
        serverPlayer = new ServerPlayer(id, color);

        camera = MyGdxGame.scene.cam;
        camera.position.set(serverPlayer.getPosition());
        setPosition(camera.position);
        setPosition(serverPlayer.getPosition());
        controller = new FirstPersonCameraController(MyGdxGame.scene.cam);
        setVelocity(velocity);
        Gdx.input.setInputProcessor(controller);
    }

    public void update() {
        Vector3 pastPosition = new Vector3(camera.position);
        controller.update();
        Vector3 potentialPosition = new Vector3(camera.position);
        float newHeight = Utils.getHeight(potentialPosition.x, potentialPosition.z);

        if (newHeight - pastPosition.y > 12) {
            setPosition(pastPosition);
            camera.position.set(pastPosition);
        } else {
            if (potentialPosition.x < 0) potentialPosition.x = 0;
            if (potentialPosition.x > MyGdxGame.terrain.terrainWidth) potentialPosition.x = MyGdxGame.terrain.terrainWidth;
            if (potentialPosition.z < 0) potentialPosition.z = 0;
            if (potentialPosition.z > MyGdxGame.terrain.terrainWidth) potentialPosition.z = MyGdxGame.terrain.terrainWidth;
            potentialPosition.y = newHeight;
            setPosition(potentialPosition);
            camera.position.set(potentialPosition);
        }

        Vector3 waterPosition = new Vector3(1192.29f, 196.15f, 294.11f);
        float waterSize = 2500;
        if ((position.x > waterPosition.x && position.x < waterPosition.x + waterSize) &&
                (position.y < waterPosition.y) && (position.z > waterPosition.z && position.z < waterPosition.z + waterSize)
        ) {
            MyGdxGame.fontBatch.setColor(1, 1, 1, 0.25f);
            MyGdxGame.fontBatch.draw(new Texture("water_overlay.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                setState(State.SPRINTING);
            } else {
                setState(State.WALKING);
            }
        } else {
            setState(State.IDLE);
        }

        setDirection(Utils.vectorToDirection(camera.direction.x, camera.direction.z));

        MyGdxGame.client.sendTCP(this.serverPlayer);
    }

    @Override
    public void setVelocity(float velocity) {
        super.setVelocity(velocity);
        controller.setVelocity(velocity);
    }
}