/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.chapters;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.Graphics.ActorSprite;
import com.ark.darthsystem.Graphics.GraphicsDriver;

/**
 *
 * @author Keven
 */
public class Chapter0 extends Novel {
    public Chapter0() {
        chapters.add((Page) () -> {
            BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "I'm glad you've answered our call.  It seems like the vessel you were going to use crashed around here somewhere.");
            BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "Your consciousness seemed to have separated from your vessel.  That isn't a good sign, but not to worry.  I will lead you to your body.");
        });
        chapters.add((Page) () -> {
            GraphicsDriver.pauseTime(1000);
        });
        chapters.add((Page) () -> {
            BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.HAPPY, "Guide me to you.  I shall explain the rules of this world along the way.");
        });
    }
    public String getMusic() {
        return null;
    }
    
}