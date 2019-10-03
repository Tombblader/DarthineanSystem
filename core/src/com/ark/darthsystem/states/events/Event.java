package com.ark.darthsystem.states.events;

import com.ark.darthsystem.graphics.ActorCollision;
import com.ark.darthsystem.states.OverheadMap;
import com.badlogic.gdx.maps.MapProperties;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Keven
 */
public abstract class Event extends ActorCollision implements Serializable {
    private int ID;
    private TriggerMethod trigger = null;
    private boolean isFinished;
    protected LocalSwitch switches;
    private boolean transience; //This determines if the event should be saved.

    public Event() {
        super();
    }
    
    public Event(String img, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
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
        return isFinished;
    }    
    
    public void setFinished(boolean finished) {
        isFinished = finished;
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
    
    @Override
    public void setMap(OverheadMap map) {
        super.setMap(map);
//        try {
//            switches = MapDatabase.getSwitchData(map.getMapName(), this);
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
        switches = new LocalSwitch(this.getClass().getSimpleName());
        
//        LocalSwitch switches = new LocalSwitch();
        
    }
    
    public void setMap(String map) {
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
