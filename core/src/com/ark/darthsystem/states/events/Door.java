/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Door extends Teleport {
    Sprite[] openImage;
    public Door(Sprite[] img, Sprite[] openImage, float getX, float getY, float getDelay, String newMap, int xCoord, int yCoord) {
        super(img, getX, getY, getDelay, newMap, xCoord, yCoord);
        this.openImage = img;
        setID(3);
    }
    
    
}
