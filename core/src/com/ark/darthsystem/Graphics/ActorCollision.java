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
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.joints.WeldJoint;
import com.badlogic.gdx.physics.box2d.joints.WeldJointDef;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Keven
 */
public class ActorCollision extends Actor {
    public static final short CATEGORY_AI = 0x0004;
    public static final short CATEGORY_AI_SKILL = 0x0040;
    public static final short CATEGORY_EVENT = 0x0100;
    public static final short CATEGORY_OBSTACLES = 0x0010;
    public static final short CATEGORY_RED = 0x0001;
    public static final short CATEGORY_BLUE = 0x0002;
    public static final short CATEGORY_RED_SKILL = 0x0020;
    public static final short CATEGORY_BLUE_SKILL = 0x0080;
    public static final short CATEGORY_WALLS = 0x0008;
    
    private String shapeName = "basiccircle";
    private Body body;
    private Fixture fixture;
    private Body sensorBody;
    private Fixture sensorFixture;
    private WeldJoint sensorJoint;
    private float initialX;
    private float initialY;

    public ActorCollision(Sprite[] img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
    }

    public ActorCollision(Sprite[] img, float getX, float getY, float delay, boolean destroy) {
        super(img, getX, getY, delay, destroy);
    }

    public ActorCollision(ActorSprite img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
    }

    public void setInitialX(float x) {
        initialX = x;
    }

    public void setInitialY(float y) {
        initialY = y;
    }
    
    public float getInitialX() {
        return initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public Joint getJoint() {
        return sensorJoint;
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
                float degrees = getFacing().getRotate();
                genericBodyType.angle = degrees * MathUtils.degreesToRadians;
            }
        }
        body = map.getPhysicsWorld().createBody(genericBodyType);
        fixtureDef.density = 0.1f; 
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0f;
        fixture = body.createFixture(fixtureDef);
        body.setUserData(this);
        BodyDef genericBodyType2 = new BodyDef();
        genericBodyType2.type = BodyDef.BodyType.DynamicBody;
        genericBodyType2.fixedRotation = true;
        genericBodyType2.position.set(getX(), getY());
        genericBodyType2.angularDamping = genericBodyType.angularDamping + .01f;
        genericBodyType2.angle = genericBodyType.angle;
        sensorBody = null;
        sensorBody = map.getPhysicsWorld().createBody(genericBodyType2);
        FixtureDef fixtureDef2 = new FixtureDef();
        if (CollisionDatabaseLoader.getShapes() == null || CollisionDatabaseLoader.getShapes().isEmpty()
                || (!MapDatabase.getMaps().containsKey("skillshapes") && map.getMap().getLayers().get("collisions") == null)) {
            fixtureDef2.shape = new CircleShape() {
                {
//                    setRadius(24f / GraphicsDriver.getPlayerCamera().getConversion());
                }
            };
        } else {
            fixtureDef2.shape = CollisionDatabaseLoader.getShape(shapeName);
        }
        fixtureDef2.density = 0.1f; 
        fixtureDef2.friction = 1.0f;
        fixtureDef2.restitution = 0f;
        sensorFixture = sensorBody.createFixture(fixtureDef2);
        sensorFixture.setSensor(true);
        sensorBody.setUserData(this);
        WeldJointDef def = new WeldJointDef();
        sensorJoint = null;
        def.dampingRatio = 1f;
        def.frequencyHz = 60;
        def.collideConnected = false;   
        if (sensorBody ==  body) {
            System.out.println(body);
        }
        def.initialize(sensorBody, body, new Vector2(getX(), getY()));
        sensorJoint = (WeldJoint) map.getPhysicsWorld().createJoint(def);        
        fixtureDef.shape.dispose();
        fixtureDef2.shape.dispose();
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
    }
    public void setMap(OverheadMap map) {
        if (getCurrentMap() != null) {
            Array<Body> temp = new Array<Body>();
            map.getPhysicsWorld().getBodies(temp);
            for (Body b : temp) {
                if (b.getUserData() != null && b.getUserData().equals(this)) {
                    map.removeBody(b);
                }
            }
        }
        setCurrentMap(map);
        map.addBody(this);
    }
    
    public void setSensorFilter(short category, short mask) {
        Filter f = new Filter();
        f.categoryBits = category;
        f.maskBits = mask;
        sensorFixture.setFilterData(f);
    }
    public void update(float delta) {
        super.update(delta);
        if (body != null) {
            setX(Math.round(body.getPosition().x * 100f) / 100f);
            setY(Math.round(body.getPosition().y * 100f) / 100f);
        }
    }
    public void setBodyX(float x) {
        body.getPosition().x = x;
    }
    public void setBodyY(float y) {
        body.getPosition().y = y;
    }
}
