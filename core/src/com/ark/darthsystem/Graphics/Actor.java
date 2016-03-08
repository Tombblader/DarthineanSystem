/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Fundamental 2dGameprogramming in java
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;

/**
 * The Actor class is the basic entity class that directly interacts with the
 * game. It comes with basic graphics functions such as animation, facing,
 * movement.
 *
 */
public class Actor {

    private boolean isRotate;
    private OverheadMap currentMap;

    public enum Facing {

        UP(0, -1, 0),
        LEFT(-1, 0, 270),
        RIGHT(1, 0, 90),
        DOWN(0, 1, 180),
        NONE(0, 0, 0),
        DOWN_LEFT(-1, 1, 225),
        DOWN_RIGHT(1, 1, 135),
        UP_LEFT(-1, -1, 315),
        UP_RIGHT(1, -1, 45);

        float x, y;
        float rotate;

        private Facing(int x, int y, float rotate) {
            this.x = x;
            this.y = y;
            this.rotate = rotate;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getRotate() {
            return rotate;
        }
    }

    private Animation animation;
    private Sprite currentImage;
    private Sprite[] images;

    private float delay;
    private boolean destroyAfterAnimation = false;
    private float elapsed = 0f;
    private Facing facing;
    private boolean isInvulnerable = false;
    private boolean isMovable = true;
    private float lastX;

    private int lastXFacing = 0;
    private float lastY;
    private int lastYFacing = 0;
    private int nextFrame = 0;
    private int pauseTime = 0;
    private float speed;
    private ActorSprite sprite;
    private Array<GameTimer> timers = new Array<>();
    private float x;
    private float y;

    public Actor(Sprite img,
            float getX,
            float getY) {
        this.delay = 16;
        x = getX;
        y = getY;
        currentImage = img;
        isMovable = false;
        speed = 0;
        destroyAfterAnimation = false;
        facing = Facing.NONE;
        isRotate = false;
    }

    public Actor(Sprite[] img,
            float getX,
            float getY,
            float delay) {
        x = getX;
        y = getY;
        this.delay = delay;
        images = img;
        if (images != null) {
            animation = new Animation(delay, img);
            animation.setPlayMode(PlayMode.LOOP);
            currentImage = (Sprite) animation.getKeyFrame(0);
        }
        speed = 0;
        destroyAfterAnimation = false;
        facing = Facing.NONE;
        isRotate = false;
    }

    public Actor(Sprite[] img,
            float getX,
            float getY,
            float delay,
            boolean destroy) {
        this(img, getX, getY, delay);
        destroyAfterAnimation = destroy;
        animation.setPlayMode(PlayMode.LOOP);
        isRotate = false;
    }

    public Actor(ActorSprite img,
            float getX,
            float getY,
            float delay) {
        sprite = img;
        x = getX;
        y = getY;
        animation = sprite.getFieldAnimation(ActorSprite.SpriteModeField.STAND, Facing.DOWN);
        animation.setPlayMode(PlayMode.LOOP);
        animation.setFrameDuration(delay);
        currentImage = (Sprite) animation.getKeyFrame(elapsed);
        isMovable = true;
        speed = 0;
        this.delay = delay;
        destroyAfterAnimation = false;
        facing = Facing.NONE;
        isRotate = false;
    }

    public void addTimer(GameTimer t) {
        timers.add(t);
    }

    public boolean canMove() {
        return isMovable;
    }

    public void disableMovement() {
        isMovable = false;
    }

    public void dispose() {
    }

    public void enableMovement() {
        isMovable = true;
    }

    public float getDelay() {
        return delay;
    }

    public Facing getFacing() {
        return facing;
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastXFacing() {
        return facing.getX();
    }

    public float getLastY() {
        return lastY;
    }

    public float getLastYFacing() {
        return facing.getY();
    }

    public float getSpeed() {
        return speed;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isInvulnerable() {
        return isInvulnerable;
    }

    public void render(Batch batch) {
        if (currentImage != null) {
            batch.draw(currentImage,
                    this.getX() - currentImage.getOriginX(),
                    this.getY() - currentImage.getOriginY(),
                    currentImage.getOriginX(),
                    currentImage.getOriginY(),
                    currentImage.getWidth(),
                    currentImage.getHeight(),
                    currentImage.getScaleX() / GraphicsDriver.getCurrentCamera().getConversion(),
                    currentImage.getScaleY() / GraphicsDriver.getCurrentCamera().getConversion(),
                    currentImage.getRotation());
        }
    }

    public void setFacing() {
        for (Facing f : Facing.values()) {
            if (lastXFacing == f.getX() && lastYFacing == f.getY() && f != Facing.NONE) {
                facing = f;
            }
        }
    }

    public void setFacing(Facing direction) {
        facing = direction;
    }

    public void setInvulnerability(int time) {
        GameTimer tempTimer = new GameTimer("Invulnerable", time) {
            public void event(Actor a) {
                a.isInvulnerable = false;
            }

            public boolean update(float delta, Actor a) {
                a.isInvulnerable = true;
                return super.update(delta, a);
            }
        };
        timers.add(tempTimer);

    }

    public void setPause(float time) {
        GameTimer tempTimer = new GameTimer("Pause", time) {
            public void event(Actor a) {
                a.isMovable = true;
            }

            public boolean update(float delta, Actor a) {
                a.isMovable = false;
                return super.update(delta, a);
            }
        };
        timers.add(tempTimer);
        isMovable = false;
    }

    public boolean isRotate() {
        return isRotate;
    }

    public void setIsRotate(boolean getRotate) {
        isRotate = getRotate;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setX(float getX) {
        x = getX;
    }

    public void setY(float getY) {
        y = getY;
    }

    public void changeX(float getX) {
        lastX = x;
        x += getX;
        lastXFacing = x > lastX ? 1 : x == lastX ? 0 : -1;

    }

    public void changeY(float getY) {
        lastY = y;
        y += getY;
        lastYFacing = y > lastY ? 1 : y == lastY ? 0 : -1;

    }

    public void update(float delta) {
        if (currentImage != null) {
            elapsed += delta / 1000f;
            currentImage = (Sprite) animation.getKeyFrame(elapsed);
        }
        for (GameTimer t : timers) {
            if (t.update(delta, this)) {
                timers.removeValue(t, true);
            }
        }
        if (animation != null && elapsed > animation.getAnimationDuration()) {
            elapsed = 0;
        }
        setFacing();
    }

    public ActorSprite getSpriteSheet() {
        return sprite;
    }

    public Animation getCurrentAnimation() {
        return animation;
    }

    public void changeAnimation(Animation a) {
        elapsed = 0;
        a.setFrameDuration(delay);
        animation = a;
        currentImage = (Sprite) a.getKeyFrame(0);
    }

    public void changeDuringAnimation(Animation a) {
        a.setFrameDuration(delay);
        animation = a;
        currentImage = (Sprite) a.getKeyFrame(elapsed);
    }

    public void resetAnimation() {
        if (images != null) {
            elapsed = 0;
            animation = new Animation(delay, images);
            currentImage = (Sprite) animation.getKeyFrame(0);
        }
    }

    public Sprite getCurrentImage() {
        return currentImage;
    }
    
    public int getCurrentImageIndex() {
        return animation.getKeyFrameIndex(elapsed);
    }
    
    public void setCurrentImage(Sprite s) {
        currentImage = s;
    }
    
    public Array<GameTimer> getTimers() {
        return timers;
    }
    
    public boolean isFinished() {
        return destroyAfterAnimation &&
                animation.getKeyFrameIndex(elapsed) >= animation.getKeyFrames().length - 1;
    }

    public float getWidth() {
        return currentImage.getRegionWidth();
    }

    public float getHeight() {
        return currentImage.getRegionHeight();
    }
    
    public float getElapsedTime() {
        return elapsed;
    }
    
    public void setElapsedTime(float delta) {
        elapsed = delta;
    }

    public OverheadMap getCurrentMap() {
        return currentMap;
    }
    
    public void setMap(OverheadMap map, boolean isPlayer) {
        currentMap = map;
        currentMap.addActor(this, isPlayer);
        setX(x);
        setY(y);
        
    }
    
    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }

}
