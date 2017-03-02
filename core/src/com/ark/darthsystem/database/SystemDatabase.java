/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.BattlerClass;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.states.Battle;
import static com.ark.darthsystem.database.SkillDatabase.*;
import static com.ark.darthsystem.database.ItemDatabase.*;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class SystemDatabase {
    public static final HashMap<Battle.Element, Double> weakness = new HashMap<>();
    public static final HashMap<Battle.Stats, Double> statusVulnerability = new HashMap<>();

    public static Skill[] Magic_Knight_Moveset = {CrossCall, Heal, Darth_Fireball, Feint_Sword_Tackle, Frozen_Miasama, Tornado_Spin, Terra_Wall, Sword_Dancer, Four_Fury, Entropy};
    public static Skill[] Warrior_Moveset = {Red_Spin, Red_Flail, Feint_Sword_Tackle, Red_Slayer, Seal_Breaker};
    public static Skill[] Lancer_Moveset = {Leg_Sweep, Tiger_Thrust, Defense_Rush, Thunder_Thrust, Reign_of_Terror};
    public static Skill[] Weapon_Master_Moveset = {Poison_Edge, Ray_Assassin, Tiger_Thrust, Feint_Sword_Tackle, Weapon_Master};
    public static Skill[] Axeman_Moveset = {Veather_Sprawl, Veather_Charm, Bladed_Din, Burial_Swipe, Brainwashing_Axe, Black_Miasama, Axe_Effect};
    public static Skill[] Mage_Moveset = {Heal, Antidote, Awaken, Defog, Honey_Voice, Diffuse, Cell_Rejuvination, Storm_Gigas, Mud_Hand, Silver_Flame, Thunderstorm, Terra_Wall, Inferno, Crystal_Whirl, HealMore, HealAll, HealUs, Darknight};
    public static Skill[] Angel_Moveset = {CrossCall, Storm_Gigas, Sword_Dancer, HealMore, HealAll, HealUs, Honey_Voice, Awaken, Revivify, White_Curse, Cell_Rejuvination, Fairy_Circle};
    public static Skill[] Water_Spirit_Moveset = {CrossCall, Heal, Sap_Shot, Frozen_Miasama, Feint_Sword_Tackle, Crystal_Whirl, Sword_Dancer, HealMore, HealUs, Revivify};
    public static Skill[] Fire_Spirit_Moveset = {Red_Spin, Fireball.overrideLevel(1), Red_Flail, Feint_Sword_Tackle, Red_Slayer, Seal_Breaker};

    public static Equipment[] MagicKnight = {Wooden_Sword, Wooden_Shield, Tunic, Headband, null};
    public static Equipment[] Warrior = {Wooden_Sword, Wooden_Shield, Tunic, Headband, null};
    public static Equipment[] Lancer = {Wooden_Staff, Wooden_Shield, Tunic, null, null};
    public static Equipment[] Master = {Bronze_Sword, null, null, null, null};
    public static Equipment[] Heavy_Warrior = {Hand_Axe, Wooden_Shield, Bronze_Armor, Headband, null};
    public static Equipment[] Angel = {White_Sword, Grooved_Gauntlet, Queens_Robe, Silver_Ribbon, null};
    public static Equipment[] Nothing = {null, null, null, null, null};
    public static Equipment[] Water_Spirit_Equipment = {Bronze_Sword, Bronze_Shield, Bronze_Armor, Bronze_Helmet, null};


    public static BattlerClass Magic_Knight_Class = new BattlerClass("Magic Knight", null, Magic_Knight_Moveset);
    public static BattlerClass Swordsman_Class = new BattlerClass("Swordsman", null, Warrior_Moveset);
    public static BattlerClass Lancer_Class = new BattlerClass("Lancer", null, Lancer_Moveset);
    public static BattlerClass Weapon_Master_Class = new BattlerClass("Weapon Master", null, Weapon_Master_Moveset);
    public static BattlerClass Axeman_Class = new BattlerClass("Axeman", null, Axeman_Moveset);
    public static BattlerClass Mage_Class = new BattlerClass("Mage", null, Mage_Moveset);
    public static BattlerClass Angel_Class = new BattlerClass("Angel", null, Angel_Moveset);
}
