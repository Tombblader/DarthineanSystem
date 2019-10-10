package com.ark.darthsystem.states.events;

import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author keven
 */
public class EnemyDefeated extends Event {

    public EnemyDefeated(String img, int id, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
    }

    @Override
    public void run() {

    }

    @Override
    public boolean isTriggered(Event.TriggerMethod method) {
        boolean isDefeated = false;
        return super.isTriggered(method) && isDefeated;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public Event createFromMap(MapProperties prop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
