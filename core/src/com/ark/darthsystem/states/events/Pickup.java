package com.ark.darthsystem.states.events;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.database.ItemDatabase;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author Keven
 */
public class Pickup extends Event {

    private Item[] item;
    private int money;
    private boolean isFinished;

    public Pickup() {
        super("", 0, 0, 6/60f);
    }
    
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
            BattleDriver.printline("Obtained" + (i.getCharges() > 0 ? " " + i.getCharges(): "") + " " + i.getName() + "!");
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
        String image = prop.get("image", String.class);
        Pickup p = new Pickup(image,
                (prop.get("x", Float.class) + prop.get("width", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                (prop.get("y", Float.class) + prop.get("height", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                1 / 12f,
                ((Item) ItemDatabase.ITEM_LIST.get(prop.get("parameters", String.class).toUpperCase()).clone()));
        p.setTriggerMethod(TriggerMethod.valueOf(prop.get("trigger", String.class).toUpperCase()));
        return p;
    }

}
