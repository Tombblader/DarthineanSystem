package com.ark.darthsystem;

import com.ark.darthsystem.states.Battle;
import java.util.HashMap;

/**
 *
 * @author Keven
 */
public interface Scenario {

    /**
     *
     */
    public static final AI[] Standard_Heal = {new AI(AI.Type.Attack, 5),
        new AI(AI.Type.AttackSkill, 5),
        new AI(AI.Type.Heal, 6)};

    public static final AI[] Brainless = {new AI(AI.Type.Attack, 10)};
    public static final AI[] Brainless_Skill = {new AI(AI.Type.AttackSkill, 10)};
    public static final AI[] Super_Brainless = {new AI(AI.Type.Nothing, 10)};
    public static final AI[] Brainless_Heal = {new AI(AI.Type.Heal, 5),
        new AI(AI.Type.SupportSkill, 5)};
    public final AI[] Standard = {new AI(AI.Type.Attack, 5), new AI(AI.Type.AttackSkill, 5)};
    public static final AI[] Standard_Support = {new AI(AI.Type.Attack, 5),
        new AI(AI.Type.AttackSkill, 5),
        new AI(AI.Type.SupportSkill, 5)};
    public static HashMap<String, AI[]> AI_TYPE = new HashMap<String, AI[]>() {
        {
            put("Standard_Heal".toUpperCase(), Standard_Heal);
            put("Brainless".toUpperCase(), Brainless);
            put("Brainless_Skill".toUpperCase(), Brainless_Skill);
            put("Brainless_Heal".toUpperCase(), Brainless_Heal);
            put("Standard".toUpperCase(), Standard);
            put("Standard_Support".toUpperCase(), Standard_Support);
            put("Offense".toUpperCase(), Standard);
        }
    };

    /**
     *
     * @param b
     * @param condition
     * @return
     */
    public boolean usable(Battle b, int condition);

    /**
     *
     * @throws GameOverException
     */
    public void run() throws GameOverException;
}
