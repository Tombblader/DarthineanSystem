/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics.Parameters;

import com.ark.darthsystem.Database.SoundDatabase;
import com.ark.darthsystem.Graphics.ActorSkill;
import com.ark.darthsystem.Skill;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class ActorSkillParameters {
    private float aftercastDelay;
    private ActorSkill.Area area;
    private float chargeTime;
    private float relX;
    private float relY;
    private Skill skill;
    private Sprite[] battlerImage;
    private Sprite[] fieldImage;
    private Sound fieldSound;
    private Sound battlerSound;
    private float translateX;
    private float translateY;

    public ActorSkillParameters(float aftercastDelay, float chargeTime, float relX, float relY, Skill skill, Sprite[] battlerImage, Sprite[] fieldImage, ActorSkill.Area area, Sound fieldSound, Sound battlerSound, float translateX, float translateY) {
        this();
        this.area = area;
        this.aftercastDelay = aftercastDelay;
        this.chargeTime = chargeTime;
        this.relX = relX;
        this.relY = relY;
        this.skill = skill;
        this.battlerImage = battlerImage;
        this.fieldImage = fieldImage;
        this.fieldSound = fieldSound;
        this.battlerSound = battlerSound;
        this.translateX = translateX;
        this.translateY = translateY;
    }
    public ActorSkillParameters() {
        this.area = ActorSkill.Area.FRONT;
        this.translateY = 0;
        this.battlerSound = SoundDatabase.battlerSwordSound;
        this.translateX = 0;
        this.fieldSound = SoundDatabase.fieldSwordSound;
        this.relY = 0;
        this.relX = 0;
        this.chargeTime = 0;
        this.aftercastDelay = 0;
        
    }
    
    public float getAftercastDelay() {
        return aftercastDelay;
    }

    public void setAftercastDelay(float aftercastDelay) {
        this.aftercastDelay = aftercastDelay;
    }

    public ActorSkill.Area getArea() {
        return area;
    }

    public void setArea(ActorSkill.Area area) {
        this.area = area;
    }

    public float getChargeTime() {
        return chargeTime;
    }

    public void setChargeTime(float chargeTime) {
        this.chargeTime = chargeTime;
    }

    public float getRelX() {
        return relX;
    }

    public void setRelX(float relX) {
        this.relX = relX;
    }

    public float getRelY() {
        return relY;
    }

    public void setRelY(float relY) {
        this.relY = relY;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Sprite[] getBattlerImage() {
        return battlerImage;
    }

    public void setBattlerImage(Sprite[] battlerImage) {
        this.battlerImage = battlerImage;
    }

    public Sprite[] getFieldImage() {
        return fieldImage;
    }

    public void setFieldImage(Sprite[] fieldImage) {
        this.fieldImage = fieldImage;
    }

    public Sound getFieldSound() {
        return fieldSound;
    }

    public void setFieldSound(Sound fieldSound) {
        this.fieldSound = fieldSound;
    }

    public Sound getBattlerSound() {
        return battlerSound;
    }

    public void setBattlerSound(Sound battlerSound) {
        this.battlerSound = battlerSound;
    }

    public float getTranslateX() {
        return translateX;
    }

    public void setTranslateX(float translateX) {
        this.translateX = translateX;
    }

    public float getTranslateY() {
        return translateY;
    }

    public void setTranslateY(float translateY) {
        this.translateY = translateY;
    }
}
