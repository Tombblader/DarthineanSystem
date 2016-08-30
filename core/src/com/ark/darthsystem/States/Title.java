/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.Database.MapDatabase;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author Keven
 */
public class Title implements State {
    private final Menu TITLE_MENU;
    private final TextureRegion titleTexture;
    private final String BGM = null;
    
    public Title() {
        titleTexture = new TextureRegion(new Texture(Gdx.files.internal("backgrounds/title.png"))) {
            {
                this.flip(false, true);
            }
        };
        TITLE_MENU = new Menu("Adventurer.", new String[]{"Start", "Continue", "Quit"}) {
            @Override
            public String confirm(String choice) {
                if (choice.equals("Start")) {
                    GraphicsDriver.newGame();
                    GraphicsDriver.addState((State) (MapDatabase.getMaps().get(MapDatabase.DEFAULT_MAP)));
                    GraphicsDriver.transition();
                    ((OverheadMap) (GraphicsDriver.getCurrentState())).updatePartial(0);
                }
                if (choice.equals("Continue")) {
                    GraphicsDriver.addMenu(new Menu("Open which slot?",
                            new String[]{"Slot 1", "Slot 2", "Slot 3"},
                            true,
                            true) {
                        @Override
                        public Object confirm(String choice) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    });
                }
                if (choice.equals("Quit")) {
                    GraphicsDriver.getState().get(0).dispose();
                    throw new GameOverException();
                }
                
                return choice;
            }
            
        };
    }
    
    @Override
    public float update(float delta) {
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
        GraphicsDriver.addMenu(TITLE_MENU);
        return delta;
    }
    
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(titleTexture, 0, 0);
    }
    
    @Override
    public void dispose() {
        titleTexture.getTexture().dispose();
    }

    @Override
    public String getMusic() {
        return BGM;
    }
    
}
