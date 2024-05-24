package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.terrain.Terrain;
import com.mbrlabs.mundus.runtime.Mundus;

public class MyGdxGame extends ApplicationAdapter {
	private Mundus mundus;
	public static Scene scene;
	public static Terrain terrain;
	private Array<Decal> mapDecals = new Array<>();
	public static DecalBatch decalBatch;
	private Monster monster;
	private boolean showMapDecals = true;
	public static MainPlayer mainPlayer;
	SpriteBatch fontBatch;
	BitmapFont font;
	OrthographicCamera fontCam;

	@Override
	public void create () {
		mundus = new Mundus(Gdx.files.internal("mundus"));
		scene = mundus.loadScene("Main Scene.mundus");

		scene.cam.position.set(230, 150, 190);
		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		fontCam = new OrthographicCamera();
		fontCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fontBatch = new SpriteBatch();
		fontBatch.setProjectionMatrix(fontCam.combined);
		mainPlayer = new MainPlayer(100f);
		Map myMap = new Map();
		terrain = mundus.getAssetManager().getTerrainAssets().get(0).getTerrain();
		monster = new Monster("astronaut_spritesheet.png", 40, 40, 0.5f);
		mapDecals = myMap.loadMap(terrain);

		decalBatch = new DecalBatch(new CameraGroupStrategy(scene.cam));
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			showMapDecals = !showMapDecals;
		}

		mainPlayer.update();
		monster.update();
		scene.sceneGraph.update();
		scene.render();


		if (showMapDecals) {
			for (Decal decal : mapDecals) {
				decalBatch.add(decal);
				DecalHelper.faceCameraPerpendicularToGround(decal, scene.cam);
				DecalHelper.applyLighting(decal, scene.cam);
			}
		}

		decalBatch.flush();

		fontBatch.begin();
		font.getData().setScale(0.3f, 0.3f);
		font.setColor(Color.WHITE);
		String[][] strings = {
			{
				"x: " + String.format("%.2f", mainPlayer.getPosition().x),
				"y: " + String.format("%.2f", mainPlayer.getPosition().y),
				"z: " + String.format("%.2f", mainPlayer.getPosition().z),
				mainPlayer.getRelativeDirection(monster.getPosition()).toString()
			},
			{
				"Monster x: " + String.format("%.2f", monster.getDecal().getPosition().x),
				"Monster y: " + String.format("%.2f", monster.getPosition().y),
				"Monster z: " + String.format("%.2f", monster.getPosition().z)
			}
		};

		for (int i = 0; i < strings.length; i++) {
			for (int j = 0; j < strings[i].length; j++) {
				font.draw(fontBatch, strings[i][j], (i * 150) + 10, Gdx.graphics.getHeight() - ((j * 30) + 10));
			}
		}
		fontBatch.end();
	}

	@Override
	public void dispose () {
		mundus.dispose();
		font.dispose();
	}
}
