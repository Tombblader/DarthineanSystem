package com.ark.darthsystem.states.events;

import com.ark.darthsystem.graphics.ActorSprite;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.ark.darthsystem.states.OverheadMap;
import com.ark.darthsystem.states.State;
import com.badlogic.gdx.maps.MapProperties;
import java.util.Objects;

/**
 *
 * @author Keven
 */
public class Teleport extends Event {

    private String map;
    private float newX;
    private float newY;

    public Teleport() {
        super("", 0, 0, 6 / 60f);
    }

    public Teleport(String img, float getX,
            float getY,
            float getDelay, String newMap, float xCoord, float yCoord) {
        super(img, getX, getY, getDelay);
        map = newMap;
        newX = xCoord + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionX();
        newY = yCoord + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionY();
        setTriggerMethod(TriggerMethod.TOUCH);
    }

    public Teleport(String img, float getX,
            float getY,
            float getDelay, String newMap, int xCoord, int yCoord) {
        super(img, getX, getY, getDelay);
        setTriggerMethod(TriggerMethod.TOUCH);
        map = newMap;
        newX = xCoord + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionX();
        newY = yCoord + 16 / PlayerCamera.PIXELS_TO_METERS + GraphicsDriver.getCamera().getScreenPositionY();
    }

    @Override
    public Event createFromMap(MapProperties prop) {
        String[] parameters = prop.get("parameters", String.class).split(",* ");
        String image = prop.get("image", String.class);
        setX(prop.get("x", Float.class));
        setY(prop.get("y", Float.class));
        map = parameters[0];
        newX = Integer.parseInt(parameters[1]);
        newY = Integer.parseInt(parameters[2]);
        Teleport t = new Teleport(image, prop.get("x", Float.class), prop.get("y", Float.class), 6 / 60f, map, newX, newY);
        t.setTriggerMethod(TriggerMethod.valueOf(prop.get("trigger", String.class).toUpperCase()));
        return t;
    }

    @Override
    public void run() {
        for (State s : GraphicsDriver.getState()) {
            if (s instanceof OverheadMap) {
                OverheadMap state = ((OverheadMap) s);
                state.clearTemporaryActors();
                GraphicsDriver.getPlayer().setFieldState(ActorSprite.SpriteModeField.STAND);
                state.setMap(map);
                GraphicsDriver.getPlayer().setMap(state, newX, newY);
                GraphicsDriver.transition();
//                GraphicsDriver.getState().set(GraphicsDriver.getState().indexOf(s, true), state);
                GraphicsDriver.playMusic(state.getMusic());
                state.updatePartial(0f);
            }
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.map);
        hash = 73 * hash + Float.floatToIntBits(this.newX);
        hash = 73 * hash + Float.floatToIntBits(this.newY);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Teleport other = (Teleport) obj;
        if (Float.floatToIntBits(this.newX) != Float.floatToIntBits(other.newX)) {
            return false;
        }
        if (Float.floatToIntBits(this.newY) != Float.floatToIntBits(other.newY)) {
            return false;
        }
        if (!Objects.equals(this.map, other.map)) {
            return false;
        }
        return super.equals(obj);
    }

}
