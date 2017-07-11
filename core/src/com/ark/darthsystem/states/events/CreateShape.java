/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import static com.ark.darthsystem.states.events.Event.setID;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author keven
 */
public class CreateShape extends Event {
    
    public CreateShape(Sprite[] img, Sprite[] openImage, float getX, float getY, float getDelay, String newMap, int xCoord, int yCoord) {
        super(img, getX, getY, getDelay);
        setID(4);
    }
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
