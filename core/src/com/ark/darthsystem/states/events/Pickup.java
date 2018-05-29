/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.database.Database1;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author Keven
 */
public class Pickup extends Event {

    private Item[] item;
    private int money;
    private boolean isFinished;

    public Pickup(String img, float getX,
            float getY,
            float getDelay,
            Item getItem) {
        super(img, getX, getY, getDelay);
        item = new Item[1];
        item[0] = getItem;
        money = 0;
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
        money = 0;
        isFinished = false;
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(1);
    }

    public Pickup(String img, float getX,
            float getY,
            float getDelay,
            Item[] getItems,
            int getMoney) {
        super(img, getX, getY, getDelay);
        item = getItems;
        money = getMoney;
        isFinished = false;
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(1);
    }
    
    
    public void run() {
        for (Item i : item) {
            BattleDriver.printline("Obtained " + i.getCharges() + " " + i.getName() + "!");
        }
        BattleDriver.addItems(item);
        if (money != 0) {
            BattleDriver.printline("Got " + money + " GP!");            
            Database1.money += money;
        }
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

    @Override
    public Event createFromMap(MapProperties prop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
