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
        this. //        Menu m = new Menu(header, Arrays.stream(choices).map(i -> i.getName()).collect(Collectors.toList()).toArray(new String[0])) {
                //        };
                itemList = choices;
        moneyMenu = new TextBox("interface/window", 0, GraphicsDriver.getWidth() * 2 / 8f, GraphicsDriver.getWidth() / 8, GraphicsDriver.getHeight() / 8) {
            @Override
            public void update(float delta) {
                this.setMessage(Database1.money + " gold");
                super.update(delta);
            }
        };
        addActor(moneyMenu);
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
                            ShopMenu.this.decreaseMenuIndex();
                        } else {
                            Item item = itemList[getCursorIndex()];
                            ShopMenu.this.addSubMenu(new Menu("Confirm?", new String[]{"Yes", "No"}) {
                                {
                                    addActor(moneyMenu);
                                }

                                @Override
                                public Object confirm(String choice) {
                                    if (choice.equals("Yes")) {
                                        BattleDriver.addItem(item);
                                        Database1.money -= item.getPrice();
                                    }
                                    ShopMenu.this.reverseMenu();
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
                        addActor(moneyMenu);
                    }
                });
                break;
            case "Sell":
                Item[] menuItems = Database1.inventory.toArray(new Item[Database1.inventory.size()]);
                if (Database1.inventory.isEmpty()) {
                    ShopMenu.this.cancel();
                } else {
                    addSubMenu(new Menu(messages[1], menuItems) {
                        @Override
                        public Object confirm(String choice) {

                            Item item = Database1.inventory.get(getCursorIndex());
                            ShopMenu.this.addSubMenu(new Menu("Confirm selling " + item.getName() + " for " + (item.getPrice() / 2) + "?", new String[]{"Yes", "No"}) {
                                {
                                    addActor(moneyMenu);
                                }

                                @Override
                                public Object confirm(String choice) {
                                    if (choice.equals("Yes")) {
                                        BattleDriver.removeItem(item, 1);
                                        Database1.money += item.getPrice() / 2;
                                    }
                                    ShopMenu.this.reverseMenu();
                                    if (Database1.inventory.isEmpty()) {
                                        reverseMenu();
                                    } else {
                                        ShopMenu.this.getCurrentMenu().setChoices(Database1.inventory);
                                    }
                                    return null;
                                }
                            });

                            return null;
                        }

                        {
                            addActor(new TextBox("interface/window", 0, 0, GraphicsDriver.getWidth(), GraphicsDriver.getHeight() / 8) {
                                public void update(float delta) {
                                    this.setMessage(itemList[getCursorIndex()].getDescription());
                                    super.update(delta);
                                }
                            });
                            addActor(moneyMenu);

                        }
                    });
                }
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
