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
import java.io.Serializable;

import java.util.ArrayList;

/**
 * The ActorBattler encapsulates the Battler so it can interact with the map.
 * @author Keven
 */
public class ActorBattler implements Serializable {

    private Battler battler;
    private transient FieldSkill currentSkill;
    private transient ArrayList<FieldSkill> skillList = new ArrayList<>();
    private transient ActorSprite spriteSheet;
    private String spriteSheetName;
    private transient SpriteModeFace face = SpriteModeFace.NORMAL;
    private float delay;
    private float speed;
    private String shapeName;

    public ActorBattler(Battler b, ActorSprite s) {
        battler = b;
        spriteSheet = s;
        speed = .4f;
        delay = 1/6f;
        shapeName = "basiccircle";
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
        speed = .4f;
        delay = 1/6f;
        shapeName = "basiccircle";
        initialize();
    }
    
    public ActorBattler(Battler b, String s, float fps, String shapeName, float speed) {
        this.shapeName = shapeName;
        battler = b;
        spriteSheetName = s;
        this.speed = speed;
        delay = fps;
        initialize();        
    }
    
    private void initialize() {
        spriteSheet = new ActorSprite(spriteSheetName);
        for (Skill skills: battler.getSkillList()) {
            try {
                if (Database2.SkillToActor(skills) != null) {
                    skillList.add(Database2.SkillToActor(skills));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (skillList != null && !skillList.isEmpty()) {
            currentSkill = skillList.get(0);
        } else {
            currentSkill = null;
        }
        
    }
    

    public FieldSkill activateCurrentSkill() {
        if (battler.getMP() >= currentSkill.getSkill().getCost()) {
            battler.changeMP(currentSkill.getSkill().getCost());
            return currentSkill;
        }
        return null;
    }

    public FieldSkill activateCurrentSkill(Player p) {
        if (battler.getMP() >= currentSkill.getSkill().getCost()) {
            battler.changeMP(currentSkill.getSkill().getCost());
            currentSkill.setInvoker(p);
//            currentSkill.playFieldSound();
            return currentSkill;
        }
        return null;
    }
    
    public final FieldSkill activateSkill(String s) {
        if (battler.getMP() >= battler.getSkill(s).getCost()) {
            battler.changeMP(battler.getSkill(s).getCost());
            return currentSkill;
        }
        return null;
    }

    public final FieldSkill activateSkill(String s, Player p) {
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
        ActorBattler newBattler = new ActorBattler((Battler) battler.clone(), spriteSheet);
        newBattler.delay = delay;
        newBattler.shapeName = shapeName;
        newBattler.speed = speed;
        return newBattler;
    }
    
    public <T extends Battler> T getBattler() {
        return (T) battler;
    }
    
    public FieldSkill getCurrentSkill() {
        return currentSkill;
    }
    
    public ArrayList<FieldSkill> getSkillList() {
        ArrayList<FieldSkill> tempSkillList = new ArrayList<>();
        for (Skill skill : battler.getSkillList()) {
            FieldSkill tempSkill = Database2.SkillToActor(skill);
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

    public float getDelay() {
        return delay;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }
    
    
}
