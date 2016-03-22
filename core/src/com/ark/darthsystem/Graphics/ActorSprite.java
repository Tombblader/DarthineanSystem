
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

    public enum SpriteModeFace {
        ANGRY,
        HAPPY,
        NORMAL,
        OUCH,
        SAD,
        CUSTOM;
    }

    public enum SpriteModeField {
        STAND,
        WALK,
        JUMP,
        ATTACK,
        CAST,
        SKILL,
        OUCH,
        DEAD,
        CUSTOM;
    }

    public enum SpriteModeBattler {
        BATTLER
/*      ATTACK,
        CAST,
        */
        
    }

    private transient String masterSpriteSheet;
    private transient HashMap<SpriteModeFace, Animation> spriteSheetFace = new HashMap<>();
    private transient HashMap<SpriteModeBattler, Animation> spriteSheetBattler = new HashMap<>();
    private transient HashMap<SpriteModeField, HashMap<Actor.Facing, Animation>> spriteSheetField = new HashMap<>();
    private transient Animation currentFieldImage;
    private transient Animation currentBattlerImage;
    private transient Animation currentFaceImage;

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
        setupFace();
        setupBattler();
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
        currentFieldImage = spriteSheetField.get(SpriteModeField.STAND).get(Facing.DOWN);
    }

    private void setupFace() {
        final int DELAY = 10;
        for (SpriteModeFace face : SpriteModeFace.values()) {
            try {
                spriteSheetFace.put(face,
                        new Animation(DELAY,
                                GraphicsDriver.getMasterSheet().
                                createSprites(masterSpriteSheet +
                                        "/face/" +
                                        face.toString().toLowerCase()),
                                Animation.PlayMode.LOOP));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        currentFaceImage = spriteSheetFace.get(SpriteModeFace.NORMAL);
    }

    private void setupBattler() {
        final int DELAY = 10;
        for (SpriteModeBattler battler : SpriteModeBattler.values()) {
            try {
                spriteSheetBattler.put(battler, new Animation(DELAY, 
                        GraphicsDriver.getMasterSheet().createSprites(masterSpriteSheet + 
                                "/battler/" + 
                                battler.toString().toLowerCase()),
                        Animation.PlayMode.LOOP));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        currentBattlerImage = spriteSheetBattler.get(SpriteModeBattler.BATTLER);
    }

    public Animation getFieldAnimation(SpriteModeField field, Actor.Facing facing) {
        return spriteSheetField.get(field).get(facing);
    }

    public Animation getFaceAnimation(SpriteModeFace face) {
        return spriteSheetFace.get(face);
    }

    public Animation getBattlerAnimation(SpriteModeBattler battler) {
        return spriteSheetBattler.get(battler);
    }

    public void setFieldAnimation(SpriteModeField field, Actor.Facing facing) {
        currentFieldImage = spriteSheetField.get(field).get(facing);
    }

    public void setFaceAnimation(SpriteModeFace face) {
        currentFaceImage = spriteSheetFace.get(face);
    }

    public void setBattlerAnimation(SpriteModeBattler battler) {
        currentBattlerImage = spriteSheetBattler.get(battler);
    }

    public Animation getCurrentFieldAnimation() {
        return currentFieldImage;
    }

    public Animation getCurrentFaceAnimation() {
        return currentFaceImage;
    }

    public Animation getCurrentBattlerAnimation() {
        return currentBattlerImage;
    }

}
