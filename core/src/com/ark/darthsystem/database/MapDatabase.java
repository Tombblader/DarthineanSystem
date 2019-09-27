package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.OverheadMap;
import com.ark.darthsystem.states.events.LocalSwitch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Array;
import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MapDatabase {

    private static HashMap<String, String> maps;
    private static HashMap<String, TiledMap> cachedMaps;
    private static HashMap<String, Array<LocalSwitch>> visitedMaps;
    private static Array<String> mapNames;
    private static final int MIN_X_TILES = 32;
    private static final int MIN_Y_TILES = 24;
    public static final String DEFAULT_MAP = "dungeon_00";
    private static final float DEFAULT_X = 14;
    private static final float DEFAULT_Y = 21;

    public MapDatabase() {
        maps = new HashMap<>();
        cachedMaps = new HashMap<>();
        initializeMaps();
        new CollisionDatabaseLoader();
        setDefaultMap();
    }

    public static HashMap<String, String> getMaps() {
        return maps;
    }

//    public static OverheadMap getMap(String mapName) {
//        return new OverheadMap(maps.get(mapName), false);
//    }
    public static TiledMap getMap(String mapName) {
        return cachedMaps.get(mapName);
    }

    private void setDefaultMap() {
        Database2.player.setMap(new OverheadMap(DEFAULT_MAP, false), DEFAULT_X, DEFAULT_Y);
    }

    private void initializeMaps() {
        GraphicsDriver.getAssets().setLoader(TiledMap.class, (new TmxMapLoader(new InternalFileHandleResolver()) {
            {
                this.flipY = false;
            }
        }));
//        File f = new File(Gdx.files.internal("maps").file().toURI()); //CANNOT LIST INSIDE JAR
        Array<FileHandle> f = new Array<>();
        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        final JarFile jar;
        try {
            jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith("maps/")) { //filter according to the path
                    f.add(Gdx.files.internal(name));
                }
            }
            jar.close();
        } catch (IOException ex) {
            Logger.getLogger(MapDatabase.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (f.size > 0) {
            for (FileHandle file : f) {
                if (file.extension().equalsIgnoreCase("tmx")) {
                    maps.put(file.nameWithoutExtension(), file.path());
                    GraphicsDriver.getAssets().load(file.path(), TiledMap.class);
                    GraphicsDriver.getAssets().finishLoading();
                    cachedMaps.put(file.nameWithoutExtension(), GraphicsDriver.getAssets().get(file.path()));
                    for (MapLayer m : cachedMaps.get(file.nameWithoutExtension()).getLayers()) {
                        if (!(m instanceof TiledMapTileLayer)) {
                            for (MapObject mo : m.getObjects()) {
                                if (mo instanceof TextureMapObject) {
//                                    ((TextureMapObject) (mo)).getTextureRegion().flip(false, true);
                                }
                            }
                        }
                    }
                    for (TiledMapTileSet tileset : cachedMaps.get(file.nameWithoutExtension()).getTileSets()) {
                        for (Iterator iterator = tileset.iterator(); iterator.hasNext();) {
                            TiledMapTile tiled = (TiledMapTile) (iterator.next());
//                            tiled.getTextureRegion().flip(false, true);
                        }
                    }
                }
            }
        } else {
            initializeMapsAlt();
        }
    }

    private void initializeMapsAlt() {
        FileHandle[] f = Gdx.files.internal("maps").list(); //CANNOT LIST INSIDE JAR
        for (FileHandle file : f) {
            if (file.extension().equalsIgnoreCase("tmx")) {
                maps.put(file.nameWithoutExtension(), file.path());
                GraphicsDriver.getAssets().load(file.path(), TiledMap.class);
                GraphicsDriver.getAssets().finishLoading();
                cachedMaps.put(file.nameWithoutExtension(), GraphicsDriver.getAssets().get(file.path()));
                for (MapLayer m : cachedMaps.get(file.nameWithoutExtension()).getLayers()) {
                    if (!(m instanceof TiledMapTileLayer)) {
                        for (MapObject mo : m.getObjects()) {
                            if (mo instanceof TextureMapObject) {
                                ((TextureMapObject) (mo)).getTextureRegion().flip(false, true);
                            }
                        }
                    }
                }
                for (TiledMapTileSet tileset : cachedMaps.get(file.nameWithoutExtension()).getTileSets()) {
                    for (Iterator iterator = tileset.iterator(); iterator.hasNext();) {
                        TiledMapTile tiled = (TiledMapTile) (iterator.next());
                        tiled.getTextureRegion().flip(false, true);
                    }
                }
            }
        }
    }

    public static void dispose() {
//        maps.forEach((k, v) -> v.dispose());
    }
}
