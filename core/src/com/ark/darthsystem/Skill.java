package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.statusEffects.Normal;
import com.ark.darthsystem.statusEffects.StatusEffect;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Keven
 */
public class Skill implements Serializable, Cloneable, Nameable {

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
    private String description;
    private int cost;
    private static final long serialVersionUID = 558633274;
//    private int level;
    private Battle.Element skillElement;
    private boolean isAlly;
    private boolean isAll;
    private transient StatusEffect statusEffect;

    public Skill() {

    }

    /**
     * A skill is a type of attack that has various parameters.
     * @param name The name of the Skill.
     * @param description A concise description of the Skill.
     * @param cost The MP cost of the Skill.
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
            String description,
            int cost,
            Battle.Element skillElement,
            boolean isAlly,
            boolean isAll,
            String statusEffect,
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
        this.description = description;
        this.cost = cost;
        this.skillElement = skillElement;
        this.isAlly = isAlly;
        this.isAll = isAll;
        try {
            this.statusEffect = (StatusEffect) Class.forName("com.ark.darthsystem.statusEffects." + statusEffect).newInstance();
        }
        catch (NullPointerException ex) {
            System.out.println(statusEffect + " not found.");
            this.statusEffect = new Normal();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
            ex.printStackTrace();
            this.statusEffect = new Normal();
        }
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
                ((int) 
                (((caster.getLevel() - target.getLevel()) * levelRatio) +
                ((caster.getHP() / caster.getMaxHP() * casterHP) +
                caster.getAttack() * casterAttack +
                caster.getDefense() * casterDefense +
                caster.getSpeed() * casterSpeed +
                caster.getMagic() * casterMagic) -
                (target.getAttack() * targetAttack +
                target.getDefense() * targetDefense +
                target.getSpeed() * targetSpeed +
                target.getMagic() * targetMagic)) /
                finalizeRatio *
                (this.getElement() == target.getElement().getWeakness() ? 2 : 1) *
                (caster.getEquipment(Equipment.Slot.MainHand.getSlot()) != null &&
                caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() != Battle.Element.Physical &&
                this.getElement() == caster.getElement() ? 1.5 : 1) * (this.getElement() != Battle.Element.Physical &&
                this.getElement() == target.getElement() ? -1 : 1) * (caster.getEquipment(Equipment.Slot.MainHand.getSlot()) != null &&
                caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() != Battle.Element.Physical &&
                target.getEquipment(Equipment.Slot.OffHand.getSlot()) != null &&
                this.getElement() == target.getEquipment(Equipment.Slot.OffHand.getSlot()).getElement() ? .5 : 1)) * (.9 + Math.random() * .25));
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
     * Overrides the MP cost of the skill.  Unlike setCost,
     * overrideCost returns a new Skill object with the new cost.
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

//    /**
//     * Gets the minimum level requirement to use this skill.
//     * @return The minimum level requirement.
//     */
//    public int getLevel() {
//        return level;
//    }
//
    /**
     * Determines whether a status change is successful and changes the status.
     * @param caster The battler who used this Skill.
     * @param target The target who the Skill is being used on.
     * @return The string that notes the results of the attempted status change.
     */
    public String changeStatus(Battler caster, Battler target) {
        String message;
        if (target.getStatus(0).getPriority() < statusEffect.getPriority() &&
                statusEffect.isSuccessful(caster, target) &&
                !(getElement() == Battle.Element.Heal)) {
            target.changeStatus((StatusEffect) statusEffect.clone());
            message = target.getName() + statusEffect.getMessage();
        } else if ((target.getStatus(statusEffect)) && getElement() == Battle.Element.Heal) {
            message = target.getName() + (target.getStatus("Death") ? " has returned to life!" : "'s " + statusEffect.getName() + " has been removed!");
            if (statusEffect instanceof Normal) {
                message = target.getName() + statusEffect.getMessage() + "";
            } else {
                target.getAllStatus().remove(statusEffect);
            }
           if (target.getAllStatus().isEmpty()) {
               target.changeStatus(new Normal());
           }
        } else {
            message = (finalizeRatio > 0.0) ? "" : "But nothing happens!";
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
    public StatusEffect getStatusEffect() {
        return statusEffect;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Skill other = (Skill) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }    
    
    private void readObject(ObjectInputStream in) throws IOException,ClassNotFoundException {
        in.defaultReadObject();
        String afflicted = (String) in.readObject();
        try {
            StatusEffect temp = (StatusEffect) Class.forName("com.ark.darthsystem.statusEffects." + afflicted).newInstance();
            statusEffect = temp;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Battler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(statusEffect.getName());
    }    

}
