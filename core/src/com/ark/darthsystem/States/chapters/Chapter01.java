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
import com.ark.darthsystem.States.Menu;

/**
 *
 * @author Keven
 */
public class Chapter01 extends Novel {
    public Chapter01() {
        chapters.add(new Novel.Page() {
            @Override
            public void run() {
                BattleDriver.printline(Database2.player.getBattler(0), "Move me with the arrow keys.  That's simple enough, right?" +
                        "  Attack with the spacebar.  Defend with the x button.  The Enter button opens up the menu.");
                BattleDriver.printline(Database2.player.getBattler(0), "If my HP falls to 0, this body will wither away." +
                        "  MP is used for skills.  This body has only two skills: Crosscall, a Holy Elemental Attack, and Heal.  Use them by pressing f and switch between them by pressing a." +
                        "  Recharge magic by pressing c rapidly.");
            }
        });
        chapters.add(new Novel.Page() {
            @Override
            public void run() {
                GraphicsDriver.addMenu(new Condition("Got it?", new String[]{"Yes", "No"}));
            }
        });
        chapters.add(new Novel.Page() {
            @Override
            public void run() {
                if (choices.equals("Yes")) {
                    BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.HAPPY, "Good.  Let's continue.");
                }
                else {
                    pageIndex = -1;
                }
            }
        });
    }
    public String getMusic() {
        return null;
    }
    
}
