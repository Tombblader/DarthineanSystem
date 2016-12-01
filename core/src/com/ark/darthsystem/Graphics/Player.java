/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.Database.DefaultMenu;
import com.ark.darthsystem.Database.InterfaceDatabase;
import com.ark.darthsystem.States.OverheadMap;
import com.ark.darthsystem.GameOverException;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author trankt1
 */
public class Player extends ActorCollision {
    private static final float SPEED = .4f;
    private static final float DELAY =  1f/12f;

    private int moveUp = Keys.UP;
    private int moveDown = Keys.DOWN;
    private int moveLeft = Keys.LEFT;
    private int moveRight = Keys.RIGHT;

    private int slowButton = Keys.SHIFT_LEFT;

    private int attackButton = Keys.SPACE;
    private int switchBattlerButton = Keys.A;
    private int skillButton = Keys.F;
    private int switchSkill = Keys.E;
    private int charge = Keys.C;
    private int defendButton = Keys.X;
//    private int confirmButton = Keys.ENTER;
    private int quitButton = Keys.ESCAPE;
    private int menuButton = Keys.ENTER;
    private int dodgeButton = Keys.V;

    ActorSkill attackAnimation;
    private float speed = SPEED;
    private boolean canAttack = true;
    private boolean attacking;
    private boolean isDodging;
    private boolean isWalking;
    private ActorSprite.SpriteModeField fieldState = ActorSprite.SpriteModeField.STAND;

    private Input playerInput;
    private BitmapFont font;

    public Player(TeamColor color, ActorSprite sprite, float getX, float getY) {
        super(sprite, getX, getY, DELAY);
        setMaxLife(5);
        setCurrentLife(5);
        setAttack(1);
        setAttackAnimation();
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(
                "fonts/monofont.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.flip = true;
        font = gen.generateFont(parameter);
        font.setColor(Color.WHITE);
        font.setUseIntegerPositions(false);
        gen.dispose();
        // playerInput = Database2.createInputInstance();
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    
    public boolean isAttacking() {
        return attacking;
    }

    public ActorSkill getAttackAnimation() {
        return attackAnimation;
    }

    public final void setAttackAnimation() {
//        Equipment tempEquipment = getCurrentBattler().getBattler().getEquipment(0);
//        if (tempEquipment != null) {
//            try {
//                ActorSkill tempAnimation =
//                    tempEquipment.getAnimation();
//                    tempAnimation.setInvoker(this);
//                    attackAnimation = tempAnimation;
//            } catch (Exception e) {
//                attackAnimation = Database2.Sword();
//                e.printStackTrace();
//            }
//        } else {
        attackAnimation = Database2.Basic();
//        }
        attackAnimation.setInvoker(this);
    }

    public void attack() {
        if (!getCurrentMap().getPhysicsWorld().isLocked()) {
            attacking = true;
            getAttackAnimation().resetAnimation();
            getAttackAnimation().setX(this);
            getAttackAnimation().setY(this);
            setPause((getAttackAnimation().getChargeTime() * 1000f));
            addTimer(new GameTimer("Attack_Charge", (int)(1/24f*4*1000)) {
                @Override
                public void event(Actor a) {
                    getAttackAnimation().playFieldSound();
                    getAttackAnimation().setFacing();
                    fieldState = ActorSprite.SpriteModeField.ATTACK;
                    setPause((getAttackAnimation().getAnimationDelay() * 1000f));
                    if (!getCurrentMap().getPhysicsWorld().isLocked()) {
                        getAttackAnimation().setMap(getCurrentMap());
                        addTimer(new GameTimer("Attack", (getAttackAnimation().getAnimationDelay() * 1000f)) {
                            @Override
                            public void event(Actor a) {
                                fieldState = ActorSprite.SpriteModeField.STAND;
                                attacking = false;
                                a.setPause(200);
                            }
                            public boolean update(float delta, Actor a) {
                                fieldState = ActorSprite.SpriteModeField.ATTACK;
                                return super.update(delta, a);
                            }
                        });
                    }
                }
                public boolean update(float delta, Actor a) {
                    attacking = true;
                    fieldState = ActorSprite.SpriteModeField.ATTACK;
                    return super.update(delta, a);
                }
            });
        }

//        getCurrentAnimation().setFrameDuration(getAttackAnimation().getAnimationDelay() / getCurrentAnimation().getKeyFrames().length);
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    // private float speed = .35;
    public void moving(float delta) {
        setSpeed(getBaseSpeed());
            
        if (Input.getKeyRepeat(slowButton)) {
            setSpeed(getBaseSpeed() * 0.5f);
        }
        if (Input.getKeyPressed(dodgeButton)) {
            dodge();
            // fieldState = ActorSprite.SpriteModeField.JUMP;
        }

        if (Input.getKeyRepeat(moveLeft)) {
            getMainBody().setLinearVelocity(-getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
            changeX(-1);
            setWalking(true);
            fieldState = ActorSprite.SpriteModeField.WALK;
        }

        if (Input.getKeyRepeat(moveRight)) {
            getMainBody().setLinearVelocity(getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
            changeX(1);
            setWalking(true);
            fieldState = ActorSprite.SpriteModeField.WALK;
        }

        if (Input.getKeyRepeat(moveUp)) {
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -getSpeed() * (float) (delta));
            changeY(-1);
            setWalking(true);
            fieldState = ActorSprite.SpriteModeField.WALK;
        }
        if (Input.getKeyRepeat(moveDown)) {
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, getSpeed() * (float) (delta));
            changeY(1);
            setWalking(true);
            fieldState = ActorSprite.SpriteModeField.WALK;
        }

        if (!Input.getKeyRepeat(moveLeft) && !Input.getKeyRepeat(moveRight)) {
            changeX(0);
            getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);

        }

        if (!Input.getKeyRepeat(moveUp) && !Input.getKeyRepeat(moveDown)) {
            changeY(0);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
        }

        if (Input.getKeyPressed(menuButton)) {
            GraphicsDriver.addMenu(new DefaultMenu());
        }
        if (Input.getKeyPressed(quitButton)) {
            throw new GameOverException();
        }
    }

    @Override
    public void update(float delta) {
        if (!isWalking) {
            if (!attacking) {
                fieldState = ActorSprite.SpriteModeField.STAND;
            }            
            getMainBody().setLinearVelocity(0, 0);
        }
        isWalking = false;
        
        if (canMove() && canAttack && !isDodging) {
            attacking(delta);
        }
        if (canMove() && !attacking && !isDodging) {
            moving(delta);
        } else {
            changeX(0);
            changeY(0);
        }
        super.update(delta);
        speed = SPEED;
        applySprite();
    }

    public boolean isJumping() {
        return isDodging;
    }

    public void setMap(OverheadMap map, float x, float y) {        
        setInitialX(x);
        setInitialY(y);
        super.setMap(map);
    }
    
    public void render(Batch batch) {
        super.render(batch);
    }

    public float getBaseSpeed() {
        return SPEED;
    }

    public void dodge() {
        final int DODGE_TIME = 250;
        if (!isDodging) {
            GameTimer tempTimer = new GameTimer("Dodge", DODGE_TIME) {
                public void event(Actor a) {
                    isDodging = false;
                    canAttack = true;
                    setMainFilter(ActorCollision.CATEGORY_PLAYER, (short)(ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
                    setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT));
                    fieldState = ActorSprite.SpriteModeField.STAND;
                    switch (getFacing()) {
                        case RIGHT:
                            changeAnimation(getSpriteSheet().
                                    getFieldAnimation(fieldState, Actor.Facing.RIGHT));
                            break;
                        case LEFT:
                            changeAnimation(getSpriteSheet().
                                    getFieldAnimation(fieldState, Actor.Facing.LEFT));
                            break;
                    default:
                    }
                }

                public boolean update(float delta, Actor a) {
                    isDodging = true;
                    canAttack = false;
                    return super.update(delta, a);
                }
            };
            this.setInvulnerability(DODGE_TIME);
            addTimer(tempTimer);
            setMainFilter(ActorCollision.CATEGORY_PLAYER, ActorCollision.CATEGORY_WALLS);
            setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_EVENT));
            isDodging = true;
            fieldState = ActorSprite.SpriteModeField.DODGE;
            canAttack = false;
        }
    }

    public void setFieldState(ActorSprite.SpriteModeField mode) {
        fieldState = mode;
    }
    
    public ActorSprite.SpriteModeField getFieldState() {
        return fieldState;
    }
    
    public void generateBody(OverheadMap map) {
        setX(getInitialX());
        setY(getInitialY());
        super.generateBody(map);
        Filter filter = new Filter();
        filter.categoryBits = ActorCollision.CATEGORY_PLAYER;
        filter.maskBits = ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES;
        getMainFixture().setFilterData(filter);
        filter.maskBits = ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT;
        getSensorFixture().setFilterData(filter);
    }
    
    private void applySprite() {
        switch (getFacing()) {
            case RIGHT:
                changeDuringAnimation(getSpriteSheet().
                        getFieldAnimation(fieldState, Actor.Facing.RIGHT));
                break;
            case LEFT:
                changeDuringAnimation(getSpriteSheet().
                        getFieldAnimation(fieldState, Actor.Facing.LEFT));
                break;
            default:
        }

    }
    
    public BitmapFont getFont() {
        return font;
    }

    private void attacking(float delta) {
        //Check for direction first
        if (Input.getKeyRepeat(moveLeft)) {
            changeX(-1);
        }

        if (Input.getKeyRepeat(moveRight)) {
            changeX(1);
        }

        if (Input.getKeyRepeat(moveUp)) {
            changeY(-1);
        }
        if (Input.getKeyRepeat(moveDown)) {
            changeY(1);
        }

        if (!Input.getKeyRepeat(moveLeft) && !Input.getKeyRepeat(moveRight)) {
            changeX(0);
        }

        if (!Input.getKeyRepeat(moveUp) && !Input.getKeyRepeat(moveDown)) {
            changeY(0);
        }
        setFacing();
        if (Input.getKeyPressed(attackButton) && canAttack) {
            attack();
        }
        
    }
}
