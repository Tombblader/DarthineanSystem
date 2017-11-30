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
public class ChapterVeather extends Novel {
    public ChapterVeather() {
        chapters.add((Page) () -> {
            BattleDriver.printline("Hey sexy, do you love water?  That means you love 80% of me.");
            BattleDriver.printline(Database2.player.getBattler(1), "Oh no.. NOT HIM!  NO NO NO NO NO NO!");
            BattleDriver.printline("Hey babe, you're so hawt that a fireman can't put you out.");
            BattleDriver.printline(Database2.player.getBattler(0), "Identify yourself!");
        });
        chapters.add((Page) () -> {
            BattleDriver.printline("Gladly!  I am the Ladies' Mann Veather Mann!  Ooooooh yeah!");
            BattleDriver.printline(Database2.player.getBattler(1), "I am so tired of you!  Why are you even here?!  Last I checked, you keep invading my temple!");
            BattleDriver.printline("Don't worry, I know you're the shy type!  You just pretend to be mean while on the inside you're just head over heels for me!"
                    + "  I'm here because I saw something fall here!  And.. I've found something amazing!");
            BattleDriver.printline(Database2.player.getBattler(0), "Oh.. is that so...?");
            BattleDriver.printline("This is a body maker!  With this, I can create the perfect hottie!");
            BattleDriver.printline(Database2.player.getBattler(0), "Intern, that is your body!  If he takes control of it, then-");
            BattleDriver.printline("Switch on!");
        });
        chapters.add((Page) () -> {
            GraphicsDriver.addMenu(new Condition("...I can only apologize for what this man has done.", new String[]{"I'll use this body anyways", "Screw you"}));
        });
        chapters.add((Page) () -> {
            if (choices.equals("I'll use this body anyways")) {
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.HAPPY, "Very well.");
            }
            else {
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.ANGRY, "...");
            }
        });
    }
    public String getMusic() {
        return null;
    }
    
}
