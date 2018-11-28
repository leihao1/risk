package com.risk.strategy;

import com.risk.model.Player;

/**
 * Return a correspond strategy according the param
 **/
public class StrategyFactory {

    /**
     * private constructor
     */
    private StrategyFactory() {}

    /**
     * static method to create strategy method
     *
     */
    public static PlayerBehaviorStrategy getStrategy(String s, Player p) {

        if (s.equalsIgnoreCase("aggressive player")) {
            return new AggressiveStrategy(p);
        } else if (s.equalsIgnoreCase("benevolent player")) {
            return new BenevolentStrategy(p);
        } else if (s.equalsIgnoreCase("human player")) {
            return new HumanStrategy(p);
        } else if (s.equalsIgnoreCase("random player")) {
            return new RandomStrategy(p);
        } else if (s.equalsIgnoreCase("cheater player")) {
            return new CheaterStrategy(p);
        }

        return null;
    }
}
