/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States;

import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Keven
 */
public class GameOver implements State {

    private Menu gameOverMenu;
    private final TextureRegion gameOverTexture;

    private String BGM = null;
    
    public GameOver() {
        gameOverTexture = new TextureRegion(new Texture(Gdx.files.internal("backgrounds/title.png"))) { {
                this.flip(false, true);
               }
            };        
        gameOverMenu = new Menu("Continue?", new String[]{"Yes", "No"}) {
            @Override
            public String confirm(String choice) {
                if (choice.equals("No")) {
                    GraphicsDriver.newGame();
                    GraphicsDriver.addState(new Title());
                }
                if (choice.equals("Yes")) {
                    GraphicsDriver.addMenu(new Menu("Open which slot?", new String[]{"Slot 1", "Slot 2", "Slot 3"},
                            true,
                            true) {
                        @Override
                        public Object confirm(String choice) {
                            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                        }
                    });
                }                
                return choice;
            }
            
        };
    }
    
    @Override
    public float update(float delta) {
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
        GraphicsDriver.addMenu(gameOverMenu);
        return delta;
    }
    
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(gameOverTexture, 0, 0);
    }
    
    @Override
    public void dispose() {
        gameOverTexture.getTexture().dispose();        
    }

    @Override
    public String getMusic() {
        return BGM;
    }
    
}
