/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.states.Menu;

/**
 *
 * @author keven
 */
public class TargetMenu extends Menu {

    public TargetMenu(String header, String[] choices) {
        super(header, choices);
    }

    @Override
    public Object confirm(String choice) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
