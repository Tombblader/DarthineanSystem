/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.ark.darthsystem.Action;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.database.DefaultMenu;
import com.ark.darthsystem.database.InterfaceDatabase;
import com.ark.darthsystem.database.SoundDatabase;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.states.OverheadMap;
import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.states.events.Event;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.Collections;

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

    private int attackButton = Keys.TAB;
    private int switchBattlerButton = Keys.A;
    private int skillButton = Keys.T;
    private int switchSkill = Keys.E;
    private int charge = Keys.C;
    private int defendButton = Keys.X;
//    private int confirmButton = Keys.ENTER;
    private int quitButton = Keys.ESCAPE;
    private int menuButton = Keys.ENTER;
    private int jumpButton = Keys.V;

    ActorSkill attackAnimation;
    private float speed = SPEED;
    private boolean canAttack = true;
    private boolean canSkill = true;
    private boolean attacking;
    private boolean skilling;
    private boolean isJumping;
    private boolean canDodge = true;
    private boolean isWalking;
    private ActorSprite.SpriteModeField fieldState = ActorSprite.SpriteModeField.STAND;
    private ActorSkill currentSkill;

    private Input playerInput;
    private BitmapFont font;
    private ActorBattler currentBattler;
    private Array<Event> eventQueue = new Array<>();
    
//    private boolean isJumping;
    private ArrayList<ActorBattler> party = new ArrayList<>();
    private int currentBattlerIndex = 0;

    public Player(ArrayList<ActorBattler> getBattler, float getX, float getY) {
        super(getBattler.get(0).getSprite(), getX, getY, DELAY);
        currentBattler = getBattler.get(0);
        currentSkill = Database2.SkillToActor(getBattler.get(0).getBattler().getSkill(0));
        party = getBattler;
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
        // playerInput = Database2.createInputInstance();
    }

    
    private void setControls() {
        moveUp = Keys.UP;
        moveDown = Keys.DOWN;
        moveLeft = Keys.LEFT;
        moveRight = Keys.RIGHT;
        slowButton = Keys.SHIFT_LEFT;
        attackButton = Keys.SPACE;
        switchBattlerButton = Keys.A;
        skillButton = Keys.F;
        switchSkill = Keys.E;
        charge = Keys.C;
        defendButton = Keys.X;
        quitButton = Keys.ESCAPE;
        menuButton = Keys.ENTER;
        jumpButton = Keys.V;
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
        ActorSkill temp = attackAnimation.clone();
        if (getFacing().getX() == -1) {
            Array<Sprite> s = new Array<>(Sprite.class);
            for (TextureRegion r : temp.getCurrentAnimation().getKeyFrames()) {
                s.add(new Sprite(r));
                s.peek().flip(true, false);
            }
            Animation<Sprite> a = new Animation<>(temp.getDelay(), s);
            temp.changeAnimation(a);
        }
        temp.setInvoker(this);
        return temp;
    }
    public void skill() {
        if (currentSkill != null) {
            currentSkill.setInvoker(this);
            ActorSkill tempSkill;
            if (getCurrentBattler().activateCurrentSkill() != null) {
                tempSkill = getCurrentBattler().getCurrentSkill().clone();
                if (getFacing().getX() == -1) {
                    Array<Sprite> s = new Array<>(Sprite.class);
                    for (TextureRegion r : tempSkill.getCurrentAnimation().getKeyFrames()) {
                        s.add(new Sprite(r));
                        s.peek().flip(true, false);
                    }
                    Animation<Sprite> a = new Animation<>(tempSkill.getDelay(), s);
                    tempSkill.changeAnimation(a);
                }
                tempSkill.setInvoker(this);
                tempSkill.setX(this);
                tempSkill.setY(this);
                attacking = true;
                addTimer(new GameTimer("Skill", tempSkill.getChargeTime()+tempSkill.getAftercastDelay() * 1000f) {
                    @Override
                    public void event(Actor a) {
                        canSkill = true;
                    }
                });
                setPause((tempSkill.getChargeTime() * 1000f));
                addTimer(new GameTimer("Skill", getDelay() * 1000 * getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacing()).getKeyFrames().length) {
                    @Override
                    public void event(Actor a) {
                        attacking = false;
                    }
                });
                addTimer(new GameTimer("Skill", tempSkill.getChargeTime() * 1000f) {
                    @Override
                    public void event(Actor a) {
                        tempSkill.playFieldSound();
                        fieldState = ActorSprite.SpriteModeField.SKILL;
                        setPause((tempSkill.getAnimationDelay() * 1000f));
                        if (tempSkill.getSkill().getAlly()) {
                            Action action = new Action(Battle.Command.Skill,
                                    tempSkill.getSkill().overrideCost(0),
                                    tempSkill.getInvoker()
                                    .getCurrentBattler().getBattler(),
                                    tempSkill.getInvoker().getCurrentBattler().getBattler(),
                                    getAllBattlers());
                            action.calculateDamage(new Battle(tempSkill.getInvoker().getAllActorBattlers(), getAllActorBattlers(), Database1.inventory, null));
                        }
                        tempSkill.setMap(getCurrentMap());
                    }
                    public boolean update(float delta) {
                        fieldState = ActorSprite.SpriteModeField.SKILL;
                        attacking = true;
                        return super.update(delta);
                    }
                });
            }
        }
    }
    public void setAttackAnimation() {
        Equipment tempEquipment = getCurrentBattler().getBattler().getEquipment(0);
        if (tempEquipment != null) {
            try {
                ActorSkill tempAnimation = tempEquipment.getAnimation();
                attackAnimation = tempAnimation.clone();
            } catch (Exception e) {
                attackAnimation = Database2.Sword();
                e.printStackTrace();
            }
        } else {
            attackAnimation = Database2.Sword();
        }
        attackAnimation.setInvoker(this);    
    }

    public void attack() {
        if (!getCurrentMap().getPhysicsWorld().isLocked()) {
            attacking = true;
            ActorSkill animation = getAttackAnimation();
            animation.setX(this);
            animation.setY(this);
            setPause((int)((this.getDelay() * (this.getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacing()).getKeyFrames().length - 1f)) * 1000f));
            addTimer(new GameTimer("Attack_Charge", this.getDelay() * getAttackAnimation().getChargeTime() * 1000f) {
                @Override
                public void event(Actor a) {
                    animation.playFieldSound();
                    animation.setFacing();
                    fieldState = ActorSprite.SpriteModeField.ATTACK;
                    setPause(((animation.getAnimationDelay()) * 1000f));
                    if (!getCurrentMap().getPhysicsWorld().isLocked()) {
                        animation.setMap(getCurrentMap());
                        addTimer(new GameTimer("Attack", animation.getAnimationDelay() * 1000) {
                            @Override
                            public void event(Actor a) {
                                fieldState = ActorSprite.SpriteModeField.STAND;
                                attacking = false;
                                a.setPause(250);
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
//                public void clear() {
//                    attacking = false;
//                    fieldState = ActorSprite.SpriteModeField.STAND;
//                }
            });
        }
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    public void moving(float delta) {
        setSpeed(getBaseSpeed());
        if (isJumping) {
            setSpeed(getBaseSpeed() * 1.5f);
        }
        
        if (Input.getKeyPressed(jumpButton) && canDodge) {
            jump();
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
        while (!currentBattler.getBattler().isAlive()) {
            switchBattler();
        }
        if (!isWalking) {
            if (!attacking && !skilling) {
                fieldState = ActorSprite.SpriteModeField.STAND;
            }
            if (getMainBody() != null) {
                getMainBody().setLinearVelocity(0, 0);
            }
        }
        isWalking = false;
        
        if (canMove() && canAttack && !isJumping) {
            attacking(delta);
        }
        if (canMove()) {
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
        return isJumping;
    }

    public void setMap(OverheadMap map, float x, float y) { 
        setInitialX(x);
        setInitialY(y);
        setX(x);
        setY(y);
        super.setMap(map);
    }
    
    public void render(Batch batch) {
        super.render(batch);
    }

    public float getBaseSpeed() {
        return SPEED;
    }

    public void jump() {
        GraphicsDriver.setMessage("I believe I can fly!");
        final int JUMP_TIME = 1050;
        if (!isJumping) {
            GameTimer tempTimer = new GameTimer("JUMP", JUMP_TIME) {
                public void event(Actor a) {
                    isJumping = false;
                    canAttack = true;
                    setMainFilter(ActorCollision.CATEGORY_PLAYER, (short)(ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
                    setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT));
                    fieldState = ActorSprite.SpriteModeField.STAND;
                    resetAnimation();
                }

                public boolean update(float delta, Actor a) {
                    isJumping = true;
                    canAttack = false;
                    return super.update(delta, a);
                }
            };
            this.setInvulnerability(JUMP_TIME);
            addTimer(tempTimer);
            setMainFilter(ActorCollision.CATEGORY_PLAYER, ActorCollision.CATEGORY_WALLS);
            setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_EVENT));
            isJumping = true;
            fieldState = ActorSprite.SpriteModeField.JUMP;
            canAttack = false;
        }
    }
    
    protected void resetSprite() {
        if (currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias()) != null && currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias()).getKeyFrames().length > 0) {
           changeAnimation(currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias()));
        } else {
            changeAnimation(currentBattler.getSprite().getFieldAnimation(ActorSprite.SpriteModeField.STAND, getFacingBias()));
        }

    }
    
    public final void setDefaultFilter() {
        setMainFilter(ActorCollision.CATEGORY_PLAYER, (short)(ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
        setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT));
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
    
    protected void applySprite() {
        if (currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias()) != null && 
                currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias())
                        .getKeyFrames()
                .length > 0) {
            changeDuringAnimation(currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias()));
        } else {
            changeDuringAnimation(currentBattler.getSprite().getFieldAnimation(ActorSprite.SpriteModeField.STAND, getFacingBias()));
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

        if (Input.getKeyRepeat(defendButton) && getCurrentBattler().getBattler().getDefend() == 1.0) {
            defend();
        } else {
            if (Input.getKeyPressed(switchSkill)) {
                getCurrentBattler().changeSkill(1);
            }
            if (Input.getKeyPressed(switchBattlerButton)) {
                switchBattler();
            }

            if (Input.getKeyPressed(attackButton) && canAttack) {
                attack();
            }
            if (Input.getKeyPressed(skillButton) && canAttack && canSkill) {
                skill();
            }
            if (Input.getKeyPressed(charge) && canAttack) {
                getCurrentBattler().getBattler().changeMP(-1);
                setPause(200f);
            }

        }        
    }

    public void switchBattler() {
        if (totalPartyKill()) {
            return;
        }
        if (!party.isEmpty() && currentBattlerIndex >= party.size()) {
            currentBattlerIndex = 0;
        }
        if (!party.isEmpty()) {
            Collections.rotate(party, -1);

            while (!party.get(0).getBattler().isAlive()) {
                Collections.rotate(party, -1);
            }
            currentBattler = party.get(0);
            setAttackAnimation();
            currentSkill = currentBattler.getCurrentSkill();
            fieldState = ActorSprite.SpriteModeField.STAND;
            applySprite();
        }
    }

    
    public void switchSkill(ActorSkill skill) {

    }

    public ActorBattler getCurrentBattler() {
        return currentBattler;
    }

    public ActorBattler getBattler(int index) {
        return party.get(index);
    }

    public ArrayList<ActorBattler> getAllActorBattlers() {
        return party;
    }

    public ArrayList<Battler> getAllBattlers() {
        ArrayList<Battler> allBattlers = new ArrayList<>();
        for (ActorBattler battler : party) {
            allBattlers.add(battler.getBattler());
        }
        return allBattlers;
    }

    public boolean totalPartyKill() {
        boolean isDead = true;
        for (ActorBattler member : party) {
            isDead &= !member.getBattler().isAlive();
        }
        return isDead;
    }
    
    public void renderGlobalData(Batch batch) {
        printStats(batch);
    }
    
    public void printStats(Batch batch) {
        final int STAT_WIDTH = Gdx.graphics.getWidth();
        final int STAT_HEIGHT = 78;
        final float SUB_WIDTH = Gdx.graphics.getWidth() / 6f;
        final float NAME_X = 64f;
        final float NAME_Y = 10f;
        final float FONT_SIZE = GraphicsDriver.getFont().getCapHeight();
        for (int i = 0; i < getAllActorBattlers().size(); i++) {
            Sprite temp = (Sprite) (getAllActorBattlers().get(i).getSprite().getCurrentFaceAnimation().getKeyFrame(getElapsedTime()));
            InterfaceDatabase.TEXT_BOX.draw(batch, SUB_WIDTH * i + GraphicsDriver.getCamera().getScreenPositionX(), (GraphicsDriver.getCamera().getScreenPositionY()), SUB_WIDTH, STAT_HEIGHT);
            batch.draw(temp,
                    (SUB_WIDTH * i + GraphicsDriver.getCamera().getScreenPositionX() + 5),
                    ((STAT_HEIGHT - 64) / 2 + GraphicsDriver.getCamera().getScreenPositionY()),
                    0,
                    0,
                    temp.getRegionWidth(),
                    temp.getRegionHeight(),
                    temp.getScaleX(),
                    temp.getScaleY(),
                    temp.getRotation());
            GraphicsDriver.drawMessage(batch, font,
                    getAllActorBattlers().get(i).getBattler().getName(),
                    ((NAME_X + SUB_WIDTH * i) + GraphicsDriver.getCamera().getScreenPositionX()),
                    (NAME_Y + GraphicsDriver.getCamera().getScreenPositionY()));
            GraphicsDriver.drawMessage(batch, font,
                    "HP: " + getAllActorBattlers().get(i).getBattler().getHP() + "/" + getAllActorBattlers().get(i).getBattler().getMaxHP(),
                    ((NAME_X + SUB_WIDTH * i) + GraphicsDriver.getCamera().getScreenPositionX()),
                    ((NAME_Y + FONT_SIZE) + GraphicsDriver.getCamera().getScreenPositionY()
                            ));
            GraphicsDriver.drawMessage(batch, font,
                    "MP: " + getAllActorBattlers().get(i).getBattler().getMP() + "/" + getAllActorBattlers().get(i).getBattler().getMaxMP(),
                    ((NAME_X + SUB_WIDTH * i) + GraphicsDriver.getCamera().getScreenPositionX()),
                    ((NAME_Y + FONT_SIZE * 2f) + GraphicsDriver.getCamera().getScreenPositionY()));
            GraphicsDriver.drawMessage(batch, font,
                    getAllActorBattlers().get(i).getCurrentSkill().getSkill().getName(),
                    ((NAME_X + SUB_WIDTH * i) + GraphicsDriver.getCamera().getScreenPositionX()),
                    ((NAME_Y + FONT_SIZE * 3f) + GraphicsDriver.getCamera().getScreenPositionY()));
        }
    }
    
    public void defend() {
        getCurrentBattler().getBattler().defend();
        disableMovement();
        addTimer(new GameTimer("DEFEND", 99999) {
            @Override
            public void event(Actor a) {
                for (Battler allBattlers : getAllBattlers()) {
                    allBattlers.resetDefend();
                }
                attacking = false;
                enableMovement();
            }
            
            public boolean isFinished() {
                return !Input.getKeyRepeat(defendButton);
            }
            
        }); 
    }
    
    public void addButtonEvent(Event e) {
        eventQueue.add(e);
    }
    
    public void removeButtonEvent(Event e) {
        eventQueue.removeValue(e, true);        
    }
    
    public Event getClosestButtonEvent() {
        Event temp = eventQueue.size != 0 ? eventQueue.get(0) : null;
        for (Event e : eventQueue) {
            if (Math.pow(e.getX() - getX(), 2) + Math.pow(e.getY() - getY(), 2) <
                    Math.pow(temp.getX() - getX(), 2) + Math.pow(temp.getY() - getY(), 2)) {
                temp = e;
            }
        }
        return temp;
    }
    
    public void ouch() {
        fieldState = ActorSprite.SpriteModeField.OUCH;
        setPause(100);
        SoundDatabase.ouchSound.play();
        addTimer(new GameTimer("OUCH", 100) {
            @Override
            public void event(Actor a) {
                fieldState = ActorSprite.SpriteModeField.STAND;
            }
            
            @Override
            public boolean update(float delta, Actor a) {
                fieldState = ActorSprite.SpriteModeField.OUCH;
                return super.update(delta, a);
            }
        });
    }    
}