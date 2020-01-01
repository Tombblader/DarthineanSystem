package com.ark.darthsystem;

import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.FieldSkill;
import com.ark.darthsystem.states.Battle;
import com.ark.darthsystem.statusEffects.Death;
import com.ark.darthsystem.statusEffects.Normal;
import java.util.ArrayList;
import java.util.EnumMap;

/**
 * A Battler, but also has AI data, Experience Points, Money, and Items dropped.
 *
 * @author Keven
 */
public class BattlerAI extends Battler implements Nameable, Cloneable {

    private static final int MAX_PRIORITY = 10;

    private AI[] AIData;
    private int experience;
    private int money;
    private Item[] drop;
    private double[] dropPercent;

    public BattlerAI() {
        super();
        experience = 0;
        money = 0;
    }

    /**
     * Creates a new instance of a BattlerAI based on a copy of a Battler.
     *
     * @param name The name of the new BattlerAI
     * @param newBattler The Battler to base the BattlerAI on.
     * @param AIData The flags and priorities that the AI uses.
     * @param experience The amount of experience points the BattlerAI gives
     * when defeated.
     * @param itemDrop The item dropped when defeated.
     * @param dropPercent
     * @param itemQuantity The amount of items dropped when defeated.
     */
    public BattlerAI(String name,
            Battler newBattler,
            AI[] AIData,
            int experience,
            Item[] itemDrop,
            double[] dropPercent,
            int[] itemQuantity) {
        super(name,
                newBattler.getDescription(),
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
        this.money = 0;
        this.AIData = AIData;
        this.experience = experience;
        try {
            drop = (Item[]) (itemDrop.clone());
            this.dropPercent = dropPercent;
            for (int i = 0; i < drop.length; i++) {
                drop[i].setQuantity(itemQuantity[i]);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Create a new Instance of BattlerAI.
     *
     * @param name The name of the BattlerAI
     * @param description
     * @param element The Element of the BattlerAI
     * @param gender The gender of the BattlerAI
     * @param level The level of the BattlerAI
     * @param HP How much damage can this Battler take before dying.
     * @param MP The amount of magic usable before needing to recharge
     * @param Attack The physical attack power of the Battler
     * @param Defense How much damage reduction the Battler has
     * @param Speed Determines whether or not the Battler goes first. Also
     * determines critical hit and dodge.
     * @param Magic Effectiveness of magic attacks and status effects. Reduces
     * magic damage as well.
     * @param skillList The skills the BattlerAI has access to immediately.
     * @param equipment The equipment currently equipped.
     * @param AIData The flags and priorities that the AI uses.
     * @param experience The amount of experience points the BattlerAI gives
     * when defeated.
     * @param money The amount of money the BattlerAI gives when defeated.
     * @param itemDrop The item dropped when defeated.
     * @param dropRate The drop rate of the item.
     * @param itemQuantity The amount of items dropped when defeated.
     */
    public BattlerAI(String name,
            String description,
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
            EnumMap<Equipment.Slot, Equipment> equipment,
            AI[] AIData,
            int experience,
            int money,
            Item[] itemDrop,
            double[] dropRate,
            int[] itemQuantity) {
        super(name,
                description,
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
        this.money = money;
        this.AIData = AIData;
        this.experience = experience;
        try {
            drop = (Item[]) (itemDrop.clone());
            this.dropPercent = dropRate;
            for (int i = 0; i < drop.length; i++) {
                drop[i].setQuantity(itemQuantity[i]);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Create a new Instance of BattlerAI.
     *
     * @param name The name of the BattlerAI
     * @param description
     * @param element The Element of the BattlerAI
     * @param gender The gender of the BattlerAI
     * @param level The level of the BattlerAI
     * @param HP How much damage can this Battler take before dying.
     * @param MP The amount of magic usable before needing to recharge
     * @param Attack The physical attack power of the Battler
     * @param Defense How much damage reduction the Battler has
     * @param Speed Determines whether or not the Battler goes first. Also
     * determines critical hit and dodge.
     * @param Magic Effectiveness of magic attacks and status effects. Reduces
     * magic damage as well.
     * @param battlerClass The class the BattlerAI is.
     * @param equipment The equipment currently equipped.
     * @param AIData The flags and priorities that the AI uses.
     * @param experience The amount of experience points the BattlerAI gives
     * when defeated.
     * @param money
     * @param itemDrop The item dropped when defeated.
     * @param itemQuantity The amount of items dropped when defeated.
     * @param dropRate
     */
    public BattlerAI(String name,
            String description,
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
            EnumMap<Equipment.Slot, Equipment> equipment,
            AI[] AIData,
            int experience,
            int money,
            Item[] itemDrop,
            double[] dropRate,
            int[] itemQuantity) {
        super(name,
                description,
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
        this.money = money;
        this.AIData = AIData;
        this.experience = experience;
        try {
            drop = (Item[]) (itemDrop.clone());
            this.dropPercent = dropRate;
            for (int i = 0; i < drop.length; i++) {
                drop[i].setQuantity(itemQuantity[i]);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Create a new Instance of BattlerAI.
     *
     * @param name The name of the BattlerAI
     * @param description
     * @param element The Element of the BattlerAI
     * @param gender The gender of the BattlerAI
     * @param level The level of the BattlerAI
     * @param HP How much damage can this Battler take before dying.
     * @param MP The amount of magic usable before needing to recharge
     * @param Attack The physical attack power of the Battler
     * @param Defense How much damage reduction the Battler has
     * @param Speed Determines whether or not the Battler goes first. Also
     * determines critical hit and dodge.
     * @param Magic Effectiveness of magic attacks and status effects. Reduces
     * magic damage as well.
     * @param skillList The skills the BattlerAI has access to immediately.
     * @param unarmedStrikeAnimation The animation to use when it's unarmed.
     * @param AIData The flags and priorities that the AI uses.
     * @param experience The amount of experience points the BattlerAI gives
     * when defeated.
     * @param money
     * @param itemDrop The item dropped when defeated.
     * @param itemQuantity The amount of items dropped when defeated.
     * @param dropRate
     */
    public BattlerAI(String name,
            String description,
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
            FieldSkill unarmedStrikeAnimation,
            AI[] AIData,
            int experience,
            int money,
            Item[] itemDrop,
            double[] dropRate,
            int[] itemQuantity) {
        this(name,
                description,
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
                new EnumMap<Equipment.Slot, Equipment>(Equipment.Slot.class),
                AIData,
                experience,
                money,
                itemDrop,
                dropRate,
                itemQuantity);
        this.money = 0;
        Weapon temp = new Weapon("Unarmed",
                "",
                "unarmed",
                "unarmed",
                "unarmed",
                0,
                new String[]{"HAND"},
                Equipment.Slot.OffHand,
                null,
                Battle.Element.Physical,
                false,
                0,
                0,
                0,
                0);
        equip(temp);
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    /**
     * Gets the command determined by the AI and ensures it is usable.
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
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().size()));
                } while (getSkill == null
                        || getSkill.getElement() != Battle.Element.Heal
                        || getSkill.getStatusEffect() instanceof Normal);
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
                } while (getSkill == null
                        || getSkill.getElement() != Battle.Element.Heal
                        || getSkill.getStatusEffect() instanceof Death);

                tempAction = new Action(Battle.Command.Skill,
                        getSkill, this,
                        getSkill.getAlly() ? deadBattler : b.getAlly((int) (Math.random() * b.getAlly().size())),
                        (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
                break;
            case AttackSkill:
                do {
                    getSkill = this.getSkill((int) (Math.random() * this.getSkillList().size()));
                } while (getSkill == null
                        || //                        getSkill.getLevel() > getLevel() ||
                        getSkill.calculateDamage(this, b.getAlly((int) (Math.random() * b.getAlly().size()))) <= 0);
                if (getSkill.getCost() > getMP()) {
                    tempAction = new Action(Battle.Command.Charge, this);
                } else if (getSkill.getAll()) {
                    tempAction = new Action(Battle.Command.Skill,
                            getSkill, this,
                            (ArrayList<Battler>) (getSkill.getAlly() ? b.getEnemy() : b.getAlly()));
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
                } while (getSkill == null
                        || getSkill.getStatusEffect() instanceof Normal);
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

    public AI.Type interpretAI(Battler b) {
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
    public Item[] getDroppedItem() {
        return drop;
    }

    public double[] getDropRate() {
        return dropPercent;
    }

    public int[] getDropNumber() {
        int[] temp = new int[drop.length];
        for (int i = 0; i < temp.length; i++) {
            if (drop[i] != null) {
                temp[i] = drop[i].getCharges();
            }
        }
        return temp;
    }

    public AI[] getAIData() {
        return AIData;
    }

    @Override
    public Object clone() {
        BattlerAI clone = (BattlerAI) super.clone();
        clone.AIData = AIData;
        return clone;
    }

}
