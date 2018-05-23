/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.graphics.ActorSprite.SpriteModeFace;
import com.badlogic.gdx.graphics.g2d.Animation;

import java.util.ArrayList;

/**
 * The ActorBattler encapsulates the Battler so it can interact with the map.
 * @author Keven
 */
public class ActorBattler {

    private Battler battler;
    private ActorSkill currentSkill;
    private transient ArrayList<ActorSkill> skillList = new ArrayList<>();
    private transient ActorSprite spriteSheet;
    private String spriteSheetName;
    private SpriteModeFace face = SpriteModeFace.NORMAL;

    public ActorBattler(Battler b, ActorSprite s) {
        battler = b;
        spriteSheet = s;
        b.getSkillList().stream().map((skill) -> {
            currentSkill = Database2.SkillToActor(skill);
            return skill;
        }).filter((_item) -> (currentSkill != null)).forEachOrdered((_item) -> {
            skillList.add(currentSkill);
        });
        if (skillList != null && !skillList.isEmpty()) {
            currentSkill = skillList.get(0);
        } else {
            currentSkill = null;
        }
    }
    
    public ActorBattler(Battler b, String s) {
        battler = b;
        spriteSheetName = s;
        initialize();
    }
    
    private void initialize() {
        spriteSheet = new ActorSprite(spriteSheetName);
        battler.getSkillList().stream().map((skill) -> {
            currentSkill = Database2.SkillToActor(skill);
            return skill;
        }).filter((_item) -> (currentSkill != null)).forEachOrdered((_item) -> {
            skillList.add(currentSkill);
        });
        if (skillList != null && !skillList.isEmpty()) {
            currentSkill = skillList.get(0);
        } else {
            currentSkill = null;
        }
        
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
        return new ActorBattler((Battler) battler.clone(), spriteSheet);
    }
    
    public <T extends Battler> T getBattler() {
        return (T) battler;
    }
    
    public ActorSkill getCurrentSkill() {
        return currentSkill;
    }
    
    public ArrayList<ActorSkill> getSkillList() {
        ArrayList<ActorSkill> tempSkillList = new ArrayList<>();
        for (Skill skill : battler.getSkillList()) {
            ActorSkill tempSkill = Database2.SkillToActor(skill);
            if (tempSkill != null) {
                tempSkillList.add(tempSkill);
            }
        }
        return tempSkillList;
    }
    
    public ActorSprite getSprite() {
        return spriteSheet;
    }
    
    public void setFace(SpriteModeFace face) {
        this.face = face;
    }

    public Animation getFace() {
        return spriteSheet.getFaceAnimation(face);
    }
    
}
