/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.ark.darthsystem.AI;
import com.ark.darthsystem.BattlerAI;
import static com.ark.darthsystem.graphics.ActorAI.State.*;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

/**
 *
 * @author trankt1
 */
public class ActorAI extends Player {
    
    private Vector2 patrolCoordinates = Vector2.Zero;
    private boolean patrolling = false;
    private float speed = .2f;
    private int vision = 10;
    private float stopInterval;
    private State state;
    private Player closestPlayer = null;
    private ArrayList<AI> aiData;
    
    public ActorAI(ArrayList<ActorBattler> getBattlers, float getX, float getY) {
        super(getBattlers, getX, getY);
        aiData = new ArrayList<>(Arrays.asList(((BattlerAI) (getBattlers.get(0).getBattler())).getAIData()));
    }
    
    public ActorAI clone() {
        ArrayList<ActorBattler> temp = new ArrayList<>();
        getAllActorBattlers().forEach((a) -> {
            temp.add(a.clone());
        });
        return new ActorAI(temp, getX(), getY());
    }
        
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
        for (ActorBattler b : this.getAllActorBattlers()) {
            allBattlers.add((BattlerAI) (b.getBattler()));
        }
        return (allBattlers);
    }
    
    public boolean isInRange() {
        final float RANGE = 2.2f;
        if (closestPlayer == null) {
            return false;
        }
        float distance = (float) Math.sqrt(
                Math.pow(closestPlayer.getX() - (this.getX()), 2.0)
                + Math.pow((closestPlayer.getY() - (this.getY())), 2.0));
        return distance < RANGE;
    }

    public boolean isInRange(float range) {
        double distance = Math.sqrt(Math.pow(GraphicsDriver.getPlayer().getX() - (this.getX()), 2) + Math.pow((GraphicsDriver.getPlayer().getY() - (this.getY())), 2));
        return distance < range;
    }
    
    public Player findClosestPlayer(float range, Player player) {
        return player;
    }
    
    public void moveTowardsPoint(float x, float y, float delta) {
        addTimer(new GameTimer("MOVE", Math.abs((x - getX()) / (speed * delta)) + Math.abs((y - getY()) / (speed * delta)) / 2f * 1000f + 1000f) {
            @Override
            public void event(Actor a) {
                patrolling = false;
            }
            public boolean update(float delta, Actor a) {
                final float OFFSET = 1.5f;
                if (x > (getX()) && x - (getX()) > OFFSET) {
                    changeX(1);
                    getMainBody().setLinearVelocity(speed * delta, getMainBody().getLinearVelocity().y);
                } else if (x < (getX()) && (getX()) - x > OFFSET) {
                    changeX(-1);
                    getMainBody().setLinearVelocity(-speed * delta, getMainBody().getLinearVelocity().y);
                } else {
                    changeX(0);
                    getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);
                }

                if (y > (getY()) && y - (getY()) > OFFSET) {
                    changeY(1);
                   getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, speed * (float) (delta));
                 } else if (y < (getY()) && (getY()) - y > OFFSET) {
                    changeY(-1);
                    getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -speed * (float) (delta));
                } else {
                    changeY(0);
                    getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
                }
                setFacing();
                if (!getMainBody().getLinearVelocity().isZero(.5f)) {
                    setFieldState(ActorSprite.SpriteModeField.WALK);
                }
                else {
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

    
    public void render(Batch batch) {
        Sprite currentImage = getCurrentImage();
        batch.draw(currentImage,
                this.getX() - currentImage.getOriginX(),
                this.getY() - currentImage.getOriginY(),
                currentImage.getOriginX(),
                currentImage.getOriginY(),
                currentImage.getWidth(),
                currentImage.getHeight(),
                currentImage.getScaleX() / PlayerCamera.PIXELS_TO_METERS,
                currentImage.getScaleY() / PlayerCamera.PIXELS_TO_METERS,
                currentImage.getRotation());        
    }
    
    public void update(float delta) {
        setElapsedTime(getElapsedTime() + GraphicsDriver.getRawDelta());
        setCurrentImage((Sprite) getCurrentAnimation().getKeyFrame(getElapsedTime()));
        for (int i = 0; i < getTimers().size; i++) {
            GameTimer t = getTimers().get(i);
            if (t.update(delta, this)) {
                getTimers().removeValue(t, true);
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
        state = IDLE;
        Optional<AI> skillResult = aiData.stream().filter(obj -> 
                obj.getType() == AI.Type.AttackSkill 
                || AI.Type.SupportSkill == obj.getType()
                || AI.Type.Heal == obj.getType()
        ).findFirst();
        if (skillResult.isPresent() && canSkill() && isInRange() && canAttack() && canMove()) {
            state = SKILL;
        }
        else
            if (isInRange() && canAttack() && canMove()) {
            state = ATTACK;
//            attack();
        } else if (vision() && canMove()) {
            state = PURSUIT;
//            moveTowardsPlayer(delta);
        }
        else if (!patrolling) {
            state = PATROL;
//            patrol(delta);
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
                attack();
                break;
            case SKILL:
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
        if ((closestPlayer.getX()) > (this.getX()) && (closestPlayer.getX()) - (this.getX()) > OFFSET) {
            changeX(1);
            getMainBody().setLinearVelocity(speed * (float) (delta), getMainBody().getLinearVelocity().y);
        } else if ((closestPlayer.getX()) < (this.getX()) && (this.getX()) - (closestPlayer.getX()) > OFFSET) {
            changeX(-1);
            getMainBody().setLinearVelocity(-speed * (float) (delta), getMainBody().getLinearVelocity().y);
        } else {
            changeX(0);
            getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);
        }
        
        if ((closestPlayer.getY()) > (this.getY()) && (closestPlayer.getY()) - (this.getY()) > OFFSET) {
            changeY(1);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, speed * (float) (delta));
        } else if ((closestPlayer.getY()) < (this.getY()) && (this.getY()) - (closestPlayer.getY()) > OFFSET) {
            changeY(-1);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -speed * (float) (delta));
        } else {
            changeY(0);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
        }
        setFacing();
        setFieldState(ActorSprite.SpriteModeField.WALK);
        applySprite();
        this.setWalking(true);
    }
    
    @Override
    public void setDefaultFilter() {
        setMainFilter(ActorCollision.CATEGORY_AI, (short)(ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES));
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
