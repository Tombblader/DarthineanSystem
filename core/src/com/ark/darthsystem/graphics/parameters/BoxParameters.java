/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.graphics.parameters;

import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 *
 * @author Keven
 */
public class BoxParameters {
    private enum BodyType {
        CIRCLE,
        BOX,
        POLYGON
    }
    public void loadFromFile() {
        String parameters = "";
        String[] split = parameters.split(", ");
        Shape s;
        switch (split[1].toUpperCase()) {
            case "CIRCLE":
                break;
            case "BOX":
                break;
            case "POLYGON":
                break;
            
        }
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.1f; 
        fixtureDef.friction = 1.0f;
        fixtureDef.restitution = 0f;
    }
    private PolygonShape getRectangle(String[] parameters) {
        PolygonShape polygon = new PolygonShape();
        float width = Integer.parseInt(parameters[2]);
        float height = Integer.parseInt(parameters[3]);
        polygon.setAsBox(width / PlayerCamera.PIXELS_TO_METERS,
                height / PlayerCamera.PIXELS_TO_METERS);
        return polygon;
    }
    private CircleShape getCircle(String[] parameters) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(Integer.parseInt(parameters[2]) / PlayerCamera.PIXELS_TO_METERS);
        return circleShape;
    }
    private PolygonShape getPolygon(String[] parameters) {
        PolygonShape polygon = new PolygonShape();
//        float[] vertices = polygonObject.getPolygon().getTransformedVertices();
//        
//        float[] worldVertices = new float[vertices.length];
//        
//        for (int i = 0; i < vertices.length; ++i) {
//            worldVertices[i] = vertices[i] / ppt;
//        }
//        
//        polygon.set(worldVertices);
        return polygon;
    }
    private ChainShape getPolyline(String parameters) {
//        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
//        Vector2[] worldVertices = new Vector2[vertices.length / 2];
//        
//        for (int i = 0; i < vertices.length / 2; ++i) {
//            worldVertices[i] = new Vector2();
//            worldVertices[i].x = vertices[i * 2] / ppt;
//            worldVertices[i].y = vertices[i * 2 + 1] / ppt;
//        }
//        
//        ChainShape chain = new ChainShape();
//        chain.createChain(worldVertices);
//        return chain;
        return null;
    }
}
