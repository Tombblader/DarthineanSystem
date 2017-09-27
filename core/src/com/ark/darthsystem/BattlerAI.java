package com.ark.darthsystem;

import com.ark.darthsystem.graphics.ActorSkill;
import com.ark.darthsystem.states.Battle;
import java.util.ArrayList;

/**
 * A Battler, but also has AI data, Experience Points, and Items dropped.
 * @author Keven
 */
public class BattlerAI extends Battler {

    private static final int MAX_PRIORITY = 10;

    private AI[] AIData;
    private int experience;
    private Item drop;

    /**
     * Creates a new instance of a BattlerAI based on a copy of a Battler.
     * @param name The name of the new BattlerAI
     * @param newBattler The Battler to base the BattlerAI on.
     * @param AIData The flags and priorities that the AI uses.
     * @param experience The amount of experience points the BattlerAI gives when defeated.
     * @param itemDrop The item dropped when defeated.
     * @param itemQuantity The amount of items dropped when defeated.
     */
    public BattlerAI(String name,
            Battler newBattler,
            AI[] AIData,
            int experience,
            Item itemDrop,
            int itemQuantity) {
        super(name,
                newBattler.getElement(),
                newBattler.getGender(),
                newBattler.getLevel(),
                newBattler.getMaxHP(),
                newBattler.getMaxMP(),
                newBattler.getBaseAttack(),
                newBattler.getBaseDefense(),
                newBattler.getBaseSpeed(),
                newBattler.getBaseMagic(),
                newBattler.getBattlerClass(),
                newBattler.getEquipmentList());
        this.AIData = AIData;
        this.experience = experience;
        try {
            drop = (Item) (itemDrop.clone());
            itemDrop.setQuantity(itemQuantity);
        } catch (Exception e) {
        }
    }

    /**
     * Create a new Instance of BattlerAI.
     * @param name The name of the BattlerAI
     * @param element The Element of the BattlerAI
     * @param gender The gender of the BattlerAI
     * @param level The level of the BattlerAI
     * @param HP How much damage can this Battler take before dying.
     * @param MP The amount of magic usable before needing to recharge
     * @param Attack The physical attack power of the Battler
     * @param Defense How much damage reduction the Battler has
     * @param Speed Determines whether or not the Battler goes first.  Also determines critical hit and dodge.
     * @param Magic Effectiveness of magic attacks and status effects.  Reduces magic damage as well.
     * @param skillList The skills the BattlerAI has access to immediately.
     * @param equipment The equipment currently equipped.
     * @param AIData The flags and priorities that the AI uses.
     * @param experience The amount of experience points the BattlerAI gives when defeated.
     * @param itemDrop The item dropped when defeated.
     * @param itemQuantity The amount of items dropped when defeated.
     */
    public BattlerAI(String name,
            Battle.Element element,
            Battler.Gender gender,
            int level,
            int HP,
            int MP,
            int Attack,
            int Defense,
            int Speed,
            int Magic,
            ArrayList<Skill> skillList,
            Equipment[] equipment,
            AI[] AIData,
            int experience,
            Item itemDrop,
            int itemQuantity) {
        super(name,
                element,
                gender,
                level,
                HP,
                MP,
                Attack,
                Defense,
                Speed,
                Magic,
                skillList,
                equipment);
        this.AIData = AIData;
        this.experience = experience;
        try {
            drop = (Item) (itemDrop.clone());
            drop.setQuantity(itemQuantity);
        } catch (Exception e) {
        }
    }
    
    /**
     * Create a new Instance of BattlerAI.
     * @param name The name of the BattlerAI
     * @param element The Element of the BattlerAI
     * @param gender The gender of the BattlerAI
     * @param level The level of the BattlerAI
     * @param HP How much damage can this Battler take before dying.
     * @param MP The amount of magic usable before needing to recharge
     * @param Attack The physical attack power of the Battler
     * @param Defense How much damage reduction the Battler has
     * @param Speed Determines whether or not the Battler goes first.  Also determines critical hit and dodge.
     * @param Magic Effectiveness of magic attacks and status effects.  Reduces magic damage as well.
     * @param battlerClass The class the BattlerAI is.
     * @param equipment The equipment currently equipped.
     * @param AIData The flags and priorities that the AI uses.
     * @param experience The amount of experience points the BattlerAI gives when defeated.
     * @param itemDrop The item dropped when defeated.
     * @param itemQuantity The amount of items dropped when defeated.
     */
    public BattlerAI(String name,
            Battle.Element element,
            Battler.Gender gender,
            int level,
            int HP,
            int MP,
            int Attack,
            int Defense,
            int Speed,
            int Magic,
            BattlerClass battlerClass,
            Equipment[] equipment,
            AI[] AIData,
            int experience,
            Item itemDrop,
            int itemQuantity) {
        super(name,
                element,
                gender,
                level,
                HP,
                MP,
                Attack,
                Defense,
                Speed,
                Magic,
                battlerClass,
                equipment);
        this.AIData = AIData;
        this.experience = experience;
        try {
            drop = (Item) (itemDrop.clone());
            drop.setQuantity(itemQuantity);
        } catch (Exception e) {
        }
    }
        
    
    
    /**
     * Create a new Instance of BattlerAI.
     * @param name The name of the BattlerAI
     * @param element The Element of the BattlerAI
     * @param gender The gender of the BattlerAI
     * @param level The level of the BattlerAI
     * @param HP How much damage can this Battler take before dying.
     * @param MP The amount of magic usable before needing to recharge
     * @param Attack The physical attack power of the Battler
     * @param Defense How much damage reduction the Battler has
     * @param Speed Determines whether or not the Battler goes first.  Also determines critical hit and dodge.
     * @param Magic Effectiveness of magic attacks and status effects.  Reduces magic damage as well.
     * @param skillList The skills the BattlerAI has access to immediately.
     * @param unarmedStrikeAnimation The animation to use when it's unarmed.
     * @param AIData The flags and priorities that the AI uses.
     * @param experience The amount of experience points the BattlerAI gives when defeated.
     * @param itemDrop The item dropped when defeated.
     * @param itemQuantity The amount of items dropped when defeated.
     */    
    public BattlerAI(String name,
            Battle.Element element,
            Battler.Gender gender,
            int level,
            int HP,
            int MP,
            int Attack,
            int Defense,
            int Speed,
            int Magic,
            ArrayList<Skill> skillList,
            ActorSkill unarmedStrikeAnimation,
            AI[] AIData,
            int experience,
            Item itemDrop,
            int itemQuantity) {
        this(name,
                element,
                gender,
                level,
                HP,
                MP,
                Attack,
                Defense,
                Speed,
                Magic,
                skillList,
                new Equipment[4], 
                AIData,
                experience,
                itemDrop,
                itemQuantity);
        Equipment temp = new Equipment("Unarmed",
            Equipment.EquipmentType.OffHand,
            null,
            false,
            0,
            0,
            0,
            0);
        temp.setAnimation(unarmedStrikeAnimation);
        equip(temp);
    }
    

    /**
     * Gets the command determined by the AI and ensures it is usable.
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
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().size()));
                } while (getSkill == null ||
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
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().size()));
                } while (getSkill == null ||
                        getSkill.getElement() != Battle.Element.Heal ||
                        getSkill.getStatusEffect() != Battle.Stats.Death);
                
                tempAction = new Action(Battle.Command.Skill,
                        getSkill, this,
                        getSkill.getAlly() ? deadBattler : b.getAlly((int) (Math.random() * b.getAlly().size())),
                        (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
                break;
            case AttackSkill:
                do {
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().size()));
                } while (getSkill == null ||
//                        getSkill.getLevel() > getLevel() ||
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
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().size()));
                } while (getSkill == null ||
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
        return new BattlerAI(getName(), getElement(), getGender(), getLevel(), 
                getHP(), getMP(), getAttack(), getDefense(), getSpeed(), 
                getMagic(), getSkillList(), getEquipmentList(), AIData, experience, drop, drop.getQuantity());
    }

}
