package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;
import static com.ark.darthsystem.BattleDriver.*;
import com.ark.darthsystem.statusEffects.*;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;

/**
 * A battler represents a character in Battle mode.  It holds the character's
 * stats and abilities.
 * @author Keven
 */
public class Battler implements Serializable, Nameable, Cloneable {

    public static final long serialVersionUID = 553786374;
    private static final double DEFEND = 0.25;
    private String name;
    private String description;
    private int HP, maxHP;
    private int MP, maxMP;
    private int attack;
    private int defense;
    private int speed;
    private int magic;
    private ArrayList<Skill> skillList;
    private Equipment[] equipmentList = new Equipment[5];
//    private HashMap<StatusEffect, Boolean> isAfflicted;
//    private Battle.Stats afflicted = Battle.Stats.Normal;
    private ArrayList<StatusEffect> isAfflicted;
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
    private int tnl = 1000;

    @Override
    public String getDescription() {
        return description;
    }

    public static enum Gender {
        Male,
        Female,
        None,
        Random
    }

    /**
     * Creates an empty Battler Object with no values initialized.
     */
    public Battler() {
        this.isAfflicted = new ArrayList<>();
        isAfflicted.add(new Normal());
    }

    /**
     *
     * @param initialName
     * @param initialDescription
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
            String initialDescription,
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
        this.isAfflicted = new ArrayList<>();
        this.isAfflicted.add(new Normal());
        this.name = initialName;
        this.description = initialDescription;
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
            e.printStackTrace();
        }
//    boolean isAfflicted[] = {false};
        this.battlerGender = initializeGender;
    }

   public Battler(String initialName,
            String initialDescription,
            BattlerClass initialClass,
            Battle.Element initialElement,
            Battler.Gender initialGender,
            int initialLevel,
            int initialHP,
            int initialMP,
            int initialAttack,
            int initialDefense,
            int initialSpeed,
            int initialMagic,
            int initialHPTier,
            int initialMPTier,
            int initialAttackTier,
            int initialDefenseTier,
            int initialSpeedTier,
            int initialMagicTier,
            Equipment[] initialEquipment) {
        this(initialName,
                initialDescription,
                initialElement,
                initialGender, 
                initialLevel, 
                initialHP,
                initialMP,
                initialAttack, 
                initialDefense, 
                initialSpeed, 
                initialMagic, 
                initialClass, 
                initialEquipment);
        this.hpTier = initialHPTier;
        this.mpTier = initialMPTier;
        this.attackTier = initialAttackTier;
        this.defenseTier = initialDefenseTier;
        this.speedTier = initialSpeedTier;
        this.magicTier = initialMagicTier;

    }
    
    /**
     *
     * @param initialName
     * @param getDescription
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
            String getDescription,
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
         this.isAfflicted = new ArrayList<>();
        this.isAfflicted.add(new Normal());
       name = initialName;
       description = getDescription;
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
        return (int) ((attack + getEquipmentAttack()) * getStatusAttack());
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
        return (int) ((defense + getEquipmentDefense()) * getStatusDefense());
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
        return (int) (isDelaying ? 1 : (speed + getEquipmentSpeed()) * getStatusSpeed());
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
        return (int) ((magic + getEquipmentMagic()) * getStatusMagic());
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

//    /**
//     *
//     * @return
//     */
//    public Battle.Stats getStatus() {
//        return afflicted;
//    }

    public StatusEffect getStatus(int index) {
        return isAfflicted.get(index);
    }

    public boolean getStatus(StatusEffect effect) {
        return isAfflicted.contains(effect);
    }

    public boolean getStatus(String effect) {
        boolean afflicted = false;
        for (StatusEffect se : isAfflicted) {
            afflicted |= se.getName().equalsIgnoreCase(effect);
        }
        return afflicted;
    }
    
    
    /**
     *
     * @return
     */
    public ArrayList<StatusEffect> getAllStatus() {
        return isAfflicted;
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
    public void changeStatus(StatusEffect getStats) {
        if (isAfflicted.contains(getStats)) {
            isAfflicted.get(isAfflicted.indexOf(getStats)).reset();
        } else if (!isAfflicted.contains(getStats)) {
            isAfflicted.add(getStats);
        }
    }

    /**
     *
     * @param value
     * @return
     */
    public boolean changeHP(int value) {
        boolean isDead;
        HP -= ((value > 0) ? (value * damageModifier * getStatusDamageModifier()) : value);
        if (HP > maxHP) {
            HP = maxHP;
        }
        isDead = HP <= 0;
        if (isDead) {
            HP = 0;
            resetDefend();
            isDelaying = false;
            isAfflicted.clear();
            isAfflicted.add(new Death());
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
        boolean canMove = true;
        canMove = isAfflicted.stream().map((status) -> status.canMove()).reduce(canMove, (accumulator, _item) -> accumulator & _item);
        return canMove;
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
        damageModifier = 1f;
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
    public final Equipment equip(Equipment newEquipment) {
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
    
    private float getStatusAttack() {
        float statusAttack = 1f;
        for (StatusEffect status : isAfflicted) {
            if (status != null) {
                statusAttack += status.getAttack();
            }
        }
        return statusAttack;
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
    
    private float getStatusDefense() {
        float statusDefense = 1f;
        for (StatusEffect status : isAfflicted) {
            if (status != null) {
                statusDefense += status.getDefense();
            }
        }
        return statusDefense;
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
    
    private float getStatusSpeed() {
        float statusSpeed = 1f;
        for (StatusEffect status : isAfflicted) {
            if (status != null) {
                statusSpeed += status.getSpeed();
            }
        }
        return statusSpeed;
    }

    private float getStatusMagic() {
        float statusMagic = 1f;
        for (StatusEffect status : isAfflicted) {
            if (status != null) {
                statusMagic += status.getMagic();
            }
        }
        return statusMagic;
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
    
    public float getStatusDamageModifier() {
        float statusDamage = 1;
        for (StatusEffect status : isAfflicted) {
            if (status != null) {
                statusDamage += status.getDamageModifier();
            }
        }
        return statusDamage;        
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

    @Override
    public Object clone() {
        try {
            Battler temp = (Battler) super.clone();
            ArrayList<StatusEffect> tempEffect = new ArrayList<>();
            for (StatusEffect e : isAfflicted) {
                tempEffect.add((StatusEffect) e.clone());
            }
            temp.skillList = (ArrayList<Skill>) skillList.clone();
            temp.equipmentList = Arrays.copyOf(equipmentList, equipmentList.length);
            temp.isAfflicted = tempEffect;
            return temp;
        }
        catch (CloneNotSupportedException e) {

        }
        return null;
    }

    public void reset() {
        fullHeal();
    }    
}
