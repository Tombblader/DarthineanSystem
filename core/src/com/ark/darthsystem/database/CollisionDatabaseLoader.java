package com.ark.darthsystem.database;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import java.util.HashMap;

public class CollisionDatabaseLoader {
    private static final float ppt = 32f;
    private static HashMap<String, Shape> shapes;
    private static MapLayer layer;
    public CollisionDatabaseLoader() {
        shapes = new HashMap<>();
        TiledMap tiledMap = MapDatabase.getMaps().get("skillshapes").getMap();
        layer = tiledMap.getLayers().get("collisions");
            for(MapObject object : layer.getObjects()) {
                Shape shape;
                if (object instanceof RectangleMapObject) {
                    shape = getRectangle((RectangleMapObject)object);
                }
                else if (object instanceof PolygonMapObject) {
                    shape = getPolygon((PolygonMapObject)object);
                }
                else if (object instanceof PolylineMapObject) {
                    shape = getPolyline((PolylineMapObject)object);
                }
                else if (object instanceof EllipseMapObject) {
                    shape = getCircle((EllipseMapObject)object);
                }
                else {
                    continue;
                }
                shapes.put(object.getName(), shape);
            }
                
    }
    
    public static Shape getShape(String shapeName) {
        TiledMap tiledMap = MapDatabase.getMaps().get("skillshapes").getMap();
        Shape shape;
        MapObject object = tiledMap.getLayers().get("collisions").getObjects().get(shapeName);
        if (object instanceof RectangleMapObject) {
            shape = getRectangle((RectangleMapObject)object);
        }
        else if (object instanceof PolygonMapObject) {
            shape = getPolygon((PolygonMapObject)object);
        }
        else if (object instanceof PolylineMapObject) {
            shape = getPolyline((PolylineMapObject)object);
        }
        else if (object instanceof EllipseMapObject) {
            shape = getCircle((EllipseMapObject)object);
        }
        else {
            shape = null;
        }
            
        return shape;
    }
    
    public static HashMap<String, Shape> getShapes() {
        return shapes;
    }
    
    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) / ppt,
                (rectangle.y + rectangle.height * 0.5f ) / ppt);
        polygon.setAsBox(rectangle.width * 0.5f / ppt,
                rectangle.height * 0.5f / ppt);
        return polygon;
    }

    private static CircleShape getCircle(EllipseMapObject circleObject) {
        Ellipse circle = circleObject.getEllipse();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(circle.height / 2 / ppt);
        return circleShape;
    }
    
    
    private static PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        polygonObject.getPolygon().setPosition(-polygonObject.getPolygon().getBoundingRectangle().width / 2, polygonObject.getPolygon().getBoundingRectangle().height / 2);
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();
        
        float[] worldVertices = new float[vertices.length];
        
        for (int i = 0; i < vertices.length; ++i) {
            worldVertices[i] = vertices[i] / ppt;
        }
        
        polygon.set(worldVertices);
        return polygon;
    }
    private static ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];
        
        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] / ppt;
            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
        }
        
        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
    public static void dispose() {
        shapes.forEach((k, v) -> v.dispose());
    }
}
