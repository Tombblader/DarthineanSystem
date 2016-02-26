/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Graphics;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Keven
 */
public class TextBox {
    private int x, y;
    private Sprite[] images;
    private final int LEFT_TOP = 0;
    private final int MIDDLE_TOP = 1;
    private final int RIGHT_TOP = 2;
    private final int RIGHT_MIDDLE = 3;
    private final int MIDDLE_MIDDLE = 4;
    private final int LEFT_MIDDLE = 5;
    private final int RIGHT_BOTTOM = 6;
    private final int MIDDLE_BOTTOM = 7;
    private final int LEFT_BOTTOM = 8;
    public TextBox(String imageName) {
        images = GraphicsDriver.getMasterSheet().createSprites(imageName).toArray();     
        new NinePatch(images);
    }
    
    public void draw(SpriteBatch batch, float x, float y, float width, float height) {
        float height1 = height - images[LEFT_TOP].getRegionHeight() * 2;
        float width1 = width - images[LEFT_TOP].getRegionWidth() * 2;
        batch.draw(images[LEFT_TOP], x, y);
        if (width1 > 0) {
            batch.draw(images[MIDDLE_TOP], x + images[LEFT_TOP].getRegionWidth(), y, width1, images[LEFT_TOP].getHeight());
        }
        batch.draw(images[RIGHT_TOP], x + width - images[RIGHT_TOP].getWidth(), y, images[LEFT_TOP].getWidth(), height1);
        y += images[LEFT_TOP].getHeight();
        
        batch.draw(images[LEFT_MIDDLE], x, y, images[LEFT_TOP].getWidth(), height1);
        if (width1 > 0) {
            batch.draw(images[MIDDLE_MIDDLE], x + images[LEFT_TOP].getRegionWidth(), y, width1, height1);
        }
        batch.draw(images[RIGHT_MIDDLE], x + width - images[RIGHT_TOP].getWidth(), y, images[RIGHT_TOP].getWidth(), height1);

        y += height1;
        batch.draw(images[LEFT_BOTTOM], x, y);
        if (width1 > 0) {
            batch.draw(images[MIDDLE_BOTTOM], x + images[LEFT_BOTTOM].getRegionWidth(), y, width1, images[LEFT_BOTTOM].getHeight());
        }
        batch.draw(images[RIGHT_BOTTOM], x + width - images[RIGHT_BOTTOM].getWidth(), y, images[LEFT_BOTTOM].getWidth(), height1);
        
    }
    
}
