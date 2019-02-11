package com.ark.darthsystem.database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerAI;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.Scenario;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class AIDatabase {
    
    public static final HashMap<String, Battler> BATTLER_LIST = new HashMap<>();
    
    public AIDatabase() {
        FileHandle file = Gdx.files.internal("databases/battlerais.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equalsIgnoreCase("Name")) {
                continue;
            }
            int i = 0;
            
            BATTLER_LIST.put(data[i].toUpperCase(), new BattlerAI(data[i], //Name
                    data[++i], //Description
                    Battle.Element.valueOf(data[++i]), //Element
                    Battler.Gender.valueOf(data[++i]), //Gender
                    Integer.parseInt(data[++i]), //Level
                    Integer.parseInt(data[++i]), //HP
                    Integer.parseInt(data[++i]), //MP
                    Integer.parseInt(data[++i]), //Attack
                    Integer.parseInt(data[++i]), //Defense
                    Integer.parseInt(data[++i]), //Speed
                    Integer.parseInt(data[++i]),  //Magic
                    new ArrayList<>(Arrays.asList(Arrays.stream(data[++i].split(", ")).map(j -> SkillDatabase.SKILL_LIST.get(j.toUpperCase())).toArray(Skill[] :: new))),
                    new EnumMap<>(Equipment.Slot.class),
                    Scenario.AI_TYPE.get(data[++i].toUpperCase()),
                    Integer.parseInt(data[++i]), // XP
                    Integer.parseInt(data[++i]), // Money
                    Arrays.stream(data[++i].split(", ")).map(j -> ItemDatabase.ITEM_LIST.get(j.toUpperCase())).toArray(Item[] :: new),
                    Arrays.stream(data[++i].split(", ")).mapToDouble(j -> Double.parseDouble(j)).toArray(),
                    Arrays.stream(data[++i].split(", ")).mapToInt(j -> Integer.parseInt(j)).toArray()
                    ));
        }
    }
}
