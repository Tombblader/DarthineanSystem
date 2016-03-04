package com.ark.darthsystem;

import java.util.Arrays;
import java.io.Serializable;

/**
 *
 * @author keven
 */
public class BattlerClass implements Serializable {

    /**
     *
     */
    public BattlerClass() {
    }

    /**
     *
     * @param initializeName
     * @param equipmentSet
     * @param skillSet
     */
    public BattlerClass(String initializeName, Equipment[] equipmentSet, Skill[] skillSet) {
        name = initializeName;
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
    public Skill[] getSkillList() {
        return skillList;
    }

    /**
     *
     * @param index
     * @return
     */
    public Skill getSkill(int index) {
        return skillList[index];
    }
    private String name;
    private Equipment[] equipmentList;
    private Skill[] skillList;
}
