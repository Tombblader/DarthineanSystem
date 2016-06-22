package com.ark.darthsystem.Database;

import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.States.OverheadMap;
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
    
    public MapDatabase() {
        maps = new HashMap<>();        
        initializeMaps();
        new CollisionDatabaseLoader();
        Database2.player.setMap(maps.get("nowhere"), 300 / GraphicsDriver.getPlayerCamera().getConversion(), 300 / GraphicsDriver.getPlayerCamera().getConversion());
        Database2.ErikAI.setMap(maps.get("complex"), 200 / GraphicsDriver.getPlayerCamera().getConversion(), 200 / GraphicsDriver.getPlayerCamera().getConversion());
//        EventDatabase.GraphicsPotion.setMap(maps.get("testing"), false);
//        EventDatabase.chapter1.setMap(maps.get("testing"), false);
    }
    
    public static HashMap<String, OverheadMap> getMaps() {
        return maps;
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
    //            String fileName = "";
    //            while (file.isDirectory()) {
    //                fileName += file.nameWithoutExtension();
    //                 
    //           }
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
//            String fileName = "";
//            while (file.isDirectory()) {
//                fileName += file.nameWithoutExtension();
//                 
//           }
             if (file.extension().equalsIgnoreCase("tmx")) {
                maps.put(file.nameWithoutExtension(), new OverheadMap(file.path()));
            }
        }
    }
    
    public static void dispose() {        
        maps.forEach((k, v) -> v.dispose());
    }
}
