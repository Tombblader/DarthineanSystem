/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.events;

import com.ark.darthsystem.Database.MapDatabase;
import com.ark.darthsystem.Graphics.Actor;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.Graphics.Player;
import com.ark.darthsystem.Graphics.PlayerCamera;
import com.ark.darthsystem.States.OverheadMap;
import com.ark.darthsystem.States.State;
import com.ark.darthsystem.States.Win;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class Base extends Event {

    TeamColor team;
    
    public Base(Sprite[] img, float getX,
            float getY,
            float getDelay, Player.TeamColor color) {
        super(img, getX, getY, getDelay);
        setTriggerMethod(TriggerMethod.TOUCH);
        setID(4);
        team = color;
    }
    

    
    
    @Override
    public void run(Actor a) {
        Player player = null;
        if (a instanceof Player) {
            player = (Player) a;
        } else {
            return;
        }
        if (player.getTeam() == team) {
            if (player.getItem() != null && player.getItem().getName().equals("Meat")) {
                GraphicsDriver.addState(new Win(team));
            }
        }
        
    }

}
