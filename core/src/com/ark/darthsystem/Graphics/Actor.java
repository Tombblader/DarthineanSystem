/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Fundamental 2dGameprogramming in java
 */
package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Item;
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
    
    public enum TeamColor {
        RED,
        BLUE,
        YELLOW        
    }
    private Animation animation;
    private Sprite currentImage;
    private OverheadMap currentMap;
    private float delay;
    private boolean destroyAfterAnimation = false;
    private float elapsed = 0f;
    private Facing facing;
    private Sprite[] images;
    private boolean isInvulnerable = false;
    private boolean isMovable = true;
    private boolean isRotate;
    private float lastX;
    private int lastXFacing = 0;
    private float lastY;
    private int lastYFacing = 0;
    private float speed;
    private ActorSprite sprite;
    private Array<GameTimer> timers = new Array<>();
    private float x;
    private float y;
    private int maxLife;
    private int currentLife;
    private int attack;
    private Item item;

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
        facing = Facing.RIGHT;
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
        facing = Facing.RIGHT;
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
        animation = sprite.getFieldAnimation(ActorSprite.SpriteModeField.IDLE, Facing.RIGHT);
        animation.setPlayMode(PlayMode.LOOP);
        animation.setFrameDuration(delay);
        currentImage = (Sprite) animation.getKeyFrame(elapsed);
        isMovable = true;
        speed = 0;
        this.delay = delay;
        destroyAfterAnimation = false;
        facing = Facing.RIGHT;
        isRotate = false;
    }

    public void addTimer(GameTimer t) {
        timers.add(t);
    }

    public boolean canMove() {
        return isMovable;
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

    public void disableMovement() {
        isMovable = false;
    }

    public void dispose() {
    }

    public void enableMovement() {
        isMovable = true;
    }
    public Animation getCurrentAnimation() {
        return animation;
    }
    public Sprite getCurrentImage() {
        return currentImage;
    }
    public void setCurrentImage(Sprite s) {
        currentImage = s;
    }
    public int getCurrentImageIndex() {
        return animation.getKeyFrameIndex(elapsed);
    }
    public OverheadMap getCurrentMap() {
        return currentMap;
    }

    public float getDelay() {
        return delay;
    }
    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }
    public float getElapsedTime() {
        return elapsed;
    }
    public void setElapsedTime(float delta) {
        elapsed = delta;
    }

    public Facing getFacing() {
        return facing;
    }
    public void setFacing(Facing direction) {
        facing = direction;
    }
    public float getHeight() {
        return currentImage.getRegionHeight();
    }
    public void setInvulnerability(int time) {
        isInvulnerable = true;
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
    public void setIsRotate(boolean getRotate) {
        isRotate = getRotate;
    }

    public float getLastXFacing() {
        return facing.getX();
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastYFacing() {
        return lastYFacing;
    }

    public float getLastY() {
        return lastY;
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
    public float getSpeed() {
        return speed;
    }


    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public ActorSprite getSpriteSheet() {
        return sprite;
    }
    public Array<GameTimer> getTimers() {
        return timers;
    }
    public float getWidth() {
        return currentImage.getRegionWidth();
    }
    public float getX() {
        return x;
    }
    public void setX(float getX) {
        x = getX;
    }
    public float getY() {
        return y;
    }
    public void setY(float getY) {
        y = getY;
    }
    
    
    public boolean isFinished() {
        return destroyAfterAnimation &&
                animation.getKeyFrameIndex(elapsed) >= animation.getKeyFrames().length - 1;
    }
    public boolean isInvulnerable() {
        return isInvulnerable;
    }
    public boolean isRotate() {
        return isRotate;
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
    public void resetAnimation() {
        if (images != null) {
            elapsed = 0;
            animation = new Animation(delay, images);
            currentImage = (Sprite) animation.getKeyFrame(0);
        }
    }
    public void setFacing() {
        if (lastXFacing < 0.0) {
            facing = Facing.LEFT;
        }
        else if (lastXFacing > 0.0) {
            facing = Facing.RIGHT;
        }
        
        
    }
    
    public void setMap(OverheadMap map) {
        currentMap = map;
        setX(x);
        setY(y);
    }
    
    public void setCurrentMap(OverheadMap map) {
        currentMap = map;
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
    public enum Facing {
        
        LEFT(-1, 0, 270),
        RIGHT(1, 0, 90);
        
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
    public void setMaxLife(int newLife) {
        maxLife = newLife;
    }
    
    public int getMaxLife() {
        return maxLife;
    }
    
    public void reduceLife(int deltaLife) {
        currentLife -= deltaLife;
        if (currentLife < 0) {
            currentLife = 0;
        }
        if (currentLife > maxLife) {
            currentLife = maxLife;
        }
    }

    public void increaseLife(int deltaLife) {
        currentLife += deltaLife;
        if (currentLife < 0) {
            currentLife = 0;
        }
        if (currentLife > maxLife) {
            currentLife = maxLife;
        }
    }
    
    public void setCurrentLife(int newLife) {
        currentLife = newLife;
    }
    
    public int getCurrentLife() {
        return currentLife;
    }

    public void setAttack(int newAttack) {
        attack = newAttack;
    }
    
    public int getAttack() {
        return attack;
    }
    
    public Item getItem() {
        return item;
    }
    
    public void setItem(Item i) {
        item = i;
    }

}
