/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerAI;
import com.ark.darthsystem.BattlerClass;
import com.ark.darthsystem.Equipment;
import static com.ark.darthsystem.Scenario.*;
import static com.ark.darthsystem.database.Database1.*;
import static com.ark.darthsystem.database.SystemDatabase.*;
import static com.ark.darthsystem.database.SkillDatabase.*;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.states.Battle;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Keven
 */
public class AIDatabase {
    public static BattlerAI Veather_Mann =
            new BattlerAI("Veather Mann", Battle.Element.Male, Battler.Gender.Male,
                5, 200, 34, 9, 9, 9, 9, Gray_Mage_Class, Gray_Mage, Standard, 100, ItemDatabase.Hand_Axe,
                1);

    public static BattlerAI Eyesore =
            new BattlerAI("Eyesore", Battle.Element.Physical, Battler.Gender.Male, 1,
            34, 99, 7, 4, 4, 7, new ArrayList<Skill>(Arrays.asList(new Skill[]{AISkillDatabase.Eyebeam})),
            new Equipment[]{null, null, null, null, null}, Brainless_Skill,
            20, ItemDatabase.Potion, 1);

    public static BattlerAI Mouthsore =
            new BattlerAI("Mouthsore", Battle.Element.Physical, Battler.Gender.Male, 1,
            34, 99, 7, 4, 4, 7, new ArrayList<Skill>(Arrays.asList(new Skill[]{AISkillDatabase.Eyebeam})),
            new Equipment[]{null, null, null, null, null}, Brainless_Skill,
            20, ItemDatabase.Potion, 1);

    public static BattlerAI Living_Sword =
            new BattlerAI("Living Sword", Battle.Element.Physical, Battler.Gender.Female, 1,
            34, 99, 7, 4, 4, 7, new ArrayList<Skill>(Arrays.asList(new Skill[]{SkillDatabase.SKILL_LIST.get("Wide Slash")})),
            new Equipment[]{null, null, null, null, null}, Brainless_Skill,
            20, ItemDatabase.Potion, 1);
            
    public static BattlerAI Ghost =
            new BattlerAI("Ghost", Battle.Element.Dark, Battler.Gender.Male, 1,
            26, 9, 7, 4, 4, 4, new BattlerClass(),
            new Equipment[]{null, null, null, null, null}, Brainless,
            20, ItemDatabase.Potion, 1);
    public static BattlerAI Undead_Darth = 
            new BattlerAI("Darth???", Battle.Element.Dark, Battler.Gender.Male,
            5, 250, 34, 13, 9, 13, 13, Magic_Knight_Class, Magic_Knight, Standard, 100, ItemDatabase.Bronze_Sword,
            1);
    public static BattlerAI Kraken =
            new BattlerAI("Kraken", Battle.Element.Water, Battler.Gender.Male,
            17, 710, 61, 16, 17, 15, 1,
            new ArrayList<Skill>(Arrays.asList(new Skill[]{Frozen_Miasama, Hallucination})), Warrior, Standard_Support, 140, null, 0);
    public static BattlerAI Ugly_Demon =
            new BattlerAI("Ugly Demon", Battle.Element.Dark, Battler.Gender.Male,
            20, 450, 40, 60, 50, 41, 54,
            new ArrayList<Skill>(Arrays.asList(new Skill[]{Poison_Edge, Storm_Gigas, Burial_Swipe})), Warrior, Standard, 0, null, 0);
    
}
