package com.ark.darthsystem.Graphics;

public class PlayerCamera extends Camera {

    public static final float PIXELS_TO_METERS = 20.0F;

    public PlayerCamera(float width, float height) {
        super(width, height);
        setToOrtho(true, GraphicsDriver.getWidth() / PIXELS_TO_METERS, GraphicsDriver.getHeight() / PIXELS_TO_METERS);
    }

    public void followPlayer(float x, float y, float boundX, float boundY) {
        final float Y_TOP_OFFSET = 72f;
        final float Y_BOTTOM_OFFSET = 72f;
        float posX = x;
        float posY = y;

        float cameraPosX = posX < GraphicsDriver.getWidth() / 2f / PIXELS_TO_METERS ? GraphicsDriver.getWidth() / 2f / PIXELS_TO_METERS : posX;
        float cameraPosY = posY < (GraphicsDriver.getHeight() / 2f - Y_TOP_OFFSET) / PIXELS_TO_METERS ? (GraphicsDriver.getHeight() / 2f - Y_TOP_OFFSET) / PIXELS_TO_METERS : posY;
        cameraPosX = x > (boundX - GraphicsDriver.getWidth() / 2f) / PIXELS_TO_METERS ? (boundX - GraphicsDriver.getWidth() / 2f) / PIXELS_TO_METERS: cameraPosX;
        cameraPosY = y > (boundY - GraphicsDriver.getHeight() / 2f + Y_BOTTOM_OFFSET)  / PIXELS_TO_METERS ? (boundY - GraphicsDriver.getHeight() / 2f + Y_BOTTOM_OFFSET) / PIXELS_TO_METERS : cameraPosY;
        this.position.set(cameraPosX, cameraPosY, 0);
    }
    
    public float getConversion() {
        return PIXELS_TO_METERS;
    }

}
