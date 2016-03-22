/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 *
 * @author Keven
 */
public class InterfaceDatabase {
    private static final int OFFSET = 16;
    public static NinePatch TEXT_BOX = GraphicsDriver.getMasterSheet().createPatch("interface/window");

    public static void dispose() {
//        TEXT_BOX.getTexture().dispose();
    }
}