/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Skill;

import java.util.ArrayList;

/**
 * The ActorBattler encapsulates the Battler so it can interact with the map.
 * @author Keven
 */
public class ActorBattler {

    private Battler battler;
    private ActorSkill currentSkill;
    private ArrayList<ActorSkill> skillList = new ArrayList<>();
    private ActorSprite spriteSheet;

    public ActorBattler(Battler b, ActorSprite s) {
        battler = b;
        spriteSheet = s;
        for (Skill skill : b.getSkillList()) {
            currentSkill = Database2.SkillToActor(skill);
            if (currentSkill != null) {
                skillList.add(currentSkill);
            }
        }
        currentSkill = skillList.get(0);
    }

    public ActorSkill activateCurrentSkill() {
        if (battler.getMP() >= currentSkill.getSkill().getCost()) {
            battler.changeMP(currentSkill.getSkill().getCost());
            return currentSkill;
        }
        return null;
    }

    public ActorSkill activateCurrentSkill(Player p) {
        if (battler.getMP() >= currentSkill.getSkill().getCost()) {
            battler.changeMP(currentSkill.getSkill().getCost());
            currentSkill.setInvoker(p);
//            currentSkill.playFieldSound();
            return currentSkill;
        }
        return null;
    }
    
    public final ActorSkill activateSkill(String s) {
        if (battler.getMP() >= battler.getSkill(s).getCost()) {
            battler.changeMP(battler.getSkill(s).getCost());
            return currentSkill;
        }
        return null;
    }

    public final ActorSkill activateSkill(String s, Player p) {
        if (battler.getMP() >= battler.getSkill(s).getCost()) {
            battler.changeMP(battler.getSkill(s).getCost());
            currentSkill.setInvoker(p);
            currentSkill.playFieldSound();
            return currentSkill;
        }
        return null;
    }

    public void changeSkill(int direction) {
        int currentIndex = getSkillList().indexOf(currentSkill);
        currentIndex += direction;
        if (currentIndex >= getSkillList().size()) {
            currentIndex = 0;
        }
        if (currentIndex < 0) {
            currentIndex = getSkillList().size() - 1;
        }
        currentSkill = getSkillList().get(currentIndex);
    }


    public ActorBattler clone() {
        return new ActorBattler(battler.clone(), spriteSheet);
    }
    
    public Battler getBattler() {
        return battler;
    }
    
    public ActorSkill getCurrentSkill() {
        return currentSkill;
    }
    
    public ArrayList<ActorSkill> getSkillList() {
        ArrayList<ActorSkill> tempSkillList = new ArrayList<>();
        for (ActorSkill a : skillList) {
            if (a.getSkill().getLevel() <= battler.getLevel()) {
                tempSkillList.add(a);
            }
        }
        return tempSkillList;
    }
    
    public ActorSprite getSprite() {
        return spriteSheet;
    }

}
