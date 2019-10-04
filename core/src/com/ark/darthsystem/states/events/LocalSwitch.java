/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states.events;

import static com.ark.darthsystem.states.events.LocalSwitch.Switch.*;
import java.io.Serializable;
import java.util.EnumMap;

/**
 * This is data that should be serialized.
 * @author keven
 */
public class LocalSwitch implements Serializable, Cloneable {
    public enum Switch {
        A,
        B,
        C,
        D,
        FINISHED
    }
    
    public boolean isFinished() {
        return switches.get(FINISHED);
    }
    
    private EnumMap<Switch, Boolean> switches;
    
    public LocalSwitch() {
        switches = new EnumMap<>(Switch.class);
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
