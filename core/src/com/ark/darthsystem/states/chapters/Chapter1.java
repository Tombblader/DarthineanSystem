/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.BattleDriver;
import static com.ark.darthsystem.database.CharacterDatabase.CHARACTER_LIST;
import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.ActorSprite;
import com.ark.darthsystem.graphics.GraphicsDriver;

/**
 *
 * @author Keven
 */
public class Chapter1 extends Novel {
    boolean isFinished = false;
    public Chapter1() {
//        chapters.add((Page) () -> {
//            BattleDriver.printline(CHARACTER_LIST.get("BLUE LADY"), ActorSprite.SpriteModeFace.NORMAL, "Oh, it's you.");
//            BattleDriver.printline(CHARACTER_LIST.get("RED LADY"), ActorSprite.SpriteModeFace.NORMAL, "HEY WHY'D YOU LEAVE ME BEHIND?!");
//            BattleDriver.printline(CHARACTER_LIST.get("BLUE LADY"), ActorSprite.SpriteModeFace.NORMAL, "You're loud.");
//            BattleDriver.printline(CHARACTER_LIST.get("RED LADY"), ActorSprite.SpriteModeFace.NORMAL, "ANSWER THE QUESTION!");
//            BattleDriver.printline(CHARACTER_LIST.get("BLUE LADY"), ActorSprite.SpriteModeFace.NORMAL, "I just did.  The intern is here.  The body is further up ahead.");
//            BattleDriver.printline(CHARACTER_LIST.get("RED LADY"), ActorSprite.SpriteModeFace.NORMAL, "'Sup, Intern!  Nice to meet'cha.");
//        });
//        chapters.add((Page) () -> {
//            GraphicsDriver.addMenu(new Condition("...", new String[]{
//                "Nice to meet you.",
//                "Loudmouth.",
//                "Hey Sexy."
//            }));
//        });
//        chapters.add((Page) () -> {
//            switch(choices) {
//                case "Nice to meet you.":
//                    break;
//                case "Loudmouth.":
//                    BattleDriver.printline(CHARACTER_LIST.get("RED LADY"), ActorSprite.SpriteModeFace.NORMAL, "HEY!  I'm not a loudmouth!!!");
//                    break;
//                case "Hey Sexy.":
//                    BattleDriver.printline(CHARACTER_LIST.get("RED LADY"), ActorSprite.SpriteModeFace.NORMAL, "Did you just say...?");
//                    BattleDriver.printline(CHARACTER_LIST.get("BLUE LADY"), ActorSprite.SpriteModeFace.NORMAL, "...Intern, one moment please.  Huddle.");
//                    BattleDriver.printline(CHARACTER_LIST.get("BLUE LADY"), ActorSprite.SpriteModeFace.NORMAL, "...");
//                    BattleDriver.printline(CHARACTER_LIST.get("RED LADY"), ActorSprite.SpriteModeFace.NORMAL, "Did you just hear what the intern said?");
//                    BattleDriver.printline(CHARACTER_LIST.get("BLUE LADY"), ActorSprite.SpriteModeFace.NORMAL, "...Yes.  This could be a problem.  It's only been a few minutes and the Intern has already begun flirting, without a body no less.");
//                    BattleDriver.printline(CHARACTER_LIST.get("RED LADY"), ActorSprite.SpriteModeFace.NORMAL, "I'm ALREADY dealing with a pervert.  Is it too late to return to sender?");
//                    BattleDriver.printline(CHARACTER_LIST.get("BLUE LADY"), ActorSprite.SpriteModeFace.NORMAL, "I'm afraid so.  If someoneone had something like that's child, it would be difficult to explain..");
//                    BattleDriver.printline(CHARACTER_LIST.get("RED LADY"), ActorSprite.SpriteModeFace.NORMAL, "Let's have a truce for now and keep an eye on the intern...");
//                    BattleDriver.printline(CHARACTER_LIST.get("BLUE LADY"), ActorSprite.SpriteModeFace.NORMAL, "Come on, Intern.");
//                    break;
//                    
//                    
//            }
//        });
//        chapters.add((Page) () -> {
//            Database2.player.getAllActorBattlers().add(CHARACTER_LIST.get("RED LADY"));
//        });
//        chapters.add((Page) () -> {
//            isFinished = true; 
//        });
    }
    public String getMusic() {
        return null;
    }
    
    public boolean isFinished() {
        return isFinished;
    }
    
}
