/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.states.chapters.*;

/**
 *
 * @author Keven
 */
public class EventDatabase {

    public static Novel chapters(String[] parameters) {
        Novel event = null;
        try {
            event = ChapterGenerator.generateNovel("chapters/" + parameters[0] + ".xml");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return event;
    }
;
}
