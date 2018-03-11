package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;
import static com.ark.darthsystem.BattleDriver.*;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A battler represents a character in Battle mode.  It holds the character's
 * stats and abilities.
 * @author Keven
 */
public class Battler implements Serializable, Nameable {

    public static final long serialVersionUID = 553786374;
    private static final double DEFEND = 0.25;
    private String name;
    private int HP, maxHP;
    private int MP, maxMP;
    private int attack;
    private int defense;
    private int speed;
    private int magic;
    private ArrayList<Skill> skillList;
    private Equipment[] equipmentList = new Equipment[5];
    private HashMap<Battle.Stats, Boolean> isAfflicted = new HashMap<>();
    private Battle.Stats afflicted = Battle.Stats.Normal;
    private int turnCount = 0;
    private int level;
    private Battle.Element battlerElement = Battle.Element.Physical;
    private Battler.Gender battlerGender;
    private boolean isDelaying = false;
    private BattlerClass battlerClass;
    private double damageModifier = 1.0;
    private int experiencePoints = 0;
    private double hpTier = HP / 50.0 / level;
    private double mpTier = MP / 60.0 / level;
    private double attackTier = attack / 8.0 / level;
    private double defenseTier = defense / 8.0 / level;
    private double speedTier = speed / 8.0 / level;
    private double magicTier = magic / 8.0 / level;
    private int tnl = 50;

    public static enum Gender {
        Male,
        Female
    }

    /**
     * Creates an empty Battler Object with no values initialized.
     */
    public Battler() {
    }

    /**
     *
     * @param initialName
     * @param initialElement
     * @param initializeGender
     * @param initialLevel
     * @param initialHP
     * @param initialMP
     * @param initialAttack
     * @param initialDefense
     * @param initialSpeed
     * @param initialMagic
     * @param initialClass
     * @param initialEquipment
     */
    public Battler(String initialName,
            Battle.Element initialElement,
            Battler.Gender initializeGender,
            int initialLevel,
            int initialHP,
            int initialMP,
            int initialAttack,
            int initialDefense,
            int initialSpeed,
            int initialMagic,
            BattlerClass initialClass,
            Equipment[] initialEquipment) {
        this.name = initialName;
        this.battlerElement = initialElement;
        this.level = initialLevel;
        this.maxHP = initialHP;
        this.maxMP = initialMP;
        this.HP = maxHP;
        this.MP = maxMP;
        this.attack = initialAttack;
        this.defense = initialDefense;
        this.speed = initialSpeed;
        this.magic = initialMagic;
        this.hpTier = HP / 50.0 / level;
        this.mpTier = MP / 50.0 / level;
        this.attackTier = attack / 8.0 / level;
        this.defenseTier = defense / 8.0 / level;
        this.speedTier = speed / 8.0 / level;
        this.magicTier = magic / 8.0 / level;
        this.battlerClass = initialClass;
        try {
            this.skillList = new ArrayList<>();
            for(int i = 1; i <= level; i++) {
                if (this.battlerClass != null && this.battlerClass.getSkillList() != null && this.battlerClass.getSkillList().containsKey(i)) {
                    this.skillList.addAll(Arrays.asList(this.battlerClass.getSkillList().get(i)));
                }
            }
            this.equipmentList = initialEquipment.clone();
        } catch (NullPointerException e) {
        }
//    boolean isAfflicted[] = {false};
        this.battlerGender = initializeGender;
    }

    /**
     *
     * @param initialName
     * @param initialElement
     * @param initializeGender
     * @param initialLevel
     * @param initialHP
     * @param initialMP
     * @param initialAttack
     * @param initialDefense
     * @param initialSpeed
     * @param initialMagic
     * @param initialSkill
     * @param initialEquipment
     */
    public Battler(String initialName,
            Battle.Element initialElement,
            Battler.Gender initializeGender,
            int initialLevel,
            int initialHP,
            int initialMP,
            int initialAttack,
            int initialDefense,
            int initialSpeed,
            int initialMagic,
            ArrayList<Skill> initialSkill,
            Equipment[] initialEquipment) {
        name = initialName;
        battlerElement = initialElement;
        level = initialLevel;
        maxHP = initialHP;
        maxMP = initialMP;
        HP = maxHP;
        MP = maxMP;
        attack = initialAttack;
        defense = initialDefense;
        speed = initialSpeed;
        magic = initialMagic;
        hpTier = HP / 50.0 / level;
        mpTier = MP / 50.0 / level;
        attackTier = attack / 8.0 / level;
        defenseTier = defense / 8.0 / level;
        speedTier = speed / 8.0 / level;
        magicTier = magic / 8.0 / level;
        try {
            skillList = new ArrayList<>();
            skillList.addAll(initialSkill);
            equipmentList = initialEquipment.clone();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
//    boolean isAfflicted[] = {false};
        battlerGender = initializeGender;
    }

    /**
     *
     * @return
     */
    public BattlerClass getBattlerClass() {
        return battlerClass;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return
     */
    public int getLevel() {
        return level;
    }

    /**
     *
     * @return
     */
    public int getHP() {
        return HP;
    }

    /**
     *
     * @return
     */
    public int getMaxHP() {
        return maxHP;
    }

    /**
     *
     * @return
     */
    public int getMP() {
        return MP;
    }

    /**
     *
     * @return
     */
    public int getMaxMP() {
        return maxMP;
    }

    /**
     *
     * @return
     */
    public int getAttack() {
        return attack + getEquipmentAttack();
    }

    /**
     *
     * @return
     */
    public int getBaseAttack() {
        return attack;
    }

    /**
     *
     * @return
     */
    public int getDefense() {
        return defense + getEquipmentDefense();
    }

    /**
     *
     * @return
     */
    public int getBaseDefense() {
        return defense;
    }

    /**
     *
     * @return
     */
    public int getSpeed() {
        return isDelaying ? 1 : speed + getEquipmentSpeed();
    }

    /**
     *
     * @return
     */
    public int getBaseSpeed() {
        return speed;
    }

    /**
     *
     * @return
     */
    public int getMagic() {
        return magic + getEquipmentMagic();
    }

    /**
     *
     * @return
     */
    public int getBaseMagic() {
        return magic;
    }

    /**
     *
     * @param skillSlot
     * @return
     */
    public Skill getSkill(int skillSlot) {
        return skillList.get(skillSlot);
    }

    /**
     *
     * @return
     */
    public ArrayList<Skill> getSkillList() {
        return skillList;
    }

//    /**
//     *
//     * @return
//     */
//    public Skill[] getCurrentSkillList() {
//        return (Skill[]) (skillList.toArray());
//    }

    /**
     *
     * @param skill
     * @return
     */
    public Skill getSkill(String skill) {
        try {
            return skillList.get(skillList.indexOf(skill));
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param equipmentSlot
     * @return
     */
    public Equipment getEquipment(int equipmentSlot) {
        return equipmentList[equipmentSlot];
    }

    /**
     *
     * @return
     */
    public Equipment[] getEquipmentList() {
        return equipmentList;
    }

    /**
     *
     * @return
     */
    public Battler.Gender getGender() {
        return battlerGender;
    }

    /**
     *
     * @return
     */
    public Battle.Element getElement() {
        return battlerElement;
    }

    /**
     *
     * @return
     */
    public Battle.Stats getStatus() {
        return afflicted;
    }

    public boolean getStatus(Battle.Stats statusCheck) {
        return isAfflicted.get(statusCheck);
    }
    
    /**
     *
     * @return
     */
    public HashMap<Battle.Stats, Boolean> getAllStatus() {
        return isAfflicted;
    }

    /**
     *
     * @return
     */
    public int getTurnCount() {
        return turnCount;
    }

    /**
     *
     * @return
     */
    public int levelUp() {
        hpTier = HP / 50.0 / (level + 1);
        mpTier = MP / 50.0 / (level + 1);
        attackTier = attack / 8.8 / (level + 1);
        defenseTier = defense / 8.8 / (level + 1);
        speedTier = speed / 8.8 / (level + 1);
        magicTier = magic / 8.8 / (level + 1);

        maxHP += (int) (0.7 *
                (1 +
                hpTier *
                2.71 *
                level) +
                Math.random() *
                5 +
                2);
        maxMP += (int) (0.7 *
                (1 +
                mpTier *
                2.71 *
                level) +
                Math.random() *
                5);
        attack += (int) (0.7 *
                (1 +
                attackTier *
                2.71 *
                level) +
                Math.random() *
                level /
                10);
        defense += (int) (0.7 *
                (1 +
                defenseTier *
                2.71 *
                level) +
                Math.random() *
                level /
                10);
        speed += (int) (0.7 *
                (1 +
                speedTier *
                2.71 *
                level) +
                Math.random() *
                level /
                10);
        magic += (int) (0.7 *
                (1 +
                magicTier *
                2.71 *
                level) +
                Math.random() *
                level /
                10);
        level++;
        printline(name + " gained a level!");
        printline(name + " is now level " + level + ".");
        if (battlerClass.getSkillList().get(level) != null) {
            for(Skill s : battlerClass.getSkillList().get(level)) {
                skillList.add(s);
                printline(name + " has learned " + s.getName() + "!");
            }
        }
            
        
        return level;
    }

    /**
     *
     * @param level
     * @return
     */
    public int levelUp(int level) {
        hpTier = HP / 50.0 / (level + 1);
        mpTier = MP / 50.0 / (level + 1);
        attackTier = attack / 8.8 / (level + 1);
        defenseTier = defense / 8.8 / (level + 1);
        speedTier = speed / 8.8 / (level + 1);
        magicTier = magic / 8.8 / (level + 1);

        for (int i = 0; i < level; i++) {

            maxHP += (int) (0.7 *
                    (1 +
                    hpTier *
                    2.71 *
                    level) +
                    Math.random() *
                    5 +
                    2);
            maxMP += (int) (0.7 *
                    (1 +
                    mpTier *
                    2.71 *
                    level) +
                    Math.random() *
                    5);
            attack += (int) (0.7 *
                    (1 +
                    attackTier *
                    2.71 *
                    level) +
                    Math.random() *
                    level /
                    10);
            defense += (int) (0.7 *
                    (1 +
                    defenseTier *
                    2.71 *
                    level) +
                    Math.random() *
                    level /
                    10);
            speed += (int) (0.7 *
                    (1 +
                    speedTier *
                    2.71 *
                    level) +
                    Math.random() *
                    level /
                    10);
            magic += (int) (0.7 *
                    (1 +
                    magicTier *
                    2.71 *
                    level) +
                    Math.random() *
                    level /
                    10);
            this.level++;
        }
        return level;
    }

    /**
     *
     * @param upHP
     * @param upMP
     * @param upAttack
     * @param upDefense
     * @param upSpeed
     * @param upMagic
     * @return
     */
    public int levelUp(int upHP,
            int upMP,
            int upAttack,
            int upDefense,
            int upSpeed,
            int upMagic) {
        level++;
        maxHP += upHP;
        maxMP += upMP;
        attack += upAttack;
        defense += upDefense;
        speed += upSpeed;
        magic += upMagic;
        return level;
    }

    /**
     *
     * @param getStats
     */
    public void changeStatus(Battle.Stats getStats) {
        afflicted = getStats;
        turnCount = 0;
    }

    /**
     *
     * @param getStats
     * @param initialTurnCount
     */
    public void changeStatus(Battle.Stats getStats,
            int initialTurnCount) {
        afflicted = getStats;
        turnCount = initialTurnCount;
    }

    /**
     *
     * @param value
     * @return
     */
    public boolean changeHP(int value) {
        boolean isDead;
        HP -= ((value > 0) ? (value * damageModifier) : value);
        if (HP > maxHP) {
            HP = maxHP;
        }
        isDead = HP <= 0;
        if (isDead) {
            HP = 0;
            resetDefend();
            isDelaying = false;
            afflicted = Battle.Stats.Death;
        }
        return isDead;
    }

    /**
     *
     * @return
     */
    public boolean isAlive() {
        return HP > 0;
    }

    /**
     *
     * @return
     */
    public boolean isDelaying() {
        return isDelaying;
    }

    /**
     *
     * @param delay
     */
    public void setDelaying(boolean delay) {
        isDelaying = delay;
    }

    /**
     *
     * @return
     */
    public boolean canMove() {
        isDelaying = false;
        return afflicted != Battle.Stats.Petrify &&
                afflicted != Battle.Stats.Death &&
                afflicted != Battle.Stats.Sleep;
    }

    /**
     *
     * @param value
     */
    public void changeMP(int value) {
        MP -= value;
        if (MP > maxMP) {
            MP = maxMP;
        }
    }

    /**
     *
     */
    public void fullHeal() {
        HP = maxHP;
        MP = maxMP;
    }

    /**
     *
     */
    public void charge() {
        MP = maxMP;
    }

    /**
     *
     */
    public void defend() {
        damageModifier = DEFEND;
    }
    
    /**
     *
     */
    public void resetDefend() {
        damageModifier = 1.0;
    }

    /**
     *
     * @return
     */
    public double getDefend() {
        return damageModifier;
    }

    /**
     *
     * @param newEquipment
     * @return
     */
    public Equipment equip(Equipment newEquipment) {
        if (!battlerClass.equippable(newEquipment)) {
            return newEquipment;
        }
        Equipment temp;
        try {
            temp = equipmentList[newEquipment.getEquipmentType().getSlot()];
        } catch (Exception e) {
            temp = null;
        } finally {
            equipmentList[newEquipment.getEquipmentType().getSlot()] = newEquipment;
        }
        return temp;
    }

    private int getEquipmentAttack() {
        int equipmentAttack = 0;
        for (Equipment equipmentList1 : equipmentList) {
            if (equipmentList1 != null) {
                equipmentAttack += equipmentList1.getAttack();
            }
        }
        return equipmentAttack;
    }

    private int getEquipmentDefense() {
        int equipmentDefense = 0;
        for (Equipment equipmentList1 : equipmentList) {
            if (equipmentList1 != null) {
                equipmentDefense += equipmentList1.getDefense();
            }
        }
        return equipmentDefense;
    }

    private int getEquipmentSpeed() {
        int equipmentSpeed = 0;
        for (Equipment equipmentList1 : equipmentList) {
            if (equipmentList1 != null) {
                equipmentSpeed += equipmentList1.getSpeed();
            }
        }
        return equipmentSpeed;
    }

    private int getEquipmentMagic() {
        int equipmentMagic = 0;
        for (Equipment equipmentList1 : equipmentList) {
            if (equipmentList1 != null) {
                equipmentMagic += equipmentList1.getMagic();
            }
        }
        return equipmentMagic;
    }

    /**
     *
     * @param newSkill
     */
    public void learnSkill(Skill newSkill) {
        skillList.add(newSkill);
    }


    /**
     *
     * @param getExperiencePoints
     */
    public void changeExperiencePoints(int getExperiencePoints) {
        experiencePoints += getExperiencePoints;
        while (experiencePoints >= tnl) {
            experiencePoints -= tnl;
            levelUp();
            tnl = (int) (tnl * Math.pow(1.5, (1 / (double) (level))) + level * 5);
        }
    }

    /**
     *
     * @param newName
     */
    public void rename(String newName) {
        name = newName;
    }

    public Battler clone() {
        Battler b = new Battler(name,
                battlerElement,
                battlerGender,
                level,
                HP,
                MP,
                attack,
                defense,
                speed,
                magic,
                battlerClass,
                equipmentList);
        b.skillList = skillList;
        return b;
    }

    public void reset() {
        fullHeal();
    }    
}
