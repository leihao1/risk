package com.risk.model;

import com.risk.common.Action;
import com.risk.common.CardType;
import com.risk.strategy.PlayerBehaviorStrategy;
import com.risk.strategy.StrategyFactory;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;


/**
 * Define class of a player
 * The following is only base code, further features need to be added
 */
public class Player extends Observable implements Serializable {

    //Unique Id for each player, starts from 1
    private int Id;
    //Counter to assign unique Id
    private static int cId=0;
    private String name;
    private long armies;
    private ArrayList<Country> countriesOwned;
    private ArrayList<Continent> continentsOwned;
    private String color = "#4B0082";
    private long totalStrength;

    private Country attacker;
    private int attackerDiceNum;
    private Country defender;
    private int defenderDiceNum;
    private int countriesSize;
    private int cardsArmy;

    private HashMap<String,Integer> cards;
    private int numberOccupy;

    //private Phase Phase.getInstance();
    //private PlayersWorldDomination worldDomination;


    private PlayerBehaviorStrategy strategy;

    /**
     * Constructor of player
     * @param name player name
     * @param countriesSize size of countries
     * @param s strategy wanted
     */
    public Player(String name, int countriesSize, String s){
        this.Id=++cId;
        this.name= name;
        this.countriesSize = countriesSize;
        armies = 0;
        countriesOwned = new ArrayList<Country>();
        continentsOwned = new ArrayList<Continent>();
        totalStrength = 0;

        cards = new HashMap<>();

        cards.put("infantry",0);
        cards.put("cavalry",0);
        cards.put("artillery",0);
        numberOccupy = 0;

        //Phase.getInstance() = Phase.getInstance();
        //worldDomination = PlayersWorldDomination.getInstance();

        strategy = StrategyFactory.getStrategy(s, this);
    }

    /**
     * Get number of occupy
     * @return numberOccupy
     */
    public int getNumberOccupy() {
        return numberOccupy;
    }

    /**
     * Set number of occupy
     * @param numberOccupy number of occupy
     */
    public void setNumberOccupy(int numberOccupy) {
        this.numberOccupy = numberOccupy;
    }

    /**
     * Get attacker country
     * @return attacker country
     */
    public Country getAttacker() {
        return attacker;
    }

    /**
     * Get the dice used in attacker country
     * @return dice used in attacker country
     */
    public int getAttackerDiceNum() {
        return attackerDiceNum;
    }

    /**
     * Get the defender country
     * @return defender country
     */
    public Country getDefender() {
        return defender;
    }

    /**
     * Get the dice used in defender country
     * @return dice used in defender country
     */
    public int getDefenderDiceNum() {
        return defenderDiceNum;
    }

    /**
     * return total number of cards owned by the player
     * @return n the total number of cards owned by the player
     */
    public int getTotalCards(){
        int n = 0;
        for (String key : cards.keySet()){
            n += cards.get(key);
        }
        return n;
    }


    /**
     * Get Player Card List
     * @return playerCardList
     */
    public List<Card> getPlayerCardList(){
        List<Card> playerCardList = new ArrayList<>();
        for(Map.Entry<String,Integer> entry:cards.entrySet()){
            if(entry.getKey().equals(CardType.ARTILLERY.toString().toLowerCase())){
                for(int i=0; i < entry.getValue(); i++){
                    playerCardList.add(new Card(CardType.ARTILLERY));
                }
            }
            if(entry.getKey().equals(CardType.CAVALRY.toString().toLowerCase())){
                for(int i=0; i < entry.getValue(); i++){
                    playerCardList.add(new Card(CardType.CAVALRY));
                }
            }
            if(entry.getKey().equals(CardType.INFANTRY.toString().toLowerCase())){
                for(int i=0; i < entry.getValue(); i++){
                    playerCardList.add(new Card(CardType.INFANTRY));
                }
            }
        }
        return playerCardList;
    }

    /**
     * Getter for color of player
     * @return Color of this player
     */
    public String getColor() {
        return color;
    }

    /**
     * Setter for color of player
     */
    public void setColor() {
        Random rand = new Random(Id*Id*Id);

        // Will produce only bright / light colours:
        float r = (float) (rand.nextFloat() / 2f + 0.4);
        float g = (float) (rand.nextFloat() / 2f + 0.4);
        float b = (float) (rand.nextFloat() / 2f + 0.4);

        Color randomColor = new Color(r, g, b);
        String hex = "#"+Integer.toHexString(randomColor.getRGB()).substring(2);

        this.color=hex;
    }

    /**
     * getter for cards
     * @return the cards
     */
    public HashMap<String,Integer> getCards(){
        return cards;
    }

    /**
    * Getter to get Id
    * @return Id of the player
    */ 
    public int getId() {
        return Id;
    }

    /**
     * Getter to get name
     * @return  player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Getter to get the number of armies
     * @return The number of armies that the player has currently
     */
    public long getArmies() {
        return armies;
    }

    /**
     * Getter to get the number of continents owned by this player
     * @return The number of continents that the player has currently
     */
    public ArrayList<Continent> getContinentsOwned() { return continentsOwned; }

    /**
     * Getter to get CountriesOwned
     * @return  The list of countries object that the player has currently
     */
    public ArrayList<Country> getCountriesOwned() {
        return countriesOwned;
    }

    /**
     * set the countries owned
     * @param  countriesOwned the countries owned
     */
    public void setCountriesOwned(ArrayList<Country> countriesOwned) {
        this.countriesOwned = countriesOwned;
    }

    /**
    * set the number of armies
    * @param  armies the number of armies
    */
    public void setArmies(long armies) {
        this.armies = armies;
    }

    /**
     * Getter to get totalStrength
     * @return  The num of totalStrength that the player has currently
     */
    public long getTotalStrength() {
        return totalStrength;
    }

    /**
     * set the number of totalStrength
     * @param totalStrength the number of totalStrength
     */
    public void setTotalStrength(long totalStrength) {
        this.totalStrength = totalStrength;
        PlayersWorldDomination.getInstance().update();
    }

    /**
     * Get Card Armies
     * @return cardArmy
     */
    public int getCardsArmy(){return cardsArmy;}

    /**
     * Get Countries Size
     * @return countries size
     */
    public int getCountriesSize(){return countriesSize;}

    /**
     * Set Strategy
     * @param newStrategy new strategy
     */
    public void setStrategy(PlayerBehaviorStrategy newStrategy){this.strategy = newStrategy;}

    /**
     * get Strategy
     * @return strategy
     */
    public PlayerBehaviorStrategy getStrategy(){return strategy;}

    /**
     * increase number of occupied
     */
    public void increaseNumberOccupy() {
        numberOccupy ++;
    }

    /**
     * Substract one for armies when allocated army in the initArmy() or the reinforcements phase
     * @param num remove army from player
     */
    public void subArmies(int num){

        long newArmies = armies - num;
        setArmies(newArmies);
    }

    /**
     * Substract one for armies when allocated army in the initArmy() or the reinforcements phase
     * @param num remove army from player
     */
    public void subTotalStrength(int num){

        long newStrength = totalStrength - num;
        setTotalStrength((newStrength));
    }

    /**
    * Verify if the armies is empty
    * @return  True if armies == 0, else False
    */
    public boolean isEmptyArmy() { return armies == 0 ? true : false; }

    /**
    * Add a country in the countriesOwned list
    * @param  c country need to be added
    */
    public void addCountry(Country c){
        //verify if the country is exist in the countriesOwned??
        countriesOwned.add(c);
        Continent continent = c.getContinent();
        for (Country country : continent.getCountry()) {
            if (country.getOwner() == null || !country.getOwner().equals(this)) {
                PlayersWorldDomination.getInstance().update();
                return;
            }
        }
        addContinent(continent);
        PlayersWorldDomination.getInstance().update();
    }

    /**
    * Remove a country from the countriesOwned list
    * @param  c country need to be deleted
    * @return true delete success, false delete failed
    */
    public boolean delCountry(Country c){

        Iterator<Country> it = countriesOwned.iterator();
        while(it.hasNext())
        {
            if (c.equals(it.next())){
                it.remove();
                if (continentsOwned.contains(c.getContinent())) {
                    delContinent(c.getContinent());
                }
                PlayersWorldDomination.getInstance().update();
                return true;
            }
        }
        return false;
    }

    /**
     * Add a continent in the continentsOwned list
     * @param  continent continent need to be added
     */
    public void addContinent(Continent continent){
        //verify if the country is exist in the countriesOwned??
        continentsOwned.add(continent);
    }

    /**
     * Remove a continent from the continentsOwned list
     * @param  continent continent need to be deleted
     * @return true delete success, false delete failed
     */
    public boolean delContinent(Continent continent){

        Iterator<Continent> it = continentsOwned.iterator();
        while(it.hasNext())
        {
            if (continent.equals(it.next())){
                it.remove();
                return true;
            }
        }
        return false;
    }



    /**
     * Verify if two countries is connected
     * @param start country 1
     * @param end country 2
     * @return true, connected; false unconnected
     */
    public boolean isConnected(Country start, Country end) {

        // if doesn't contain both of countries
        if (!isContain(start) ||  !isContain(end)) {
            return false;
        }

        //both contain, BFS find a path
        for (Country c : countriesOwned) {
            if (!c.equals(start)) {
                c.setProcessed(false);
            }
        }

        Queue<Country> queue = new PriorityQueue<Country>();
        queue.add(start);
        start.setProcessed(true);

        while (queue.isEmpty() == false) {
            Country c = queue.poll();

            ArrayList<Country> adjCountries = c.getAdjCountries();

            if ( adjCountries != null && adjCountries.size() != 0) {

                for(Country each : adjCountries) {

                    if (each.equals(end))  return true;

                    if (countriesOwned.contains(each) && !each.isProcessed()) {
                        each.setProcessed(true);
                        queue.add(each);
                    }
                }
            }
        }
        return false;
    }

    /**
     * verify if two users is equal
     * @param p Player need to be compare
     * @return true when comparing the same player false otherwise
     */
    public boolean equals(Player p) { return this.getId() == p.getId(); }


    /**
     * Verify if player is win all the game
     * @return true win, false, not yet
     */
    public boolean isWin(){
        return this.getCountriesOwned().size() == countriesSize;
    }

    /**
     * Verify if player is loose the game
     * @return true, loose, false not yet
     */
    public boolean isGg() {
        return this.countriesOwned.size() == 0;
    }

    /**
     *  Implementation of reinforcement
     */
    public void reinforcement(){
        try {
            strategy.reinforcement();
        } catch (InterruptedException e) {
            System.out.println("Payer.reinforcement(): " + e.getMessage());
        }

    }

    /**
     * Add armies in the very first of the reinforcement phase
     * The number of armies added is computed based on the number of countries and cards it has
     */
    public void addRoundArmies(){

        int newArmies = getArmiesAdded();
        setArmies(newArmies);
        setTotalStrength(totalStrength + newArmies);
    }


    /**
     * Compute the armiesAdded based on the number of countries continent and cards it has
     * @return armies need to be added
     */
    private int getArmiesAdded() {

        int armiesAdded = 0;

        // based on countries num
        if (countriesOwned.size() > 0) {
            armiesAdded = countriesOwned.size() / 3;
        }

        //based on continent
        armiesAdded += getArmiesAddedFromContinent();

        System.out.println("REINFORCEMENT ARMY NUMBER: " + armiesAdded);
        System.out.println("CARD ARMY NUMBER: " + cardsArmy);
        //need to implement next phase
        armiesAdded += cardsArmy;

        cardsArmy = 0;

        // the minimal number of reinforcement armies is 3
        if (armiesAdded < 3) {
            armiesAdded = 3;
        }

        return armiesAdded;
    }

    /**
     * Compute the armiesAdded based on the continents it has
     * @return the number of armies need to be added based on the continents it has
     */
    private int getArmiesAddedFromContinent() {

        int armiesAdded = 0;

        for (Continent continent : continentsOwned) {
            armiesAdded += continent.getControlVal();
        }

        return armiesAdded;
    }

    /**
     * player exchange three cards
     * @param card1 name of the first card
     * @param card2 name of the second card
     * @param card3 name of the third card
     */
    public void handleCards(String card1, String card2, String card3){
        cards.put(card1,cards.get(card1) - 1);
        cards.put(card2,cards.get(card2) - 1);
        cards.put(card3,cards.get(card3) - 1);

        if (strategy.getName().equalsIgnoreCase("human")) {
            CardModel.getInstance().update();
        }
    }

    /**
     * player change cards for armies
     */
    public void exchangeForArmy(){
        cardsArmy += Model.cardsValue;
        Model.cardsValue += 5;
    }


    /**
     * trade card
     */
    public void autoTradeCard() {

        for (String card : cards.keySet()) {

            if (cards.get(card) >= 3) {
                handleCards(card, card, card);
                exchangeForArmy();
                return;
            }
        }

        handleCards("infantry","cavalry","artillery");
        exchangeForArmy();
    }

    /**
     * Method for attack operation
     * @param attacker The country who start an attack
     * @param attackerNum how many dice the attacker choose
     * @param defender The country who defend
     * @param defenderNum how many dice the defender choose
     * @param isAllOut true, if the attacker want to all-out; else false
     */
    public void attack(Country attacker, String attackerNum, Country defender, String defenderNum, boolean isAllOut){

        try {
            strategy.attack(attacker, attackerNum, defender, defenderNum, isAllOut);
        } catch (InterruptedException e) {
            System.out.println("Payer.reinforcement(): " + e.getMessage());
        }

    }

    /**
     * Test if attack is valid
     * @param attacker The country who start the attack
     * @param attackerNum how many dise the attacker will use in this attack
     * @param defender The country who defend the attack
     * @param defenderNum how many dise the defender will use in this attack
     * @return if two country is adjacent, and their dice is less the armies they owned, return true, else false
     */
    public boolean isValidAttack(Country attacker, String attackerNum, Country defender, String defenderNum){

        // if any of countries is none
        if (attacker == null || defender == null) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Countries can not be none");
            Phase.getInstance().update();
            return false;
        }

        // if int valid
        int attackerDiceNum = 0;
        int defenderDiceNum = 0;
        try{
            attackerDiceNum = Integer.valueOf(attackerNum);
            defenderDiceNum = Integer.valueOf(defenderNum);
        } catch (Exception e){
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Input error, invalid dice number.");
            Phase.getInstance().update();
            return false;
        }

        if (!attacker.getOwner().equals(this)) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Invalid attack, This is not your country.");
            Phase.getInstance().update();
            return false;
        }

        //if the contries owner is the same
        if (attacker.getOwner().equals(defender.getOwner())) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Invalid attack, cannot attack a country owned by player himself.");
            Phase.getInstance().update();
            return false;
        }

        // if attacker's dice valid
        if (!attacker.isValidAttacker(attackerDiceNum)) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Invalid attacker dice number, armies in attacker must more than two, and the dice must less than armies");
            Phase.getInstance().update();
            return false;
        }

        // if defender's dice valid
        if (!defender.isValidDefender(defenderDiceNum)) {
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Invalid defender's dice number, the dice must less than armies");
            Phase.getInstance().update();
            return false;
        }

        // if two countries adjacent
        if (!attacker.isAdjacent(defender)){
            Phase.getInstance().setActionResult(Action.Invalid_Move);
            Phase.getInstance().setInvalidInfo("Two countries is not adjacent");
            Phase.getInstance().update();
            return false;
        }

        // update the attack info
        this.attacker = attacker;
        this.attackerDiceNum = attackerDiceNum;
        this.defender = defender;
        this.defenderDiceNum = defenderDiceNum;
        return true;
    }

    /**
     * Verify if defender loose the country
     * @return True-defender lost, otherwise false
     */
    public boolean isDefenderLoose() {
        return isDefenderLoose(attacker,defender);
    }

    /**
     * Verify if defender loose the country
     * @param attacker Attacking country
     * @param defender Defending country
     * @return True-defender lost, otherwise false
     */
    public boolean isDefenderLoose(Country attacker, Country defender) {

        if (defender.getArmies() == 0) {
            System.out.println(defender.getName()+ "lost all armies");
            if (defender.getOwner().countriesOwned.size() == 1) {

                // add all cards form defender's owner
                Phase.getInstance().setInvalidInfo(defender.getOwner().getName() + " lost all the countries!");
                Phase.getInstance().update();
                getDefenderCards(attacker.getOwner(),defender.getOwner());
            }

            // change the ownership of the defender country
            defender.getOwner().delCountry(defender);
            defender.setPlayer(attacker.getOwner());
            attacker.getOwner().addCountry(defender);

            // add numOfOccupy
            numberOccupy++;
            Phase.getInstance().setActionResult(Action.Move_After_Conquer);
            Phase.getInstance().setInvalidInfo("Successfully Conquered Country : "+ defender.getName()
                    +". Now You Must Place At Least " + attackerDiceNum + " Armies.");

            // if attacker win the game
            if (attacker.getOwner().isWin()) {
                Phase.getInstance().setActionResult(Action.Win);
                Model.winner = attacker.getName();
                // give the name of winner
                Phase.getInstance().setInvalidInfo("Congratulations, You Win!");
                System.out.println();
                System.out.println(name + ", Congratulations, You Win!");
                Model.winner = attacker.getOwner().getName();
            }
            return true;
        }
        return false;
    }

    /**
     * attacker get all the defender's cards
     * @param attacker the player who attack
     * @param defender the player who defend
     */
    public void getDefenderCards(Player attacker, Player defender){
        for(String key : defender.cards.keySet()){
            attacker.cards.put(key, attacker.cards.get(key) + defender.cards.get(key));
            defender.cards.put(key,0);
        }
    }

    /**
     * Battle until the defender be occupied or the attacker consume its armies
     */
    public void allOut() {

        while (true) {

            long three = 3;
            long two = 2;

            attackerDiceNum = Math.toIntExact(attacker.getArmies() > 3? 3 : attacker.getArmies());
            defenderDiceNum = Math.toIntExact(defender.getArmies() > 2? 2 : defender.getArmies());

            attackOnce();
            // if defender is occupied by attacker
            if(attacker.getOwner().equals(defender.getOwner())) break;
            // if attacker exhaust all its armies
            // if attack possible
            if(attacker.getArmies() == 0) break;
        }
        return;
    }

    /**
     * Battle until the defender be occupied or the attacker consume its armies
     * @param attacker attack country
     * @param defender defend country
     */
    public void allOut(Country attacker, Country defender) {

        this.attacker = attacker;
        this.defender = defender;


        //if defender country doesn't has army
        if (isDefenderLoose()) {
            return;
        }

        allOut();
        System.out.println();
    }

    /**
     * Battle only run once time
     * @param attacker Attacking country
     * @param attackerDiceNum Number of dices of attacking player
     * @param defender Defending country
     * @param defenderDiceNum Number of dices of defending player
     */
    public void attackOnce(Country attacker, int attackerDiceNum, Country defender, int defenderDiceNum) {

        // roll the dices to battle
        System.out.print(attacker.getName() + " VS " + defender.getName() + ": ");
        ArrayList<Integer> dicesAttacker = getRandomDice(attackerDiceNum);
        ArrayList<Integer> diceDefender = getRandomDice(defenderDiceNum);
        System.out.print(dicesAttacker);
        System.out.print(" VS ");
        System.out.print(diceDefender);
        System.out.print(",  ");
        System.out.println(" ");

        // compare the rolling result
        long range = attackerDiceNum < defenderDiceNum? attackerDiceNum : defenderDiceNum;
        for (int i=0; i<range; i++){

            if (diceDefender.get(i) >= dicesAttacker.get(i)) {
                attacker.setArmies(attacker.getArmies()-1);
                attacker.getOwner().subTotalStrength(1);
                System.out.println(" "+attacker.getName() + " lost one army");

            } else {
                defender.setArmies(defender.getArmies()-1);
                defender.getOwner().subTotalStrength(1);
                System.out.println(" "+defender.getName() + " lost one army");
                //if defender's armies == 0, attacker victory
                if (isDefenderLoose(attacker, defender)) return;
            }
        }
        Phase.getInstance().setInvalidInfo("Attack Finish. You Can Start Another Attack Or Enter Next Phase Now.");
        return;

    }


    /**
     * Overriding attackOnce for non-parameter call, for human player
     */
    public void attackOnce() {
        attackOnce(attacker, attackerDiceNum, defender, defenderDiceNum);
    }

    /**
     * Get a sorted list of random dices
     * @param num how many dices needed
     * @return list of random dice
     */
    public ArrayList<Integer> getRandomDice(int num){

        ArrayList<Integer> dices = new ArrayList<Integer>();
        Random random = new Random();

        for (int i=0; i<num; i++){
            int temp = random.nextInt(6)+1;
//            System.out.println("Dice " + i + " : " + temp);
            dices.add(temp);
        }
        Collections.sort(dices, Collections.reverseOrder());
        return dices;
    }

    /**
     * Verify if attack is possible
     * @return true, if has at least one country to attack; else false
     */
    public boolean isAttackPossible() {

        for (Country c : countriesOwned) {
            if (c.getArmies() < 2) continue;
            for (Country adj : c.getAdjCountries()) {
                if (!adj.getOwner().equals(c.getOwner())){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Move number of armies to the new conquered country
     * @param num the number of armies need to be move
     */
    public void moveArmy(String num){

        strategy.moveArmy(num);

    }

    /**
     * player receive a random card
     * @param newCard name of the new card
     */
    public void addRandomCard(String newCard) {
        if (numberOccupy > 0) {
            int value = cards.get(newCard) + 1;
            cards.put(newCard, value);
        }
        //reset the number of occupy
        numberOccupy = 0;
    }

    /**
     * Add Random card without param
     */
    public void addRandomCard() {

        if (getNumberOccupy() > 0) {
            Random random = new Random();
            int num = random.nextInt(3);
            String newCard = Model.cards[num];
            addRandomCard(newCard);
        }
    }

    /**
     * Is country c include in the countries owned
     * @param c country need to check
     * @return true, yes, false, no
     */
    public boolean isContain(Country c) {

        for (Country each : countriesOwned) {
            if (c.equals(each)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method for fortification operation
     * @param source The country moves out army
     * @param target The country receives out army
     * @param armyNumber Number of armies to move
     */
    public void fortification(Country source, Country target, int armyNumber) {
        try {
            strategy.fortification(source, target, armyNumber);
        } catch (InterruptedException e) {
            System.out.println("Payer.reinforcement(): " + e.getMessage());
        }
    }

    /**
     * For the computer player to orderly execute reinforcement(), attack() and fortification method
     */
    public void execute() {
        try {
            strategy.execute();
        } catch (InterruptedException e) {
            System.out.println("Payer.reinforcement(): " + e.getMessage());
        }
    }
}
