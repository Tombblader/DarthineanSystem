package com.ark.darthsystem;

import java.util.Arrays;
import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author keven
 */
public class BattlerClass implements Serializable, Nameable {

    /**
     *
     */
    public BattlerClass() {
        skillList = new HashMap<>();
    }

    /**
     *
     * @param name
     * @param equipmentSet
     * @param skillSet
     */
    public BattlerClass(String name, Equipment[] equipmentSet, HashMap<Integer, Skill[]> skillSet) {
        this.name = name;
        equipmentList = equipmentSet;
        skillList = skillSet;
    }

    /**
     *
     * @param equipped
     * @return
     */
    public boolean equippable(Equipment equipped) {
        return equipmentList == null || Arrays.binarySearch(equipmentList, equipped) >= 0;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public HashMap<Integer, Skill[]> getSkillList() {
        return skillList;
    }

    /**
     *
     * @param index
     * @return
     */
    public Skill[] getSkill(int index) {
        return skillList.get(index);
    }
    private String name;
    private Equipment[] equipmentList;
    private HashMap<Integer, Skill[]> skillList;
}
