/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 *
 * @author keven
 */
public class TextBox extends Actor {
  
    private NinePatch patch;
    private float width = 0;
    private float height = 0;
    private final int PADDING_X = 15;
    private final int PADDING_Y = 12;
    /**
     * Create an empty text box.
     * @param img
     * @param getX
     * @param getY
     * @param width
     * @param height
     */
    public TextBox(String img, float getX, float getY, float width, float height) {
        super(img, getX, getY, 12/60f);
        patch = GraphicsDriver.getMasterSheet().createPatch(img);
        this.width = width;
        this.height = height;
        
    }
    
    public void render(Batch batch) {
        patch.draw(batch, getX(), getY(), width, height);
    }
    
    public void render(Batch batch, String message) {
        patch.draw(batch, getX(), getY(), width, height);
        GraphicsDriver.getFont().draw(batch, message, getX() + PADDING_X, getY() + PADDING_Y);
    }
    
    public void render(Batch batch, float x, float y, float width, float height) {
        patch.draw(batch, x, y, width, height);
    }

    public void render(Batch batch, String message, float x, float y, float width, float height) {
        patch.draw(batch, x, y, width, height);
        GraphicsDriver.getFont().draw(batch, message, x + PADDING_X, y + PADDING_Y);
    }
    
}
