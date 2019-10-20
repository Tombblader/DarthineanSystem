package com.ark.darthsystem.states.events;

import com.ark.darthsystem.database.Database1;
import com.ark.darthsystem.database.MonsterDatabase;
import com.ark.darthsystem.graphics.FieldBattlerAI;
import com.ark.darthsystem.graphics.GraphicsDriver;
import com.ark.darthsystem.graphics.PlayerCamera;
import com.ark.darthsystem.states.Battle;
import com.badlogic.gdx.maps.MapProperties;

/**
 *
 * @author keven
 */
public class BattleStart extends Event {


    private FieldBattlerAI enemy;

    public BattleStart() {
        super();
    }

    public BattleStart(String img, String[] ai, float getX, float getY, float delay) {
        super(img, getX, getY, delay);
        enemy = MonsterDatabase.MONSTER_LIST.get(ai[0].toUpperCase()).clone();
        ;
        for (int i = 1; i < ai.length; i++) {
            enemy.getAllFieldBattlers().addAll(MonsterDatabase.MONSTER_LIST.get(ai[i].toUpperCase()).clone().getAllFieldBattlers());
        }
    }

    @Override
    public Event createFromMap(MapProperties prop) {
        String image = prop.get("image", String.class);
        SpawnEnemy s = new SpawnEnemy(image,
                prop.get("parameters", String.class).split(", *"),
                (prop.get("x", Float.class) + prop.get("width", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                (prop.get("y", Float.class) + prop.get("height", Float.class) / 2) / PlayerCamera.PIXELS_TO_METERS,
                6 / 60f);
        s.setTriggerMethod(Event.TriggerMethod.valueOf(prop.get("trigger", String.class)));
        return s;
    }

    @Override
    public void run() {
        if (!isFinished() && enemy.getCurrentMap() != this.getCurrentMap()) {
            enemy.setMap(this.getCurrentMap(), getX(), getY());
            GraphicsDriver.addState(new Battle(GraphicsDriver.getPlayer().getAllFieldBattlers(), enemy.getAllFieldBattlers(), Database1.inventory, null).start());
            GraphicsDriver.getPlayer().setInvulnerability(1000);
        } else if (enemy.totalPartyKill()) {
            switches.switchOn(LocalSwitch.Switch.FINISHED);
        }
    }

}
