/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.states.Menu;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 *
 * @author keven
 */
public class ShopMenu extends Menu {
    
    private Item[] itemList;
    private String[] messages;
    private TextBox descriptionWindow;
    private Menu itemMenu;
    private TextBox moneyMenu;
    public ShopMenu(String header, Item[] choices, String[] messages) {
        super(header, new String[]{"Buy", "Sell", "Exit"});
        itemList = choices;
        this.
//        Menu m = new Menu(header, Arrays.stream(choices).map(i -> i.getName()).collect(Collectors.toList()).toArray(new String[0])) {
//        };
        itemList = choices;
        addActor(new TextBox("interface/window", 0, GraphicsDriver.getWidth() * 2/8f, GraphicsDriver.getWidth() / 8, GraphicsDriver.getHeight() / 8) {
                @Override
                public void update(float delta) {
                    this.setMessage(Database1.money + " gold");
                    super.update(delta);
                }
            }
        );
        this.messages = messages;
    }
    
    @Override
    public Object confirm(String choice) {
        switch (choice) {
            case "Buy":
                addSubMenu(new Menu(messages[0], Arrays.stream(itemList).map(i -> i.getName()).collect(Collectors.toList()).toArray(new String[0])) {
                    @Override
                    public Object confirm(String choice) {
                        if (itemList[getCursorIndex()].getPrice() > Database1.money) {
                            setHeader("Not enough money!");
                            ShopMenu.this.cancelMenu();
                        } else {
                            Item item = itemList[getCursorIndex()];
                            ShopMenu.this.addSubMenu(new Menu("Confirm?", new String[]{"Yes", "No"}) {
                                @Override
                                public Object confirm(String choice) {
                                    if (choice.equals("Yes")) {
                                        BattleDriver.addItem((Item) item.clone());
                                        Database1.money -= item.getPrice();
                                    }
                                    ShopMenu.this.cancelMenu();
                                    ShopMenu.this.decreaseMenuIndex();
                                    return null;
                                }
                            });
                        }
                        return null;                        
                    }
                    {
                        addActor(new TextBox("interface/window", 0, 0, GraphicsDriver.getWidth(), GraphicsDriver.getHeight() / 8) {
                            public void update(float delta) {
                                this.setMessage(itemList[getCursorIndex()].getDescription());
                                super.update(delta);
                            }
                        });

                    }
                });
                        
                break;
            case "Sell":
                break;
            case "Exit":
                this.cancel();
        }
        return choice;        
    }
    
    private class ValidateMenu extends Menu {

        public ValidateMenu() {
            super("Confirm?", new String[]{"Yes", "No"});
        }

        @Override
        public Object confirm(String choice) {
            return choice;
        }
        
    }
    
}
