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
import com.ark.darthsystem.graphics.PlayerCamera;
import com.ark.darthsystem.states.chapters.Novel;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.maps.MapProperties;
import java.util.Arrays;

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
        super("event/chest/field/stand/down", 0, 0, 6 / 60f);
    }

    public Treasure(String img, float getX,
            float getY,
            float getDelay,
            Item getItem) {
        super(img, getX, getY, getDelay);
//        setLocalSwitch(LocalSwitch.A);
        opened = new Animation(getDelay, GraphicsDriver.getMasterSheet().createSprites("event/chest/field/custom/down"));
        item = new Item[1];
        item[0] = getItem;
        money = 0;
        isFinished = false;
        setTriggerMethod(TriggerMethod.PRESS);
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
        if (!switches.getSwitch(LocalSwitch.Switch.A)) {
            if (!GraphicsDriver.getState().contains(pickedUpMessage, true)) {
                GraphicsDriver.addState(pickedUpMessage);
            }
            switches.setSwitch(LocalSwitch.Switch.A, pickedUpMessage.isFinished());
        }
    }

    @Override
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
                (prop.get("x", Float.class) + prop.get("width", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                (prop.get("y", Float.class) + prop.get("height", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                6 / 60f,
                ItemDatabase.ITEM_LIST.get(prop.get("parameters", String.class).toUpperCase()));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Arrays.deepHashCode(this.item);
        hash = 71 * hash + this.money;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Treasure other = (Treasure) obj;
        if (this.money != other.money) {
            return false;
        }
        if (!Arrays.deepEquals(this.item, other.item)) {
            return false;
        }
        return super.equals(obj);
    }

}
