/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.graphics.ActorCollision;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author Keven
 */
public abstract class Event extends ActorCollision {
    private static int ID;
    public enum LocalSwitch {

        A,
        B,
        C,
        D
    }
    private LocalSwitch localSwitch = LocalSwitch.A;
    private TriggerMethod trigger = null;
    private int eventID;
    
    public Event(String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
    }

    public abstract Event createFromMap(MapProperties prop);

    public enum TriggerMethod {
        TOUCH,
        PRESS,
        AUTO,
        PARALLEL,
        ADJACENT;
    }

    public boolean isTriggered(TriggerMethod trigger) {
        return trigger == getTriggerMethod();
    }
    
    public abstract void run();
    
    public abstract boolean isFinished();
    
    public final LocalSwitch getLocalSwitch() {
        return localSwitch;
    }

    public final TriggerMethod getTriggerMethod() {
        return trigger;
    }

    public final void setLocalSwitch(LocalSwitch localSwitch) {
        this.localSwitch = localSwitch;
    }

    public final void setTriggerMethod(TriggerMethod trigger) {
        this.trigger = trigger;
    }    

    @Override
    public void generateBody(OverheadMap map) {
        super.generateBody(map);
        setMainFilter(ActorCollision.CATEGORY_EVENT, (short) (0));
        setSensorFilter(ActorCollision.CATEGORY_EVENT, CATEGORY_PLAYER);
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        if (getTriggerMethod() == TriggerMethod.AUTO) {
            run();
        }
    }
    
    public static int getID() {
        return ID;
    }
        
    public static void setID(int newID) {
        ID = newID;
    }
    
}
