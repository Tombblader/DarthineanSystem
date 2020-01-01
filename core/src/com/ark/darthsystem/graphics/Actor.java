/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Actor wraps the animation class to use with the game.
 *
 */
public class Actor implements Serializable {

    private transient Animation<Sprite> animation;
    private transient Sprite currentImage;
    private transient OverheadMap currentMap;
    private String currentMapName;
    private float delay;
    private boolean destroyAfterAnimation;
    private float elapsed;
    private Facing facing;
    private transient Sprite[] images;
    private boolean isMovable;
    private boolean isRotate;
    private float lastX;
    private int lastXFacing;
    private float lastY;
    private int lastYFacing;
    private float speed;
//    private transient ActorSprite sprite;
    private ArrayList<GameTimer> timers;// = new Array<>(GameTimer.class);
    private Vector2 position;// = Vector2.Zero;
    private Vector3 position3D;
    private Facing xFacingBias;
    private String imageName;
    private boolean isFinished;// = false;

    public Actor() {
        this.xFacingBias = Facing.DOWN;
        delay = 16 / 60f;
        isMovable = false;
        speed = 0;
        elapsed = 0f;
        lastXFacing = 0;
        lastYFacing = 0;
        destroyAfterAnimation = false;
        facing = Facing.DOWN;
        isRotate = false;
        timers = new ArrayList<>();
        position = Vector2.Zero;
        position3D = Vector3.Zero;
        isFinished = false;
    }

    public Actor(String img,
            float getX,
            float getY,
            float delay) {
        this();
        position = new Vector2(getX, getY);
        position3D = new Vector3(getX, getY, 0);
        this.delay = delay;
        imageName = img;
        images = GraphicsDriver.getMasterSheet().createSprites(img).toArray(Sprite.class);
        if (images != null && images.length > 0) {
            animation = new Animation<>(delay, new Array<>(images));
            animation.setPlayMode(PlayMode.LOOP);
            currentImage = (Sprite) animation.getKeyFrame(0);
        } else {
            animation = new Animation<>(0, new Array<>());
        }
        isMovable = true;
        destroyAfterAnimation = false;
    }

    public Actor(String img,
            float getX,
            float getY,
            float delay,
            boolean destroy) {
        this(img, getX, getY, delay);
        destroyAfterAnimation = destroy;
        animation.setPlayMode(destroy ? PlayMode.NORMAL : PlayMode.LOOP);
    }

    public void addTimer(GameTimer t) {
        timers.add(t);
    }

    public boolean canMove() {
        return isMovable;
    }

    public void changeAnimation(Animation<Sprite> a) {
        elapsed = 0;
        a.setFrameDuration(delay);
        animation = a;
        currentImage = (Sprite) a.getKeyFrame(0);
    }

    public void changeDuringAnimation(Animation<Sprite> a) {
        a.setFrameDuration(delay);
        animation = a;
        currentImage = (a.getKeyFrame(elapsed));
    }

    public void changeX(float getX) {
        lastX = position.x;
        position.x += getX;
        position3D.x += getX;
        lastXFacing = position.x > lastX ? 1 : position.x == lastX ? 0 : -1;
    }

    public void changeY(float getY) {
        lastY = position.y;
        position.y += getY;
        position3D.y += getY;
        lastYFacing = position.y > lastY ? 1 : position.y == lastY ? 0 : -1;

    }

    public void disableMovement() {
        isMovable = false;
    }

    public void dispose() {
    }

    public void enableMovement() {
        isMovable = true;
    }

    public Animation<Sprite> getCurrentAnimation() {
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

    public void setDelay(float delay) {
        this.delay = delay;
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

    public ArrayList<GameTimer> getTimers() {
        return timers;
    }

    public float getWidth() {
        return currentImage.getRegionWidth();
    }

    public float getX() {
        return position.x;
    }

    public void setX(float getX) {
        position.x = getX;
        position3D.x = getX;
    }

    public float getY() {
        return position.y;
    }

    public void setY(float getY) {
        position.y = getY;
        position3D.y = getY;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position.set(position);
        this.position3D.set(position, 0);
    }

    public boolean isFinished() {
        return isFinished;
//        return destroyAfterAnimation &&
//                animation.getAnimationDuration() >= elapsed;
//                animation.getKeyFrameIndex(elapsed) >= animation.getKeyFrames().length;
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
        if (images != null && images.length > 0) {
            elapsed = 0;
            animation = new Animation<>(delay, images);
            currentImage = (Sprite) animation.getKeyFrame(0);
            isFinished = false;
        }
    }

    public void setFacing() {
        if (lastXFacing > 0) {
            xFacingBias = Facing.RIGHT;
        }
        if (lastXFacing < 0) {
            xFacingBias = Facing.LEFT;
        }
        if (lastYFacing < 0) {
            xFacingBias = Facing.UP;
        }
        if (lastYFacing > 0) {
            xFacingBias = Facing.DOWN;
        }

        for (Facing f : Facing.values()) {
            if (lastXFacing == f.getX() && lastYFacing == f.getY()) {
                facing = f;
            }
        }
    }

    public void setMap(OverheadMap map) {
        currentMapName = map.getMapName();
        currentMap = map;
        setX(position.x);
        setY(position.y);
    }

    public void setCurrentMap(OverheadMap map) {
        currentMapName = map.getMapName();
        currentMap = map;
    }

    public void setMapName(String map) {
        currentMapName = map;
    }

    public void update(float delta) {
        if (currentImage != null && animation.getAnimationDuration() > 0.0) {
            elapsed += delta / 1000f;
            currentImage = (Sprite) animation.getKeyFrame(elapsed);
        }
        for (int i = 0; i < timers.size(); i++) {
            GameTimer t = timers.get(i);
            if (t.update(delta, this)) {
                timers.remove(i);
                i--;
            }
        }
        if (animation != null && elapsed >= animation.getAnimationDuration()) {
            isFinished = destroyAfterAnimation;
            elapsed = 0;
        }
        setFacing();
    }

    public void updatePartial(float delta) {
//        if (currentImage != null) {
//            elapsed += delta / 1000f;
//            currentImage = (Sprite) animation.getKeyFrame(elapsed);
//        }
//        if (animation != null && elapsed > animation.getAnimationDuration()) {
//            isFinished = destroyAfterAnimation;
//            elapsed = 0;
//        }
//        setFacing();
    }

    public Facing getFacingBias() {
        return xFacingBias;
    }

    /**
     * @return the currentMapName
     */
    public String getCurrentMapName() {
        return currentMapName;
    }

    /**
     * @param currentMapName the currentMapName to set
     */
    public void setCurrentMapName(String currentMapName) {
        this.currentMapName = currentMapName;
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        images = GraphicsDriver.getMasterSheet().createSprites(imageName).toArray(Sprite.class);
        animation = new Animation<>(delay, images);
        animation.setPlayMode(destroyAfterAnimation ? PlayMode.NORMAL : PlayMode.LOOP);
        if (images.length > 0) {
            currentImage = (Sprite) animation.getKeyFrame(0);
        }

    }

    public enum Facing {

        UP(0, -1, 0),
        LEFT(-1, 0, 270),
        RIGHT(1, 0, 90),
        DOWN(0, 1, 180),
        LEFT_DOWN(-1, 1, 225),
        RIGHT_DOWN(1, 1, 135),
        LEFT_UP(-1, -1, 315),
        RIGHT_UP(1, -1, 45);

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

        public Vector2 getVector() {
            return new Vector2(x, y);
        }

        public float getRotate() {
            return rotate;
        }
    }
    
    public boolean inCamera(Camera c) {
        if (currentImage == null) {
            return false;
        }
        return c.frustum.sphereInFrustumWithoutNearFar(position3D, getWidth() / c.getConversion() + 32f / c.getConversion());
    }

}
