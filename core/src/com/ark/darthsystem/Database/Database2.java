/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.*;
import static com.ark.darthsystem.Database.CharacterDatabase.*;
import com.ark.darthsystem.Graphics.*;
import com.ark.darthsystem.States.events.*;

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
    public static ArrayList<Event> events = new ArrayList<>();

    public Database2() {
        super();
        Database2.SkillToActor = new HashMap<Skill, ActorSkill>() {
            {
                put(SkillDatabase.CrossCall,
                        new ActorSkill(GraphicsDriver.getMasterSheet().createSprites("skills/wiccan_cross/field/wiccan_cross").toArray(Sprite.class),
                                (Sprite[]) GraphicsDriver.getMasterSheet().createSprites("skills/crosscall/battler/crosscall").toArray(Sprite.class),
                                1,
                                1,
                                1.0f / 24.0f,
                                SkillDatabase.CrossCall,
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.Red_Spin,
                        new ActorSkill((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("skills/red_spin/field/red_spin").toArray(Sprite.class),
                                GraphicsDriver.getMasterSheet().createSprites("skills/crosscall/battler/crosscall").toArray(Sprite.class),
                                0,
                                0,
                                1.0f / 12.0f,
                                SkillDatabase.Red_Spin,
                                ActorSkill.Area.SELF) {{setShape("widespin");}});
                put(SkillDatabase.Sap_Shot,
                        new ActorSkill((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("skills/wiccan_cross/field/wiccan_cross").toArray(Sprite.class),
                                GraphicsDriver.getMasterSheet().createSprites("skills/crosscall/battler/crosscall").toArray(Sprite.class),
                                0,
                                0,
                                10f,
                                0,
                                1.0f / 24.0f,
                                1f,
                                SkillDatabase.Sap_Shot,
                                ActorSkill.Area.FRONT));
                put(SkillDatabase.Heal,
                        new ActorSkill((Sprite[]) GraphicsDriver.getMasterSheet().createSprites("skills/wiccan_cross/field/wiccan_cross").toArray(Sprite.class),
                                GraphicsDriver.getMasterSheet().createSprites("skills/crosscall/battler/crosscall").toArray(Sprite.class),
                                0,
                                0,
                                1.0f / 24.0f,
                                1.0f,
                                SkillDatabase.Heal,
                                ActorSkill.Area.SELF_BENEFIT));
                
//                put(Leg_Sweep, new ActorSkill(new Sprite("com.ark.darthsystem/GraphicsPack/assets/WiccanCross.png", false).getImages(), 1, 0, 17, Database1.Leg_Sweep));
            }
            private static final long serialVersionUID = 1L;

        };
        
        new SpriteDatabase();
        new CharacterDatabase();
        new MonsterDatabase();

        
//        Database2.enemies = new ActorBattler[];
//        Database2.ProtoxAI = new ActorAI(new ArrayList<>(Arrays.asList(new ActorBattler[]{new ActorBattler(new BattlerAI("Protox Toxorp", Database1.Protox, Scenario.Standard, 50, null, 0), DarthSprite)})), 500, 400);
        ActorBattler[] battlers;

        battlers = new ActorBattler[]{
            Water_Spirit_Battler
        };
        player = new Player(new ArrayList<>(Arrays.asList(battlers)), 0, 0);
    }

    public static HashMap<Skill, ActorSkill> SkillToActor;

    
//    public static ActorAI DarthQuestionMark;

    public static ArrayList<Actor> enemySampleMap;
    public static Pickup GraphicsPotion;
    public static NovelMode chapter1;

    public static final ActorSkill Spear = new ActorSkill(
            GraphicsDriver.getMasterSheet().
            createSprites("items/equipment/spear/field/field").toArray(Sprite.class),
            1,
            1,
            1f/24f,
            null);

    public static ActorSkill Sword() {
        return new ActorSkill(GraphicsDriver.getMasterSheet().createSprites("items/equipment/sword/field/field").toArray(Sprite.class),
                GraphicsDriver.getMasterSheet().createSprites("items/equipment/sword/battler/battler").toArray(Sprite.class),
                1,
                1,
                1f/24f,
                null,
                ActorSkill.Area.FRONT) {{setShape("widesword");}};
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
