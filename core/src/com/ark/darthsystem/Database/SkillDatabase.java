/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.Graphics.ActorSkill;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 *
 * @author Keven
 */
public class SkillDatabase {
    public static ActorSkill Spear_Throw = new ActorSkill(GraphicsDriver.getMasterSheet().createSprites("skills/wiccan_cross/field/wiccan_cross").toArray(Sprite.class),
            0,
            0,
            10f,
            10f,
            1f/24f);

}
