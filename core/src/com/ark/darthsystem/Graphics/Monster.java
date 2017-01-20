/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Database.Database;
import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

/**
 *
 * @author trankt1
 */
public class Monster extends Player {
    
    private Vector2 patrolCoordinates = Vector2.Zero;
    private boolean patrolling = false;
    private float speed = .2f;
    private int vision = 64;
    private float stopInterval;
    private State state;
    private Player closestPlayer = null;
    
    public Monster(ActorSprite sprites, float getX, float getY) {
        super(Actor.TeamColor.YELLOW, sprites, getX, getY);
        setMaxLife(15);
        setCurrentLife(15);
        setAttack(2);
        setShape("monster");
    }
    
    public void attack() {
        super.attack();
    }
    
    public void generateBody(OverheadMap map) {
        super.generateBody(map);
        Filter filter = new Filter();
        filter.categoryBits = ActorCollision.CATEGORY_AI;
        filter.maskBits = ActorCollision.CATEGORY_WALLS | ActorCollision.CATEGORY_OBSTACLES | ActorCollision.CATEGORY_AI | ActorCollision.CATEGORY_RED | ActorCollision.CATEGORY_BLUE;
        getMainFixture().setFilterData(filter);
        filter.maskBits = ActorCollision.CATEGORY_BLUE_SKILL | ActorCollision.CATEGORY_RED_SKILL;
        getSensorFixture().setFilterData(filter);
    }

    public ActorSkill getAttackAnimation() {
        ActorSkill temp = Database.BasicMonster();
        temp.setInvoker(this);
        return temp;
//        return attackAnimation;
    }
    
    
    public boolean isInRange() {
        final float RANGE = 228;
        if (closestPlayer == null) {
            return false;
        }
        float distance = (float) Math.sqrt(
                Math.pow(closestPlayer.getX() - (this.getX()), 2)
                + Math.pow((closestPlayer.getY() - (this.getY())), 2));
        return distance < RANGE / PlayerCamera.PIXELS_TO_METERS;
    }

    public boolean isInRange(float range) {
        double distance = Math.sqrt(Math.pow(GraphicsDriver.getPlayer().getX() - (this.getX()), 2) + Math.pow((GraphicsDriver.getPlayer().getY() - (this.getY())), 2));
        return distance < range;
    }
    
    public Player findClosestPlayer(float range, Array<Player> player) {
        closestPlayer = null;
        double distance = 99999;
        for (Player p : player) {
            if (p.getCurrentLife() <= 0) {
                continue;
            }
            double newDistance = Math.sqrt(Math.pow(p.getX() - (this.getX()), 2) + Math.pow((p.getY() - (this.getY())), 2));
            if (newDistance < distance) {
                distance = newDistance;
                closestPlayer = p;
            }
        }
        return closestPlayer;
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
                    setFieldState(ActorSprite.SpriteModeField.RUN);
                }
                else {
                    setFieldState(ActorSprite.SpriteModeField.IDLE);                    
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
//        setAttacking(false);
        setElapsedTime(getElapsedTime() + GraphicsDriver.getRawDelta());
        setCurrentImage((Sprite) getCurrentAnimation().getKeyFrame(getElapsedTime()));
        for (GameTimer t : getTimers()) {
            if (t.update(delta, this)) {
                getTimers().removeValue(t, true);
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
                setFieldState(ActorSprite.SpriteModeField.IDLE);
                resetSprite();

//                switch (getFacing()) {
//                    case RIGHT_DOWN:
//                    case RIGHT_UP:
//                        if (getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.IDLE, getFacing()) != null) {
//                           changeAnimation(getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.IDLE, getFacing()));
//                            break;
//                        }
//                     case RIGHT:
//                        changeAnimation(getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.IDLE, Actor.Facing.RIGHT));
//                        break;
//                    case LEFT_DOWN:
//                    case LEFT_UP:
//                        if (getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.IDLE, getFacing()) != null) {
//                           changeAnimation(getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.IDLE, getFacing()));
//                            break;
//                        }
//                    case LEFT:
//                        changeAnimation(getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.IDLE, Actor.Facing.LEFT));
//                        break;
//                    default:
//                }
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
        closestPlayer = findClosestPlayer(vision, getCurrentMap().getAllPlayers());
        if (isInRange()) {
            attack();
        } else if (vision()) {
            moveTowardsPlayer(delta);
        }
        else if (!patrolling) {
            patrol(delta);
        }
    }
    
    private void interpretAIState(float delta) {
        switch (state) {
            case PATROL:
                break;
            case PURSUIT:
                break;
            case ATTACK:
                break;
            case RECHARGE:
                break;
        }
    }
    
    private void moveTowardsPlayer(float delta) {
        if (closestPlayer == null) {
            return;
        }
        if ((closestPlayer.getX()) > (this.getX()) && (closestPlayer.getX()) - (this.getX()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
            changeX(1);
            getMainBody().setLinearVelocity(speed * (float) (delta), getMainBody().getLinearVelocity().y);
        } else if ((closestPlayer.getX()) < (this.getX()) && (this.getX()) - (closestPlayer.getX()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
            changeX(-1);
            getMainBody().setLinearVelocity(-speed * (float) (delta), getMainBody().getLinearVelocity().y);
        } else {
            changeX(0);
            getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);
        }
        
        if ((closestPlayer.getY()) > (this.getY()) && (closestPlayer.getY()) - (this.getY()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
            changeY(1);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, speed * (float) (delta));
        } else if ((closestPlayer.getY()) < (this.getY()) && (this.getY()) - (closestPlayer.getY()) > 4.0 / PlayerCamera.PIXELS_TO_METERS) {
            changeY(-1);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -speed * (float) (delta));
        } else {
            changeY(0);
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
        }
        setFacing();
        setFieldState(ActorSprite.SpriteModeField.RUN);
        applySprite();

//        switch (getFacing()) {
//            case RIGHT:
//                changeDuringAnimation(getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.RUN, Actor.Facing.RIGHT));
//                break;
//            case LEFT:
//                changeDuringAnimation(getSpriteSheet().getFieldAnimation(ActorSprite.SpriteModeField.RUN, Actor.Facing.LEFT));
//                break;
//            default:
//        }
        this.setWalking(true);
    }
    
    public void setAttackAnimation() {
        attackAnimation = Database.BasicMonster();
        System.out.println("MONSTER SLASH");
        attackAnimation.setInvoker(this);
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
