package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapDatabase {
    private static HashMap<String, OverheadMap> maps;
    private static final int MIN_X_TILES = 32;
    private static final int MIN_Y_TILES = 24;
    public static final String DEFAULT_MAP = "nowhere";
    private static final float DEFAULT_X = 9;
    private static final float DEFAULT_Y = 9;
    
    public MapDatabase() {
        maps = new HashMap<>();        
        initializeMaps();
        new CollisionDatabaseLoader();
        setDefaultMap();
        MonsterDatabase.ErikAI.setMap(maps.get("complex"), 200 / GraphicsDriver.getPlayerCamera().getConversion(), 200 / GraphicsDriver.getPlayerCamera().getConversion());
//        Database2.ProtoxAI.setMap(maps.get("complex"), 200 / GraphicsDriver.getPlayerCamera().getConversion(), 200 / GraphicsDriver.getPlayerCamera().getConversion());
    }
    
    public static HashMap<String, OverheadMap> getMaps() {
        return maps;
    }
    
    private void setDefaultMap() {
        Database2.player.setMap(maps.get(DEFAULT_MAP), 9, 9);
    }
    
    private void initializeMaps() {
//        File f = new File(Gdx.files.internal("maps").file().toURI()); //CANNOT LIST INSIDE JAR
        Array<FileHandle> f = new Array<>();
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        final JarFile jar;        
        try {
            jar = new JarFile(jarFile);            
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while(entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith("maps/")) { //filter according to the path
                    f.add(Gdx.files.internal(name));
                }
            }
            jar.close();
        } catch (IOException ex) {
            Logger.getLogger(MapDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (f.size > 0) {
            for (FileHandle file : f) {
                if (file.extension().equalsIgnoreCase("tmx")) {
                    maps.put(file.nameWithoutExtension(), new OverheadMap(file.path()));
                }
            }
        } else {
            initializeMapsAlt();
        }
    }
    
    private void initializeMapsAlt() {
        FileHandle[] f = Gdx.files.internal("maps").list(); //CANNOT LIST INSIDE JAR
        for (FileHandle file : f) {
             if (file.extension().equalsIgnoreCase("tmx")) {
                maps.put(file.nameWithoutExtension(), new OverheadMap(file.path()));
            }
        }
    }
    
    public static void dispose() {        
        maps.forEach((k, v) -> v.dispose());
    }
}
