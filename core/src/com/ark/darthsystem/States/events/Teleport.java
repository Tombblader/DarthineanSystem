/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.events;

import com.ark.darthsystem.Graphics.PlayerCamera;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Teleport extends Event {

    private OverheadMap map;
    private float newX;
    private float newY;
    
    public Teleport(Sprite[] img, float getX,
            float getY,
            float getDelay, OverheadMap newMap, float xCoord, float yCoord) {
        super(img, getX, getY, getDelay);
        setTriggerMethod(TriggerMethod.TOUCH);
    }
    
    public Teleport(Sprite[] img, float getX,
            float getY,
            float getDelay, OverheadMap newMap, int xCoord, int yCoord) {
        super(img, getX, getY, getDelay);
        setTriggerMethod(TriggerMethod.TOUCH);
        map = newMap;
        newX = xCoord * 32 / PlayerCamera.PIXELS_TO_METERS - 16 / PlayerCamera.PIXELS_TO_METERS;
        newY = yCoord * 32 / PlayerCamera.PIXELS_TO_METERS - 16 / PlayerCamera.PIXELS_TO_METERS;
    }
    
    
    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
