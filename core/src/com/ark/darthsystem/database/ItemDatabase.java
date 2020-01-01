package com.ark.darthsystem.database;

import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.Weapon;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class ItemDatabase {

    public static final HashMap<String, Equipment> EQUIPMENT_LIST = new HashMap<>();
    public static final HashMap<String, Item> ITEM_LIST = new HashMap<>();

    public ItemDatabase() {
        FileHandle file = Gdx.files.internal("databases/Database - Equipment.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;
            if (data[7].equalsIgnoreCase("MainHand")) {
            EQUIPMENT_LIST.put(data[i].toUpperCase(), new Weapon(data[i], //Name
                    data[++i], //Description
                    data[++i], //iconName
                    data[++i], //field animation
                    data[++i], //battlerAnimation
                    Integer.parseInt(data[++i]), //Market Price
                    data[++i].toUpperCase().split(", "), //Equipment Types
                    Equipment.Slot.valueOf(data[++i]), //Slot
                    SkillDatabase.SKILL_LIST.get(data[++i].toUpperCase()), //Skill Effect
                    Battle.Element.valueOf(data[++i]), //Element
                    Boolean.valueOf(data[++i].toLowerCase()), //Use MP?
                    Integer.parseInt(data[++i]), //Attack
                    Integer.parseInt(data[++i]), //Defense Multiplier
                    Integer.parseInt(data[++i]), //Speed Multiplier
                    Integer.parseInt(data[++i]) //Magic Multiplier
            )); //Divider
                
            } else {
            EQUIPMENT_LIST.put(data[i].toUpperCase(), new Equipment(data[i], //Name
                    data[++i], //Description
                    data[++i], //iconName
                    Integer.parseInt(data[i = i + 3]), //Market Price
                    data[++i].toUpperCase().split(", "), //Equipment Types
                    Equipment.Slot.valueOf(data[++i]), //Slot
                    SkillDatabase.SKILL_LIST.get(data[++i].toUpperCase()), //Skill Effect
                    Battle.Element.valueOf(data[++i]), //Element
                    Boolean.valueOf(data[++i].toLowerCase()), //Use MP?
                    Integer.parseInt(data[++i]), //Attack
                    Integer.parseInt(data[++i]), //Defense Multiplier
                    Integer.parseInt(data[++i]), //Speed Multiplier
                    Integer.parseInt(data[++i]) //Magic Multiplier
            )); //Divider
            }
        }
        file = Gdx.files.internal("databases/Database - Items.tsv");
        massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equalsIgnoreCase("Name")) {
                continue;
            }
            int i = 0;

            ITEM_LIST.put(data[i].toUpperCase(), new Item(data[i], //Name
                    data[++i], //Description
                    data[++i], //Image
                    Integer.parseInt(data[++i]), //Market Price
                    Integer.parseInt(data[++i]), //Charges
                    Integer.parseInt(data[++i]), //HP Effect
                    Integer.parseInt(data[++i]), //MP Effect
                    Boolean.valueOf(data[++i].toLowerCase()) //Is All?
            ));

        }
        file = Gdx.files.internal("databases/Database - Special Items.tsv");
        massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equalsIgnoreCase("Name")) {
                continue;
            }
            int i = 0;

            ITEM_LIST.put(data[i].toUpperCase(), new Item(data[i], //Name
                    data[++i], //Description
                    data[++i], //image;
                    Integer.parseInt(data[++i]), //Market Price
                    Integer.parseInt(data[++i]), //Charges
                    SkillDatabase.SKILL_LIST.get(data[++i].toUpperCase()), //Skill Effect
                    Boolean.valueOf(data[++i].toLowerCase()) //Is All?
            ));
        }
        ITEM_LIST.putAll(EQUIPMENT_LIST);
    }

}
