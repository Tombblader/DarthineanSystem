/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.BattlerAI;
import static com.ark.darthsystem.database.AIDatabase.*;
import static com.ark.darthsystem.database.Database1.*;
import static com.ark.darthsystem.database.SpriteDatabase.*;
import com.ark.darthsystem.graphics.ActorBattler;
import com.ark.darthsystem.graphics.ActorAI;
import com.ark.darthsystem.Scenario;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Keven
 */
public class MonsterDatabase {

    public static ActorAI ProtoxAI;
    public static ActorAI ErikAI;
    public static ActorAI Eyesore_Actor;

    public MonsterDatabase() {
        ErikAI = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler((new BattlerAI(
                    "Erik the Red",
                    Erik,
                    Scenario.Standard,
                    50,
                    null,
                    0)),
                    ErikSprite)})), 0, 0);
        Eyesore_Actor = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler(Eyesore, Eyesore_Sprite)})), 0,0);
    }
    

}
