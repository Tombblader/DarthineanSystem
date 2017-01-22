/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States;

import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.Graphics.Player.TeamColor;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Keven
 */
public class Win implements State {

    private Menu winMenu;
    private final TextureRegion winTexture;

    private String winner = "";
    
    public Win(TeamColor color) {
        winTexture = new TextureRegion(new Texture(Gdx.files.internal("backgrounds/" + color.toString().toLowerCase() + " wins.png"))) { {
                this.flip(false, true);
               }
            };
        winMenu = new Menu(new String[]{"Continue", "Quit"}) {
            @Override
            public String confirm(String choice) {
                if (choice.equals("Continue")) {
                    GraphicsDriver.newGame();
                    GraphicsDriver.addState(new Title());
                }
                if (choice.equals("Quit")) {
                    throw new GameOverException();
                }                
                return choice;
            }
            
        };
    }
        
    @Override
    public float update(float delta) {
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
        GraphicsDriver.addMenu(winMenu);
        return delta;
    }
    
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(winTexture, 0, 0);
    }
    
    @Override
    public void dispose() {
        winTexture.getTexture().dispose();        
    }

    @Override
    public String getMusic() {
        return "";
    }

    @Override
    public void initialize() {
    }
    
}
