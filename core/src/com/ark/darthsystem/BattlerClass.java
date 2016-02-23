package com.ark.darthsystem;

import java.util.Arrays;
import java.io.Serializable;

/**
 *
 * @author keven
 */
public class BattlerClass implements Serializable {

    public BattlerClass() {
    }

    public BattlerClass(String initializeName, Equipment[] equipmentSet, Skill[] skillSet) {
        name = initializeName;
        equipmentList = equipmentSet;
        skillList = skillSet;
    }

    public boolean equippable(Equipment equipped) {
        return equipmentList == null || Arrays.binarySearch(equipmentList, equipped) >= 0;
    }

    public String getName() {
        return name;
    }

    public Skill[] getSkillList() {
        return skillList;
    }

    public Skill getSkill(int index) {
        return skillList[index];
    }
    private String name;
    private Equipment[] equipmentList;
    private Skill[] skillList;
}
