/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.ActorCollision;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.State;
import com.ark.darthsystem.states.Menu;
import com.ark.darthsystem.states.Message;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author trankt1
 */
public abstract class Novel implements State {

    ArrayList<Page> chapters;
    
    
    String choices;
    int pageIndex = 0;
    CopyOnWriteArrayList<TickEvent> timers = new CopyOnWriteArrayList<>();
    public Novel() {
        this.chapters = new ArrayList<>();
    }


    public void dispose() {

    }
    
    public void moveActor(float x, float y, float speed, int wait) {
        
    }
    public void render(SpriteBatch batch) {
        State ste = null;
        for (State state : GraphicsDriver.getState()) {
            if (!(state instanceof Message)
                    && !(state instanceof Menu)
                    && !(state instanceof Novel)
                    ) {
                ste = state;
            }
        }
        if (ste != null) {
            ste.render(batch);
        }
    }
    
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
    public interface Page {
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
    
    public boolean isFinished() {
        return true;
    }

}
