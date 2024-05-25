package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;

public class MainPlayer extends Player {
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
        position.y = MyGdxGame.terrain.getHeightAtWorldCoord(position.x, position.z, new Matrix4());

        if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                setState(State.SPRINTING);
            } else {
                setState(State.WALKING);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.D)) {
                setDirection(Entity.Direction.NE);
            } else if (Gdx.input.isKeyPressed(Input.Keys.W) && Gdx.input.isKeyPressed(Input.Keys.A)) {
                setDirection(Entity.Direction.NW);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.D)) {
                setDirection(Entity.Direction.SE);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S) && Gdx.input.isKeyPressed(Input.Keys.A)) {
                setDirection(Entity.Direction.SW);
            } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                setDirection(Entity.Direction.N);
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                setDirection(Entity.Direction.S);
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                setDirection(Entity.Direction.E);
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                setDirection(Entity.Direction.W);
            }
        } else {
            setState(State.IDLE);
        }

        setPosition(position);
        setState(state);
        setDirection(Utils.vectorToDirection(camera.direction.x, camera.direction.z));
        controller.update();

        MyGdxGame.client.sendTCP(this.serverPlayer);
    }

    @Override
    public void setVelocity(float velocity) {
        super.setVelocity(velocity);
        controller.setVelocity(velocity);
    }
}