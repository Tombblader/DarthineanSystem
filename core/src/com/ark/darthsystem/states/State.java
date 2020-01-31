package com.ark.darthsystem.states;

import com.ark.darthsystem.graphics.GameTimer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 *
 * @author Keven
 */
public interface State {

    /**
     * This is called at every frame. Here, run the game logic.
     *
     * @param delta The time since this method was called.
     * @return the delta parameter.
     */
    public float update(float delta);

    public void render(SpriteBatch batch);

    public void dispose();
    
    public void addTimer(GameTimer t);

    public String getMusic();

}
