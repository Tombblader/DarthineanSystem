/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.States.Battle;

/**
 *
 * @author Keven
 */
public class ItemDatabase {

    public static Equipment Wooden_Staff =
            new Equipment("Wooden Staff", Equipment.EquipmentType.MainHand, null,
            false, 2, 0, 0, 0);
    public static Equipment Bronze_Sword =
            new Equipment("Bronze Sword", Equipment.EquipmentType.MainHand, null,
            false, 6, 0, 0, 0);
    public static Equipment Tunic =
            new Equipment("Tunic", Equipment.EquipmentType.Body, null, false, 0,
            1, 0, 0);
    public static Equipment Silver_Ribbon =
            new Equipment("Silver Ribbon", Equipment.EquipmentType.Head, null,
            false, 0, 12, 1, 2);
    public static Equipment Roof_Cap =
            new Equipment("Roof Cap", Equipment.EquipmentType.Head, null, false,
            1, 9, 0, 0);
    public static Equipment Wooden_Sword =
            new Equipment("Wooden Sword", Equipment.EquipmentType.MainHand, null,
            false, 2, 0, 0, 0);
    public static Equipment Blue_Javelin =
            new Equipment("Blue Javelin", Equipment.EquipmentType.MainHand,
            SkillDatabase.Thunder_Thrust, Battle.Element.Water, true, 14, 0, 0,
            0);
    public static Equipment Red_Hood =
            new Equipment("Red Hood", Equipment.EquipmentType.Head, null, false,
            0, 7, 2, 2);
    public static Equipment White_Sword =
            new Equipment("White Sword", Equipment.EquipmentType.MainHand, null,
            Battle.Element.Light, false, 15, 0, 0, 15);
    public static Equipment Metal_Cloak =
            new Equipment("Metal Cloak", Equipment.EquipmentType.Body, null,
            false, 0, 12, 1, 0);
    public static Item Potion_All = new Item("Potion All", true, 35, 0, true);
    public static Equipment Knight_Armor =
            new Equipment("Knight Armor", Equipment.EquipmentType.Body, null,
            false, 0, 18, 0, 0);
    public static Equipment Golden_Axe =
            new Equipment("Golden Axe", Equipment.EquipmentType.MainHand,
            SkillDatabase.Burial_Swipe, Battle.Element.Earth, false, 21, 0, 0, 0);
    public static Equipment Bronze_Armor =
            new Equipment("Bronze Armor", Equipment.EquipmentType.Body, null,
            false, 0, 4, 0, 0);
    public static Equipment Violet_Staff =
            new Equipment("Violet Staff", Equipment.EquipmentType.MainHand,
            SkillDatabase.Storm_Gigas, Battle.Element.Wind, false, 2, 0, 20, 0);
    public static Equipment Dark_Saber =
            new Equipment("Dark Saber", Equipment.EquipmentType.MainHand, null,
            Battle.Element.Dark, false, 31, 0, 0, 5);
    public static Equipment Red_Sword =
            new Equipment("Red Sword", Equipment.EquipmentType.MainHand,
            SkillDatabase.Red_Slayer, Battle.Element.Fire, false, 17, 0, 0, 0);
    public static Equipment Headband =
            new Equipment("Headband", Equipment.EquipmentType.Head, null, false,
            0, 1, 1, 0);
    public static Equipment Grooved_Gauntlet =
            new Equipment("Grooved Gauntlet", Equipment.EquipmentType.OffHand,
            null, false, 0, 8, 2, 0);
    public static Equipment Hero_Circlet =
            new Equipment("Hero Circlet", Equipment.EquipmentType.Head, null,
            false, 0, 15, 1, 5);
    public static Equipment Bronze_Spear =
            new Equipment("Bronze Spear", Equipment.EquipmentType.MainHand, null,
            false, 7, 0, 0, 0);
    public static Item Charger = new Item("Charger", false, 0, 100, false);
    public static Equipment Iron_Armor =
            new Equipment("Iron Armor", Equipment.EquipmentType.Body, null,
            false, 0, 8, 0, 0);
    public static Equipment Iron_Shield =
            new Equipment("Iron Shield", Equipment.EquipmentType.OffHand, null,
            false, 0, 5, 0, 0);
    public static Item Tonic =
            new Item("Tonic", true, SkillDatabase.Revivify, false);
    public static Equipment Hand_Axe =
            new Equipment("Hand_Axe", Equipment.EquipmentType.MainHand, null,
            false, 8, 0, 0, 0);
    public static Equipment Crest_Shield =
            new Equipment("Crest Shield", Equipment.EquipmentType.OffHand, null,
            false, 0, 7, 0, 3);
    public static Equipment Queens_Robe =
            new Equipment("Queen's Robe", Equipment.EquipmentType.Body, null,
            false, 0, 24, 2, 10);
    public static Equipment Iron_Helmet =
            new Equipment("Iron Helmet", Equipment.EquipmentType.Head, null,
            false, 0, 6, 0, 0);
    public static Equipment Wooden_Shield =
            new Equipment("Wooden Shield", Equipment.EquipmentType.OffHand, null,
            false, 0, 1, 0, 0);
    public static Equipment Bronze_Shield =
            new Equipment("Bronze Shield", Equipment.EquipmentType.OffHand, null,
            false, 0, 3, 0, 0);
    public static Equipment Leather_Robe =
            new Equipment("Leather Robe", Equipment.EquipmentType.Body, null,
            false, 0, 5, 2, 0);
    public static Equipment Bronze_Helmet =
            new Equipment("Bronze Helmet", Equipment.EquipmentType.Head, null,
            false, 0, 3, 0, 0);
    public static Item Potion = new Item("Potion", true, 35, 0, false);
    
}
