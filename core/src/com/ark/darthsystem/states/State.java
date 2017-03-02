/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.ark.darthsystem.graphics.GraphicsDriver;

/**
 *
 * @author Keven
 */
public interface State {

//    public final int WIDTH = GraphicsDriver.getWidth();
//    public final int HEIGHT = GraphicsDriver.getHeight();
    
    public float update(float delta);

    public void render(SpriteBatch batch);

    public void dispose();
    public String getMusic();
}
