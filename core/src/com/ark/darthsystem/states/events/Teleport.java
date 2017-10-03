/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.database.MapDatabase;
import com.ark.darthsystem.graphics.ActorSprite;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.ark.darthsystem.states.OverheadMap;
import com.ark.darthsystem.states.State;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Teleport extends Event {

    private String map;
    private float newX;
    private float newY;
    
    public Teleport(String img, float getX,
            float getY,
            float getDelay, String newMap, float xCoord, float yCoord) {
        super(img, getX, getY, getDelay);
        map = newMap;
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(2);
    }
    
    public Teleport(String img, float getX,
            float getY,
            float getDelay, String newMap, int xCoord, int yCoord) {
        super(img, getX, getY, getDelay);
        setTriggerMethod(TriggerMethod.TOUCH);
        map = newMap;
        newX = xCoord * 32 / PlayerCamera.PIXELS_TO_METERS + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionX();
        newY = yCoord * 32 / PlayerCamera.PIXELS_TO_METERS + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionY();
        setID(2);
    }
    
    
    @Override
    public void run() {
        for (State s : GraphicsDriver.getState()) {
            if (s instanceof OverheadMap) {
                GraphicsDriver.getPlayer().setFieldState(ActorSprite.SpriteModeField.STAND);
                GraphicsDriver.getPlayer().setMap(MapDatabase.getMaps().get(map), newX, newY);
                GraphicsDriver.transition();
                GraphicsDriver.getState().set(GraphicsDriver.getState().indexOf(s, true), MapDatabase.getMaps().get(map));
                GraphicsDriver.playMusic(MapDatabase.getMaps().get(map).getMusic());
                ((OverheadMap) (MapDatabase.getMaps().get(map))).updatePartial(0f);
            }
        }
    }
    
    public boolean isFinished() {
        return false;
    }
}
