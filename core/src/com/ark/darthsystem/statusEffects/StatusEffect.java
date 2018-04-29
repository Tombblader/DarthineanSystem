/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.statusEffects;

import com.ark.darthsystem.Action;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Nameable;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.Player;
import com.ark.darthsystem.states.Battle;
import java.util.Objects;

/**
 *
 * @author keven
 */
public abstract class StatusEffect implements Nameable, Cloneable {
    
    private String name;
    private int priority;
    private double success;
    private double fade;
    private int turnCount;
    private double attackFade;
    private String message;
    private boolean restrictMove;
    private int initialTurnCount;

    public StatusEffect(String name, int setPriority,
            double setSuccess,
            double setFade,
            int setTurnCount,
            double setAttackFade,
            boolean restrictMove,
            String getMessage) {
        this.name = name;
        priority = setPriority;
        success = setSuccess;
        fade = setFade;
        turnCount = setTurnCount;
        attackFade = setAttackFade;
        this.restrictMove = restrictMove;
        message = getMessage;
        initialTurnCount = 0;
    }
    
    public StatusEffect(String name, int setPriority,
             double setSuccess,
             double setFade,
             int setTurnCount,
             boolean restrictMove,
             String getMessage) {
        this.name = name;
        priority = setPriority;
        success = setSuccess;
        fade = setFade;
        turnCount = setTurnCount;
        attackFade = 0.0;
        this.restrictMove = restrictMove;
        message = getMessage;
        initialTurnCount = 0;
     }
       
    public int getPriority() {
        return priority;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public double getFade() {
        return fade;
    }

    public double getAttackFade() {
        return attackFade;
    }

    public boolean isSuccessful(Battler caster, Battler target) {
        return (Math.random() <= success
                - ((caster.getLevel()
                - target.getLevel())
                / (caster.getLevel()
                + target.getLevel())
                + (caster.getMagic()
                - target.getMagic())
                / (caster.getMagic()
                + target.getMagic())));
    }

    public boolean faded(Battler caster, int currentTurn) {
        return (turnCount != 0
                && (turnCount >= currentTurn - getInitialTurnCount()
                || (Math.random() <= fade //                    - (1.0 / (101.1 - getCaster.getLevel())
                //                    / (getCaster.getDefense() - getCaster.getLevel())
                //                    / (getCaster.getMagic() - getCaster.getLevel()))
                )));
    }

    public boolean attackFaded() {
        return (attackFade != 0.0 && Math.random() < attackFade);
    }

    public String getMessage() {
        return message;
    }
    
    public int getInitialTurnCount() {
        return initialTurnCount;
    }
    
    public final void setInitialTurnCount(int turnCount) {
        initialTurnCount = turnCount;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    public boolean canMove() {
        return !restrictMove;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.name);
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
        final StatusEffect other = (StatusEffect) obj;
        return Objects.equals(this.name, other.name);
    }
    
    public abstract boolean checkStatus(Action action, Battle b);

    public abstract void checkFieldStatus(Player player, GameTimer timer);
    
    public abstract void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta);

    @Override
    public Object clone() throws CloneNotSupportedException {
        return (StatusEffect) super.clone(); 
    }
    
}
