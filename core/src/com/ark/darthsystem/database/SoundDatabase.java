/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

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
    
    public static void dispose() {
        fieldCastingSound.dispose();
        fieldSwordSound.dispose();
        battlerSwordSound.dispose();
        ouchSound.dispose();
    }
}
