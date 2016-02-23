package com.ark.darthsystem.Graphics;

public class PlayerCamera extends Camera {

    public static final float PIXELS_TO_METERS = 2.0F;

    public PlayerCamera(float width, float height) {
        super(width, height);
        setToOrtho(true, GraphicsDriver.getWidth() / PIXELS_TO_METERS, GraphicsDriver.getHeight() / PIXELS_TO_METERS);
    }

    public void followPlayer(int x, int y, int boundX, int boundY) {
        final int Y_TOP_OFFSET = 66;
        final int Y_BOTTOM_OFFSET = 66;
        int posX = x;
        int posY = y;

        float cameraPosX = posX < GraphicsDriver.getWidth() / 2 / PIXELS_TO_METERS ? GraphicsDriver.getWidth() / 2 / PIXELS_TO_METERS: posX;
        float cameraPosY = posY < (GraphicsDriver.getHeight() / 2 - Y_TOP_OFFSET) / PIXELS_TO_METERS ? (GraphicsDriver.getHeight() / 2 - Y_TOP_OFFSET) / PIXELS_TO_METERS: posY;
        cameraPosX = x > (boundX - GraphicsDriver.getWidth() / 2) / PIXELS_TO_METERS ? (boundX - GraphicsDriver.getWidth() / 2) / PIXELS_TO_METERS: cameraPosX;
        cameraPosY = y > (boundY - GraphicsDriver.getHeight() / 2 + Y_BOTTOM_OFFSET)  / PIXELS_TO_METERS ? (boundY - GraphicsDriver.getHeight() / 2 + Y_BOTTOM_OFFSET) / PIXELS_TO_METERS: cameraPosY;
        this.position.set(cameraPosX, cameraPosY, 0);
    }
    
    public float getConversion() {
        return PIXELS_TO_METERS;
    }

}
