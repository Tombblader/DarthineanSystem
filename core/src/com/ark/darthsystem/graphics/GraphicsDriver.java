package com.ark.darthsystem.graphics;

import com.ark.darthsystem.states.Menu;
import com.ark.darthsystem.states.Title;
import com.ark.darthsystem.states.Message;
import com.ark.darthsystem.states.GameOver;
import com.ark.darthsystem.states.State;
import com.ark.darthsystem.states.OverheadMap;
import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.database.CollisionDatabaseLoader;
import com.ark.darthsystem.database.MapDatabase;

import java.util.ArrayList;
import com.ark.darthsystem.database.Database2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphicsDriver extends com.badlogic.gdx.Game {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private static SpriteBatch batch;
    private static float delta;
    private static Camera camera;
    private static Viewport viewport;
    private static PlayerCamera playerCamera;
    private static Viewport playerViewport;
    private static BitmapFont font;
    private static float currentTime;
    private static Array<State> states;
    private static TextureAtlas masterSheet;
    private static Music backgroundMusic;
    private static com.ark.darthsystem.graphics.Camera currentCamera;
    private static String backgroundMusicString = "";
    private static Sprite screenshot;
    private static ArrayList<Transition> transitions = new ArrayList<>();
    private static final float FONT_SCALE = 1f;
    private static AssetManager assets;

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
        return HEIGHT;
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static void newGame() {
        states = new Array<>(State.class);
        new Database2();
        new MapDatabase();
    }

    public static void loadGame(String load) {
        new Database2(load);
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

    public static void removeAllstates() {
        states.forEach(s -> {
            if (!(s instanceof OverheadMap)) {
                s.dispose();
            }
        });
        states.clear();
    }

    public static void addMenu(Menu menu) {
        if (menu.isPause() && !(getCurrentState() instanceof Menu)) {
            states.add(menu);
        } else {
            ((Menu) (getCurrentState())).addSubMenu(menu);
        }
    }

    public static void addState(State state) {
        states.add(state);
        if (state.getMusic() != null) {
            playMusic(state.getMusic());
        }
    }

    public static void setState(State getState) {
        if (!(states.get((states.size - 1)) instanceof OverheadMap)) {
            states.get((states.size - 1)).dispose();
        }
        states.set((states.size - 1), getState);
        if (getState.getMusic() != null) {
            playMusic(getState.getMusic());
        }
    }

    public static void setMessage(String header, ArrayList<String> message) {
        if (!(getCurrentState() instanceof Message)
                && !(getCurrentState() instanceof OverheadMap)) {
            states.add(new Message(header, message));
        } else {
            if ((getCurrentState() instanceof Message)) {
                ((Message) (getCurrentState())).addMessage(new Message(header, message));
            }
            if ((getCurrentState() instanceof OverheadMap)) {
//                ((OverheadMap) (getCurrentState())).appendMessage(getMessage);
            }
        }
    }

    public static void setMessage(String header, Animation<Sprite> face, ArrayList<String> message) {
        if (!(getCurrentState() instanceof Message)
                && !(getCurrentState() instanceof OverheadMap)) {
            states.add(new Message(header, face, message));
        } else {
            if ((getCurrentState() instanceof Message)) {
                ((Message) (getCurrentState())).addMessage(new Message(header, face, message));
            }
            if ((getCurrentState() instanceof OverheadMap)) {
//                ((OverheadMap) (getCurrentState())).appendMessage(getMessage);
            }
        }
    }

    public static void setMessage(ArrayList<String> message) {
        if (!(getCurrentState() instanceof Message) && !(getCurrentState() instanceof OverheadMap)) {
            states.add(new Message(message));
        } else {
            if ((getCurrentState() instanceof Message)) {
                ((Message) (getCurrentState())).addMessage(message);
            }
            if ((getCurrentState() instanceof OverheadMap)) {
                ((OverheadMap) (getCurrentState())).appendMessage(message);
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
                backgroundMusic.stop();
            }
            if (musicName != null && !musicName.equalsIgnoreCase("music/null")) {
                backgroundMusic = assets.get(musicName, Music.class);
                backgroundMusic.setLooping(true);
                backgroundMusicString = musicName;
                backgroundMusic.play();
            }
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
    }

    public static void clearAllstates() {
        for (State s : states) {
            s.dispose();
        }
        states.clear();
    }

    public static void flashScreen() {
        screenshot.setColor(1, 1, 1, 1);
    }

    public static AssetManager getAssets() {
        return assets;
    }

    private Input input;

    private long start = System.currentTimeMillis();

    @Override
    public void create() {
        assets = new AssetManager();
        loadAssets();
        assets.finishLoading();
        input = new Input();
        DisplayMode optimal = Gdx.graphics.getDisplayMode();
//        Gdx.graphics.setFullscreenMode(optimal);
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new Camera(w, h);
        viewport = new FitViewport(1024, 768, camera);
        playerCamera = new PlayerCamera(w, h);
        playerViewport = new FitViewport(32, 24, playerCamera);
        currentCamera = camera;
        batch = new SpriteBatch();
        masterSheet = assets.get("master/MasterSheet.atlas");
        for (AtlasRegion t : masterSheet.getRegions()) {
            if (t.splits == null) {
                t.flip(false, true);
            }
        }
        font = assets.get("fonts/monofont.ttf", BitmapFont.class);
        Gdx.input.setInputProcessor(input);
        states = new Array<>();
        states.add(new Title());
    }

    private void loadAssets() {
        InternalFileHandleResolver f = new InternalFileHandleResolver();
        assets = new AssetManager(f);
        assets.load("master/MasterSheet.atlas", TextureAtlas.class);
        assets.load("backgrounds/gameover.png", Texture.class);
        assets.load("backgrounds/title.png", Texture.class);
        assets.load("backgrounds/battle.png", Texture.class);
        FileHandle folder = f.resolve("").child("sounds");
        if (folder.list().length == 0) {
            loadAssetsInsideJar(f);
        } else {
            for (FileHandle asset : folder.list()) {
                if (asset.extension().equalsIgnoreCase("wav") || asset.extension().equalsIgnoreCase("ogg")) {
                    assets.load(asset.path(), Sound.class);
                }
            }
            folder = f.resolve("").child("music");
            for (FileHandle asset : folder.list()) {
                if (asset.extension().equalsIgnoreCase("mp3") || asset.extension().equalsIgnoreCase("wav")) {
                    assets.load(asset.path(), Music.class);
                }
            }

            folder = f.resolve("").child("maps");
            assets.setLoader(TiledMap.class, (new TmxMapLoader(f)));
            TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters() {
                {
                    flipY = false;
                }
            };
            for (FileHandle asset : folder.list()) {
                if (asset.extension().equalsIgnoreCase("tmx")) {
                    assets.load(asset.path(), TiledMap.class, parameters);
                }
            }
        }
        loadFonts(f);
        assets.finishLoading();
        //Post-Loading
        font = assets.get("fonts/monofont.ttf", BitmapFont.class);
        Array<TiledMap> temp = new Array<>();
        assets.getAll(TiledMap.class, temp);
        for (TiledMap t : temp) {
            for (TiledMapTileSet tileset : t.getTileSets()) {
                for (Iterator iterator = tileset.iterator(); iterator.hasNext();) {
                    TiledMapTile tiled = (TiledMapTile) (iterator.next());
                    tiled.getTextureRegion().flip(false, true);
                }
            }
        }
    }

    private void loadFonts(InternalFileHandleResolver f) {
        final int FONT_SIZE = 24;
        assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(f));
        assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(f));
        FreeTypeFontLoaderParameter fontParam = new FreeTypeFontLoaderParameter();
        fontParam.fontFileName = "fonts/monofont.ttf";
        fontParam.fontParameters.size = FONT_SIZE;
        fontParam.fontParameters.flip = true;
        fontParam.fontParameters.borderColor = Color.BLACK;
        fontParam.fontParameters.borderWidth = .1f;
        fontParam.fontParameters.color = Color.WHITE;
        assets.load("fonts/monofont.ttf", BitmapFont.class, fontParam);

    }

    private void loadAssetsInsideJar(InternalFileHandleResolver f) {
        Array<FileHandle> mapList = new Array<>();
        Array<FileHandle> soundList = new Array<>();
        Array<FileHandle> musicList = new Array<>();
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        final JarFile jar;
        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters() {
            {
                flipY = false;
            }
        };
        try {
            jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith("maps/")) { //filter according to the path
                    mapList.add(f.resolve(name));
                }
                if (name.startsWith("music/")) { //filter according to the path
                    musicList.add(f.resolve(name));
                }
                if (name.startsWith("sounds/")) { //filter according to the path
                    soundList.add(f.resolve(name));
                }
            }
            jar.close();
        } catch (IOException ex) {
            Logger.getLogger(MapDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (FileHandle file : soundList) {
            if (file.extension().equalsIgnoreCase("wav")) {
                GraphicsDriver.getAssets().load(file.path(), Sound.class);
            }
        }
        for (FileHandle file : musicList) {
            if (file.extension().equalsIgnoreCase("mp3")) {
                GraphicsDriver.getAssets().load(file.path(), Music.class);
            }
        }
        assets.setLoader(TiledMap.class, (new TmxMapLoader(f)));
        for (FileHandle file : mapList) {
            if (file.extension().equalsIgnoreCase("tmx")) {
                GraphicsDriver.getAssets().load(file.path(), TiledMap.class, parameters);
            }
        }
        loadFonts(f);

    }

    @Override
    public void dispose() {
        super.dispose();
        CollisionDatabaseLoader.dispose();
        batch.dispose();
        assets.dispose();
    }

    @Override
    public void render() {
        if (getPlayer() != null && getPlayer().totalPartyKill()) {
            BattleDriver.fullHeal(getPlayer().getAllBattlers());
            removeAllstates();
            addState(new GameOver());
        }
        delta = (Gdx.graphics.getDeltaTime() * 1000.0f);
        currentTime += Gdx.graphics.getDeltaTime();
        if (!transitions.isEmpty()) {
            transitions.get(0).update(Gdx.graphics.getDeltaTime());
            if (transitions.get(0).isFinished()) {
                transitions.remove(0);
            }

        } else {
            getCurrentState().update(delta);
        }
        Gdx.gl30.glClearColor(0, 0, 0, 1);
        Gdx.gl30.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!(getCurrentState() instanceof OverheadMap)) {
            camera.update();
            viewport.apply(true);
            batch.setProjectionMatrix(camera.combined);
        } else {
            playerCamera.update();
            playerViewport.apply(true);
            batch.setProjectionMatrix(playerCamera.combined);
        }
        batch.begin();
        getCurrentState().render(batch);
        if (!transitions.isEmpty()) {
            State ste = null;
            for (State state : GraphicsDriver.getState()) {
                if (state instanceof OverheadMap) {
                    ste = state;
                }
            }
            if (ste != null) {
                SpriteBatch tempBatch = (SpriteBatch) ((OverheadMap) (ste)).getBatch();
                tempBatch.setProjectionMatrix(camera.combined);
                tempBatch.begin();
                transitions.get(0).render(tempBatch);
                tempBatch.end();
                tempBatch.setProjectionMatrix(playerCamera.combined);
            } else {
                batch.setProjectionMatrix(camera.combined);
                transitions.get(0).render(batch);
            }
        }
        batch.end();
        Gdx.graphics.setTitle(Integer.toString(Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void resize(int width, int height) {
        System.out.println(Gdx.app.getApplicationListener().
                toString()
                + " Resize: "
                + width
                + ","
                + height);
        viewport.update(width, height, false);
        playerViewport.update(width, height, true);
    }

    public void resume() {
        System.out.println("Resumed");
    }

}
