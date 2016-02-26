/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.States.chapters;

import com.ark.darthsystem.Graphics.Actor;
import com.ark.darthsystem.Graphics.ActorCollision;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.States.State;
import com.ark.darthsystem.States.Menu;
import com.ark.darthsystem.States.Message;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

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
    
    public abstract class TickEvent {
        private String name;
        private int frameTime;
        private int currentFrame;
        public TickEvent(String name, int frames) {
            this.name = name;
            frameTime = frames;
            currentFrame = 0;
        }
        public abstract void run();
        public void update(float delta) {
            run();
            currentFrame++;
        }
        public boolean isFinished() {
            return currentFrame >= frameTime;
        }
        public String getName() {
            return name;
        }
    }
    
    
    String choices;
    int pageIndex = 0;
    ArrayList<Page> chapters;
    CopyOnWriteArrayList<TickEvent> timers = new CopyOnWriteArrayList<>();
    
//    public abstract void run();

    public float update(float delta) {
        if (timers.isEmpty()) {
            if (chapters.size() <= pageIndex) {
                GraphicsDriver.removeState(this);
            }
            else {
                chapters.get(pageIndex).run();
                pageIndex++;
            }
        } else {
            for (TickEvent t : timers) {
                t.update(delta);
                if (t.isFinished()) {
                    timers.remove(t);
                }
            }
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
    
    public void moveActor(float x, float y, float speed, int wait) {
        
    }

}
