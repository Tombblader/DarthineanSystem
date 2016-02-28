package com.ark.darthsystem;

import com.ark.darthsystem.States.Battle;

import java.io.Serializable;

import static com.ark.darthsystem.BattleDriver.*;
import com.ark.darthsystem.Database.SoundDatabase;

import java.util.ArrayList;

public class Action implements Serializable {

    private static final int ATTACK_CONSTANT = 10;
    private static final int DEFENSE_CONSTANT = 6;
    private static final int ATTACK_RATIO = 5;
    private Battle.Command actionCommand;
    private Skill actionSkill;
    private Item actionItem;
    private Battler caster;
    private Battler target;
    private ArrayList<Battler> allTargets;
    private int damage;

    public Action(Battle.Command initializeCommand) {
        actionCommand = initializeCommand;
    }

    public Action(Battle.Command initializeCommand, Battler getCaster) {
        actionCommand = initializeCommand;
        caster = getCaster;
    }

    public Action(Battle.Command initializeCommand,
            Battler getCaster,
            Battler getTarget,
            ArrayList<Battler> getAllTargets) {
        actionCommand = initializeCommand;
        target = getTarget;
        allTargets = getAllTargets;
        caster = getCaster;
    }

    public Action(Battle.Command initializeCommand,
            Item initializeItem,
            Battler initializeCaster,
            Battler initializeTarget,
            ArrayList<Battler> initializeAllTargets) {
        actionCommand = initializeCommand;
        actionItem = initializeItem;
        caster = initializeCaster;
        target = initializeTarget;
        allTargets = initializeAllTargets;
    }

    public Action(Battle.Command initializeCommand,
            Item initializeItem,
            Battler initializeCaster,
            ArrayList<Battler> initializeTarget) {
        actionCommand = initializeCommand;
        actionItem = initializeItem;
        caster = initializeCaster;
        allTargets = initializeTarget;
    }

    public Action(Battle.Command initializeCommand,
            Skill initializeSkill,
            Battler initializeCaster,
            Battler initializeTarget,
            ArrayList<Battler> initializeAllTargets) {
        actionCommand = initializeCommand;
        actionSkill = initializeSkill;
        caster = initializeCaster;
        target = initializeTarget;
//        isAll = false;
        allTargets = initializeAllTargets;
    }

    public Action(Battle.Command initializeCommand,
            Skill initializeSkill,
            Battler initializeCaster,
            ArrayList<Battler> initializeTarget) {
        actionCommand = initializeCommand;
        caster = initializeCaster;
        allTargets = initializeTarget;
//        isAll = true;
        actionSkill = initializeSkill;
    }

    public void declareAttack(Battle b) {
        if (checkStatus(b)) {
            switch (actionCommand) {
                case Attack:
                    printline(caster.getName() + " attacks!");
                    break;
                case Defend:
                    printline(caster.getName() + " defends.");
                    break;
                case Skill:
                    printline(caster.getName() + " activates " + actionSkill.getName() + "!");
                    break;
                case Charge:
                    printline(caster.getName() + " charges MP!");
                    break;
                case Item:
                    printline(caster.getName() + " uses " + actionItem.getName() + "!");
                    break;
                case Analyze:
                    printline(caster.getName() + " analyzes the enemy.");
                    break;
                case Run:
                    printline(caster.getName() + " runs away!");
            }
        }
    }

    public void calculateDamage(Battle b) {
        damage = 0;
        if (checkStatus(b)) {
            switch (actionCommand) {
                case Attack:
                    damage = (int) ((caster.getAttack() *
                            ATTACK_CONSTANT -
                            target.getDefense() *
                            DEFENSE_CONSTANT) /
                            ATTACK_RATIO *
                            (caster.getEquipment(Equipment.EquipmentType.RightArm.getSlot()) != null &&
                            caster.getEquipment(Equipment.EquipmentType.RightArm.getSlot()).getElement() != Battle.Element.Physical &&
                            caster.getEquipment(Equipment.EquipmentType.RightArm.getSlot()).getElement() ==
                            target.getElement().getWeakness() ? 2 : 1) * (caster.getEquipment(Equipment.EquipmentType.RightArm.getSlot()) != null &&
                            caster.getEquipment(
                                    Equipment.EquipmentType.RightArm.getSlot()).
                            getElement() !=
                            Battle.Element.Physical &&
                            caster.getEquipment(
                                    Equipment.EquipmentType.RightArm.getSlot()).
                            getElement() ==
                            caster.getElement() ? 1.5 : 1) *
                            (caster.getEquipment(
                                    Equipment.EquipmentType.RightArm.getSlot()) !=
                            null &&
                            caster.getEquipment(
                                    Equipment.EquipmentType.RightArm.getSlot()).
                            getElement() !=
                            Battle.Element.Physical &&
                            caster.getEquipment(
                                    Equipment.EquipmentType.RightArm.getSlot()).
                            getElement() ==
                            target.getElement() ? -1 : 1) *
                            (caster.getEquipment(
                                    Equipment.EquipmentType.RightArm.getSlot()) !=
                            null &&
                            caster.getEquipment(
                                    Equipment.EquipmentType.RightArm.getSlot()).
                            getElement() !=
                            Battle.Element.Physical &&
                            target.getEquipment(
                                    Equipment.EquipmentType.LeftArm.getSlot()) !=
                            null &&
                            caster.getEquipment(
                                    Equipment.EquipmentType.RightArm.getSlot()).
                            getElement() ==
                            target.getEquipment(Equipment.EquipmentType.LeftArm.getSlot()).getElement() ? .5 : 1) * (.9 + Math.random() * .25));
                    damageStep(b,
                            damage);
                    break;
                case Defend:
                    break;
                case Skill:
                    if (actionSkill.getCost() > caster.getMP()) {
                        print("However, " + caster.getName() + " doesn't have enough MP!");
                    } else {
                        caster.changeMP(actionSkill.getCost());
                        if (!actionSkill.getAll()) {
                            setNewTarget();
                            damage = actionSkill.calculateDamage(caster, target);
                            damageStep(b, damage);
                        } else {
                            allDamageStep(b);
                        }
                    }
                    break;
                case Charge:
                    caster.charge();
                    print(caster.getName() +
                            "'s MP has been restored!");
                    break;
                case Item:
                    if (actionItem.getInvoke() == null) {
                        while (!target.isAlive()) {
                            setNewTarget();
                        }
                        if (actionItem.getHPValue() > 0) {
                            print(target.getName() +
                                    "'s HP is restored by " +
                                    actionItem.getHPValue() +
                                    "!");
                        }
                        if (actionItem.getMPValue() >
                                0) {
                            print(target.getName() +
                                    "'s MP is restored by " +
                                    actionItem.getMPValue() +
                                    "!");
                        }
                        target.changeHP(-actionItem.getHPValue());
                        target.changeMP(-actionItem.getMPValue());
                    } else {
                        actionSkill = actionItem.getInvoke();
                        if ((actionItem.useMP() &&
                                actionSkill.getCost() > caster.getMP())) {
                            print("However, " +
                                    caster.getName() +
                                    " doesn't have enough MP!");
                        }
                        if (actionItem.getAll()) {
                            allDamageStep(b);
                        } else {
                            damageStep(b, actionSkill.calculateDamage(caster, target));
                        }
                    }
                    if (actionItem.getExpendable()) {
                        actionItem.decreaseQuantity(1);
                        if (actionItem.getQuantity() <= 0) {
                            b.getItem().remove(actionItem);
                        }
                    }
                    break;
                case Delay:
//                    (b).calculateDamage(b);
                    break;
                case Analyze:
//                    printline(caster.getName() + " analyzes the situation.");
//                    printStats(b.getEnemy());
                    break;
                case Run:
                    b.exitBattle();
            }
        }

    }

    private void damageStep(Battle b, int getDamage) {
//        setNewTarget();
        getDamage = getDamage <= 0 &&
                (actionSkill == null ||
                (actionSkill != null &&
                actionSkill.getElement() != Battle.Element.Heal)) ? (int) (Math.random() + 5) : getDamage;
        if (getDamage > 0) {
            if (b.getAlly().contains(target)) {
//                GraphicsDriver.getPlayer().hasTakenDamage(target);
            } else {
//               shakeScreen(DAMAGE_SHAKE);
            }
            SoundDatabase.ouchSound.stop();
            SoundDatabase.ouchSound.play();
        }
        String temp = ((actionSkill == null ||
                actionSkill.getElement() != Battle.Element.Heal) ? target.getName() +
                        " took " + ((int) (getDamage * target.getDefend())) + " damage!" : "");
        printline(temp);
        if (getDamage > 0) {
            if (b.getAlly().contains(target)) {
//                GraphicsDriver.getPlayer().hasTakenDamage(target);
            }
        }
        if (actionSkill != null) {
            printline(actionSkill.changeStatus(caster, target, b.getTurnCount()));
        }
        if (getDamage > 0 && target.getStatus().attackFaded()) {
            target.changeStatus(Battle.Stats.Normal, b.getTurnCount());
            printline(target.getName() + "'s status effect faded away.\n");
        }
        temp = target.changeHP(getDamage) ? target.getName() + " has fallen!\n" : "";
        if (!temp.equals("")) {
            print(temp);
        }
    }

    private void allDamageStep(Battle b) {
        for (Battler allTarget : allTargets) {
            damage = actionSkill.calculateDamage(caster, allTarget);
            if (allTarget.isAlive() ||
                    (actionSkill.getStatusEffect() == Battle.Stats.Death &&
                    actionSkill.getElement() == Battle.Element.Heal &&
                    !allTarget.isAlive())) {
                damage = damage <= 0 &&
                        (actionSkill == null ||
                        (actionSkill != null &&
                        actionSkill.getElement() != Battle.Element.Heal)) ? 0 : damage;
                if (damage > 0) {
                    if ((b.getAlly()).contains(allTarget)) {
//                        GraphicsDriver.getPlayer().hasTakenDamage(allTarget);
                    } else {

                    }
                }
                String tempMessage = ((actionSkill == null ||
                        actionSkill.getElement() != Battle.Element.Heal) ? 
                        allTarget.getName() + " took " +
                                ((int) (damage * allTarget.getDefend())) +
                                " damage!\n" : "");
                if (!tempMessage.equals("\n") && !tempMessage.equals("")) {
                    print(tempMessage);
                }
                tempMessage = actionSkill.changeStatus(caster, allTarget, b.getTurnCount());
                if (!tempMessage.equals("\n") && !tempMessage.equals("")) {
                    print(tempMessage);
                }
                if (damage > 0 && allTarget.getStatus().attackFaded()) {
                    allTarget.changeStatus(Battle.Stats.Normal, b.getTurnCount());
                    printline(allTarget.getName() + "'s status effect faded away.");
                }
                tempMessage = allTarget.changeHP(damage) ? allTarget.getName() + " has fallen!\n" : "";
                if (!tempMessage.equals("\n") && !tempMessage.equals("")) {
                    print(tempMessage);
                }
            }
        }
    }

    public Battler getCaster() {
        return caster;
    }

    public Battler getTarget() {
        return target;
    }

    public void setNewTarget() {
        boolean allAlive = true;
        for (Battler allTarget : allTargets) {
            allAlive = allAlive && allTarget.isAlive();
        }
        if (!allAlive) {
            while ((actionSkill != null &&
                    actionSkill.getStatusEffect() == Battle.Stats.Death &&
                    actionSkill.getElement() == Battle.Element.Heal &&
                    target.isAlive()) ||
                    (!target.isAlive() &&
                    (actionSkill == null ||
                    !(actionSkill.getStatusEffect() == Battle.Stats.Death &&
                    actionSkill.getElement() == Battle.Element.Heal)))) {
                target = allTargets.get((int) (Math.random() * allTargets.size()));
            }
        }
    }

    public Battle.Command getCommand() {
        return actionCommand;
    }

    private int criticalHit() {
        if (damage >
                0 &&
                caster.getAttack() / 255 +
                caster.getSpeed() / 255 +
                caster.getMagic() / 510 >
                Math.random()) {
            print("A critical hit!");
            return damage;
        }
        return 0;
    }

    private int dodge() {
        if ((damage > 0) &&
                (target != null) && 
                ((target.getSpeed() - caster.getSpeed()) / 255 + target.getSpeed() * 2 / 512 > Math.random())) {
            print("Miss!");
            return 0;
        }
        return 1;
    }

    private boolean checkStatus(Battle b) {
        boolean move = true;
        switch (caster.getStatus()) {
            case Sleep:
            case Petrify:
            case Death:
                move = false;
                break;
            case Silence:
                if (actionCommand == Battle.Command.Skill) {
                    move = false;
                }
                break;
            case Poison:
                printline(caster.getName() + " takes " + (caster.getMaxHP() / 14) + " damage from the poison.");
                if (caster.changeHP(caster.getMaxHP() / 14)) {
                    printline(caster.getName() + " has collapsed from the poison!");
                    move = false;
                }
                break;
            case Fog:
                break;
            case Confuse:
                target = (Math.random() > .5 ? b.getAlly((int) (Math.random() *
                        b.getAlly().size())) : b.getAlly((int) (Math.random() *
                        b.getAlly().size())));
//                allTargets = (Math.random() > .5 ? b.getAlly() : (b.getEnemy()));
                actionSkill = caster.getSkill((int) (Math.random() * caster.getSkillList().length));
                actionCommand = (Math.random() > .5 ? Battle.Command.Attack : Battle.Command.Skill);
                break;
        }
        return move;
    }

    public Skill getSkill() {
        return actionSkill;
    }

    public ArrayList<Battler> getAllTargets() {
        return allTargets;
    }
}
