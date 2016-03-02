/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem;

import com.ark.darthsystem.Database.Database1;
import com.ark.darthsystem.States.Battle;
import static com.ark.darthsystem.BattleDriver.*;
import com.ark.darthsystem.Database.SystemDatabase;
import com.ark.darthsystem.Graphics.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;


/**
 *
 * @author trankt1
 */
public class BattleDriver {

    public BattleDriver() {

    }

    public static void addGoodKarma(int i) {
        Database1.karma += i;
    }

    public static void addBadKarma(int i) {
        Database1.karma -= i;
    }

    public static void addGoodKarma() {
        Database1.karma++;
    }

    public static void addBadKarma() {
        Database1.karma--;
    }

    public static boolean isGood() {
        return Database1.karma >
                0;
    }

    public static boolean isBad() {
        return Database1.karma <
                0;
    }

    public static void setSwitch(String switchName, boolean b) {
        Database1.switches.put(switchName, b);
    }

    public static void toggleSwitch(String switchName) {
        Database1.switches.put(switchName, !Database1.switches.get(switchName));
    }

    public static boolean getSwitch(String name) {
        return Database1.switches.get(name);
    }

    public static Battler characterCreation() {
        String name;
        Battler.Gender gender;
        int HP;
        int MP;
        int attack;
        int defense;
        int speed;
        int magic;
        Equipment[] startEquip = SystemDatabase.Warrior;
        Skill[] newSkill = new Skill[0];
        BattleDriver.printline("You can create your character here.");
        name = condition("First, state your name.");
//        gender = Enum.valueOf(Battler.Gender.class, condition("What is your gender?", new String[]{"Male", "Female"}));
        BattleDriver.printline("You have 40 stat points to distribute.");
        HP = Integer.valueOf(condition("Enter HP."));
        MP = Integer.valueOf(condition("Enter MP."));
        BattleDriver.printline("You have 20 stat points to distribute.");
        attack = Integer.valueOf(condition("Enter attack."));
        defense = Integer.valueOf(condition("Enter defense."));
        speed = Integer.valueOf(condition("Enter speed."));
        magic = Integer.valueOf(condition("Enter magic."));
//        return new Battler(name, Battle.Element.Physical, gender, 1, HP, MP, attack, defense, speed, magic, newSkill, startEquip);
        return null;
    }

    public static void fullHeal(ArrayList<Battler> group) {
        for (Battler group1 : group) {
            if (group1 != null) {
                group1.fullHeal();
                group1.changeStatus(Battle.Stats.Normal);
            }
        }
    }

    public static void printline(String newMessage) {
        if (!newMessage.equals("")) {
            ArrayList<String> formattedMessage = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(newMessage, " \n", true);
            String formatted = "";
            while (st.hasMoreTokens()) {
                String temp = st.nextToken();
                if ((formatted + temp).length() > WRAP_LENGTH || temp.equals("\n")) {
                    formattedMessage.add(formatted);
                    if (temp.equals(" ") || temp.equals("\n")) {
                        formatted = "";
                    } else {
                        formatted = temp;
                    }
                } else {
                    formatted += temp;
                }
            }
            formattedMessage.add(formatted);
            GraphicsDriver.setMessage(formattedMessage);
        }
    }
    
    public static void printline(ActorBattler battler, String newMessage) {
        if (!newMessage.equals("")) {
            ArrayList<String> formattedMessage = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(newMessage, " \n", true);
            String formatted = "";
            while (st.hasMoreTokens()) {
                String temp = st.nextToken();
                if ((formatted + temp).length() > WRAP_LENGTH || temp.equals("\n")) {
                    formattedMessage.add(formatted);
                    if (temp.equals(" ") || temp.equals("\n")) {
                        formatted = "";
                    } else {
                        formatted = temp;
                    }
                } else {
                    formatted += temp;
                }
            }
            formattedMessage.add(formatted);
            GraphicsDriver.setMessage(battler.getBattler().getName(), battler.getSprite().getFaceAnimation(ActorSprite.SpriteModeFace.NORMAL), formattedMessage);
        }        
    }

    public static void printline(ActorBattler battler, ActorSprite.SpriteModeFace faceMode, String newMessage) {
        if (!newMessage.equals("")) {
            ArrayList<String> formattedMessage = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(newMessage, " \n", true);
            String formatted = "";
            while (st.hasMoreTokens()) {
                String temp = st.nextToken();
                if ((formatted + temp).length() > WRAP_LENGTH || temp.equals("\n")) {
                    formattedMessage.add(formatted);
                    if (temp.equals(" ") || temp.equals("\n")) {
                        formatted = "";
                    } else {
                        formatted = temp;
                    }
                } else {
                    formatted += temp;
                }
            }
            formattedMessage.add(formatted);
            GraphicsDriver.setMessage(battler.getBattler().getName(), battler.getSprite().getFaceAnimation(faceMode), formattedMessage);
        }        
    }
    
    
    public static void print(String newMessage) {
        StringTokenizer st = new StringTokenizer(newMessage, " \n", true);
        ArrayList<String> formattedMessage = new ArrayList<>();
        String formatted = "";
        String temp = "";
        while (st.hasMoreTokens()) {
            temp = st.nextToken();
            if ((formatted + temp).length() > WRAP_LENGTH || temp.equals("\n")) {
                formattedMessage.add(formatted);
                if (temp.equals(" ") || temp.equals("\n")) {
                    formatted = "";
                } else {
                    formatted = temp;
                }
            } else {
                formatted += temp;
            }
        }
        if (!formatted.isEmpty()) {
            formattedMessage.add(formatted);
        }
        GraphicsDriver.appendMessage(formattedMessage);
    }

    public static String condition(String header) {
        printline(header);
        textCondition = "";

        return textCondition;
    }

    public static void save() {
        if (false) {
            try {
                Database1.save(condition("Save to which file?"));
            } catch (Exception e) {
                System.out.print(e);
                save();
            }
        }
    }

    public static boolean load() {
        boolean loaded = false;
        if (loaded) {
            try {
                Database1.load(BattleDriver.condition("Type in the save file's name."));
            } catch (IOException | ClassNotFoundException e) {
                System.out.println(e);
                loaded = load();
            }
        }
        return loaded;
    }

    public static void equip(Battler tempBattler, Equipment newEquipment) {
        Equipment temp = tempBattler.equip(newEquipment);
        if (temp != null) {
            Database1.inventory.add(temp);
        }
    }

    public static void addItem(Item newItem) {
        if (Database1.inventory.contains(newItem)) {
            if (Database1.inventory.get(Database1.inventory.indexOf(newItem)) == newItem) {
                Database1.inventory.get(Database1.inventory.indexOf(newItem)).increaseQuantity(1);
            } else {
                Database1.inventory.get(Database1.inventory.indexOf(newItem)).increaseQuantity(newItem.getQuantity());
            }
        } else {
            Database1.inventory.add(newItem);
        }
    }

    public static void printStats(Battler[] party) {
        for (Battler party1 : party) {
            BattleDriver.printline(party1.getName() +
                    "\t HP: " +
                    party1.getHP() +
                    "/" +
                    party1.getMaxHP() +
                    "\t MP: " +
                    party1.getMP() +
                    "/" +
                    party1.getMaxMP());
            BattleDriver.printline("Level: " + party1.getLevel());
            BattleDriver.printline("Attack: " + party1.getAttack());
            BattleDriver.printline("Defense: " + party1.getDefense());
            BattleDriver.printline("Speed: " + party1.getSpeed());
            BattleDriver.printline("Magic: " + party1.getMagic());
        }
    }

    public static void removePartyMember(Battler[] party, Battler removed) {
        Battler[] tempParty = new Battler[party.length];
        int position = Arrays.binarySearch(party, removed);
        if (position == -1) {
            return;
        }
        party[position] = null;
        System.arraycopy(tempParty, 0, party, 0, position);
        if (party.length > position + 1) {
            System.arraycopy(tempParty,
                    position,
                    party,
                    position + 1,
                    party.length - position + 1);
        }
        party = tempParty;

    }

    public static void addMember(Battler[] party, Battler added) {
        Battler[] tempParty = new Battler[party.length + 1];
        System.arraycopy(tempParty, 0, party, 0, party.length);
        tempParty[tempParty.length - 1] = added;
        party = tempParty;
    }

    public static String textCondition = "";
    public static final int WRAP_LENGTH = 80;

}
