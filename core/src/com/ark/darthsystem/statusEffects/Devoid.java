/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class Devoid extends StatusEffect {

    public Devoid() {
        super("Devoid", 2, .25, .1, 0, false, " has been corrupted...");
    }

    public Devoid(int turn) {
        this();
        setInitialTurnCount(turn);
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
        printline(action.getCaster().getName() + " takes " + (action.getCaster().getMaxHP() / 6) + " damage from the corruption.");
        if (action.getCaster().changeHP(action.getCaster().getMaxHP() / 6)) {
            printline(action.getCaster().getName() + " has collapsed from the corruption.");
            return false;
        }
        return true;
    }

    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {
        printline(battler.getName() + " takes " + (battler.getMaxHP() / 6) + " damage from the corruption.");
        if (battler.changeHP(battler.getMaxHP() / 6)) {
            printline(battler.getName() + " has been fully corrupted.");
        }
    }

    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {

    }

    @Override
    public String getDescription() {
        return "Victim takes heavy damage each turn and becomes Dark element.";
    }

}
