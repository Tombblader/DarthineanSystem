package com.ark.darthsystem.Database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerAI;
import com.ark.darthsystem.BattlerClass;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.States.Battle;
import static com.ark.darthsystem.Scenario.*;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.Skill;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Database1 implements Serializable {

    public static final long serialVersionUID = 553786371;

    public static final HashMap<Battle.Stats, Double> statusVulnerability = new HashMap<>();
    public static final HashMap<Battle.Element, Double> weakness =  new HashMap<>();

    public static Skill CrossCall = new Skill("CrossCall",
            1,
            5,
            Battle.Element.Light,
            false, //Targets ally?
            false, //Targets all?
            Battle.Stats.Normal, //Inflict Status?
            0, 0.0, 0.0, //Level Difference/HP%/MP%
            6, 0,
            0, 6,
            0, 3,
            0, 3,
            5.5);
    public static Skill WitchesCross = new Skill("Witches' Cross",
            1,
            6,
            Battle.Element.Dark,
            false, //Targets ally?
            false, //Targets all?
            Battle.Stats.Normal, //Inflict Status?
            6, 6.6, 0.0, //Base/HP%/MP%
            6, 6,
            6, 0,
            5, 5,
            5, 0,
            6.66);
    public static Skill Red_Spin = new Skill("Red Spin",
            1,
            6,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Normal,
            2, 0.5, 0.0,
            10, 0,
            0, 0,
            0, 6,
            0, 0,
            6.0);
    public static Skill Leg_Sweep = new Skill("Leg Sweep",
            1,
            5,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Normal,
            0, 1.0, 0.0,
            0, 0,
            -6, 0,
            0, 0,
            -10, 0,
            6.0);
    public static Skill Poison_Edge = new Skill("Poison Edge",
            1,
            7,
            Battle.Element.Dark,
            false,
            true,
            Battle.Stats.Poison,
            0, 0.0, 0.0,
            10, 0,
            0, 0,
            0, 8,
            0, 0,
            6.0);

    public static Skill Heal = new Skill("Heal",
            2,
            3,
            Battle.Element.Heal,
            true,
            false,
            Battle.Stats.Normal,
            -20, 0.0, 0.0,
            0, 0,
            0, -1,
            0, 0,
            0, 1,
            1.0);
    public static Skill Darth_Fireball = new Skill("Darth Fireball",
            4,
            4,
            Battle.Element.Fire,
            false,
            false,
            Battle.Stats.Normal,
            20, 0.0, 0.0,
            0, 0,
            0, 6,
            0, 3,
            0, 3,
            5.0);
    public static Skill Fireball = new Skill("Fireball",
            4,
            4,
            Battle.Element.Fire,
            false,
            false,
            Battle.Stats.Normal,
            10, 0.0, 0.0,
            0, 0,
            0, 7,
            0, 0,
            0, 5,
            5.0);

    public static Skill Red_Flail = new Skill("Red Flail",
            4,
            9,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Normal,
            6, 0.0, 0.6,
            6, 0,
            0, 0,
            0, 6,
            0, 0,
            6.0);
    public static Skill Tiger_Thrust = new Skill("Tiger Thrust",
            8,
            10,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            12, 0,
            0, 0,
            0, 6,
            0, 0,
            5.5);
    public static Skill Feint_Sword_Tackle = new Skill("Feint Sword Tackle",
            7,
            8,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            6, 0,
            6, 0,
            3, 0,
            3, -1,
            5.5);
    public static Skill Ray_Assassin = new Skill("Ray Assassin",
            1,
            7,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            6, 0,
            6, 0,
            3, 0,
            3, 0,
            5.5);
    public static Skill Veather_Cop = new Skill("Veather Cop",
            1,
            10,
            Battle.Element.Male,
            false,
            false,
            Battle.Stats.Fog,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);

    public static Skill Nutcracker = new Skill("Nutcracker",
            1,
            10,
            Battle.Element.Female,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            1, 0,
            0, 0,
            1, -1,
            0, 0,
            1.0);

    public static Skill Tornado_Spin = new Skill("Tornado Spin",
            11,
            10,
            Battle.Element.Wind,
            false,
            true,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            4, 0,
            4, 4,
            1, 2,
            2, 2,
            5.5);
    public static Skill Sword_Dancer = new Skill("Sword Dancer",
            16,
            17,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Normal,
            10, 1.0, 0.0,
            6, 7,
            0, 0,
            0, 3,
            3, 0,
            5.5);
    public static Skill Defense_Rush = new Skill("Defense Rush",
            5,
            12,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            3, 9,
            0, 0,
            1, 5,
            0, 0,
            5.5);
    public static Skill Thunder_Thrust = new Skill("Thunder Thrust",
            15,
            18,
            Battle.Element.Wind,
            false,
            true,
            Battle.Stats.Normal,
            0, 1.0, 0.0,
            11, 0,
            0, 0,
            0, 3,
            0, 3,
            5.5);
    public static Skill Red_Slayer = new Skill("Red Slayer",
            10,
            9,
            Battle.Element.Fire,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            10, 0,
            0, 0,
            5, 0,
            0, 0,
            5.0);
    public static Skill Veather_Sprawl = new Skill("Veather Sprawl",
            1,
            7,
            Battle.Element.Male,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            1, 0,
            0, 3,
            0, 2,
            0, -5,
            1.5);
    public static Skill Bladed_Din = new Skill("Bladed Din",
            10,
            9,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            12, 0,
            0, 0,
            6, 0,
            0, 0,
            6.5);
    public static Skill Brainwashing_Axe = new Skill("Brainwashing Axe",
            21,
            14,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Confuse,
            0, 0.0, 0.0,
            10, 0,
            0, 0,
            0, 4,
            0, 4,
            4.5);
    public static Skill Burial_Swipe = new Skill("Burial Swipe",
            16,
            9,
            Battle.Element.Earth,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            6, 0,
            0, 2,
            0, 3,
            0, 1,
            3.5);

    public static Skill Frozen_Miasama = new Skill("Frozen Miasama",
            17,
            12,
            Battle.Element.Water,
            false,
            true,
            Battle.Stats.Normal,
            30, 0.0, 0.0,
            0, 0,
            0, 8,
            0, 0,
            0, 8,
            4.0);
    public static Skill Storm_Gigas = new Skill("Storm Gigas",
            18,
            13,
            Battle.Element.Wind,
            false,
            true,
            Battle.Stats.Normal,
            45, 0.0, 0.0,
            0, 0,
            0, 1,
            0, 0,
            0, 1,
            1.0);
    public static Skill Mud_Hand = new Skill("Mud Hand",
            16,
            12,
            Battle.Element.Earth,
            false,
            false,
            Battle.Stats.Normal,
            55, 0.0, 0.0,
            0, 0,
            0, 1,
            0, 0,
            0, 1,
            1.0);
    public static Skill Silver_Flame = new Skill("Silver Flame",
            18,
            14,
            Battle.Element.Fire,
            false,
            false,
            Battle.Stats.Normal,
            60, 0.0, 0.0,
            0, 0,
            0, 1,
            0, 0,
            0, 1,
            1.0);

    public static Skill Crystal_Whirl = new Skill("Crystal Whirl",
            25,
            18,
            Battle.Element.Water,
            false,
            false,
            Battle.Stats.Normal,
            80, 0.0, 0.0,
            0, 0,
            0, 1,
            0, 0,
            0, 1,
            1.0);
    public static Skill Thunderstorm = new Skill("Thunderstorm",
            24,
            19,
            Battle.Element.Wind,
            false,
            false,
            Battle.Stats.Normal,
            90, 0.0, 0.0,
            0, 0,
            0, 1,
            0, 0,
            0, 1,
            1.0);
    public static Skill Terra_Wall = new Skill("Terra Wall",
            26,
            20,
            Battle.Element.Earth,
            false,
            true,
            Battle.Stats.Normal,
            67, 0.0, 0.0,
            0, 0,
            0, 6,
            0, 3,
            0, 3,
            6.0);
    public static Skill Inferno = new Skill("Inferno",
            27,
            17,
            Battle.Element.Fire,
            false,
            true,
            Battle.Stats.Normal,
            120, 0.0, 0.0,
            0, 0,
            0, 1,
            0, 0,
            0, 1,
            1.0);

    public static Skill Sleep = new Skill("Sleep",
            4,
            3,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Sleep,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Venomnater = new Skill("Venomater",
            7,
            4,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Poison,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Hallucination = new Skill("Hallucination",
            13,
            9,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Confuse,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            2.0);
    public static Skill Silence = new Skill("Silence",
            12,
            18,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Silence,
            0, 0.0, 0.0,
            0, 0,
            0, 3,
            0, 0,
            0, 2,
            2.0);
    public static Skill Petrify = new Skill("Petrify",
            17,
            20,
            Battle.Element.Earth,
            false,
            false,
            Battle.Stats.Petrify,
            0, 0.0, 0.0,
            0, 0,
            0, 3,
            0, 0,
            0, 2,
            2.0);
    public static Skill Veather_Charm = new Skill("Veather Charm",
            1,
            10,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Fog,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);

    public static Skill Hypnosis_Echo = new Skill("Hypnosis Echo",
            10,
            10,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Sleep,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Toxic_River = new Skill("Toxic River",
            11,
            13,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Poison,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Black_Miasama = new Skill("Black Miasama",
            16,
            13,
            Battle.Element.Dark,
            false,
            true,
            Battle.Stats.Fog,
            0, 0.0, 0.0,
            0, 0,
            0, 3,
            0, 0,
            0, 2,
            2.0);
    public static Skill White_Curse = new Skill("White Curse",
            20,
            25,
            Battle.Element.Light,
            false,
            true,
            Battle.Stats.Silence,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Mass_Hallucination = new Skill("Mass Hallucination",
            21,
            26,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Confuse,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Fossilize = new Skill("Fossilize",
            28,
            40,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Petrify,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);

    public static Skill HealMore = new Skill("HealMore",
            14,
            7,
            Battle.Element.Heal,
            true,
            false,
            Battle.Stats.Normal,
            -70, 0.0, 0.0,
            0, 0,
            0, -6,
            0, 0,
            0, 6,
            6.0);
    public static Skill HealAll = new Skill("HealAll",
            23,
            12,
            Battle.Element.Heal,
            true,
            false,
            Battle.Stats.Normal,
            -999, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill HealUs = new Skill("HealUs",
            38,
            15,
            Battle.Element.Heal,
            true,
            true,
            Battle.Stats.Normal,
            -100, 0.0, 0.0,
            0, 0,
            0, -6,
            0, 0,
            0, 6,
            6.0);
    public static Skill Antidote = new Skill("Antidote",
            6,
            5,
            Battle.Element.Heal,
            true,
            false,
            Battle.Stats.Poison,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Awaken = new Skill("Awaken",
            12,
            7,
            Battle.Element.Heal,
            true,
            true,
            Battle.Stats.Sleep,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Defog = new Skill("Defog",
            11,
            11,
            Battle.Element.Heal,
            true,
            true,
            Battle.Stats.Fog,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Honey_Voice = new Skill("Honey Voice",
            15,
            9,
            Battle.Element.Heal,
            true,
            false,
            Battle.Stats.Silence,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Diffuse = new Skill("Diffuse",
            17,
            12,
            Battle.Element.Heal,
            true,
            true,
            Battle.Stats.Confuse,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Cell_Rejuvination = new Skill("Cell Rejuvination",
            21,
            15,
            Battle.Element.Heal,
            true,
            false,
            Battle.Stats.Petrify,
            0, 0.0, 0.0,
            0, 0,
            0, 0,
            0, 0,
            0, 0,
            1.0);
    public static Skill Revivify = new Skill("Revivify",
            38,
            20,
            Battle.Element.Heal,
            true,
            false,
            Battle.Stats.Death,
            -30, 0.3, 0.0,
            0, 0,
            0, -6,
            0, 0,
            0, 6,
            6.0);

    public static Skill Seal_Breaker = new Skill("Seal Breaker",
            36,
            23,
            Battle.Element.Physical,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            10, 0,
            0, -4,
            0, 7,
            0, -8,
            6.0);
    public static Skill Reign_of_Terror = new Skill("Reign of Terror",
            36,
            21,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            13, 0,
            0, 0,
            0, 7,
            0, 0,
            5.5);
    public static Skill Weapon_Master = new Skill("Weapon Master",
            35,
            7,
            Battle.Element.Physical,
            false,
            true,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            6, 0,
            6, 0,
            0, 3,
            3, 0,
            5.0);
    public static Skill Corruption_Force_Wave = new Skill(
            "Corruption Force Wave",
            35,
            7,
            Battle.Element.Dark,
            false,
            true,
            Battle.Stats.Normal,
            0, 0.35, 0.0,
            0, 0,
            0, 13,
            0, 0,
            0, 6,
            8.0);
    public static Skill Paladins_Fury = new Skill("Paladin's Fury",
            37,
            25,
            Battle.Element.Light,
            false,
            false,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            7, 0,
            0, 7,
            0, 3,
            0, 4,
            7.0);
    public static Skill Axe_Effect = new Skill("Axe Effect",
            37,
            25,
            Battle.Element.Earth,
            false,
            true,
            Battle.Stats.Fog,
            0, 0.0, 0.0,
            7, 0,
            0, -7,
            0, 3,
            0, -3,
            5.0);
    public static Skill Fairy_Circle = new Skill("Fairy Circle",
            39,
            25,
            Battle.Element.Light,
            false,
            true,
            Battle.Stats.Normal,
            0, 0.0, 0.0,
            7, 0,
            7, 7,
            0, 3,
            3, 4,
            5.5);
    public static Skill Darknight = new Skill("Darknight",
            37,
            30,
            Battle.Element.Dark,
            false,
            false,
            Battle.Stats.Normal,
            70, 0.0, 0.0,
            0, 0,
            0, 16,
            0, 0,
            0, 8,
            8.0);

    public static Skill[] Magic_Knight_Moveset = {CrossCall, Heal, Darth_Fireball, Feint_Sword_Tackle, Frozen_Miasama, Tornado_Spin, Terra_Wall, Sword_Dancer, Paladins_Fury, Corruption_Force_Wave};
    public static Skill[] Warrior_Moveset = {Red_Spin, Red_Flail, Feint_Sword_Tackle, Red_Slayer, Seal_Breaker};
    public static Skill[] Lancer_Moveset = {Leg_Sweep, Tiger_Thrust, Defense_Rush, Thunder_Thrust, Reign_of_Terror};
    public static Skill[] Weapon_Master_Moveset = {Poison_Edge, Ray_Assassin, Tiger_Thrust, Feint_Sword_Tackle, Weapon_Master};
    public static Skill[] Axeman_Moveset = {Veather_Sprawl, Veather_Charm, Bladed_Din, Burial_Swipe, Brainwashing_Axe, Black_Miasama, Axe_Effect};
    public static Skill[] Mage_Moveset = {Heal, Antidote, Awaken, Defog, Honey_Voice, Diffuse, Cell_Rejuvination, Storm_Gigas, Mud_Hand, Silver_Flame, Thunderstorm, Terra_Wall, Inferno, Crystal_Whirl, HealMore, HealAll, HealUs, Darknight};
    public static Skill[] Angel_Moveset = {CrossCall, Storm_Gigas, Sword_Dancer, HealMore, HealAll, HealUs, Honey_Voice, Awaken, Revivify, White_Curse, Cell_Rejuvination, Fairy_Circle};
    public static Skill[] Water_Spirit_Moveset = {CrossCall, Heal, Frozen_Miasama, Feint_Sword_Tackle, Crystal_Whirl, Sword_Dancer, HealMore, HealUs, Revivify};

    public static Equipment Wooden_Sword = new Equipment("Wooden Sword",
            Equipment.EquipmentType.RightArm,
            null,
            false,
            2, 0, 0, 0);
    public static Equipment Wooden_Staff = new Equipment("Wooden Staff",
            Equipment.EquipmentType.RightArm,
            null,
            false,
            2, 0, 0, 0);
    public static Equipment Bronze_Sword = new Equipment("Bronze Sword",
            Equipment.EquipmentType.RightArm,
            null,
            false,
            6, 0, 0, 0);
    public static Equipment Hand_Axe = new Equipment("Hand_Axe",
            Equipment.EquipmentType.RightArm,
            null,
            false,
            8, 0, 0, 0);
    public static Equipment Bronze_Spear = new Equipment("Bronze Spear",
            Equipment.EquipmentType.RightArm,
            null,
            false,
            7, 0, 0, 0);
    public static Equipment Blue_Javelin = new Equipment("Blue Javelin",
            Equipment.EquipmentType.RightArm,
            Thunder_Thrust,
            Battle.Element.Water,
            true,
            14, 0, 0, 0);
    public static Equipment Red_Sword = new Equipment("Red Sword",
            Equipment.EquipmentType.RightArm,
            Red_Slayer,
            Battle.Element.Fire,
            false,
            17, 0, 0, 0);
    public static Equipment Golden_Axe = new Equipment("Golden Axe",
            Equipment.EquipmentType.RightArm,
            Burial_Swipe,
            Battle.Element.Earth,
            false,
            21, 0, 0, 0);
    public static Equipment Violet_Staff = new Equipment("Violet Staff",
            Equipment.EquipmentType.RightArm,
            Storm_Gigas,
            Battle.Element.Wind,
            false,
            2, 0, 20, 0);
    public static Equipment Dark_Saber = new Equipment("Dark Saber",
            Equipment.EquipmentType.RightArm,
            null,
            Battle.Element.Dark,
            false,
            31, 0, 0, 5);
    public static Equipment White_Sword = new Equipment("White Sword",
            Equipment.EquipmentType.RightArm,
            null,
            Battle.Element.Light,
            false,
            15, 0, 0, 15);

    public static Equipment Headband = new Equipment("Headband",
            Equipment.EquipmentType.Head,
            null,
            false,
            0, 1, 1, 0);
    public static Equipment Bronze_Helmet = new Equipment("Bronze Helmet",
            Equipment.EquipmentType.Head,
            null,
            false,
            0, 3, 0, 0);
    public static Equipment Iron_Helmet = new Equipment("Iron Helmet",
            Equipment.EquipmentType.Head,
            null,
            false,
            0, 6, 0, 0);
    public static Equipment Red_Hood = new Equipment("Red Hood",
            Equipment.EquipmentType.Head,
            null,
            false,
            0, 7, 2, 2);
    public static Equipment Roof_Cap = new Equipment("Roof Cap",
            Equipment.EquipmentType.Head,
            null,
            false,
            1, 9, 0, 0);
    public static Equipment Silver_Ribbon = new Equipment("Silver Ribbon",
            Equipment.EquipmentType.Head,
            null,
            false,
            0, 12, 1, 2);
    public static Equipment Hero_Circlet = new Equipment("Hero Circlet",
            Equipment.EquipmentType.Head,
            null,
            false,
            0, 15, 1, 5);

    public static Equipment Wooden_Shield = new Equipment("Wooden Shield",
            Equipment.EquipmentType.LeftArm,
            null,
            false,
            0, 1, 0, 0);
    public static Equipment Bronze_Shield = new Equipment("Bronze Shield",
            Equipment.EquipmentType.LeftArm,
            null,
            false,
            0, 3, 0, 0);
    public static Equipment Iron_Shield = new Equipment("Iron Shield",
            Equipment.EquipmentType.LeftArm,
            null,
            false,
            0, 5, 0, 0);
    public static Equipment Crest_Shield = new Equipment("Crest Shield",
            Equipment.EquipmentType.LeftArm,
            null,
            false,
            0, 7, 0, 3);
    public static Equipment Grooved_Gauntlet = new Equipment("Grooved Gauntlet",
            Equipment.EquipmentType.LeftArm,
            null,
            false,
            0, 8, 2, 0);

    public static Equipment Tunic = new Equipment("Tunic",
            Equipment.EquipmentType.Body,
            null,
            false,
            0, 1, 0, 0);
    public static Equipment Bronze_Armor = new Equipment("Bronze Armor",
            Equipment.EquipmentType.Body,
            null,
            false,
            0, 4, 0, 0);
    public static Equipment Iron_Armor = new Equipment("Iron Armor",
            Equipment.EquipmentType.Body,
            null,
            false,
            0, 8, 0, 0);
    public static Equipment Leather_Robe = new Equipment("Leather Robe",
            Equipment.EquipmentType.Body,
            null,
            false,
            0, 5, 2, 0);
    public static Equipment Metal_Cloak = new Equipment("Metal Cloak",
            Equipment.EquipmentType.Body,
            null,
            false,
            0, 12, 1, 0);
    public static Equipment Knight_Armor = new Equipment("Knight Armor",
            Equipment.EquipmentType.Body,
            null,
            false,
            0, 18, 0, 0);
    public static Equipment Queens_Robe = new Equipment("Queen's Robe",
            Equipment.EquipmentType.Body,
            null,
            false,
            0, 24, 2, 10);

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
    public static BattlerClass Custom_Class;

    public static Battler Darth = new Battler("Darth",
            Battle.Element.Physical,
            Battler.Gender.Male,
            1,
            20, 20,
            5, 4, 5, 6,
            Magic_Knight_Class,
            MagicKnight);
    public static Battler Erik = new Battler("Erik",
            Battle.Element.Physical,
            Battler.Gender.Male,
            1,
            28, 12,
            7, 5, 6, 2,
            Swordsman_Class,
            Warrior);
    public static Battler Protox = new Battler("Protox",
            Battle.Element.Physical,
            Battler.Gender.Male,
            1,
            32, 8,
            8, 7, 1, 4,
            Lancer_Class,
            Lancer);
    public static Battler Gladia = new Battler("???",
            Battle.Element.Physical,
            Battler.Gender.Female,
            5,
            88, 34,
            20, 14, 16, 9,
            Weapon_Master_Class,
            Master);
    public static Battler Veather = new Battler("Veather Mann",
            Battle.Element.Physical,
            Battler.Gender.Male,
            6,
            60, 21,
            32, 18, 21, 10,
            Axeman_Class,
            Heavy_Warrior);
    public static Battler Magia = new Battler("Magia",
            Battle.Element.Physical,
            Battler.Gender.Female,
            8,
            61, 60,
            10, 21, 40, 61,
            Mage_Class,
            Lancer);
    public static Battler Nairarum = new Battler("Nairarum",
            Battle.Element.Light,
            Battler.Gender.Female,
            37,
            777, 77,
            127, 107, 114, 127,
            Angel_Class,
            Angel);
    public static Battler Fire_Spirit = new Battler("???",
            Battle.Element.Fire,
            Battler.Gender.Female,
            1,
            32, 15,
            7, 5, 5, 5,
            Water_Spirit_Moveset,
            Water_Spirit_Equipment);

    public static Battler Water_Spirit = new Battler("???",
            Battle.Element.Water,
            Battler.Gender.Female,
            1,
            30, 30,
            6, 4, 7, 5,
            Water_Spirit_Moveset,
            Water_Spirit_Equipment);
    public static Battler Wind_Spirit = new Battler("???",
            Battle.Element.Wind,
            Battler.Gender.Female,
            1,
            20, 35,
            5, 4, 5, 7,
            Water_Spirit_Moveset,
            Water_Spirit_Equipment);
    public static Battler Earth_Spirit = new Battler("???",
            Battle.Element.Earth,
            Battler.Gender.Female,
            1,
            40, 10,
            5, 7, 6, 4,
            Water_Spirit_Moveset,
            Water_Spirit_Equipment);
    public static Battler you;
    public static int mapNumber = 0;

    public static BattlerAI Ghost = new BattlerAI("Ghost",
            Battle.Element.Dark,
            Battler.Gender.Male,
            1,
            26, 9,
            7, 4, 4, 4,
            null,
            new Equipment[]{null, null, null, null, null},
            Brainless,
            20,
            Database1.Potion,
            1);
    public static BattlerAI Undead_Darth = new BattlerAI("Darth???",
            Battle.Element.Dark,
            Battler.Gender.Male,
            5,
            250, 34,
            13, 9, 13, 13,
            Database1.Darth.getSkillList(),
            Warrior,
            Standard,
            100,
            Database1.Bronze_Sword,
            1);

    public static BattlerAI Ugly_Demon = new BattlerAI("Ugly Demon",
            Battle.Element.Dark,
            Battler.Gender.Male,
            20,
            450, 40,
            60, 50, 41, 54,
            new Skill[]{Poison_Edge, Storm_Gigas, Burial_Swipe},
            Database1.Warrior,
            Standard,
            0,
            null,
            0);
    public static BattlerAI Kraken = new BattlerAI(
            "Kraken",
            Battle.Element.Water,
            Battler.Gender.Male,
            17,
            710, 61,
            16, 17, 15, 1,
            new Skill[]{Frozen_Miasama, Hallucination},
            Database1.Warrior,
            Standard_Support,
            140,
            null,
            0);

    public static Item Potion = new Item("Potion", true, 35, 0, false);
    public static Item Potion_All = new Item("Potion All", true, 35, 0, true);
    public static Item Charger = new Item("Charger", false, 0, 100, false);
    public static Item Tonic = new Item("Tonic", true, Revivify, false);

    public Database1() {
        
    }
    
    public static ArrayList<Item> inventory = new ArrayList<>();

    public static int karma = 0;//Positive Karma is good, Negative is bad.

    public static HashMap<String, Boolean> switches = new HashMap<>();

    public static HashMap<String, Integer> variables = new HashMap<>();

    public static void save(String fileName) throws FileNotFoundException, IOException {
        FileOutputStream fileStream = new FileOutputStream(fileName);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        objectStream.writeObject(Darth);
        objectStream.writeObject(Erik);
        objectStream.writeObject(Protox);
        objectStream.writeObject(Gladia);
        objectStream.writeObject(Veather);
        objectStream.writeObject(Magia);
        objectStream.writeObject(Nairarum);
        objectStream.writeObject(Fire_Spirit);
        objectStream.writeObject(Water_Spirit);
        objectStream.writeObject(Wind_Spirit);
        objectStream.writeObject(Earth_Spirit);
        objectStream.writeObject(you);
        objectStream.writeInt(mapNumber);
        objectStream.writeObject(inventory);
        objectStream.writeInt(karma);
        objectStream.writeObject(switches);
        objectStream.writeObject(variables);

        objectStream.close();
    }

    public static void load(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream(fileName);
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);

        Database1.Darth = (Battler) objectStream.readObject();
        Database1.Erik = (Battler) objectStream.readObject();
        Database1.Protox = (Battler) objectStream.readObject();
        Database1.Gladia = (Battler) objectStream.readObject();
        Database1.Veather = (Battler) objectStream.readObject();
        Database1.Magia = (Battler) objectStream.readObject();
        Database1.Nairarum = (Battler) objectStream.readObject();
        Database1.Fire_Spirit = (Battler) objectStream.readObject();
        Database1.Water_Spirit = (Battler) objectStream.readObject();
        Database1.Wind_Spirit = (Battler) objectStream.readObject();
        Database1.Earth_Spirit = (Battler) objectStream.readObject();
        Database1.you = (Battler) objectStream.readObject();
        Database1.mapNumber = objectStream.readInt();
        Database1.inventory = (ArrayList<Item>) objectStream.readObject();
        Database1.karma = objectStream.readInt();
        Database1.switches = (HashMap<String, Boolean>) objectStream.
                readObject();
        Database1.variables = (HashMap<String, Integer>) objectStream.
                readObject();
        objectStream.close();
    }

}
