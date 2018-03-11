/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.State;
import com.ark.darthsystem.states.Menu;
import com.ark.darthsystem.states.Message;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author trankt1
 */
public abstract class Novel implements State {

    public ArrayList<Page> chapters;
    
    
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
            for (int i = 0; i < timers.size(); i++) {
                TickEvent t = timers.get(i);
                t.update(delta);
                if (t.isFinished()) {
                    timers.remove(t);
                    i--;
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
    
    public class Dialogue implements Page {
        private String textFile;
        public Dialogue(String text) {
            textFile = text;
            try (Scanner s = new Scanner(Gdx.files.internal(text).read())) {
                StringTokenizer parser = new StringTokenizer(":");
                while (s.hasNextLine()) {
                    String input = s.nextLine();
                    if (parser.countTokens() <= 1) {
                        
                    } else {
                    
                        while (parser.hasMoreTokens()) {
                            parser.nextToken();
                        }
                    }
                }
                
            }
        }
        public void run() {
            
        }
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
