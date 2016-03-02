package com.ark.darthsystem;

import com.ark.darthsystem.States.Battle;

import java.io.Serializable;

public class Skill implements Serializable, Cloneable {

    private int base;
    private double levelRatio;
    private double casterHP;
//  private double casterMP;
    private int casterAttack;
    private int casterDefense;
    private int casterSpeed;
    private int casterMagic;
    private double targetHP;
//  private double targetMP;
    private double targetAttack;
    private double targetDefense;
    private double targetSpeed;
    private double targetMagic;
    private double finalizeRatio;
    private String name;
    private int cost;
    private int level;
    private Battle.Element skillElement;
    private String Target;
    private boolean isAlly;
    private boolean isAll;
//  private boolean[] skillStats;
    private Battle.Stats statusEffect;

    public Skill() {

    }

    public Skill(String initializeName,
            int initializeLevel,
            int initializeCost,
            Battle.Element initializeElement,
            boolean initializeAlly,
            boolean initializeAll,
            Battle.Stats initializeStats,
            int initializeBase,
            double levelDifference,
            double initializeHP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic,
            int initializeTargetAttack,
            int initializeTargetDefense,
            int initializeTargetSpeed,
            int initializeTargetMagic,
            double ratio) {
        name = initializeName;
        level = initializeLevel;
        cost = initializeCost;
        skillElement = initializeElement;
        isAlly = initializeAlly;
        isAll = initializeAll;
        statusEffect = initializeStats;
        levelRatio = levelDifference;
        casterHP = initializeHP;
        base = initializeBase;
        casterAttack = initializeAttack;
        casterDefense = initializeDefense;
        casterSpeed = initializeSpeed;
        casterMagic = initializeMagic;
        targetAttack = initializeTargetAttack;
        targetDefense = initializeTargetDefense;
        targetSpeed = initializeTargetSpeed;
        targetMagic = initializeTargetMagic;
        finalizeRatio = ratio;
    }

    public int calculateDamage(Battler caster, Battler target) {
        return (int) (base +
                ((int) (((caster.getLevel() -
                target.getLevel()) *
                levelRatio) +
                (((caster.getHP() /
                caster.getMaxHP() *
                casterHP) +
                caster.getAttack() *
                casterAttack +
                caster.getDefense() *
                casterDefense +
                caster.getSpeed() *
                casterSpeed +
                caster.getMagic() *
                casterMagic) -
                (target.getAttack() *
                targetAttack +
                target.getDefense() *
                targetDefense +
                target.getSpeed() *
                targetSpeed +
                target.getMagic() *
                targetMagic)) /
                finalizeRatio *
                (this.getElement() == target.getElement().getWeakness() ? 2 : 1) *
                (caster.getEquipment(Equipment.EquipmentType.RightArm.getSlot()) != null &&
                caster.getEquipment(Equipment.EquipmentType.RightArm.getSlot()).getElement() != Battle.Element.Physical &&
                this.getElement() == caster.getElement() ? 1.5 : 1) * (this.getElement() != Battle.Element.Physical &&
                this.getElement() == target.getElement() ? -1 : 1) * (caster.getEquipment(Equipment.EquipmentType.RightArm.getSlot()) != null &&
                caster.getEquipment(Equipment.EquipmentType.RightArm.getSlot()).getElement() != Battle.Element.Physical &&
                target.getEquipment(Equipment.EquipmentType.LeftArm.getSlot()) != null &&
                this.getElement() == target.getEquipment(Equipment.EquipmentType.LeftArm.getSlot()).getElement() ? .5 : 1)) * (.9 + Math.random() * .25)));
    }

    public int getCost() {
        return cost;
    }

    public Skill overrideLevel(int newLevel) {
        Skill newSkill = null;
        try {
            newSkill = (Skill) (this.clone());
            newSkill.level = newLevel;
        } catch (Exception e) {
        }
        return newSkill;
    }

    public int getLevel() {
        return level;
    }

    public String changeStatus(Battler caster,
            Battler target,
            int turnCount) {
        String message = "";
        if (target.getStatus().getPriority() < statusEffect.getPriority() &&
                statusEffect.isSuccessful(caster, target) &&
                !(getElement() == Battle.Element.Heal)) {
            target.changeStatus(statusEffect, turnCount);
            message = target.getName() + statusEffect.getMessage() + "\n";
        } else if ((target.getStatus() == statusEffect) &&
                getElement() == Battle.Element.Heal) {
            message = target.getName() +
                    (target.getStatus() == Battle.Stats.Death ? " has returned to life!\n" : "'s status has returned to normal!\n");
            if (statusEffect == Battle.Stats.Normal) {
                message = target.getName() + statusEffect.getMessage() + "\n";
            }
            target.changeStatus(Battle.Stats.Normal, turnCount);
        } else {
            message = (finalizeRatio > 0.0) ? "" : "But nothing happens! \n";
        }
        return message;
    }

    public String getName() {
        return name;
    }

    public boolean getAlly() {
        return isAlly;
    }

    public boolean getAll() {
        return isAll;
    }

    public Battle.Element getElement() {
        return skillElement;
    }

    public Battle.Stats getStatusEffect() {
        return statusEffect;
    }

}
