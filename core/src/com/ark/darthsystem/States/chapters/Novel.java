/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.chapters;

import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.States.State;
import com.ark.darthsystem.States.Menu;
import com.ark.darthsystem.States.Message;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;

/**
 *
 * @author trankt1
 */
public abstract class Novel implements State {

    public Novel() {
        this.chapters = new ArrayList<>();
    }

    public class Condition extends Menu {
        public Condition(String header, String[] choices) {
            super(header, choices, true, true);
        }
        @Override
        public Object confirm(String choice) {
            choices = choice;
            return choice;
        }
    }
    public abstract class Page {
        public abstract void run();
    }
    
    
    String choices;
    int pageIndex = 0;
    ArrayList<Page> chapters;
    
//    public abstract void run();

    public float update(float delta) {
        if (chapters.size() <= pageIndex) {
            GraphicsDriver.removeState(this);
        }
        else {
            chapters.get(pageIndex).run();
            pageIndex++;
        }
        return delta;
    }

    public void render(SpriteBatch batch) {
        State ste = null;
        for (State state : GraphicsDriver.getState()) {
            if (!(state instanceof Message)
                    && !(state instanceof Menu)
                    && !(state instanceof Novel)//&& !(state instanceof Pause)
                    ) {
                ste = state;
            }
        }
        if (ste != null) {
            ste.render(batch);
        }
    }

    public void dispose() {

    }
    
    
    


}
