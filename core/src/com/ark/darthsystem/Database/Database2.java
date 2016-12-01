/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.*;
import com.ark.darthsystem.Graphics.*;
import com.ark.darthsystem.States.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author trankt1
 */
public class Database2 {

//    public static Player[] players = new Player[2];
    public static Player player;
    public static Monster ProtoxAI;
    public static Monster ErikAI;
    public static ArrayList<Event> events = new ArrayList<>();
    public static ActorSprite defaultSprite = new ActorSprite("characters/darth_invader");

    public Database2() {
        defaultSprite = new ActorSprite("characters/darth_invader");
    }
    public static Input createInputInstance() {
        return new Input();
    }

    public static Input createInputInstance(String instance) {
        return new Input();
    }
    public static ActorSkill Basic() {        
        return new ActorSkill(GraphicsDriver.getMasterSheet().createSprites("items/equipment/sword/field/field").toArray(Sprite.class),
                1,
                1,
                1f/24f,
                ActorSkill.Area.FRONT) {{setShape("widesword");}};
    }
}
