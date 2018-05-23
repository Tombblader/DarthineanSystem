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
public class Blind extends StatusEffect {
    public Blind() {
        super("Blind", 2, .5, .15, 7, 0.2, false, " is blinded!");
    }    
    
    @Override
    public boolean checkStatus(Action action, Battle b) {
        return true;
    }    

    @Override
    public String getDescription() {
        return "Victim has a 50% miss chance, and a -50% dodge chance";
    }
    
    @Override
    public float getAccuracy() {
        return .5f;
    }

    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {

    }
    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {

    }
}
