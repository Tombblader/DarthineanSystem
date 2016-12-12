/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.events;

import com.ark.darthsystem.Database.MapDatabase;
import com.ark.darthsystem.Graphics.Actor;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.Graphics.PlayerCamera;
import com.ark.darthsystem.States.OverheadMap;
import com.ark.darthsystem.States.State;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Teleport extends Event {

    private String map;
    private float newX;
    private float newY;
    
    public Teleport(Sprite[] img, float getX,
            float getY,
            float getDelay, String newMap, float xCoord, float yCoord) {
        super(img, getX, getY, getDelay);
        map = newMap;
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(2);
    }
    
    public Teleport(Sprite[] img, float getX,
            float getY,
            float getDelay, String newMap, int xCoord, int yCoord) {
        super(img, getX, getY, getDelay);
        setTriggerMethod(TriggerMethod.TOUCH);
        map = newMap;
        newX = xCoord * 32 / PlayerCamera.PIXELS_TO_METERS + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getPlayerCamera().getScreenPositionX();
        newY = yCoord * 32 / PlayerCamera.PIXELS_TO_METERS + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getPlayerCamera().getScreenPositionY();
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(2);
    }
    
    
    @Override
    public void run(Actor a) {
        for (State s : GraphicsDriver.getState()) {
            if (s instanceof OverheadMap) {
                GraphicsDriver.transition();
                GraphicsDriver.getState().set(GraphicsDriver.getState().indexOf(s, true), MapDatabase.getMaps().get(map));
                GraphicsDriver.playMusic(MapDatabase.getMaps().get(map).getMusic());
                GraphicsDriver.getPlayer().setMap(MapDatabase.getMaps().get(map), newX, newY);
                ((OverheadMap) (GraphicsDriver.getCurrentState())).updatePartial(0);
            }
        }
    }
    
}
