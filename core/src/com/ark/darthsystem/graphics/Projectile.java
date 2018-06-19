/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Skill;

/**
 *
 * @author keven
 */
public class Projectile extends FieldSkill {
    private Battler battler;
    private float speed = .5f;
    
    public Projectile(String img, float getX, float getY, float delay, Skill getSkill) {
        super(img, getX, getY, delay, getSkill);
    }
    
    public Battler getBattler() {
        return battler;
    }
    
    public void setBattler(Battler b) {
        battler = b;
    }
    
    public void moveTowardsPoint(float x, float y, float delta) {
        addTimer(new GameTimer("MOVE", Math.abs((x - getX()) / (speed * delta)) + Math.abs((y - getY()) / (speed * delta)) / 2f * 1000f + 1000f) {
            @Override
            public void event(Actor a) {
//                patrolling = false;
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
                return super.update(delta, a);
            }
            public boolean isFinished() {
                return super.isFinished() || getMainBody().getLinearVelocity().isZero(.5f);
            }
        });
    }        

    private void moveTowardsPlayer(float delta) {
        final float OFFSET = 1f;
        Player closestPlayer = GraphicsDriver.getPlayer();
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
    }

}
