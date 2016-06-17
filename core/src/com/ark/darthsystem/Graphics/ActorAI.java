/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.BattlerAI;
import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Filter;
import java.util.ArrayList;

/**
 *
 * @author trankt1
 */
public class ActorAI extends Player {
    
    private Vector2 patrolCoordinates = Vector2.Zero;
    private boolean patrolling = false;
    private float speed = .4f;
    private int vision = (int) (300 / PlayerCamera.PIXELS_TO_METERS);

    public ActorAI(ArrayList<ActorBattler> getBattlers, float getX, float getY) {
        super(getBattlers, getX, getY);
    }

    public void attack() {
        setAttacking(true);
        getAttackAnimation().resetAnimation();
        setPause((getAttackAnimation().getAnimationDelay() + getAttackAnimation().getAftercastDelay()) * 1000f);
        getAttackAnimation().setX(this);
        getAttackAnimation().setY(this);
        getAttackAnimation().setFacing();
        getAttackAnimation().setMap(getCurrentMap(), false);
        getAttackAnimation().playFieldSound();
        setFieldState(ActorSprite.SpriteModeField.ATTACK);
        switch (super.getFacing()) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                changeAnimation(getCurrentBattler().getSprite().
                        getFieldAnimation(getFieldState(), Actor.Facing.UP));
                break;
            case RIGHT:
                changeAnimation(getCurrentBattler().getSprite().
                        getFieldAnimation(getFieldState(), Actor.Facing.RIGHT));
                break;
            case LEFT:
                changeAnimation(getCurrentBattler().getSprite().
                        getFieldAnimation(getFieldState(), Actor.Facing.LEFT));
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                changeAnimation(getCurrentBattler().getSprite().
                        getFieldAnimation(getFieldState(), Actor.Facing.DOWN));
                break;
            default:
        }
        getCurrentAnimation().setFrameDuration(getAttackAnimation().getAnimationDelay() / getCurrentAnimation().getKeyFrames().length);
        addTimer(new GameTimer("Attack", 10000) {
            @Override
            public void event(Actor a) {
                setFieldState(ActorSprite.SpriteModeField.STAND);
                setAttacking(false);
            }
            public boolean isFinished() {
                return getCurrentAnimation().isAnimationFinished(getElapsedTime());
            }
        });
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
        float distance = (float) Math.sqrt(
                Math.pow(GraphicsDriver.getPlayer().getX() - (this.getX()), 2)
                + Math.pow((GraphicsDriver.getPlayer().getY() - (this.getY())), 2));
        return distance < 64f / PlayerCamera.PIXELS_TO_METERS;
    }

    public boolean isInRange(float range) {
        double distance = Math.sqrt(Math.pow(GraphicsDriver.getPlayer().getX() - (this.getX()), 2) + Math.pow((GraphicsDriver.getPlayer().getY() - (this.getY())), 2));
        return distance < range;
    }
    
    public void moveTowardsPoint(float x, float y, float delta) {
        addTimer(new GameTimer("MOVE", Math.abs((x - getX()) / (speed * delta)) + Math.abs((y - getY()) / (speed * delta)) / 2f * 1000f + 1000f) {
            @Override
            public void event(Actor a) {
                patrolling = false;
            }
            public boolean update(float delta, Actor a) {
                if (x > (getX()) && x - (getX()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
                    changeX(1);
                    getMainBody().setLinearVelocity(speed * delta, getMainBody().getLinearVelocity().y);
                } else if (x < (getX()) && (getX()) - x > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
                    changeX(-1);
                    getMainBody().setLinearVelocity(-speed * delta, getMainBody().getLinearVelocity().y);
                } else {
                    changeX(0);
                    getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);
                }

                if (y > (getY()) && y - (getY()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
                    changeY(1);
                   getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, speed * (float) (delta));
                 } else if (y < (getY()) && (getY()) - y > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
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
                switch (getFacing()) {
                    case UP:
                    case UP_LEFT:
                    case UP_RIGHT:
                        changeDuringAnimation(getCurrentBattler().getSprite().getFieldAnimation(getFieldState(), Actor.Facing.UP));
                        break;
                    case RIGHT:
                        changeDuringAnimation(getCurrentBattler().getSprite().getFieldAnimation(getFieldState(), Actor.Facing.RIGHT));
                        break;
                    case LEFT:
                        changeDuringAnimation(getCurrentBattler().getSprite().getFieldAnimation(getFieldState(), Actor.Facing.LEFT));
                        break;
                    case DOWN:
                    case DOWN_LEFT:
                    case DOWN_RIGHT:
                        changeDuringAnimation(getCurrentBattler().getSprite().getFieldAnimation(getFieldState(), Actor.Facing.DOWN));
                        break;
                    default:
                }
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
        for (GameTimer t : getTimers()) {
            if (t.update(delta, this)) {
                getTimers().removeValue(t, true);
            }
        }
        if (canMove()) {
            interpretAI(delta);
        }
        setX(getMainBody().getPosition().x);
        setY(getMainBody().getPosition().y);
        
        if (!isWalking()) {
            getMainBody().setLinearVelocity(0, 0);
            if (!isAttacking()) {
                setFieldState(ActorSprite.SpriteModeField.STAND);
                switch (getFacing()) {
                    case UP:
                    case UP_LEFT:
                    case UP_RIGHT:
                        changeAnimation(getCurrentBattler().getSprite().getFieldAnimation(ActorSprite.SpriteModeField.STAND, Actor.Facing.UP));
                        break;
                    case RIGHT:
                        changeAnimation(getCurrentBattler().getSprite().getFieldAnimation(ActorSprite.SpriteModeField.STAND, Actor.Facing.RIGHT));
                        break;
                    case LEFT:
                        changeAnimation(getCurrentBattler().getSprite().getFieldAnimation(ActorSprite.SpriteModeField.STAND, Actor.Facing.LEFT));
                        break;
                    case DOWN:
                    case DOWN_LEFT:
                    case DOWN_RIGHT:
                        changeAnimation(getCurrentBattler().getSprite().getFieldAnimation(ActorSprite.SpriteModeField.STAND, Actor.Facing.DOWN));
                        break;
                    default:
                }
            }
        }
        setWalking(false);
    }
        
    public boolean vision() {
        return isInRange(vision);
    }    
    private void interpretAI(float delta) {
        if (vision()) {
            moveTowardsPlayer(delta);
            if (isInRange()) {
                attack();
            }
        }
        else if (!patrolling) {
            patrol(delta);
        }
    }
    
    private void moveTowardsPlayer(float delta) {
        if ((GraphicsDriver.getPlayer().getX()) > (this.getX()) && (GraphicsDriver.getPlayer().getX()) - (this.getX()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
            changeX(1);
            getMainBody().setLinearVelocity(speed * (float) (delta), getMainBody().getLinearVelocity().y);
        } else if ((GraphicsDriver.getPlayer().getX()) < (this.getX()) && (this.getX()) - (GraphicsDriver.getPlayer().getX()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
            changeX(-1);
            getMainBody().setLinearVelocity(-speed * (float) (delta), getMainBody().getLinearVelocity().y);
        } else {
            changeX(0);
            getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);
        }
        
        if ((GraphicsDriver.getPlayer().getY()) > (this.getY()) && (GraphicsDriver.getPlayer().getY()) - (this.getY()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
            changeY(1);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, speed * (float) (delta));
        } else if ((GraphicsDriver.getPlayer().getY()) < (this.getY()) && (this.getY()) - (GraphicsDriver.getPlayer().getY()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
            changeY(-1);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -speed * (float) (delta));
        } else {
            changeY(0);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
        }
        setFacing();
        setFieldState(ActorSprite.SpriteModeField.WALK);
        switch (getFacing()) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                changeDuringAnimation(getCurrentBattler().getSprite().getFieldAnimation(ActorSprite.SpriteModeField.WALK, Actor.Facing.UP));
                break;
            case RIGHT:
                changeDuringAnimation(getCurrentBattler().getSprite().getFieldAnimation(ActorSprite.SpriteModeField.WALK, Actor.Facing.RIGHT));
                break;
            case LEFT:
                changeDuringAnimation(getCurrentBattler().getSprite().getFieldAnimation(ActorSprite.SpriteModeField.WALK, Actor.Facing.LEFT));
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                changeDuringAnimation(getCurrentBattler().getSprite().getFieldAnimation(ActorSprite.SpriteModeField.WALK, Actor.Facing.DOWN));
                break;
            default:
        }
        this.setWalking(true);
    }
    
    private void patrol(float delta) {
        patrolling = true;
        final Actor.Facing direction = Actor.Facing.values()[(int) (Math.random() * Actor.Facing.values().length)];
        final float distance = 4;
        moveTowardsPoint(getX() + direction.x * distance, getY() + direction.getY() * distance, delta);
    }
    
    public enum State {
        PATROL,
        PURSUIT,
        ATTACK,
        RECHARGE
    }
}
