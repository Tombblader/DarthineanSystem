/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.events;

import com.ark.darthsystem.Graphics.Actor;
import com.ark.darthsystem.Graphics.ActorCollision;
import static com.ark.darthsystem.Graphics.ActorCollision.CATEGORY_BLUE;
import static com.ark.darthsystem.Graphics.ActorCollision.CATEGORY_RED;
import com.ark.darthsystem.Graphics.Player;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.States.OverheadMap;
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

    public void run(Actor a) {
        if ((a instanceof Player) && ((Player) a).getCurrentLife() > 0) {
            ((Player) a).setItem(item);
            isFinished = true;
        } else {
            isFinished = false;
        }
    }
    public void generateBody(OverheadMap map) {
        super.generateBody(map);
        setMainFilter(ActorCollision.CATEGORY_EVENT, (short) (CATEGORY_RED | CATEGORY_BLUE));
        setSensorFilter(ActorCollision.CATEGORY_EVENT,(short) (CATEGORY_RED | CATEGORY_BLUE));
    }

    public boolean isFinished() {
        return isFinished;
    }

    public Item getItem() {
        return item;
    }

}
