/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import java.io.Serializable;
import java.util.EnumMap;

/**
 *
 * @author keven
 */
public class LocalSwitch implements Serializable, Cloneable {
    public enum Switch {
        A,
        B,
        C,
        D
    }
    private String eventName;
    private EnumMap<Switch, Boolean> switches;
    
    public LocalSwitch() {
        switches = new EnumMap<>(Switch.class);
    }
    
    public LocalSwitch(String eventName) {
        this();
        this.eventName = eventName;
    }
    
    public String getEventName() {
        return eventName;
    }
    
    public void setSwitch(Switch s, boolean b) {
        switches.put(s, b);
    }
    
    public void switchOn(Switch s) {
        switches.put(s, true);
    }
    
    public void switchToggle(Switch s) {
        switches.put(s, !switches.get(s));
    }
            
    public boolean getSwitch(Switch s) {
        return switches.getOrDefault(s, false);
    }
}
