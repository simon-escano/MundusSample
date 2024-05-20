package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.utils.FirstPersonCameraController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.terrain.Terrain;
import com.mbrlabs.mundus.runtime.Mundus;

import java.awt.event.KeyEvent;

public class MyGdxGame extends ApplicationAdapter {
	private Mundus mundus;
	private Scene scene;
	private FirstPersonCameraController controller;
	public static Terrain terrain;
	private Array<Decal> mapDecals = new Array<>();
	private DecalBatch decalBatch;
	private Monster monster;
	private boolean showMapDecals = true;
	private int decalCtr = 0;

	@Override
	public void create () {
		mundus = new Mundus(Gdx.files.internal("mundus"));
		scene = mundus.loadScene("Main Scene.mundus");

		scene.cam.position.set(230, 150, 190);

		controller = new FirstPersonCameraController(scene.cam);
		controller.setVelocity(100f);
		Gdx.input.setInputProcessor(controller);
		Map myMap = new Map();
		terrain = mundus.getAssetManager().getTerrainAssets().get(0).getTerrain();
		monster = new Monster("malt-1.png", 0.5f, 9,30);
		mapDecals = myMap.loadMap(terrain);

		decalBatch = new DecalBatch(new CameraGroupStrategy(scene.cam));
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (Gdx.input.isKeyPressed(Input.Keys.F1)) {
			showMapDecals = !showMapDecals;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.T)) {
			monster.setDecal("malt-" + ((decalCtr++ % 2) + 1) + ".png");
		}

		scene.cam.position.y = terrain.getHeightAtWorldCoord(scene.cam.position.x,scene.cam.position.z, new Matrix4());
		controller.update();
		scene.sceneGraph.update();
		scene.render();

		if (showMapDecals) {
			for (Decal decal : mapDecals) {
				decalBatch.add(decal);
				DecalHelper.faceCameraPerpendicularToGround(decal, scene.cam);
				DecalHelper.applyLighting(decal, scene.cam);
			}
		}

		monster.chase(scene.cam.position);
		decalBatch.add(monster.decal);
		DecalHelper.faceCameraPerpendicularToGround(monster.decal, scene.cam);
		DecalHelper.applyLighting(monster.decal, scene.cam);

		decalBatch.flush();
	}

	@Override
	public void dispose () {
		mundus.dispose();
	}
}
