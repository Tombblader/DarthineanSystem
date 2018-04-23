/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class ItemDatabase {
    public static final HashMap<String, Equipment> EQUIPMENT_LIST = new HashMap<>();
    public static final HashMap<String, Item> ITEM_LIST = new HashMap<>();
    
    public ItemDatabase() {
        FileHandle file = Gdx.files.internal("databases/equipment.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;
            
            EQUIPMENT_LIST.put(data[i].toUpperCase(), new Equipment(data[i], //Name
                    data[++i], //Description
                    Integer.parseInt(data[++i]), //Market Price
                    data[++i].split(", "), //Equipment Types
                    Equipment.Slot.valueOf(data[++i]), //Slot
                    SkillDatabase.SKILL_LIST.get(data[++i].toUpperCase()),
                    Battle.Element.valueOf(data[++i]), //Element
                    Boolean.valueOf(data[++i].toLowerCase()), //Use MP?
                    Integer.parseInt(data[++i]), //Attack
                    Integer.parseInt(data[++i]), //Defense Multiplier
                    Integer.parseInt(data[++i]), //Speed Multiplier
                    Integer.parseInt(data[++i])  //Magic Multiplier
                    )); //Divider
        }
        file = Gdx.files.internal("databases/items.tsv");
        massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equalsIgnoreCase("Name")) {
                continue;
            }
            int i = 0;
            
            ITEM_LIST.put(data[i].toUpperCase(), new Item(data[i], //Name
                    data[++i], //Description
                    Integer.parseInt(data[++i]), //Market Price
                    Integer.parseInt(data[++i]), //Charges
                    Integer.parseInt(data[++i]), //HP Effect
                    Integer.parseInt(data[++i]), //MP Effect
                    Boolean.valueOf(data[++i].toLowerCase()) //Is All?
                    ));
            
        }
        file = Gdx.files.internal("databases/special_items.tsv");
        massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equalsIgnoreCase("Name")) {
                continue;
            }
            int i = 0;
            
            ITEM_LIST.put(data[i].toUpperCase(), new Item(data[i], //Name
                    data[++i], //Description
                    Integer.parseInt(data[++i]), //Market Price
                    Integer.parseInt(data[++i]), //Charges
                    SkillDatabase.SKILL_LIST.get(data[++i]), //Skill Effect
                    Boolean.valueOf(data[++i].toLowerCase()) //Is All?
                    ));
            
        }
    }


//
//    public static Equipment Wooden_Staff =
//            new Equipment("Wooden Staff", MainHand, null, false, 2, 0, 0, 0);
//    public static Equipment Bronze_Sword =
//            new Equipment("Bronze Sword", MainHand, null, false, 6, 0, 0, 0);
//    public static Equipment Tunic =
//            new Equipment("Tunic", Body, null, false, 0, 1, 0, 0);
//    public static Equipment Silver_Ribbon =
//            new Equipment("Silver Ribbon", Head, null, false, 0, 12, 1, 2);
//    public static Equipment Roof_Cap =
//            new Equipment("Roof Cap", Head, null, false, 1, 9, 0, 0);
//    public static Equipment Wooden_Sword =
//            new Equipment("Wooden Sword", MainHand, null, false, 2, 0, 0, 0);
//    public static Equipment Blue_Javelin =
//            new Equipment("Blue Javelin", MainHand, SkillDatabase.Thunder_Thrust, Water, true, 14, 0, 0, 0);
//    public static Equipment Red_Hood =
//            new Equipment("Red Hood", Head, null, false, 0, 7, 2, 2);
//    public static Equipment White_Sword =
//            new Equipment("White Sword", MainHand, null, Light, false, 15, 0, 0, 15);
//    public static Equipment Metal_Cloak = 
//            new Equipment("Metal Cloak", Body, null, false, 0, 12, 1, 0);
//    public static Equipment Knight_Armor =
//            new Equipment("Knight Armor", Body, null,false, 0, 18, 0, 0);
//    public static Equipment Golden_Axe =
//            new Equipment("Golden Axe", MainHand, SkillDatabase.Burial_Swipe, Earth, false, 21, 0, 0, 0);
//    public static Equipment Bronze_Armor =
//            new Equipment("Bronze Armor", Body, null, false, 0, 4, 0, 0);
//    public static Equipment Violet_Staff =
//            new Equipment("Violet Staff", MainHand, SkillDatabase.Storm_Gigas, Wind, false, 2, 0, 20, 0);
//    public static Equipment Dark_Saber =
//            new Equipment("Dark Saber", MainHand, null, Dark, false, 31, 0, 0, 5);
//    public static Equipment Red_Sword =
//            new Equipment("Red Sword", MainHand, SkillDatabase.Red_Slayer, Fire, false, 17, 0, 0, 0);
//    public static Equipment Headband =
//            new Equipment("Headband", Head, null, false, 0, 1, 1, 0);
//    public static Equipment Grooved_Gauntlet =
//            new Equipment("Grooved Gauntlet", OffHand, null, false, 0, 8, 2, 0);
//    public static Equipment Hero_Circlet =
//            new Equipment("Hero Circlet", Head, null, false, 0, 15, 1, 5);
//    public static Equipment Bronze_Spear =
//            new Equipment("Bronze Spear", MainHand, null, false, 7, 0, 0, 0);
//    public static Equipment Iron_Armor =
//            new Equipment("Iron Armor", Body, null, false, 0, 8, 0, 0);
//    public static Equipment Iron_Shield =
//            new Equipment("Iron Shield", OffHand, null, false, 0, 5, 0, 0);
//    public static Equipment Hand_Axe =
//            new Equipment("Hand_Axe", MainHand, null, false, 8, 0, 0, 0);
//    public static Equipment Crest_Shield =
//            new Equipment("Crest Shield", OffHand, null, false, 0, 7, 0, 3);
//    public static Equipment Queens_Robe =
//            new Equipment("Queen's Robe", Body, null, false, 0, 24, 2, 10);
//    public static Equipment Iron_Helmet =
//            new Equipment("Iron Helmet", Head, null, false, 0, 6, 0, 0);
//    public static Equipment Wooden_Shield =
//            new Equipment("Wooden Shield", OffHand, null, false, 0, 1, 0, 0);
//    public static Equipment Bronze_Shield =
//            new Equipment("Bronze Shield", OffHand, null, false, 0, 3, 0, 0);
//    public static Equipment Leather_Robe =
//            new Equipment("Leather Robe", Body, null, false, 0, 5, 2, 0);
//    public static Equipment Bronze_Helmet =
//            new Equipment("Bronze Helmet", Head, null, false, 0, 3, 0, 0);
//
//    public static Item Potion = new Item("Potion", true, 35, 0, false);
//    public static Item Potion_All = new Item("Potion All", true, 35, 0, true);
//    public static Item Tonic =
//            new Item("Tonic", true, SkillDatabase.Revivify, false);
//    public static Item Charger = new Item("Charger", false, 0, 100, false);
    
}
