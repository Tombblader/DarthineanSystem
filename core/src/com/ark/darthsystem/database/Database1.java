package com.ark.darthsystem.database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerClass;
import static com.ark.darthsystem.database.SystemDatabase.*;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.Item;

import com.badlogic.gdx.Gdx;

import java.io.*;
import java.util.HashMap;
import java.util.ArrayList;

public class Database1 implements Serializable {

    public static final long serialVersionUID = 553786371;

    public static BattlerClass Custom_Class;

    public static Battler Darth;
    public static Battler Erik;
    public static Battler Protox;
    public static Battler Gladia;
    public static Battler Veather;
    public static Battler Karin;
    public static Battler Nairarum;
    public static Battler Fire_Spirit;

    public static Battler Water_Spirit;
    public static Battler Wind_Spirit;
    public static Battler Earth_Spirit;
    public static Battler you;

    public Database1() {
        Darth = new Battler("Darth",
                Battle.Element.Physical,
                Battler.Gender.Male,
                1,
                20, 20,
                5, 4, 5, 6, Magic_Knight_Class, Magic_Knight);
        Erik = new Battler("Erik",
                Battle.Element.Physical,
                Battler.Gender.Male,
                1,
                28, 12,
                7, 5, 6, 2, Swordsman_Class, Warrior);
        Protox = new Battler("Protox",
                Battle.Element.Physical,
                Battler.Gender.Male,
                1,
                32, 8,
                8, 7, 1, 4, Lancer_Class, Lancer);
        Gladia = new Battler("???",
                Battle.Element.Physical,
                Battler.Gender.Female,
                5,
                88, 34,
                20, 14, 16, 9, Weapon_Master_Class, Master);
        Veather = new Battler("Veather Mann",
                Battle.Element.Physical,
                Battler.Gender.Male,
                6,
                60, 21,
                32, 18, 21, 10, Gray_Mage_Class, Gray_Mage);
        Karin = new Battler("Karin",
                Battle.Element.Physical,
                Battler.Gender.Female,
                8,
                61, 60,
                10, 21, 40, 61, Mage_Class, Lancer);
        Nairarum = new Battler("Nairarum",
                Battle.Element.Light,
                Battler.Gender.Female,
                37,
                777, 77,
                127, 107, 114, 127, Angel_Class, Angel);
        Fire_Spirit = new Battler("???",
                Battle.Element.Fire,
                Battler.Gender.Female,
                1,
                32, 15,
                7, 3, 4, 5, Fire_Spirit_Class, Water_Spirit_Equipment);

        Water_Spirit = new Battler("???",
                Battle.Element.Water,
                Battler.Gender.Female,
                1,
                30, 30,
                3, 4, 7, 5, Water_Spirit_Class, Water_Spirit_Equipment);
        Wind_Spirit = new Battler("???",
                Battle.Element.Wind,
                Battler.Gender.Female,
                1,
                20, 35,
                5, 4, 5, 7, Water_Spirit_Class, Water_Spirit_Equipment);
        Earth_Spirit = new Battler("???",
                Battle.Element.Earth,
                Battler.Gender.Female,
                1,
                40, 10,
                5, 7, 6, 4, Water_Spirit_Class, Water_Spirit_Equipment);
        battlers = new HashMap<>(12);
        
        battlers.put("You", you);
        battlers.put("Darth", Darth);
        battlers.put("Erik", Erik);
        battlers.put("Protox", Protox);
        battlers.put("Gladia", Gladia);
        battlers.put("Veather", Veather);
        battlers.put("Karin", Karin);
        battlers.put("Nairarum", Nairarum);
        battlers.put("Fire Spirit", Fire_Spirit);
        battlers.put("Water Spirit", Water_Spirit);
        battlers.put("Wind Spirit", Wind_Spirit);
        battlers.put("Earth Spirit", Earth_Spirit);
        
        
        inventory = new ArrayList<>();
        money = 0;
        karma = 0;
        switches = new HashMap<>();
        variables = new HashMap<>();
    }

    public static ArrayList<Item> inventory;
    
    public static int money;

    public static int karma;//Positive Karma is good, Negative is bad.

    public static HashMap<String, Boolean> switches;
    
    public static HashMap<String, Battler> battlers;

    public static HashMap<String, Integer> variables;

    public static void save(String fileName) throws FileNotFoundException, IOException {
        try (ObjectOutputStream objectStream
                = new ObjectOutputStream(Gdx.files.local(fileName).write(false))) {
            objectStream.writeObject(Darth);
            objectStream.writeObject(Erik);
            objectStream.writeObject(Protox);
            objectStream.writeObject(Gladia);
            objectStream.writeObject(Veather);
            objectStream.writeObject(Karin);
            objectStream.writeObject(Nairarum);
            objectStream.writeObject(Fire_Spirit);
            objectStream.writeObject(Water_Spirit);
            objectStream.writeObject(Wind_Spirit);
            objectStream.writeObject(Earth_Spirit);
            objectStream.writeObject(you);
            objectStream.writeObject(inventory);
            objectStream.writeInt(karma);
            objectStream.writeObject(switches);
            objectStream.writeObject(variables);
        }
    }

    public static void load(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        try (ObjectInputStream objectStream
                = new ObjectInputStream(Gdx.files.internal(fileName).read())) {
            Database1.Darth = (Battler) objectStream.readObject();
            Database1.Erik = (Battler) objectStream.readObject();
            Database1.Protox = (Battler) objectStream.readObject();
            Database1.Gladia = (Battler) objectStream.readObject();
            Database1.Veather = (Battler) objectStream.readObject();
            Database1.Karin = (Battler) objectStream.readObject();
            Database1.Nairarum = (Battler) objectStream.readObject();
            Database1.Fire_Spirit = (Battler) objectStream.readObject();
            Database1.Water_Spirit = (Battler) objectStream.readObject();
            Database1.Wind_Spirit = (Battler) objectStream.readObject();
            Database1.Earth_Spirit = (Battler) objectStream.readObject();
            Database1.you = (Battler) objectStream.readObject();
            Database1.inventory = (ArrayList<Item>) objectStream.readObject();
            Database1.karma = objectStream.readInt();
            Database1.switches = (HashMap<String, Boolean>) objectStream.readObject();
            Database1.variables = (HashMap<String, Integer>) objectStream.readObject();
        }
    }
}
