package com.ark.darthsystem.graphics;

import com.badlogic.gdx.InputAdapter;

public class Input extends InputAdapter {

    public static int[] keys = new int[65536];
    public static boolean[] keyPressed = new boolean[65536];

    public void invoke(long window,
            int keycode,
            int scancode,
            int action,
            int mods) {
        keys[keycode] = action;
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

}
