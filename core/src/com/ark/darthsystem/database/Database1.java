package com.ark.darthsystem.database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.Item;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Database1 implements Serializable {

    public static final long serialVersionUID = 553786371;

    public static final HashMap<String, Battler> BATTLER_LIST = new HashMap<>();
    
    public Database1() {
        FileHandle file = Gdx.files.internal("databases/battlers.tsv");
        String[] massiveString = file.readString().split("(\r\n|\r|\n)");
        for (String token : massiveString) {
            String[] data = token.split("\t");
            if (data[0].equals("Name")) {
                continue;
            }
            int i = 0;
            
            BATTLER_LIST.put(data[i].toUpperCase(), new Battler(data[i], //Name
                    data[++i],
                    SystemDatabase.CLASS_LIST.get(data[++i].toUpperCase()), //Class
                    Battle.Element.valueOf(data[++i]), //Element
                    Battler.Gender.valueOf(data[++i]), //Gender                    
                    Integer.parseInt(data[++i]), //Level
                    Integer.parseInt(data[++i]), //HP
                    Integer.parseInt(data[++i]), //MP
                    Integer.parseInt(data[++i]), //Attack
                    Integer.parseInt(data[++i]), //Defense
                    Integer.parseInt(data[++i]), //Speed
                    Integer.parseInt(data[++i]),  //Magic
                    Integer.parseInt(data[++i]), //HPTier
                    Integer.parseInt(data[++i]), //MPTier
                    Integer.parseInt(data[++i]), //AttackTier
                    Integer.parseInt(data[++i]), //DefenseTier
                    Integer.parseInt(data[++i]), //SpeedTier
                    Integer.parseInt(data[++i]),  //MagicTier
                    new EnumMap<>(Equipment.Slot.class)
                    )); //Divider
        }
        
        inventory = new ArrayList<>();
        money = 1000;
        karma = 0;
        switches = new HashMap<>();
        variables = new HashMap<>();
    }

    public static ArrayList<Item> inventory;
    
    public static int money;

    public static int karma;//Positive Karma is good, Negative is bad.

    public static HashMap<String, Boolean> switches;

    public static HashMap<String, String> variables;

    public static void save(String fileName) throws FileNotFoundException, IOException {
        try (ObjectOutputStream objectStream
                = new ObjectOutputStream(Gdx.files.local(fileName).write(false))) {
            objectStream.writeObject(BATTLER_LIST);
            objectStream.writeObject(inventory);
            objectStream.writeInt(karma);
            objectStream.writeInt(money);
            objectStream.writeObject(switches);
            objectStream.writeObject(variables);
        }
    }

    public static void load(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream objectStream
                = new ObjectInputStream(Gdx.files.local(fileName).read())) {
            Database1.BATTLER_LIST.clear();
            Database1.BATTLER_LIST.putAll((HashMap<String, Battler>) objectStream.readObject());
            Database1.inventory = (ArrayList<Item>) objectStream.readObject();
            Database1.karma = objectStream.readInt();
            Database1.money = objectStream.readInt();
            Database1.switches = (HashMap<String, Boolean>) objectStream.readObject();
            Database1.variables = (HashMap<String, String>) objectStream.readObject();
        }
    }
}
