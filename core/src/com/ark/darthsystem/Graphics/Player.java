/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Database.Database;
import com.ark.darthsystem.Database.DefaultMenu;
import com.ark.darthsystem.Database.SkillDatabase;
import com.ark.darthsystem.States.OverheadMap;
import com.ark.darthsystem.GameOverException;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author trankt1
 */
public class Player extends ActorCollision {
    private static final float SPEED = .6f;
    private static final float DELAY =  1f/12f;

    private int moveUp = Keys.UP;
    private int moveDown = Keys.DOWN;
    private int moveLeft = Keys.LEFT;
    private int moveRight = Keys.RIGHT;

//    private int slowButton = Keys.SHIFT_LEFT;

    private int attackButton = Keys.TAB;
    private int switchBattlerButton = Keys.A;
    private int skillButton = Keys.T;
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
    private boolean canSkill = true;
    private boolean attacking;
    private boolean isDodging;
    private boolean isWalking;
    private ActorSprite.SpriteModeField fieldState = ActorSprite.SpriteModeField.IDLE;
    private ActorSkill currentSkill;

    private Input playerInput;
    private BitmapFont font;
    private TeamColor team;

    public Player(TeamColor color, ActorSprite sprite, float getX, float getY) {
        super(sprite, getX, getY, DELAY);
        team = color;
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
        setControls();
        currentSkill = SkillDatabase.Spear_Throw;
        setShape("player");
//        setDefaultFilter();
        // playerInput = Database2.createInputInstance();
    }
    
    private void setControls() {
        if (team == TeamColor.RED) {
            moveUp = Keys.D;
            moveDown = Keys.C;
            moveLeft = Keys.X;
            moveRight = Keys.V;


            attackButton = Keys.Z;
            skillButton = Keys.A;
            quitButton = Keys.ESCAPE;
            menuButton = Keys.ENTER;
            dodgeButton = Keys.Q;
        }
        if (team == TeamColor.BLUE) {
            moveUp = Keys.UP;
            moveDown = Keys.DOWN;
            moveLeft = Keys.LEFT;
            moveRight = Keys.RIGHT;
            attackButton = Keys.COMMA;
            skillButton = Keys.PERIOD;
            quitButton = Keys.ESCAPE;
            menuButton = Keys.ENTER;
            dodgeButton = Keys.SLASH;            
        }
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }
    
    public boolean isAttacking() {
        return attacking;
    }
    
    public boolean canAttack() {
        return canAttack;        
    }

    public ActorSkill getAttackAnimation() {
        ActorSkill temp = Database.Basic();
        temp.setInvoker(this);
        return temp;
//        return attackAnimation;
    }
    public void skill() {
        if (currentSkill != null) {
            currentSkill.setInvoker(this);
            ActorSkill tempSkill = this.currentSkill.clone();
            tempSkill.setInvoker(this);
            tempSkill.setX(this);
            tempSkill.setY(this);            
            if (tempSkill != null) {
                attacking = true;
                addTimer(new GameTimer("Skill", 5000) {
                    @Override
                    public void event(Actor a) {
                        canSkill = true;
                    }
                });
                setPause((tempSkill.getChargeTime() * 1000f));
                addTimer(new GameTimer("Skill", tempSkill.getChargeTime() * 1000f) {
                    @Override
                    public void event(Actor a) {
                        tempSkill.playFieldSound();
//                        fieldState = ActorSprite.SpriteModeField.SKILL;
                        setPause((tempSkill.getAnimationDelay() * 1000f));
                        tempSkill.setMap(getCurrentMap());
                        attacking = false;
                    }
                });
            }
        }
    }
    public void setAttackAnimation() {
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
        attackAnimation = Database.Basic();
//        }
        attackAnimation.setInvoker(this);
    }

    public void attack() {
        if (!getCurrentMap().getPhysicsWorld().isLocked()) {
            attacking = true;
            ActorSkill animation = getAttackAnimation();
            animation.resetAnimation();
            animation.setX(this);
            animation.setY(this);
//            setPause((int)((this.getDelay() * (this.getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacing()).getKeyFrames().length - 1f)) * 1000f));
            addTimer(new GameTimer("Attack_Charge", this.getDelay() * ((this.getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacing()).getKeyFrames().length - 1f)) * 1000f) {
                public void event(Actor a) {
                    animation.playFieldSound();
                    animation.setFacing();
                    fieldState = ActorSprite.SpriteModeField.ATTACK;
//                    setPause(((animation.getAnimationDelay()) * 1000f));
                    if (!getCurrentMap().getPhysicsWorld().isLocked()) {
                        animation.setMap(getCurrentMap());
                        addTimer(new GameTimer("Attack", animation.getAnimationDelay() * 1000) {
                            @Override
                            public void event(Actor a) {
                                fieldState = ActorSprite.SpriteModeField.IDLE;
                                attacking = false;
//                                a.setPause(250);
                                a.addTimer(new GameTimer("Delay", 500) {
                                    @Override
                                    public void event(Actor a) {
                                        canAttack = true;
                                    }
                                    public boolean update(float delta, Actor a) {
                                        canAttack = false;
                                        return super.update(delta, a);
                                    }
                            });
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
                public void clear() {
                    attacking = false;
                    fieldState = ActorSprite.SpriteModeField.IDLE;
                }
            });
        }
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    // private float speed = .35;
    public void moving(float delta) {
        setSpeed(getBaseSpeed() * (getItem() != null && getItem().getName().equalsIgnoreCase("Meat") ? 0.5f : 1));
            
        if (Input.getKeyPressed(dodgeButton)) {
            dodge();
        }

        if (Input.getKeyRepeat(moveLeft)) {
            getMainBody().setLinearVelocity(-getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
            changeX(-1);
            setWalking(true);
            fieldState = ActorSprite.SpriteModeField.RUN;
        }

        if (Input.getKeyRepeat(moveRight)) {
            getMainBody().setLinearVelocity(getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
            changeX(1);
            setWalking(true);
            fieldState = ActorSprite.SpriteModeField.RUN;
        }

        if (Input.getKeyRepeat(moveUp)) {
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -getSpeed() * (float) (delta));
            changeY(-1);
            setWalking(true);
            fieldState = ActorSprite.SpriteModeField.RUN;
        }
        if (Input.getKeyRepeat(moveDown)) {
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, getSpeed() * (float) (delta));
            changeY(1);
            setWalking(true);
            fieldState = ActorSprite.SpriteModeField.RUN;
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
                fieldState = ActorSprite.SpriteModeField.IDLE;
            }
            getMainBody().setLinearVelocity(0, 0);
        }
        isWalking = false;
        
        if (canMove() && canAttack && !isDodging) {
            attacking(delta);
        }
        if (canMove() && !isDodging) {
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
        final int DODGE_TIME = (int) (this.getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ROLL, Facing.RIGHT).getKeyFrames().length * getDelay() * 1000f);
        if (!isDodging) {
            GameTimer tempTimer = new GameTimer("Dodge", DODGE_TIME) {
                public void event(Actor a) {
                    isDodging = false;
                    canAttack = true;
                    short mainFilter = 0;
                    short subFilter = 0;
                    switch (team) {
                        case RED:
                            mainFilter = ActorCollision.CATEGORY_RED;
                            subFilter = (short) (ActorCollision.CATEGORY_BLUE_SKILL | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT);
                            break;
                        case BLUE:
                            mainFilter = ActorCollision.CATEGORY_BLUE;
                            subFilter = (short) (ActorCollision.CATEGORY_RED_SKILL | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT);
                            break;
                        case YELLOW:
                            mainFilter = ActorCollision.CATEGORY_AI;
                            subFilter = (short) (ActorCollision.CATEGORY_RED_SKILL | ActorCollision.CATEGORY_BLUE_SKILL | ActorCollision.CATEGORY_EVENT);
                            break;
                    }
                    setMainFilter(mainFilter, (short)(ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES | ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_RED | ActorCollision.CATEGORY_BLUE));
                    setSensorFilter(mainFilter, subFilter);
                    fieldState = ActorSprite.SpriteModeField.IDLE;
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
                    getMainBody().setLinearVelocity(5 * getFacing().x * getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
                    changeX(getFacing().x);
//                    setWalking(true);
                    fieldState = ActorSprite.SpriteModeField.ROLL;
                    isDodging = true;
                    canAttack = false;
                    return super.update(delta, a);
                }
            };
            this.setInvulnerability(DODGE_TIME);
            addTimer(tempTimer);
            switch (team) {
                case RED:
                    setMainFilter(ActorCollision.CATEGORY_RED, ActorCollision.CATEGORY_WALLS);
                    setSensorFilter(ActorCollision.CATEGORY_RED, (short) (ActorCollision.CATEGORY_EVENT));
                    break;
                case BLUE:
                    setMainFilter(ActorCollision.CATEGORY_BLUE, ActorCollision.CATEGORY_WALLS);
                    setSensorFilter(ActorCollision.CATEGORY_BLUE, (short) (ActorCollision.CATEGORY_EVENT));
                    break;
                case YELLOW:
                    setMainFilter(ActorCollision.CATEGORY_AI, ActorCollision.CATEGORY_WALLS);
                    setSensorFilter(ActorCollision.CATEGORY_AI, (short) (ActorCollision.CATEGORY_EVENT));
                    break;
            }
            
            isDodging = true;
            fieldState = ActorSprite.SpriteModeField.ROLL;
            canAttack = false;
        }
    }
    
    public final void setDefaultFilter() {
        short mainFilter = 0;
        short subFilter = 0;
        switch (team) {
            case RED:
                mainFilter = ActorCollision.CATEGORY_RED;
                subFilter = (short) (ActorCollision.CATEGORY_BLUE_SKILL | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT);
                break;
            case BLUE:
                mainFilter = ActorCollision.CATEGORY_BLUE;
                subFilter = (short) (ActorCollision.CATEGORY_RED_SKILL | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT);
                break;
            case YELLOW:
                mainFilter = ActorCollision.CATEGORY_AI;
                subFilter = (short) (ActorCollision.CATEGORY_RED_SKILL | ActorCollision.CATEGORY_BLUE_SKILL | ActorCollision.CATEGORY_EVENT);
                break;
        }
        setMainFilter(mainFilter, (short)(ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
        setSensorFilter(mainFilter, subFilter);
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
        short mainFilter = 0;
        short subFilter = 0;
        switch (team) {
            case RED:
                mainFilter = ActorCollision.CATEGORY_RED;
                subFilter = (short) (ActorCollision.CATEGORY_BLUE_SKILL | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT);
                break;
            case BLUE:
                mainFilter = ActorCollision.CATEGORY_BLUE;
                subFilter = (short) (ActorCollision.CATEGORY_RED_SKILL | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT);
                break;
            case YELLOW:
                mainFilter = ActorCollision.CATEGORY_AI;
                subFilter = (short) (ActorCollision.CATEGORY_RED_SKILL | ActorCollision.CATEGORY_BLUE_SKILL | ActorCollision.CATEGORY_EVENT);
                break;
        }

        filter.categoryBits = mainFilter;
        filter.maskBits = ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES | ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_RED | ActorCollision.CATEGORY_BLUE;
        getMainFixture().setFilterData(filter);
        filter.maskBits = subFilter;
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
        if (Input.getKeyPressed(skillButton) && canAttack && canSkill) {
            skill();
        }
        
    }

    public TeamColor getTeam() {
        return team;
    }
}
