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
import com.badlogic.gdx.Gdx;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author trankt1
 */
public class Database2 {

    public static Player player;
//    public static HashMap<Skill, ActorSkill> SkillToActor;

    public Database2(String load) {
        new SkillDatabase();  
        SkillToActor = new HashMap<>();
//        for (Skill skills : SkillDatabase.SKILL_LIST.values()) {
//            
//            put(skills, new ActorSkill("skills/" + ""))
//        }
        Database2.SkillToActor = new HashMap<Skill, ActorSkill>() {
            {
                put(SkillDatabase.SKILL_LIST.get("CROSSCALL"),
                        new ActorSkill("skills/crosscall/field/crosscall",
                                "skills/crosscall/battler/crosscall",
                                1,
                                1,
                                1.0f / 24.0f,
                                SkillDatabase.SKILL_LIST.get("CROSSCALL"),
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.SKILL_LIST.get("RED SPIN"),
                        new ActorSkill("skills/red_spin/field/red_spin",
                                "skills/red_spin/battler/red_spin",
                                0,
                                0,
                                1.0f / 12.0f,
                                SkillDatabase.SKILL_LIST.get("RED SPIN"),
                                ActorSkill.Area.SELF,
                                "widespin"));
                put(SkillDatabase.SKILL_LIST.get("SAP SHOT"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                0,
                                0,
                                10f,
                                0,
                                1.0f / 24.0f,
                                1f,
                                SkillDatabase.SKILL_LIST.get("SAP SHOT"),
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.SKILL_LIST.get("FIREBALL"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                0,
                                0,
                                10f,
                                0,
                                1.0f / 24.0f,
                                1f,
                                SkillDatabase.SKILL_LIST.get("FIREBALL"),
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.SKILL_LIST.get("HEAL"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                0,
                                0,
                                1.0f / 24.0f,
                                1.0f,
                                SkillDatabase.SKILL_LIST.get("HEAL"),
                                ActorSkill.Area.SELF_BENEFIT));
                put(SkillDatabase.SKILL_LIST.get("EYEBEAM"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                1,
                                1,
                                1.0f / 24.0f,
                                .2f,
                                SkillDatabase.SKILL_LIST.get("EYEBEAM"),
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.SKILL_LIST.get("MOUTHBEAM"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                1,
                                1,
                                1.0f / 24.0f,
                                .2f,
                                SkillDatabase.SKILL_LIST.get("MOUTHBEAM"),
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.SKILL_LIST.get("ATTACK BOOST"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                1,
                                1,
                                1.0f / 24.0f,
                                .2f,
                                SkillDatabase.SKILL_LIST.get("ATTACK BOOST"),
                                ActorSkill.Area.SELF));
                put(SkillDatabase.SKILL_LIST.get("BARRIER"),
                        new ActorSkill("skills/wiccan_cross/field/wiccan_cross",
                                "skills/crosscall/battler/crosscall",
                                1,
                                1,
                                1.0f / 24.0f,
                                .2f,
                                SkillDatabase.SKILL_LIST.get("BARRIER"),
                                ActorSkill.Area.SELF));
//                put(Leg_Sweep, new ActorSkill(new Sprite("com.ark.darthsystem/GraphicsPack/assets/WiccanCross.png", false).getImages(), 1, 0, 17, Database1.Leg_Sweep));
            }
            private static final long serialVersionUID = 1L;

        };
        new SpriteDatabase();
        new SystemDatabase();
        new ItemDatabase();
        new AIDatabase();
        if (!load.equals("")) {
            try {
                Database1.load(load);
                Database2.load(load);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                new Database1();
                new CharacterDatabase();
                ActorBattler[] battlers;
                battlers = new ActorBattler[]{
                    Water_Spirit_Battler
                };
                player = new Player(new ArrayList<>(Arrays.asList(battlers)), 0, 0);
            }
        } else {
            new Database1();
            new CharacterDatabase();
            ActorBattler[] battlers;
            battlers = new ActorBattler[]{
                Water_Spirit_Battler
            };
            player = new Player(new ArrayList<>(Arrays.asList(battlers)), 0, 0);
        }
        new CharacterDatabase();
        new MonsterDatabase();
        
    }
    
    public static void load(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream objectStream
                = new ObjectInputStream(Gdx.files.local(fileName).read())) {
            player = (Player) objectStream.readObject();
        }        
    }
    
    public Database2() {
        this("");
    }
    
    public void initialize() {
        
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

    public static ActorSkill getDefaultUnarmedAnimation() {
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
