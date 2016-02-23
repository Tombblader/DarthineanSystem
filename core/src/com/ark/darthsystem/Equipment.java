package com.ark.darthsystem;

import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.Graphics.ActorSkill;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.States.Battle;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Equipment extends Item implements Cloneable {

    public enum EquipmentType {

        RightArm(0),
        LeftArm(1),
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
    private ActorSkill animation;

    public Equipment() {
        super();
        this.equipElement = Battle.Element.Physical;
    }

    public Equipment(String getName,
            EquipmentType initializeType,
            Skill invoke,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        super(getName,
                false,
                invoke,
                useMP);
        this.equipElement = Battle.Element.Physical;
        equipmentSlot = initializeType;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = Battle.Element.Physical;
        animation = Database2.Sword();
    }

    public Equipment(String getName,
            EquipmentType initializeType,
            Skill invoke,
            Battle.Element initializeElement,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        super(getName,
                false,
                invoke,
                useMP);
        this.equipElement = Battle.Element.Physical;
        equipmentSlot = initializeType;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = initializeElement;
        animation = Database2.Sword();
    }

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
        //       this.battleAnimation = new Sprite(battleAnimation, false, false);
            animation = new ActorSkill((Sprite[]) GraphicsDriver.getMasterSheet().createSprites(battleAnimation).toArray(Sprite.class),
                    (Sprite[]) GraphicsDriver.getMasterSheet().createSprites(fieldAnimation).toArray(Sprite.class), 
                    1, 1, 5.0f/60f, null, ActorSkill.Area.FRONT);
    }

    public EquipmentType getEquipmentType() {
        return equipmentSlot;
    }

    public Battle.Element getElement() {
        return equipElement;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMagic() {
        return magic;
    }
    
    public ActorSkill getAnimation() {
        return animation;
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
