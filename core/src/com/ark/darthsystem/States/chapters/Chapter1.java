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
public class Chapter1 extends Novel {
    public Chapter1() {
        chapters.add(new Page() {
            @Override
            public void run() {
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "You should be somewhere about here.");
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.ANGRY, "You should be somewhere about herfdase.");
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.HAPPY, "Guide me to you.");
            }
        });
        chapters.add(new Page() {
            @Override
            public void run() {
            }
        });
    }
    public String getMusic() {
        return null;
    }
    
}
