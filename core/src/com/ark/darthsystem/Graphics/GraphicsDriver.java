package com.ark.darthsystem.Graphics;

import com.ark.darthsystem.Database.Database1;
import com.ark.darthsystem.Database.Database1Point5;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.GameOverException;
import com.ark.darthsystem.States.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class GraphicsDriver extends com.badlogic.gdx.Game {

    private static SpriteBatch batch;
    private static float delta;
    private static Camera camera;
    private static PlayerCamera playerCamera;
    private static BitmapFont font;
    private static float currentTime;
    private static CopyOnWriteArrayList<State> states;
    private static TextureAtlas masterSheet;
    private static Music backgroundMusic;
    private static final int WIDTH = 1024, HEIGHT = 768;
    private static com.ark.darthsystem.Graphics.Camera currentCamera;
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

    public static float getCurrentTime() {
        return currentTime;
    }

    public static int getHeight() {
        return HEIGHT;

    }

    public static int getWidth() {
        return WIDTH;
    }

    public static void newGame() {
        states = new CopyOnWriteArrayList<>();
        new Database1();
        new Database2();
        new Database1Point5();
    }

    public static void drawMessage(Batch batch, String message, float x, float y) {
        font.getData().scaleX = FONT_SCALE / getCurrentCamera().getConversion();
        font.getData().scaleY = FONT_SCALE / getCurrentCamera().getConversion();
        font.draw(batch, message, x, y);
    }

    public static void drawMessage(Batch batch, BitmapFont fonty, String message, float x, float y) {
        fonty.draw(batch, message, x, y);
    }

    public static State getCurrentState() {
        return states.get(states.size() - 1);
    }

    public static BitmapFont getFont() {
        return font;
    }

    public static CopyOnWriteArrayList<State> getState() {
        return states;
    }

    public static void removeCurrentState() {
        states.get(states.size() - 1).dispose();
        states.remove(states.size() - 1);
    }

    public static void removeState(State state) {
        state.dispose();
        states.remove(state);
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
    }

    public static void setState(State getState) {
        states.set((states.size() - 1), getState);
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
//				((OverheadMap) (getCurrentState())).addMessage(getMessage);
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
//			((OverheadMap) (getCurrentState())).appendMessage(getMessage);
        } else if (getMessage != null &&
                !getMessage.isEmpty() &&
                !getMessage.get(0).
                equals("")) {
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
            backgroundMusic.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private Input input;

    private long diff, start = System.currentTimeMillis();
    private static final float FONT_SCALE = 1f;
    
    @Override
    public void create() {
        input = new Input();
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new Camera(w, h);
        playerCamera = new PlayerCamera(w, h);
        currentCamera = camera;
        batch = new SpriteBatch();
        masterSheet = new TextureAtlas(Gdx.files.internal(
                "master/MasterSheet.txt"));
        for (TextureRegion t : masterSheet.getRegions()) {
            t.flip(false, true);
        }
        
        FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(
                "fonts/monofont.ttf"));
        FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 24;
        parameter.flip = true;
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.LIGHT_GRAY;
        font = gen.generateFont(parameter);
        font.setUseIntegerPositions(false);
        gen.dispose();
        /*        font = new BitmapFont(Gdx.files.internal(
                "com/ark/darthsystem/assets/fonts/font.fnt"),
                true);
        font.getData().scaleX = FONT_SCALE;
        font.getData().scaleY = FONT_SCALE;*/
        Gdx.input.setInputProcessor(input);
        new Database2();
        states = new CopyOnWriteArrayList();
        states.add(new Title());
    }
    public void dispose() {
        font.dispose();
        batch.dispose();
        masterSheet.dispose();
    }
    public void pause() {
    }
    public void render() {
        if (getPlayer().totalPartyKill()) {
            throw new GameOverException();
        } 
        delta = (Gdx.graphics.getDeltaTime() * 1000.0f);
        currentTime += Gdx.graphics.getDeltaTime();
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        currentCamera.update();
        batch.setProjectionMatrix(currentCamera.combined);
        batch.begin();
        getCurrentState().render(batch);
        batch.end();
//        sleep(60);
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
        Gdx.graphics.setTitle(Integer.toString(Gdx.graphics.getFramesPerSecond()));
        
    }

    public void sleep(int fps) {
        if (fps > 0) {
            diff = System.currentTimeMillis() - start;
            long targetDelay = 1000 / fps;
            if (diff < targetDelay) {
                try {
                    Thread.sleep(targetDelay - diff);
                } catch (InterruptedException e) {
                }
            }
            start = System.currentTimeMillis();
        }
    }
}
