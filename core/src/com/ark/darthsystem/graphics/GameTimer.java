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
    private int currentTime;
    private static final long serialVersionUID = 517358734L;

    public GameTimer() {
        this.name = "";
    }

    public GameTimer(String name, float time) {
        this.currentTime = 0;
        this.time = time;
        this.name = name;
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
        currentTime += delta;
        if (isFinished()) {
            currentTime = 0;
            event(null);
            return true;
        }
        return false;
    }

    public boolean isFinished() {
        return currentTime >= time;
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
