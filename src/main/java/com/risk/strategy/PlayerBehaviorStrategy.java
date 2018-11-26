package com.risk.strategy;

import com.risk.model.Country;

public interface PlayerBehaviorStrategy {
    void reinforcement( );

    void allocateArmy(Country country, boolean disable);

    void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut);

    void moveArmy(String num);

    boolean isAttackPossible();

    void fortification(Country source, Country target, int armyNumber);

}
