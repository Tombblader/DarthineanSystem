/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.ActorSprite;
import com.ark.darthsystem.graphics.GraphicsDriver;

/**
 *
 * @author Keven
 */
public class Chapter01 extends Novel {
    int annoyance = 0;
    public Chapter01() {
        chapters.add((Novel.Page) () -> {
            BattleDriver.printline(Database2.player.getBattler(0), "I understand that you have some streamlined method of issuing orders, correct?" +
                    "Let me see here...  I do not like the idea of a magic item that issues controls, but I will do what I must.  Here are the instructions.");
            BattleDriver.printline(Database2.player.getBattler(0), "Move me with the arrow keys.  That's simple enough, right?" +
                    "  Attack with the spacebar.  Defend with the x button.  The Enter button opens up the menu.");
            BattleDriver.printline(Database2.player.getBattler(0), "If my HP falls to 0, this body will wither away." +
                    "  MP is used for skills.  This body has only two skills: Crosscall, a Holy Elemental Attack, and Heal.  Use them by pressing f and switch between them by pressing a." +
                    "  Recharge magic by pressing c rapidly.");
        });
        chapters.add((Novel.Page) () -> {
            GraphicsDriver.addMenu(new Condition("Got it?", new String[]{"Yes", "No"}));
        });
        chapters.add((Novel.Page) () -> {
            if (choices.equals("Yes")) {
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.HAPPY, "Good.  Let's continue.");
            }
            else {
                pageIndex = -1;
                annoyance++;
                if (annoyance >= 5) {
                    BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.ANGRY, "....I can't tell if you are slow or if you are simply messing around.");
                    pageIndex = 1;
                }
            }
        });
    }
    public String getMusic() {
        return null;
    }
    
}
