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
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author keven
 */
public class TextBox extends Actor {
  
    private NinePatch patch;
    private String message;
    private Array<Actor> subActors;
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
        message = "";
        this.width = width;
        this.height = height;
    }

    public TextBox(String img, String message, float getX, float getY, float width, float height) {
        this(img, getX, getY, width, height);
        this.message = message;

    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public void addActor(Actor a) {
        subActors.add(a);
    }
    
    @Override
    public void render(Batch batch) {
        patch.draw(batch, getX(), getY(), width, height);
        Rectangle scissors = new Rectangle();
        Rectangle clipBounds = new Rectangle((int) getX() + PADDING_X, (int) getY() + PADDING_Y, (int) width - PADDING_X, (int) height - PADDING_Y);
        ScissorStack.calculateScissors(GraphicsDriver.getCamera(), batch.getTransformMatrix(), clipBounds, scissors);
        ScissorStack.pushScissors(scissors);
        GraphicsDriver.drawMessage(batch, message, getX() + PADDING_X, getY() + PADDING_Y);
        for (Actor a : subActors) {
            a.render(batch);
        }        
        batch.flush();
        ScissorStack.popScissors();
    }
    
    public void render(Batch batch, String message) {
        InterfaceDatabase.TEXT_BOX.draw(batch, getX(), getY(), width, height);
        GraphicsDriver.drawMessage(batch, message, getX() + PADDING_X, getY() + PADDING_Y);
    }
    
    public void render(Batch batch, float x, float y, float width, float height) {
        patch.draw(batch, x, y, width, height);
    }

    public void render(Batch batch, String message, float x, float y, float width, float height) {
        patch.draw(batch, x, y, width, height);
        GraphicsDriver.getFont().draw(batch, message, x + PADDING_X, y + PADDING_Y);
    }
    
}
