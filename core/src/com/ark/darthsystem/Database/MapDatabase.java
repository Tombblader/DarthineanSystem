package com.ark.darthsystem.Database;

import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.States.OverheadMap;

public class MapDatabase {

    public static OverheadMap testing;
    public MapDatabase() {
        testing = new OverheadMap("maps/Testing2.tmx", "music/Forbidden Shrine of Heroes.mp3");
        Database2.player.setMap(testing, 80 / GraphicsDriver.getPlayerCamera().getConversion(), 80 / GraphicsDriver.getPlayerCamera().getConversion());
        Database2.ErikAI.setMap(testing, 500 / GraphicsDriver.getPlayerCamera().getConversion(), 500 / GraphicsDriver.getPlayerCamera().getConversion());
        Database2.GraphicsPotion.setMap(testing, false);
        Database2.chapter1.setMap(testing, false);
    }
    
    public static void dispose() {
        testing.dispose();
    }
}
