package com.ark.darthsystem.states;

import com.ark.darthsystem.*;
import com.ark.darthsystem.database.Database2;
import com.ark.darthsystem.graphics.Actor;
import com.ark.darthsystem.graphics.FieldBattler;
import com.ark.darthsystem.graphics.GameTimer;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import java.util.ArrayList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Battle implements State {

    private static final String BATTLE_MUSIC = "music/Forbidden Secret Unfolding.mp3";
    private ArrayList<Battler> party;
    private ArrayList<Battler> enemy;
    private ArrayList<FieldBattler> partyActors;
    private ArrayList<FieldBattler> enemyActors;
    private ArrayList<Item> partyItems;
    private ArrayList<Action> partyAction;
    private ArrayList<Action> enemyAction;
    private ArrayList<Action> allAction;
    private ArrayList<Actor> animations;
    private int turnCount;
    private Scenario script;
    private State state;
    private Sprite background;
    private float elapsed;
    private ArrayList<GameTimer> timers;
    private String bgm;
    private ArrayList<Sound> sounds;

    public Battle(ArrayList<FieldBattler> initializeParty,
            ArrayList<FieldBattler> initializeEnemy,
            ArrayList<Item> initializeItems,
            Scenario initializeScenario) {
        this.sounds = new ArrayList<>();
        this.elapsed = 0;
        this.turnCount = 1;
        this.animations = new ArrayList<>();
        this.allAction = new ArrayList<>();
        this.enemyAction = new ArrayList<>();
        this.partyAction = new ArrayList<>();
        partyActors = initializeParty;
        party = new ArrayList<>();
        for (FieldBattler allies : initializeParty) {
            party.add(allies.getBattler());
        }
        enemyActors = initializeEnemy;
        enemy = new ArrayList<>();
        for (FieldBattler enemies : initializeEnemy) {
            enemy.add((enemies.getBattler()));
        }
        partyItems = initializeItems;
        script = initializeScenario;
        turnCount = 1;
        bgm = BATTLE_MUSIC;
    }

    public Battle(ArrayList<FieldBattler> initializeParty,
            ArrayList<FieldBattler> initializeEnemy,
            ArrayList<Item> initializeItems,
            Scenario initializeScenario,
            String initializeMusic) {
        this.sounds = new ArrayList<>();
        this.elapsed = 0;
        this.turnCount = 1;
        this.animations = new ArrayList<>();
        this.allAction = new ArrayList<>();
        this.enemyAction = new ArrayList<>();
        this.partyAction = new ArrayList<>();
        bgm = initializeMusic;
        partyActors = initializeParty;
        party = new ArrayList<>();
        for (FieldBattler allies : initializeParty) {
            party.add(allies.getBattler());
        }
        enemyActors = initializeEnemy;
        enemy = new ArrayList<>();
        for (FieldBattler enemies : initializeEnemy) {
            enemy.add((enemies.getBattler()));
        }
        partyItems = initializeItems;
        script = initializeScenario;
        turnCount = 1;
    }

    private void renderPersistentActors(SpriteBatch batch) {
        final float BATTLER_Y = 768 / 2;
        int divider = (enemyActors.size() > 1) ? 1024 / (enemyActors.size() + 1) : 512;
        for (int i = 0; i < enemyActors.size(); i++) {
            if (enemy.get(i).isAlive()) {
                Sprite s = (Sprite) (enemyActors.get(i).getSprite().getCurrentBattlerAnimation().getKeyFrame(elapsed));
                batch.draw(s,
                        divider * (i + 1),
                        BATTLER_Y,
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
            Sprite s = animations.get(i).getCurrentImage();
            if (s != null) {
                batch.draw(s,
                        animations.get(i).getX(),
                        animations.get(i).getY(),
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
            if (s != null) {
                s.stop();
                s.play();
            }
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
        final int BATTLE_DELAY = 3000;
        for (int i = 0; i < GraphicsDriver.getPlayer().getTimers().size(); i++) {
            GameTimer t = GraphicsDriver.getPlayer().getTimers().get(i);
            if (t.getName().equalsIgnoreCase("Invulnerable") || t.getName().equalsIgnoreCase("Jump")) {
                t.event(GraphicsDriver.getPlayer());
                GraphicsDriver.getPlayer().getTimers().remove(i);
                i--;
            }
        }
        GraphicsDriver.getPlayer().setSuperInvulnerability(BATTLE_DELAY);
        GraphicsDriver.removeState(this);
    }

    private void addAnimationAndSound(Action currentAction) {
        final float BATTLER_Y = GraphicsDriver.getHeight() / 2f;
        int divider = (enemyActors.size() > 1) ? GraphicsDriver.getWidth() / (enemyActors.size() + 1) : GraphicsDriver.getWidth() / 2;
        Battler tempBattler = currentAction.getTarget();

        if (currentAction.getCommand() == Command.Skill
                && (currentAction.getTarget() instanceof BattlerAI)
                && currentAction.getCaster().getMP() >= currentAction.getSkill().getCost()) {
            Skill tempSkill = currentAction.getSkill();
            sounds.add(tempSkill.getSound());
            tempSkill.getAnimation().resetAnimation();
            animations.add(tempSkill.getAnimation());
//            animations.get(animations.size() - 1).setX((float) ((GraphicsDriver.getWidth() + divider * (enemy.indexOf(tempBattler) - 1)) / 2 + divider * enemy.indexOf(tempBattler)));
            animations.get(animations.size() - 1).setX(divider * ((enemy.indexOf(tempBattler) + 1)));
            animations.get(animations.size() - 1).setY(BATTLER_Y);
        }
        if (currentAction.getCommand() == Command.Skill
                && (currentAction.getSkill().getAll())
                && (currentAction.getAllTargets().get(0) instanceof BattlerAI)
                && currentAction.getCaster().getMP() >= currentAction.getSkill().getCost()) {
            Skill tempSkill = currentAction.getSkill();
            sounds.add(tempSkill.getSound());
            tempSkill.getAnimation().resetAnimation();
            animations.add(tempSkill.getAnimation());
            animations.get(animations.size() - 1).setX((GraphicsDriver.getWidth() - animations.get(0).getWidth()) / 2);
            animations.get(animations.size() - 1).setY((GraphicsDriver.getHeight() - animations.get(0).getHeight()) / 2f);
        }
        Actor tempSkill;
        if (currentAction.getCommand() == Command.Attack
                && currentAction.getTarget() instanceof BattlerAI) {
            try {
                tempSkill = (currentAction.getCaster().
                        getWeapon().
                        getBattlerAnimation());
                tempSkill.resetAnimation();
                sounds.add(currentAction.getCaster().
                        getEquipment(Equipment.Slot.MainHand).getSound());
            } catch (NullPointerException e) {
                tempSkill = Database2.getDefaultBattlerUnarmedAnimation();
            }
            animations.add(tempSkill);
            animations.get(animations.size() - 1).setX(divider * (enemy.indexOf(tempBattler) + 1));
            animations.get(animations.size() - 1).setY(BATTLER_Y);
        }
    }

    @Override
    public String getMusic() {
        return bgm;
    }

    public Battle start() {
        GraphicsDriver.setCurrentCamera(GraphicsDriver.getCamera());
        background = new Sprite(GraphicsDriver.getAssets().get("backgrounds/battle.png", Texture.class));
        background.flip(false, true);

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
        Collections.sort(allAction, (Comparator<Action>) (a1, a2) -> {
            if (a1 == null) {
                return 1;
            }
            if (a2 == null) {
                return -1;
            }
            if (a1.getCaster().getSpeed() > a2.getCaster().getSpeed()
                    || (a1.getCaster().getSpeed() == a2.getCaster().getSpeed()
                    && Math.random() < .5)) {
                return -1;
            }
            return 1;
        });
    }

    public void actionCalculator() {
        for (Action allAction1 : allAction) {
            if (!(allAction1 == null) && allAction1.getCaster().isAlive()) {
                if (allAction1.checkStatus(this)) {
                    allAction1.calculateDamage(this);
                }
                if (hasWon() || hasLost()) {
                    break;
                }
            }
        }
    }

    public void statusCheck() {
        for (Battler party1 : party) {
            for (int i = 0; i < party1.getAllStatus().size(); i++) {
                if (party1.getStatus(i).faded(party1)) {
                    BattleDriver.printline(party1.getName() + "'s " + party1.getStatus(i).getName() + " faded away.");
                    party1.getAllStatus().remove(i);
                    i--;
                } else {
                    party1.getStatus(i).incrementTurn();
                }
            }
        }
        for (Battler enemy1 : enemy) {
            for (int i = 0; i < enemy1.getAllStatus().size(); i++) {
                if (enemy1.getStatus(i).faded(enemy1)) {
                    BattleDriver.printline(enemy1.getName() + "'s " + enemy1.getStatus(i) + " faded away.");
                    enemy1.getAllStatus().remove(i);
                    i--;
                } else {
                    enemy1.getStatus(i).incrementTurn();
                }
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

    public ArrayList<Action> getActions() {
        return allAction;
    }

    public ArrayList<Action> getPartyActions() {
        return partyAction;
    }

    public ArrayList<Action> getEnemyActions() {
        return enemyAction;
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
            for (int i = 0; i < ((BattlerAI) enemy1).getDroppedItem().length; i++) {
                Item item = ((Math.random() < ((BattlerAI) enemy1).getDropRate()[i]) ? (Item) (((BattlerAI) enemy1).getDroppedItem()[i].clone()) : null);
                if (item != null && item.isStackable() && dropped.contains(item)) {
                    dropped.get(dropped.indexOf(item)).increaseQuantity(((BattlerAI) enemy1).getDropNumber()[i]);
                } else if (item != null) {
                    dropped.add(item);
                }
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
                    state = State.COMMAND;
                    state.setMaxTicks(party.size());
                    break;
                case COMMAND:
                    if (!(State.COMMAND.update())) {
                        if (State.COMMAND.getTicks() - 1 >= 0
                                && party.get(State.COMMAND.getTicks() - 1).canMove()) {
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
                    state.setTicks(0);
                    state.setMaxTicks(allAction.size() * 2);
                    break;
                case ACTION:
                    if (!(State.ACTION.update())) {
                        Action currentAction;
                        if (State.ACTION.getTicks() % 2 == 1) {
                            currentAction = allAction.get(State.ACTION.getTicks() / 2);
                            if (!(currentAction == null) && currentAction.getCaster().canMove()) {
                                currentAction.declareAttack(this);
                                if (partyAction.contains(currentAction)
                                        && currentAction.getAllTargets() != null //                                        && currentAction.getCommand() == Battle.Command.Skill && 
                                        //                                        currentAction.getCaster().getMP() >= currentAction.getSkill().getCost()
                                        ) {
                                    if (currentAction.getSkill() == null || !currentAction.getSkill().getAll()) {
                                        currentAction.setNewTarget();
                                    }
                                    if (currentAction.checkStatus(this)) {
                                        addAnimationAndSound(currentAction);
                                    }
//                                    animations.add(Database2.SkillToActor(allAction.get(State.ACTION.getTicks() / 2).getSkill()).getBattlerAnimation());
                                }
                            }
                        } else if (State.ACTION.getTicks() % 2 == 0) {
                            currentAction = allAction.get(State.ACTION.getTicks() / 2 - 1);
                            if (!(currentAction == null) && currentAction.getCaster().isAlive()) {
                                if (currentAction.checkStatus(this)) {
                                    currentAction.calculateDamage(this);
                                }
                                if (!allAction.isEmpty() && currentAction.getCommand() != Command.Run) {

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
                        state.setMaxTicks(party.size());
                    }
                    break;
                case END:
                    exitBattle();
                    break;
            }

            if (hasWon() && state != State.END) {
                BattleDriver.printline("Victory is yours!");
                BattleDriver.printline("You gained " + getTotalExperience() + " experience points!");
                for (Battler party1 : party) {
                    party1.changeExperiencePoints(getTotalExperience());
                }
                ArrayList<Item> allDropped = getAllItems();
                for (int i = 0; i < allDropped.size(); i++) {
                    if (allDropped.get(i) != null) {
                        BattleDriver.printline("You get "
                                + allDropped.get(i).getCharges()
                                + " "
                                + allDropped.get(i).getName()
                                + (allDropped.get(i).getCharges() > 1 ? "s!" : "!"));
                        BattleDriver.addItem(allDropped.get(i));
                    }
                }
                state = State.END;
            }
            if (hasLost() && state != State.END) {
                GraphicsDriver.removeAllstates();
                GraphicsDriver.addState(new GameOver());
            }
        }
        GraphicsDriver.getPlayer().updateGlobal(delta);
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
//        background.getTexture().dispose();
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

    public enum Command {

        Attack,
        Defend,
        Skill,
        Charge,
        Analyze,
        Delay,
        Item,
        Run;

        public void run(final Battle battle, final Battler caster) {
            caster.setDelaying(false);
            switch (this) {
                case Attack:
                    String[] temp = new String[battle.getEnemy().size()];
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = battle.getEnemy().get(i).getName();
                    }

                    Menu targetMenu = new Menu("Target?", temp, true, true) {
                        @Override
                        public Object confirm(String choice) {
                            battle.getPartyActions().add(new Action(Command.Attack, caster, (battle.getEnemy().get(this.getCursorIndex())), battle.getEnemy()));
                            return choice;
                        }
                    };
                    GraphicsDriver.addMenu(targetMenu);
                    break;
                case Defend:
                    caster.defend();
                    battle.getPartyActions().add(new Action(this, caster));
                    break;
                case Skill:
                    final ArrayList<Skill> getSkillList = new ArrayList<>();
                    if (caster.getSkillList() != null && caster.getSkillList().size() > 0) {
                        for (Skill skillList1 : caster.getSkillList()) {
                            if (skillList1 != null) {
                                getSkillList.add(skillList1);
                            }
                        }
                        try {
                            temp = new String[getSkillList.size()];
                            for (int i = 0; i < temp.length; i++) {
                                temp[i] = getSkillList.get(i).getName();
                            }
                            Menu skillMenu = new Menu("Activate which skill?", temp, true, true) {
                                @Override
                                public Object confirm(String choice) {
                                    final Skill getSkill = getSkillList.get(getCursorIndex());
                                    final ArrayList<Battler> targetList = (ArrayList<Battler>) (getSkill.getAlly() ? battle.getAlly() : battle.getEnemy());
                                    if (getSkill.getCost() <= caster.getMP()) {
                                        if (!getSkill.getAll()) {
                                            String[] temp = new String[targetList.size()];
                                            for (int i = 0; i < temp.length; i++) {
                                                temp[i] = targetList.get(i).getName();
                                            }

                                            Menu targetMenu = new Menu("Target?", temp, true, true) {
                                                @Override
                                                public Object confirm(String choice) {
                                                    battle.getPartyActions().add(new Action(Command.Skill,
                                                            getSkill,
                                                            caster,
                                                            (targetList.get(this.getCursorIndex())),
                                                            targetList));
                                                    return choice;
                                                }
                                            };
                                            GraphicsDriver.addMenu(targetMenu);
                                        } else {
                                            battle.getPartyActions().add(new Action(Command.Skill,
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
                    if (battle.getItem() != null && battle.getItem().size() > 0) {
                        Menu menuItem = new Menu("Use which Item?", battle.getItemNames(), true, true) {
                            @Override
                            public Object confirm(String choice) {
                                final Item useItem;
                                useItem = battle.getItem(this.getCursorIndex());
                                final ArrayList<Battler> targetList = (ArrayList<Battler>) (useItem.getAlly() ? battle.getAlly() : battle.getEnemy());
                                if (!useItem.getAll()) {
                                    String[] temp = new String[targetList.size()];
                                    for (int i = 0; i < temp.length; i++) {
                                        temp[i] = targetList.get(i).getName();
                                    }
                                    Menu menuTarget = new Menu("Target?", temp, true, true) {
                                        @Override
                                        public Object confirm(String choice) {
                                            battle.getPartyActions().add(useItem.use(caster,
                                                    targetList.get(getCursorIndex()),
                                                    targetList));
                                            return choice;
                                        }
                                    };
                                    GraphicsDriver.addMenu(menuTarget);
                                } else {
                                    battle.getPartyActions().add(useItem.use(caster, targetList));
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
                    battle.getPartyActions().add(new Action(this, caster));
                    break;
                case Delay:
                    caster.setDelaying(true);
                case Run:
                    battle.getPartyActions().add(new Action(this, caster));
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
            maxTicks = 0;
            ticks = 0;
        }
        private int maxTicks;
        private int ticks;

        public int getMaxTicks() {
            return maxTicks;
        }

        public int getTicks() {
            return ticks;
        }

        public boolean update() {
            ticks++;
            if (ticks > maxTicks) {
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
    
    @Override
    public void addTimer(GameTimer t) {
        
    }

}
