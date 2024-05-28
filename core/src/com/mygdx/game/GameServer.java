package com.mygdx.game;

import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameServer {
    Server server;
    Kryo kryo;

    public final Map<Integer, ServerPlayer> players = new HashMap<>();
    private int gameStateIndex;

    public GameServer() throws IOException {
        server = new Server();
        server.start();
        server.bind(54555, 54777);

        kryo = server.getKryo();
        kryo.register(ServerPlayer.class);
        kryo.register(Entity.Direction.class);
        kryo.register(Entity.State.class);
        kryo.register(Vector3.class);
        kryo.register(String.class);

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {

                if (object instanceof ServerPlayer) {
                    ServerPlayer player = (ServerPlayer) object;
                    players.put(player.getID(), player);
                    System.out.println("hey");

                    for (ServerPlayer pos : players.values()) {
                        server.sendToAllExceptTCP(connection.getID(),pos);
                    }
                }

                if (object instanceof Integer) {
                    gameStateIndex = (Integer) object;
                    server.sendToAllTCP(gameStateIndex);
                }
            }
            @Override
            public void disconnected(Connection connection) {
                players.remove(connection.getID());
            }
        });
    }

    public static void main(String[] args) throws IOException {
        new LoginServer();
        new GameServer();

        Scanner sc = new Scanner(System.in);
        String string = sc.nextLine();
        if (string.equals("stop")) {
            System.out.println("Stopping server...");
            System.exit(0);
        }

    }
}