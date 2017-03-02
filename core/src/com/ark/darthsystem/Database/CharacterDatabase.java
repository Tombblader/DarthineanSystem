/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import static com.ark.darthsystem.Database.Database1.*;
import static com.ark.darthsystem.Database.SpriteDatabase.*;
import com.ark.darthsystem.Graphics.ActorBattler;

/**
 *
 * @author Keven
 */
public class CharacterDatabase {
    public static ActorBattler Fire_Spirit_Battler;
    public static ActorBattler Water_Spirit_Battler;
    public static ActorBattler Darth_Battler;
    public static ActorBattler Erik_Battler;

    public CharacterDatabase() {
        Water_Spirit_Battler = new ActorBattler(Water_Spirit, Water_Spirit_Sprite);
        Fire_Spirit_Battler = new ActorBattler(Fire_Spirit, Fire_Spirit_Sprite);
        Darth_Battler = new ActorBattler(Darth, DarthSprite);
        Erik_Battler = new ActorBattler(Erik, ErikSprite);
        
    }

}
