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
import com.badlogic.gdx.maps.MapProperties;
import java.util.Objects;

/**
 *
 * @author Keven
 */
public class Teleport extends Event {

    private String map;
    private float newX;
    private float newY;

    public Teleport() {
        super("", 0, 0, 6/60f);
    }
    
    
    
    public Teleport(String img, float getX,
            float getY,
            float getDelay, String newMap, float xCoord, float yCoord) {
        super(img, getX, getY, getDelay);
        map = newMap;
        newX = xCoord + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionX();
        newY = yCoord + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionY();
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(2);
    }
    
    public Teleport(String img, float getX,
            float getY,
            float getDelay, String newMap, int xCoord, int yCoord) {
        super(img, getX, getY, getDelay);
        setTriggerMethod(TriggerMethod.TOUCH);
        map = newMap;
        newX = xCoord + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionX();
        newY = yCoord + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionY();
        setID(2);
    }
    
    @Override
    public Event createFromMap(MapProperties prop) {
        String[] parameters = prop.get("parameters", String.class).split(",* ");
        String image = prop.get("image", String.class);
        this.setCurrentImage(GraphicsDriver.getMasterSheet().createSprite(image));
        setX(prop.get("x", Float.class));
        setY(prop.get("y", Float.class));
        map = parameters[0];
        newX = Integer.parseInt(parameters[1]);
        newY = Integer.parseInt(parameters[2]);
        Teleport t = new Teleport(image, prop.get("x", Float.class), prop.get("y", Float.class), 6 / 60f, map, newX, newY);
        t.setTriggerMethod(TriggerMethod.valueOf(prop.get("trigger", String.class).toUpperCase()));
        return t;
//        return this;
//                image = GraphicsDriver.getMasterSheet().createSprites(prop.get("image", String.class)).toArray(Sprite.class);
//                e = new Teleport(image.length > 0 ? image : null,
//         new Teleport(image,
//                        prop.get("x", Float.class),
//                        prop.get("y", Float.class),
//                        6 / 60f,
//                        parameters[0],
//                        Integer.parseInt(parameters[1]),
//                        Integer.parseInt(parameters[2]));        
    }
    
    @Override
    public void run() {
        for (State s : GraphicsDriver.getState()) {
            if (s instanceof OverheadMap) {
                ((OverheadMap) s).clearTemporaryActors();
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

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.map);
        hash = 73 * hash + Float.floatToIntBits(this.newX);
        hash = 73 * hash + Float.floatToIntBits(this.newY);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Teleport other = (Teleport) obj;
        if (Float.floatToIntBits(this.newX) != Float.floatToIntBits(other.newX)) {
            return false;
        }
        if (Float.floatToIntBits(this.newY) != Float.floatToIntBits(other.newY)) {
            return false;
        }
        if (!Objects.equals(this.map, other.map)) {
            return false;
        }
        return super.equals(obj);
    }

    
}
