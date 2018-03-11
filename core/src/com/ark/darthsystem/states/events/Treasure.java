/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.chapters.Novel;
import static com.ark.darthsystem.states.events.Event.setID;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 *
 * @author keven
 */
public class Treasure extends Event {

    private Item[] item;
    private boolean isFinished;
    private Novel pickedUpMessage;
    private Animation opened;

    public Treasure(String img, float getX,
            float getY,
            float getDelay,
            Item getItem) {
        super("event/chest/field/stand/down", getX, getY, getDelay);
        opened = new Animation(getDelay, GraphicsDriver.getMasterSheet().createSprites("event/chest/field/custom/down"));
        item = new Item[1];
        item[0] = getItem;
        isFinished = false;
        setTriggerMethod(TriggerMethod.PRESS);
        setID(3);
        pickedUpMessage = new Novel() { 
            {
            chapters.add((Novel.Page) () -> {
                for (Item i : item) {
                    BattleDriver.printline("Obtained " + i.getQuantity() + " " + i.getName() + "!");
                }
                BattleDriver.addItems(item);
                Treasure.this.changeAnimation(opened);
            });
        }
            @Override
            public String getMusic() {
                return null;
            }
            
        };

    }

    public Treasure(String img, float getX,
            float getY,
            float getDelay,
            Item[] getItems) {
        super("event/chest/field/stand/down", getX, getY, getDelay);
        opened = new Animation(getDelay, GraphicsDriver.getMasterSheet().createSprites("event/chest/field/custom/down"));
        item = getItems;
        isFinished = false;
        setTriggerMethod(TriggerMethod.PRESS);
        setID(3);
        pickedUpMessage = new Novel() { 
            {
            chapters.add((Novel.Page) () -> {
                for (Item i : item) {
                    BattleDriver.printline("Obtained " + i.getQuantity() + " " + i.getName() + "!");
                }
                BattleDriver.addItems(item);
                Treasure.this.changeAnimation(opened);
            });
        }
            @Override
            public String getMusic() {
                return null;
            }
            
        };
    }
    
    
    public void run() {
        if (!GraphicsDriver.getState().contains(pickedUpMessage, true)) {
            GraphicsDriver.addState(pickedUpMessage);
        }
        isFinished = pickedUpMessage.isFinished();
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
