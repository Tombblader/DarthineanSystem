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
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "Oh, it's you.");
                BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "HEY WHY'D YOU LEAVE ME BEHIND?!");
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "You're loud.");
                BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "ANSWER THE QUESTION!");
                BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "I just did.  The intern is here.  The body is further up ahead.");
                BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "'Sup, Intern!  Nice to meetcha.");
                
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
                        break;
                    case "Hey Sexy.":
                        BattleDriver.printline(Database2.Fire_Spirit_Actor, ActorSprite.SpriteModeFace.NORMAL, "Did you just say...?");
                        BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "...Intern, one moment please.  Huddle");
                       
                        break;
                        

                }
            }
        });
    }
    public String getMusic() {
        return null;
    }
    
}
