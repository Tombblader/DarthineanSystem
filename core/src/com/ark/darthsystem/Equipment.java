package com.ark.darthsystem;

import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.ActorSkill;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Equipment extends Item implements Cloneable {

    public enum EquipmentType {

        MainHand(0),
        OffHand(1),
        Body(2),
        Head(3),
        Accessory(4);
        int slot;

        private EquipmentType(int setSlot) {
            slot = setSlot;
        }

        public int getSlot() {
            return slot;
        }
    }
    private EquipmentType equipmentSlot;
    private int attack, defense, speed, magic;
    private Battle.Element equipElement;
    private transient ActorSkill animation;

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
     * @param initializeType
     * @param invoke
     * @param useMP
     * @param initializeAttack
     * @param initializeDefense
     * @param initializeSpeed
     * @param initializeMagic
     */
    public Equipment(String getName,
            EquipmentType initializeType,
            Skill invoke,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        super(getName, false, invoke, useMP);
        this.equipElement = Battle.Element.Physical;
        equipmentSlot = initializeType;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = Battle.Element.Physical;
        animation = Database2.Sword();
    }

    /**
     *
     * @param getName
     * @param initializeType
     * @param invoke
     * @param initializeElement
     * @param useMP
     * @param initializeAttack
     * @param initializeDefense
     * @param initializeSpeed
     * @param initializeMagic
     */
    public Equipment(String getName,
            EquipmentType initializeType,
            Skill invoke,
            Battle.Element initializeElement,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        super(getName, false, invoke, useMP);
        this.equipElement = Battle.Element.Physical;
        equipmentSlot = initializeType;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = initializeElement;
        animation = Database2.Sword();
    }

    /**
     *
     * @param getName
     * @param initializeType
     * @param invoke
     * @param useMP
     * @param initializeAttack
     * @param initializeDefense
     * @param initializeSpeed
     * @param initializeMagic
     * @param battleAnimation
     * @param fieldAnimation
     */
    public Equipment(String getName,
            EquipmentType initializeType,
            Skill invoke,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic,
            String battleAnimation,
            String fieldAnimation) {
        this(getName,
                initializeType,
                invoke,
                useMP,
                initializeAttack,
                initializeDefense,
                initializeSpeed,
                initializeMagic);
            animation = new ActorSkill(fieldAnimation,
                    battleAnimation, 
                    1, 1, 1.0f/12f, null, ActorSkill.Area.FRONT);
    }

    /**
     *
     * @return
     */
    public EquipmentType getEquipmentType() {
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
    
    /**
     *
     * @return
     */
    public ActorSkill getAnimation() {
        return animation;
    }
    
    public void setAnimation(ActorSkill animation) {
        this.animation = animation;
    }

    public Equipment clone() {
        Equipment temp = new Equipment();
        temp.equipElement = equipElement;
        temp.equipmentSlot = equipmentSlot;
        temp.attack = attack;
        temp.defense = defense;
        temp.speed = speed;
        temp.magic = magic;
        temp.equipElement = equipElement;
        return temp;
    }
}
