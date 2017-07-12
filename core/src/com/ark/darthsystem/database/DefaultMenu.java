/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.Battler;
import static com.ark.darthsystem.database.Database1.inventory;
import static com.ark.darthsystem.database.Database2.player;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.graphics.ActorBattler;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.states.Menu;
import com.ark.darthsystem.states.Title;

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
        Battler caster = player.getCurrentBattler().getBattler();
        switch (choice) {
            case "Item":
                if (inventory != null && !inventory.isEmpty()) {
                    String[] getItemList = new String[inventory.size()];
                    for (int i = 0; i < getItemList.length; i++) {
                        getItemList[i] = (i + 1) + ". " + inventory.get(i).getName() + " x" + inventory.get(i).getQuantity();
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
                                    temp[i] = (i + 1) + ". " + targetList.get(i).getName();
                                }
                                Menu menuTarget = new Menu("Target?", temp, true, true) {
                                    @Override
                                    public Object confirm(String choice) {
                                        useItem.use(caster, targetList.get(getCursorIndex()), targetList).calculateDamage(new Battle(player.getAllActorBattlers(), player.getAllActorBattlers(), Database1.inventory, null));
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
            case "Skill":
                ArrayList<Battler> party = new ArrayList<>();
                party.addAll(Database2.player.getAllBattlers());
                String[] getPlayerList = new String[party.size()];
                for (int i = 0; i < getPlayerList.length; i++) {
                    getPlayerList[i] = (i + 1) + ". " + party.get(i).getName();
                }
                Menu skillBattlers = new Menu("Select a battler.",
                        getPlayerList,
                        true,
                        true) {
                    @Override
                    public Object confirm(String choice) {
                        final int sourceIndex = getCursorIndex();
                        ActorBattler caster = Database2.player.getBattler(sourceIndex);
                        String[] skillList = new String[caster.getSkillList().size()];
                        for (int i = 0; i < skillList.length; i++) {
                            skillList[i] = (i + 1) + ". " + caster.getSkillList().get(i).getSkill().getName() + " " +
                                    caster.getSkillList().get(i).getSkill().getCost() + " MP";
                        }
                        Menu menuTarget = new Menu("Select a Skill", skillList, true, true) {
                            @Override
                            public Object confirm(String choice) {
                                caster.getSkillList().get(getCursorIndex()).activate(player);
//                                if (caster.getBattler().getMP())
                                return choice;
                                
                            }
                        };
                        GraphicsDriver.addMenu(menuTarget);
                        return choice;
                    }
                };
                GraphicsDriver.addMenu(skillBattlers);
                break;
            case "Reorder":
                party = new ArrayList<>();
                party.addAll(Database2.player.getAllBattlers());
                getPlayerList = new String[party.size()];
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
                        Menu menuTarget = new Menu("Target?", getPlayerList, true, true) {
                            @Override
                            public Object confirm(String choice) {
                                if (Database2.player.getAllBattlers().get(getCursorIndex()).isAlive()) {
                                    Collections.swap(Database2.player.getAllActorBattlers(), sourceIndex, getCursorIndex());
                                } else {
                                    cancel();
                                }
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
