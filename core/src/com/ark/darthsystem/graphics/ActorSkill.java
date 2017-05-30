/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.ark.darthsystem.Action;
import com.ark.darthsystem.database.SoundDatabase;
import com.ark.darthsystem.Skill;
import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

    private float aftercastDelay = 0;
    private Area area;
    private transient Actor battlerAnimation;
    
    private transient Sound battlerSound = SoundDatabase.battlerSwordSound;
    private float chargeTime = 0;
    private float currentX = 0;
    private float currentY = 0;
    private transient Sound fieldSound = SoundDatabase.fieldSwordSound;
    private transient Player invoker;
    private WeldJoint joint;
    private String originalBattlerImageName;
    private transient Sprite[] originalBattlerImage;
    private String originalFieldImageName;
    private transient Sprite[] originalFieldImage;
    private float relX;
    private float relY;
    private Skill skill;
    private float translateX;
    private float translateY;

    public ActorSkill(Sprite[] img,
            float getX,
            float getY,
            float delay,
            Skill getSkill) {
        super(img, getX, getY, delay, true);
        originalFieldImageName = "";
        originalBattlerImageName = "";
        originalFieldImage = img;
        for (Sprite s : originalFieldImage) {
            s.setCenter(s.getWidth() / 2f, s.getHeight() / 2f);
            s.setOriginCenter();
        }
        relX = getX;
        relY = getY;
        skill = getSkill;
        aftercastDelay = delay;
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
        this(img, getX, getY, delay, getSkill);
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
            float getX,
            float getY,
            int translateX,
            int translateY,
            int delay,
            Skill getSkill) {
        this(img, getX, getY, delay, getSkill);
        this.translateX = translateX;
        this.translateY = translateY;
        area = Area.FRONT;
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
        this(img, battlerImg, getX, getY, delay, castTime, getSkill, getArea);
        translateX = getTranslateX;
        translateY = getTranslateY;
    }

    public void generateBody(OverheadMap map) {
        super.generateBody(map);
        getMainBody().setBullet(true);
        currentX = 0;
        currentY = 0;
        Array<Joint> temp = new Array<>();
        map.getPhysicsWorld().getJoints(temp);
        if (joint != null && temp.contains(joint, true)) {
            map.removeJoint(joint);
        }
        Filter filter = new Filter();
        filter.categoryBits = !(getInvoker() instanceof ActorAI) ? ActorCollision.CATEGORY_PLAYER_SKILL : ActorCollision.CATEGORY_AI_SKILL;
        filter.maskBits = (short) (ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES | ((getInvoker() instanceof ActorAI) ? ActorCollision.CATEGORY_PLAYER_SKILL : ActorCollision.CATEGORY_AI_SKILL));
        getMainFixture().setFilterData(filter);
        filter.maskBits = ((getInvoker() instanceof ActorAI) ? ActorCollision.CATEGORY_PLAYER : ActorCollision.CATEGORY_AI);
        getSensorFixture().setFilterData(filter);
        if (invoker.getMainBody() != null && (translateX == 0 && (area == Area.FRONT))) {
            WeldJointDef def = new WeldJointDef();
            def.dampingRatio = 1f;
            def.frequencyHz = 60;
            def.collideConnected = false;
            def.initialize(invoker.getMainBody(), getMainBody(), new Vector2(getX(), getY()));            
            joint = (WeldJoint) map.getPhysicsWorld().createJoint(def);
        }
        else if (invoker.getMainBody() != null && (translateX == 0 && (area == Area.SELF_BENEFIT))) {
            getMainBody().setTransform(invoker.getMainBody().getPosition(), getMainBody().getAngle());
            getSensorBody().setTransform(invoker.getSensorBody().getPosition(), getSensorBody().getAngle());
            WeldJointDef def = new WeldJointDef();
            def.dampingRatio = 1f;
            def.frequencyHz = 60;
            def.collideConnected = false;
            def.initialize(invoker.getMainBody(), getMainBody(), new Vector2(getX(), getY()));            
            joint = (WeldJoint) map.getPhysicsWorld().createJoint(def);
        } else if (translateX != 0) {

        }
        
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

    public void setInvoker(Player p) {
        invoker = p;
    }

    public float getRelX() {
        return relX;
    }

    public float getRelY() {
        return relY;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill s) {
        skill = s;
    }

    public Skill getSkill(Player a) {
        invoker = a;
        return skill;
    }
    public Actor getBattlerAnimation() {
        return battlerAnimation;
    }

    public Actor getBattlerAnimation(float x, float y) {
        battlerAnimation.setX(x);
        battlerAnimation.setY(y);
        return battlerAnimation;
    }

    public Sound getBattlerSound() {
        return battlerSound;
    }

    public void setX(ActorCollision a) {
        super.changeX(a.getFacing().getX());
        if (area != Area.SELF) {
            super.setX((relX + (a.getWidth() / 4 / (a.getFacing().y != 0 ? (float) Math.sqrt(2.0) : 1)) / PlayerCamera.PIXELS_TO_METERS) * a.getFacing().getX() + a.getMainBody().getPosition().x);
        } else {
            super.setX( a.getMainBody().getPosition().x);
        }
    }

    public void setY(ActorCollision a) {
        super.changeY(a.getFacing().getY());
        if (area != Area.SELF) {
            super.setY((relY + (a.getHeight() / 4 / (a.getFacing().x != 0 ? (float) Math.sqrt(2.0) : 1)) / PlayerCamera.PIXELS_TO_METERS) * a.getFacing().getY() + a.getMainBody().getPosition().y);
        } else {
            super.setY( a.getMainBody().getPosition().y);
        }
    }

    public void playBattlerSound() {
        try {
            battlerSound.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playFieldSound() {
        try {
            fieldSound.stop();
            fieldSound.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render(Batch batch) {
        super.render(batch);
    }

    public void setAnimationFacing() {
        getCurrentImage().setRotation(this.getFacing().getRotate());
    }

    public void stopBattlerSound() {
        try {
            battlerSound.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopFieldSound() {
        try {
            fieldSound.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void update(float delta) {
        setFacing();
//        currentX += translateX * getFacing().getX();
//        currentY += translateY * getFacing().getY();
        if (translateX != 0) {
            getMainBody().setLinearVelocity(getFacing().getX() * translateX, getFacing().getY() * translateX);
        }
        super.update(delta);
        setAnimationFacing();
    }

    public enum Area {

        ALL,
        BOOMERANG,
        CONE,
        CROSS,
        FRONT,
        LINE,
        SELF,
        SELF_BENEFIT,
        RADIUS,
        SPHERE;

        private int translateX;
        private int translateY;

        float updateX(float delta, Facing f) {
            switch (this) {
                case SELF:
                case SELF_BENEFIT:
                    return 0;
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
    public ActorSkill clone() {
        return new ActorSkill(originalFieldImage,
                originalBattlerImage,
                relX,
                relY,
                translateX,
                translateY,
                this.getDelay(),
                chargeTime,
                skill,
                area);
    
    }
    
    public void activate(Player player) {
        ActorSkill tempSkill = clone();
        if (getFacing().getX() == -1) {
            Array<Sprite> s = new Array<>(Sprite.class);
            for (TextureRegion r : tempSkill.getCurrentAnimation().getKeyFrames()) {
                s.add(new Sprite(r));
                s.peek().flip(true, false);
            }
            Animation<Sprite> a = new Animation<>(tempSkill.getDelay(), s);
            tempSkill.changeAnimation(a);
        }
        tempSkill.setInvoker(player);
        tempSkill.setX(player);
        tempSkill.setY(player);
        player.setAttacking(true);
        addTimer(new GameTimer("Skill", tempSkill.getChargeTime()+tempSkill.getAftercastDelay() * 1000f) {
            @Override
            public void event(Actor a) {
//                playertrue;
            }
        });
        setPause((tempSkill.getChargeTime() * 1000f));
        addTimer(new GameTimer("Skill", getDelay() * 1000 * getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacing()).getKeyFrames().length) {
            @Override
            public void event(Actor a) {
                player.setAttacking(false);
            }
        });
        addTimer(new GameTimer("Skill", tempSkill.getChargeTime() * 1000f) {
            @Override
            public void event(Actor a) {
                tempSkill.playFieldSound();
                player.setFieldState(ActorSprite.SpriteModeField.SKILL);
                setPause((tempSkill.getAnimationDelay() * 1000f));
                if (tempSkill.getSkill().getAlly()) {
                    Action action = new Action(Battle.Command.Skill,
                            tempSkill.getSkill().overrideCost(0),
                            tempSkill.getInvoker()
                            .getCurrentBattler().getBattler(),
                            tempSkill.getInvoker().getCurrentBattler().getBattler(),
                            player.getAllBattlers());
                    action.calculateDamage(new Battle(tempSkill.getInvoker().getAllActorBattlers(), player.getAllActorBattlers(), Database1.inventory, null));
                }
                tempSkill.setMap(getCurrentMap());
            }
            public boolean update(float delta) {
                player.setFieldState(ActorSprite.SpriteModeField.SKILL);
                player.setAttacking(true);
                return super.update(delta);
            }
        });
    }

}