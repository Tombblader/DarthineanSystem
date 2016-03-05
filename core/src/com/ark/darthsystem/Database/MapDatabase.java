package com.ark.darthsystem.Database;

import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.States.OverheadMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.HashMap;

public class MapDatabase {
    public static HashMap<String, OverheadMap> maps;
    private static final int MIN_X_TILES = 32;
    private static final int MIN_Y_TILES = 24;
    
    public MapDatabase() {
        maps = new HashMap<>();
        initializeMaps();
        Database2.player.setMap(maps.get("nowhere"), 300 / GraphicsDriver.getPlayerCamera().getConversion(), 300 / GraphicsDriver.getPlayerCamera().getConversion());
        Database2.ErikAI.setMap(maps.get("testing"), 500 / GraphicsDriver.getPlayerCamera().getConversion(), 500 / GraphicsDriver.getPlayerCamera().getConversion());
        EventDatabase.GraphicsPotion.setMap(maps.get("testing"), false);
        EventDatabase.chapter1.setMap(maps.get("testing"), false);
    }
    
    private void initializeMaps() {
        FileHandle[] f = Gdx.files.internal("maps").list();
        for (FileHandle file : f) {
//            String fileName = "";
//            while (file.isDirectory()) {
//                fileName += file.nameWithoutExtension();
//                 
//           }
            if (!file.extension().equalsIgnoreCase("png")) {
                maps.put(file.nameWithoutExtension(), new OverheadMap(file.path()));
            }
        }
    }
    
    public static void dispose() {        
        maps.forEach((k, v) -> v.dispose());
    }
}
