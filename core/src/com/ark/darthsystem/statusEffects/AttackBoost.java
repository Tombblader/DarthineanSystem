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
public class AttackBoost extends StatusEffect {

    public AttackBoost() {
        super("AttackBoost", 2, 2, -1, 5, 0, false, " received an attack boost!");
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        return true;
    }

    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {
    }

    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {

    }

    @Override
    public float getAttack() {
        return .3f;
    }

    @Override
    public String getDescription() {
        return "Increases attack slightly.";
    }

}
