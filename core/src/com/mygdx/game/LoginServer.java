package com.mygdx.game;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class LoginServer {
    Server server;
    Kryo kryo;


//    private final Map<Integer, ServerPlayer> players = new HashMap<>();

    public LoginServer() throws IOException {
        server = new Server();
        server.start();
        server.bind(33003, 32002);

        kryo = server.getKryo();
        kryo.register(LoginCredentials.class);
        Thread accountsThread = new Thread(new Account());
        accountsThread.start(); //here start the listener for findUser

        server.addListener(new Listener(){

            @Override
            public void received(Connection connection, Object object) {

                if (object instanceof LoginCredentials) {
                    LoginCredentials myCredentials = (LoginCredentials) object;
                    String username = myCredentials.getUsername();
                    int password = myCredentials.getPassword().hashCode();
                    Account account = new Account();
                    int userID = account.findUser(username, password);
                    //sendtotcp if successful finding account
                    server.sendToTCP(connection.getID(), userID);
                }
            }
            @Override
            public void disconnected(Connection connection) {
//                players.remove(connection.getID());
            }
        });
    }

    public static void main(String[] args) throws IOException {
        new LoginServer();
    }
}
