package com.ark.darthsystem.states.events;

import com.ark.darthsystem.graphics.ActorCollision;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author keven
 */
public class CreateShape extends Event {

    private String openImage;
    private String shape;

    public CreateShape(String img, String openImage, float getX, float getY, float getDelay, String newMap, String shape) {
        super(img, getX, getY, getDelay);
        this.shape = shape;
    }

    @Override
    public void run() {
        ActorCollision a = new ActorCollision(openImage, getX(), getY(), getDelay(), shape);
        a.setMap(getCurrentMap());
        setFinished(true);
    }

    @Override
    public Event createFromMap(MapProperties prop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
