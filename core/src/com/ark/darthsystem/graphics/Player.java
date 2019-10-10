package com.ark.darthsystem.graphics;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.database.DefaultMenu;
import com.ark.darthsystem.database.InterfaceDatabase;
import com.ark.darthsystem.database.SoundDatabase;
import com.ark.darthsystem.Equipment;
import com.ark.darthsystem.states.OverheadMap;
import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.database.CharacterDatabase;
import com.ark.darthsystem.states.events.Event;
import com.ark.darthsystem.statusEffects.Normal;
import com.ark.darthsystem.statusEffects.StatusEffect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Keven Tran
 */
public class Player extends ActorCollision implements Serializable {

    private static final long serialVersionUID = 553782345;

    private static final float SPEED = .4f;
    private static final float DELAY = 1f / 10f;

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
    private int confirmButton = Keys.SPACE;
    private int quitButton = Keys.ESCAPE;
    private int menuButton = Keys.ENTER;
    private int jumpButton = Keys.V;

    private transient FieldSkill attackAnimation;
    private float speed = SPEED;
    private boolean canAttack = true;
    private boolean canSkill = true;
    private boolean attacking;
    private boolean skilling;
    private boolean isJumping;
    private boolean canDodge = true;
    private boolean isWalking;
    private ActorSprite.SpriteModeField fieldState = ActorSprite.SpriteModeField.STAND;
    private transient FieldSkill currentSkill;

    private transient Input playerInput;
//    private transient BitmapFont font;
    private transient FieldBattler currentBattler;
    private transient Array<Event> eventQueue;
//    private boolean isJumping;
    private transient ArrayList<FieldBattler> party;
    private int currentBattlerIndex = 0;
    private transient RayCastCallback buttonPushFinder;
    private boolean hasEvent;

    public Player() {
        eventQueue = new Array<>();
        party = new ArrayList<>();
    }

    public Player(ArrayList<FieldBattler> getBattler, float getX, float getY) {
        super(getBattler.get(0).getSprite().getMasterSpriteSheet() + "/field/stand/down", getX, getY, getBattler.get(0).getDelay(), getBattler.get(0).getShapeName());
        this.eventQueue = new Array<>();
        this.party = new ArrayList<>();
        party = getBattler;
        initialize();
    }

    private void initialize() {
        eventQueue = new Array<>();
        currentBattler = party.get(0);
        if (party.get(0).getBattler().getSkillList() != null
                && !party.get(0).getBattler().getSkillList().isEmpty()) {
            currentSkill = Database2.SkillToActor(party.get(0).getBattler().getSkill(0).getName());
        }
        setAttackAnimation();
//        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/monofont.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 24;
//        parameter.flip = true;
//        font = gen.generateFont(parameter);
//        font.getData().markupEnabled = true;        
//        font.setColor(Color.WHITE);
//        font.setUseIntegerPositions(false);
//        gen.dispose();
        checkStatusEffects();
        setControls();
    }

    private void setControls() {
        moveUp = Keys.UP;
        moveDown = Keys.DOWN;
        moveLeft = Keys.LEFT;
        moveRight = Keys.RIGHT;
        slowButton = Keys.SHIFT_LEFT;
        attackButton = Keys.SPACE;
        confirmButton = Keys.SPACE;
        switchBattlerButton = Keys.A;
        skillButton = Keys.F;
        switchSkill = Keys.S;
        charge = Keys.C;
        defendButton = Keys.X;
        quitButton = Keys.ESCAPE;
        menuButton = Keys.ENTER;
        jumpButton = Keys.V;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setCanAttack(boolean canAttack) {
        this.canAttack = canAttack;
    }

    public void setCanSkill(boolean canSkill) {
        this.canSkill = canSkill;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public boolean canAttack() {
        return canAttack;
    }

    public boolean canSkill() {
        return canSkill;
    }

    public FieldSkill getAttackAnimation() {
        FieldSkill temp = attackAnimation.placeOnMap();
        if (getFacing().getX() == -1) {
            Array<Sprite> s = new Array<>(Sprite.class);
            for (TextureRegion r : temp.getCurrentAnimation().getKeyFrames()) {
                s.add(new Sprite((TextureRegion) r));
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
            currentSkill.activate(this);
        }
    }

    public void setAttackAnimation() {
        Equipment tempEquipment = getCurrentBattler().getBattler().getEquipment(Equipment.Slot.MainHand);
        if (tempEquipment != null) {
            try {
                FieldSkill tempAnimation = tempEquipment.getAnimation();
                attackAnimation = tempAnimation.placeOnMap();
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
            FieldSkill animation = getAttackAnimation();
            animation.setX(this);
            animation.setY(this);
//            setPause((int)((this.getDelay() * (this.getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacing()).getKeyFrames().length - 1f)) * 1000f));
            addTimer(new GameTimer("Attack_Charge", this.getDelay() * animation.getChargeTime() * 1000f) {
                @Override
                public void event(Actor a) {
                    animation.playFieldSound();
                    animation.setFacing();
                    fieldState = ActorSprite.SpriteModeField.ATTACK;
                    setPause(((animation.getAnimationDelay()) * 1000f));
                    changeAnimation(currentBattler.getSprite().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacingBias()));
                    if (!getCurrentMap().getPhysicsWorld().isLocked()) {
                        animation.setMap(getCurrentMap());
                        addTimer(new GameTimer("Attack", 1000f * (getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacingBias()).getAnimationDuration() + animation.getAftercastDelay())) {
                            @Override
                            public void event(Actor a) {
                                attacking = false;
                            }

                            @Override
                            public boolean update(float delta, Actor a) {
                                attacking = true;
                                return super.update(delta, a);
                            }
                        });
                        addTimer(new GameTimer("Attack", 1000f * getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.ATTACK, getFacingBias()).getAnimationDuration()) {
                            @Override
                            public void event(Actor a) {
                                fieldState = ActorSprite.SpriteModeField.STAND;
                            }

                            @Override
                            public boolean update(float delta, Actor a) {
                                fieldState = ActorSprite.SpriteModeField.ATTACK;
                                return super.update(delta, a);
                            }
                        });
                    }
                }

                @Override
                public boolean update(float delta, Actor a) {
                    attacking = true;
//                    canAttack = false;
//                    fieldState = ActorSprite.SpriteModeField.ATTACK;
                    return super.update(delta, a);
                }

                public void clear() {
                    attacking = false;
                    fieldState = ActorSprite.SpriteModeField.STAND;
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

    public void moving(float x, float y, float delta) {
        getMainBody().setLinearVelocity(x * getBaseSpeed() * delta, y * getBaseSpeed() * delta);
    }

    public void moving(float delta) {
        boolean slowDown = Input.getKeyRepeat(slowButton) && !isJumping;
        setSpeed(getBaseSpeed() * (slowDown ? .5f : 1));
        if (isJumping) {
            setSpeed(getBaseSpeed() * 1.5f);
        }

        if (Input.getKeyPressed(jumpButton) && canDodge && !slowDown) {
            jump();
        } else {
            if (Input.getKeyRepeat(moveLeft)) {
                getMainBody().setLinearVelocity(-getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
                if (!slowDown) {
                    changeX(-1);
                }
                setWalking(true);
                fieldState = ActorSprite.SpriteModeField.WALK;
            }

            if (Input.getKeyRepeat(moveRight)) {
                getMainBody().setLinearVelocity(getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
                if (!slowDown) {
                    changeX(1);
                }
                setWalking(true);
                fieldState = ActorSprite.SpriteModeField.WALK;
            }

            if (Input.getKeyRepeat(moveUp)) {
                getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -getSpeed() * (float) (delta));
                if (!slowDown) {
                    changeY(-1);
                }
                setWalking(true);
                fieldState = ActorSprite.SpriteModeField.WALK;
            }
            if (Input.getKeyRepeat(moveDown)) {
                getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, getSpeed() * (float) (delta));
                if (!slowDown) {
                    changeY(1);
                }
                setWalking(true);
                fieldState = ActorSprite.SpriteModeField.WALK;
            }

            if (!Input.getKeyRepeat(moveLeft) && !Input.getKeyRepeat(moveRight)) {
                if (!slowDown) {
                    changeX(0);
                }
                getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);

            }

            if (!Input.getKeyRepeat(moveUp) && !Input.getKeyRepeat(moveDown)) {
                if (!slowDown) {
                    changeY(0);
                }
                getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
            }

            if (Input.getKeyPressed(menuButton)) {
                GraphicsDriver.addMenu(new DefaultMenu());
            }
            if (Input.getKeyPressed(quitButton)) {
                throw new GameOverException();
            }
        }
    }

    @Override
    public void update(float delta) {
        while (!currentBattler.getBattler().isAlive()) {
            switchBattler();
        }
        checkStatusEffects();
        if (!isWalking) {
            if (!attacking && !skilling) {
                fieldState = ActorSprite.SpriteModeField.STAND;
            }
            if (getMainBody() != null) {
                getMainBody().setLinearVelocity(0, 0);
            }
        }
        isWalking = false;

        if (canMove()) {
            moving(delta);
        } else {
            changeX(0);
            changeY(0);
        }
        if (canMove() && canAttack && !isJumping) {
            attacking(delta);
        }
        super.update(delta);
        speed = currentBattler.getSpeed();
        applySprite();

    }

    @Override
    public void updatePartial(float delta) {
        isWalking = false;
        fieldState = ActorSprite.SpriteModeField.STAND;
        if (getMainBody() != null) {
            getMainBody().setLinearVelocity(0, 0);
        }
        changeX(0);
        changeY(0);
        super.updatePartial(delta);
        speed = currentBattler.getSpeed();
        applySprite();
    }

    public void checkStatusEffects() {
        for (Battler b : getAllBattlers()) {
            for (StatusEffect status : b.getAllStatus()) {
                if (!status.equals("Normal") && !status.equals("Death")) {
                    boolean isAccountedFor = false;
                    for (GameTimer t : getTimers()) {
                        if (t.getName().equals(b.toString() + status.getName())) {
                            isAccountedFor = true;
                        }
                    }
                    if (isAccountedFor) {
                        continue;
                    }
                    addTimer(new GameTimer(b.toString() + status.getName(), 3000) {
                        StatusEffect stat = status;

                        @Override
                        public void event(Actor a) {
                            if (b.getStatus(stat) && stat.faded(b)) {
                                BattleDriver.printline(b.getName() + "'s " + stat.getName() + " faded away.");
                                b.getAllStatus().remove(stat);
                                if (b.getAllStatus().isEmpty()) {
                                    b.changeStatus(new Normal());
                                }
                                Player.this.enableMovement();
                                Player.this.setCanSkill(true);
                            } else {
                                stat.checkFieldStatus(Player.this, b, this);
                                if (!b.getStatus("Death")) {
                                    Player.this.addTimer(this);
                                    this.resetTimer();
                                    stat.incrementTurn();
                                }
                            }
                        }

                        @Override
                        public boolean update(float delta, Actor a) {
                            stat.updateFieldStatus(Player.this, b, this, delta);
                            return super.update(delta, a);
                        }

                    });
                }
            }
        }
    }

    public boolean isJumping() {
        return isJumping;
    }

    public void setMap(OverheadMap map, float x, float y) {
        setX(x);
        setY(y);
        super.setMap(map);
    }

    public float getBaseSpeed() {
        return currentBattler.getSpeed();
    }

    public void jump() {
        final int JUMP_TIME = 100;
        if (!isJumping) {
            GameTimer tempTimer = new GameTimer("JUMP", JUMP_TIME) {
                @Override
                public void event(Actor a) {
                    isJumping = false;
                    canAttack = true;
                    setMainFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
                    setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT));
                    fieldState = ActorSprite.SpriteModeField.STAND;
                    resetAnimation();
                }

                @Override
                public boolean update(float delta, Actor a) {
                    isJumping = true;
                    canAttack = false;
                    getMainBody().setLinearVelocity(3 * (getFacing().x - getFacing().x * Math.abs(getFacing().y) * (1 - .707f)) * getSpeed() * delta,
                            3 * getSpeed() * delta * (getFacing().y - getFacing().y * Math.abs(getFacing().x) * (1 - .707f)));
                    changeX(getFacing().x);
                    changeY(getFacing().y);
                    return super.update(delta, a);
                }
            };
            addTimer(new GameTimer("JUMP_DELAY", 700) {
                @Override
                public void event(Actor a) {
                    canDodge = true;
                }

                @Override
                public boolean update(float delta, Actor a) {
                    canDodge = false;
                    return super.update(delta, a);
                }
            });
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

    public void setDefaultFilter() {
        setMainFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
        setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT));
    }

    public void setSuperInvulnerability(int time) {
        GameTimer tempTimer = new GameTimer("Invulnerable", time) {
            public void event(Actor a) {
                setDefaultFilter();
            }

            public boolean update(float delta, Actor a) {
                setMainFilter(ActorCollision.CATEGORY_PLAYER, ActorCollision.CATEGORY_WALLS);
                setSensorFilter(ActorCollision.CATEGORY_PLAYER, ActorCollision.CATEGORY_EVENT);
                return super.update(delta, a);
            }

        };
        addTimer(tempTimer);
        setMainFilter(ActorCollision.CATEGORY_PLAYER, ActorCollision.CATEGORY_WALLS);
        setSensorFilter(ActorCollision.CATEGORY_PLAYER, ActorCollision.CATEGORY_EVENT);
    }

    public void setInvulnerability(int time) {
        GameTimer tempTimer = new GameTimer("Invulnerable", time) {
            @Override
            public void event(Actor a) {
                setDefaultFilter();
            }

            @Override
            public boolean update(float delta, Actor a) {
                setMainFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
                setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_EVENT));
                getMainBody().setAwake(true);
                getSensorBody().setAwake(true);
                return super.update(delta, a);
            }

        };
        addTimer(tempTimer);
        setMainFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
        setSensorFilter(ActorCollision.CATEGORY_PLAYER, (short) (ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_EVENT));
    }

    public void setFieldState(ActorSprite.SpriteModeField mode) {
        fieldState = mode;
    }

    public ActorSprite.SpriteModeField getFieldState() {
        return fieldState;
    }

    @Override
    public void generateBody(OverheadMap map) {
//        setX(getInitialX());
//        setY(getInitialY());
        super.generateBody(map);
        Filter filter = new Filter();
        filter.categoryBits = ActorCollision.CATEGORY_PLAYER;
        filter.maskBits = ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES;
        getMainFixture().setFilterData(filter);
        filter.maskBits = ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_AI_SKILL | ActorCollision.CATEGORY_EVENT;
        getSensorFixture().setFilterData(filter);

    }

    protected void applySprite() {
        if (currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias()) != null
                && currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias()).getKeyFrames().length > 0) {
            changeDuringAnimation(currentBattler.getSprite().getFieldAnimation(fieldState, getFacingBias()));
        } else {
            changeDuringAnimation(currentBattler.getSprite().getFieldAnimation(ActorSprite.SpriteModeField.STAND, getFacingBias()));
        }
    }

    private void attacking(float delta) {
        //Check for direction first
        boolean slowDown = Input.getKeyRepeat(slowButton);
        if (Input.getKeyRepeat(moveLeft) && !slowDown) {
            changeX(-1);
        }

        if (Input.getKeyRepeat(moveRight) && !slowDown) {
            changeX(1);
        }

        if (Input.getKeyRepeat(moveUp) && !slowDown) {
            changeY(-1);
        }
        if (Input.getKeyRepeat(moveDown) && !slowDown) {
            changeY(1);
        }

        if (!Input.getKeyRepeat(moveLeft) && !Input.getKeyRepeat(moveRight) && !slowDown) {
            changeX(0);
        }

        if (!Input.getKeyRepeat(moveUp) && !Input.getKeyRepeat(moveDown) && !slowDown) {
            changeY(0);
        }
        setFacing();

        if (Input.getKeyRepeat(defendButton) && getCurrentBattler().getBattler().getDefend() == 1.0) {
            defend();
        } else {
            if (Input.getKeyPressed(switchSkill)) {
                getCurrentBattler().changeSkill(1);
                currentSkill = getCurrentBattler().getCurrentSkill();
            }
            if (Input.getKeyPressed(switchBattlerButton)) {
                switchBattler();
            }

            if (Input.getKeyPressed(attackButton) && canAttack) {
                hasEvent = false;
                buttonPushFinder = (Fixture fxtr, Vector2 vctr, Vector2 vctr1, float f) -> {
                    if (fxtr.getBody().getUserData() instanceof Event && ((Event) fxtr.getBody().getUserData()).isTriggered(Event.TriggerMethod.PRESS)) {
                        ((Event) fxtr.getBody().getUserData()).run();
                        Player.this.hasEvent = true;
                        return 1;
                    }
                    return -1;
                };
                getCurrentMap().getPhysicsWorld().rayCast(buttonPushFinder, getMainBody().getPosition(),
                        (new Vector2(getFacing().getVector()).add(getMainBody().getPosition())).add(getFacing().getVector().scl(.25f)));

                if (!hasEvent) {
                    attack();
                }
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
            setDelay(currentBattler.getDelay());
            setShape(currentBattler.getShapeName());
            setMap(getCurrentMap());
            setAttackAnimation();
            currentSkill = currentBattler.getCurrentSkill();
            fieldState = ActorSprite.SpriteModeField.STAND;
            applySprite();
        }
    }

    public void switchBattler(int index) {
        if (totalPartyKill()) {
            return;
        }
//        currentBattlerIndex = index;
        if (!party.isEmpty()) {
            Collections.rotate(party, -index);

            while (!party.get(0).getBattler().isAlive()) {
                Collections.rotate(party, -1);
            }
            currentBattler = party.get(0);
            setDelay(currentBattler.getDelay());
            setShape(currentBattler.getShapeName());
            setMap(getCurrentMap());
            setAttackAnimation();
            currentSkill = currentBattler.getCurrentSkill();
            fieldState = ActorSprite.SpriteModeField.STAND;
            applySprite();
        }
    }

    public void switchSkill(FieldSkill skill) {

    }

    public FieldBattler getCurrentBattler() {
        return currentBattler;
    }

    public FieldBattler getBattler(int index) {
        return party.get(index);
    }

    public ArrayList<FieldBattler> getAllFieldBattlers() {
        return party;
    }

    public ArrayList<Battler> getAllBattlers() {
        ArrayList<Battler> allBattlers = new ArrayList<>();
        for (FieldBattler battler : party) {
            allBattlers.add(battler.getBattler());
        }
        return allBattlers;
    }

    public boolean totalPartyKill() {
        boolean isDead = true;
        for (FieldBattler member : party) {
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
        for (int i = 0; i < getAllFieldBattlers().size(); i++) {
            Sprite temp = (Sprite) (getAllFieldBattlers().get(i).getFace().getKeyFrame(getElapsedTime()));
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
            GraphicsDriver.drawMessage(batch, GraphicsDriver.getFont(),
                    getAllFieldBattlers().get(i).getBattler().getName(),
                    ((NAME_X + SUB_WIDTH * i) + GraphicsDriver.getCamera().getScreenPositionX()),
                    (NAME_Y + GraphicsDriver.getCamera().getScreenPositionY()));
            GraphicsDriver.drawMessage(batch, GraphicsDriver.getFont(),
                    "HP: " + getAllFieldBattlers().get(i).getBattler().getHP() + "/" + getAllFieldBattlers().get(i).getBattler().getMaxHP(),
                    ((NAME_X + SUB_WIDTH * i) + GraphicsDriver.getCamera().getScreenPositionX()),
                    ((NAME_Y + FONT_SIZE) + GraphicsDriver.getCamera().getScreenPositionY()));
            GraphicsDriver.drawMessage(batch, GraphicsDriver.getFont(),
                    "MP: " + getAllFieldBattlers().get(i).getBattler().getMP() + "/" + getAllFieldBattlers().get(i).getBattler().getMaxMP(),
                    ((NAME_X + SUB_WIDTH * i) + GraphicsDriver.getCamera().getScreenPositionX()),
                    ((NAME_Y + FONT_SIZE * 2f) + GraphicsDriver.getCamera().getScreenPositionY()));
            GraphicsDriver.drawMessage(batch, GraphicsDriver.getFont(),
                    getAllFieldBattlers().
                            get(i).
                            getCurrentSkill().
                            getSkill().
                            getName(),
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

            @Override
            public boolean update(float delta, Actor a) {
                disableMovement();
                return super.update(delta, a);
            }

            @Override
            public boolean isFinished() {
                return !Input.getKeyRepeat(defendButton);
            }

        });
    }

    public ActorSprite getSpriteSheet() {
        return currentBattler.getSprite();
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
            if (Math.pow(e.getX() - getX(), 2) + Math.pow(e.getY() - getY(), 2)
                    < Math.pow(temp.getX() - getX(), 2) + Math.pow(temp.getY() - getY(), 2)) {
                temp = e;
            }
        }
        return temp;
    }

    public void ouch(Battler b) {
        FieldBattler ouchBattler = getBattler(getAllBattlers().indexOf(b));
        fieldState = ActorSprite.SpriteModeField.OUCH;
        getCurrentBattler().setFace(ActorSprite.SpriteModeFace.OUCH);
        setPause(250);
        setInvulnerability(550);
        SoundDatabase.ouchSound.play();
        addTimer(new GameTimer("OUCH", 250) {
            @Override
            public void event(Actor a) {
                if (b.isAlive()) {
                    fieldState = ActorSprite.SpriteModeField.STAND;
                    ouchBattler.setFace(ActorSprite.SpriteModeFace.NORMAL);
                } else {
                    addTimer(new GameTimer("DEAD", 9999) {
                        @Override
                        public boolean isFinished() {
                            return ouchBattler.getBattler().isAlive();
                        }

                        @Override
                        public void event(Actor a) {
                            fieldState = ActorSprite.SpriteModeField.STAND;
                            ouchBattler.setFace(ActorSprite.SpriteModeFace.NORMAL);
                        }

                        @Override
                        public boolean update(float delta, Actor a) {
                            if (!b.isAlive()) {
//                                fieldState = ActorSprite.SpriteModeField.OUCH;
                                ouchBattler.setFace(ActorSprite.SpriteModeFace.OUCH);
                            }
                            return super.update(delta, a);
                        }
                    });
                }
            }

            @Override
            public boolean update(float delta, Actor a) {
                if (ouchBattler.getBattler().isAlive()) {
                    fieldState = ActorSprite.SpriteModeField.OUCH;
                    ouchBattler.setFace(ActorSprite.SpriteModeFace.OUCH);
                }
                return super.update(delta, a);
            }

        });

    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        ArrayList<String> partyNames = (ArrayList<String>) in.readObject();
        party = new ArrayList<>();
        for (String partyName : partyNames) {
            party.add(CharacterDatabase.CHARACTER_LIST.get(partyName.toUpperCase()));
        }
        initialize();
    }

    private void writeObject(ObjectOutputStream out) throws IOException, ClassNotFoundException {
        out.defaultWriteObject();
        ArrayList<String> partyNames = new ArrayList<>();
        for (FieldBattler f : party) {
            partyNames.add(f.getBattler().getName());
        }
        out.writeObject(partyNames);

    }

    public void ouch() {
        ouch(currentBattler.getBattler());
    }
}
