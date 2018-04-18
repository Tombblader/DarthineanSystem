/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.statusEffects;

import com.ark.darthsystem.Action;
import static com.ark.darthsystem.BattleDriver.printline;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.Player;
import com.ark.darthsystem.states.Battle;

/**
 *
 * @author keven
 */
public class Poison extends StatusEffect {

    public Poison() {
        super("Poisoned", 3, .25, .1, 0, false, " has been poisoned!");
    }

    public boolean checkStatus(Action action, Battle b) {
        printline(action.getCaster().getName() + " takes " + (action.getCaster().getMaxHP() / 20) + " damage from the poison.");
        if (action.getCaster().changeHP(action.getCaster().getMaxHP() / 20)) {
            printline(action.getCaster().getName() + " has collapsed from the poison!");
            return false;
        }
    return true;
    }
    
    @Override
    public void checkFieldStatus(Player player, GameTimer timer) {

    }

    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {

    }
    
    
    @Override
    public String getDescription() {
        return "Victim takes damage each turn.";
    }

}
