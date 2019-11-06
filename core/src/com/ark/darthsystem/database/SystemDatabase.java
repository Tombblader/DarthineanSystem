/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.BattlerClass;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.states.Battle;
import static com.ark.darthsystem.database.SkillDatabase.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class SystemDatabase {

    public static final HashMap<Battle.Element, Double> WEAKNESS = new HashMap<>();
    public static final HashMap<String, BattlerClass> CLASS_LIST = new HashMap<>();

    public SystemDatabase() {
        FileHandle file = Gdx.files.internal("databases/Database - Class.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");

        ArrayList<String> relevantData = new ArrayList<>();
        String mode = "";
        HashMap<Integer, Skill[]> tempMap = new HashMap<>();
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equalsIgnoreCase("Name")) {
                mode = "Name";
                if (!relevantData.isEmpty()) {
                    CLASS_LIST.put(relevantData.get(0).toUpperCase(), new BattlerClass(relevantData.get(0), //Name
                            relevantData.get(1).split("\n"), //Description
                            tempMap
                    ));
                    relevantData.clear();
                }
                relevantData.add(data[1]);
                tempMap = new HashMap<>();
                continue;
            }
            if (data[0].equalsIgnoreCase("Equipment")) {
                mode = "Equipment";
                relevantData.add(String.join("\n", Arrays.copyOfRange(data, 1, data.length)));
                continue;
            }
            if (data[0].equalsIgnoreCase("Skills")) {
                mode = data[0];
                continue;
            }
            if (mode.equalsIgnoreCase("Skills")) {
                tempMap.put(Integer.parseInt(data[0]),
                        Arrays.stream(Arrays.copyOfRange(data, 1, data.length))
                                .map(skill -> SKILL_LIST.get(skill.toUpperCase()))
                                .toArray(Skill[]::new)
                );

            }

        }
        int i = 0;

        CLASS_LIST.put(relevantData.get(i).toUpperCase(), new BattlerClass(relevantData.get(i), //Name
                relevantData.get(++i).split("\n"), //Description
                tempMap
        ));
    }
}
