/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import java.util.Scanner;

/**
 *
 * @author Keven
 */
public class ChapterGenerator {
    public static Novel generateNovel(String filename) {
        FileHandle f = Gdx.files.internal(filename);
        try {
            String[] massiveString = f.readString().split("(\r\n|\r|\n)");
            String input;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
        return null;
    }
}
