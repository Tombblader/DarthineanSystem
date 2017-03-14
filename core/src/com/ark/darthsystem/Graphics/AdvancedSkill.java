/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Keven
 */
public class AdvancedSkill extends ActorSkill {
    
    public Array<GameTimer> loadedTimers = new Array<>();
    
    public AdvancedSkill(Sprite[] img, float getX, float getY, float translateX, float translateY, float delay) {
        super(img, getX, getY, translateX, translateY, delay);
    }
    
    public void addTimer(GameTimer t) {
        loadedTimers.add(t);
    }
    
    public Array<GameTimer> getLoadedTimers() {
        return loadedTimers;
    }
    
}
