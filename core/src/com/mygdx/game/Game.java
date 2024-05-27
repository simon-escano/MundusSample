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

public class Game extends ApplicationAdapter {
	int id;
	String color;
	private Mundus mundus;
	public static Scene scene;
	public static Terrain terrain;
	public static DecalBatch decalBatch;
	public MapDecals mapDecals;
	public static ArrayList<Player> players;
	private ConcurrentLinkedQueue<ServerPlayer> playerUpdates;
	public static ArrayList<GameState> gameStates;
	public static int gameStateIndex;
	private boolean showMapDecals = true;
	public static MainPlayer mainPlayer;
	public static SpriteBatch effectOverlays;
	public static SpriteBatch fontBatch;
	public static BitmapFont font;
	public static Client client;

	public Game(int id, String color) {
		this.id = id;
		this.color = color;
	}

	@Override
	public void create () {
		players = new ArrayList<>();
		playerUpdates = new ConcurrentLinkedQueue<>();
		mundus = new Mundus(Gdx.files.internal("world"));
		scene = mundus.loadScene("Main Scene.mundus");
		terrain = mundus.getAssetManager().getTerrainAssets().get(0).getTerrain();
		mapDecals = new MapDecals();

		gameStates = new ArrayList<>();
		gameStates.add(new GameState("Prologue"));
		gameStates.add(new GameState("Chapter 1: Stranded", 670, 2000, new Python()));
		gameStates.add(new GameState("Chapter 2: The Forest of Shadows", 1800, 2500, new ForestLurker()));
		gameStates.add(new GameState("Chapter 2: The Ruins of The Forgotten", 1700, 1000));
		gameStates.add(new GameState("Chapter 3: The Awakening", new Cthulhu()));
		gameStateIndex = 1;
		gameStates.get(gameStateIndex).start();

		mainPlayer = new MainPlayer(id, color,150f);

		font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"));
		OrthographicCamera orthographicCamera = new OrthographicCamera();
		orthographicCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		fontBatch = new SpriteBatch();
		effectOverlays = new SpriteBatch();
		fontBatch.setProjectionMatrix(orthographicCamera.combined);
		effectOverlays.setProjectionMatrix(orthographicCamera.combined);
		decalBatch = new DecalBatch(new CameraGroupStrategy(scene.cam));

		handleClient();
	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		gameStates.get(gameStateIndex).update();

		if (Gdx.input.isKeyJustPressed(Input.Keys.F1)) {
			showMapDecals = !showMapDecals;
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
			if (gameStates.get(gameStateIndex).orb != null && mainPlayer.position.dst(gameStates.get(gameStateIndex).orb.position) < 50) {
				gameStates.get(gameStateIndex).orb.pickup();
			}
		}

		while (!playerUpdates.isEmpty()) {
			ServerPlayer serverPlayer = playerUpdates.poll();
			updatePlayers(serverPlayer);
		}

		for (Player player : players) {
			if (player.serverPlayer.getID() != mainPlayer.serverPlayer.getID()) {
				player.update();
			}
		}

		scene.sceneGraph.update();
		scene.render();

		if (showMapDecals) {
			mapDecals.update();
		}

		decalBatch.flush();

		fontBatch.begin();
		font.getData().setScale(0.3f, 0.3f);
		font.setColor(Color.WHITE);
		String[][] strings = {
			{
				mainPlayer.serverPlayer.toString(),
			}
		};

		mainPlayer.update();

		for (int i = 0; i < strings.length; i++) {
			for (int j = 0; j < strings[i].length; j++) {
				font.draw(fontBatch, strings[i][j], (i * 150) + 10, Gdx.graphics.getHeight() - ((j * 30) + 10));
			}
		}
		fontBatch.end();
	}

	public void updatePlayers(ServerPlayer serverPlayer) {
		boolean updated = false;
		for (Player player : players) {
			if (player.serverPlayer.getID() == serverPlayer.getID()) {
				player.serverPlayer.setPosition(serverPlayer.getPosition());
				player.serverPlayer.setDirection(serverPlayer.getDirection());
				player.serverPlayer.setState(serverPlayer.getState());
				updated = true;
				break;
			}
		}
		if (!updated) {
			players.add(new Player(serverPlayer));
		}
	}

	public void handleClient() {
		client = new Client();
		client.start();
		Kryo kryo = client.getKryo();
//		kryo.register(ServerLeviathan.class);
		kryo.register(ServerPlayer.class);
		kryo.register(Entity.Direction.class);
		kryo.register(Entity.State.class);
		kryo.register(Vector3.class);
		kryo.register(String.class);

		try {
			client.connect(5000, "localhost", 54555, 54777);
//			client.connect(5000, "54.253.165.207", 54555, 54777);
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
					System.out.println("Received from server.");
				}

				if (object instanceof Integer) {
					gameStateIndex = (Integer) object;
					System.out.println(gameStateIndex);
					gameStates.get(gameStateIndex).start();
				}
			}
		});
	}

	@Override
	public void dispose () {
		mundus.dispose();
		font.dispose();
	}

	@Override
	public void resize(int width, int height) {
		scene.cam.viewportWidth = width;
		scene.cam.viewportHeight = height;
		scene.cam.update();

		fontBatch.getProjectionMatrix().setToOrtho2D(0, 0, width, height);
	}
}