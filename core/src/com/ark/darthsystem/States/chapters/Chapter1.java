/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.chapters;

import com.ark.darthsystem.BattleDriver;
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
                BattleDriver.printline("Erik: I AM THE GREATEST I AM THE GREATEST");
                BattleDriver.printline("Protox: I AM THE GREATEST I AM THE GREATEST");
                BattleDriver.printline("Darth: Ugh...");
            }
        });
        chapters.add(new Page() {
            @Override
            public void run() {
                GraphicsDriver.addMenu(new Condition("Will you cease this foolishness?", new String[]{"Yes", "No"}));
            }
        });
        chapters.add(new Page() {
            @Override
            public void run() {
                if (choices.equals("Yes")) {
                    BattleDriver.printline("Darth: STOPPPP!!!!");
                }
                else {
                    BattleDriver.printline("Darth: Urge to destroy world... rising...");
                }
                
            }
        });
    }
    public String getMusic() {
        return null;
    }
    
}
