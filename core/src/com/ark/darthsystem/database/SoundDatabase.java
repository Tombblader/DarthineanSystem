/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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

/**
 *
 * @author Keven
 */
public class SoundDatabase {
    public static Sound fieldCastingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/cast.wav"));
    public static Sound battlerCastingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/cast.wav"));
    public static Sound buffingSound = Gdx.audio.newSound(Gdx.files.internal("sounds/buff.wav"));   
    public static Sound fieldSwordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/slash.wav"));
    public static Sound battlerSwordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/slash.wav"));
    public static Sound ouchSound = Gdx.audio.newSound(Gdx.files.internal("sounds/ouch.wav"));
    public static HashMap<String, Sound> SOUNDS;
    
    public SoundDatabase() {
        SOUNDS = new HashMap<>();
        initializeSounds();
    }

    private void initializeSounds() {
//        File f = new File(Gdx.files.internal("maps").file().toURI()); //CANNOT LIST INSIDE JAR
        Array<FileHandle> f = new Array<>();
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        final JarFile jar;        
        try {
            jar = new JarFile(jarFile);            
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while(entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith("sounds/")) { //filter according to the path
                    f.add(Gdx.files.classpath(name));
                }
            }
            jar.close();
        } catch (IOException ex) {
            Logger.getLogger(MapDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (f.size > 0) {
            for (FileHandle file : f) {
                if (file.extension().equalsIgnoreCase("wav")) {
                    SOUNDS.put(file.nameWithoutExtension(), Gdx.audio.newSound(file));
                }
            }
        } else {
            initializeSoundsAlt();
        }
    }
    
    private void initializeSoundsAlt() {
        FileHandle[] f = Gdx.files.internal("sounds").list(); //CANNOT LIST INSIDE JAR
        for (FileHandle file : f) {
             if (file.extension().equalsIgnoreCase("wav")) {
                SOUNDS.put(file.nameWithoutExtension(), Gdx.audio.newSound(file));
            }
        }
    }
    
    public static void dispose() {
        fieldCastingSound.dispose();
        battlerCastingSound.dispose();
        buffingSound.dispose();
        fieldSwordSound.dispose();
        battlerSwordSound.dispose();
        ouchSound.dispose();
        for (Sound s : SOUNDS.values()) {
            s.dispose();
        }
    }
}
