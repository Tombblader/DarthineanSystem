/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.Graphics.*;
import com.ark.darthsystem.States.events.*;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author trankt1
 */
public class Database {

//    public static Player[] players = new Player[2];
    public static Player player;
    public static ArrayList<Event> events = new ArrayList<>();
    public static ActorSprite defaultRedSprite = new ActorSprite("characters/flint_spear");
    public static ActorSprite defaultBlueSprite = new ActorSprite("characters/flint_spear");
    public static ActorSprite defaultYellowSprite = new ActorSprite("characters/mammoth");
    

    public Database() {
        defaultRedSprite = new ActorSprite("characters/flint_spear");
        defaultBlueSprite = new ActorSprite("characters/flint_spear");
        defaultYellowSprite = new ActorSprite("characters/mammoth");
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
                ActorSkill.Area.FRONT) {{setShape("thinthrust");}};
    }
    
    public static ActorSkill BasicMonster() {
        return new ActorSkill(GraphicsDriver.getMasterSheet().createSprites("items/equipment/sword/field/field").toArray(Sprite.class),
                1,
                1,
                1f/24f,
                ActorSkill.Area.FRONT) {{setShape("monsterslash");}};
    }
}
