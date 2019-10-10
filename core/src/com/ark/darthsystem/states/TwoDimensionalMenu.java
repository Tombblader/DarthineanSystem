/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.states;

/**
 *
 * @author Keven
 */
public abstract class TwoDimensionalMenu extends Menu {

    public TwoDimensionalMenu(String header, String[][] choices) {
        super(header, choices[0]);
    }

}
