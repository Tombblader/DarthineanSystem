/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Database.SoundDatabase;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author trankt1
 */
public class ActorSkill extends ActorCollision {

    public enum Area {

        ALL,
        BOOMERANG,
        CONE,
        CROSS,
        FRONT,
        LINE,
        SELF,
        RADIUS,
        SPHERE;

        private int translateX;
        private int translateY;

        float updateX(float delta, Facing f) {
            switch (this) {
                case FRONT:
                    return translateX * f.getX();
                case LINE:
                    return f.getX();
                case RADIUS:

                    break;
                case CROSS:
                    break;
                case CONE:
                    break;
                case SPHERE:
                    break;
                case BOOMERANG:
                    break;
                default:
                    while (true) {
                        System.out.println("You shouldn't have done that.");
                    }
            }
            return 0;
        }

    }
    private float aftercastDelay = 0;
    private Area area;
    private float chargeTime = 0;
    private Player invoker;
    private float relX;
    private float relY;
    private Skill skill;
    private Actor battlerAnimation;
    private Sound fieldSound = SoundDatabase.fieldSwordSound;
    private Sound battlerSound = SoundDatabase.battlerSwordSound;
    private float translateX;
    private float translateY;
    private float currentX = 0;
    private float currentY = 0;
    private Sprite[] originalFieldImage;
    private Sprite[] originalBattlerImage;
    private WeldJoint joint;

    public ActorSkill(Sprite[] img,
            float getX,
            float getY,
            int translateX,
            int translateY,
            int delay,
            Skill getSkill) {
        super(img, getX, getY, delay, true);
        originalFieldImage = img;
        for (Sprite s : originalFieldImage) {
            s.setCenter(s.getWidth() / 2f, s.getHeight() / 2f);
            s.setOriginCenter();
        }
        relX = getX;
        relY = getY;
        skill = getSkill;
        aftercastDelay = delay;
        this.translateX = translateX;
        this.translateY = translateY;
        area = Area.FRONT;
    }

    public ActorSkill(Sprite[] img,
            float getX,
            float getY,
            float delay,
            Skill getSkill) {
        super(img, getX, getY, delay, true);
        originalFieldImage = img;
        for (Sprite s : originalFieldImage) {
            s.setCenter(s.getWidth() / 2f, s.getHeight() / 2f);
            s.setOriginCenter();
        }
        relX = getX;
        relY = getY;
        skill = getSkill;
        aftercastDelay = 0;
        translateX = 0;
        translateY = 0;
        area = Area.FRONT;
    }

    public ActorSkill(Sprite[] img,
            float getX,
            float getY,
            float delay,
            Skill getSkill,
            Area getArea) {
        super(img, getX, getY, delay, true);
        originalFieldImage = img;
        for (Sprite s : originalFieldImage) {
            s.setCenter(s.getWidth() / 2f, s.getHeight() / 2f);
            s.setOriginCenter();
        }
        relX = getX;
        relY = getY;
        skill = getSkill;
        aftercastDelay = 0;
        translateX = 0;
        translateY = 0;
        area = getArea;
    }
    
    public ActorSkill(Sprite[] img,
            Sprite[] battlerImg,
            float getX,
            float getY,
            float delay,
            Skill getSkill,
            Area getArea) {
        this(img, getX, getY, delay, getSkill, getArea);
        originalBattlerImage = battlerImg;
        for (Sprite s : originalBattlerImage) {
            s.setCenter(s.getWidth() / 2f, s.getHeight() / 2f);
            s.setOriginCenter();
        }
        battlerAnimation = new Actor(originalBattlerImage, 0, 0, delay, true);
    }
    
    public ActorSkill(Sprite[] img,
            Sprite[] battlerImg,
            float getX,
            float getY,
            float delay,
            float castTime,
            Skill getSkill,
            Area getArea) {
        this(img, battlerImg, getX, getY, delay, getSkill, getArea);
        chargeTime = castTime;
    }

    public ActorSkill(Sprite[] img,
            Sprite[] battlerImg,
            float getX,
            float getY,
            float getTranslateX,
            float getTranslateY,
            float delay,
            float castTime,
            Skill getSkill,
            Area getArea) {
        this(img, battlerImg, getX, getY, castTime, delay, getSkill, getArea);
        translateX = getTranslateX;
        translateY = getTranslateY;
    }
    
    public float getAftercastDelay() {
        return aftercastDelay;
    }

    public float getAnimationDelay() {
        return ((float) (originalFieldImage.length) * (this.getDelay())) + aftercastDelay;
    }

    public float getChargeTime() {
        return chargeTime;
    }

    public Player getInvoker() {
        return invoker;
    }

    public Skill getSkill() {
        return skill;
    }

    public Skill getSkill(Player a) {
        invoker = a;
        return skill;
    }

    public void setInvoker(Player p) {
        invoker = p;
    }

    public void setSkill(Skill s) {
        skill = s;
    }

    public void setX(Player a) {
        super.changeX(a.getFacing().getX());
        if (area != Area.SELF) {
            super.setX((relX + (a.getWidth() / 4 / (a.getFacing().y != 0 ? (float) Math.sqrt(2.0) : 1)) / PlayerCamera.PIXELS_TO_METERS) * a.getFacing().getX() + a.getMainBody().getPosition().x + currentX);
        } else {
            super.setX((relX) + a.getMainBody().getPosition().x + a.getSpeed() + (float) (GraphicsDriver.getRawDelta() / 2f));
        }
    }

    public void setY(ActorCollision a) {
        super.changeY(a.getFacing().getY());
        if (area != Area.SELF) {
            super.setY((relY + (a.getHeight() / 4 / (a.getFacing().x != 0 ? (float) Math.sqrt(2.0) : 1)) / PlayerCamera.PIXELS_TO_METERS) * a.getFacing().getY() + a.getMainBody().getPosition().y);
        } else {
            super.setY((relY) + a.getMainBody().getPosition().y + (float) (GraphicsDriver.getRawDelta() / 2f));
        }
    }

    public void update(float delta) {
        setFacing();
        currentX += translateX * getFacing().getX();
        currentY += translateY * getFacing().getY();
        super.update(delta);
        setAnimationFacing();
    }
    
    public Actor getBattlerAnimation() {
        return battlerAnimation;
    }
    
    public Actor getBattlerAnimation(float x, float y) {
        battlerAnimation.setX(x);
        battlerAnimation.setY(y);        
        return battlerAnimation;
    }
    
    public void playFieldSound() {
        try {
            fieldSound.stop();
            fieldSound.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Sound getBattlerSound() {
        return battlerSound;
    }
    
    public void stopFieldSound() {
        try {
            fieldSound.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void playBattlerSound() {
        try {
            battlerSound.play();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopBattlerSound() {
        try {
            battlerSound.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(Batch batch) {
        super.render(batch);
    }

    public void setAnimationFacing() {
        getCurrentImage().setRotation(this.getFacing().getRotate());
    }

    public float getRelX() {
        return relX;
    }

    public float getRelY() {
        return relX;
    }

    public void setMap(OverheadMap map, boolean isPlayer) {
        super.setMap(map, isPlayer);
        currentX = 0;
        currentY = 0;
        Array<Joint> temp = new Array<>();
        map.getPhysicsWorld().getJoints(temp);
        if (joint != null && temp.contains(joint, true)) {
            map.getPhysicsWorld().destroyJoint(joint);
        }
        Filter filter = new Filter();
        filter.categoryBits = isPlayer ? ActorCollision.CATEGORY_PLAYER_SKILL : ActorCollision.CATEGORY_AI_SKILL;
        filter.maskBits = (short)(ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES | (!isPlayer ? ActorCollision.CATEGORY_PLAYER_SKILL : ActorCollision.CATEGORY_AI_SKILL));
        getMainFixture().setFilterData(filter);
        filter.maskBits = (!isPlayer ? ActorCollision.CATEGORY_PLAYER : ActorCollision.CATEGORY_AI);
        getSensorFixture().setFilterData(filter);
        if ((translateX == 0 && area == Area.FRONT)) {
            WeldJointDef def = new WeldJointDef();
            def.dampingRatio = 1f;
            def.frequencyHz = 60;
            def.collideConnected = false;
            def.initialize(invoker.getMainBody(), getMainBody(), new Vector2(getX(), getY()));        
            joint = (WeldJoint) map.getPhysicsWorld().createJoint(def);
        }
    }    
    
    public void generateBody(OverheadMap map) {
        super.generateBody(map);
    }
    
    public void dispose() {
//        fieldSound.dispose();
//        battlerSound.dispose();
    }
}
