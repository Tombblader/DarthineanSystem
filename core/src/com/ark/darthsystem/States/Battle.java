package com.ark.darthsystem.States;

import com.ark.darthsystem.*;
import com.ark.darthsystem.Database.Database2;
import com.ark.darthsystem.Graphics.Actor;
import com.ark.darthsystem.Graphics.ActorBattler;
import com.ark.darthsystem.Graphics.ActorSkill;
import com.ark.darthsystem.Graphics.GameTimer;
import com.ark.darthsystem.Graphics.GraphicsDriver;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;
//import java.util.conArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Iterator;

public class Battle implements State {

    private static final String BATTLE_MUSIC = "music/Forbidden Secret Unfolding.mp3";
    private static ArrayList<Battler> party;
    private static ArrayList<Battler> enemy;
    private static ArrayList<ActorBattler> partyActors;
    private static ArrayList<ActorBattler> enemyActors;
    private static ArrayList<Item> partyItems;
    private static boolean inBattle = false;
    private static ArrayList<Action> partyAction = new ArrayList<>();
    private static ArrayList<Action> enemyAction = new ArrayList<>();
    private static ArrayList<Action> allAction = new ArrayList<>();
    private ArrayList<Actor> animations = new ArrayList<>();
    
    private int turnCount = 1;
    private Scenario script;
    private static State state;
    private Sprite background;
    private float elapsed = 0;
    private ArrayList<GameTimer> timers;
    private String bgm;
    private ArrayList<Sound> sounds = new ArrayList<>();

    
    private void renderPersistentActors(SpriteBatch batch) {
        final float BATTLER_Y = 400;
        int divider = enemyActors.isEmpty() ? 512 / enemyActors.size() : 0;
        for (int i = 0; i < enemyActors.size(); i++) {
            if (enemy.get(i).isAlive()) {
                Sprite s = (Sprite) (enemyActors.get(i).getSprite().getCurrentBattlerAnimation().getKeyFrame(elapsed));
                batch.draw(s, 
                    (float) ((WIDTH + divider * (i - 1)) / 2 + divider * i) - s.getOriginX(),
                    BATTLER_Y - s.getOriginY(),
                    s.getOriginX(),
                    s.getOriginY(),
                    s.getWidth(),
                    s.getHeight(),
                    s.getScaleX(),
                    s.getScaleY(),
                    s.getRotation());
            }
        }
    }
    
    private void renderTemporaryActors(SpriteBatch batch) {
        for (int i = 0; i < animations.size(); i++) {
            animations.get(i).render(batch);
        }
        
    }

    public Menu menuCommand(final Battle b, final Battler battler) {
        Menu command = new Menu(battler.getName() + ", enter a command",
                new String[]{"Attack", "Defend", "Skill", "Charge", "Analyze", "Delay", "Item", "Run"},
                true,
                true) {
            @Override
            public Object confirm(String choice) {
                Command tempAction = Enum.valueOf(Battle.Command.class, choice);
                tempAction.run(b, battler);
                return null;
            }

            public String cancel() {
                if (!partyAction.isEmpty()) {
                    partyAction.remove(partyAction.size() - 1);
                    State.COMMAND.setTicks(State.COMMAND.getTicks() - 2);
                } else {
                    State.COMMAND.setTicks(0);
                }
                super.cancel();
                return null;
            }
        };
        return command;
    }

    private void updatePersistentActors(float delta) {

    }
    
    private void updateTemporaryActors(float delta) {
        elapsed += GraphicsDriver.getRawDelta();
        for (Iterator<Actor> it = animations.iterator(); it.hasNext();) {
            Actor a = it.next();
            a.update(delta);
            if (a.isFinished()) {
                a.resetAnimation();
                it.remove();
            } 
        }
        playSounds();
    }
    
    private void playSounds() {
        for (Iterator<Sound> it = sounds.iterator(); it.hasNext();) {
            Sound s = it.next();
            s.stop();
            s.play();
            it.remove();
        }
    }

    public void addSounds(Sound s) {
        sounds.add(s);
    }
    
    public void exitBattle() {
        state = State.END;
        allAction.clear();
        enemyAction.clear();
        partyAction.clear();
        GraphicsDriver.removeState(this);
    }

    private void addAnimationAndSound(Action currentAction) {
        final float BATTLER_Y = 400;
        int divider = enemyActors.isEmpty() ? 512 / enemyActors.size() : 0;
        Battler tempBattler = currentAction.getTarget();
        
        if (currentAction.getCommand() == Command.Skill && 
                (currentAction.getTarget() instanceof BattlerAI) &&
                currentAction.getCaster().getMP() >= currentAction.getSkill().getCost()) {
            ActorSkill tempSkill = Database2.SkillToActor(currentAction.getSkill());
            sounds.add(tempSkill.getBattlerSound());
            animations.add(tempSkill.getBattlerAnimation());
            animations.get(animations.size() - 1).setX((float) ((WIDTH + divider * (enemy.indexOf(tempBattler) - 1)) / 2 + divider * enemy.indexOf(tempBattler)));
            animations.get(animations.size() - 1).setY(BATTLER_Y);
        }
        if (currentAction.getCommand() == Command.Skill && 
                (currentAction.getSkill().getAll()) &&
                (currentAction.getAllTargets().get(0) instanceof BattlerAI) &&
                currentAction.getCaster().getMP() >= currentAction.getSkill().getCost()) {
            ActorSkill tempSkill = Database2.SkillToActor(currentAction.getSkill());
            sounds.add(tempSkill.getBattlerSound());
            animations.add(tempSkill.getBattlerAnimation());
            animations.get(animations.size() - 1).setX(GraphicsDriver.getWidth() / 2f);
            animations.get(animations.size() - 1).setY(GraphicsDriver.getHeight() / 2f);
        }
        if (currentAction.getCommand() == Command.Attack && 
                currentAction.getTarget() instanceof BattlerAI) {
            ActorSkill tempSkill = (currentAction.getCaster().getEquipment(Equipment.EquipmentType.LeftArm.getSlot()).getAnimation());
            sounds.add(tempSkill.getBattlerSound());
            animations.add(tempSkill.getBattlerAnimation());
            animations.get(animations.size() - 1).setX((float) ((WIDTH + divider * (enemy.indexOf(tempBattler) - 1)) / 2 + divider * enemy.indexOf(tempBattler)));
            animations.get(animations.size() - 1).setY(BATTLER_Y);
        }
    }

    @Override
    public String getMusic() {
        return bgm;
    }

    public enum Stats {

        Normal(1, 1.0, 0.0, 0, "'s wounds are healed!"),
        Death(8, .10, 0.0, 0, " has instantly died!"),
        Poison(3, .25, 0.1, 7, " has been poisoned!"),
        Sleep(4, .25, 0.1, 8, 0.5, " has fallen asleep!"),
        Silence(5, .25, 0.16, 6, "'s skills have been sealed!"),
        Fog(2, .5, .15, 7, 0.2, " is covered in a hallucinating fog!"),
        Confuse(6, .25, 0.15, 5, 0.25, " has become confused!"),
        Petrify(7, .10, 0.0, 0, " is now petrified!");
        private int priority;
        private int turnCount;
        private double success;
        private double fade;
        private double attackFade;
        private String message;

        private Stats(int setPriority,
                double setSuccess,
                double setFade,
                int setTurnCount,
                double setAttackFade,
                String getMessage) {
            priority = setPriority;
            success = setSuccess;
            fade = setFade;
            turnCount = setTurnCount;
            attackFade = setAttackFade;
            message = getMessage;
        }

        private Stats(int setPriority,
                double setSuccess,
                double setFade,
                int setTurnCount,
                String getMessage) {
            priority = setPriority;
            success = setSuccess;
            fade = setFade;
            turnCount = setTurnCount;
            attackFade = 0.0;
            message = getMessage;
        }

        public int getPriority() {
            return priority;
        }

        public int getTurnCount() {
            return turnCount;
        }

        public boolean isSuccessful(Battler getCaster, Battler getTarget) {
            return (Math.random() <= success
                    - ((getCaster.getLevel()
                    - getTarget.getLevel())
                    / (getCaster.getLevel()
                    + getTarget.getLevel())
                    + (getCaster.getMagic()
                    - getTarget.getMagic())
                    / (getCaster.getMagic()
                    + getTarget.getMagic())));
        }

        public boolean faded(Battler getCaster) {
            return (this != Battle.Stats.Normal
                    && turnCount != 0
                    && (turnCount >= getTurnCount() - getCaster.getTurnCount()
                    || (Math.random() <= fade 
                    //                    - (1.0 / (101.1 - getCaster.getLevel())
                    //                    / (getCaster.getDefense() - getCaster.getLevel())
                    //                    / (getCaster.getMagic() - getCaster.getLevel()))
                    )));
        }

        public boolean attackFaded() {
            return ((this != Battle.Stats.Normal)
                    && attackFade != 0.0
                    && Math.random() < attackFade);
        }

        public String getMessage() {
            return message;
        }

    }

    public static enum Element {

        Physical,
        Heal,
        Male,
        Female,
        Fire,
        Water,
        Wind,
        Earth,
        Light,
        Dark;

        private Element() {
        }

        public Element getWeakness() {
            Element weakness = null;
            switch (this) {
                case Male:
                    weakness = Battle.Element.Female;
                    break;
                case Female:
                    weakness = Battle.Element.Male;
                    break;
                case Fire:
                    weakness = Battle.Element.Water;
                    break;
                case Water:
                    weakness = Battle.Element.Fire;
                    break;
                case Wind:
                    weakness = Battle.Element.Earth;
                    break;
                case Earth:
                    weakness = Battle.Element.Wind;
                    break;
                case Light:
                    weakness = Battle.Element.Dark;
                    break;
                case Dark:
                    weakness = Battle.Element.Light;
                    break;
            }
            return weakness;
        }
    }

    public static enum Command {

        Attack,
        Defend,
        Skill,
        Charge,
        Analyze,
        Delay,
        Item,
        Run;

        public void run(final Battle b, final Battler caster) {
            caster.setDelaying(false);
            Skill getSkill;
            switch (this) {
                case Attack:
                    String[] temp = new String[enemy.size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = enemy.get(i).getName();
                    }

                    Menu targetMenu = new Menu("Target?", temp, true, true) {
                        @Override
                        public Object confirm(String choice) {
                            partyAction.add(new Action(Command.Attack, caster, (enemy.get(this.getCursorIndex())), enemy));
                            return choice;
                        }
                    };
                    GraphicsDriver.addMenu(targetMenu);
                    break;
                case Defend:
                    caster.defend();
                    partyAction.add(new Action(this, caster));
                    break;
                case Skill:
                    final ArrayList<Skill> getSkillList = new ArrayList<>();
                    if (caster.getSkillList() != null) {
                        for (Skill skillList1 : caster.getSkillList()) {
                            if (skillList1 != null && skillList1.getLevel() <= caster.getLevel()) {
                                getSkillList.add(skillList1);
                            }
                        }
                        try {
                            temp = new String[getSkillList.size()];
                            for (int i = 0; i < temp.length; i++) {
                                temp[i] = getSkillList.get(i).getName();
                            }
                            Menu skillMenu = new Menu("Activate which skill?",
                                    temp,
                                    true,
                                    true) {
                                @Override
                                public Object confirm(String choice) {
                                    final Skill getSkill = getSkillList.get(getCursorIndex());
                                    final ArrayList<Battler> targetList = (ArrayList<Battler>) (getSkill. getAlly() ? b.getAlly() : b. getEnemy());
                                    if (getSkill.getCost() <= caster.getMP()) {
                                        if (!getSkill.getAll()) {
                                            String[] temp = new String[targetList.size()];
                                            for (int i = 0; i < temp.length; i++) {
                                                temp[i] = targetList.get(i).getName();
                                            }

                                            Menu targetMenu = new Menu("Target?", temp, true, true) {
                                                @Override
                                                public Object confirm(String choice) {
                                                    partyAction.add(new Action(Command.Skill,
                                                            getSkill,
                                                            caster,
                                                            (targetList.get(this.getCursorIndex())),
                                                            targetList));
                                                    return choice;
                                                }
                                            };
                                            GraphicsDriver.addMenu(targetMenu);
                                        } else {
                                            partyAction.add(new Action(Command.Skill,
                                                    getSkill,
                                                    caster,
                                                    targetList));
                                        }
                                    } else {
                                        cancel();
                                    }
                                    return null;
                                }
                            };
                            GraphicsDriver.addMenu(skillMenu);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                    break;
                case Item:
                    ArrayList<Item> getItemList = new ArrayList<>();
                    if (b.getItem()
                            != null) {
                        Menu menuItem = new Menu("Use which Item?",
                                b.getItemNames(),
                                true,
                                true) {
                            @Override
                            public Object confirm(String choice) {
                                final Item useItem;
                                useItem = b.getItem(this.getCursorIndex());
                                final ArrayList<Battler> targetList = (ArrayList<Battler>) (useItem.getAlly() ? b.getAlly() : b.getEnemy());
                                if (!useItem.getAll()) {
                                    String[] temp = new String[targetList.size()];
                                    for (int i = 0; i < temp.length; i++) {
                                        temp[i] = targetList.get(i).getName();
                                    }
                                    Menu menuTarget = new Menu("Target?",
                                            temp,
                                            true,
                                            true) {
                                        @Override
                                        public Object confirm(String choice) {
                                            partyAction.add(useItem.use(caster,
                                                    targetList.get(getCursorIndex()),
                                                    targetList));
                                            return choice;
                                        }
                                    };
                                    GraphicsDriver.addMenu(menuTarget);
                                } else {
                                    partyAction.add(useItem.use(caster,
                                            targetList));
                                }
                                return choice;
                            }
                        };
                        GraphicsDriver.addMenu(menuItem);
                    } else {

                    }
                    break;
                case Charge:
                case Analyze:
                    partyAction.add(new Action(this, caster));
                    break;
                case Delay:
                    caster.setDelaying(true);
                case Run:
                    partyAction.add(new Action(this, caster));
                    break;
            }
        }
    }

    public enum State {

        START,
        COMMAND,
        ENEMY_COMMAND,
        ACTION,
        END;

        State() {

        }
        private int maxTicks = 0;
        private int ticks = 0;

        public int getMaxTicks() {
            return maxTicks;
        }

        public int getTicks() {
            return ticks;
        }

        public boolean update() {
            ticks++;
            if (ticks
                    > maxTicks) {
                ticks = 0;
                return true;
            }
            return false;
        }

        public void setMaxTicks(int size) {
            maxTicks = size;
        }

        private void setTicks(int i) {
            ticks = i;
        }
    }

    public Battle(ArrayList<ActorBattler> initializeParty,
            ArrayList<ActorBattler> initializeEnemy,
            ArrayList<Item> initializeItems,
            Scenario initializeScenario) {
        partyActors = initializeParty;
        party = new ArrayList<>();
        for (ActorBattler allies : initializeParty) {
            party.add(allies.getBattler());
        }
        enemyActors = initializeEnemy;
        enemy = new ArrayList<>();
        for (ActorBattler enemies : initializeEnemy) {
            enemy.add((enemies.getBattler()));
        }
        partyItems = initializeItems;
        script = initializeScenario;
        turnCount = 1;
        bgm = BATTLE_MUSIC;
    }

    public Battle(ArrayList<ActorBattler> initializeParty,
            ArrayList<ActorBattler> initializeEnemy,
            ArrayList<Item> initializeItems,
            Scenario initializeScenario,
            String initializeMusic) {
        bgm = initializeMusic;
        partyActors = initializeParty;
        party = new ArrayList<>();
        for (ActorBattler allies : initializeParty) {
            party.add(allies.getBattler());
        }
        enemyActors = initializeEnemy;
        enemy = new ArrayList<>();
        for (ActorBattler enemies : initializeEnemy) {
            enemy.add((enemies.getBattler()));
        }
        partyItems = initializeItems;
        script = initializeScenario;
        turnCount = 1;
    }

    public Battle start() {
//        previousMusic = BattleDriver.getCurrentMusic();
//        GraphicsDriver.playMusic(BATTLE_MUSIC);
        
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
        background = new Sprite(new Texture(Gdx.files.internal("backgrounds/title.png")));
        background.flip(false, true);
        updatePersistentActors(0);
        state = State.START;
        return this;
    }

    public void enemyCommand() {
        for (int i = 0; i < enemy.size(); i++) {
            enemy.get(i).resetDefend();
            enemyAction.add(enemy.get(i).canMove() ? ((BattlerAI) (enemy.get(i))).getCommand(this) : null);
        }
    }

    public void reorder() {
        allAction.addAll(partyAction);
        allAction.addAll(enemyAction);
        for (int i = 0; i < allAction.size(); i++) {
            for (int j = 0; j < allAction.size(); j++) {
                Action tempAction;
                if (!(allAction.get(i) == null ||  allAction.get(j) == null)) {
                    if (allAction.get(i).getCaster().getSpeed() > allAction.get(j).getCaster().getSpeed() ||
                            (allAction.get(j).getCaster().getSpeed() == allAction.get(j).getCaster().getSpeed() && 
                            Math.random() < .5)) {
                        tempAction = allAction.get(i);
                        allAction.set(i, allAction.get(j));
                        allAction.set(j, tempAction);
                    }
                }
            }
        }
    }

    public void actionCalculator() {
        for (Action allAction1 : allAction) {
            if (!(allAction1 == null) && allAction1.getCaster().isAlive()) {
                allAction1.calculateDamage(this);
                if (hasWon() || hasLost()) {
                    break;
                }
            }
        }
    }

    public void statusCheck() {
        for (Battler party1 : party) {
            if (party1.getStatus().faded(party1)) {
                BattleDriver.printline(party1.getName() + "'s status effect faded away.");
                party1.changeStatus(Battle.Stats.Normal, 0);
            }
        }
        for (Battler enemy1 : enemy) {
            if (enemy1.getStatus().faded(enemy1)) {
                BattleDriver.printline(enemy1.getName() + "'s status effect faded away.");
                enemy1.changeStatus(Battle.Stats.Normal, 0);
            }
        }

    }

    public boolean hasWon() {
        boolean allDead = true;
        for (Battler enemy1 : enemy) {
            allDead = (allDead && !enemy1.isAlive());
        }
        return allDead;
    }

    public boolean hasLost() {
        boolean allDead = true;
        for (Battler party1 : party) {
            allDead = (allDead && !party1.isAlive());
        }
        return allDead;
    }

    public ArrayList<Item> getItem() {
        return partyItems;
    }

    public String[] getItemNames() {
        String[] temp = new String[partyItems.size()];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = partyItems.get(i).getName();
        }
        return temp;
    }

    public Item getItem(int index) {
        return partyItems.get(index);
    }

    public ArrayList<Battler> getAlly() {
        return party;
    }

    public static boolean inBattle() {
        return inBattle;
    }

    public ArrayList<Action> getActions() {
        return allAction;
    }

    public Battler getAlly(int index) {
        return party.get(index);
    }

    public ArrayList<Battler> getEnemy() {
        return enemy;
    }

    public Battler getEnemy(int index) {
        return enemy.get(index);
    }

    public Battler getRandomAliveEnemy() {
        Battler temp;
        do {
            temp = enemy.get((int) (Math.random() * enemy.size()));
        } while (!temp.isAlive());
        return temp;
    }

    public Battler getRandomAliveAlly() {
        Battler temp;
        do {
            temp = party.get((int) (Math.random() * party.size()));
        } while (!temp.isAlive());
        return temp;
    }

    public int getTurnCount() {
        return turnCount;
    }

    public int getTotalExperience() {
        int total = 0;
        for (Battler enemy1 : enemy) {
            total += ((BattlerAI) (enemy1)).getExperienceValue();
        }
        return total;
    }

    public ArrayList<Item> getAllItems() {
        ArrayList<Item> dropped = new ArrayList<>();
        for (Battler enemy1 : enemy) {
            if (dropped.contains(((BattlerAI) (enemy1)).getDroppedItem()) && ((BattlerAI) (enemy1)).getDroppedItem() != null) {
                dropped.get(partyItems.indexOf(((BattlerAI) (enemy1)). getDroppedItem())).increaseQuantity(((BattlerAI) (enemy1)).getDroppedItem().getQuantity());
            } else {
                dropped.add(((BattlerAI) (enemy1)).getDroppedItem());
            }
        }
        return dropped;
    }

    public float update(float delta) {
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
        updateTemporaryActors(delta);        
        if (animations.isEmpty()) {
            switch (state) {
                case START:
                    BattleDriver.printline("Battle Start!");
                    State.COMMAND.setMaxTicks(party.size());
                    state = State.COMMAND;
                    break;
                case COMMAND:
                    if (!(State.COMMAND.update())) {
                        if (State.COMMAND.getTicks() - 1 >= 0 && 
                                party.get(State.COMMAND.getTicks() - 1).canMove()) {
                            GraphicsDriver.addMenu(menuCommand(this, party.get(State.COMMAND.getTicks() - 1)));
                        } else {
                            partyAction.add(null);
                        }
                    } else if (State.COMMAND.getTicks() >= 0) {
                        state = State.ENEMY_COMMAND;
                    }
                    break;
                case ENEMY_COMMAND:
                    enemyCommand();
                    reorder();
                    state = State.ACTION;
                    state.setMaxTicks(allAction.size() * 2);
                    break;
                case ACTION:
                    if (!(State.ACTION.update())) {
                        Action currentAction;
                        if (State.ACTION.getTicks() % 2 == 1) {
                            currentAction = allAction.get(State.ACTION.getTicks() / 2);
                            if (!(currentAction == null)
                                    && currentAction.getCaster().canMove()) {
                                currentAction.declareAttack(this);
                                if (partyAction.contains(currentAction)
//                                        && currentAction.getCommand() == Battle.Command.Skill && 
//                                        currentAction.getCaster().getMP() >= currentAction.getSkill().getCost()
                                        ) {
                                        addAnimationAndSound(currentAction);
//                                    animations.add(Database2.SkillToActor(allAction.get(State.ACTION.getTicks() / 2).getSkill()).getBattlerAnimation());
                                }
                            }
                        } else if (State.ACTION.getTicks() % 2 == 0) {
                            currentAction = allAction.get(State.ACTION.getTicks() / 2 - 1);
                            if (!(currentAction == null) && 
                                    currentAction.getCaster().isAlive()) {
                                currentAction.calculateDamage(this);
                                if (!allAction.isEmpty() && 
                                        currentAction.getCommand() != Command.Run) {
                                }
                            }
                        }
                    } else {
                        allAction.clear();
                        enemyAction.clear();
                        partyAction.clear();
                        turnCount++;
                        statusCheck();
                        state = State.COMMAND;
                        state.setTicks(0);
                    }
                    break;
                case END:
                    exitBattle();
                    break;
            }        

            updatePersistentActors(delta);

            if (hasWon() && state != State.END) {
                BattleDriver.printline("Victory is yours!");
                BattleDriver.printline("You gained " + getTotalExperience() + " experience points!");
                for (Battler party1 : party) {
                    party1.changeExperiencePoints(getTotalExperience());
                }
                for (int i = 0; i < enemy.size(); i++) {
                    ArrayList<Item> allDropped = getAllItems();
                    if (allDropped.get(i) != null) {
                        BattleDriver.printline("You get "
                                + allDropped.get(i).getQuantity()
                                + " "
                                + allDropped.get(i).getName()
                                + (allDropped.get(i).getQuantity() > 1 ? "s!" : "!"));
                        BattleDriver.addItem(allDropped.get(i));
                    }
                }
                state = State.END;
            }
            if (hasLost() && state != State.END) {
                GraphicsDriver.removeAllStates();
                GraphicsDriver.addState(new GameOver());
            }
        }
        return delta;
    }

    public void render(SpriteBatch batch) {
        batch.draw(background, 0, 0);
        renderPersistentActors(batch);
        if (GraphicsDriver.getCurrentState().equals(this)) {
            renderTemporaryActors(batch);
        }
        GraphicsDriver.getPlayer().renderGlobalData(batch);
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub

    }

}
