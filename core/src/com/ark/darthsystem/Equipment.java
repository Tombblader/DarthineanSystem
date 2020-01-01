package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;
import java.io.Serializable;

/**
 *
 * @author Keven
 */
public class Equipment extends Item implements Cloneable, Serializable {

    private static final long serialVersionUID = 553786345;
    private Slot equipmentSlot;
    private Type[] type;
    private int attack, defense, speed, magic;
    private Battle.Element equipElement;

    /**
     *
     */
    public Equipment() {
        super();
        this.equipElement = Battle.Element.Physical;
    }

    /**
     *
     * @param getName
     * @param getDescription
     * @param getMarketPrice
     * @param type
     * @param imageName
     * @param slot
     * @param invoke
     * @param initializeElement
     * @param useMP
     * @param initializeAttack
     * @param initializeDefense
     * @param initializeSpeed
     * @param initializeMagic
     */
    public Equipment(String getName,
            String getDescription,
            String imageName,
            int getMarketPrice,
            String[] type,
            Slot slot,
            Skill invoke,
            Battle.Element initializeElement,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        super(getName, getDescription, imageName, getMarketPrice, -1, invoke, useMP);
        this.equipElement = Battle.Element.Physical;
        this.type = new Type[type.length];
        for (int i = 0; i < this.type.length; i++) {
            this.type[i] = Type.valueOf(type[i].toUpperCase());
        }
        equipmentSlot = slot;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = initializeElement;
    }

    public Equipment(String getName,
            String getDescription,
            String icon,
            String animationName,
            int getMarketPrice,
            String[] type,
            Slot slot,
            Skill invoke,
            Battle.Element initializeElement,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        super(getName, getDescription, icon, getMarketPrice, -1, invoke, useMP);
        this.type = new Type[type.length];
        for (int i = 0; i < this.type.length; i++) {
            this.type[i] = Type.valueOf(type[i].toUpperCase());
        }
        equipmentSlot = slot;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = initializeElement;
    }

    /**
     *
     * @param getName
     * @param getDescription
     * @param imageName
     * @param getMarketPrice
     * @param type
     * @param slot
     * @param invoke
     * @param initializeElement
     * @param useMP
     * @param initializeAttack
     * @param initializeDefense
     * @param initializeSpeed
     * @param initializeMagic
     */
    public Equipment(String getName,
            String getDescription,
            String iconName,
            int getMarketPrice,
            Type[] type,
            Slot slot,
            Skill invoke,
            Battle.Element initializeElement,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        super(getName, getDescription, iconName, getMarketPrice, -1, invoke, useMP);
        this.equipElement = Battle.Element.Physical;
        this.type = type;
        equipmentSlot = slot;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = initializeElement;
    }
    

    public Type[] getType() {
        return type;
    }

    /**
     *
     * @return
     */
    public Slot getEquipmentType() {
        return equipmentSlot;
    }

    /**
     *
     * @return
     */
    public Battle.Element getElement() {
        return equipElement;
    }

    /**
     *
     * @return
     */
    public int getAttack() {
        return attack;
    }

    /**
     *
     * @return
     */
    public int getDefense() {
        return defense;
    }

    /**
     *
     * @return
     */
    public int getSpeed() {
        return speed;
    }

    /**
     *
     * @return
     */
    public int getMagic() {
        return magic;
    }

    public enum Slot implements Nameable {

        MainHand("Main Hand"),
        OffHand("Off-Hand"),
        Body("Body"),
        Head("Head"),
        Accessory("Accessory");
        String slot;

        private Slot(String setSlot) {
            slot = setSlot;
        }

        @Override
        public String getName() {
            return slot;
        }

        @Override
        public String getDescription() {
            return "";
        }
    }

    public enum Type {
        HAND,
        SWORD,
        DAGGER,
        STAFF,
        SPEAR,
        BOOMERANG,
        HAMMER,
        AXE,
        HEAVY,
        LIGHT,
        MALE,
        FEMALE,
        PRISMATIC,
        GRAY,
        RED,
        BLUE,
        YELLOW,
        GREEN,
        WHITE,
        BLACK,
        DARCY,
        ERIK,
        ORLY,
        GLADIA,
        KARIN,
        VEATHER,
        LAVENDER
    }
}
