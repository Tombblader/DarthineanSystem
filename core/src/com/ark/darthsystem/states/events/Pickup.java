/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Item;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Pickup extends Event {

    private Item[] item;
    private boolean isFinished;

    public Pickup(String img, float getX,
            float getY,
            float getDelay,
            Item getItem) {
        super(img, getX, getY, getDelay);
        item = new Item[1];
        item[0] = getItem;
        isFinished = false;
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(1);
    }

    public Pickup(String img, float getX,
            float getY,
            float getDelay,
            Item[] getItems) {
        super(img, getX, getY, getDelay);
        item = getItems;
        isFinished = false;
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(1);
    }
    
    
    public void run() {
        for (Item i : item) {
            BattleDriver.printline("Obtained " + i.getQuantity() + " " + i.getName() + "!");
        }
        BattleDriver.addItems(item);
        isFinished = true;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Item getItem() {
        return item[0];
    }
    
    public Item[] getAllItems() {
        return item;
    }

}
