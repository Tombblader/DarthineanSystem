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
        chapters.add(new Page() {
            @Override
            public void run() {
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "I am glad you answered our call.\nIt is quite dangerous, so I am going to pick you up.");
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "I think you are at the end of this dungeon.");
            }
        });
        chapters.add(new Page() {
            @Override
            public void run() {
                GraphicsDriver.pauseTime(1000);
            }
        });
        chapters.add(new Page() {
            @Override
            public void run() {
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.HAPPY, "Guide me to you.  I shall explain the rules of this world along the way.");
            }
        });
    }
    public String getMusic() {
        return null;
    }
    
}
