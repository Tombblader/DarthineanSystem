package com.ark.darthsystem.states.chapters;

import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.FieldBattlerAI;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.State;
import com.ark.darthsystem.states.Menu;
import com.ark.darthsystem.states.Message;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 *
 * @author trankt1
 */
public abstract class Novel implements State {

    public ArrayList<Page> chapters;
    public Array<Actor> actorList;
    private boolean isFinished = false;
    private Actor mainActor;

    String choices;
    int pageIndex = 0;
    ArrayList<GameTimer> timers = new ArrayList<>();

    public Novel() {
        this.chapters = new ArrayList<>();
        this.actorList = new Array<>();
    }

    public void dispose() {

    }

    public void setMainActor(Actor a) {
        mainActor = a;
    }
    
    public Actor getMainActor() {
        return mainActor;
    }
    public void moveActor(float x, float y, float speed, int wait) {

    }

    public void render(SpriteBatch batch) {
        State ste = null;
        for (State state : GraphicsDriver.getState()) {
            if (!(state instanceof Message)
                    && !(state instanceof Menu)
                    && !(state instanceof Novel)) {
                ste = state;
            }
        }
        if (ste != null) {
            ste.render(batch);
        }
        for (Actor a : actorList) {
            a.render(batch);
        }
    }

    @Override
    public float update(float delta) {
        State ste = null;
        for (State state : GraphicsDriver.getState()) {
            if (state instanceof OverheadMap) {
                ste = state;
            }
        }
        if (ste != null) {
            ((OverheadMap) (ste)).updatePartial(delta);
        }
        for (int i = 0; i < actorList.size; i++) {
            Actor a = actorList.get(i);
            if (a instanceof FieldBattlerAI && ((FieldBattlerAI) (a)).totalPartyKill()) {
                actorList.removeIndex(i);
                i--;
            } else {
                a.update(delta);
                if (a.isFinished()) {
                    actorList.removeIndex(i);
                    i--;
                }
            }
        }
        if (timers.isEmpty()) {
            if (chapters.size() <= pageIndex) {
                GraphicsDriver.removeState(this);
                isFinished = true;
            } else {
                chapters.get(pageIndex).run();
                pageIndex++;
            }
        } else {
            for (int i = 0; i < timers.size(); i++) {
                GameTimer t = timers.get(i);
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

    public abstract class tempPage implements Page {

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

    public void addTimer(GameTimer t) {
        timers.add(t);
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
        return isFinished;
//        return !(GraphicsDriver.getCurrentState() instanceof Novel || GraphicsDriver.getCurrentState() instanceof Message || GraphicsDriver.getCurrentState() instanceof Menu);
    }

}
