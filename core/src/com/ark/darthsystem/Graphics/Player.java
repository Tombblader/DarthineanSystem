/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.Database.DefaultMenu;
import com.ark.darthsystem.Database.InterfaceDatabase;
import com.ark.darthsystem.States.OverheadMap;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.GameOverException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.physics.box2d.Filter;
import java.util.Iterator;

/**
 *
 * @author trankt1
 */
public class Player extends ActorCollision {
    private static final float SPEED = .4f;
    private static final float DELAY = 6f / 60f;

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
    private int confirmButton = Keys.ENTER;
    private int quitButton = Keys.ESCAPE;
    private int menuButton = Keys.N;
    private int jumpButton = Keys.V;

    ActorSkill attackAnimation;
    private float speed = SPEED;
    private boolean canAttack = true;
    private boolean attacking;
    private boolean isJumping;
    private ArrayList<ActorBattler> party = new ArrayList<>();
//    private ArrayList<Integer> currentFace = new ArrayList<>();
    private int currentBattlerIndex = 0;
    private boolean isWalking;
    private ActorSprite.SpriteModeField fieldState = ActorSprite.SpriteModeField.STAND;
    private float boundX = 1024 * GraphicsDriver.getCurrentCamera().getConversion();
    private float boundY = 768 * GraphicsDriver.getCurrentCamera().getConversion();
    private ActorSkill currentSkill;
    private ActorBattler currentBattler;
    private Input playerInput;
    private BitmapFont font;

    public Player(ArrayList<ActorBattler> getBattler, float getX, float getY) {
        super(getBattler.get(0).getSprite(), getX, getY, DELAY);
        currentBattler = getBattler.get(0);
        currentSkill = Database2.SkillToActor(getBattler.get(0).getBattler().getSkill(0));
        party = getBattler;
        for (Iterator<ActorBattler> it = party.iterator(); it.hasNext();) {
            it.next();
        }
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

    public void addPartyMember(ActorBattler b) {
        party.add(b);
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
        Equipment tempEquipment = getCurrentBattler().getBattler().getEquipment(0);
        if (tempEquipment != null) {
            try {
                ActorSkill tempAnimation =
                 tempEquipment.getAnimation();
                 tempAnimation.setInvoker(this);
                 attackAnimation = tempAnimation;
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
            setAttacking(true);
            getAttackAnimation().resetAnimation();
            getAttackAnimation().setX(this);
            getAttackAnimation().setY(this);
            this.setPause((getAttackAnimation().getAnimationDelay() * 1000f));
            getAttackAnimation().setMap(getCurrentMap(), true);
            getAttackAnimation().playFieldSound();
            fieldState = ActorSprite.SpriteModeField.ATTACK;
            switch (super.getFacing()) {
                case UP:
                case UP_LEFT:
                case UP_RIGHT:
                    changeAnimation(currentBattler.getSprite().
                            getFieldAnimation(fieldState, Actor.Facing.UP));
                    break;
                case RIGHT:
                    changeAnimation(currentBattler.getSprite().
                            getFieldAnimation(fieldState, Actor.Facing.RIGHT));
                    break;
                case LEFT:
                    changeAnimation(currentBattler.getSprite().
                            getFieldAnimation(fieldState, Actor.Facing.LEFT));
                    break;
                case DOWN:
                case DOWN_LEFT:
                case DOWN_RIGHT:
                    changeAnimation(currentBattler.getSprite().
                            getFieldAnimation(fieldState, Actor.Facing.DOWN));
                    break;
            default:
        }
        getCurrentAnimation().setFrameDuration(getAttackAnimation().getAnimationDelay() / getCurrentAnimation().getKeyFrames().length);
        addTimer(new GameTimer("Attack", 10000) {
            @Override
            public void event(Actor a) {
                fieldState = ActorSprite.SpriteModeField.STAND;
                attacking = false;
            }
            public boolean isFinished() {
                System.out.println(getElapsedTime());
                return getCurrentAnimation().isAnimationFinished(getElapsedTime());
            }
        });        
    }
    
    public void skill() {
        if (getCurrentBattler().getCurrentSkill() != null) {
            getCurrentBattler().getCurrentSkill().setX(this);
            getCurrentBattler().getCurrentSkill().setY(this);
            ActorSkill tempSkill = getCurrentBattler().activateCurrentSkill(this);
            if (tempSkill != null) {
                setPause((getCurrentBattler().getCurrentSkill().getChargeTime() * 1000f));
                addTimer(new GameTimer("Skill", getCurrentBattler().getCurrentSkill().getChargeTime() * 1000f) {
                    @Override
                    public void event(Actor a) {
//                        fieldState = ActorSprite.SpriteModeField.SKILL;
                        setPause((getCurrentBattler().getCurrentSkill().getAnimationDelay() * 1000f));
                        tempSkill.setMap(getCurrentMap(), true);
                    }
                    
                });
//                fieldState = ActorSprite.SpriteModeField.CAST;
//                setPause((getCurrentBattler().getCurrentSkill().getAnimationDelay() * 1000f));
 //               tempSkill.setMap(getCurrentMap(), true);
            }
        }
    }
        
    public ActorSkill getCurrentSkill() {
        return currentSkill;
    }

    public void removePartyMember(ActorBattler b) {
        party.remove(party.indexOf(b));
    }

    public boolean isWalking() {
        return isWalking;
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
    }

    // private float speed = .35;
    public void Moving(float delta) {
        setSpeed(getBaseSpeed());
        if (isJumping) {
            setSpeed(getBaseSpeed() * 1.5f);
        }
            
        if (Input.getKeyRepeat(slowButton)) {
            setSpeed(getBaseSpeed() * 0.5f);
        }
        if (Input.getKeyPressed(jumpButton)) {
            jump();
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

        if (Input.getKeyPressed(Keys.F2)) {

        }
        if (Input.getKeyPressed(switchSkill)) {
            getCurrentBattler().changeSkill(1);
        }
        if (Input.getKeyPressed(attackButton) && canAttack) {
            attack();
        }
        if (Input.getKeyPressed(charge) && canAttack) {
            getCurrentBattler().getBattler().changeMP(-1);
            setPause(200f);
        }

        if (Input.getKeyPressed(skillButton) && canAttack) {
            skill();
        }
        if (Input.getKeyPressed(switchBattlerButton)) {
            switchBattler();
        }

        if (Input.getKeyPressed(confirmButton)) {
            // GraphicsDriver.pauseState();
        }
        if (Input.getKeyPressed(menuButton)) {
            GraphicsDriver.addMenu(new DefaultMenu());
        }
        if (Input.getKeyPressed(quitButton)) {
            throw new GameOverException();
        }
    }

    public void printStats(Batch batch) {
        final int STAT_WIDTH = Gdx.graphics.getWidth();
        final int STAT_HEIGHT = 78;
        final float SUB_WIDTH = Gdx.graphics.getWidth() / 6f;
        final float NAME_X = 64f;
        final float NAME_Y = 10f;
        final float FONT_SIZE = GraphicsDriver.getFont().getCapHeight();
//        if (GraphicsDriver.getCurrentCamera() instanceof PlayerCamera) {
//            batch.end();
//            batch.begin();
//            batch.setProjectionMatrix(GraphicsDriver.getCamera().combined);
//        }
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
                    getAllActorBattlers().get(i).currentSkill.getSkill().getName(),
                    ((NAME_X + SUB_WIDTH * i) + GraphicsDriver.getCamera().getScreenPositionX()),
                    ((NAME_Y + FONT_SIZE * 3f) + GraphicsDriver.getCamera().getScreenPositionY()));
        }
//        if (GraphicsDriver.getCurrentCamera() instanceof PlayerCamera) {
//            batch.end();
//            batch.begin();
//            batch.setProjectionMatrix(GraphicsDriver.getPlayerCamera().combined);
//        }

    }

    @Override
    public void update(float delta) {
        while (!currentBattler.getBattler().isAlive()) {
            switchBattler();
        }
        if (!isWalking) {
            if (!attacking) {
                fieldState = ActorSprite.SpriteModeField.STAND;
            }
            getMainBody().setLinearVelocity(0, 0);
        }
        isWalking = false;
        if (canMove()) {
            Moving(delta);
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

    public void setBoundX(int x) {
        boundX = x;
    }

    public void setBoundY(int y) {
        boundY = y;
    }

    public boolean totalPartyKill() {
        boolean isDead = true;
        for (ActorBattler member : party) {
            isDead &= !member.getBattler().isAlive();
        }
        return isDead;
    }

    public void setMap(OverheadMap map, float x, float y) {
        boundX = map.getWidth();
        boundY = map.getHeight();
        setX(x);
        setY(y);
        super.setMap(map, !(this instanceof ActorAI));
    }
    
    public void render(Batch batch) {
        super.render(batch);
    }

    public void renderGlobalData(Batch batch) {
        printStats(batch);
    }

    public float getBaseSpeed() {
        return SPEED;
    }

    public void jump() {
        GraphicsDriver.setMessage("I believe I can fly!");
        final int JUMP_TIME = 1050;
        if (!isJumping) {
            GameTimer tempTimer = new GameTimer("Jump", JUMP_TIME) {
                public void event(Actor a) {
                    isJumping = false;
                    canAttack = true;
                    setMainFilter(ActorCollision.CATEGORY_PLAYER, (short)(ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
                    setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT));
                    fieldState = ActorSprite.SpriteModeField.STAND;
                    switch (getFacing()) {
                        case UP:
                        case UP_LEFT:
                        case UP_RIGHT:
                            changeAnimation(currentBattler.getSprite().
                                    getFieldAnimation(fieldState, Actor.Facing.UP));
                            break;
                        case RIGHT:
                            changeAnimation(currentBattler.getSprite().
                                    getFieldAnimation(fieldState, Actor.Facing.RIGHT));
                            break;
                        case LEFT:
                            changeAnimation(currentBattler.getSprite().
                                    getFieldAnimation(fieldState, Actor.Facing.LEFT));
                            break;
                        case DOWN:
                        case DOWN_LEFT:
                        case DOWN_RIGHT:
                            changeAnimation(currentBattler.getSprite().
                                    getFieldAnimation(fieldState, Actor.Facing.DOWN));
                            break;
                    default:
                    }
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
            System.out.println("I believe I can touch the sky!");
        }
    }

    public void hasTakenDamage(final Battler b) {
        GameTimer t = new GameTimer("Ouch", 1000) {
            int battlerIndex = getAllBattlers().indexOf(b);

            @Override
            public void event(Actor a) {
//                currentFace.set(battlerIndex, 1);
            }
        };
        addTimer(t);
//        currentFace.set(getAllBattlers().indexOf(b), 2);
    }

    public void setFieldState(ActorSprite.SpriteModeField mode) {
        fieldState = mode;
    }
    
    public ActorSprite.SpriteModeField getFieldState() {
        return fieldState;
    }
    
    public void generateBody(OverheadMap map) {
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
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                changeDuringAnimation(currentBattler.getSprite().
                        getFieldAnimation(fieldState, Actor.Facing.UP));
                break;
            case RIGHT:
                changeDuringAnimation(currentBattler.getSprite().
                        getFieldAnimation(fieldState, Actor.Facing.RIGHT));
                break;
            case LEFT:
                changeDuringAnimation(currentBattler.getSprite().
                        getFieldAnimation(fieldState, Actor.Facing.LEFT));
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                changeDuringAnimation(currentBattler.getSprite().
                        getFieldAnimation(fieldState, Actor.Facing.DOWN));
                break;
            default:
        }

    }
    
    public BitmapFont getFont() {
        return font;
    }
}
