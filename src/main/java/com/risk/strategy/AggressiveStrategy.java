package com.risk.strategy;


import com.risk.common.Action;
import com.risk.common.Tool;
import com.risk.model.Country;
import com.risk.model.Phase;
import com.risk.model.Player;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Aggressive strategy class
 */
public class AggressiveStrategy implements PlayerBehaviorStrategy, Serializable {

    private String name;
    private Player player;
//    private Phase Phase.getInstance();

    /**
     * constructor
     * @param player player with this strategy
     */
    public AggressiveStrategy(Player player) {

        name = "aggressive";
        this.player = player;
//        Phase.getInstance() = Phase.getInstance();
    }


    /**
     * Get name
     * @return name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Orderly execute reinforcement(), attack() and fortification method
     */
    @Override
    public void execute() throws InterruptedException {

        Tool.printBasicInfo(player,"Before Round-Robin");

        //reinforcement
        reinforcement();

        //attack
        Phase.getInstance().setCurrentPhase("Attack Phase");
        Phase.getInstance().update();
        attack(null, "0", null, "0", true);
        if (Phase.getInstance().getActionResult() == Action.Win) {
            return;
        }

        //fortification
        Phase.getInstance().setCurrentPhase("Fortification Phase");
        Phase.getInstance().update();
        fortification(null, null, 0);
    }


    /**
     * Reinforcement method
     * reinforces its strongest country
     */
    @Override
    public void reinforcement() throws InterruptedException {

        System.out.println(player.getName() + " enter the reinforcement phase");
        System.out.println(" ");
        HashMap<String, Long> reinforceCountry = new HashMap<>();
        // change card first
        // cards = {"infantry","cavalry","artillery"};
        while (player.getTotalCards() >= 5) {
            player.autoTradeCard();
        }

        // computer the armies that need to added roundly
//        Phase.getInstance().setCurrentPhase("Reinforcement Phase");
        player.addRoundArmies();
        Phase.getInstance().update();


        // find the strongest country
        Country strongest = player.getCountriesOwned().stream()
                .max(Comparator.comparingLong(Country::getArmies))
                .get();

        // add all the armies to weakest
        strongest.addArmies(player.getArmies());
        reinforceCountry.put(strongest.getName(),player.getArmies());
        player.setArmies(0);

        // update phase
        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().update();

        Tool.printBasicInfo(player, "After reinforcement: ");
        System.out.println(reinforceCountry);

//        sleep(500);
    }



    /**
     * Attack method
     * always attack with the strongest country until it cannot attack anymore
     * @param attacker null
     * @param attackerNum 0
     * @param defender null
     * @param defenderNum 0
     * @param isAllOut true
     */
    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) throws InterruptedException {
        System.out.println(" ");
        System.out.println(player.getName() + " enter the attack phase");
        System.out.println(" ");

        // attacker is the strongest country
        Country strongest = player.getCountriesOwned().stream()
                .max(Comparator.comparingLong(Country::getArmies))
                .get();

        List<Country> enemies = strongest.getAdjCountries().stream()
                .filter(c -> !c.getOwner().equals(player))
                .collect(Collectors.toList());

        for (Country enemy : enemies) {
            if (strongest.getArmies() >= 2) {
                player.allOut(strongest, enemy);

                if (Phase.getInstance().getActionResult() == Action.Win) {
                    Phase.getInstance().update();
                    player.addRandomCard();
                    return;
                }

                if (Phase.getInstance().getActionResult() == Action.Move_After_Conquer) {
                    moveArmy(String.valueOf(player.getAttackerDiceNum()));
                }
            }
        }

        player.addRandomCard();
        Phase.getInstance().update();
        Tool.printBasicInfo(player,"After attack: ");

//        sleep(500);
    }



    /**
     * Move army method
     * move the mininum armies that could
     * @param num number of army to move
     */
    @Override
    public void moveArmy(String num) {

        int numArmies = Integer.valueOf(num);
        Country attacker = player.getAttacker();
        Country defender = player.getDefender();

        attacker.setArmies(attacker.getArmies() - numArmies);
        defender.setArmies(defender.getArmies() + numArmies);

        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().setInvalidInfo("Army Movement Finish. You Can Start Another Attack Or Enter Next Phase Now");
    }


    /**
     * Fortification method
     * maximize aggregation of forces in one country
     * @param source from source
     * @param target to target
     * @param armyNumber move num of army
     */
    @Override
    public void fortification(Country source, Country target, int armyNumber) throws InterruptedException {

        System.out.println(player.getName() + " enter the fortification phase");
        System.out.println(" ");
        List<Country> decreaseSorted = player.getCountriesOwned().stream()
                .sorted((c1, c2) -> {
                    if (c2.getArmies() - c1.getArmies() > 0 ) return 1;
                    else if (c2.getArmies() - c1.getArmies() == 0) return 0;
                    else return -1; })
                .collect(Collectors.toList());

        for (int i = 0; i < decreaseSorted.size(); i++) {
            for (int j = 1; j < decreaseSorted.size(); j++) {

                Country c1 = decreaseSorted.get(i);
                Country c2 = decreaseSorted.get(j);

                if (player.isConnected(c1, c2)){

                    System.out.println("From Country : "+c2.getName());
                    System.out.println("TO Country : "+c1.getName());

                    // re-allocated armies
                    c1.setArmies(c1.getArmies() + c2.getArmies());
                    System.out.println("Move "+c2.getArmies() +" Armies");
                    c2.setArmies(0);

                    Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
                    Phase.getInstance().update();

                    Tool.printBasicInfo(player,"After fortification: ");
                    return;
                }
            }
        }

        Tool.printBasicInfo(player,"After fortification: ");
//        sleep(500);
    }
}
