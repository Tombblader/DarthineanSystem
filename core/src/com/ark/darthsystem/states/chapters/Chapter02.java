/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.database.MonsterDatabase;
import com.ark.darthsystem.graphics.ActorSprite;

/**
 *
 * @author Keven
 */
public class Chapter02 extends Novel {
    public Chapter02() {
        chapters.add((Novel.Page) () -> {
            BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "Oh, look, a sword.  We'll take this along so you can use it in your body.");
            BattleDriver.printline(Database2.player.getBattler(1), ActorSprite.SpriteModeFace.NORMAL, "HEY!  LOOK OUT!.");
        });
        chapters.add((Novel.Page) () -> {
            BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "What?");
            BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "Ack!");
            BattleDriver.printline(Database2.player.getBattler(0), ActorSprite.SpriteModeFace.NORMAL, "What?");
        });
        chapters.add((Novel.Page) () -> {
                    MonsterDatabase.MONSTER_LIST.get("Living Sword".toUpperCase()
                    ).clone().setMap(Database2.player.getCurrentMap(), 10, 11);
            
        });
    }
    
    @Override
    public String getMusic() {
        return null;
    }
    
}