/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.Database;

import com.ark.darthsystem.Battler;
import static com.ark.darthsystem.Database.Database1.inventory;
import static com.ark.darthsystem.Database.Database2.player;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.States.Battle;
import com.ark.darthsystem.States.Menu;
import com.ark.darthsystem.States.Title;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Keven
 */
public class DefaultMenu extends Menu {

    public DefaultMenu() {
        super("",
                new String[]{"Item", "Skill", "Equip", "Reorder", "Status", "Save", "Return to Title", "Back"},
                true,
                true);
    }

    @Override
    public String confirm(String choice) {
        switch (choice) {
            case "Item":
                final Battler caster = player.getCurrentBattler().getBattler();
                if (inventory != null && !inventory.isEmpty()) {
                    String[] getItemList = new String[inventory.size()];
                    for (int i = 0; i < getItemList.length; i++) {
                        getItemList[i] = inventory.get(i).getName();
                    }
                    Menu menuItem = new Menu("Use which Item?",
                            getItemList,
                            true,
                            true) {
                        @Override
                        public Object confirm(String choice) {
                            final Item useItem;
                            useItem = inventory.get(this.getCursorIndex());
                            final ArrayList<Battler> targetList = Database2.player.getAllBattlers();
                            if (!useItem.getAll()) {
                                String[] temp = new String[targetList.size()];
                                for (int i = 0; i < temp.length; i++) {
                                    temp[i] = targetList.get(i).getName();
                                }
                                Menu menuTarget = new Menu("Target?",
                                        temp,
                                        true,
                                        true) {
                                    @Override
                                    public Object confirm(String choice) {
                                        (useItem.use(caster, targetList.get(getCursorIndex()),
                                                targetList)).calculateDamage(new Battle(player.getAllActorBattlers(),
                                                                player.getAllActorBattlers(),
                                                                Database1.inventory,
                                                                null));
                                        return choice;
                                    }
                                };
                                GraphicsDriver.addMenu(menuTarget);
                            } else {
                                (useItem.use(caster, targetList)).calculateDamage(new Battle(player.getAllActorBattlers(),
                                                player.getAllActorBattlers(),
                                                Database1.inventory,
                                                null));
                            }
                            return choice;
                        }
                    };
                    GraphicsDriver.addMenu(menuItem);
                } else {

                }
                break;
            case "Reorder":
                final ArrayList<Battler> party = new ArrayList<>();
                party.addAll(Database2.player.getAllBattlers());
                final String[] getPlayerList = new String[party.size()];
                for (int i = 0; i < getPlayerList.length; i++) {
                    getPlayerList[i] = party.get(i).getName();
                }
                Menu menuBattlers = new Menu("Switching Battlers..",
                        getPlayerList,
                        true,
                        true) {
                    @Override
                    public Object confirm(String choice) {
                        final int sourceIndex = getCursorIndex();
                        Menu menuTarget = new Menu("Target?",
                                getPlayerList,
                                true,
                                true) {
                            @Override
                            public Object confirm(String choice) {
                                Collections.swap(Database2.player.getAllActorBattlers(),
                                        sourceIndex,
                                        getCursorIndex());
                                return choice;
                            }
                        };
                        GraphicsDriver.addMenu(menuTarget);
                        return choice;
                    }
                };
                GraphicsDriver.addMenu(menuBattlers);
                break;
            case "Return to Title":
                GraphicsDriver.getState().clear();
                GraphicsDriver.getState().add(new Title());
                GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
                break;
        }
        return choice;
    }
}
