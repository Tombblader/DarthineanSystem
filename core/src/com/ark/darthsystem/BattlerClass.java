package com.ark.darthsystem;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author keven
 */
public class BattlerClass implements Serializable {

    private String name;
    private Equipment.Type[] equipmentList;
    private HashMap<Integer, Skill[]> skillList;
    private int[] growthList;

    /**
     *
     */
    public BattlerClass() {
        skillList = new HashMap<>();
        growthList = new int[]{0, 0, 0, 0, 0, 0};
    }

    /**
     *
     * @param name
     * @param equipmentSet
     * @param skillSet
     */
    public BattlerClass(String name, Equipment.Type[] equipmentSet, HashMap<Integer, Skill[]> skillSet) {
        this.name = name;
        equipmentList = equipmentSet;
        skillList = skillSet;
        growthList = new int[]{0, 0, 0, 0, 0, 0};
    }

    /**
     *
     * @param name
     * @param equipmentSet
     * @param skillSet
     */
    public BattlerClass(String name, String[] equipmentSet, HashMap<Integer, Skill[]> skillSet) {
        this.name = name;
        equipmentList = new Equipment.Type[equipmentSet.length];
        for (int i = 0; i < equipmentSet.length; i++) {
            equipmentList[i] = Equipment.Type.valueOf(equipmentSet[i].toUpperCase());
        }
        skillList = skillSet;
        growthList = new int[]{0, 0, 0, 0, 0, 0};
    }

    /**
     *
     * @param equipped
     * @return
     */
    public boolean equippable(Equipment equipped) {
        boolean equippable = false;
        for (Equipment.Type value : equipped.getType()) {
            for (Equipment.Type key : equipmentList) {
                equippable |= key == value;
            }
        }
        return equippable;
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
}
