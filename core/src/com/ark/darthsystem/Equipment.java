package com.ark.darthsystem;

import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.ActorSkill;
import com.ark.darthsystem.states.Battle;

/**
 *
 * @author Keven
 */
public class Equipment extends Item implements Cloneable {

    public Type[] getType() {
        return type;
    }

    public enum Slot {

        MainHand(0),
        OffHand(1),
        Body(2),
        Head(3),
        Accessory(4);
        int slot;

        private Slot(int setSlot) {
            slot = setSlot;
        }

        public int getSlot() {
            return slot;
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
    private Slot equipmentSlot;
    private Type[] type;
    private int attack, defense, speed, magic;
    private Battle.Element equipElement;
    private String animationName;
    private transient ActorSkill animation;

    /**
     *
     */
    public Equipment() {
        super();
        this.equipElement = Battle.Element.Physical;
    }

//    /**
//     *
//     * @param getName
//     * @param initializeType
//     * @param invoke
//     * @param useMP
//     * @param initializeAttack
//     * @param initializeDefense
//     * @param initializeSpeed
//     * @param initializeMagic
//     */
//    public Equipment(String getName,
//            EquipmentType initializeType,
//            Skill invoke,
//            boolean useMP,
//            int initializeAttack,
//            int initializeDefense,
//            int initializeSpeed,
//            int initializeMagic) {
//        super(getName, "", 0, false, invoke, useMP);
//        this.equipElement = Battle.Element.Physical;
//        equipmentSlot = initializeType;
//        attack = initializeAttack;
//        defense = initializeDefense;
//        speed = initializeSpeed;
//        magic = initializeMagic;
//        equipElement = Battle.Element.Physical;
//        animation = Database2.Sword();
//    }

    /**
     *
     * @param getName
     * @param getDescription
     * @param getMarketPrice
     * @param type
     * 
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
        super(getName, getDescription, getMarketPrice, -1, invoke, useMP);
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
        animation = Database2.Sword();
    }

    /**
     *
     * @param getName
     * @param getDescription
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
        super(getName, getDescription, getMarketPrice, -1, invoke, useMP);
        this.equipElement = Battle.Element.Physical;
        this.type = type;
        equipmentSlot = slot;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = initializeElement;
        animation = Database2.Sword();
    }   
    
    public Equipment(String getName,
            String getDescription,
            String getAnimation,
            ActorSkill.Area getArea,
            String getShape,
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
        animationName = getAnimation;
        animation = new ActorSkill("equipment/" + getAnimation + "/field/" + getAnimation,
                    "equipment/" + getAnimation + "/battler/" + getAnimation, 
                    1, 1, 1.0f/12f, null, getArea, getShape);  
    }      
    
//    /**
//     *
//     * @param getName
//     * @param initializeType
//     * @param invoke
//     * @param useMP
//     * @param initializeAttack
//     * @param initializeDefense
//     * @param initializeSpeed
//     * @param initializeMagic
//     * @param battleAnimation
//     * @param fieldAnimation
//     */
//    public Equipment(String getName,
//            EquipmentType initializeType,
//            Skill invoke,
//            boolean useMP,
//            int initializeAttack,
//            int initializeDefense,
//            int initializeSpeed,
//            int initializeMagic,
//            String battleAnimation,
//            String fieldAnimation) {
//        this(getName,
//                initializeType,
//                invoke,
//                useMP,
//                initializeAttack,
//                initializeDefense,
//                initializeSpeed,
//                initializeMagic);
//            animation = new ActorSkill(fieldAnimation,
//                    battleAnimation, 
//                    1, 1, 1.0f/12f, null, ActorSkill.Area.FRONT);
//    }

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
        Equipment temp = new Equipment(getName(), getDescription(), getPrice(), type, equipmentSlot, getInvoke(), getElement(), useMP(), attack, defense, speed, magic);        
        temp.animation = animation.clone();
        temp.animationName = animationName;
        return temp;
    }
}
