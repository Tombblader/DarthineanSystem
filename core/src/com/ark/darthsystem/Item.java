package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Keven
 */
public class Item implements Serializable, Cloneable {

    private int HPValue;
    private int MPValue;
    private boolean expendable;
    private boolean isAll;
    private Skill invoke;
    private boolean useMP;
    private String name;
    private int quantity;

    /**
     *
     */
    public Item() {
    }

    /**
     *
     * @param getName
     * @param initializeExpendable
     * @param initializeHP
     * @param initializeMP
     * @param all
     */
    public Item(String getName,
            boolean initializeExpendable,
            int initializeHP,
            int initializeMP,
            boolean all) {
        name = getName;
        HPValue = initializeHP;
        MPValue = initializeMP;
        isAll = all;
        expendable = initializeExpendable;
        quantity = 1;
    }

    /**
     *
     * @param getName
     * @param initializeExpendable
     * @param initializeSkill
     * @param initializeUseMP
     */
    public Item(String getName,
            boolean initializeExpendable,
            Skill initializeSkill,
            boolean initializeUseMP) {
        name = getName;
        invoke = initializeSkill;
        useMP = initializeUseMP;
        isAll = initializeSkill != null ? initializeSkill.getAll() : false;
        expendable = initializeExpendable;
        quantity = 1;
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
    public int getQuantity() {
        return quantity;
    }

    /**
     *
     * @param amount
     * @return
     */
    public Item setQuantity(int amount) {
        quantity = amount;
        return this;
    }

    /**
     *
     * @param amount
     * @return
     */
    public Item increaseQuantity(int amount) {
        quantity += amount;
        return this;
    }

    /**
     *
     * @param amount
     * @return
     */
    public Item decreaseQuantity(int amount) {
        Item thisItem = this;
        quantity -= amount;
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
    public boolean getExpendable() {
        return expendable;
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
    public String getName() {
        return name;
    }

    public Object clone() {
        Item cloned = new Item();
        cloned.HPValue = this.HPValue;
        cloned.MPValue = this.MPValue;
        cloned.expendable = this.expendable;
        cloned.isAll = this.isAll;
        cloned.invoke = this.invoke;

        cloned.useMP = this.useMP;
        cloned.name = this.name;
        cloned.quantity = this.quantity;
        return cloned;
    }

    public boolean equals(Object o) {
        return o != null && ((Item) (o)).getName().equals(this.getName());
    }

    public void reset() {
        quantity = 1;
    }
}
