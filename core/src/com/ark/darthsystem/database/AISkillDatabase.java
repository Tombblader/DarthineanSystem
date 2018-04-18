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
public class AISkillDatabase {
    public static final HashMap<String, Skill> SKILL_LIST = new HashMap<>();
    
    public AISkillDatabase() {
        FileHandle file = Gdx.files.internal("databases/skillsai.tsv");
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
                    Boolean.getBoolean(data[++i].toLowerCase()),
                    data[++i], //Inflicts stats?
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
}
