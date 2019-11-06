package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.FieldBattler;
import com.ark.darthsystem.graphics.FieldBattlerAI;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class MonsterDatabase {

    public static HashMap<String, FieldBattlerAI> MONSTER_LIST = new HashMap<>();

    public MonsterDatabase() {
        FileHandle file = Gdx.files.internal("databases/Database - FieldBattlerAI.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;

            MONSTER_LIST.put(data[i].toUpperCase(), new FieldBattlerAI(new ArrayList<>(Arrays.asList(new FieldBattler[]{new FieldBattler(AIDatabase.BATTLER_LIST.get(data[i].toUpperCase()), //Name
                "monsters/" + data[++i], //Sprite Name
                (float) (1f / Double.parseDouble(data[++i])), //fps
                data[++i], //ShapeName
                (float) Double.parseDouble(data[++i]) //Speed

                )})), 0, 0));

        }
    }
}
