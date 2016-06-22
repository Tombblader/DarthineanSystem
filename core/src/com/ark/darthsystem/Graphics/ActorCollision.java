/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Database.CollisionDatabaseLoader;
import com.ark.darthsystem.Database.MapDatabase;
import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Keven
 */
public class ActorCollision extends Actor {
    public static final short CATEGORY_AI = 0x0002;
    public static final short CATEGORY_AI_SKILL = 0x0020;
    public static final short CATEGORY_EVENT = 0x0040;
    public static final short CATEGORY_OBSTACLES = 0x0008;
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_PLAYER_SKILL = 0x0010;
    public static final short CATEGORY_WALLS = 0x0004;
    
    private String shapeName = "basiccircle";
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
    
    public void setShape(String name) {
        shapeName = name;
    }
    
    public void generateBody(OverheadMap map) {        
        BodyDef genericBodyType = new BodyDef();
        genericBodyType.type = BodyDef.BodyType.DynamicBody;
        genericBodyType.fixedRotation = true;
        genericBodyType.position.set(getX(), getY());
        FixtureDef fixtureDef = new FixtureDef();
        if (CollisionDatabaseLoader.getShapes() == null || CollisionDatabaseLoader.getShapes().isEmpty()
                || (!MapDatabase.getMaps().containsKey("skillshapes") && map.getMap().getLayers().get("collisions") == null)) {
            fixtureDef.shape = new CircleShape() {
                {
//                    setRadius(24f / GraphicsDriver.getPlayerCamera().getConversion());
                }
            };
        } else {
            fixtureDef.shape = CollisionDatabaseLoader.getShape(shapeName);
            if (fixtureDef.shape instanceof PolygonShape) {
                float degrees = 0;
                switch (this.getFacing()) {
                    case UP:
                        degrees = 0;
                        break;
                    case UP_LEFT:
                        degrees = 360 - 45;
                        break;
                    case UP_RIGHT:
                        degrees = 45;
                        break;
                    case RIGHT:
                        degrees = 90;
                        break;
                    case DOWN_RIGHT:
                        degrees = 135;
                        break;
                    case DOWN:
                        degrees = 180;
                        break;
                    case DOWN_LEFT:
                        degrees = 225;
                        break;
                    case LEFT:
                        degrees = 270;
                        break;
                }
                genericBodyType.angle = degrees * MathUtils.degreesToRadians;
            }
        }
        body = map.getPhysicsWorld().createBody(genericBodyType);
        
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
        if (!sensorBody.equals(body)) { //Bandage Problem.  Why would the bodies the same?
            def.initialize(sensorBody, body, new Vector2(getX(), getY()));
        }
        sensorJoint = (WeldJoint) map.getPhysicsWorld().createJoint(def);        
        
//        fixtureDef.shape.dispose();
    }
    
    
    
    public Body getMainBody() {
        return body;
    }
    

    public void setMainBody(Body body) {
        this.body = body;
    }
    
    
    public Fixture getMainFixture() {
        return fixture;
    }
    public void setPause(float pause) {
        super.setPause(pause);
        body.setLinearVelocity(0, 0);
    }
    public Body getSensorBody() {
        return sensorBody;
    }
    public void setSensorBody(Body body) {
        sensorBody = body;
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
    public void setMap(OverheadMap map, boolean isPlayer) {
        if (getCurrentMap() != null) {
            Array<Body> temp = new Array<Body>();
            map.getPhysicsWorld().getBodies(temp);
            for (Body b : temp) {
                if (b.getUserData() != null && b.getUserData().equals(this)) {
                    map.removeBody(b);
                }
            }
        }
        super.setMap(map, isPlayer);
//        Array<Body> temp = new Array<Body>();
//        map.getPhysicsWorld().getBodies(temp);
//        if (temp.contains(body, true)) {
//            body.setActive(true);
//            body.setTransform(getX(), getY(), 0);
//            sensorBody.setActive(true);
//            sensorBody.setTransform(getX(), getY(), 0);
//        }
//        else {
        if (!map.getPhysicsWorld().isLocked()) {
            generateBody(map);
        } else {
              map.addBody(this);
        }
//        }

    }
    
    public void setSensorFilter(short category, short mask) {
        Filter f = new Filter();
        f.categoryBits = category;
        f.maskBits = mask;
        sensorFixture.setFilterData(f);
    }
    public void update(float delta) {
        super.update(delta);
        setX(Math.round(body.getPosition().x * 100f) / 100f);
        setY(Math.round(body.getPosition().y * 100f) / 100f);
    }
}
