/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.graphics.FieldSkill;
import com.ark.darthsystem.graphics.Player;
import com.ark.darthsystem.graphics.FieldBattler;
import com.ark.darthsystem.graphics.Input;
import com.ark.darthsystem.graphics.Actor;
import static com.ark.darthsystem.database.CharacterDatabase.*;
import static com.ark.darthsystem.database.Database1.BATTLER_LIST;
import static com.ark.darthsystem.database.Database1.inventory;
import static com.ark.darthsystem.database.Database1.karma;
import static com.ark.darthsystem.database.Database1.money;
import static com.ark.darthsystem.database.Database1.switches;
import static com.ark.darthsystem.database.Database1.variables;
import com.ark.darthsystem.states.events.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

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
        new SoundDatabase();
        new SkillDatabase();
        new SystemDatabase();
        new ItemDatabase();
        new AIDatabase();
        if (!load.equals("")) {
            try {
//                new Database1();
                new Database1();
                load(load);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                newGame();
            }
        } else {
            newGame();
        }
        new MonsterDatabase();

    }

    public static void newGame() {
        new Database1();
        new CharacterDatabase();
        FieldBattler[] battlers;
        battlers = new FieldBattler[]{
            CHARACTER_LIST.get("BLUE LADY")
        };
        CHARACTER_LIST.get("BLUE LADY").getBattler().equip(ItemDatabase.EQUIPMENT_LIST.get("BRONZE SWORD"));

        player = new Player(new ArrayList<>(Arrays.asList(battlers)), 0, 0);

    }

    public static void load(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream objectStream
                = new ObjectInputStream(Gdx.files.local(fileName).read(2*2*2*2*2*2*2*2*2*2*2*2*2))) {
            Database1.BATTLER_LIST.clear();
            Database1.BATTLER_LIST.putAll((HashMap<String, Battler>) objectStream.readObject());
            Database1.inventory = (ArrayList<Item>) objectStream.readObject();
            Database1.karma = objectStream.readInt();
            Database1.money = objectStream.readInt();
            Database1.switches = (HashMap<String, Boolean>) objectStream.readObject();
            Database1.variables = (HashMap<String, String>) objectStream.readObject();
            new CharacterDatabase();
            player = (Player) objectStream.readObject();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void save(String fileName) throws FileNotFoundException, IOException {
//        Database1.save(fileName);
        try (ObjectOutputStream objectStream
                = new ObjectOutputStream(Gdx.files.local(fileName).write(false, 2*2*2*2*2*2*2*2*2*2*2*2*2))) {
            objectStream.writeObject(BATTLER_LIST);
            objectStream.writeObject(inventory);
            objectStream.writeInt(karma);
            objectStream.writeInt(money);
            objectStream.writeObject(switches);
            objectStream.writeObject(variables);
            objectStream.writeObject(player);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Database2() {
        this("");
    }

    public void initialize() {

    }

//    public static HashMap<Skill, FieldSkill> SkillToActor;
    public static ArrayList<Actor> enemySampleMap;
    public static Pickup GraphicsPotion;
    public static NovelMode chapter1;


    public static FieldSkill Sword() {
        return new FieldSkill("items/equipment/sword/field/field",
                1,
                1,
                1f / 24f,
                null,
                FieldSkill.Area.FRONT,
                "widesword");
    }

    public static FieldSkill getDefaultUnarmedAnimation() {
        return new FieldSkill("items/equipment/sword/field/field",
                1,
                1,
                1f / 24f,
                null,
                FieldSkill.Area.FRONT,
                "widesword");
    }

    public static Actor getDefaultBattlerUnarmedAnimation() {
        Actor a = new Actor("items/equipment/sword/battler/battler",
                0,
                0,
                1f / 24f,
                true);
        for (Sprite s : a.getCurrentAnimation().getKeyFrames()) {
            s.setOriginCenter();
        }
        return a;
    }

    //This generates a skill list based on the skills available to the character.  It accepts an input of a battler, then assigns animations based on the index of each skill.
    public static FieldSkill SkillToActor(String s) {
        return SkillDatabase.FIELD_SKILL_LIST.get(s.toUpperCase());
    }

    public static Input createInputInstance() {
        return new Input();
    }

    public static Input createInputInstance(String instance) {
        return new Input();
    }
}
