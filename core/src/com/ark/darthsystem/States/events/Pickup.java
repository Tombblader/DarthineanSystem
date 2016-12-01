/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.events;

import com.ark.darthsystem.Item;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Pickup extends Event {

    private Item item;
    private boolean isFinished;

    public Pickup(Sprite[] img, float getX,
            float getY,
            float getDelay,
            Item getItem) {
        super(img, getX, getY, getDelay);
        item = getItem;
        isFinished = false;
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(1);
    }

    public void run() {

        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Item getItem() {
        return item;
    }

}
