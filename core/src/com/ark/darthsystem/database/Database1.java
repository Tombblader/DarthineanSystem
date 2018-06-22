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
                    new EnumMap<Equipment.Slot, Equipment>(Equipment.Slot.class)
                    )); //Divider
        }//        Darth = new Battler("Darth",
//                Battle.Element.Physical,
//                Battler.Gender.Male,
//                1,
//                20, 20,
//                5, 4, 5, 6, Magic_Knight_Class, Magic_Knight);
//        Erik = new Battler("Erik",
//                Battle.Element.Physical,
//                Battler.Gender.Male,
//                1,
//                28, 12,
//                7, 5, 6, 2, Swordsman_Class, Warrior);
//        Protox = new Battler("Protox",
//                Battle.Element.Physical,
//                Battler.Gender.Male,
//                1,
//                32, 8,
//                8, 7, 1, 4, Lancer_Class, Lancer);
//        Gladia = new Battler("???",
//                Battle.Element.Physical,
//                Battler.Gender.Female,
//                5,
//                88, 34,
//                20, 14, 16, 9, Weapon_Master_Class, Master);
//        Veather = new Battler("Veather Mann",
//                Battle.Element.Physical,
//                Battler.Gender.Male,
//                6,
//                60, 21,
//                32, 18, 21, 10, Gray_Mage_Class, Gray_Mage);
//        Karin = new Battler("Karin",
//                Battle.Element.Physical,
//                Battler.Gender.Female,
//                8,
//                61, 60,
//                10, 21, 40, 61, Mage_Class, Lancer);
//        Naira = new Battler("Naira",
//                Battle.Element.Light,
//                Battler.Gender.Female,
//                37,
//                777, 77,
//                127, 107, 114, 127, Angel_Class, Angel);
//        Fire_Spirit = new Battler("???",
//                Battle.Element.Fire,
//                Battler.Gender.Female,
//                1,
//                32, 15,
//                7, 3, 4, 5, Fire_Spirit_Class, Water_Spirit_Equipment);
//
//        Water_Spirit = new Battler("???",
//                Battle.Element.Water,
//                Battler.Gender.Female,
//                1,
//                30, 30,
//                3, 4, 7, 5, Water_Spirit_Class, Water_Spirit_Equipment);
//        Wind_Spirit = new Battler("???",
//                Battle.Element.Wind,
//                Battler.Gender.Female,
//                1,
//                20, 35,
//                5, 4, 5, 7, Water_Spirit_Class, Water_Spirit_Equipment);
//        Earth_Spirit = new Battler("???",
//                Battle.Element.Earth,
//                Battler.Gender.Female,
//                1,
//                40, 10,
//                5, 7, 6, 4, Water_Spirit_Class, Water_Spirit_Equipment);
//        battlers = new HashMap<>(12);
//        
//        battlers.put("You", you);
//        battlers.put("Darth", Darth);
//        battlers.put("Erik", Erik);
//        battlers.put("Protox", Protox);
//        battlers.put("Gladia", Gladia);
//        battlers.put("Veather", Veather);
//        battlers.put("Karin", Karin);
//        battlers.put("Naira", Naira);
//        battlers.put("Fire Spirit", Fire_Spirit);
//        battlers.put("Water Spirit", Water_Spirit);
//        battlers.put("Wind Spirit", Wind_Spirit);
//        battlers.put("Earth Spirit", Earth_Spirit);
        
        
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

    public static HashMap<String, Integer> variables;

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
            Database1.variables = (HashMap<String, Integer>) objectStream.readObject();
        }
    }
}
