/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author keven
 */
public class Editor extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        primaryStage.setTitle("My First JavaFX App");
        primaryStage.show();
    }
    
    private void create() {
//        masterSheet = new TextureAtlas(Gdx.files.internal("master/MasterSheet.atlas"));
//        for (TextureAtlas.AtlasRegion t : masterSheet.getRegions()) {
//            if (t.splits == null) {
//                t.flip(false, true);
//            }
//        }
//        
//        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/monofont.ttf"));
//        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//        parameter.size = 24;
//        parameter.flip = true;
//        parameter.borderColor = Color.BLACK;
//        parameter.borderWidth = .1f;
//        parameter.color = Color.WHITE;
//        font = gen.generateFont(parameter);
//        font.getData().markupEnabled = true;
//        gen.dispose();
//        Gdx.input.setInputProcessor(input);        
    }
    
    
    
    public static void main(String[] args) {
        Application.launch(args);
    }
}
