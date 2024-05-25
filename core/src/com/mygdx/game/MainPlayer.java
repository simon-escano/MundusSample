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
    public ServerPlayer serverPlayer;

    public MainPlayer(float velocity) {
        super();
        serverPlayer = new ServerPlayer();

        camera = MyGdxGame.scene.cam;
        setPosition(camera.position);
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
    }

    @Override
    public void setPosition(Vector3 position) {
        super.setPosition(position);
        serverPlayer.setPosition(position);
    }

    @Override
    public void setDirection(Direction direction) {
        super.setDirection(direction);
        serverPlayer.setDirection(direction);
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        serverPlayer.setState(state);
    }

    @Override
    public void setVelocity(float velocity) {
        super.setVelocity(velocity);
        controller.setVelocity(velocity);
    }
}