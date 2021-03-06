package com.risk.strategy;

import com.risk.common.Action;
import com.risk.model.*;

import java.io.Serializable;

import static java.lang.Thread.sleep;

/**
 * Human strategy class
 */
public class HumanStrategy implements PlayerBehaviorStrategy, Serializable {

    private String name;
    private Player player;
//    private Phase Phase.getInstance();

    /**
     * Constructor
     * @param player the correspinding player
     */
    public HumanStrategy(Player player){
        name = "human";
        this.player = player;
//        phase = Phase.getInstance();
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
     * Not need
     */
    @Override
    public void execute() {
    }

    //-----------------------------------------reinforcement----------------------------------------------
    /**
     *  Implementation of reinforcement
     */
    @Override
    public void reinforcement() throws InterruptedException {
        Phase.getInstance().setCurrentPhase("Reinforcement Phase");
        player.addRoundArmies();
        Phase.getInstance().update();

    }


//--------------------------------------------------attack--------------------------------------------------------
    /**
     * Method for attack operation
     * @param attacker The country who start an attack
     * @param attackerNum how many dice the attacker choose
     * @param defender The country who defend
     * @param defenderNum how many dice the defender choose
     * @param isAllOut true, if the attacker want to all-out; else false
     */
    @Override
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut) throws InterruptedException {

        if (!player.isValidAttack(attacker, attackerNum, defender, defenderNum)) {
            return;
        }

        // if defender country doesn't has army
        if (player.isDefenderLoose()) {
            Phase.getInstance().update();
            return;
        }

        if (isAllOut) {
            // dice number depend by computer
            player.allOut();
        } else {
            // players choose how many dice need to put
            player.attackOnce();
        }

        //update phase info
        if (Phase.getInstance().getActionResult() == null) {
            Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        }
        if (Phase.getInstance().getActionResult() != Action.Win && Phase.getInstance().getActionResult() != Action.Move_After_Conquer){
            Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
            if (!player.isAttackPossible()) {
                Phase.getInstance().setActionResult(Action.Attack_Impossible);
                Phase.getInstance().setInvalidInfo("Attack Impossible. You Can Enter Next Phase Now.");
            }
        }

        Phase.getInstance().update();

        return;
    }


    /**
     * Move number of armies to the new conquered country
     * @param num the number of armies need to be move
     */
    @Override
    public void moveArmy(String num){

        Country attacker = player.getAttacker();
        Country defender = player.getDefender();
        int attackerDiceNum = player.getAttackerDiceNum();

        int numArmies = 0;
        try{
            numArmies = Integer.valueOf(num);
        } catch (Exception e){
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Please input a number");
            Phase.getInstance().update();
            return;
        }

        if (player.isContain(attacker) && player.isContain(defender)
                && attacker.getArmies() >= numArmies && numArmies >= attackerDiceNum) {
            attacker.setArmies(attacker.getArmies() - numArmies);
            defender.setArmies(defender.getArmies() + numArmies);

            Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
            Phase.getInstance().setInvalidInfo("Army Movement Finish. You Can Start Another Attack Or Enter Next Phase Now");
            if (!player.isAttackPossible()) {
                Phase.getInstance().setActionResult(Action.Attack_Impossible);
                Phase.getInstance().setInvalidInfo("Attack Impossible. You Can Enter Next Phase Now.");
            }
            Phase.getInstance().update();
            return;
        }
        Phase.getInstance().setActionResult(Action.Invalid_Move);
        Phase.getInstance().setInvalidInfo("You Must Place At Least " + attackerDiceNum + ", And Maximum "
                +attacker.getArmies()+" Armies.");
        Phase.getInstance().update();
        return;

    }


//---------------------------------------------fortify----------------------------------------------------
    /**
     * Method for fortification operation
     * @param source The country moves out army
     * @param target The country receives out army
     * @param armyNumber Number of armies to move
     */
    @Override
    public void fortification(Country source, Country target, int armyNumber){
        //return no response to view if source country's army number is less than the number of armies on moving,
        //or the source and target countries aren't connected through the same player's countries
        if (!source.getOwner().equals(player) || !target.getOwner().equals(player)) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Invalid move, This is not your country.");
            Phase.getInstance().update();
            return;
        }

        if(source.getArmies()<armyNumber || !source.getOwner().isConnected(source,target)) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("invalid move");
            Phase.getInstance().update();
            return;
        }

        source.setArmies(source.getArmies()-armyNumber);
        target.setArmies(target.getArmies()+armyNumber);

        Phase.getInstance().setActionResult(Action.Show_Next_Phase_Button);
        Phase.getInstance().update();
    }

}
