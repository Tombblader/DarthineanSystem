package com.ark.darthsystem;

import com.ark.darthsystem.States.Battle;
import static com.ark.darthsystem.BattleDriver.*;

import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class Battler implements Serializable {

    public static final long serialVersionUID = 553786374;
    private static final double DEFEND = 0.25;
    private String imageName;
    private String name;
    private int HP, maxHP;
    private int MP, maxMP;
    private int attack;
    private int defense;
    private int speed;
    private int magic;
    private Skill[] skillList;
    private Equipment[] equipmentList = new Equipment[5];
    private HashMap<Battle.Stats, Boolean> isAfflicted = new HashMap<>();
    private Battle.Stats afflicted = Battle.Stats.Normal;
    private int turnCount = 0;
    private int level;
    private Battle.Element battlerElement = Battle.Element.Physical;
    private Battler.Gender battlerGender;
    private Action battlerAction;
    private Battle.Command battlerCommand;
    private boolean isDelaying = false;
    private BattlerClass Class;
    private double damageModifier = 1.0;
    private int experiencePoints = 0;
    private double hpTier = HP /
            50.0 /
            level;
    private double mpTier = MP /
            60.0 /
            level;
    private double attackTier = attack /
            8.0 /
            level;
    private double defenseTier = defense /
            8.0 /
            level;
    private double speedTier = speed /
            8.0 /
            level;
    private double magicTier = magic /
            8.0 /
            level;
    private int tnl = 50;

    public static enum Gender {

        Male,
        Female
    }

    public Battler() {
    }

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
        hpTier = HP /
                50.0 /
                level;
        mpTier = MP /
                50.0 /
                level;
        attackTier = attack /
                8.0 /
                level;
        defenseTier = defense /
                8.0 /
                level;
        speedTier = speed /
                8.0 /
                level;
        magicTier = magic /
                8.0 /
                level;
        Class = initialClass;
        try {
            skillList = initialClass.getSkillList().
                    clone();
            equipmentList = initialEquipment.clone();
        } catch (NullPointerException e) {
        }
//    boolean isAfflicted[] = {false};
        battlerGender = initializeGender;
    }

    public Battler(String initialName,
            String initialImageName,
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
        name = initialName;
        imageName = initialImageName;
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
        hpTier = HP /
                50.0 /
                level;
        mpTier = MP /
                50.0 /
                level;
        attackTier = attack /
                8.0 /
                level;
        defenseTier = defense /
                8.0 /
                level;
        speedTier = speed /
                8.0 /
                level;
        magicTier = magic /
                8.0 /
                level;
        Class = initialClass;
        try {
            skillList = initialClass.getSkillList().
                    clone();
            equipmentList = initialEquipment.clone();
        } catch (NullPointerException e) {
        }
//    boolean isAfflicted[] = {false};
        battlerGender = initializeGender;
    }

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
            Skill[] initialSkill,
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
        hpTier = HP /
                50.0 /
                level;
        mpTier = MP /
                50.0 /
                level;
        attackTier = attack /
                8.0 /
                level;
        defenseTier = defense /
                8.0 /
                level;
        speedTier = speed /
                8.0 /
                level;
        magicTier = magic /
                8.0 /
                level;
        try {
            skillList = initialSkill.clone();
            equipmentList = initialEquipment.clone();
        } catch (NullPointerException e) {
//            e.printStackTrace();
        }
//    boolean isAfflicted[] = {false};
        battlerGender = initializeGender;
    }

    public Battler(String initialName,
            String initialImageName,
            Battle.Element initialElement,
            Battler.Gender initializeGender,
            int initialLevel,
            int initialHP,
            int initialMP,
            int initialAttack,
            int initialDefense,
            int initialSpeed,
            int initialMagic,
            Skill[] initialSkill,
            Equipment[] initialEquipment) {
        name = initialName;
        imageName = initialImageName;
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
        hpTier = HP /
                50.0 /
                level;
        mpTier = MP /
                50.0 /
                level;
        attackTier = attack /
                8.0 /
                level;
        defenseTier = defense /
                8.0 /
                level;
        speedTier = speed /
                8.0 /
                level;
        magicTier = magic /
                8.0 /
                level;
        try {
            skillList = initialSkill.clone();
            equipmentList = initialEquipment.clone();
        } catch (NullPointerException e) {
        }
//    boolean isAfflicted[] = {false};
        battlerGender = initializeGender;
    }

    public BattlerClass getBattlerClass() {
        return Class;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getHP() {
        return HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getMP() {
        return MP;
    }

    public int getMaxMP() {
        return maxMP;
    }

    public int getAttack() {
        return attack +
                getEquipmentAttack();
    }

    public int getBaseAttack() {
        return attack;
    }

    public int getDefense() {
        return defense +
                getEquipmentDefense();
    }

    public int getBaseDefense() {
        return defense;
    }

    public int getSpeed() {
        return isDelaying ? 1 : speed +
                getEquipmentSpeed();
    }

    public int getBaseSpeed() {
        return speed;
    }

    public int getMagic() {
        return magic +
                getEquipmentMagic();
    }

    public int getBaseMagic() {
        return magic;
    }

    public Skill getSkill(int skillSlot) {
        return skillList[skillSlot];
    }

    public Skill[] getSkillList() {
        return skillList;
    }

    public Skill[] getCurrentSkillList() {
        ArrayList<Skill> getSkillList = new ArrayList<>();
        for (Skill skillList1 : skillList) {
            if (skillList1 != null && skillList1.getLevel() <= level) {
                getSkillList.add(skillList1);
            }
        }
        return (Skill[]) (getSkillList.toArray());
    }

    public Skill getSkill(String skill) {
        return skillList[Arrays.binarySearch(skillList,
                skill)];
    }

    public Equipment getEquipment(int equipmentSlot) {
        return equipmentList[equipmentSlot];
    }

    public Equipment[] getEquipmentList() {
        return equipmentList;
    }

    public Battler.Gender getGender() {
        return battlerGender;
    }

    public Battle.Element getElement() {
        return battlerElement;
    }

    public Battle.Stats getStatus() {
        return afflicted;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public int levelUp() {
        hpTier = HP /
                50.0 /
                (level +
                1);
        mpTier = MP /
                50.0 /
                (level +
                1);
        attackTier = attack /
                8.8 /
                (level +
                1);
        defenseTier = defense /
                8.8 /
                (level +
                1);
        speedTier = speed /
                8.8 /
                (level +
                1);
        magicTier = magic /
                8.8 /
                (level +
                1);

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
        for (Skill i : skillList) {
            if (level == i.getLevel()) {
                printline(name + " has learned " + i.getName() + "!");
            }
        }
        return level;
    }

    public int levelUp(int level) {
        hpTier = HP /
                50.0 /
                (level +
                1);
        mpTier = MP /
                50.0 /
                (level +
                1);
        attackTier = attack /
                8.8 /
                (level +
                1);
        defenseTier = defense /
                8.8 /
                (level +
                1);
        speedTier = speed /
                8.8 /
                (level +
                1);
        magicTier = magic /
                8.8 /
                (level +
                1);

        for (int i = 0; i <
                level; i++) {

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

    public void changeStatus(Battle.Stats getStats) {
        afflicted = getStats;
        turnCount = 0;
    }

    public void changeStatus(Battle.Stats getStats,
            int initialTurnCount) {
        afflicted = getStats;
        turnCount = initialTurnCount;
    }

    public boolean changeHP(int value) {
        boolean isDead;
        HP -= ((value >
                0) ? (value *
                        damageModifier) : value);
        if (HP >
                maxHP) {
            HP = maxHP;
        }
        isDead = HP <=
                0;
        if (isDead) {
            HP = 0;
            resetDefend();
            isDelaying = false;
            afflicted = Battle.Stats.Death;
        }
        return isDead;
    }

    public boolean isAlive() {
        return HP > 0;
    }

    public boolean isDelaying() {
        return isDelaying;
    }

    public void setDelaying(boolean delay) {
        isDelaying = delay;
    }

    public boolean canMove() {
        isDelaying = false;
        return afflicted != Battle.Stats.Petrify &&
                afflicted != Battle.Stats.Death &&
                afflicted != Battle.Stats.Sleep;
    }

    public void changeMP(int value) {
        MP -= value;
        if (MP > maxMP) {
            MP = maxMP;
        }
    }

    public void fullHeal() {
        HP = maxHP;
        MP = maxMP;
    }

    public void charge() {
        MP = maxMP;
    }

    public void defend() {
        damageModifier = DEFEND;
    }

    public void resetDefend() {
        damageModifier = 1.0;
    }

    public double getDefend() {
        return damageModifier;
    }

    public Equipment equip(Equipment newEquipment) {
        if (!Class.equippable(newEquipment)) {
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

    public void learnSkill(Skill newSkill) {
        Skill[] tempList = new Skill[skillList.length + 1];
        System.arraycopy(skillList,
                0,
                tempList,
                0,
                skillList.length);
        tempList[skillList.length] = newSkill;
        skillList = tempList;
    }

    public void learnSkill(Skill newSkill, int overrideLevel) {
        Skill[] tempList = new Skill[skillList.length + 1];
        System.arraycopy(skillList,
                0,
                tempList,
                0,
                skillList.length);
        tempList[skillList.length] = newSkill.overrideLevel(overrideLevel);
        skillList = tempList;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImage(String newImage) {
        imageName = newImage;
    }

    public void changeExperiencePoints(int getExperiencePoints) {
        experiencePoints += getExperiencePoints;
        while (experiencePoints >= tnl) {
            experiencePoints -= tnl;
            levelUp();
            tnl = (int) (tnl * Math.pow(1.5, (1 / (double) (level))) + level * 5);
        }
    }

    public void rename(String newName) {
        name = newName;
    }

    public Battler clone() {
        return new Battler(name,
                battlerElement,
                battlerGender,
                level,
                HP,
                MP,
                attack,
                defense,
                speed,
                magic,
                skillList,
                equipmentList);
    }
}
