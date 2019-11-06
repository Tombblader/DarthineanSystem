package com.ark.darthsystem.states.events;

import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.ActorCollision;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.maps.MapProperties;
import java.util.Objects;

/**
 *
 * @author Keven
 */
public abstract class Event extends ActorCollision {

    private int ID;
    private TriggerMethod trigger = null;
    protected LocalSwitch switches;

    public Event() {
        super();
        switches = new LocalSwitch();
    }

    public Event(String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
        switches = new LocalSwitch();
    }

    public abstract Event createFromMap(MapProperties prop);

    public enum TriggerMethod {
        TOUCH,
        PRESS,
        AUTO,
        PARALLEL,
        ADJACENT;
    }

    public boolean isTriggered(TriggerMethod trigger) {
        return trigger == getTriggerMethod();
    }

    public abstract void run();

    @Override
    public boolean isFinished() {
        return switches.isFinished();
    }

    public void setFinished(boolean finished) {
        switches.setSwitch(LocalSwitch.Switch.FINISHED, finished);
    }

    public final TriggerMethod getTriggerMethod() {
        return trigger;
    }

    public final void setTriggerMethod(TriggerMethod trigger) {
        this.trigger = trigger;
    }

    @Override
    public void generateBody(OverheadMap map) {
        super.generateBody(map);
        setMainFilter(ActorCollision.CATEGORY_EVENT, (short) (0));
        setSensorFilter(ActorCollision.CATEGORY_EVENT, CATEGORY_PLAYER);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if (getTriggerMethod() == TriggerMethod.AUTO) {
            run();
        }
    }

    public int getID() {
        return ID;
    }

    public void setID(int newID) {
        ID = newID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    /**
     *
     * @param map
     */
    @Override
    public void setMap(OverheadMap map) {
        switches = Database2.mapStates.getOrDefault(map.getMapName().toUpperCase() + "_" + ID, null);
        if (switches == null) {
            Database2.mapStates.put(map.getMapName().toUpperCase() + "_" + ID, new LocalSwitch());
            switches = Database2.mapStates.get(map.getMapName().toUpperCase() + "_" + ID);
        }
        if (!isFinished()) {
            super.setMap(map);
        }
    }
    
    public void setTemporaryMap(OverheadMap map) {
        super.setMap(map);
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
        final Event other = (Event) obj;
        return (!Objects.equals(this.getCurrentMapName(), other.getCurrentMapName()));
    }

}
