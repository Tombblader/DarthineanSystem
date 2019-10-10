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
public class Sleep extends StatusEffect {

    public Sleep() {
        super("Sleep", 2, .2, .5, 4, true, " is asleep!");
        setInitialTurnCount(0);
    }

    public Sleep(int turn) {
        this();
        setInitialTurnCount(turn);
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Victim is asleep.  May wake up from a hit.";
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
