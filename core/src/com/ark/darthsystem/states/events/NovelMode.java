/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.database.EventDatabase;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.ark.darthsystem.states.chapters.Novel;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author Keven
 */
public class NovelMode extends Event {

    public NovelMode(Novel n, String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
        novel = n;
        setTriggerMethod(TriggerMethod.AUTO);
    }

    public NovelMode() {
        super("", 0, 0, 6 / 60f);
    }

    public NovelMode(Novel n, String img, float getX, float getY, float delay, TriggerMethod t) {
        super(img, getX, getY, delay);
        novel = n;
        setTriggerMethod(t);
    }

    private Novel novel;

    @Override
    public void run() {
        if (!isFinished()) {
            GraphicsDriver.addState(novel);
            switches.setSwitch(LocalSwitch.Switch.FINISHED, novel.isFinished());
        }
    }

    @Override
    public Event createFromMap(MapProperties prop) {
        String[] parameters = prop.get("parameters", String.class).split(",* ");
        String image = prop.get("image", String.class);
        return new NovelMode(EventDatabase.chapters(parameters),
                image,
                (prop.get("x", Float.class) + prop.get("width", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                (prop.get("y", Float.class) + prop.get("height", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                6 / 60f);
    }
}
