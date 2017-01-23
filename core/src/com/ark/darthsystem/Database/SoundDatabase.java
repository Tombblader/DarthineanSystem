/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 *
 * @author Keven
 */
public class SoundDatabase {
    public static Sound fieldSwordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/slash.wav"));
    public static Sound fieldSpearSound = Gdx.audio.newSound(Gdx.files.internal("sounds/spear.mp3"));
    public static Sound battlerSwordSound = Gdx.audio.newSound(Gdx.files.internal("sounds/slash.wav"));
    public static Sound ouchSound = Gdx.audio.newSound(Gdx.files.internal("sounds/ouch.mp3"));
    
    public static void dispose() {
        fieldSwordSound.dispose();
        battlerSwordSound.dispose();
        ouchSound.dispose();
    }
}
