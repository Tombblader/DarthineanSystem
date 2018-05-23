/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.ark.darthsystem.Item;
import com.ark.darthsystem.database.ShopMenu;
import com.ark.darthsystem.graphics.GraphicsDriver;

/**
 *
 * @author keven
 */
public class Shopkeeper extends Event {
    private Item[] inventory;
    private ShopMenu menu;
    public Shopkeeper(String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
//        menu = new ShopMenu("", inventory, null);
    }

    @Override
    public void run() {
        GraphicsDriver.addMenu(menu);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}
