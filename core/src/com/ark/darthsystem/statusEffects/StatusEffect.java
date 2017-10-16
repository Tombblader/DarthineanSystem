/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.statusEffects;

import com.ark.darthsystem.Battler;

/**
 *
 * @author keven
 */
public abstract class StatusEffect {

    private int priority;
    private double success;
    private double fade;
    private int turnCount;
    private double attackFade;
    private String message;

    public StatusEffect(int setPriority,
            double setSuccess,
            double setFade,
            int setTurnCount,
            double setAttackFade,
            String getMessage) {
        priority = setPriority;
        success = setSuccess;
        fade = setFade;
        turnCount = setTurnCount;
        attackFade = setAttackFade;
        message = getMessage;
    }
    
    public StatusEffect(int setPriority,
             double setSuccess,
             double setFade,
             int setTurnCount,
             String getMessage) {
         priority = setPriority;
         success = setSuccess;
         fade = setFade;
         turnCount = setTurnCount;
         attackFade = 0.0;
         message = getMessage;
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

    public boolean isSuccessful(Battler getCaster, Battler getTarget) {
        return (Math.random() <= success
                - ((getCaster.getLevel()
                - getTarget.getLevel())
                / (getCaster.getLevel()
                + getTarget.getLevel())
                + (getCaster.getMagic()
                - getTarget.getMagic())
                / (getCaster.getMagic()
                + getTarget.getMagic())));
    }

    public boolean faded(Battler getCaster) {
        return (turnCount != 0
                && (turnCount >= getTurnCount() - getCaster.getTurnCount()
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
}
