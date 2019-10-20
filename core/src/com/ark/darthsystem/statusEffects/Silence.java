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
public class Silence extends StatusEffect {

    public Silence() {
        super("Silence", 2, .25, 0.16, 6, false, "'s skills have been sealed!");
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        return !(action.getCommand() == Battle.Command.Skill);
    }

    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {
    }

    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {
        if (player.getCurrentBattler().getBattler().equals(battler)) {
            player.setCanSkill(false);
        } else {
            player.setCanSkill(true);
        }
    }

    @Override
    public String getDescription() {
        return "Victim cannot use skills.";
    }

}
