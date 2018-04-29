/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import static com.ark.darthsystem.database.SpriteDatabase.*;
import com.ark.darthsystem.graphics.ActorBattler;

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
        Water_Spirit_Battler = new ActorBattler(Database1.BATTLER_LIST.get("BLUE LADY"), Water_Spirit_Sprite);
        Fire_Spirit_Battler = new ActorBattler(Database1.BATTLER_LIST.get("RED LADY"), Fire_Spirit_Sprite);
        Darth_Battler = new ActorBattler(Database1.BATTLER_LIST.get("DARCY"), DarthSprite);
        Erik_Battler = new ActorBattler(Database1.BATTLER_LIST.get("ERIK"), ErikSprite);
        
    }

}
