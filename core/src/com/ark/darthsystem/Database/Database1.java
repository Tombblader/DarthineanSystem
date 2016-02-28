package com.ark.darthsystem.Database;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.BattlerClass;
import static com.ark.darthsystem.Database.SystemDatabase.*;
import com.ark.darthsystem.States.Battle;
import com.ark.darthsystem.Item;

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
    public static Battler Magia;
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
                    5, 4, 5, 6, Magic_Knight_Class, MagicKnight);
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
                    32, 18, 21, 10, Axeman_Class, Heavy_Warrior);
            Magia = new Battler("Magia",
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
                    7, 5, 5, 5, Water_Spirit_Moveset, Water_Spirit_Equipment);

            Water_Spirit = new Battler("???",
                    Battle.Element.Water,
                    Battler.Gender.Female,
                    1,
                    30, 30,
                    6, 4, 7, 5, Water_Spirit_Moveset, Water_Spirit_Equipment);
            Wind_Spirit = new Battler("???",
                    Battle.Element.Wind,
                    Battler.Gender.Female,
                    1,
                    20, 35,
                    5, 4, 5, 7, Water_Spirit_Moveset, Water_Spirit_Equipment);
            Earth_Spirit = new Battler("???",
                    Battle.Element.Earth,
                    Battler.Gender.Female,
                    1,
                    40, 10,
                    5, 7, 6, 4, Water_Spirit_Moveset, Water_Spirit_Equipment);
        inventory = new ArrayList<>();
        karma = 0;
        switches = new HashMap<>();
        variables = new HashMap<>();
    }
    
    public static ArrayList<Item> inventory;

    public static int karma;//Positive Karma is good, Negative is bad.

    public static HashMap<String, Boolean> switches;

    public static HashMap<String, Integer> variables;

    public static void save(String fileName) throws FileNotFoundException, IOException {
        FileOutputStream fileStream = new FileOutputStream(fileName);
        ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);

        objectStream.writeObject(Darth);
        objectStream.writeObject(Erik);
        objectStream.writeObject(Protox);
        objectStream.writeObject(Gladia);
        objectStream.writeObject(Veather);
        objectStream.writeObject(Magia);
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

        objectStream.close();
    }

    public static void load(String fileName)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fileStream = new FileInputStream(fileName);
        ObjectInputStream objectStream = new ObjectInputStream(fileStream);

        Database1.Darth = (Battler) objectStream.readObject();
        Database1.Erik = (Battler) objectStream.readObject();
        Database1.Protox = (Battler) objectStream.readObject();
        Database1.Gladia = (Battler) objectStream.readObject();
        Database1.Veather = (Battler) objectStream.readObject();
        Database1.Magia = (Battler) objectStream.readObject();
        Database1.Nairarum = (Battler) objectStream.readObject();
        Database1.Fire_Spirit = (Battler) objectStream.readObject();
        Database1.Water_Spirit = (Battler) objectStream.readObject();
        Database1.Wind_Spirit = (Battler) objectStream.readObject();
        Database1.Earth_Spirit = (Battler) objectStream.readObject();
        Database1.you = (Battler) objectStream.readObject();
        Database1.inventory = (ArrayList<Item>) objectStream.readObject();
        Database1.karma = objectStream.readInt();
        Database1.switches = (HashMap<String, Boolean>) objectStream.
                readObject();
        Database1.variables = (HashMap<String, Integer>) objectStream.
                readObject();
        objectStream.close();
    }

}
