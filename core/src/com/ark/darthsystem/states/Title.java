package com.ark.darthsystem.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.database.MapDatabase;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 *
 * @author Keven
 */
public class Title implements State {
    private final Menu TITLE_MENU;
    private final TextureRegion titleTexture;
    private final String BGM = null;
    
    public Title() {
        titleTexture = new TextureRegion(new Texture(Gdx.files.internal("backgrounds/title.png"))) {
            {
                this.flip(false, true);
            }
        };
        TITLE_MENU = new Menu("Adventurer.", new String[]{"Start", "Continue", "Quit"}) {
            @Override
            public String confirm(String choice) {
                if (choice.equals("Start")) {
                    GraphicsDriver.newGame();
                    GraphicsDriver.addState((State) (MapDatabase.getMaps().get(MapDatabase.DEFAULT_MAP)));
                    GraphicsDriver.transition();
                    ((OverheadMap) (GraphicsDriver.getCurrentState())).updatePartial(0);
                }
                if (choice.equals("Continue")) {
                    GraphicsDriver.addMenu(new Menu("Open which slot?",
                            new String[]{"Slot 1", "Slot 2", "Slot 3"},
                            true,
                            true) {
                        @Override
                        public Object confirm(String choice) {
                            GraphicsDriver.loadGame("save" + getCursorIndex() + ".sav");
                            GraphicsDriver.addState((State) (MapDatabase.getMaps().get(MapDatabase.DEFAULT_MAP))); //Need to fix loaded map..
                            GraphicsDriver.transition();
                            ((OverheadMap) (GraphicsDriver.getCurrentState())).updatePartial(0);
                            return choice;
                        }
                    });
                }
                if (choice.equals("Quit")) {
                    GraphicsDriver.getState().get(0).dispose();
                    throw new GameOverException();
                }
                
                return choice;
            }
            @Override
            public String cancel() {
                setCursorIndex(0);
                return "";
            }
            
        };
    }
    
    @Override
    public float update(float delta) {
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
        GraphicsDriver.addMenu(TITLE_MENU);
        return delta;
    }
    
    @Override
    public void render(SpriteBatch batch) {
        batch.draw(titleTexture, 0, 0);
    }
    
    @Override
    public void dispose() {
        titleTexture.getTexture().dispose();
    }

    @Override
    public String getMusic() {
        return BGM;
    }
    
}
