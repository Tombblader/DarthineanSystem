/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author keven
 */
public class SpawnEnemy extends Event {    
    
    public SpawnEnemy(String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
    }

    @Override
    public Event createFromMap(MapProperties prop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
