/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.statusEffects;

import com.ark.darthsystem.Action;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.Player;
import com.ark.darthsystem.states.Battle;

/**
 *
 * @author keven
 */
public class Normal extends StatusEffect {
    
    public Normal() {
        super("Normal", 1, 1.0, 0.0, 0, false, "'s wounds are healed!");
        setInitialTurnCount(0);
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        return true;
    }

    @Override
    public String getDescription() {
        return "You're a normie.  Congratulations.";
    }
    
    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {

    }
    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {

    }
    
}
