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
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mbrlabs.mundus.commons.Scene;
import com.mbrlabs.mundus.commons.terrain.Terrain;
import com.mbrlabs.mundus.runtime.Mundus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MyGdxGame extends ApplicationAdapter {
	private Mundus mundus;
	public static Scene scene;
	public static Terrain terrain;
	public static DecalBatch decalBatch;
	public Map map;
	private ArrayList<Player> otherPlayers;
	private ConcurrentLinkedQueue<ServerPlayer> playerUpdates;
	private boolean showMapDecals = true;
	public static MainPlayer mainPlayer;
	private SpriteBatch fontBatch;
	private BitmapFont font;

	public static Client client;

	@Override
	public void create () {
		otherPlayers = new ArrayList<>();
		playerUpdates = new ConcurrentLinkedQueue<>();
		mundus = new Mundus(Gdx.files.internal("mundus"));
		scene = mundus.loadScene("Main Scene.mundus");

		handleClient();

		terrain = mundus.getAssetManager().getTerrainAssets().get(0).getTerrain();
		map = new Map();

		mainPlayer = new MainPlayer(4, "_sus",100f);

		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		OrthographicCamera fontCam = new OrthographicCamera();
		fontCam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fontBatch = new SpriteBatch();
		fontBatch.setProjectionMatrix(fontCam.combined);

		decalBatch = new DecalBatch(new CameraGroupStrategy(scene.cam));
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			showMapDecals = !showMapDecals;
		}

		mainPlayer.update();

		while (!playerUpdates.isEmpty()) {
			ServerPlayer serverPlayer = playerUpdates.poll();
			updatePlayers(serverPlayer);
		}

		for (Player player : otherPlayers) {
			if (player.serverPlayer.getID() != mainPlayer.serverPlayer.getID()) {
				player.update();
			}
		}
		scene.sceneGraph.update();
		scene.render();

		if (showMapDecals) {
			map.update();
		}

		decalBatch.flush();

		fontBatch.begin();
		font.getData().setScale(0.3f, 0.3f);
		font.setColor(Color.WHITE);
		String[][] strings = {
			{
				mainPlayer.serverPlayer.toString()
			}
		};

		for (int i = 0; i < strings.length; i++) {
			for (int j = 0; j < strings[i].length; j++) {
				font.draw(fontBatch, strings[i][j], (i * 150) + 10, Gdx.graphics.getHeight() - ((j * 30) + 10));
			}
		}
		fontBatch.end();
	}

	public void updatePlayers(ServerPlayer serverPlayer) {
		boolean updated = false;
		for (Player player : otherPlayers) {
			if (player.serverPlayer.getID() == serverPlayer.getID()) {
				player.serverPlayer.setPosition(serverPlayer.getPosition());
				player.serverPlayer.setDirection(serverPlayer.getDirection());
				player.serverPlayer.setState(serverPlayer.getState());
				updated = true;
				break;
			}
		}
		if (!updated) {
			otherPlayers.add(new Player(serverPlayer));
		}
	}

	public void handleClient() {
		client = new Client();
		client.start();
		Kryo kryo = client.getKryo();
		kryo.register(ServerPlayer.class);
		kryo.register(Entity.Direction.class);
		kryo.register(Entity.State.class);
		kryo.register(Vector3.class);
		kryo.register(String.class);

		try {
			client.connect(5000, "localhost", 54555, 54777);
		} catch (IOException e) {
			Gdx.app.log("GameClient", "Unable to connect to server: " + e.getMessage());
			Gdx.app.exit();
		}

		client.addListener(new Listener() {
			@Override
			public void received(Connection connection, Object object) {
				if (object instanceof ServerPlayer) {
					ServerPlayer player = (ServerPlayer) object;
					playerUpdates.add(player);
				}
			}
		});
	}

	@Override
	public void dispose () {
		mundus.dispose();
		font.dispose();
	}
}