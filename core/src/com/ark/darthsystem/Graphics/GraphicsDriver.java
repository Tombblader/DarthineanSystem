package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Database.Database1;
import com.ark.darthsystem.Database.MapDatabase;
import java.util.ArrayList;
import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.Database.InterfaceDatabase;
import com.ark.darthsystem.Database.SoundDatabase;
import com.ark.darthsystem.States.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.profiling.GL30Profiler;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GraphicsDriver extends com.badlogic.gdx.Game {

    private static SpriteBatch batch;
    private static float delta;
    private static Camera camera;
    private static PlayerCamera playerCamera;
    private static BitmapFont font;
    private static float currentTime;
    private static Array<State> states;
    private static TextureAtlas masterSheet;
    private static Music backgroundMusic;
//    private static final int WIDTH, HEIGHT;
    private static com.ark.darthsystem.Graphics.Camera currentCamera;
    private static String backgroundMusicString = null;
    private static Sprite screenshot;
    private static ArrayList<Transition> transitions = new ArrayList<>();
    private static final float FONT_SCALE = 1f;
    
    public static Player getPlayer() {
        return Database2.player;
    }

    public static Camera getCamera() {
        return camera;
    }

    public static PlayerCamera getPlayerCamera() {
        return playerCamera;
    }

    public static TextureAtlas getMasterSheet() {
        return masterSheet;
    }
    
    public static void screenshot() {
        screenshot = new Sprite(ScreenUtils.getFrameBufferTexture());
        screenshot.flip(false, true);
    }

    public static float getCurrentTime() {
        return currentTime;
    }

    public static int getHeight() {
        return Gdx.graphics.getHeight();
    }

    public static int getWidth() {
        return Gdx.graphics.getWidth();
    }

    public static void newGame() {
        states = new Array<>();
        new Database1();
        new Database2();
        new MapDatabase();
    }

    public static void drawMessage(Batch batch, String message, float x, float y) {
        font.draw(batch, message, x, y);
    }

    public static void drawMessage(Batch batch, BitmapFont fonty, String message, float x, float y) {
        fonty.draw(batch, message, x, y);
    }

    public static State getCurrentState() {
        return states.get(states.size - 1);
    }

    public static BitmapFont getFont() {
        return font;
    }

    public static Array<State> getState() {
        return states;
    }

    public static void removeCurrentState() {        
        if (!(states.get(states.size - 1) instanceof OverheadMap)) {
            states.get(states.size - 1).dispose();
        }
        states.removeIndex(states.size - 1);
        if (getCurrentState().getMusic() != null && !backgroundMusicString.equals(getCurrentState().getMusic())) {
            playMusic(getCurrentState().getMusic());
        }
    }

    public static void removeState(State state) {
        if (!(state instanceof OverheadMap)) {
            state.dispose();
        }
        states.removeValue(state, true);
        if (getCurrentState().getMusic() != null && !backgroundMusicString.equals(getCurrentState().getMusic())) {
            playMusic(getCurrentState().getMusic());
        }
    }

    public static void removeAllStates() {
        states.forEach(s -> {
            if (!(s instanceof OverheadMap)) {
                s.dispose();
            }
        });
        states.clear();
    }
    
    public static void addMenu(Menu m) {
        if (m.isPause() && !(getCurrentState() instanceof Menu)) {
            states.add(m);
        } else {
            ((Menu) (getCurrentState())).addSubMenu(m);
        }
    }

    public static void addState(State getStates) {
        states.add(getStates);
        if (getStates.getMusic() != null) {
            playMusic(getStates.getMusic());
        }
    }

    public static void setState(State getState) {
        states.get((states.size - 1)).dispose();
        states.set((states.size - 1), getState);
        if (getState.getMusic() != null) {
            playMusic(getState.getMusic());
        }
    }

    public static void setMessage(String header, Animation face, ArrayList<String> getMessage) {
        if (!(getCurrentState() instanceof Message) 
                && !(getCurrentState() instanceof OverheadMap)
                ) {
            states.add(new Message(header, face, getMessage));
        } else {
            if ((getCurrentState() instanceof Message)) {
                ((Message) (getCurrentState())).addMessage(new Message(header, face, getMessage));
            }
            if ((getCurrentState() instanceof OverheadMap)) {
//                ((OverheadMap) (getCurrentState())).appendMessage(getMessage);
            }
        }
    }
    public static void setMessage(ArrayList<String> getMessage) {
        if (!(getCurrentState() instanceof Message) 
                && !(getCurrentState() instanceof OverheadMap)
                ) {
            states.add(new Message(getMessage));
        } else {
            if ((getCurrentState() instanceof Message)) {
                ((Message) (getCurrentState())).addMessage(getMessage);
            }
            if ((getCurrentState() instanceof OverheadMap)) {
                ((OverheadMap) (getCurrentState())).appendMessage(getMessage);
            }
        }
    }

    public static void setMessage(String getMessage) {
        ArrayList<String> tempMessage = new ArrayList<>();
        tempMessage.add(getMessage);
        setMessage(tempMessage);
    }

    public static void appendMessage(ArrayList<String> getMessage) {
        if ((getCurrentState() instanceof Message)) {
            ((Message) (getCurrentState())).appendMessage(getMessage);
        } else if ((getCurrentState() instanceof OverheadMap)) {
            ((OverheadMap) (getCurrentState())).appendMessage(getMessage);
        } else if (getMessage != null && !getMessage.isEmpty() && !getMessage.get(0).equals("")) {
            setMessage(getMessage);
        }
    }

    public static void appendMessage(String getMessage) {
        ArrayList<String> temp = new ArrayList<>();
        temp.add(getMessage);
        appendMessage(temp);
    }

    public static Batch getBatch() {
        return batch;
    }

    public static float getRawDelta() {
        return Gdx.graphics.getDeltaTime();
    }

    public static Camera getCurrentCamera() {
        return currentCamera;
    }

    public static void setCurrentCamera(Camera c) {
        currentCamera = c;
    }

    public static void playMusic(String musicName) {
        try {
            if (backgroundMusic != null && backgroundMusic.isPlaying()) {
                backgroundMusic.dispose();
            }
            backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal(musicName));
            backgroundMusic.setLooping(true);
            backgroundMusicString = musicName;
            backgroundMusic.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void pauseTime(int time) {
        transitions.add(new Transition(Transition.TransitionType.PAUSE, time));
    }
    public static void transition() {
        transitions.add(new Transition(Transition.TransitionType.FADE_IN_OUT));
    }
    public static void transition(Transition transition) {
        transitions.add(transition);
    }
    public static void transition(Sprite s) {
        screenshot = s;
//        transitions.add(new Transition());
    }
    public static void clearAllStates() {
        for (State s : states) {
            s.dispose();
        }
        states.clear();
    }
    public static void flashScreen() {
        screenshot.setColor(1, 1, 1, 1);
    }
    private Input input;

    private long diff, start = System.currentTimeMillis();
    
    @Override
    public void create() {
        input = new Input();
        DisplayMode optimal = null;
        for (DisplayMode d : Gdx.graphics.getDisplayModes()) {
            if (d.width == 1024 && d.height == 768)
            optimal = d;
        }
        Gdx.graphics.setFullscreenMode(optimal);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();        
        camera = new Camera(w, h);
        playerCamera = new PlayerCamera(w, h);
        currentCamera = camera;
        batch = new SpriteBatch();
        masterSheet = new TextureAtlas(Gdx.files.internal("master/MasterSheet.atlas"));
        for (AtlasRegion t : masterSheet.getRegions()) {
            if (t.splits == null) {
                t.flip(false, true);
            }
        }
        
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/monofont.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 24;
        parameter.flip = true;
        parameter.borderColor = Color.BLACK;
        parameter.borderWidth = .1f;
        parameter.color = Color.WHITE;
        font = gen.generateFont(parameter);
        gen.dispose();
        Gdx.input.setInputProcessor(input);
        new Database2();
        states = new Array();
        states.add(new Title());
    }
    
    public void dispose() {
        super.dispose();
        InterfaceDatabase.dispose();
        SoundDatabase.dispose();
        MapDatabase.dispose();
        font.dispose();
        batch.dispose();
        masterSheet.dispose();
    }
    
    
    public void render() {
        if (getPlayer().totalPartyKill()) {
            BattleDriver.fullHeal(getPlayer().getAllBattlers());
            removeAllStates();
            addState(new GameOver());
        } 
        delta = (Gdx.graphics.getDeltaTime() * 1000.0f);
        currentTime += Gdx.graphics.getDeltaTime();
        if (!transitions.isEmpty()) {
            transitions.get(0).update(Gdx.graphics.getDeltaTime());
            if (transitions.get(0).isFinished()) {
                transitions.remove(0);
            }
        }
        else {
            update(delta);
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        currentCamera.update();
        batch.setProjectionMatrix(currentCamera.combined);
        batch.begin();
        getCurrentState().render(batch);
        if (!transitions.isEmpty()) {
            if (getCurrentState() instanceof OverheadMap) {
                SpriteBatch tempBatch = (SpriteBatch) ((OverheadMap)(getCurrentState())).getBatch();
                tempBatch.setProjectionMatrix(camera.combined);
                tempBatch.begin();
                transitions.get(0).render(tempBatch);
                tempBatch.end();
                tempBatch.setProjectionMatrix(currentCamera.combined);
            } else {
                transitions.get(0).render(batch);
            }
        }
        batch.end();
    }
    
    public void resize(int width,
            int height) {
        System.out.println(Gdx.app.getApplicationListener().
                toString() +
                " Resize: " +
                width +
                "," +
                height);
    }
    public void resume() {
        System.out.println("Resumed");
    }
    
    public void update(float delta) {
        getCurrentState().update(delta);
//        System.out.println(Integer.toString(Gdx.graphics.getFramesPerSecond()));
        Gdx.graphics.setTitle(Integer.toString(Gdx.graphics.getFramesPerSecond()));        
    }
    
}
