/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.ActorSkill;
import com.ark.darthsystem.graphics.Player;
import com.ark.darthsystem.graphics.ActorBattler;
import com.ark.darthsystem.graphics.Input;
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.*;
import static com.ark.darthsystem.database.CharacterDatabase.*;
import com.ark.darthsystem.states.events.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author trankt1
 */
public class Database2 extends Database1 {

    public static Player player;

    public Database2() {
        super();
        Database2.SkillToActor = new HashMap<Skill, ActorSkill>() {
            {
                put(SkillDatabase.SKILL_LIST.get("Crosscall"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                1,
                                1,
                                1.0f / 20.0f,
                                SkillDatabase.SKILL_LIST.get("Crosscall"),
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.SKILL_LIST.get("Red Spin"),
                        new ActorSkill("skills/red_spin/field/red_spin",
                                "skills/red_spin/battler/red_spin",
                                0,
                                0,
                                1.0f / 12.0f,
                                SkillDatabase.SKILL_LIST.get("Red Spin"),
                                ActorSkill.Area.SELF,
                                "widespin"));
                put(SkillDatabase.SKILL_LIST.get("Sap Shot"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                0,
                                0,
                                10f,
                                0,
                                1.0f / 24.0f,
                                1f,
                                SkillDatabase.SKILL_LIST.get("Sap Shot"),
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.SKILL_LIST.get("Fireball"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                0,
                                0,
                                10f,
                                0,
                                1.0f / 24.0f,
                                1f,
                                SkillDatabase.SKILL_LIST.get("Fireball"),
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.SKILL_LIST.get("Fireball"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                0,
                                0,
                                1.0f / 24.0f,
                                1.0f,
                                SkillDatabase.SKILL_LIST.get("Fireball"),
                                ActorSkill.Area.SELF_BENEFIT));
                put(AISkillDatabase.SKILL_LIST.get("Eyebeam"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                1,
                                1,
                                1.0f / 24.0f,
                                .2f,
                                AISkillDatabase.SKILL_LIST.get("Eyebeam"),
                                ActorSkill.Area.FRONT));
//                put(Leg_Sweep, new ActorSkill(new Sprite("com.ark.darthsystem/GraphicsPack/assets/WiccanCross.png", false).getImages(), 1, 0, 17, Database1.Leg_Sweep));
            }
            private static final long serialVersionUID = 1L;

        };
        new SkillDatabase();
        new SpriteDatabase();
        new CharacterDatabase();
        new MonsterDatabase();

        
//        Database2.ProtoxAI = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler(new BattlerAI("Protox Toxorp", Database1.Protox, Scenario.Standard, 50, null, 0), DarthSprite)})), 500, 400);
        ActorBattler[] battlers;

        battlers = new ActorBattler[]{
            Water_Spirit_Battler
        };
        player = new Player(new ArrayList<>(Arrays.asList(battlers)), 0, 0);
    }

    public static HashMap<Skill, ActorSkill> SkillToActor;

    public static ArrayList<Actor> enemySampleMap;
    public static Pickup GraphicsPotion;
    public static NovelMode chapter1;

    public static final ActorSkill Spear = new ActorSkill("items/equipment/spear/field/field",
            1,
            1,
            1f/24f,
            null);

    public static ActorSkill Sword() {
        return new ActorSkill("items/equipment/sword/field/field",
                "items/equipment/sword/battler/battler",
                1,
                1,
                1f/24f,
                null,
                ActorSkill.Area.FRONT,
                "widesword");
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
