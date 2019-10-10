package com.ark.darthsystem.database;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.events.LocalSwitch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import java.util.HashMap;

public class MapDatabase {

    private static Array<String> maps;
    private static HashMap<String, TiledMap> cachedMaps;
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
//        Database2.player.setMap(new OverheadMap(DEFAULT_MAP, false), DEFAULT_X, DEFAULT_Y);
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
}
