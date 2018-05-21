package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;

import java.io.Serializable;

import static com.ark.darthsystem.BattleDriver.*;
import com.ark.darthsystem.database.SoundDatabase;
import com.ark.darthsystem.statusEffects.*;


import java.util.ArrayList;

/**
 * An action is a Battle object created when a battler declares an attack
 * against another battler.
 * @author Keven
 */
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

    /**
     *
     * @param actionCommand
     */
    public Action(Battle.Command actionCommand) {
        this.actionCommand = actionCommand;
    }

    /**
     *
     * @param command
     * @param caster
     */
    public Action(Battle.Command command, Battler caster) {
        this(command);
        this.caster = caster;
    }

    /**
     *
     * @param command
     * @param caster
     * @param target
     * @param allTargets
     */
    public Action(Battle.Command command,
            Battler caster,
            Battler target,
            ArrayList<Battler> allTargets) {
        this(command, caster);
        this.target = target;
        this.allTargets = allTargets;
    }

    /**
     *
     * @param command
     * @param item
     * @param caster
     * @param target
     * @param allTargets
     */
    public Action(Battle.Command command,
            Item item,
            Battler caster,
            Battler target,
            ArrayList<Battler> allTargets) {
        this(command, caster, target, allTargets);
        this.actionItem = item;
    }

    /**
     *
     * @param command
     * @param initializeItem
     * @param initializeCaster
     * @param initializeTarget
     */
    public Action(Battle.Command command,
            Item initializeItem,
            Battler initializeCaster,
            ArrayList<Battler> initializeTarget) {
        this.actionCommand = command;
        this.actionItem = initializeItem;
        this.caster = initializeCaster;
        this.allTargets = initializeTarget;
    }

    /**
     *
     * @param command
     * @param initializeSkill
     * @param initializeCaster
     * @param initializeTarget
     * @param initializeAllTargets
     */
    public Action(Battle.Command command,
            Skill initializeSkill,
            Battler initializeCaster,
            Battler initializeTarget,
            ArrayList<Battler> initializeAllTargets) {
        this.actionCommand = command;
        this.actionSkill = initializeSkill;
        this.caster = initializeCaster;
        this.target = initializeTarget;
//        isAll = false;
        this.allTargets = initializeAllTargets;
    }

    /**
     *
     * @param command
     * @param initializeSkill
     * @param initializeCaster
     * @param initializeTarget
     */
    public Action(Battle.Command command,
            Skill initializeSkill,
            Battler initializeCaster,
            ArrayList<Battler> initializeTarget) {
        this.actionCommand = command;
        this.caster = initializeCaster;
        this.allTargets = initializeTarget;
//        isAll = true;
        this.actionSkill = initializeSkill;
    }

    /**
     *
     * @param b
     */
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

    /**
     *
     * @param b
     */
    public void calculateDamage(Battle b) {
        damage = 0;
        switch (actionCommand) {
            case Attack:
                damage = (int) ((caster.getAttack() *
                        ATTACK_CONSTANT -
                        target.getDefense() * DEFENSE_CONSTANT) /
                        ATTACK_RATIO *
                        (caster.getEquipment(Equipment.Slot.MainHand.getSlot()) != null &&
                        caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() != Battle.Element.Physical &&
                        caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() ==
                        target.getElement().getWeakness() ? 2 : 1) * (caster.getEquipment(Equipment.Slot.MainHand.getSlot()) != null &&
                        caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() != Battle.Element.Physical &&
                        caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() ==
                        caster.getElement() ? 1.5 : 1) *
                        (caster.getEquipment(Equipment.Slot.MainHand.getSlot()) != null &&
                        caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() != Battle.Element.Physical &&
                        caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() == target.getElement() ? -1 : 1) * (caster.getEquipment(Equipment.Slot.MainHand.getSlot()) !=null &&
                        caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() != Battle.Element.Physical &&
                        target.getEquipment(Equipment.Slot.OffHand.getSlot()) != null &&
                        caster.getEquipment(Equipment.Slot.MainHand.getSlot()).getElement() == target.getEquipment(Equipment.Slot.OffHand.getSlot()).getElement() ? .5 : 1) * (.9 + Math.random() * .25));
                setNewTarget();
                damageStep(b, damage);
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
                print(caster.getName() + "'s MP has been restored!");
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
                    if (actionItem.getMPValue() > 0) {
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
                if (actionItem.isStackable()) {
                    actionItem.decreaseQuantity(1);
                    if (actionItem.getCharges() <= 0) {
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

    private void damageStep(Battle b, int getDamage) {
//        setNewTarget();
//        getDamage = getDamage <= 0 &&
//                (actionSkill == null ||
//                (actionSkill != null &&
//                actionSkill.getElement() != Battle.Element.Heal)) ? (int) (Math.random() + 5) : getDamage;
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
                        " took " + ((int) (getDamage * target.getDefend() * target.getStatusDamageModifier())) + " damage!" : "");
        if (!temp.equals("")) {
            printline(temp);
        }
        if (getDamage > 0) {
            if (b.getAlly().contains(target)) {
//                GraphicsDriver.getPlayer().hasTakenDamage(target);
            }
        }
        if (actionSkill != null) {
            printline(actionSkill.changeStatus(caster, target, b.getTurnCount()));
        }
        for (int i = 0; i < target.getAllStatus().size(); i++) {
            if (getDamage > 0 && target.getStatus(i).attackFaded()) {
                target.getAllStatus().remove(i);
                printline(target.getName() + "'s status effect faded away.\n");
                i--;
            }
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
                    (actionSkill.getStatusEffect() instanceof Death) &&
                    actionSkill.getElement() == Battle.Element.Heal &&
                    !allTarget.isAlive()) {
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
                for (int i = 0; i < allTarget.getAllStatus().size(); i++ ) {
                    if (damage > 0 && allTarget.getAllStatus().get(i).attackFaded()) {
                        allTarget.getAllStatus().remove(i);                        
                        printline(allTarget.getName() + "'s status effect faded away.");
                        i--;
                    }
                }
                tempMessage = allTarget.changeHP(damage) ? allTarget.getName() + " has fallen!\n" : "";
                if (!tempMessage.equals("\n") && !tempMessage.equals("")) {
                    print(tempMessage);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public Battler getCaster() {
        return caster;
    }

    /**
     *
     * @return
     */
    public Battler getTarget() {
        return target;
    }

    /**
     *
     */
    public void setNewTarget() {
        boolean allAlive = true;
        for (Battler allTarget : allTargets) {
            allAlive = allAlive && allTarget.isAlive();
        }
        if (!allAlive) {
            while ((actionSkill != null &&
                    actionSkill.getStatusEffect() instanceof Death &&
                    actionSkill.getElement() == Battle.Element.Heal &&
                    target.isAlive()) ||
                    (!target.isAlive() &&
                    (actionSkill == null ||
                    !(actionSkill.getStatusEffect() instanceof Death &&
                    actionSkill.getElement() == Battle.Element.Heal)))) {
                target = allTargets.get((int) (Math.random() * allTargets.size()));
            }
        }
    }

    /**
     *
     * @return
     */
    public Battle.Command getCommand() {
        return actionCommand;
    }

    private int criticalHit() {
        if (damage > 0 &&
                caster.getAttack() / 255f +
                caster.getSpeed() / 255f +
                caster.getMagic() / 510f >
                Math.random()) {
            print("A critical hit!");
            return damage;
        }
        return 0;
    }

    private int dodge() {
        if ((damage > 0) &&
                (target != null) && 
                ((target.getSpeed() - caster.getSpeed()) / 255f + target.getSpeed() * 2f / 512f > Math.random())) {
            print("Miss!");
            return 0;
        }
        return 1;
    }

    public boolean checkStatus(Battle b) {
        boolean move = true;
        move = caster.getAllStatus().stream().map((status) -> status.checkStatus(this, b)).reduce(move, (accumulator, _item) -> accumulator & _item);
        
//        switch (caster.getStatus()) {
//            case Sleep:
//            case Paralyze:
//            case Stun:
//            case Petrify:
//            case Death:
//                move = false;
//                break;
//            case Silence:
//                if (actionCommand == Battle.Command.Skill) {
//                    move = false;
//                }
//                break;
//            case Poison:
//                printline(caster.getName() + " takes " + (caster.getMaxHP() / 20) + " damage from the poison.");
//                if (caster.changeHP(caster.getMaxHP() / 20)) {
//                    printline(caster.getName() + " has collapsed from the poison!");
//                    move = false;
//                }
//                break;
//            case Fog:
//                break;
//            case Confuse:
//                target = (Math.random() > .5 ? b.getAlly((int) (Math.random() *
//                        b.getAlly().size())) : b.getAlly((int) (Math.random() *
//                        b.getAlly().size())));
////                allTargets = (Math.random() > .5 ? b.getAlly() : (b.getEnemy()));
//                actionSkill = caster.getSkill((int) (Math.random() * caster.getSkillList().size()));
//                actionCommand = (Math.random() > .5 ? Battle.Command.Attack : Battle.Command.Skill);
//                break;
//        }
        return move;
    }

    /**
     *
     * @return
     */
    public Skill getSkill() {
        return actionSkill;
    }
    
    public void setTarget(Battler target) {
        this.target = target;
    }

    public void setAllTarget(ArrayList<Battler> targets) {
        this.allTargets = targets;
    }

    /**
     *
     * @return
     */
    public ArrayList<Battler> getAllTargets() {
        return allTargets;
    }

    public void setSkill(Skill skill) {
        actionSkill = skill;
    }

    public void setCommand(Battle.Command command) {
        actionCommand = command;
    }

}
