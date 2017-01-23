
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
                    Array<Sprite> tempSprites;                    
                    tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                    "/field/" +
                                    field.toString().toLowerCase() +
                                    '/' + direction.toString().toLowerCase());
                    switch (direction) {
                        case LEFT_DOWN:
                            if (tempAnimation.get(Actor.Facing.RIGHT_DOWN) != null) {
                                tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                    "/field/" +
                                    field.toString().toLowerCase() +
                                    "/right_down");
                                for (Sprite s : tempSprites) {
                                    s.flip(true, false);
                                }
                            }
                            if (tempSprites.size != 0) {
                                break;
                            }
                        case LEFT_UP:
                            if (tempAnimation.get(Actor.Facing.RIGHT_UP) != null) {
                                tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                    "/field/" +
                                    field.toString().toLowerCase() +
                                    "/right_up");
                                for (Sprite s : tempSprites) {
                                    s.flip(true, false);
                                }
                            }
                            if (tempSprites.size != 0) {
                                break;
                            }
                        case LEFT:
                            tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                            "/field/" +
                                            field.toString().toLowerCase() +
                                            "/right");
                            for (Sprite s : tempSprites) {
                                 s.flip(true, false);
                             }
                             break;
                        case RIGHT_DOWN:
                            tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                            "/field/" +
                                            field.toString().toLowerCase() +
                                            '/' + direction.toString().toLowerCase());
                            if (tempSprites.size != 0) {
                                break;
                            }
                        case RIGHT_UP:
                            tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                            "/field/" +
                                            field.toString().toLowerCase() +
                                            '/' + direction.toString().toLowerCase());
                            if (tempSprites.size != 0) {
                                break;
                            }
                        case RIGHT:
                            tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                            "/field/" +
                                            field.toString().toLowerCase() +
                                            "/right");
                            break;
                        case UP:
                        case DOWN:
                            tempSprites = GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet +
                                            "/field/" +
                                            field.toString().toLowerCase() +
                                            '/' + direction.toString().toLowerCase());
                            break;
                        default:
                            throw new AssertionError(direction.name());
                    }


                    for (Sprite s : tempSprites) {
                        s.setOriginCenter();
                    }
                    if (tempSprites != null && tempSprites.size != 0) {
                        tempAnimation.put(direction, new Animation(DELAY, tempSprites, Animation.PlayMode.LOOP));
                    }
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
        ROLL,
        SKILL,
        OUCH,
        DEAD,
        SHADOW,
        CUSTOM;
    }
}
