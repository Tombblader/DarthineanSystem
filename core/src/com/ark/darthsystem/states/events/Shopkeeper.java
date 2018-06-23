/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.database.CharacterDatabase;
import com.ark.darthsystem.database.ItemDatabase;
import com.ark.darthsystem.database.ShopMenu;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.ark.darthsystem.states.chapters.Novel;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author keven
 */
public class Shopkeeper extends Event {
    private Item[] inventory;
    private ShopMenu menu;
    public Shopkeeper() {
        super("", 0, 0, 6/60f);
    }
    
    public Shopkeeper(String img, String[] items, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
        inventory = new Item[items.length];
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = ItemDatabase.ITEM_LIST.get(items[i].toUpperCase());
        }
        this.setTriggerMethod(TriggerMethod.PRESS);
        menu = new ShopMenu("What did you want?", inventory, new String[]{"What would you like to buy?", "What would you like to sell?"});
    }

    @Override
    public void run() {
        if (!(GraphicsDriver.getCurrentState() instanceof Novel)) {
            GraphicsDriver.addState(new Novel() {
                @Override
                public String getMusic() {
                    return null;
                }
                {
                    this.chapters.add(() -> BattleDriver.printline(CharacterDatabase.CHARACTER_LIST.get("GREEN LADY"), "Look... at... my wares..."));
                    this.chapters.add(() -> GraphicsDriver.addMenu(new ShopMenu("What... would you like...?", inventory, new String[]{"Buying.. is good..", "I... don't like buying from you..."})));
                }
                });
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Event createFromMap(MapProperties prop) {
        String image = prop.get("image", String.class);
        Shopkeeper s = new Shopkeeper(image,
                prop.get("parameters", String.class).split(", *"),
                (prop.get("x", Float.class) + prop.get("width", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                (prop.get("y", Float.class) + prop.get("height", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                6 / 60f);
        s.setTriggerMethod(Event.TriggerMethod.valueOf(prop.get("trigger", String.class)));
        return s;
    }
    
}
