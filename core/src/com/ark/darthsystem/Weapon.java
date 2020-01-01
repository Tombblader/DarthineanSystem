/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem;

import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.database.SkillDatabase;
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.FieldSkill;
import com.ark.darthsystem.states.Battle;
import java.io.IOException;
import java.util.Arrays;

/**
 *
 * @author keven
 */
public class Weapon extends Equipment {

    private String animationName;
    private String battlerAnimationName;
    private transient FieldSkill animation;
    private transient Actor battlerAnimation;
    private FieldSkill.Area areaName;
    private String shapeName;
    private float fps;

    public Weapon(String getName,
            String getDescription,
            String icon,
            String fieldAnimation,
            String battlerAnimation,
            int getMarketPrice,
            Equipment.Type[] type,
            Skill invoke,
            Battle.Element initializeElement,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        super(getName, getDescription, icon, getMarketPrice, type, Slot.MainHand, invoke, initializeElement, useMP, initializeAttack, initializeDefense, initializeSpeed, initializeMagic);
        animationName = fieldAnimation;
        battlerAnimationName = battlerAnimation;
        if (animationName == null || animationName.isEmpty()) {
            animationName = "sword";
        }
        if (battlerAnimationName == null || battlerAnimationName.isEmpty()) {
            battlerAnimationName = "sword";
        }
        this.battlerAnimation = new Actor("items/equipment/" + battlerAnimation + "/battler/battler", 0, 0, 1 / 12f, true);
        animation = SkillDatabase.FIELD_SKILL_LIST.get(animationName.toUpperCase());
        shapeName = animation.getShape();
        areaName = animation.getArea();
        fps = animation.getDelay();

    }

    public Weapon(String getName,
            String getDescription,
            String icon,
            String fieldAnimation,
            String battlerAnimation,
            int getMarketPrice,
            String[] type,
            Slot slot,
            Skill invoke,
            Battle.Element initializeElement,
            boolean useMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic) {
        this(getName,
                getDescription,
                icon,
                fieldAnimation,
                battlerAnimation,
                getMarketPrice,
                Arrays.stream(type).map((name) -> Equipment.Type.valueOf(name)).toArray(Equipment.Type[]::new),
                invoke,
                initializeElement,
                useMP,
                initializeAttack,
                initializeDefense,
                initializeSpeed,
                initializeMagic);
    }

    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
        stream.defaultReadObject();
        this.battlerAnimation = new Actor("items/equipment/" + battlerAnimation + "/battler/battler", 0, 0, 1 / 12f, true);
        animation = SkillDatabase.FIELD_SKILL_LIST.get(animationName.toUpperCase());
    }

    /**
     *
     * @return
     */
    public FieldSkill getAnimation() {
        return animation;
    }

    public void setAnimation(FieldSkill animation) {
        this.animation = animation;
    }

    public String getAnimationName() {
        return animationName;
    }

    public void setAnimationName(String animationName) {
        this.animationName = animationName;
    }

    public FieldSkill.Area getAreaName() {
        return areaName;
    }

    public void setAreaName(FieldSkill.Area areaName) {
        this.areaName = areaName;
    }

    public String getShapeName() {
        return shapeName;
    }

    public void setShapeName(String shapeName) {
        this.shapeName = shapeName;
    }

    public Actor getBattlerAnimation() {
        return battlerAnimation;
    }

    public Equipment clone() {
        Weapon temp = new Weapon(getName(), getDescription(), getIcon(), animationName, battlerAnimationName, getPrice(), getType(), getInvoke(), getElement(), useMP(), getAttack(), getDefense(), getSpeed(), getMagic());
        temp.animation = animation.placeOnMap();
        return temp;
    }
}
