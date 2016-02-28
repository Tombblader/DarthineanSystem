/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerAI;
import com.ark.darthsystem.BattlerClass;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.Scenario;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.States.Battle;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class AIDatabase {

    public static BattlerAI Ghost =
            new BattlerAI("Ghost", Battle.Element.Dark, Battler.Gender.Male, 1,
            26, 9, 7, 4, 4, 4, null,
            new Equipment[]{null, null, null, null, null}, Scenario.Brainless,
            20, ItemDatabase.Potion, 1);
    public static BattlerAI Undead_Darth =
            new BattlerAI("Darth???", Battle.Element.Dark, Battler.Gender.Male,
            5, 250, 34, 13, 9, 13, 13, Database1.Darth.getSkillList(), SystemDatabase.Warrior, Scenario.Standard, 100, ItemDatabase.Bronze_Sword,
            1);
    public static BattlerAI Kraken =
            new BattlerAI("Kraken", Battle.Element.Water, Battler.Gender.Male,
            17, 710, 61, 16, 17, 15, 1,
            new Skill[]{SkillDatabase.Frozen_Miasama, SkillDatabase.Hallucination}, SystemDatabase.Warrior, Scenario.Standard_Support, 140, null, 0);
    public static BattlerAI Ugly_Demon =
            new BattlerAI("Ugly Demon", Battle.Element.Dark, Battler.Gender.Male,
            20, 450, 40, 60, 50, 41, 54,
            new Skill[]{SkillDatabase.Poison_Edge, SkillDatabase.Storm_Gigas, SkillDatabase.Burial_Swipe}, SystemDatabase.Warrior, Scenario.Standard, 0, null, 0);
    
}
