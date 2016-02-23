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
import com.badlogic.gdx.physics.box2d.Filter;
import java.util.ArrayList;

/**
 *
 * @author trankt1
 */
public class ActorAI extends Player {
    private float speed = 20;
    private int vision = (int) (300 / PlayerCamera.PIXELS_TO_METERS);

    public ActorAI(ArrayList<ActorBattler> getBattlers, float getX, float getY) {
        super(getBattlers, getX, getY);
    }

    public void update(float delta) {
        setElapsedTime(getElapsedTime() + GraphicsDriver.getRawDelta());
        setCurrentImage((Sprite) getCurrentAnimation().getKeyFrame(getElapsedTime()));
        for (GameTimer t : getTimers()) {
            if (t.update(delta,
                    this)) {
                getTimers().remove(t);
            }
        }
        if (canMove()) {
            if (vision()) {
                moveTowardsPlayer(delta);
                if (isInRange()) {
                    attack();
                }
            }
        }
        setX(getMainBody().getPosition().x);
        setY(getMainBody().getPosition().y);

        if (!isWalking()) {
            getMainBody().setLinearVelocity(0, 0);
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
        setWalking(false);
    }

    public void attack() {
        setAttacking(true);
        getAttackAnimation().resetAnimation();
        setPause((getAttackAnimation().getAnimationDelay() + getAttackAnimation().getAftercastDelay()) * 1000f);
        getAttackAnimation().setX(this);
        getAttackAnimation().setY(this);
        getAttackAnimation().setMap(getCurrentMap(), false);
    }

    public boolean isInRange() {
        double distance = Math.sqrt(Math.pow(GraphicsDriver.getPlayer().getX() - (this.getX()), 2) + Math.pow((GraphicsDriver.getPlayer().getY() - (this.getY())), 2));
        return distance < 80 / PlayerCamera.PIXELS_TO_METERS;
    }

    public boolean isInRange(int range) {
        double distance = Math.sqrt(Math.pow(GraphicsDriver.getPlayer().getX() - (this.getX()), 2) + Math.pow((GraphicsDriver.getPlayer().getY() - (this.getY())), 2));
        return distance < range;
    }

    private void moveTowardsPlayer(float delta) {
        if ((GraphicsDriver.getPlayer().getX()) > (this.getX()) && (GraphicsDriver.getPlayer().getX()) - (this.getX()) > 4.0) {
            getMainBody().setLinearVelocity(speed * (float) (delta), getMainBody().getLinearVelocity().y);
            changeX(1);
        } else if ((GraphicsDriver.getPlayer().getX()) < (this.getX()) && (this.getX()) - (GraphicsDriver.getPlayer().getX()) > 4.0) {
            getMainBody().setLinearVelocity(-speed * (float) (delta), getMainBody().getLinearVelocity().y);
            changeX(-1);
        } else {
            getMainBody().setLinearVelocity(0, getMainBody().getLinearVelocity().y);
            changeX(0);
        }

        if ((GraphicsDriver.getPlayer().getY()) > (this.getY()) && (GraphicsDriver.getPlayer().getY()) - (this.getY()) > 4.0) {
           getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, speed * (float) (delta));
            changeY(1);
         } else if ((GraphicsDriver.getPlayer().getY()) < (this.getY()) && (this.getY()) - (GraphicsDriver.getPlayer().getY()) > 4.0) {
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, -speed * (float) (delta));
            changeY(-1);
        } else {
            getMainBody().setLinearVelocity(getMainBody().getLinearVelocity().x, 0);
            changeY(0);
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

    public ArrayList<BattlerAI> getAllBattlerAI() {
        ArrayList<BattlerAI> allBattlers = new ArrayList<>();
        for (ActorBattler b : this.getAllActorBattlers()) {
            allBattlers.add((BattlerAI) (b.getBattler()));
        }
        return (allBattlers);
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
    
    public void render(Batch batch) {
        Sprite currentImage = getCurrentImage();
        batch.draw(currentImage,
                (int) this.getX() - currentImage.getOriginX(),
                (int) this.getY() - currentImage.getOriginY(),
                currentImage.getOriginX(),
                currentImage.getOriginY(),
                currentImage.getWidth(),
                currentImage.getHeight(),
                currentImage.getScaleX() / PlayerCamera.PIXELS_TO_METERS,
                currentImage.getScaleY() / PlayerCamera.PIXELS_TO_METERS,
                currentImage.getRotation());        
    }
        
    public boolean vision() {
        return isInRange(vision);
    }    
}
