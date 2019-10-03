package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.OverheadMap;
import com.ark.darthsystem.states.events.LocalSwitch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

public class MapDatabase {

    private static Array<String> maps;
    private static HashMap<String, TiledMap> cachedMaps;
    private static HashMap<String, Array<LocalSwitch>> visitedMaps;
    private static Array<String> mapNames;
    private static final int MIN_X_TILES = 32;
    private static final int MIN_Y_TILES = 24;
    public static final String DEFAULT_MAP = "dungeon_00";
    public static final float DEFAULT_X = 14;
    public static final float DEFAULT_Y = 21;

    public MapDatabase() {
        maps = new Array<>();
        cachedMaps = new HashMap<>();
        initialize();
        new CollisionDatabaseLoader();
        setDefaultMap();
    }

    public static Array<String> getMaps() {
        return maps;
    }

    public static TiledMap getMap(String mapName) {
        return cachedMaps.get(mapName);
    }

    private void setDefaultMap() {
        Database2.player.setMap(new OverheadMap(DEFAULT_MAP, false), DEFAULT_X, DEFAULT_Y);
    }

    private void initialize() {
        Array<TiledMap> tempMaps = new Array<>();
        GraphicsDriver.getAssets().getAll(TiledMap.class, tempMaps);
        for (TiledMap map : tempMaps) {
            String mapName = GraphicsDriver.getAssets().getAssetFileName(map).substring(5, GraphicsDriver.getAssets().getAssetFileName(map).length() - 4);
            cachedMaps.put(mapName, map);
            maps.add(mapName);
        }
    }
    
//    private void initializeMaps() {
//        System.out.println("maps/" + DEFAULT_MAP + ".tmx");
//        if (GraphicsDriver.getAssets().containsAsset("maps/" + DEFAULT_MAP + ".tmx")) {
//            return;
//        }
//        GraphicsDriver.getAssets().setLoader(TiledMap.class, (new TmxMapLoader(new InternalFileHandleResolver())));
//        TmxMapLoader.Parameters parameters = new TmxMapLoader.Parameters() {
//            {
//                this.flipY = false;
//            }
//        };
////        File f = new File(Gdx.files.internal("maps").file().toURI()); //CANNOT LIST INSIDE JAR
//        Array<FileHandle> f = new Array<>();
//        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
//        final JarFile jar;
//        try {
//            jar = new JarFile(jarFile);
//            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
//            while (entries.hasMoreElements()) {
//                final String name = entries.nextElement().getName();
//                if (name.startsWith("maps/")) { //filter according to the path
//                    f.add(Gdx.files.internal(name));
//                }
//            }
//            jar.close();
//        } catch (IOException ex) {
//            Logger.getLogger(MapDatabase.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        if (f.size > 0) {
//            for (FileHandle file : f) {
//                if (file.extension().equalsIgnoreCase("tmx")) {
//                    maps.put(file.nameWithoutExtension(), file.path());
//                    GraphicsDriver.getAssets().load(file.path(), TiledMap.class, parameters);
//                    GraphicsDriver.getAssets().finishLoading();
//                    cachedMaps.put(file.nameWithoutExtension(), GraphicsDriver.getAssets().get(file.path()));
//                    for (TiledMapTileSet tileset : cachedMaps.get(file.nameWithoutExtension()).getTileSets()) {
//                        for (Iterator iterator = tileset.iterator(); iterator.hasNext();) {
//                            TiledMapTile tiled = (TiledMapTile) (iterator.next());
//                            tiled.getTextureRegion().flip(false, true);
//                        }
//                    }
//                }
//            }
//        } else {
//            initializeMapsAlt(parameters);
//        }
//    }
//
//    private void initializeMapsAlt(TmxMapLoader.Parameters parameters) {
//        FileHandle[] f = Gdx.files.internal("maps").list(); //CANNOT LIST INSIDE JAR
//        for (FileHandle file : f) {
//            if (file.extension().equalsIgnoreCase("tmx")) {
//                maps.put(file.nameWithoutExtension(), file.path());
//                GraphicsDriver.getAssets().load(file.path(), TiledMap.class, parameters);
//                GraphicsDriver.getAssets().finishLoading();
//                cachedMaps.put(file.nameWithoutExtension(), GraphicsDriver.getAssets().get(file.path()));
//                for (TiledMapTileSet tileset : cachedMaps.get(file.nameWithoutExtension()).getTileSets()) {
//                    for (Iterator iterator = tileset.iterator(); iterator.hasNext();) {
//                        TiledMapTile tiled = (TiledMapTile) (iterator.next());
//                        tiled.getTextureRegion().flip(false, true);
//                    }
//                }
//            }
//        }
//    }

    public static void dispose() {
        cachedMaps.forEach((k, v) -> v.dispose());
    }
}
