/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Keven
 */
public interface State {
    
    public float update(float delta);

    public void render(SpriteBatch batch);

    public void dispose();
    public String getMusic();
    
}
