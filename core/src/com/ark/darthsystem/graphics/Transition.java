/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Transition effects for the screen.
 * @author Keven
 */
public class Transition {

    private boolean isFinished;
    private float time;
    public enum TransitionType {
        FADE,
        FADE_IN_OUT,
        FLASH,
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        PAUSE
    }
    
    private Sprite screenshot;
    private Sprite screenshotClone;
    private TransitionType type;
    private float alpha;
    
    public Transition(TransitionType type) {
        this.time = 1000;
        this.isFinished = false;
        this.type = type;
        this.stepUp = false;
        screenshot = screenshot();
        switch (type) {
            case FADE:
                alpha = 1;
                break;
            case FADE_IN_OUT:
                alpha = 0;
                screenshotClone = screenshot();
                break;
            case PAUSE:
                alpha = 0;
            case FLASH:
                break;
            case MOVE_UP:
                break;
            case MOVE_DOWN:
                break;
            case MOVE_LEFT:
                break;
            case MOVE_RIGHT:
                break;
            default:
                throw new AssertionError(type.name());
            
        }
    }
    
    public Transition(TransitionType type, float time) {
        this(type);
        this.time = time;
    }
    
    private Sprite screenshot() {
        Sprite tempScreenshot = new Sprite(ScreenUtils.getFrameBufferTexture());
        tempScreenshot.flip(false, true);
        tempScreenshot.setSize(GraphicsDriver.getWidth(), GraphicsDriver.getHeight());
        return tempScreenshot;
    }

    public void init(TransitionType t) {
        screenshot = screenshot();
        type = t;
        switch (type) {
            case FADE:
                alpha = 1;
                break;
            case FADE_IN_OUT:
                alpha = 0;
                screenshotClone = screenshot();
                break;
            case PAUSE:
                alpha = 0;
            case FLASH:
                break;
            case MOVE_UP:
                break;
            case MOVE_DOWN:
                break;
            case MOVE_LEFT:
                break;
            case MOVE_RIGHT:
                break;
            default:
                throw new AssertionError(type.name());
            
        }
    }
    
    private boolean stepUp;
    public float update(float delta) {
        switch (type) {
            case FADE:
                alpha -= delta;
                screenshot.setAlpha(alpha);
                isFinished = alpha <= 0;
                break;
            case FADE_IN_OUT:
                if (!stepUp) {
                    screenshot.setColor(0, 0, 0, alpha);
                    alpha += delta;
                    stepUp = alpha >= 1;
                    if (stepUp) {
                        alpha = 1;
                    }
                } else {
                    screenshot.setAlpha(alpha);
                    alpha -= delta;
                    isFinished = alpha <= 0;
                }
                break;
            case PAUSE:
                alpha += delta * 1000f;
                isFinished = alpha >= time;
            case FLASH:
                break;
            case MOVE_UP:
                break;
            case MOVE_DOWN:
                break;
            case MOVE_LEFT:
                break;
            case MOVE_RIGHT:
                break;
            default:
                type = TransitionType.FADE;
                
        }
        return delta;
    }
    
    public void render(Batch batch) {
        if (type == TransitionType.FADE_IN_OUT && screenshotClone != null) {
            screenshotClone.draw(batch);
            if (stepUp) {
                screenshotClone = null;
            }
        }
        if (type != TransitionType.PAUSE) {
            screenshot.draw(batch);
        }
        
    }
    
    public boolean isFinished() {
        return isFinished;
    }
    
    public void reset() {
        screenshot = null;
        screenshotClone = null;                
        alpha = 1;
        time = 1000;
        switch (type) {
            case FADE:
                alpha = 1;
                break;
            case FADE_IN_OUT:
                alpha = 0;
                break;
            case PAUSE:
                alpha = 0;
            case FLASH:
                break;
            case MOVE_UP:
                break;
            case MOVE_DOWN:
                break;
            case MOVE_LEFT:
                break;
            case MOVE_RIGHT:
                break;
            default:
                throw new AssertionError(type.name());
            
        }
    }
}
