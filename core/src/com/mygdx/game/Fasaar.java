package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Fasaar extends Game {
    @Override
    public void create() {
        setScreen(new LoginApp(this));
    }
    public void render(){
        super.render();
    }
    public void dispose(){
        super.dispose();
    }
}
