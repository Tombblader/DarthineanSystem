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
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.ActorSprite;
import com.ark.darthsystem.graphics.FieldBattler;
import com.ark.darthsystem.graphics.FieldSkill;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.states.Menu;
import com.ark.darthsystem.states.Title;
import com.ark.darthsystem.statusEffects.StatusEffect;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
                        getItemList[i] = (i + 1) + ". " + inventory.get(i).getName() + (inventory.get(i).getCharges() > 0 ? " x" + inventory.get(i).getCharges() : "");
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

                        @Override
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
                        FieldBattler b = Database2.player.getBattler(getCursorIndex());
                        Battler battler = b.getBattler();
                        DefaultMenu.this.addSubMenu(new Menu("Which Slot", Equipment.Slot.values()) {
                            @Override
                            public Object confirm(String choice) {

                                ArrayList<Equipment> equippableItems = new ArrayList<>();
                                for (Item i : Database1.inventory) {
                                    if (i instanceof Equipment 
                                            && battler.getBattlerClass().equippable((Equipment) (i)) 
                                            && ((Equipment) (i)).getEquipmentType().getName().equalsIgnoreCase(choice)) {
                                        equippableItems.add((Equipment) i);
                                    }
                                }
                                if (equippableItems.isEmpty()) {
                                    reverseMenu();
                                } else {
                                    DefaultMenu.this.addSubMenu(new Menu("Which Item?", equippableItems.toArray(new Equipment[equippableItems.size()])) {
                                        @Override
                                        public Object confirm(String choice) {
                                            //Can't use the default remove operation.  Must remove only the same reference.
                                            for (int i = 0; i < Database1.inventory.size(); i++) {
                                                if (Database1.inventory.get(i) == equippableItems.get(getCursorIndex())) {
                                                    Database1.inventory.remove(i);
                                                    i = Database1.inventory.size(); // Ends the loop.
                                                }
                                            }
                                            BattleDriver.addItem(battler.equip(equippableItems.get(getCursorIndex())));
                                            return null;
                                        }

                                    });
                                }

                                return null;
                            }
                            @Override
                            public void updateMenu(float delta) {
                                status.updateBattler(b);
                                status.setMessage(status.formatBattler());
                                super.updateMenu(delta);
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
                StatusBox statusBox = new StatusBox();
                DefaultMenu.this.addSubMenu(new Menu("Status",
                        getPlayerList,
                        true,
                        true) {
                    {
                        TextBox moneyBox = new TextBox("interface/window", 0, GraphicsDriver.getWidth() * 3 / 8f, GraphicsDriver.getWidth() / 7, GraphicsDriver.getHeight() / 10) {
                            @Override
                            public void update(float delta) {
                                this.setMessage(Database1.money + " gold");
                                statusBox.updateBattler(actorParty.get(getCursorIndex()));
                                statusBox.setMessage(statusBox.formatBattler());
                                super.update(delta);
                            }
                                {
                                    addActor(new Actor("items/money/icon", getX() + getWidth() - 32, getY() + 16, 1/12f));
                                }
                        };
                        addActor(moneyBox);
                        addActor(statusBox);
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

    protected class StatusBox extends TextBox {
        
        private FieldBattler currentBattler;
        private Actor sprite;
        
        StatusBox() {
            super("interface/window", GraphicsDriver.getWidth() / 4, GraphicsDriver.getHeight() / 4, GraphicsDriver.getWidth() / 2, GraphicsDriver.getHeight() / 2);
        }        

        private void updateBattler(FieldBattler b) {
            if (currentBattler == null || currentBattler != b) {
                currentBattler = b;
                getActors().removeValue(sprite, true);                
                sprite = new Actor(currentBattler.getSprite().getMasterSpriteSheet() + "/battler/battler", 
                        200 + getX() + getWidth() + currentBattler.getSprite().getBattlerAnimation(ActorSprite.SpriteModeBattler.BATTLER).getKeyFrame(0).getWidth(),
                        100 + getY() + getHeight() / 3, currentBattler.getDelay());
                addActor(sprite);
            }
        }
        
        private String formatBattler() {
            StringBuilder formatted = new StringBuilder();
            Battler tempBattler = currentBattler.getBattler();
            formatted.append(tempBattler.getName()).append('\n');

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
            formatted.append("HP:  ").append(tempBattler.getHP()).append(" / ").append(tempBattler.getMaxHP()).append('\n');
            formatted.append("MP:  ").append(tempBattler.getMP()).append(" / ").append(tempBattler.getMaxMP()).append('\n');
            formatted.append("ATK: ").append(tempBattler.getBaseAttack()).append('\n');
            formatted.append("DEF: ").append(tempBattler.getBaseDefense()).append('\n');
            formatted.append("SPD: ").append(tempBattler.getBaseSpeed()).append('\n');
            formatted.append("MAG: ").append(tempBattler.getBaseMagic()).append('\n');
            for (Equipment.Slot slot : Equipment.Slot.values()) {
                formatted.append(slot.getName()).append(": ").append(tempBattler.getEquipment(slot) != null ? tempBattler.getEquipment(slot).getName() : "").append("\n");
            }
            return formatted.toString();
        }
    };

}
