package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.ActorSprite;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Keven
 */
public class SpriteDatabase {
    public static ActorSprite DarthSprite;
    public static ActorSprite ErikSprite;
    public static ActorSprite Water_Spirit_Sprite;
    public static ActorSprite Fire_Spirit_Sprite;
    public static ActorSprite Eyesore_Sprite;

    public SpriteDatabase() {
        ErikSprite = new ActorSprite("characters/darth_invader");
        DarthSprite = new ActorSprite("characters/darth_invader");
        Water_Spirit_Sprite = new ActorSprite("characters/water_spirit");
        Fire_Spirit_Sprite = new ActorSprite("characters/fire_spirit");
        Eyesore_Sprite = new ActorSprite("monsters/eyesore");
    }
}
