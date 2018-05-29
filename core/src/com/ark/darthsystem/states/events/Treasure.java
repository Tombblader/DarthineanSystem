/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.database.ItemDatabase;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.chapters.Novel;
import static com.ark.darthsystem.states.events.Event.setID;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author keven
 */
public class Treasure extends Event {

    private Item[] item;
    private int money;
    private boolean isFinished;
    private Novel pickedUpMessage;
    private Animation opened;

    public Treasure() {
        super("event/chest/field/stand/down", 0, 0, 6/60f);
    }
    
    public Treasure(String img, float getX,
            float getY,
            float getDelay,
            Item getItem) {
        super("event/chest/field/stand/down", getX, getY, getDelay);
        opened = new Animation(getDelay, GraphicsDriver.getMasterSheet().createSprites("event/chest/field/custom/down"));
        item = new Item[1];
        item[0] = getItem;
        money = 0;
        isFinished = false;
        setTriggerMethod(TriggerMethod.PRESS);
        setID(3);
        pickedUpMessage = new Novel() { 
            {
            chapters.add((Novel.Page) () -> {
                for (Item i : item) {
                    BattleDriver.printline("Obtained " + i.getCharges() + " " + i.getName() + "!");
                }
                if (money != 0) {
                    BattleDriver.printline("Got " + money + " GP!");            
                    Database1.money += money;
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
        money = 0;
        isFinished = false;
        setTriggerMethod(TriggerMethod.PRESS);
        setID(3);
        pickedUpMessage = new Novel() { 
            {
            chapters.add((Novel.Page) () -> {
                for (Item i : item) {
                    BattleDriver.printline("Obtained " + i.getCharges() + " " + i.getName() + "!");
                }
                BattleDriver.addItems(item);
                if (money != 0) {
                    BattleDriver.printline("Got " + money + " GP!");            
                    Database1.money += money;
                }
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

    @Override
    public Event createFromMap(MapProperties prop) {
        String image = prop.get("image", String.class);
        return new Treasure(image,
                prop.get("x", Float.class),
                prop.get("y", Float.class),
                6 / 60f,
                ItemDatabase.ITEM_LIST.get(prop.get("parameters", String.class).toUpperCase()));
    }

}
