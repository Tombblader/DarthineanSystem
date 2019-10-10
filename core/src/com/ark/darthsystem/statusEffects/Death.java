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
public class Death extends StatusEffect {

    public Death() {
        super("Death", 8, .10, 0.0, 0, true, " has instantly died!");
        setInitialTurnCount(0);
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Victim has 0 HP and thus is a lifeless pile of flesh.  Victim cannot act until revived.";
    }

    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {

    }

    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {

    }

}
