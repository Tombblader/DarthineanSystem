package com.ark.darthsystem;

import com.ark.darthsystem.States.Battle;
import java.io.Serializable;

/**
 *
 * @author Keven
 */
public class AI implements Serializable {

    /**
     *
     */
    public enum Type {
        Attack,
        Nothing,
        Defend,
        Heal,
        Revive,
        AttackSkill,
        SupportSkill,
        MercilessAttack,
        MercilessHeal,
        Run;
    }
    private static final int NO_FLAG = -1;
    private Type AIType;
    private float disengageChance;
    private int disengageTurn;
    private int priority;
    private double lowHP = NO_FLAG;
    private int turn = NO_FLAG;
    private int turnInterval = NO_FLAG;
    private boolean dead;
    private Battle.Stats checkStatus;

    AI(Type getType, int setPriority) {
        AIType = getType;
        priority = setPriority;
    }

    AI(Type getType, float disengageChance, int disengageTurn) {
        AIType = getType;
        priority = 1;
        this.disengageChance = disengageChance;
        turnInterval = disengageTurn;
//        this.disengageTurn = disengageTurn;
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

    int getPriority() {
        return priority;
    }

    void setPriority(int newPriority) {
        priority = newPriority;
    }

    void setLowHP(double newLow) {
        lowHP = newLow;
    }

    void setTurn(int newTurn) {
        turn = newTurn;
    }

    void setDead(boolean newDead) {
        dead = newDead;
    }

    void setStats(Battle.Stats newStats) {
        checkStatus = newStats;
    }

    boolean turnCondition(Battle b) {
        return !(turn == NO_FLAG &&
                turnInterval == NO_FLAG) &&
                (b.getTurnCount() == turn ||
                b.getTurnCount() % turnInterval == 0);
    }

    boolean thereIsDead(Battle b) {
        boolean isDead = false;
        for (int i = 0; i < b.getAlly().size(); i++) {
            if (!b.getAlly(i).isAlive()) {
                isDead = true;
                break;
            }
        }
        return isDead && dead;
    }

    boolean checkLowHP(Battle b) {
        boolean isLow = false;
        for (int i = 0; i < b.getAlly().size(); i++) {
            if (b.getAlly(i).getHP() / b.getAlly(i).getMaxHP() <= lowHP) {
                isLow = true;
                break;
            }
        }
        return (lowHP != (double) (NO_FLAG)) && isLow;
    }

    boolean checkStatus(Battle b) {
        boolean isAfflicted = false;
        for (int i = 0; i < b.getAlly().size(); i++) {
            isAfflicted = !isAfflicted && b.getAlly(i).getStatus() == checkStatus;
            if (isAfflicted) {
                break;
            }
        }
        return isAfflicted && checkStatus != null;
    }

    boolean worthUsing(Battle b) {
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
}
