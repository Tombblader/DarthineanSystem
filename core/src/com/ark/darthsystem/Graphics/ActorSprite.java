
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Graphics.Actor.Facing;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author trankt1
 */
public class ActorSprite implements Serializable {


    private transient String masterSpriteSheet;
    private transient HashMap<SpriteModeField, HashMap<Actor.Facing, Animation>> spriteSheetField = new HashMap<>();
    private transient Animation currentFieldImage;

    public ActorSprite(String imageName) {
        masterSpriteSheet = imageName;
        setupImage();
    }

    public void setImage(String imageName) {
        masterSpriteSheet = imageName;
        setupImage();
    }

    private void setupImage() {
        setupWalking();
    }

    private void setupWalking() {
        final int DELAY = 10;
        for (SpriteModeField field : SpriteModeField.values()) {
            HashMap<Actor.Facing, Animation> tempAnimation = new HashMap<>();

            for (Actor.Facing direction : Actor.Facing.values()) {
                try {
                    Array<Sprite> tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                    "/field/" +
                                    field.toString().toLowerCase() +
                                    '/' +
                                    direction.toString().toLowerCase());
                    for (Sprite s : tempSprites) {
                        s.setOriginCenter();
                    }
                    tempAnimation.put(direction, new Animation(DELAY, tempSprites, Animation.PlayMode.LOOP));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (field != null) {
                spriteSheetField.put(field, tempAnimation);
            }
        }
        currentFieldImage = spriteSheetField.get(SpriteModeField.IDLE).get(Facing.RIGHT);
    }


    public Animation getFieldAnimation(SpriteModeField field, Actor.Facing facing) {
        return spriteSheetField.get(field).get(facing);
    }


    public void setFieldAnimation(SpriteModeField field, Actor.Facing facing) {
        currentFieldImage = spriteSheetField.get(field).get(facing);
    }

    public Animation getCurrentFieldAnimation() {
        return currentFieldImage;
    }


    public enum SpriteModeField {
        IDLE,
        RUN,
        ATTACK,
        CAST,
        DODGE,
        SKILL,
        OUCH,
        DEAD,
        CUSTOM;
    }
}
