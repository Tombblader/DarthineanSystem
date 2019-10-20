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
        super("Poison", 2, .25, .1, 0, false, " has been poisoned!");
    }

    public Poison(int turn) {
        this();
        setInitialTurnCount(turn);
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        printline(action.getCaster().getName() + " takes " + (action.getCaster().getMaxHP() / 33) + " damage from the poison.");
        if (action.getCaster().changeHP(action.getCaster().getMaxHP() / 33)) {
            printline(action.getCaster().getName() + " has collapsed from the poison!");
            return false;
        }
        return true;
    }

    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {
        printline(battler.getName() + " takes " + (battler.getMaxHP() / 33) + " damage from the poison.");
        if (battler.changeHP(battler.getMaxHP() / 33)) {
            printline(battler.getName() + " has collapsed from the poison!");
        }
    }

    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {

    }

    @Override
    public String getDescription() {
        return "Victim takes damage each turn.";
    }

}
