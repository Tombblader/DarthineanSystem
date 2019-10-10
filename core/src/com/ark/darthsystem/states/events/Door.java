package com.ark.darthsystem.states.events;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Door extends Teleport {

    Sprite[] openImage;

    public Door(String img, String openImage, float getX, float getY, float getDelay, String newMap, int xCoord, int yCoord) {
        super(img, getX, getY, getDelay, newMap, xCoord, yCoord);
        this.openImage = GraphicsDriver.getMasterSheet().createSprites(img).toArray(Sprite.class);
        setID(3);
    }

}
