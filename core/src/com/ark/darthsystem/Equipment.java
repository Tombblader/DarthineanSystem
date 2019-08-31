package com.ark.darthsystem;

import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.FieldSkill;
import com.ark.darthsystem.states.Battle;
import java.io.IOException;

/**
 *
 * @author Keven
 */
public class Equipment extends Item implements Cloneable {

    private static final long serialVersionUID = 553786345;
    private Slot equipmentSlot;
    private Type[] type;
    private int attack, defense, speed, magic;
    private Battle.Element equipElement;
    private String animationName;
    private transient FieldSkill animation;
    private transient Actor battlerAnimation;
    private FieldSkill.Area areaName;
    private String shapeName;

    /**
     *
     */
    public Equipment() {
        super();
        this.shapeName = "basiccircle";
        this.areaName = FieldSkill.Area.FRONT;
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
        this.shapeName = "basiccircle";
        this.areaName = FieldSkill.Area.FRONT;
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
        animationName = "sword";
        battlerAnimation = new Actor("items/equipment/sword/battler/battler", 0, 0, 1/12f, true);
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
            String imageName,
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
        super(getName, getDescription, imageName, getMarketPrice, -1, invoke, useMP);
        this.shapeName = "basiccircle";
        this.areaName = FieldSkill.Area.FRONT;
        this.equipElement = Battle.Element.Physical;
        this.type = type;
        equipmentSlot = slot;
        attack = initializeAttack;
        defense = initializeDefense;
        speed = initializeSpeed;
        magic = initializeMagic;
        equipElement = initializeElement;
        animationName = "sword";
        animation = Database2.Sword();
        battlerAnimation = new Actor("items/equipment/sword/battler/battler", 0, 0, 1/12f, true);

    }   
    
    public Equipment(String getName,
            String getDescription,
            String getAnimation,
            FieldSkill.Area getArea,
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
        this(getName, 
                getDescription,
                getAnimation,
                getMarketPrice,
                type,
                slot,
                invoke,
                initializeElement,
                useMP,
                initializeAttack,
                initializeDefense,
                initializeSpeed,
                initializeMagic);
        animationName = getAnimation;
        areaName = getArea;
        shapeName = getShape;
        battlerAnimation = new Actor("items/equipment/" + getAnimation + "/battler/battler", 0, 0, 1/12f, true);
        animation = new FieldSkill("items/equipment/" + getAnimation + "/field/field",
//                    "items/equipment/" + getAnimation + "/battler/" + getAnimation, 
                    1, 1, 1.0f/12f, null, getArea, getShape);  
    }      
    
    private void readObject(java.io.ObjectInputStream stream)
        throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        battlerAnimation = new Actor("items/equipment/" + animationName + "/battler/battler", 0, 0, 1/12f, true);        
        animation = new FieldSkill("items/equipment/" + animationName + "/field/field",
                       1, 1, 1.0f/12f, null, areaName, shapeName);
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
    
    /**
     *
     * @return
     */
    public FieldSkill getAnimation() {
        return animation;
    }
    
    public void setAnimation(FieldSkill animation) {
        this.animation = animation;
    }

    public String getAnimationName() {
        return animationName;
    }

    public void setAnimationName(String animationName) {
        this.animationName = animationName;
    }

    public FieldSkill.Area getAreaName() {
        return areaName;
    }

    public void setAreaName(FieldSkill.Area areaName) {
        this.areaName = areaName;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public Actor getBattlerAnimation() {
        return battlerAnimation;
    }
    

    public Equipment clone() {
        Equipment temp = new Equipment(getName(), getDescription(), animationName, getPrice(), type, equipmentSlot, getInvoke(), getElement(), useMP(), attack, defense, speed, magic);        
        temp.animation = animation.placeOnMap();
        return temp;
    }
    public enum Slot implements Nameable {
        
        MainHand("Main Hand"),
        OffHand("Off Hand"),
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
