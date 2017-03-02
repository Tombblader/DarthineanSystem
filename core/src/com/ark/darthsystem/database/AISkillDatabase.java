/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.Skill;
import com.ark.darthsystem.states.Battle;

/**
 *
 * @author Keven
 */
public class AISkillDatabase {
    public static Skill Eyebeam = new Skill("Eyebeam",
            1,
            1,
            Battle.Element.Dark,
            false, //Targets ally?
            false, //Targets all?
            Battle.Stats.Normal, //Inflict Status?
            0, 0.0, 0.0, //Level Difference/HP%/MP%
            0, 0,
            0, 11,
            0, 0,
            0, 6,
            5.5);    
}
