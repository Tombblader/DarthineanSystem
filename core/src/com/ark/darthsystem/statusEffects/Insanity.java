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
public class Insanity extends StatusEffect {

    public Insanity() {
        super("Insanity", 2, .25, 0.15, 5, 0.25, false, " has become insane...");
        setInitialTurnCount(0);
    }

    public Insanity(int turn) {
        this();
        setInitialTurnCount(turn);
    }

    @Override
    public boolean checkStatus(Action action, Battle b) {
//        action
        action.setTarget(Math.random() > .5 ? b.getAlly((int) (Math.random()
                * b.getAlly().size())) : b.getAlly((int) (Math.random()
                * b.getAlly().size())));
        action.setAllTarget(Math.random() > .5 ? b.getAlly() : (b.getEnemy()));
        action.setSkill(action.getCaster().getSkill((int) (Math.random() * action.getCaster().getSkillList().size())));
        action.setCommand(Math.random() > .5 ? Battle.Command.Attack : Battle.Command.Skill);
        return true;
    }

    @Override
    public String getDescription() {
        return "Victiom acts irrationally.";
    }

    @Override
    public void checkFieldStatus(Player player, Battler battler, GameTimer timer) {

    }

    @Override
    public void updateFieldStatus(Player player, Battler battler, GameTimer timer, float delta) {
        int random = ((int) (Math.random() * 2)) - 1;
        player.getMainBody()
                .setLinearVelocity(random * player.getSpeed() * (float) (delta),
                        player.getMainBody().getLinearVelocity().y);
        player.changeX(random);
        random = ((int) (Math.random() * 2)) - 1;
        player.getMainBody().setLinearVelocity(player.getMainBody().getLinearVelocity().x, random * player.getSpeed() * (float) (delta));
        player.changeY(random);

    }
}
