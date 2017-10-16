/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.Item;
import com.ark.darthsystem.states.Menu;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author keven
 */
public class ShopMenu extends Menu {
    
    Item[] itemList;
    public ShopMenu(String header, Item[] choices) {
        super(header, Arrays.stream(choices).map(i -> i.getName()).collect(Collectors.toList()).toArray(new String[0]));
        itemList = choices;
    }

    @Override
    public Object confirm(String choice) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
