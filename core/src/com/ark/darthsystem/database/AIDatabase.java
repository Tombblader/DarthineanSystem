package com.ark.darthsystem.database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerAI;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.Scenario;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class AIDatabase {
    
    public static final HashMap<String, Battler> BATTLER_LIST = new HashMap<>();
    
    public AIDatabase() {
        FileHandle file = Gdx.files.internal("databases/battlerais.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equalsIgnoreCase("Name")) {
                continue;
            }
            int i = 0;
            
            BATTLER_LIST.put(data[i].toUpperCase(), new BattlerAI(data[i], //Name
                    data[++i], //Description
                    Battle.Element.valueOf(data[++i]), //Element
                    Battler.Gender.valueOf(data[++i]), //Gender
                    Integer.parseInt(data[++i]), //Level
                    Integer.parseInt(data[++i]), //HP
                    Integer.parseInt(data[++i]), //MP
                    Integer.parseInt(data[++i]), //Attack
                    Integer.parseInt(data[++i]), //Defense
                    Integer.parseInt(data[++i]), //Speed
                    Integer.parseInt(data[++i]),  //Magic
                    new ArrayList<>(Arrays.asList(Arrays.stream(data[++i].split(", ")).map(j -> SkillDatabase.SKILL_LIST.get(j.toUpperCase())).toArray(Skill[] :: new))),
                    new EnumMap<>(Equipment.Slot.class),
                    Scenario.AI_TYPE.get(data[++i].toUpperCase()),
                    Integer.parseInt(data[++i]), // XP
                    Integer.parseInt(data[++i]), // Money
                    Arrays.stream(data[++i].split(", ")).map(j -> ItemDatabase.ITEM_LIST.get(j.toUpperCase())).toArray(Item[] :: new),
                    Arrays.stream(data[++i].split(", ")).mapToDouble(j -> Double.parseDouble(j)).toArray(),
                    Arrays.stream(data[++i].split(", ")).mapToInt(j -> Integer.parseInt(j)).toArray()
                    ));
        }
    }
//    public static BattlerAI Veather_Mann =
//            new BattlerAI("Veather Mann", Battle.Element.Male, Battler.Gender.Male,
//                5, 200, 34, 9, 9, 9, 9, Gray_Mage_Class, Gray_Mage, Standard, 100, ItemDatabase.Hand_Axe,
//                1);
//
//    public static BattlerAI Eyesore =
//            new BattlerAI("Eyesore", Battle.Element.Physical, Battler.Gender.Male, 1,
//            34, 99, 7, 4, 4, 7, new ArrayList<Skill>(Arrays.asList(new Skill[]{AISkillDatabase.Eyebeam})),
//            new Equipment[]{null, null, null, null, null}, Brainless_Skill,
//            20, ItemDatabase.Potion, 1);
//
//    public static BattlerAI Mouthsore =
//            new BattlerAI("Mouthsore", Battle.Element.Physical, Battler.Gender.Male, 1,
//            34, 99, 7, 4, 4, 7, new ArrayList<Skill>(Arrays.asList(new Skill[]{AISkillDatabase.Eyebeam})),
//            new Equipment[]{null, null, null, null, null}, Brainless_Skill,
//            20, ItemDatabase.Potion, 1);
//
//    public static BattlerAI Living_Sword =
//            new BattlerAI("Living Sword", Battle.Element.Physical, Battler.Gender.Female, 1,
//            34, 99, 7, 4, 4, 7, new ArrayList<Skill>(Arrays.asList(new Skill[]{SkillDatabase.SKILL_LIST.get("Wide Slash")})),
//            new Equipment[]{null, null, null, null, null}, Brainless_Skill,
//            20, ItemDatabase.Potion, 1);
//            
//    public static BattlerAI Ghost =
//            new BattlerAI("Ghost", Battle.Element.Dark, Battler.Gender.Male, 1,
//            26, 9, 7, 4, 4, 4, new BattlerClass(),
//            new Equipment[]{null, null, null, null, null}, Brainless,
//            20, ItemDatabase.Potion, 1);
//    public static BattlerAI Undead_Darth = 
//            new BattlerAI("Darth???", Battle.Element.Dark, Battler.Gender.Male,
//            5, 250, 34, 13, 9, 13, 13, Magic_Knight_Class, Magic_Knight, Standard, 100, ItemDatabase.Bronze_Sword,
//            1);
//    public static BattlerAI Kraken =
//            new BattlerAI("Kraken", Battle.Element.Water, Battler.Gender.Male,
//            17, 710, 61, 16, 17, 15, 1,
//            new ArrayList<Skill>(Arrays.asList(new Skill[]{Frozen_Miasama, Hallucination})), Warrior, Standard_Support, 140, null, 0);
//    public static BattlerAI Ugly_Demon =
//            new BattlerAI("Ugly Demon", Battle.Element.Dark, Battler.Gender.Male,
//            20, 450, 40, 60, 50, 41, 54,
//            new ArrayList<Skill>(Arrays.asList(new Skill[]{Poison_Edge, Storm_Gigas, Burial_Swipe})), Warrior, Standard, 0, null, 0);
    
}
