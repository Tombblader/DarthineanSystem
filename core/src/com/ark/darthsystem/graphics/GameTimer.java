package com.ark.darthsystem.graphics;

import java.io.Serializable;

/**
 *
 * @author Keven
 */
public abstract class GameTimer implements Serializable {

    private boolean isOn;
    private float time;
    private final String name;
    private Actor caller;
    private int currentTime;
    private static final long serialVersionUID = 517358734L;

    public GameTimer() {
        this.name = "";
    }
    
    public GameTimer(String name, float time) {
        this.currentTime = 0;
        this.time = time;
        this.name = name;
        this.caller = null;
    }

    public GameTimer(String name, float time, Actor caller) {
        this.currentTime = 0;
        this.time = time;
        this.name = name;
        this.caller = caller;
    }

    public String getName() {
        return name;
    }

    public float getTime() {
        return time;
    }

    public boolean isOn() {
        return isOn;
    }

    public boolean update(float delta, Actor a) {
        currentTime += delta;
        if (isFinished()) {
            currentTime = 0;
            event(a);
            return true;
        }
        return false;
    }

    public boolean update(float delta) {
        return update(delta, caller);        
//        currentTime += delta;
//        if (isFinished()) {
//            currentTime = 0;
//            event(caller);
//            return true;
//        }
//        return false;
    }

    public boolean isFinished() {
        return currentTime >= time;
    }
    
    public void setCaller(Actor caller) {
        this.caller = caller;
    }

    public float getCurrentTime() {
        return currentTime;
    }

    public void resetTimer() {
        currentTime = 0;
    }

    public abstract void event(Actor a);

    public void clear() {

    }
}
