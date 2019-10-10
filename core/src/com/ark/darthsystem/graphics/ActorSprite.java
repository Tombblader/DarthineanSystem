
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.ark.darthsystem.graphics.Actor.Facing;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author Keven Tran
 */
public class ActorSprite implements Serializable {

    private String masterSpriteSheet;
    private transient HashMap<SpriteModeFace, Animation<Sprite>> spriteSheetFace = new HashMap<>();
    private transient HashMap<SpriteModeBattler, Animation<Sprite>> spriteSheetBattler = new HashMap<>();
    private transient HashMap<SpriteModeField, HashMap<Actor.Facing, Animation<Sprite>>> spriteSheetField = new HashMap<SpriteModeField, HashMap<Actor.Facing, Animation<Sprite>>>();
    private transient Animation<Sprite> currentFieldImage;
    private transient Animation<Sprite> currentBattlerImage;
    private transient Animation<Sprite> currentFaceImage;

    public ActorSprite() {
        masterSpriteSheet = "";
    }

    public ActorSprite(String imageName) {
        masterSpriteSheet = imageName;
        setupImage();
    }

    public void setImage(String imageName) {
        setMasterSpriteSheet(imageName);
        setupImage();
    }

    private void setupImage() {
        setupWalking();
        setupFace();
        setupBattler();
    }

    private void setupWalking() {
        final float DELAY = 1 / 10f;
        for (SpriteModeField field : SpriteModeField.values()) {
            HashMap<Actor.Facing, Animation<Sprite>> tempAnimation = new HashMap<>();
            for (Actor.Facing direction : Actor.Facing.values()) {
                try {
                    Array<Sprite> tempSprites = GraphicsDriver.getMasterSheet().createSprites(getMasterSpriteSheet()
                            + "/field/"
                            + field.toString().toLowerCase()
                            + '/'
                            + direction.toString().toLowerCase());
                    if (tempSprites.size == 0) {
                        tempSprites = GraphicsDriver.getMasterSheet().createSprites("characters/darcy_alma"
                                + "/field/"
                                + field.toString().toLowerCase()
                                + '/'
                                + direction.toString().toLowerCase());
                    }
                    for (Sprite s : tempSprites) {
                        s.setOriginCenter();
                    }
                    tempAnimation.put(direction, new Animation<>(DELAY, tempSprites,
                            field == SpriteModeField.WALK || field == SpriteModeField.STAND
                                    ? Animation.PlayMode.LOOP : Animation.PlayMode.NORMAL
                    ));
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
                Array<Sprite> tempSprites = GraphicsDriver.getMasterSheet().createSprites(getMasterSpriteSheet() + "/face/" + face.toString().toLowerCase());
                if (tempSprites.size == 0) {
                    tempSprites = GraphicsDriver.getMasterSheet().createSprites("characters/darcy_alma" + "/face/" + face.toString().toLowerCase());
                }
                spriteSheetFace.put(face,
                        new Animation<>(DELAY, tempSprites, Animation.PlayMode.LOOP));
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
                Array<Sprite> tempSprites = GraphicsDriver.getMasterSheet().createSprites(getMasterSpriteSheet()
                        + "/battler/" + battler.toString().toLowerCase());
                if (tempSprites.size == 0) {
                    tempSprites = GraphicsDriver.getMasterSheet().createSprites("darcy_alma/"
                            + "/battler/" + battler.toString().toLowerCase());
                }
                spriteSheetBattler.put(battler, new Animation<>(DELAY,
                        tempSprites,
                        Animation.PlayMode.LOOP));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        currentBattlerImage = spriteSheetBattler.get(SpriteModeBattler.BATTLER);
    }

    public Animation<Sprite> getFieldAnimation(SpriteModeField field, Actor.Facing facing) {
        return (spriteSheetField.get(field)).get(facing);
    }

    public Animation<Sprite> getFaceAnimation(SpriteModeFace face) {
        return spriteSheetFace.get(face);
    }

    public Animation<Sprite> getBattlerAnimation(SpriteModeBattler battler) {
        return spriteSheetBattler.get(battler);
    }

    public void setFieldAnimation(SpriteModeField field, Actor.Facing facing) {
        currentFieldImage = spriteSheetField.get(field).get(facing);
    }

    public void setFieldAnimation(SpriteModeField field, Actor.Facing facing, Animation animation) {
        currentFieldImage = animation;
        spriteSheetField.get(field).put(facing, animation);
    }

    public void setFaceAnimation(SpriteModeFace face) {
        currentFaceImage = spriteSheetFace.get(face);
    }

    public void setBattlerAnimation(SpriteModeBattler battler) {
        currentBattlerImage = spriteSheetBattler.get(battler);
    }

    public Animation<Sprite> getCurrentFieldAnimation() {
        return currentFieldImage;
    }

    public Animation<Sprite> getCurrentFaceAnimation() {
        return currentFaceImage;
    }

    public Animation<Sprite> getCurrentBattlerAnimation() {
        return currentBattlerImage;
    }

    /**
     * @return the masterSpriteSheet
     */
    public String getMasterSpriteSheet() {
        return masterSpriteSheet;
    }

    /**
     * @param masterSpriteSheet the masterSpriteSheet to set
     */
    public void setMasterSpriteSheet(String masterSpriteSheet) {
        this.masterSpriteSheet = masterSpriteSheet;
        setupImage();
    }

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
        BATTLER,
        CUSTOM;
        /*
        ATTACK,
        CAST,
        OUCH
         */

    }

}
