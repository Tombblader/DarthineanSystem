/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.events;

import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.States.chapters.Novel;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class NovelMode extends Event {
    public NovelMode(Novel n, Sprite[] img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
        novel = n;
        setTriggerMethod(TriggerMethod.AUTO);
        setID(0);
    }

    public NovelMode(Novel n, Sprite[] img, float getX, float getY, float delay, TriggerMethod t) {
        super(img, getX, getY, delay);
        novel = n;
        setTriggerMethod(t);
        setID(0);
    }
    
    private Novel novel;
    private boolean isFinished = false;


    @Override
    public void run() {
        GraphicsDriver.addState(novel);
        isFinished = true;
    }
    
    public boolean isFinished() {
        return isFinished;
    }    
}
