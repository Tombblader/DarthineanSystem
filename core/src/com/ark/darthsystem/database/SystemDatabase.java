/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.BattlerClass;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.states.Battle;
import static com.ark.darthsystem.database.SkillDatabase.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class SystemDatabase {

    public static final HashMap<Battle.Element, Double> WEAKNESS = new HashMap<>();
    public static final HashMap<String, BattlerClass> CLASS_LIST = new HashMap<>();

    public SystemDatabase() {
        FileHandle file = Gdx.files.internal("databases/classes.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");

        ArrayList<String> relevantData = new ArrayList<>();
        String mode = "";
        HashMap<Integer, Skill[]> tempMap = new HashMap<>();
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equalsIgnoreCase("Name")) {
                mode = "Name";
                if (!relevantData.isEmpty()) {
                    CLASS_LIST.put(relevantData.get(0).toUpperCase(), new BattlerClass(relevantData.get(0), //Name
                            relevantData.get(1).split("\n"), //Description
                            tempMap
                    ));
                    relevantData.clear();
                }
                relevantData.add(data[1]);
                tempMap = new HashMap<>();
                continue;
            }
            if (data[0].equalsIgnoreCase("Equipment")) {
                mode = "Equipment";
                relevantData.add(String.join("\n", Arrays.copyOfRange(data, 1, data.length)));
                continue;
            }
            if (data[0].equalsIgnoreCase("Skills")) {
                mode = data[0];
                continue;
            }
            if (mode.equalsIgnoreCase("Skills")) {
                tempMap.put(Integer.parseInt(data[0]),
                        Arrays.stream(Arrays.copyOfRange(data, 1, data.length))
                                .map(skill -> SKILL_LIST.get(skill.toUpperCase()))
                                .toArray(Skill[]::new)
                );

            }

        }
        int i = 0;

        CLASS_LIST.put(relevantData.get(i).toUpperCase(), new BattlerClass(relevantData.get(i), //Name
                relevantData.get(++i).split("\n"), //Description
                tempMap
        )); //Divider
    }

//    public static final HashMap<Battle.Stats, Double> STATUS_VULNERABILITY = new HashMap<>();
//
//    public static HashMap<Integer, Skill[]> Magic_Knight_Moveset = new HashMap<Integer, Skill[]>()
//    {
//        {
//            put(1, new Skill[]{CrossCall, Heal}); 
//            put(3, new Skill[]{Darth_Fireball}); 
//            put(5, new Skill[]{Feint_Sword_Tackle});
//            put(7, new Skill[]{Frozen_Miasama}); 
//            put(9, new Skill[]{Tornado_Spin}); 
//            put(10, new Skill[]{Terra_Wall}); 
//            put(11, new Skill[]{Sword_Dancer}); 
//            put(13, new Skill[]{Fours_Fury}); 
//            put(15, new Skill[]{Entropy});                    
//        }    
//    };
//    public static HashMap<Integer, Skill[]> Warrior_Moveset = new HashMap<Integer, Skill[]>()
//    {
//        {
//            put(1, new Skill[]{Red_Spin}); 
//            put(3, new Skill[]{Red_Flail}); 
//            put(5, new Skill[]{Feint_Sword_Tackle});
//            put(7, new Skill[]{Frozen_Miasama}); 
//            put(9, new Skill[]{Red_Slayer}); 
//            put(10, new Skill[]{Seal_Breaker}); 
//        }    
//    };
//    public static HashMap<Integer, Skill[]> Lancer_Moveset = new HashMap<Integer, Skill[]>(){
//        {
//            put(1, new Skill[]{Leg_Sweep}); 
//            put(3, new Skill[]{Tiger_Thrust});
//            put(5, new Skill[]{Defense_Rush}); 
//            put(7, new Skill[]{Thunder_Thrust});
//            put(9, new Skill[]{Reign_of_Terror});
//        }
//    };
//    public static HashMap<Integer, Skill[]> Weapon_Master_Moveset =  new HashMap<Integer, Skill[]>()
//    {
//        {
//            put(1, new Skill[]{Poison_Edge, Ray_Assassin});
//            put(3, new Skill[]{Tiger_Thrust});
//            put(5, new Skill[]{Feint_Sword_Tackle});
//            put(7, new Skill[]{Weapon_Master});
//        }
//    };
//    public static HashMap<Integer, Skill[]> Gray_Mage_Moveset = new HashMap<Integer, Skill[]>()
//    {
//        {
//            put(1, new Skill[]{Veather_Sprawl, Veather_Charm});
//            put(3, new Skill[]{Bladed_Din});
//            put(5, new Skill[]{Burial_Swipe}); 
//            put(7, new Skill[]{Brainwashing_Axe});
//            put(9, new Skill[]{Black_Miasama});
//            put(11, new Skill[]{Axe_Effect});
//        }
//    };
//    public static HashMap<Integer, Skill[]> Mage_Moveset = new HashMap<Integer, Skill[]>()
//    {
//        {
//            put(1, new Skill[]{Heal, Antidote, Awaken, Defog, Honey_Voice, Diffuse, Cell_Rejuvination});
//            put(3, new Skill[]{Storm_Gigas});
//            put(5, new Skill[]{Mud_Hand});
//            put(7, new Skill[]{Silver_Flame});
//            put(9, new Skill[]{Thunderstorm});
//            put(11, new Skill[]{Terra_Wall});
//            put(13, new Skill[]{Inferno});
//            put(15, new Skill[]{Crystal_Whirl});
//            put(17, new Skill[]{HealMore});
//            put(19, new Skill[]{HealAll});
//            put(21, new Skill[]{HealUs});
//            put(23, new Skill[]{Darknight});
//        }
//    };
//    public static HashMap<Integer, Skill[]> Angel_Moveset = new HashMap<Integer, Skill[]>()
//    {
//        {
//            put(1, new Skill[]{CrossCall});
//            put(3, new Skill[]{Storm_Gigas});
//            put(5, new Skill[]{Sword_Dancer});
//            put(7, new Skill[]{HealMore});
//            put(9, new Skill[]{HealAll});
//            put(11, new Skill[]{HealUs});
//            put(13, new Skill[]{Honey_Voice});
//            put(15, new Skill[]{Awaken});
//            put(17, new Skill[]{Revivify});
//            put(19, new Skill[]{White_Curse});
//            put(21, new Skill[]{Cell_Rejuvination});
//            put(23, new Skill[]{Fairy_Circle});
//        }            
//    };
//    public static HashMap<Integer, Skill[]> Water_Spirit_Moveset = new HashMap<Integer, Skill[]>()
//    {
//        {
//            put(1, new Skill[]{CrossCall, Heal, Sap_Shot});
//            put(3, new Skill[]{Frozen_Miasama});
//            put(5, new Skill[]{Feint_Sword_Tackle});
//            put(7, new Skill[]{Crystal_Whirl});
//            put(9, new Skill[]{Sword_Dancer});
//            put(11, new Skill[]{HealMore});
//            put(13, new Skill[]{HealUs});
//            put(15, new Skill[]{Revivify});
//        }
//    };
//    public static HashMap<Integer, Skill[]> Fire_Spirit_Moveset = new HashMap<Integer, Skill[]>()
//    {
//        {
//        
//            put(1, new Skill[]{Red_Spin, Fireball});
//            put(3, new Skill[]{Red_Flail});
//            put(5, new Skill[]{Feint_Sword_Tackle});
//            put(7, new Skill[]{Red_Slayer});
//            put(11, new Skill[]{Seal_Breaker});
//        }
//    };
//
//    public static Equipment[] Magic_Knight = {Wooden_Sword, Wooden_Shield, Tunic, Headband, null};
//    public static Equipment[] Warrior = {Wooden_Sword, Wooden_Shield, Tunic, Headband, null};
//    public static Equipment[] Lancer = {Wooden_Staff, Wooden_Shield, Tunic, null, null};
//    public static Equipment[] Master = {Bronze_Sword, null, null, null, null};
//    public static Equipment[] Gray_Mage = {Hand_Axe, Wooden_Shield, Bronze_Armor, Headband, null};
//    public static Equipment[] Angel = {White_Sword, Grooved_Gauntlet, Queens_Robe, Silver_Ribbon, null};
//    public static Equipment[] Nothing = {null, null, null, null, null};
//    public static Equipment[] Water_Spirit_Equipment = {Bronze_Sword, Bronze_Shield, Bronze_Armor, Bronze_Helmet, null};
//    public static BattlerClass Magic_Knight_Class = new BattlerClass("Magic Knight", null, Magic_Knight_Moveset);
//    public static BattlerClass Swordsman_Class = new BattlerClass("Swordsman", null, Warrior_Moveset);
//    public static BattlerClass Lancer_Class = new BattlerClass("Lancer", null, Lancer_Moveset);
//    public static BattlerClass Weapon_Master_Class = new BattlerClass("Weapon Master", null, Weapon_Master_Moveset);
//    public static BattlerClass Gray_Mage_Class = new BattlerClass("Gray Mage", null, Gray_Mage_Moveset);
//    public static BattlerClass Mage_Class = new BattlerClass("Mage", null, Mage_Moveset);
//    public static BattlerClass Angel_Class = new BattlerClass("Angel", null, Angel_Moveset);
//    public static BattlerClass Water_Spirit_Class = new BattlerClass("Water Spirit", null, Water_Spirit_Moveset);
//    public static BattlerClass Fire_Spirit_Class = new BattlerClass("Fire Spirit", null, Fire_Spirit_Moveset);
}
