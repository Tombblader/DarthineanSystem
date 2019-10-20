package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.badlogic.gdx.graphics.g2d.NinePatch;

/**
 *
 * @author Keven
 */
public class InterfaceDatabase {

    private static final int OFFSET = 16;
    public static NinePatch TEXT_BOX = GraphicsDriver.getMasterSheet().createPatch("interface/window");

}
