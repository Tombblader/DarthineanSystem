/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.graphics.ActorBattler;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class CharacterDatabase {
    public static ActorBattler Fire_Spirit_Battler;
    public static ActorBattler Water_Spirit_Battler;
    public static ActorBattler Darth_Battler;
    public static ActorBattler Erik_Battler;
    public static HashMap<String, ActorBattler> CHARACTER_LIST = new HashMap<>();

    public CharacterDatabase() {
        
        FileHandle file = Gdx.files.internal("databases/actorbattlers.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;
            
            CHARACTER_LIST.put(data[i].toUpperCase(), new ActorBattler(Database1.BATTLER_LIST.get(data[i].toUpperCase()), //Name
                    "characters/" + data[++i], //Sprite Name
                    (float) (1f / Double.parseDouble(data[++i])), //fps
                    data[++i], //ShapeName
                    (float) Double.parseDouble(data[++i]) //Speed
            ));
        }        
        
//        Water_Spirit_Battler = new ActorBattler(Database1.BATTLER_LIST.get("BLUE LADY"), Water_Spirit_Sprite);
//        Fire_Spirit_Battler = new ActorBattler(Database1.BATTLER_LIST.get("RED LADY"), Fire_Spirit_Sprite);
//        Darth_Battler = new ActorBattler(Database1.BATTLER_LIST.get("DARCY"), DarthSprite);
//        Erik_Battler = new ActorBattler(Database1.BATTLER_LIST.get("ERIK"), ErikSprite);
        
    }

}
