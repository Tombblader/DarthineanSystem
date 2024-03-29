package com.ark.darthsystem.graphics;

import com.ark.darthsystem.AI;
import com.ark.darthsystem.BattlerAI;
import static com.ark.darthsystem.graphics.FieldBattlerAI.State.*;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.utils.Array;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class FieldBattlerAI extends Player implements Serializable {

    private Vector2 patrolCoordinates = new Vector2(Vector2.Zero);
    private Vector2 lastSeenPosition = new Vector2(Vector2.Zero);
    private boolean patrolling = false;
//    private float speed = .2f;
    private int vision = 10;
    private float stopInterval;
    private State state;
    private Player closestPlayer = null;
    private ArrayList<AI> aiData;
    private transient RayCastCallback rayVision;

    public FieldBattlerAI() {
    }

    public FieldBattlerAI(ArrayList<FieldBattler> getBattlers, float getX, float getY) {
        super(getBattlers, getX, getY);
        setSpeed(getBattlers.get(0).getSpeed());
        aiData = new ArrayList<>(Arrays.asList(((BattlerAI) (getBattlers.get(0).getBattler())).getAIData()));
    }

    public FieldBattlerAI(Player player) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public FieldBattlerAI clone() {
        ArrayList<FieldBattler> temp = new ArrayList<>();
        getAllFieldBattlers().forEach((a) -> {
            temp.add(a.clone());
        });
        return new FieldBattlerAI(temp, getX(), getY());
    }

    @Override
    public void generateBody(OverheadMap map) {
        super.generateBody(map);
        Filter filter = new Filter();
        filter.categoryBits = ActorCollision.CATEGORY_AI;
        filter.maskBits = ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES;
        getMainFixture().setFilterData(filter);
        filter.maskBits = ActorCollision.CATEGORY_PLAYER | ActorCollision.CATEGORY_PLAYER_SKILL;
        getSensorFixture().setFilterData(filter);
    }

    public ArrayList<BattlerAI> getAllBattlerAI() {
        ArrayList<BattlerAI> allBattlers = new ArrayList<>();
        for (FieldBattler b : this.getAllFieldBattlers()) {
            allBattlers.add((BattlerAI) (b.getBattler()));
        }
        return (allBattlers);
    }

    public boolean isInRange() {
        final float RANGE = 2.2f;
        return isInRange(RANGE);
    }

    public boolean isInRange(float range) {
        final Array<Fixture> contacts = new Array<>(Fixture.class);
        if (closestPlayer == null) {
            return false;
        }
        float distance = (float) Math.sqrt(
                Math.pow(closestPlayer.getX() - (this.getX()), 2.0)
                + Math.pow((closestPlayer.getY() - (this.getY())), 2.0));
        rayVision = (Fixture fxtr, Vector2 vctr, Vector2 vctr1, float f) -> {
            if (fxtr.getFilterData().categoryBits == ActorCollision.CATEGORY_WALLS || fxtr.getFilterData().categoryBits == ActorCollision.CATEGORY_OBSTACLES) {
                contacts.add(fxtr);
                return 0;
            }
            return -1;
        };
        getCurrentMap().getPhysicsWorld().rayCast(rayVision, getMainBody().getPosition(), new Vector2(closestPlayer.getMainBody().getPosition()));
        return contacts.size == 0 && distance < range;
    }

    public Player findClosestPlayer(float range, Player player) {
        return player;
    }

    @Override
    public void moveTowardsPoint(float x, float y, float delta) {
        addTimer(new GameTimer("MOVE", Math.abs((x - getX()) / (getCurrentBattler().getSpeed() * delta)) + Math.abs((y - getY()) / (getCurrentBattler().getSpeed() * delta)) / 2f * 1000f + 1000f) {
            @Override
            public void event(Actor a) {
                patrolling = false;
            }

            @Override
            public boolean update(float delta, Actor a) {
                final float OFFSET = 1.5f;
                if (x > (getX()) && x - (getX()) > OFFSET) {
                    changeX(1);
                    getMainBody().setLinearVelocity(getCurrentBattler().getSpeed() * delta, getMainBody().getLinearVelocity().y);
                } else if (x < (getX()) && (getX()) - x > OFFSET) {
                    changeX(-1);
                    getMainBody().setLinearVelocity(-getCurrentBattler().getSpeed() * delta, getMainBody().getLinearVelocity().y);
                } else {
                    changeX(0);
                    getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);
                }

                if (y > (getY()) && y - (getY()) > OFFSET) {
                    changeY(1);
                    getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, getCurrentBattler().getSpeed() * (float) (delta));
                } else if (y < (getY()) && (getY()) - y > OFFSET) {
                    changeY(-1);
                    getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -getCurrentBattler().getSpeed() * (float) (delta));
                } else {
                    changeY(0);
                    getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
                }
                setFacing();
                if (!getMainBody().getLinearVelocity().isZero(.5f)) {
                    setFieldState(ActorSprite.SpriteModeField.WALK);
                } else {
                    setFieldState(ActorSprite.SpriteModeField.STAND);
                }
                applySprite();
                setWalking(true);
                return super.update(delta, a);
            }

            public boolean isFinished() {
                return super.isFinished() || getMainBody().getLinearVelocity().isZero(.5f);
            }
        });
    }

    @Override
    public void update(float delta) {
        while (!getCurrentBattler().getBattler().isAlive()) {
            switchBattler();
        }
        setElapsedTime(getElapsedTime() + GraphicsDriver.getRawDelta());
        setCurrentImage((Sprite) getCurrentAnimation().getKeyFrame(getElapsedTime()));
        for (int i = 0; i < getTimers().size(); i++) {
            GameTimer t = getTimers().get(i);
            if (t.update(delta, this)) {
                getTimers().remove(i);
                i--;
            }
        }
        if (canMove() && !isAttacking()) {
            interpretAI(delta);
        }
        setX(getMainBody().getPosition().x);
        setY(getMainBody().getPosition().y);

        if (!isWalking()) {
            getMainBody().setLinearVelocity(0, 0);
            if (!isAttacking()) {
                setFieldState(ActorSprite.SpriteModeField.STAND);
                resetSprite();
            } else {
                applySprite();
            }
        }
        setWalking(false);
    }

    public boolean vision() {
        return isInRange(vision);
    }

    private void interpretAI(float delta) {
        closestPlayer = findClosestPlayer(vision, GraphicsDriver.getPlayer());
        if (state == null) {
            state = IDLE;
        }
        Optional<AI> skillResult = aiData.stream().filter(obj
                -> obj.getType() == AI.Type.AttackSkill
                || AI.Type.SupportSkill == obj.getType()
                || AI.Type.Heal == obj.getType()
        ).findFirst();
        if (skillResult.isPresent() && canSkill() && isInRange() && canAttack() && canMove()) {
            for (int i = 0; i < getTimers().size(); i++) {
                if (getTimers().get(i).getName().equalsIgnoreCase("MOVE")) {
                    getTimers().remove(i);
                    i--;
                }
            }
            state = SKILL;
        } else if (isInRange() && canAttack() && canMove()) {
            for (int i = 0; i < getTimers().size(); i++) {
                if (getTimers().get(i).getName().equalsIgnoreCase("MOVE")) {
                    getTimers().remove(i);
                    i--;
                }
            }
            state = ATTACK;
        } else if (vision() && canMove()) {
            state = PURSUIT;
//            moveTowardsPlayer(delta);
        } else if (!patrolling) {
            if (state == PURSUIT) {
                moveTowardsPoint(lastSeenPosition.x, lastSeenPosition.y, delta);
                patrolling = true;
                state = IDLE;
            } else {
                state = PATROL;
            }
//            patrol(delta);
        } else {
            state = IDLE;
        }
        interpretAIState(delta);
    }

    private void interpretAIState(float delta) {
        switch (state) {
            case PATROL:
                patrol(delta);
                break;
            case PURSUIT:
                moveTowardsPlayer(delta);
                break;
            case ATTACK:
                moveTowardsPlayer(0f);
                setFacing();
                attack();
                break;
            case SKILL:
                moveTowardsPlayer(0f);
                setFacing();
                skill();
            case RECHARGE:
                break;
        }
    }

    private void moveTowardsPlayer(float delta) {
        final float OFFSET = 1f;
        if (closestPlayer == null) {
            return;
        }
        float closestX = closestPlayer.getX();
        float closestY = closestPlayer.getY();
        final Array<Fixture> contacts = new Array<>(Fixture.class);
        rayVision = (Fixture fxtr, Vector2 vctr, Vector2 vctr1, float f) -> {
            if (fxtr.getFilterData().categoryBits == ActorCollision.CATEGORY_WALLS || fxtr.getFilterData().categoryBits == ActorCollision.CATEGORY_OBSTACLES) {
                contacts.add(fxtr);
                return 0;
            }
            return -1;
        };
//        getCurrentMap().getPhysicsWorld().rayCast(rayVision, getMainBody().getPosition().add(getMainFixture().getShape().getRadius()), new Vector2(closestPlayer.getMainBody().getPosition()));

        patrolling = false;
        if ((closestX) > (getX()) && (closestX) - (getX()) > OFFSET) {
            changeX(1);
            getMainBody().setLinearVelocity(getCurrentBattler().getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
        } else if ((closestX) < (getX()) && (getX()) - (closestX) > OFFSET) {
            changeX(-1);
            getMainBody().setLinearVelocity(-getCurrentBattler().getSpeed() * (float) (delta), getMainBody().getLinearVelocity().y);
        } else {
            changeX(0);
            getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);
        }

        if ((closestY) > (getY()) && (closestY) - (getY()) > OFFSET) {
            changeY(1);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, getCurrentBattler().getSpeed() * (float) (delta));
        } else if ((closestY) < (getY()) && (getY()) - (closestY) > OFFSET) {
            changeY(-1);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -getCurrentBattler().getSpeed() * (float) (delta));
        } else {
            changeY(0);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
        }
        lastSeenPosition.x = closestX;
        lastSeenPosition.y = closestY;
        setFacing();
        setFieldState(ActorSprite.SpriteModeField.WALK);
        applySprite();
        this.setWalking(true);
    }

    @Override
    public void setDefaultFilter() {
        setMainFilter(ActorCollision.CATEGORY_AI, (short) (ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
        setSensorFilter(ActorCollision.CATEGORY_AI, (short) (CATEGORY_PLAYER | ActorCollision.CATEGORY_PLAYER_SKILL));
    }

    @Override
    public void setInvulnerability(int time) {
        GameTimer tempTimer = new GameTimer("Invulnerable", time) {
            @Override
            public void event(Actor a) {
                setDefaultFilter();
            }

            @Override
            public boolean update(float delta, Actor a) {
                setMainFilter(ActorCollision.CATEGORY_AI, ActorCollision.CATEGORY_WALLS);
                setSensorFilter(ActorCollision.CATEGORY_AI, ActorCollision.CATEGORY_PLAYER);
                getMainBody().setAwake(true);
                getSensorBody().setAwake(true);
                return super.update(delta, a);
            }

        };
        addTimer(tempTimer);
        setMainFilter(ActorCollision.CATEGORY_AI, ActorCollision.CATEGORY_WALLS);
        setSensorFilter(ActorCollision.CATEGORY_AI, ActorCollision.CATEGORY_PLAYER);
    }

    private void patrol(float delta) {
        patrolling = true;
        final Actor.Facing direction = Actor.Facing.values()[(int) (Math.random() * Actor.Facing.values().length)];
        final float distance = 4;
        moveTowardsPoint(getX() + direction.getX() * distance, getY() + direction.getY() * distance, delta);
    }

    public enum State {
        IDLE,
        PATROL,
        PURSUIT,
        ATTACK,
        SKILL,
        RECHARGE
    }
}
