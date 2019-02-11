package com.ark.darthsystem.database;

import com.ark.darthsystem.Skill;
import com.ark.darthsystem.graphics.ActorSprite;
import com.ark.darthsystem.graphics.FieldSkill;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public class SkillDatabase {

    public static final HashMap<String, Skill> SKILL_LIST = new HashMap<>();
    public static final HashMap<String, FieldSkill> FIELD_SKILL_LIST = new HashMap<>();

    public SkillDatabase() {
        FileHandle file = Gdx.files.internal("databases/skills.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;
            SKILL_LIST.put(data[i].toUpperCase(), new Skill(data[i], //Name
                    data[++i], //Description
                    ActorSprite.SpriteModeBattler.valueOf(data[++i]),
                    data[++i],
                    1 / (float) Double.parseDouble(data[++i]),
                    data[++i],
                    data[++i],
                    Integer.parseInt(data[++i]), //Cost
                    Battle.Element.valueOf(data[++i]), //Element
                    Boolean.valueOf(data[++i]), //Targets ally?
                    Boolean.valueOf(data[++i]), //Is All
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
        file = Gdx.files.internal("databases/skillsai.tsv");
        massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;
            SKILL_LIST.put(data[i].toUpperCase(), new Skill(data[i], //Name
                    data[++i], //Description
                    ActorSprite.SpriteModeBattler.valueOf(data[++i]),
                    data[++i],
                    1 / (float) Double.parseDouble(data[++i]),
                    data[++i],
                    data[++i],
                    Integer.parseInt(data[++i]), //Cost
                    Battle.Element.valueOf(data[++i]), //Element
                    Boolean.valueOf(data[++i]), //Targets ally?
                    Boolean.valueOf(data[++i]),
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

        file = Gdx.files.internal("databases/fieldskills.tsv");
        massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;

            FIELD_SKILL_LIST.put(data[i].toUpperCase(), new FieldSkill(data[i], //Name
                    data[++i], //castimage
                    data[++i], //final image
                    data[++i], //cast sound
                    data[++i], //cast sound
                    data[++i].split(", *"), //tag
                    (float) (Double.parseDouble(data[++i])), //startx
                    (float) (Double.parseDouble(data[++i])), //starty
                    (float) (Double.parseDouble(data[++i])), //speed
                    (float) (Double.parseDouble(data[++i])), //range
                    (float) (1 / Double.parseDouble(data[++i])), //fps
                    (float) (Double.parseDouble(data[++i])), //cast time
                    (float) (Double.parseDouble(data[++i])), //aftercast delay
                    SKILL_LIST.get(data[++i].toUpperCase()), //skill
                    FieldSkill.Area.valueOf(data[++i].toUpperCase()), //area
                    data[++i] //shape
            ));
        }

        file = Gdx.files.internal("databases/fieldskillsai.tsv");
        massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;

            FIELD_SKILL_LIST.put(data[i].toUpperCase(), new FieldSkill(data[i], //Name
                    data[++i],
                    data[++i],
                    data[++i],
                    data[++i],
                    data[++i].split(", *"),
                    (float) (Double.parseDouble(data[++i])),
                    (float) (Double.parseDouble(data[++i])),
                    (float) (Double.parseDouble(data[++i])),
                    (float) (Double.parseDouble(data[++i])),
                    (float) (1 / Double.parseDouble(data[++i])),
                    (float) (Double.parseDouble(data[++i])),
                    (float) (Double.parseDouble(data[++i])),
                    SKILL_LIST.get(data[++i].toUpperCase()),
                    FieldSkill.Area.valueOf(data[++i].toUpperCase()),
                    data[++i]
            ));
        }

    }
}
