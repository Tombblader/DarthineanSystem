package com.ark.darthsystem.statusEffects;

import com.ark.darthsystem.Action;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.Player;
import com.ark.darthsystem.states.Battle;

public class Petrify extends StatusEffect {

    public Petrify() {
        super("Petrify", 7, .10, 0.0, 0, true, " is petrified!");
        setInitialTurnCount(0);
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Victim is a lifeless statue.  Victim cannot act until cured.";
    }

    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {

    }

    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {
        if (player.getCurrentBattler().getBattler().equals(battler)) {
            player.disableMovement();
        } else {
            player.enableMovement();
        }

    }

}
