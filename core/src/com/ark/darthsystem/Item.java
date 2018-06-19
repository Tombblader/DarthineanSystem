package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.audio.Sound;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Keven
 */
public class Item implements Serializable, Cloneable, Nameable {

    private static final long serialVersionUID = 553786344;    
    private int HPValue;
    private int MPValue;
    private boolean stackable;
    private boolean isAll;
    private Skill invoke;
    private boolean useMP;
    private String name;
    private String description;
    private int price;
    private int maxCharges;
    private int charges;
    private String iconName;
    public Sound sound;

    /**
     * Get the value of sound
     *
     * @return the value of sound
     */
    public Sound getSound() {
        return sound;
    }

    /**
     * Set the value of sound
     *
     * @param sound new value of sound
     */
    public void setSound(Sound sound) {
        this.sound = sound;
    }


    /**
     *
     */
    public Item() {
        this.iconName = "items/potion/icon";
    }

    /**
     *
     * @param getName
     * @param getDescription
     * @param price
     * @param charges
     * @param initializeHP
     * @param initializeMP
     * @param all
     */
    public Item(String getName,
            String getDescription,
            int price,
            int charges,
            int initializeHP,
            int initializeMP,
            boolean all) {
        this.iconName = "items/potion/icon";
        name = getName;
        description = getDescription;
        this.price = price;
        HPValue = initializeHP;
        MPValue = initializeMP;
        isAll = all;
        maxCharges = charges;
        this.charges = maxCharges;
        stackable = charges <= 1;
    }

    /**
     *
     * @param getName
     * @param getDescription
     * @param price
     * @param charges
     * @param initializeSkill
     * @param initializeUseMP
     */
    public Item(String getName,
            String getDescription,
            int price,
            int charges,
            Skill initializeSkill,
            boolean initializeUseMP) {
        this.iconName = "items/potion/icon";
        name = getName;
        description = getDescription;
        this.price = price;
        maxCharges = charges;
        invoke = initializeSkill;
        useMP = initializeUseMP;
        isAll = initializeSkill != null ? initializeSkill.getAll() : false;
        stackable = maxCharges <= 1;
        this.charges = maxCharges;
    }

    /**
     *
     * @param caster
     * @param target
     * @param allTargets
     * @return
     */
    public Action use(Battler caster,
            Battler target,
            ArrayList<Battler> allTargets) {
        return new Action(Battle.Command.Item,
                this,
                caster,
                target,
                allTargets);
    }

    /**
     *
     * @param caster
     * @param target
     * @return
     */
    public Action use(Battler caster,
            ArrayList<Battler> target) {
        return new Action(Battle.Command.Item,
                this,
                caster,
                target);
    }

    /**
     *
     * @return
     */
    public int getCharges() {
        return charges;
    }
    
    public int getPrice() {
        return price;
    }

    /**
     *
     * @param amount
     * @return
     */
    public Item setQuantity(int amount) {
        charges = amount;
        return this;
    }

    /**
     *
     * @param amount
     * @return
     */
    public Item increaseQuantity(int amount) {
        charges += amount;
        return this;
    }

    /**
     *
     * @param amount
     * @return
     */
    public Item decreaseQuantity(int amount) {
        Item thisItem = this;
        charges -= amount;
        if (amount <= 0) {
            thisItem = null;
        }
        return thisItem;
    }

    /**
     *
     * @return
     */
    public Skill getInvoke() {
        return invoke;
    }

    /**
     *
     * @return
     */
    public int getHPValue() {
        return HPValue;
    }

    /**
     *
     * @return
     */
    public int getMPValue() {
        return MPValue;
    }

    /**
     *
     * @return
     */
    public boolean isStackable() {
        return stackable;
    }

    /**
     *
     * @return
     */
    public boolean getAll() {
        return isAll;
    }

    /**
     *
     * @return
     */
    public boolean useMP() {
        return useMP;
    }

    /**
     *
     * @return
     */
    public boolean getAlly() {
        if (invoke == null) {
            return true;
        } else {
            return invoke.getAlly();
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Object clone() {
        Item cloned = new Item();
        cloned.name = this.name;
        cloned.description = this.description;
        cloned.price = this.price;
        cloned.HPValue = this.HPValue;
        cloned.MPValue = this.MPValue;
        cloned.stackable = this.stackable;
        cloned.isAll = this.isAll;
        cloned.invoke = this.invoke;

        cloned.useMP = this.useMP;
        cloned.maxCharges = this.maxCharges;
        cloned.charges = this.charges;
        return cloned;
    }

    @Override
    public boolean equals(Object o) {
        return o != null && ((Item) (o)).getName().equals(this.getName());
    }

    public void reset() {
        charges = 1;
    }

    public String getIcon() {
        return iconName;
    }

    public void setIcon(String iconName) {
        this.iconName = iconName;
    }
    
    
}
