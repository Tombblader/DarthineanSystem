/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Keven
 */
public class ActorCollision extends Actor {
    public static final short CATEGORY_WALLS = 0x0004;
    public static final short CATEGORY_OBSTACLES = 0x0008;
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_AI = 0x0002;
    public static final short CATEGORY_PLAYER_SKILL = 0x0010;
    public static final short CATEGORY_AI_SKILL = 0x0020;
    public static final short CATEGORY_EVENT = 0x0040;
    
    private Body body;
    private Fixture fixture;
    private Body sensorBody;
    private Fixture sensorFixture;
    private WeldJoint sensorJoint;

    public ActorCollision(Sprite[] img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
    }

    public ActorCollision(Sprite[] img, float getX, float getY, float delay, boolean destroy) {
        super(img, getX, getY, delay, destroy);
    }

    public ActorCollision(ActorSprite img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
    }
    
    public void generateBody(OverheadMap map) {        
        BodyDef genericBodyType = new BodyDef();
        genericBodyType.type = BodyDef.BodyType.DynamicBody;
        genericBodyType.fixedRotation = true;
        genericBodyType.position.set(getX(), getY());
        body = map.getPhysicsWorld().createBody(genericBodyType);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = new CircleShape() {
            {
                setRadius(12f);
            }
        };
        fixtureDef.density = 0.1f; 
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0f;
        fixture = body.createFixture(fixtureDef);
        body.setUserData(this);
        genericBodyType.type = BodyDef.BodyType.DynamicBody;
        sensorBody = map.getPhysicsWorld().createBody(genericBodyType);
        sensorFixture = sensorBody.createFixture(fixtureDef);
        sensorFixture.setSensor(true);
        sensorBody.setUserData(this);
        WeldJointDef def = new WeldJointDef();
        def.dampingRatio = 1f;
        def.frequencyHz = 60;
        def.collideConnected = false;
        def.initialize(sensorBody, body, new Vector2(getX(), getY()));        
        sensorJoint = (WeldJoint) map.getPhysicsWorld().createJoint(def);        
        
        fixtureDef.shape.dispose();
    }    
    
    public Body getMainBody() {
        return body;
    }
    
    public Body getSensorBody() {
        return sensorBody;
    }

    public void setMainBody(Body body) {
        this.body = body;
    }
    
    public void setSensorBody(Body body) {
        sensorBody = body;
    }    
    
    public void update(float delta) {
        super.update(delta);
        setX(body.getPosition().x);
        setY(body.getPosition().y);        
    }
    
    public void setMap(OverheadMap map, boolean isPlayer) {
        super.setMap(map, isPlayer);
        Array<Body> temp = new Array<Body>();
        map.getPhysicsWorld().getBodies(temp);
        if (temp.contains(body, false)) {
            body.setActive(true);
            body.setTransform(getX(), getY(), 0);
            sensorBody.setActive(true);
            sensorBody.setTransform(getX(), getY(), 0);
        }
        else {
            generateBody(map);
        }
        
    }
    
    public Fixture getMainFixture() {
        return fixture;
    }

    public Fixture getSensorFixture() {
        return sensorFixture;
    }
    
    public void setMainFilter(short category, short mask) {
        Filter f = new Filter();
        f.categoryBits = category;
        f.maskBits = mask;
        fixture.setFilterData(f);
//        sensorFixture.setFilterData(f);
    }
    
    public void setSensorFilter(short category, short mask) {
        Filter f = new Filter();
        f.categoryBits = category;
        f.maskBits = mask;
        sensorFixture.setFilterData(f);
    }
    
    public void setPause(float pause) {
        super.setPause(pause);
        body.setLinearVelocity(0, 0);
    }
}
