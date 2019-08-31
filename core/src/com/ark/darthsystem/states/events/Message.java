/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author Keven
 */
public class Message extends Event {
    private String message;
    public Message(String message, String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
        this.message = message;
        setTriggerMethod(Event.TriggerMethod.PRESS);
//        setID(0);
    }
    
    public Message() {
        super("", 0, 0, 6/60f);
    }

    public Message(String message, String img, float getX, float getY, float delay, Event.TriggerMethod t) {
        super(img, getX, getY, delay);
        this.message = message;
        setTriggerMethod(t);
//        setID(0);
    }
    
    private boolean isFinished = false;


    @Override
    public void run() {
        if (!isFinished) {
            BattleDriver.printline(message);
            GraphicsDriver.getPlayer().addTimer(new GameTimer("DELAY", 1000/60) {
                @Override
                public void event(Actor a) {
                    isFinished = false;
                }
            });
            isFinished = true;
        }
    }
    
    @Override
    public boolean isFinished() {
        return false;
    }    

    @Override
    public Event createFromMap(MapProperties prop) {
        String parameter = prop.get("parameters", String.class);
        String image = prop.get("image", String.class);
        return new Message(parameter,
                image,
                (prop.get("x", Float.class) + prop.get("width", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                (prop.get("y", Float.class) + prop.get("height", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                6 / 60f);
    }
}
