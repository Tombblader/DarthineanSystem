package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.FieldBattler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class CharacterDatabase {

    public static HashMap<String, FieldBattler> CHARACTER_LIST = new HashMap<>();

    public CharacterDatabase() {
        FileHandle file = Gdx.files.internal("databases/fieldbattlers.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;

            CHARACTER_LIST.put(data[i].toUpperCase(), new FieldBattler(Database1.BATTLER_LIST.get(data[i].toUpperCase()), //Name
                    "characters/" + data[++i], //Sprite Name
                    (float) (1f / Double.parseDouble(data[++i])), //fps
                    data[++i], //ShapeName
                    (float) Double.parseDouble(data[++i]) //Speed
            ));
        }
    }

}
