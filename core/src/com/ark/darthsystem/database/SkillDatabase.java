/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.Skill;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class SkillDatabase {
    public static final HashMap<String, Skill> SKILL_LIST = new HashMap<>();
    
    public SkillDatabase() {
        FileHandle file = Gdx.files.internal("databases/skills.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;
            SKILL_LIST.put(data[i], new Skill(data[i], //Name
                    data[++i], //Description
                    Integer.parseInt(data[++i]), //Cost
                    Battle.Element.valueOf(data[++i]), //Element
                    Boolean.getBoolean(data[++i].toLowerCase()), //Targets ally?
                    Boolean.getBoolean(data[++i].toLowerCase()), //Targets all?
                    Battle.Stats.valueOf(data[++i]), //Inflicts stats?
                    Integer.parseInt(data[++i]), //Base Damage
                    Double.parseDouble(data[++i]), //Level Difference Multiplier
                    Double.parseDouble(data[++i]), //HP %
                    Integer.parseInt(data[++i]), //Attack Multiplier
                    Integer.parseInt(data[++i]), //Defense Multiplier
                    Integer.parseInt(data[++i]), //Speed Multiplier
                    Integer.parseInt(data[++i]), //Magic Multiplier
                    Integer.parseInt(data[++i]), //Enemy Attack Multiplier
                    Integer.parseInt(data[++i]), //Enemy Defense Multiplier
                    Integer.parseInt(data[++i]), //Enemy Speed Multiplier
                    Integer.parseInt(data[++i]), //Enemy Magic Multiplier
                    Double.parseDouble(data[++i]))); //Divider
        }
    }
    public static Skill CrossCall = new Skill("CrossCall",
            "Physical Attack",
            5,
            Battle.Element.Light,
            false, //Targets ally?
            false, //Targets all?
            Battle.Stats.Normal, //Inflict Status?
            0, 0.0, 0.0, //Base/Level Difference/HP%
            6, 0,
            0, 6,
            0, 3,
            0, 3,
            5.5);
    public static Skill WitchesCross = new Skill("Witches' Cross",
            "",
            6,
            Battle.Element.Dark,
            false, //Targets ally?
            false, //Targets all?
            Battle.Stats.Normal, //Inflict Status?
            6, 6.6, 0.0, //Base/Level Difference %/HP%
            6, 6,
            6, 0,
            5, 5,
            5, 0,
            6.66);
    public static Skill Red_Spin = new Skill("Red Spin",
            "",
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
            "",
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
            "",
            7,
            Battle.Element.Dark,
            false,
            false,
            Battle.Stats.Poison,
            0, 0.0, 0.0,
            10, 0,
            0, 0,
            0, 8,
            0, 0,
            6.0);

    public static Skill Sap_Shot = new Skill("Sap Shot",
            "",
            4,
            Battle.Element.Water,
            false,
            false,
            Battle.Stats.Normal,
            10, 0.0, 0.0,
            0, 0,
            0, 6,
            0, 3,
            0, 3,
            5.0);
    
    public static Skill Heal = new Skill("Heal",
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
            "",
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
    public static Skill Entropy = new Skill("Entropy",
            "",
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
    public static Skill Fours_Fury = new Skill("Four's Fury",
            "",
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
            "",
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
            "",
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
            "",
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
}
