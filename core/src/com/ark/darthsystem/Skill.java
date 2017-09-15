package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;

import java.io.Serializable;

/**
 *
 * @author Keven
 */
public class Skill implements Serializable, Cloneable {

    private int base;
    private double levelRatio;
    private double casterHP;
    private int casterAttack;
    private int casterDefense;
    private int casterSpeed;
    private int casterMagic;
    private double targetHP;
    private double targetAttack;
    private double targetDefense;
    private double targetSpeed;
    private double targetMagic;
    private double finalizeRatio;
    private String name;
    private int cost;
    private int level;
    private Battle.Element skillElement;
    private boolean isAlly;
    private boolean isAll;
    private Battle.Stats statusEffect;

    public Skill() {

    }

    /**
     * A skill is a type of attack that has various parameters.
     * @param name The name of the Skill
     * @param level The minimum level required to learn the skill
     * @param cost The MP cost of the Skill
     * @param skillElement The Element of the skill.
     * @param isAlly If true, affects allies instead of enemies.
     * @param isAll If true, targets all enemies or allies.
     * @param statusEffect The status effect to inflict.  Normal does not inflict a status effect.
     * @param base The base value of the skill.
     * @param levelRatio How much the level difference affects the skill.
     * @param casterHP How much influence the caster's HP difference affects the skill.
     * @param casterAttack How much influence the caster's Attack affects the skill.
     * @param casterDefense How much influence the caster's Defense affects the skill.
     * @param casterSpeed How much influence the caster's Speed affects the skill.
     * @param casterMagic How much influence the caster's Magic affects the skill.
     * @param targetAttack How much influence the target's Attack affects the skill.
     * @param targetDefense How much influence the target's Defense affects the skill.
     * @param targetSpeed How much influence the target's Speed affects the skill.
     * @param targetMagic How much influence the target's Magic affects the skill.
     * @param finalizeRatio Take all of these values and divide it by what to equalize.
     */
    public Skill(String name,
            int level,
            int cost,
            Battle.Element skillElement,
            boolean isAlly,
            boolean isAll,
            Battle.Stats statusEffect,
            int base,
            double levelRatio,
            double casterHP,
            int casterAttack,
            int casterDefense,
            int casterSpeed,
            int casterMagic,
            int targetAttack,
            int targetDefense,
            int targetSpeed,
            int targetMagic,
            double finalizeRatio) {
        this.name = name;
        this.level = level;
        this.cost = cost;
        this.skillElement = skillElement;
        this.isAlly = isAlly;
        this.isAll = isAll;
        this.statusEffect = statusEffect;
        this.levelRatio = levelRatio;
        this.casterHP = casterHP;
        this.base = base;
        this.casterAttack = casterAttack;
        this.casterDefense = casterDefense;
        this.casterSpeed = casterSpeed;
        this.casterMagic = casterMagic;
        this.targetAttack = targetAttack;
        this.targetDefense = targetDefense;
        this.targetSpeed = targetSpeed;
        this.targetMagic = targetMagic;
        this.finalizeRatio = finalizeRatio;
    }

    /**
     * Calculates the damage dealt to a target.
     * @param caster The battler using the skill.
     * @param target The battler being the skill is being used on.
     * @return The damage that should be dealt to the target.
     */
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
                (caster.getEquipment(Equipment.EquipmentType.MainHand.getSlot()) != null &&
                caster.getEquipment(Equipment.EquipmentType.MainHand.getSlot()).getElement() != Battle.Element.Physical &&
                this.getElement() == caster.getElement() ? 1.5 : 1) * (this.getElement() != Battle.Element.Physical &&
                this.getElement() == target.getElement() ? -1 : 1) * (caster.getEquipment(Equipment.EquipmentType.MainHand.getSlot()) != null &&
                caster.getEquipment(Equipment.EquipmentType.MainHand.getSlot()).getElement() != Battle.Element.Physical &&
                target.getEquipment(Equipment.EquipmentType.OffHand.getSlot()) != null &&
                this.getElement() == target.getEquipment(Equipment.EquipmentType.OffHand.getSlot()).getElement() ? .5 : 1)) * (.9 + Math.random() * .25)));
    }

    /**
     * Get the MP cost of the skill.
     * @return The MP cost of the skill
     */
    public int getCost() {
        return cost;
    }
    
    /**
     * Overrides the MP cost of the skill.
     * @param cost The new MP cost of the skill.
     */
    public void setCost(int cost) {
        this.cost = cost;
    }

    /**
     * Overrides the level requirement to use this skill.
     * @param newLevel The new minimum level.
     * @return A copy of the skill that has this new level requirement.
     */
    public Skill overrideLevel(int newLevel) {
        Skill newSkill = null;
        try {
            newSkill = (Skill) (this.clone());
            newSkill.level = newLevel;
        } catch (Exception e) {
        }
        return newSkill;
    }
    
    /**
     * Overrides the MP cost of the skill.  Unlike setCost,
     * @param newCost The new MP cost of the skill.
     * @return A copy of the skill that has the new cost.
     * @see #setCost(int)
     */
    public Skill overrideCost(int newCost) {
        Skill newSkill = null;
        try {
            newSkill = (Skill) (this.clone());
            newSkill.cost = newCost;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return newSkill;
    }

    /**
     * Gets the minimum level requirement to use this skill.
     * @return The minimum level requirement.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Determines whether a status change is successful and changes the status.
     * @param caster The battler who used this Skill.
     * @param target The target who the Skill is being used on.
     * @param turnCount The current amount of turns elapsed this battle.
     * @return The string that notes the results of the attempted status change.
     */
    public String changeStatus(Battler caster,
            Battler target,
            int turnCount) {
        String message;
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
                message = target.getName() + statusEffect.getMessage() + "";
            }
            target.changeStatus(Battle.Stats.Normal, turnCount);
        } else {
            message = (finalizeRatio > 0.0) ? "" : "But nothing happens! \n";
        }
        return message;
    }

    /**
     * Gets the name of the skill.
     * @return The name of the Skill.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if this skill affects allies.
     * @return True if it affects allies, false if it does not.
     */
    public boolean getAlly() {
        return isAlly;
    }

    /**
     * Checks if this skill affects all on a side.
     * @return True if it affects all, false if it does not.
     */
    public boolean getAll() {
        return isAll;
    }

    /**
     * Gets the Element of the skill.
     * @return The element of the skill.
     */
    public Battle.Element getElement() {
        return skillElement;
    }

    /**
     * Gets the status effect that the skill has a chance of inflicting.
     * @return The status effect of the skill.
     */
    public Battle.Stats getStatusEffect() {
        return statusEffect;
    }

}
