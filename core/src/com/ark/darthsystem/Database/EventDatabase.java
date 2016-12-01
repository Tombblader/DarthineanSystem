/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;


import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.States.events.Pickup;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class EventDatabase {
    public static Pickup GraphicsPotion = new Pickup((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("items/potion/icon").toArray(Sprite.class), 350.0f, 350.0f, .1f, new Item("Meat"));
}
