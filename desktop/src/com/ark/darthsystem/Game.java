/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.badlogic.gdx.backends.lwjgl3.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

/**
 *
 * @author Keven Tran
 */
public class Game {

    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1024, 768);
        config.setTitle("Adventurer");
        config.useOpenGL3(true, 3, 3);       
        config.setResizable(false);
        ShaderProgram.prependVertexCode = "#version 330 core\n#define varying out\n#define attribute in\n";
        ShaderProgram.prependFragmentCode = "#version 330 core\n#define varying in\n#define texture2D texture\n#define gl_FragColor fragColor\nout vec4 fragColor;\nprecision mediump float;\n";
        config.useVsync(true);
        GraphicsDriver listener = new GraphicsDriver();  // example of sharing context
        new Lwjgl3Application(listener, config);
    }
}
