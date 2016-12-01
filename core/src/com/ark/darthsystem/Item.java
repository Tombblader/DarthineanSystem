package com.ark.darthsystem;

import com.badlogic.gdx.utils.Pool.Poolable;
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
    }

    /**
     *
     * @param getName
     * @param initializeExpendable
     * @param initializeSkill
     * @param initializeUseMP
     */
    public Item(String getName) {
        name = getName;
        quantity = 1;
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
