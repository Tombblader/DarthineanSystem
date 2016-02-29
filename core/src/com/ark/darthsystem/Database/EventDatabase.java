/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;


import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.States.chapters.Chapter1;
import com.ark.darthsystem.States.events.NovelMode;
import com.ark.darthsystem.States.events.Pickup;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class EventDatabase {
    public static Pickup GraphicsPotion = new Pickup((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("items/potion").toArray(Sprite.class), 350.0f, 350.0f, .1f, ItemDatabase.Potion);
    public static NovelMode chapter1 = new NovelMode(new Chapter1(), null, 450.0f, 450.0f, .1f);
    
}
