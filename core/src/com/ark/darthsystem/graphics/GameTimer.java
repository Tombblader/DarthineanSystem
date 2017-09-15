/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

/**
 *
 * @author Keven
 */
public abstract class GameTimer {

    private boolean isOn;
    private float time;
    private final String name;
    private int currentTime = 0;

    public GameTimer(String name, float time) {
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

    public abstract void event(Actor a);

    public void clear() {
        
    }
}
