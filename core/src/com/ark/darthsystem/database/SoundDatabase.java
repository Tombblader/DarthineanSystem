/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

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
        fieldCastingSound = GraphicsDriver.getAssets().get("sounds/cast.wav");
        battlerCastingSound = GraphicsDriver.getAssets().get("sounds/cast.wav");
        buffingSound = GraphicsDriver.getAssets().get("sounds/buff.wav");
        fieldSwordSound = GraphicsDriver.getAssets().get("sounds/slash.wav");
        battlerSwordSound = GraphicsDriver.getAssets().get("sounds/slash.wav");
        ouchSound = GraphicsDriver.getAssets().get("sounds/ouch.wav");
        initializeSoundsAssetManager(GraphicsDriver.getAssets());
//        initializeSounds();
    }

    private static void initializeSoundsAssetManager(AssetManager assets) {
        Array<Sound> temp = new Array<>();
        assets.getAll(Sound.class, temp);
        for (Sound s : temp) {
            SOUNDS.put(assets.getAssetFileName(s).substring(7, assets.getAssetFileName(s).length() - 4).toUpperCase(), s);
        }
    }

    public static void dispose() {
//        fieldCastingSound.dispose();
//        battlerCastingSound.dispose();
//        buffingSound.dispose();
//        fieldSwordSound.dispose();
//        battlerSwordSound.dispose();
//        ouchSound.dispose();
//        for (Sound s : SOUNDS.values()) {
//            s.dispose();
//        }
    }
}
