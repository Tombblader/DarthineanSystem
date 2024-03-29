package com.ark.darthsystem;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.ActorSprite;
import com.ark.darthsystem.graphics.FieldBattler;
import com.ark.darthsystem.database.Database1;
import static com.ark.darthsystem.BattleDriver.*;
import com.ark.darthsystem.graphics.ActorSprite.SpriteModeFace;
import com.ark.darthsystem.statusEffects.Normal;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * com.ark.darthsystem.BattleDriver A list of helper static methods mainly used
 * in Battle State.
 *
 * @author Keven Tran
 */
public class BattleDriver {

    public static String textCondition = "";
    public static final int WRAP_LENGTH = 80;

    /**
     *
     * @param i
     */
    public static void addGoodKarma(int i) {
        Database1.karma += i;
    }

    /**
     *
     * @param i
     */
    public static void addBadKarma(int i) {
        Database1.karma -= i;
    }

    /**
     *
     */
    public static void addGoodKarma() {
        Database1.karma++;
    }

    /**
     *
     */
    public static void addBadKarma() {
        Database1.karma--;
    }

    /**
     *
     * @return
     */
    public static boolean isGood() {
        return Database1.karma > 0;
    }

    /**
     *
     * @return
     */
    public static boolean isBad() {
        return Database1.karma < 0;
    }

    /**
     *
     * @param switchName
     * @param b
     */
    public static void setSwitch(String switchName, boolean b) {
        Database1.switches.put(switchName, b);
    }

    /**
     *
     * @param switchName
     */
    public static void toggleSwitch(String switchName) {
        Database1.switches.put(switchName, !Database1.switches.get(switchName));
    }

    /**
     *
     * @param name
     * @return
     */
    public static boolean getSwitch(String name) {
        return Database1.switches.get(name);
    }

    /**
     *
     * @param group
     */
    public static void fullHeal(ArrayList<Battler> group) {
        for (Battler group1 : group) {
            if (group1 != null) {
                group1.fullHeal();
                group1.getAllStatus().clear();
                group1.changeStatus(new Normal());
            }
        }
    }

    /**
     *
     * @param newMessage
     */
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

    /**
     *
     * @param battler
     * @param newMessage
     */
    public static void printline(FieldBattler battler, String newMessage) {
        printline(battler, SpriteModeFace.NORMAL, newMessage);
    }

    public static void printline(String name, String newMessage) {
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
            GraphicsDriver.setMessage(name, formattedMessage);
        }
    }

    /**
     *
     * @param battler
     * @param faceMode
     * @param newMessage
     */
    public static void printline(FieldBattler battler, ActorSprite.SpriteModeFace faceMode, String newMessage) {
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
            GraphicsDriver.setMessage(battler.getBattler().getName(),
                    battler.getSprite().getFaceAnimation(faceMode),
                    formattedMessage);
        }
    }

    /**
     *
     * @param newMessage
     */
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

    /**
     *
     * @param tempBattler
     * @param newEquipment
     */
    public static void equip(Battler tempBattler, Equipment newEquipment) {
        Equipment temp = tempBattler.equip(newEquipment);
        if (temp != null) {
            Database1.inventory.add(temp);
        }
    }

    /**
     *
     * @param newItem
     */
    public static void addItem(Item newItem) {
        if (newItem != null) {
            newItem = (Item) newItem.clone();
            if (Database1.inventory.contains(newItem) && (newItem.isStackable())) {
                Database1.inventory.get(Database1.inventory.indexOf(newItem)).increaseQuantity(newItem.getCharges());
            } else {
                Database1.inventory.add(newItem);
            }
        }
    }

    public static void removeItem(Item newItem, int qty) {
        if ((newItem.isStackable())) {
            Database1.inventory.get(Database1.inventory.indexOf(newItem)).decreaseQuantity(qty);
            if (Database1.inventory.get(Database1.inventory.indexOf(newItem)).getCharges() <= 0) {
                Database1.inventory.remove(newItem);
            }
        } else {
            Database1.inventory.remove(newItem);
        }
    }

    public static void addItems(Item[] item) {
        for (Item i : item) {
            addItem(i);
        }
    }

    /**
     *
     * @param party
     */
    public static void printStats(Battler[] party) {
        for (Battler party1 : party) {
            BattleDriver.printline(party1.getName()
                    + "\t HP: "
                    + party1.getHP()
                    + "/"
                    + party1.getMaxHP()
                    + "\t MP: "
                    + party1.getMP()
                    + "/"
                    + party1.getMaxMP());
            BattleDriver.printline("Level: " + party1.getLevel());
            BattleDriver.printline("Attack: " + party1.getAttack());
            BattleDriver.printline("Defense: " + party1.getDefense());
            BattleDriver.printline("Speed: " + party1.getSpeed());
            BattleDriver.printline("Magic: " + party1.getMagic());
        }
    }

    /**
     *
     */
    public BattleDriver() {

    }

}
