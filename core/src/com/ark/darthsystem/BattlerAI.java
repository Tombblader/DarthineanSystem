package com.ark.darthsystem;

import com.ark.darthsystem.Graphics.ActorSkill;
import com.ark.darthsystem.States.Battle;
import com.badlogic.gdx.utils.Pool.Poolable;
import java.util.ArrayList;

/**
 *
 * @author Keven
 */
public class BattlerAI extends Battler implements Poolable {

    private static final int MAX_PRIORITY = 10;

    private AI[] AIData;
    private int experience;
    private Item drop;

    /**
     *
     * @param getName
     * @param newBattler
     * @param getAIData
     * @param initializeExperience
     * @param itemDrop
     * @param itemQuantity
     */
    public BattlerAI(String getName,
            Battler newBattler,
            AI[] getAIData,
            int initializeExperience,
            Item itemDrop,
            int itemQuantity) {
        super(getName,
                newBattler.getElement(),
                newBattler.getGender(),
                newBattler.getLevel(),
                newBattler.getMaxHP(),
                newBattler.getMaxMP(),
                newBattler.getBaseAttack(),
                newBattler.getBaseDefense(),
                newBattler.getBaseSpeed(),
                newBattler.getBaseMagic(),
                newBattler.getSkillList(),
                newBattler.getEquipmentList());
//         this.setSprite(newBattler.getBattlerSprite());
        AIData = getAIData;
        experience = initializeExperience;
        try {
            drop = (Item) (itemDrop.clone());
            itemDrop.setQuantity(itemQuantity);
        } catch (Exception e) {
        }
    }

    /**
     *
     * @param initializeName
     * @param initializeElement
     * @param initializeGender
     * @param initializeLevel
     * @param initializeHP
     * @param initializeMP
     * @param initializeAttack
     * @param initializeDefense
     * @param initializeSpeed
     * @param initializeMagic
     * @param initializeSkill
     * @param initializeEquipment
     * @param getAIData
     * @param initializeExperience
     * @param itemDrop
     * @param itemQuantity
     */
    public BattlerAI(String initializeName,
            Battle.Element initializeElement,
            Battler.Gender initializeGender,
            int initializeLevel,
            int initializeHP,
            int initializeMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic,
            Skill[] initializeSkill,
            Equipment[] initializeEquipment,
            AI[] getAIData,
            int initializeExperience,
            Item itemDrop,
            int itemQuantity) {
        super(initializeName,
                initializeElement,
                initializeGender,
                initializeLevel,
                initializeHP,
                initializeMP,
                initializeAttack,
                initializeDefense,
                initializeSpeed,
                initializeMagic,
                initializeSkill,
                initializeEquipment);
        AIData = getAIData;
        experience = initializeExperience;
        try {
            drop = (Item) (itemDrop.clone());
            drop.setQuantity(itemQuantity);
        } catch (Exception e) {
        }
    }
    
    public BattlerAI(String initializeName,
            Battle.Element initializeElement,
            Battler.Gender initializeGender,
            int initializeLevel,
            int initializeHP,
            int initializeMP,
            int initializeAttack,
            int initializeDefense,
            int initializeSpeed,
            int initializeMagic,
            Skill[] initializeSkill,
            ActorSkill initializeUnarmed,
            AI[] getAIData,
            int initializeExperience,
            Item itemDrop,
            int itemQuantity) {
        this(initializeName,
                initializeElement,
                initializeGender,
                initializeLevel,
                initializeHP,
                initializeMP,
                initializeAttack,
                initializeDefense,
                initializeSpeed,
                initializeMagic,
                initializeSkill,
                new Equipment[4], 
                getAIData,
                initializeExperience,
                itemDrop,
                itemQuantity);
        Equipment temp = new Equipment("Unarmed",
            Equipment.EquipmentType.LeftArm,
            null,
            false,
            0,
            0,
            0,
            0);
        temp.setAnimation(initializeUnarmed);
        equip(temp);
    }
    

    /**
     *
     * @param b : The battle that this entity is currently in.
     * @return : The resulting action.
     */
    public Action getCommand(Battle b) {
        Action tempAction = null;
        Skill getSkill;
        
        switch (interpretAI(b)) {
            case Run:
                tempAction = new Action(Battle.Command.Run, this);
                break;
            case Attack:
                tempAction = new Action(Battle.Command.Attack,
                        this,
                        b.getAlly((int) (Math.random() * b.getAlly().size())),
                        b.getAlly());
                break;
            case Nothing:
                tempAction = null;
                break;
            case Defend:
                tempAction = new Action(Battle.Command.Defend, this);
                break;
            case Heal:
                do {
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().length));
                } while ((getSkill == null ||
                        getSkill.getLevel() > getLevel()) ||
                        getSkill.getElement() != Battle.Element.Heal ||
                        getSkill.getStatusEffect() != Battle.Stats.Normal);
                if (getSkill.getCost() > getMP()) {
                    tempAction = new Action(Battle.Command.Charge, this);
                } else if (getSkill.getAll()) {
                    tempAction = new Action(Battle.Command.Skill,
                            getSkill, this,
                            (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
                } else {
                    tempAction = new Action(Battle.Command.Skill,
                            getSkill, this,
                            getSkill.getAlly() ? b.getEnemy((int) (Math.random() * b.getEnemy().size())) : b.getAlly((int) (Math.random() * b.getAlly().size())),
                            (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
                }
                break;
            case Revive:
                Battler deadBattler;
                
                do {
                    deadBattler = b.getEnemy((int) (Math.random() * b.getEnemy().size()));
                } while (deadBattler.isAlive());
                
                do {
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().length));
                } while ((getSkill == null ||
                        getSkill.getLevel() > getLevel()) ||
                        getSkill.getElement() != Battle.Element.Heal ||
                        getSkill.getStatusEffect() != Battle.Stats.Death);
                
                tempAction = new Action(Battle.Command.Skill,
                        getSkill, this,
                        getSkill.getAlly() ? deadBattler : b.getAlly((int) (Math.random() * b.getAlly().size())),
                        (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
                break;
            case AttackSkill:
                do {
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().length));
                } while (getSkill == null ||
                        getSkill.getLevel() > getLevel() ||
                        getSkill.calculateDamage(this,  b.getAlly((int) (Math.random() *  b.getAlly().size()))) <= 0);
                if (getSkill.getCost() > getMP()) {
                    tempAction = new Action(Battle.Command.Charge, this);
                } else if (getSkill.getAll()) {
                    tempAction = new Action(Battle.Command.Skill,
                            getSkill, this,
                            (ArrayList<Battler>) (getSkill.getAlly() ? b. getEnemy() : b.getAlly()));
                } else {
                    tempAction = new Action(Battle.Command.Skill,
                            getSkill, this,
                            getSkill.getAlly() ? b.getEnemy(
                                    (int) (Math.random() * b.getEnemy().size())) : b.getAlly((int) (Math.random() * b.getAlly().size())),
                            (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
                }
                break;
            case SupportSkill:
                do {
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().length));
                } while ((getSkill == null ||
                        getSkill.getLevel() > getLevel()) &&
                        getSkill.getStatusEffect() != Battle.Stats.Normal);
                if (getSkill.getCost() > getMP()) {
                    tempAction = new Action(Battle.Command.Charge, this);
                } else if (getSkill.getAll()) {
                    tempAction = new Action(Battle.Command.Skill,
                            getSkill, this,
                            (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
                } else {
                    tempAction = new Action(Battle.Command.Skill,
                            getSkill, this,
                            getSkill.getAlly() ? b.getEnemy((int) (Math.random() * b.getEnemy().size())) : b.getAlly((int) (Math.random() * b.getAlly().size())),
                            (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
                }
                break;
        }
        return tempAction;
    }

    private AI.Type interpretAI(Battle b) {
        AI tempAI;
        int tempPosition;
        do {
            tempPosition = (int) (Math.random() * AIData.length);
            tempAI = AIData[tempPosition].getPriority() <= Math.random() * MAX_PRIORITY + 1 ? AIData[tempPosition] : null;
        } while (tempAI == null || !tempAI.worthUsing(b));
        return tempAI.getType();
    }

    /**
     *
     * @return
     */
    public int getExperienceValue() {
        return experience;
    }

    /**
     *
     * @return
     */
    public Item getDroppedItem() {
        return drop;
    }
    
    public BattlerAI clone() {
        return new BattlerAI(getName(), super.clone(), AIData, experience, drop, drop.getQuantity());
    }

    @Override
    public void reset() {
        fullHeal();
    }
}
