package com.ark.darthsystem.graphics;

import com.badlogic.gdx.InputAdapter;

public class Input extends InputAdapter {

    public static int[] keys = new int[65536];
    public static boolean[] keyPressed = new boolean[65536];
    private static boolean canInput = true;

    public enum Key {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        BUTTON1,
        BUTTON2,
        BUTTON3,
        BUTTON4;
    }

    public Input() {
        super();
    }

    public void invoke(long window,
            int keycode,
            int scancode,
            int action,
            int mods) {
        if (canInput) {
            keys[keycode] = action;
        }
    }

    public static void enableInput() {
        canInput = true;
    }

    public static void disableInput() {
        canInput = false;
    }

    public static void clearInput() {
        for (int i = 0; i < keys.length; i++) {
            keys[i] = 0;
            keyPressed[i] = false;
        }
    }

    public static boolean getKeyRepeat(int keycode) {
        return keyPressed[keycode];
    }

    public static boolean getKeyPressed(int keycode) {
        boolean temp = keyPressed[keycode];
        keyPressed[keycode] = false;
        return temp;
    }

    @Override
    public boolean keyDown(int keycode) {
        keyPressed[keycode] = true;
        return false;
    }

    @Override
    public boolean keyTyped(char arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keyPressed[keycode] = false;
        return false;
    }

    public static boolean isEnabled() {
        return canInput;
    }

}
