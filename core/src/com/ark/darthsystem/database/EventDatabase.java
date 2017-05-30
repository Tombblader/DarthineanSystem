/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;


import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.chapters.*;
import com.ark.darthsystem.states.events.Pickup;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class EventDatabase {
    public static Pickup GraphicsPotion = new Pickup((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("items/potion/icon").toArray(Sprite.class), 350.0f, 350.0f, .1f, ItemDatabase.Potion);
    public static Novel chapters(String[] parameters){
        Novel event = null;
        switch (parameters[0]) {
            case "chapter0":
                event = new Chapter0();
                break;
            case "chapter01":
                event = new Chapter01();
                break;
            case "chapter1":
                event = new Chapter1();
        }
        return event;
    };
}