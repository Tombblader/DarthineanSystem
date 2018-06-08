/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.ark.darthsystem.database.CollisionDatabaseLoader;
import com.ark.darthsystem.database.MapDatabase;
import com.ark.darthsystem.states.OverheadMap;
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
import java.io.Serializable;

/**
 *
 * @author Keven
 */
public class ActorCollision extends Actor implements Serializable {
    public static final short CATEGORY_AI = 0x0002;
    public static final short CATEGORY_AI_SKILL = 0x0020;
    public static final short CATEGORY_EVENT = 0x0040;
    public static final short CATEGORY_OBSTACLES = 0x0008;
    public static final short CATEGORY_PLAYER = 0x0001;
    public static final short CATEGORY_PLAYER_SKILL = 0x0010;
    public static final short CATEGORY_WALLS = 0x0004;
    
    private static final long serialUID = 123524l;
    
    private String shapeName;
    private transient Body body;
    private transient Fixture fixture;
    private transient Body sensorBody;
    private transient Fixture sensorFixture;
    private transient WeldJoint sensorJoint;
//    private float initialX;
//    private float initialY;

    public ActorCollision(String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
        shapeName = "basiccircle";
    }

    public ActorCollision(String img, float getX, float getY, float delay, String shape) {
        super(img, getX, getY, delay);
        shapeName = shape;
    }
    
    
    public ActorCollision(String img, float getX, float getY, float delay, boolean destroy) {
        super(img, getX, getY, delay, destroy);
        shapeName = "basiccircle";
    }
    
    public ActorCollision(String img, float getX, float getY, float delay, boolean destroy, String shape) {
        this(img, getX, getY, delay, destroy);
        shapeName = shape;
    }

//    public ActorCollision(ActorSprite img, float getX, float getY, float delay) {
//        super(img, getX, getY, delay);
//        shapeName = "basiccircle";
//    }
//
//    public ActorCollision(ActorSprite img, float getX, float getY, float delay, String shape) {
//        super(img, getX, getY, delay);
//        shapeName = shape;
//    }

//    public void setInitialX(float x) {
//        initialX = x;
//    }
//
//    public void setInitialY(float y) {
//        initialY = y;
//    }
//    
//    public float getInitialX() {
//        return initialX;
//    }
//
//    public float getInitialY() {
//        return initialY;
//    }

    public Joint getJoint() {
        return sensorJoint;
    }
    
    public void setShape(String name) {
        shapeName = name;
    }
    
    public String getShape() {
        return shapeName;
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
                    case LEFT_UP:
                        degrees = 315;
                        break;
                    case RIGHT_UP:
                        degrees = 45;
                        break;
                    case RIGHT:
                        degrees = 90;
                        break;
                    case RIGHT_DOWN:
                        degrees = 135;
                        break;
                    case DOWN:
                        degrees = 180;
                        break;
                    case LEFT_DOWN:
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
        BodyDef genericBodyType2 = new BodyDef();
        genericBodyType2.type = BodyDef.BodyType.DynamicBody;
        genericBodyType2.fixedRotation = true;
        genericBodyType2.position.set(getX(), getY());
        genericBodyType2.angularDamping = genericBodyType.angularDamping + .01f;
        genericBodyType2.angle = genericBodyType.angle;
        sensorBody = null;
        sensorBody = map.getPhysicsWorld().createBody(genericBodyType2);
        FixtureDef fixtureDef2 = new FixtureDef();
//        if (CollisionDatabaseLoader.getShape(shapeName) == null) {
//            fixtureDef2.shape = new CircleShape();
//            
//        }
//        else {
//            fixtureDef2.shape = CollisionDatabaseLoader.getShape(shapeName);
//            
//        }
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
    
    @Override
    public void setMap(OverheadMap map) {
        if (getCurrentMap() != null) {
            Array<Body> temp = new Array<>(Body.class);
            map.getPhysicsWorld().getBodies(temp);
            map.removeActor(this);
        }
        setCurrentMap(map);
        map.addBody(this);
    }

    public void setMap(String map) {
        OverheadMap newMap = MapDatabase.getMaps().get(map);
        if (getCurrentMap() != null) {
            Array<Body> temp = new Array<>(Body.class);
            newMap.getPhysicsWorld().getBodies(temp);
            for (Body b : temp) {
                if (b.getUserData() != null && b.getUserData().equals(this)) {
                    newMap.removeBody(b);
                }
            }
        }
        setCurrentMap(newMap);
        newMap.addBody(this);
    }    
    
    public void setSensorFilter(short category, short mask) {
        Filter f = new Filter();
        f.categoryBits = category;
        f.maskBits = mask;
        sensorFixture.setFilterData(f);
    }
    @Override
    public void update(float delta) {
        super.update(delta);
        if (body != null) {
            setX(Math.round(body.getPosition().x * 32f) / 32f);
            setY(Math.round(body.getPosition().y * 32f) / 32f);
        }
    }
    public void setBodyX(float x) {
        body.setTransform(x, body.getPosition().y, body.getAngle());
    }
    public void setBodyY(float y) {
        body.setTransform(body.getPosition().x, y, body.getAngle());
    }
}
