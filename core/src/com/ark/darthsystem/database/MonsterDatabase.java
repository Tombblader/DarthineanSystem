/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import static com.ark.darthsystem.database.CharacterDatabase.CHARACTER_LIST;
import static com.ark.darthsystem.database.SpriteDatabase.*;
import com.ark.darthsystem.graphics.ActorBattler;
import com.ark.darthsystem.graphics.ActorAI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class MonsterDatabase {

    public static ActorAI ProtoxAI;
    public static ActorAI ErikAI;
    public static ActorAI Eyesore_Actor;
    public static ActorAI Mouthsore_Actor;
    public static ActorAI Living_Sword_Actor;
    public static HashMap<String, ActorAI> MONSTER_LIST = new HashMap<>();

    public MonsterDatabase() {
        FileHandle file = Gdx.files.internal("databases/actorbattlersai.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;
            
            MONSTER_LIST.put(data[i].toUpperCase(), new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler(AIDatabase.BATTLER_LIST.get(data[i].toUpperCase()), //Name
                    "monsters/" + data[++i], //Sprite Name
                    (float) (1f / Double.parseDouble(data[++i])), //fps
                    data[++i], //ShapeName
                    (float) Double.parseDouble(data[++i]) //Speed
                    
            )})), 0, 0));
        
//        ErikAI = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler((new BattlerAI(
//                    "Erik the Red",
//                    Erik,
//                    Scenario.Standard,
//                    50,
//                    null,
//                    0)),
//                    ErikSprite)})), 0, 0);
//        Eyesore_Actor = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler(AIDatabase.BATTLER_LIST.get("EYESORE"), Eyesore_Sprite)})), 0,0);
//        Mouthsore_Actor = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler(AIDatabase.BATTLER_LIST.get("MOUTHSORE"), Mouthsore_Sprite)})), 0,0);
//        Living_Sword_Actor = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler(Living_Sword, Living_Sword_Sprite)})), 0,0);
//        monsters.put("ERIK_AI", ErikAI);
//        monsters.put("EYESORE", Eyesore_Actor);
//        monsters.put("MOUTHSORE", Mouthsore_Actor);
//        monsters.put("LIVING_SWORD", Living_Sword_Actor);
        
        }
    }
}
