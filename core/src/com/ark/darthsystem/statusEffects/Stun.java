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
public class Stun extends StatusEffect {

    public Stun() {
        super("Stun", 2, .5, 0, 1, true, " is stunned!");
        setInitialTurnCount(0);
    }

    public Stun(int turn) {
        this();
        setInitialTurnCount(turn);
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        return false;
    }

    @Override
    public String getDescription() {
        return "Victim can't move for a single turn.";
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
