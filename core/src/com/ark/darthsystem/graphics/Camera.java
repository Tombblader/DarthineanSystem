package com.ark.darthsystem.graphics;

import com.badlogic.gdx.graphics.OrthographicCamera;

public class Camera extends OrthographicCamera {

    public static final float PIXELS_TO_METERS = 1.0F;

    public Camera(float width, float height) {
        super(width, height);
        position.set(0, 0, 0);
        setToOrtho(true, width, height);
    }

    public float getScreenPositionX() {
        return position.x - viewportWidth / 2f;
    }

    public float getScreenPositionY() {
        return position.y - viewportHeight / 2f;
    }
    
    public float getConversion() {
        return PIXELS_TO_METERS;
    }

}
