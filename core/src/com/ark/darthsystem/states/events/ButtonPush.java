/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author Keven
 */
public class ButtonPush extends Event {
    private Event event;
    public ButtonPush(String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
    }

    @Override
    public void run() {
        event.run();
    }
    

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Event createFromMap(MapProperties prop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
