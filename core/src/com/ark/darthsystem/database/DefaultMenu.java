/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ark.darthsystem.database;

import com.ark.darthsystem.BattleDriver;
import com.ark.darthsystem.Battler;
import com.ark.darthsystem.Equipment;
import static com.ark.darthsystem.database.Database1.inventory;
import static com.ark.darthsystem.database.Database2.player;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.Item;
import com.ark.darthsystem.graphics.FieldBattler;
import com.ark.darthsystem.graphics.FieldSkill;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.states.Menu;
import com.ark.darthsystem.states.Title;
import com.ark.darthsystem.statusEffects.StatusEffect;
import java.io.IOException;

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
                        getItemList[i] = (i + 1) + ". " + inventory.get(i).getName() + " x" + inventory.get(i).getCharges();
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
                                DefaultMenu.this.addSubMenu(menuTarget);
                            } else {
                                (useItem.use(caster, targetList)).calculateDamage(new Battle(player.getAllActorBattlers(),
                                        player.getAllActorBattlers(),
                                        Database1.inventory,
                                        null));
                            }
                            return choice;
                        }

                        public void updateMenu(float delta) {
                            super.updateMenu(delta);
                            setHeader(Database1.inventory.get(getCursorIndex()).getDescription());
                        }

                    };
                    DefaultMenu.this.addSubMenu(menuItem);
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
                        FieldBattler caster = Database2.player.getBattler(sourceIndex);
                        String[] skillList = new String[caster.getSkillList().size()];
                        for (int i = 0; i < skillList.length; i++) {
                            skillList[i] = (i + 1) + ". " + caster.getSkillList().get(i).getSkill().getName() + " "
                                    + caster.getSkillList().get(i).getSkill().getCost() + " MP";
                        }
                        Menu menuTarget = new Menu("Select a Skill", skillList, true, true) {
                            @Override
                            public Object confirm(String choice) {
                                FieldSkill skill = caster.getSkillList().get(getCursorIndex());
                                if (skill.getSkill().getAlly()) {
                                    GraphicsDriver.addMenu(new Menu("Target?", Database2.player.getAllBattlers().toArray(new Battler[0])) {
                                        @Override
                                        public Object confirm(String choice) {
                                            skill.activate(Database2.player, caster, Database2.player.getAllActorBattlers().get(getCursorIndex()));
                                            return choice;
                                        }
                                    });
                                } else {
                                    caster.getSkillList().get(getCursorIndex()).activate(player);
                                }
                                return choice;
                            }

                            public void updateMenu(float delta) {
                                super.updateMenu(delta);
                                setHeader(caster.getSkillList().get(getCursorIndex()).getSkill().getDescription());
                            }
                        };
                        GraphicsDriver.addMenu(menuTarget);
                        return choice;
                    }
                };
                GraphicsDriver.addMenu(skillBattlers);
                break;
            case "Equip":
                StatusBox status = new StatusBox();
                addSubMenu(new Menu("Equip who?", Database2.player.getAllBattlers().toArray(new Battler[Database2.player.getAllBattlers().size()])) {
                    @Override
                    public Object confirm(String choice) {
                            DefaultMenu.this.addSubMenu(new Menu("Which Slot", Equipment.Slot.values()) {
                                @Override
                                public Object confirm(String choice) {
                                    Battler battler = Database2.player.getBattler(getCursorIndex()).getBattler();
                                     ArrayList<Equipment> equippableItems = new ArrayList<>();
                                     for (Item i : Database1.inventory) {
                                         if (i instanceof Equipment && battler.getBattlerClass().equippable((Equipment)(i))) {
                                             equippableItems.add((Equipment) i);
                                         }
                                     }
                                     if (equippableItems.isEmpty()) {
                                         reverseMenu();
                                     } else {
                                         DefaultMenu.this.addSubMenu(new Menu("Which Item?", equippableItems.toArray(new Equipment[equippableItems.size()])) {
                                             @Override
                                             public Object confirm(String choice) {
                                                 Database1.inventory.remove(getCursorIndex());
                                                 BattleDriver.addItem(battler.equip(equippableItems.get(getCursorIndex())));
                                                 return null;
                                             }
                                             
                                         });
                                     }
                                     
                                     return null;
                                }
                                {
                                    addActor(status);
                                }
                            });
                            return null;
                        }
                    

                });
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
                                    Database2.player.switchBattler(0);
                                } else {
                                    reverseMenu();
                                }
                                return choice;

                            }
                        };
                        DefaultMenu.this.addSubMenu(menuTarget);
                        return choice;
                    }
                };
                DefaultMenu.this.addSubMenu(menuBattlers);
                break;
            case "Status":
                ArrayList<FieldBattler> actorParty = new ArrayList<>();
                actorParty.addAll(Database2.player.getAllActorBattlers());
                getPlayerList = new String[actorParty.size()];
                for (int i = 0; i < getPlayerList.length; i++) {
                    getPlayerList[i] = actorParty.get(i).getBattler().getName();
                }
                DefaultMenu.this.addSubMenu(new Menu("Status",
                        getPlayerList,
                        true,
                        true) {
                    StatusBox statusBox = new StatusBox();
                    {
                        {
                            addActor(statusBox);
                            addActor(new TextBox("interface/window", 0, GraphicsDriver.getWidth() * 3 / 8f, GraphicsDriver.getWidth() / 8, GraphicsDriver.getHeight() / 8) {
                                @Override
                                public void update(float delta) {
                                    this.setMessage(Database1.money + " gold");
                                    statusBox.setMessage(statusBox.formatBattler(actorParty.get(getCursorIndex())));
                                    super.update(delta);
                                }
                            }
                            );
                        }
                    }

                    @Override
                    public Object confirm(String choice) {
                        DefaultMenu.this.reverseMenu();
                        return choice;
                    }
                });

                break;
            case "Save":
                DefaultMenu.this.addSubMenu(new Menu("Save to which slot?",
                        new String[]{"Slot 1", "Slot 2", "Slot 3"},
                        true,
                        true) {
                    @Override
                    public Object confirm(String choice) {
                        try {
                            Database1.save("save" + getCursorIndex() + ".sav");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return choice;
                    }
                });
                break;
            case "Return to Title":
                GraphicsDriver.getState().clear();
                GraphicsDriver.getState().add(new Title());
                GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
                break;
        }
        return choice;
    }

    private class StatusBox extends TextBox {

        StatusBox() {
            super("interface/window", 1024 / 4, 768 / 4, 1024 / 2, 768 / 2);
        }

        private String formatBattler(FieldBattler b) {
            StringBuilder formatted = new StringBuilder();
            Battler tempBattler = b.getBattler();
            formatted.append(tempBattler.getName());
            formatted.append('\n');

            if (tempBattler.getAllStatus().size() < 2) {
                formatted.append(tempBattler.getAllStatus().get(0).getName());
            } else {
                for (int i = 0; i < tempBattler.getAllStatus().size(); i++) {
                    StatusEffect effect = tempBattler.getAllStatus().get(i);
                    if (!effect.getName().equals("Normal")) {
                        formatted.append(effect.getName());
                        if (i + 1 != tempBattler.getAllStatus().size()) {
                            formatted.append(", ");
                        }
                    }
                }
            }
            formatted.append('\n');
            formatted.append("HP:  ");
            formatted.append(tempBattler.getHP());
            formatted.append(" / ");
            formatted.append(tempBattler.getMaxHP());
            formatted.append('\n');
            formatted.append("MP:  ");
            formatted.append(tempBattler.getMP());
            formatted.append(" / ");
            formatted.append(tempBattler.getMaxMP());
            formatted.append('\n');
            formatted.append("ATK: ");
            formatted.append(tempBattler.getBaseAttack());
            formatted.append('\n');
            formatted.append("DEF: ");
            formatted.append(tempBattler.getBaseDefense());
            formatted.append('\n');
            formatted.append("SPD: ");
            formatted.append(tempBattler.getBaseSpeed());
            formatted.append('\n');
            formatted.append("MAG: ");
            formatted.append(tempBattler.getBaseMagic());
            formatted.append('\n');
            return formatted.toString();
        }
    };

}
