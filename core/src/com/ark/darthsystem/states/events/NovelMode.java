/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.chapters.Novel;

/**
 *
 * @author Keven
 */
public class NovelMode extends Event {
    public NovelMode(Novel n, String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
        novel = n;
        setTriggerMethod(TriggerMethod.AUTO);
        setID(0);
    }

    public NovelMode(Novel n, String img, float getX, float getY, float delay, TriggerMethod t) {
        super(img, getX, getY, delay);
        novel = n;
        setTriggerMethod(t);
        setID(0);
    }
    
    private Novel novel;
    private boolean isFinished = false;


    @Override
    public void run() {
        if (!GraphicsDriver.getState().contains(novel, true)) {
            GraphicsDriver.addState(novel);
        }
        isFinished = novel.isFinished();
    }
    
    public boolean isFinished() {
        return isFinished;
    }    
}
