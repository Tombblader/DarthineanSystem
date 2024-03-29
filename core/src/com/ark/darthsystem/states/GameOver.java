package com.ark.darthsystem.states;

import com.ark.darthsystem.database.MapDatabase;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 *
 * @author Keven
 */
public class GameOver implements State {

    private Menu gameOverMenu;
    private final TextureRegion gameOverTexture;

    private String BGM = "music/WhySuffering.mp3";

    public GameOver() {
        gameOverTexture = new TextureRegion(GraphicsDriver.getAssets().get("backgrounds/gameover.png", Texture.class)) {
            {
                this.flip(false, true);
            }
        };
        gameOverMenu = new Menu("Continue?", new String[]{"Yes", "No"}) {
            @Override
            public String confirm(String choice) {
                if (choice.equals("No")) {
                    GraphicsDriver.newGame();
                    GraphicsDriver.addState(new Title());
                }
                if (choice.equals("Yes")) {
                    GraphicsDriver.addMenu(new Menu("Open which slot?", new String[]{"Slot 1", "Slot 2", "Slot 3"},
                            true,
                            true) {
                        @Override
                        public Object confirm(String choice) {
                            GraphicsDriver.loadGame("save" + getCursorIndex() + ".sav");
                            GraphicsDriver.addState((State) (MapDatabase.getMap(MapDatabase.DEFAULT_MAP))); //Need to fix loaded map..
                            GraphicsDriver.transition();
                            ((OverheadMap) (GraphicsDriver.getCurrentState())).updatePartial(0);
                            return choice;
                        }
                    });
                }
                return choice;
            }
            @Override
            public String cancel() {
                GraphicsDriver.newGame();
                GraphicsDriver.addState(new Title());
                return "";
            }

        };
    }

    @Override
    public float update(float delta) {
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
        GraphicsDriver.addMenu(gameOverMenu);
        return delta;
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(gameOverTexture, 0, 0);
    }

    @Override
    public void dispose() {
    }

    @Override
    public String getMusic() {
        return BGM;
    }

    @Override
    public void addTimer(GameTimer t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
