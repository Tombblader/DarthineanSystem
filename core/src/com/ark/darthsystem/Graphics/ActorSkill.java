/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Skill;
import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;

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
    private int chargeTime = 0;
    private Player invoker;
    private float relX;
    private float relY;
    private Skill skill;
    private Actor battlerAnimation;
    private Sound fieldSound = Gdx.audio.newSound(Gdx.files.internal("sounds/slash.wav"));
    private Sound battlerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/slash.wav"));
    private float translateX;
    private float translateY;
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

    
    public float getAftercastDelay() {
        return aftercastDelay;
    }

    public float getAnimationDelay() {
        return ((float) (originalFieldImage.length) * (this.getDelay())) + aftercastDelay;
    }

    public int getChargeTime() {
        return chargeTime;
    }

    public Player getInvoker() {
        if (invoker == null) {
            System.out.println("null invoker");
        }
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

    public void setX(Actor a) {
        if (area != Area.SELF) {
            super.setX((relX + a.getWidth() / PlayerCamera.PIXELS_TO_METERS) * a.getFacing().getX() + a.getX());
        } else {
            super.setX((relX) + a.getX());
        }
        super.changeX(a.getFacing().getX());
    }

    public void setY(Actor a) {
        if (area != Area.SELF) {
            super.setY((relY + a.getHeight() / PlayerCamera.PIXELS_TO_METERS) * a.getFacing().getY() + a.getY());
        } else {
            super.setY((relY) + a.getY());
        }
        super.changeY(a.getFacing().getY());
    }

    public void update(float delta) {
        setFacing();
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
        if (joint != null) {
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
        fieldSound.dispose();
        battlerSound.dispose();
    }
}
