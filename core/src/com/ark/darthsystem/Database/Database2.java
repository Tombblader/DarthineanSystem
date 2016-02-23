/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.States.OverheadMap;
import com.ark.darthsystem.*;
import com.ark.darthsystem.Graphics.*;
import com.ark.darthsystem.States.chapters.*;
import com.ark.darthsystem.States.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author trankt1
 */
public class Database2 extends com.ark.darthsystem.Database.Database1 {

    public static Player player;
//    private static ActorAI ProtoxAI;
    public static ActorAI ErikAI;
    public static ArrayList<Event> events = new ArrayList<>();

    public Database2() {
        super();
        Database2.SkillToActor = new HashMap<Skill, ActorSkill>() {
            {
                put(CrossCall,
                        new ActorSkill((Sprite[]) GraphicsDriver.
                                getMasterSheet().
                                createSprites(
                                        "animations/wiccan_cross/wiccan_cross").
                                toArray(Sprite.class),
                                (Sprite[]) GraphicsDriver.getMasterSheet().
                                        createSprites("animations/crosscall/crosscall").toArray(Sprite.class),
                                1,
                                1,
                                2.0f / 60.0f,
                                Database1.CrossCall,
                                ActorSkill.Area.FRONT));
                put(Red_Spin,
                        new ActorSkill((Sprite[]) GraphicsDriver.
                                getMasterSheet().
                                createSprites("animations/red_spin/red_spin").
                                toArray(Sprite.class),
                                (Sprite[]) GraphicsDriver.getMasterSheet().
                                        createSprites("animations/crosscall/crosscall").toArray(Sprite.class),
                                1,
                                1,
                                5.0f / 60.0f,
                                Database1.Red_Spin,
                                ActorSkill.Area.SELF));
//                put(Red_Spin, new ActorSkill(new Sprite("animations/wiccan_cross/red_spin", false, false), 1, 1, 17, Database1.Red_Spin, ActorSkill.Area.SELF));
//                put(Leg_Sweep, new ActorSkill(new Sprite("com.ark.darthsystem/GraphicsPack/assets/WiccanCross.png", false).getImages(), 1, 0, 17, Database1.Leg_Sweep));
//                put(Heal, new ActorSkill(new Sprite("com.ark.darthsystem/GraphicsPack/assets/WiccanCross.png", false).getImages(), 0, 0, 30, Database1.Heal, ActorSkill.Area.SELF));
            }
            private static final long serialVersionUID = 1L;

        };

        Database2.ErikSprite = new ActorSprite("characters/darth_invader");
        Database2.DarthSprite = new ActorSprite("characters/darth_invader");
//        Sprite tempPotionSprite = new Sprite("com.ark.darthsystem/GraphicsPack/assets/potion.png", false, true);
        Database2.enemies = new ActorBattler[]{new ActorBattler((new BattlerAI(
            "Erik the Red",
            Erik,
            Scenario.Standard,
            50,
            null,
            0)),
            ErikSprite)};
//        Database2.ProtoxAI = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler(new BattlerAI("Protox Toxorp", Database1.Protox, Scenario.Standard, 50, null, 0), DarthSprite)})), 500, 400);
        BattlerAI temp = new BattlerAI(
            "Erik the Red",
            Erik,
            Scenario.Standard,
            50,
            null,
            0);

        ArrayList<ActorBattler> ErikAIParty = new ArrayList<>();
        ErikAIParty.add(new ActorBattler(temp, ErikSprite));
        ErikAI = new ActorAI(ErikAIParty, 500, 500);
        Database2.battlers = new ActorBattler[]{new ActorBattler(Darth, DarthSprite), new ActorBattler(Erik, ErikSprite)};
        Database2.GraphicsPotion = new Pickup((Sprite[]) GraphicsDriver.getMasterSheet().
                                        createSprites("items/potion").toArray(Sprite.class), 350.0f, 350.0f, .1f, Potion);
        Database2.chapter1 = new NovelMode(new Chapter1(), null, 450.0f, 450.0f, .1f);
        player = new Player(new ArrayList<>(Arrays.asList(battlers)), 0, 0);

        /*        enemySampleMap.add(ProtoxAI);
        enemySampleMap.add(ErikAI);
        enemySampleMap.add(GraphicsPotion);
        

        Database2.SampleMap = new GraphicsMap("com.ark.darthsystem/GraphicsPack/assets/tileset.png", "com.ark.darthsystem/GraphicsPack/Map/Map0.txt", enemySampleMap);
        BattleDriver.addItem(Potion);
        BattleDriver.addItem(Potion);
        BattleDriver.addItem(Potion);
        BattleDriver.addItem(Potion);
        BattleDriver.addItem(Potion);
        BattleDriver.addItem(Tonic);*/
        new Database1Point5();

    }

    public static OverheadMap SampleMap;
    public static HashMap<Skill, ActorSkill> SkillToActor;

    public static ActorSprite DarthSprite;
    public static ActorSprite ErikSprite;

//    public static ActorAI DarthQuestionMark;
    public static ActorBattler[] battlers;
    public static ActorBattler[] enemies;

    public static ArrayList<Actor> enemySampleMap;
    public static Pickup GraphicsPotion;
    public static NovelMode chapter1;

//    public static final TextBox DEFAULT_WINDOW = new TextBox("com.ark.darthsystem/GraphicsPack/assets/Window.png");
//    public static final Sprite DEFAULT_CURSOR = new Sprite("com.ark.darthsystem/GraphicsPack/assets/Cursor.png", false);
    public static final ActorSkill Spear = new ActorSkill(
            (Sprite[]) GraphicsDriver.getMasterSheet().
            createSprites("animations/spear/spear").
            toArray(Sprite.class),
            1,
            1,
            100,
            null);

    public static ActorSkill Sword() {
        return new ActorSkill((Sprite[]) GraphicsDriver.getMasterSheet().
                createSprites("animations/sword/sword").
                toArray(Sprite.class),
                (Sprite[]) GraphicsDriver.getMasterSheet().
                        createSprites("animations/sword_slash/sword_slash").toArray(Sprite.class),
                1,
                1,
                2.0f / 60.0f,
                null,
                ActorSkill.Area.FRONT);
    }

    //  public static GraphicsEvent Pickup;
    //This generates a skill list based on the skills available to the character.  It accepts an input of a battler, then assigns animations based on the index of each skill.
    public static ActorSkill SkillToActor(Skill s) {
        return SkillToActor.get(s);
    }

    public static Input createInputInstance() {
        return new Input();
    }

    public static Input createInputInstance(String instance) {
        return new Input();
    }
}
