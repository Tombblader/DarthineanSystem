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
    boolean isFinished = false;
    public Chapter1() {
        chapters.add(new Page() {
            @Override
            public void run() {
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "Oh, it's you.");
                BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "HEY WHY'D YOU LEAVE ME BEHIND?!");
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "You're loud.");
                BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "ANSWER THE QUESTION!");
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "I just did.  The intern is here.  The body is further up ahead.");
                BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "'Sup, Intern!  Nice to meet'cha.");
                
            }
        });
        chapters.add(new Page() {
            @Override
            public void run() {
                GraphicsDriver.addMenu(new Condition("...", new String[]{
                    "Nice to meet you.",
                    "Loudmouth.",
                    "Hey Sexy."
                }));
            }
        });
        chapters.add(new Page() {
            @Override
            public void run() {
                switch(choices) {
                    case "Nice to meet you.":
                        break;
                    case "Loudmouth.":
                        BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "HEY!  I'm not a loudmouth!!!");
                        BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "...Intern, one moment please.  Huddle.");
                        break;
                    case "Hey Sexy.":
                        BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "Did you just say...?");
                        BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "...Intern, one moment please.  Huddle.");
                        BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "...");
                        BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "Did you just hear what the intern said?");
                        BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "...Yes.  This could be a problem.  It's only been a few minutes and the Intern has already begun flirting, without a body no less.");
                        BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "I'm ALREADY dealing with a pervert.  Is it too late to return to sender?");
                        BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "If someoneone had something like that's child, it would be difficult to explain..");
                        BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "Let's have a truce for now and keep an eye on the intern...");
                        BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "Come on, Intern.");
                        break;
                        

                }
            }
        });
        chapters.add(new Page() {
            @Override
            public void run() {
                Database2.player.getAllActorBattlers().add(Database2.Fire_Spirit_Actor);
            }
        });
        chapters.add(new Page() {
           public void run() {
               isFinished = true;
           } 
        });
    }
    public String getMusic() {
        return null;
    }
    
    public boolean isFinished() {
        return isFinished;
    }
    
}
