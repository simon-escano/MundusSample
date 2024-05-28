package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.audio.Mp3;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Vector3;

import javax.lang.model.element.VariableElement;
import java.io.Serializable;
import java.util.Objects;

public class MainPlayer extends Player implements Serializable {
    private final FirstPersonCameraController controller;
    private final Camera camera;
    private boolean isFlying = false;

    public MainPlayer(int id, String color, float velocity) {
        super();
        System.out.println(FRAME_ROWS);
        serverPlayer = new ServerPlayer(id, color);
        camera = Game.scene.cam;
        camera.position.set(serverPlayer.getPosition());
        setPosition(camera.position);
        setPosition(serverPlayer.getPosition());
        controller = new FirstPersonCameraController(Game.scene.cam);
        setVelocity(velocity);
        Gdx.input.setInputProcessor(controller);
    }

    public void update() {
        Vector3 pastPosition = new Vector3(camera.position);
        controller.update();
        Vector3 potentialPosition = new Vector3(camera.position);
        float newHeight = Utils.getHeight(potentialPosition.x, potentialPosition.z);

        if (Objects.requireNonNull(state) == State.WALKING) {
            setVelocity(100f);
            stateTime += Gdx.graphics.getDeltaTime();
            if (stateTime > 0.5) {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/walk.mp3"));
                sound.play(0.5f);
                stateTime = 0;
            }
        } else if (Objects.requireNonNull(state) == State.SPRINTING) {
            setVelocity(150f);
            stateTime += Gdx.graphics.getDeltaTime();
            if (stateTime > 0.3) {
                Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/walk.mp3"));
                sound.play(0.5f);
                stateTime = 0;
            }
        }

        if (newHeight - pastPosition.y > 12) {
            setPosition(pastPosition);
            camera.position.set(pastPosition);
        } else {
            potentialPosition.y = newHeight;
            setPosition(potentialPosition);
            camera.position.set(potentialPosition);
        }

        Vector3 waterPosition = new Vector3(1192.29f, 196.15f, 294.11f);
        float waterSize = 2500;
        if ((position.x > waterPosition.x && position.x < waterPosition.x + waterSize) &&
                (position.y < waterPosition.y) && (position.z > waterPosition.z && position.z < waterPosition.z + waterSize)
        ) {
            Game.fontBatch.setColor(1, 1, 1, 0.25f);
            Game.fontBatch.draw(new Texture("water_overlay.png"), 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

        Game.client.sendTCP(this.serverPlayer);
    }

    public void die(Leviathan leviathan) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sounds/death.mp3"));
        sound.play(0.5f);

        if (leviathan instanceof Cthulhu) {
            setPosition(9999, Utils.getHeight(9999, 9999), 9999);
        } else {
            setPosition(260, Utils.getHeight(260, 230), 230);
        }

        setPosition(260, Utils.getHeight(260, 230), 230);
        camera.position.set(position);
        Game.client.sendTCP(this.serverPlayer);
    }

    @Override
    public void setVelocity(float velocity) {
        super.setVelocity(velocity);
        controller.setVelocity(velocity);
    }
}