package com.ark.darthsystem;

import com.ark.darthsystem.States.Battle;

public interface Scenario {

    public static final AI[] Standard_Heal = {new AI(AI.Type.Attack, 5),
        new AI(AI.Type.AttackSkill, 5),
        new AI(AI.Type.Heal, 6)};
    public static final AI[] Brainless = {new AI(AI.Type.Attack, 10)};
    public static final AI[] Super_Brainless = {new AI(AI.Type.Nothing, 10)};
    public static final AI[] Brainless_Heal = {new AI(AI.Type.Heal, 5),
        new AI(AI.Type.SupportSkill, 5)};
    public final AI[] Standard = {new AI(AI.Type.Attack, 5), new AI(AI.Type.AttackSkill, 5)};
    public static final AI[] Standard_Support = {new AI(AI.Type.Attack, 5),
        new AI(AI.Type.AttackSkill, 5),
        new AI(AI.Type.SupportSkill, 5)};

    public boolean usable(Battle b, int condition);

    public void run() throws GameOverException;
}
