package com.ark.darthsystem;

import com.ark.darthsystem.States.Battle;
import java.io.Serializable;
import java.util.ArrayList;

public class Item implements Serializable, Cloneable {

    private int HPValue;
    private int MPValue;
    private boolean expendable;
    private boolean isAll;
    private Skill invoke;
    private boolean useMP;
    private String name;
    private int quantity;

    public Item() {
    }

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

    public Action use(Battler caster,
            Battler target,
            ArrayList<Battler> allTargets) {
        return new Action(Battle.Command.Item,
                this,
                caster,
                target,
                allTargets);
    }

    public Action use(Battler caster,
            ArrayList<Battler> target) {
        return new Action(Battle.Command.Item,
                this,
                caster,
                target);
    }

    public int getQuantity() {
        return quantity;
    }

    public Item setQuantity(int amount) {
        quantity = amount;
        return this;
    }

    public Item increaseQuantity(int amount) {
        quantity += amount;
        return this;
    }

    public Item decreaseQuantity(int amount) {
        Item thisItem = this;
        quantity -= amount;
        if (amount <= 0) {
            thisItem = null;
        }
        return thisItem;
    }

    public Skill getInvoke() {
        return invoke;
    }

    public int getHPValue() {
        return HPValue;
    }

    public int getMPValue() {
        return MPValue;
    }

    public boolean getExpendable() {
        return expendable;
    }

    public boolean getAll() {
        return isAll;
    }

    public boolean useMP() {
        return useMP;
    }

    public boolean getAlly() {
        if (invoke == null) {
            return true;
        } else {
            return invoke.getAlly();
        }
    }

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
}
