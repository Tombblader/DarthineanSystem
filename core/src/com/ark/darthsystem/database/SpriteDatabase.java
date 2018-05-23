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
    public static ActorSprite Mouthsore_Sprite;
    public static ActorSprite Living_Sword_Sprite;

    public SpriteDatabase() {
        ErikSprite = new ActorSprite("characters/darcy_alma");
        DarthSprite = new ActorSprite("characters/darcy_alma");
        Water_Spirit_Sprite = new ActorSprite("characters/water_spirit");
        Fire_Spirit_Sprite = new ActorSprite("characters/fire_spirit");
        Eyesore_Sprite = new ActorSprite("monsters/eyesore");
        Mouthsore_Sprite = new ActorSprite("monsters/mouthsore");
        Living_Sword_Sprite = new ActorSprite("monsters/eyesore");
    }
}
