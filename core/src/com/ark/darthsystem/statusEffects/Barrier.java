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
public class Barrier extends StatusEffect {

    public Barrier() {
        super("Barrier", 2, 2, -1, 10, .1, false, " is covered by a barrier.");
    }

    @Override
    public float getDamageModifier() {
        return -0.5f;
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
    public String getDescription() {
        return "Halves damage.  May break when hit with an attack.";
    }

}
