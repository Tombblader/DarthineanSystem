package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.statusEffects.StatusEffect;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A discrete action that an AI would take, depending on the priority or
 * turn or other conditions.
 * @author Keven
 */
public class AI implements Serializable {

    /**
     * The type of the AI.  Flags for the BattlerAI to consider.
     */
    public enum Type {
        Attack, //Attack
        Nothing, //Do absolutely nothing
        Defend, //Defend
        Heal, //Prioritize healing
        Revive, //Marks this BattlerAI as capable of resurrection
        AttackSkill, //Prioritizes Attack Skills
        SupportSkill, //Prioritizes non-attack skills
        MercilessAttack, //Uses the most powerful attack skill available
        MercilessHeal, //Uses the best healing skill available
        Run; //Run away
    }
    
    //NO_FLAG means that the AI does not take this variable into consideration.
    private static final int NO_FLAG = -1; 
    private Type AIType;
    private float disengageChance;
    private int disengageTurn;
    private int priority;
    private double lowHP = NO_FLAG;
    private int turn = NO_FLAG;
    private int turnInterval = NO_FLAG;
    private boolean dead;
    private StatusEffect checkStatus;

    public AI(Type getType, int setPriority) {
        AIType = getType;
        priority = setPriority;
    }

    public AI(Type getType, float disengageChance, int disengageTurn) {
        AIType = getType;
        priority = 1;
        this.disengageChance = disengageChance;
        turnInterval = disengageTurn;
    }
    
    public Type getType() {
        return AIType;
    }
    
    public float getDisengageChance() {
        return disengageChance;
    }

    public float getDisengageTurn() {
        return disengageTurn;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int newPriority) {
        priority = newPriority;
    }

    public void setLowHP(double newLow) {
        lowHP = newLow;
    }

    public void setTurn(int newTurn) {
        turn = newTurn;
    }

    public void setDead(boolean newDead) {
        dead = newDead;
    }

    public void setStats(StatusEffect newStats) {
        checkStatus = newStats;
    }

    public boolean turnCondition(Battle b) {
        return !(turn == NO_FLAG &&
                turnInterval == NO_FLAG) &&
                (b.getTurnCount() == turn ||
                b.getTurnCount() % turnInterval == 0);
    }

    public boolean thereIsDead(Battle b) {
        boolean isDead = false;
        for (int i = 0; i < b.getAlly().size(); i++) {
            if (!b.getAlly(i).isAlive()) {
                isDead = true;
                break;
            }
        }
        return isDead && dead;
    }

    public boolean checkLowHP(Battle b) {
        boolean isLow = false;
        for (int i = 0; i < b.getAlly().size(); i++) {
            if (b.getAlly(i).getHP() / b.getAlly(i).getMaxHP() <= lowHP) {
                isLow = true;
                break;
            }
        }
        return (lowHP != (double) (NO_FLAG)) && isLow;
    }

    /**
     * Check if all battlers in a Battle State has this status.
     * @param b The Battle State to check.
     * @return True if the battlers is afflicted with the status associated with this action.
     */
    public boolean checkStatus(Battle b) {
        boolean isAfflicted = false;
        for (int i = 0; i < b.getAlly().size(); i++) {
            isAfflicted = !isAfflicted && b.getAlly(i).getAllStatus().contains(checkStatus);
            if (isAfflicted) {
                break;
            }
        }
        return isAfflicted && checkStatus != null;
    }

    /**
     * Checks if the action is worth using.
     * @param b The current battle context state.
     * @return If the action should be done or not.
     */
    public boolean worthUsing(Battle b) {
        boolean isUsable = checkStatus(b) ||
                checkLowHP(b) ||
                thereIsDead(b) ||
                turnCondition(b);
        isUsable = isUsable ||
                (checkStatus == null &&
                lowHP == (double) (NO_FLAG) &&
                !dead &&
                (turn == NO_FLAG &&
                turnInterval == NO_FLAG));
        return isUsable;
    }
    
    /**
     * Check if the HP is below a certain threshold.
     * @param b The target Battler.
     * @return true if the Battler's HP % is less than the AI data stored here.
     */
    public boolean checkLowHP(Battler b) {
        boolean isLow = b.getHP() / b.getMaxHP() <= lowHP;
        return (lowHP != (double) (NO_FLAG)) && isLow;
    }

    /**
     * Check if the battler has this status.
     * @param b The target Battler.
     * @return True if the battler is afflicted with the status associated with this action.
     */
    public boolean checkStatus(Battler b) {
        boolean isAfflicted =  b.getStatus(checkStatus);
        return isAfflicted && checkStatus != null;
    }

    /**
     * Checks if the action is worth using.
     * @param b The target Battler to check against.
     * @return If the action should be done or not.
     */
    public boolean worthUsing(Battler b) {
        boolean isUsable = checkStatus(b) || checkLowHP(b);
        isUsable = isUsable ||
                (checkStatus == null &&
                lowHP == (double) (NO_FLAG) &&
                !dead &&
                (turn == NO_FLAG &&
                turnInterval == NO_FLAG));
        return isUsable;
    }    
}
