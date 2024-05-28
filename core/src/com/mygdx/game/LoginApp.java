package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class LoginApp extends ApplicationAdapter implements Screen {

    Fasaar game;
    private Stage stage;
    private Skin skin;
    private static Client client;

    public LoginApp(Fasaar game) {
        this.game = game;
    }

    public void handleClient() {
        client = new Client();
        client.start();
        Kryo kryo = client.getKryo();
        kryo.register(LoginCredentials.class);

        try {
            client.connect(5000, "localhost", 33003, 32002);
        } catch (IOException e) {
            Gdx.app.log("Login", "Unable to connect to server: " + e.getMessage());
            Gdx.app.exit();
        }

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
            if (object instanceof LoginCredentials) {
                LoginCredentials myCredentials = (LoginCredentials) object;
                if(myCredentials.getAuth()){
                    System.out.println("Success!");
                }
            }
            if (object instanceof Integer){
                if((int)object==-1){
                    System.out.println("Wrong credentials!");
                } else if((int)object>0){

                    //how to run other window :((
                }
                System.out.println("UserID: "+ object);
            }
            }
        });
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label usernameLabel = new Label("Username:", skin);
        TextField usernameField = new TextField("", skin);
        Label passwordLabel = new Label("Password:", skin);
        TextField passwordField = new TextField("", skin);
        passwordField.setPasswordCharacter('*');
        passwordField.setPasswordMode(true);
        TextButton loginButton = new TextButton("Login", skin);
        handleClient();

        loginButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                String username = usernameField.getText();
                String password = passwordField.getText();
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
                LoginCredentials myCredentials = new LoginCredentials(username,password);
                client.sendTCP(myCredentials);
                return true;
            }
        });

        table.add(usernameLabel).pad(10);
        table.add(usernameField).width(200).pad(10);
        table.row();
        table.add(passwordLabel).pad(10);
        table.add(passwordField).width(200).pad(10);
        table.row();
        table.add(loginButton).colspan(2).center().pad(10);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}

