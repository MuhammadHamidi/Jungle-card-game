import java.util.*;

/**
 * <h1>Play Game</h1>
 * Making an object from this class provides an space to run the game.
 * every object from this class contains 12 initial string that keeps
 * animal's information , two player and a variable that tells now is
 * who's turn to play.
 *
 * @author mohammad hamidi
 */
public class Game {
    private String[] animals = new String[12];
    private Player[] players = new Player[2];
    private int turn = 0;

    Random random = new Random();

    {
        animals[0] = "Lion " + 150 + " " + 500 + " " + 1000 + " " + 900;
        animals[1] = "Bear " + 130 + " " + 600 + " " + 900 + " " + 850;
        animals[2] = "Tiger " + 120 + " " + 650 + " " + 850 + " " + 850;
        animals[3] = "Vulture " + 100 + " " + 0 + " " + 600 + " " + 350;
        animals[4] = "Fox " + 90 + " " + 0 + " " + 600 + " " + 400;
        animals[5] = "Elephant " + 50 + " " + 70 + " " + 500 + " " + 1200;
        animals[6] = "Wolf " + 0 + " " + 700 + " " + 700 + " " + 450;
        animals[7] = "Hog " + 80 + " " + 0 + " " + 500 + " " + 1100;
        animals[8] = "Hippo " + 110 + " " + 0 + " " + 360 + " " + 1000;
        animals[9] = "Cow " + 90 + " " + 100 + " " + 400 + " " + 750;
        animals[10] = "Rabbit " + 80 + " " + 0 + " " + 350 + " " + 200;
        animals[11] = "Turtle " + 200 + " " + 0 + " " + 230 + " " + 350;
    }

    /**
     * this method receives a player and sets randomly 30 carts for him.
     * @param player desired player
     */
    public void setFirst30(Player player){
        int counter = 0;
        while (counter < 30){
            int rand = random.nextInt(12);
            String name = animals[rand].split(" ")[0];
            int normalDamage = Integer.parseInt(animals[rand].split(" ")[1]);
            int hardDamage = Integer.parseInt(animals[rand].split(" ")[2]);
            double energy = Double.parseDouble(animals[rand].split(" ")[3]);
            double health = Double.parseDouble(animals[rand].split(" ")[4]);
            Animal animal = new Animal(name , normalDamage , hardDamage , energy , health);
            if(player.allowedCart(animal)){          //checks that player is allowed to add or not
                player.addCart(animal);
                counter++;
            }
        }
    }

    /**
     * this method receives a player and after showing 30 carts to him
     * gets indexes that player chose them and wants to start the game with these carts.
     * @param player desired player
     */
    public void chooseTenCartsHuman(Player player){
        Scanner input = new Scanner(System.in);
        player.printCartsNames();
        System.out.println("choose 10 carts with this formats: (n1 n2 n3 ... n10)");
        String choices = input.nextLine();
        while (choices.split(" ").length != 10){          //checks that input format and num of choices is correct or not
            System.out.println("please pay attention to the format and num of choices. try again");
            choices = input.nextLine();
        }
        String[] carts = choices.split(" ");
        player.setCartsHuman(carts);
    }

    /**
     * this method receives a robot player and randomly chose 10 carts from 30 initial carts for it.
     * @param player desired player
     */
    public void chooseTenCartsRobot(Player player){
        HashSet<Integer> indexes = new HashSet<>();
        while (indexes.size() != 10){
            int rand = random.nextInt(30);
            indexes.add(rand);
        }
        player.setCartsRobot(indexes);
    }

    /**
     * this method prints all of player's carts with their information at first of every turn.
     * this method has no return and no parameter.
     */
    public void printAllCarts(){
        System.out.println("player1 :");
        players[0].printCarts();
        System.out.println("player2 :");
        players[1].printCarts();
    }

    /**
     * this method receives index of a player and gets from him attacker cart(s) and target cart.
     * if energies was enough attack is done else gets attacker cart(s) and target cart again.
     * @param playerIndex index of player
     */
    public void attackHuman(int playerIndex){
        Scanner input = new Scanner(System.in);
        HashMap<Integer , Integer> attackers = new HashMap<>();
        while (true){
            System.out.println("1. choose attacker cart\n2. done");
            String choice = input.nextLine();
            if(!choice.equals("1") && !choice.equals("2")){
                System.out.println("invalid input. try again");
            }
            else if(choice.equals("1")){
                System.out.println("choose a cart:");
                String attacker = input.nextLine();
                int index = Integer.parseInt(attacker) -1;
                while (index >= players[playerIndex].getCartsNum() || index < 0){          //checks that index exist or not
                    System.out.println("invalid index. try again");
                    attacker = input.nextLine();
                    index = Integer.parseInt(attacker) -1;
                }
                players[playerIndex].chooseDamageType(attackers , index);
            }
            else if(attackers.size() == 0){          //checks that any cart is selected as attacker or not
                System.out.println("you have to choose at least one attacker");
            }
            else if(!players[playerIndex].enoughEnergy(attackers)){          //checks that energies are enough or not
                System.out.println("energy is not enough. choose another set of carts again");
                attackers.clear();
            }
            else {
                int otherPlayerIndex = playerIndex == 0 ? 1 : 0;
                players[playerIndex].decreaseEnergy(attackers);
                System.out.println("choose a target cart from the other player:");
                String target = input.nextLine();
                int index = Integer.parseInt(target) -1;
                while (index >= players[otherPlayerIndex].getCartsNum() || index < 0){          //checks that index exist or not
                    System.out.println("invalid index. try again");
                    target = input.nextLine();
                    index = Integer.parseInt(target) -1;
                }
                double damage = players[playerIndex].sumOfAttackDamage(attackers);
                players[otherPlayerIndex].damage(damage , index);
                break;
            }
        }
    }

    /**
     * this method receives index of a robot player and makes random attacker cart(s) and target carts for it.
     * if energies was enough attack is done else makes random attacker cart(s) and target cart again.
     * @param playerIndex index of player
     */
    public void attackRobot(int playerIndex){
        HashMap<Integer , Integer> attackers = new HashMap<>();
        HashSet<Integer> indexes = new HashSet<>();
        while (true){
            int num = random.nextInt(players[playerIndex].getCartsNum()) + 1;
            while (indexes.size() < num){
                int rand = random.nextInt(players[playerIndex].getCartsNum());
                indexes.add(rand);
            }
            for (Integer index : indexes) {
                players[playerIndex].chooseDamageTypeRobot(attackers , index);
            }
            if(!players[playerIndex].enoughEnergy(attackers)){          //checks that energies are enough or not
                indexes.clear();
                attackers.clear();
            }
            else {
                int otherPlayerIndex = playerIndex == 0 ? 1 : 0;
                players[playerIndex].decreaseEnergy(attackers);
                int target = random.nextInt(players[otherPlayerIndex].getCartsNum());
                double damage = players[playerIndex].sumOfAttackDamage(attackers);
                players[otherPlayerIndex].damage(damage , target);
                break;
            }
        }
    }

    /**
     * this method handles inputs and runs the game.
     * this method has no return and no parameter.
     */
    public void newGame(){
        Scanner input = new Scanner(System.in);
        boolean gameDone = false;
        while (true) {
            System.out.println("1. new game\n2. exit");
            String in = input.nextLine();
            while (!in.equals("1") && !in.equals("2")){
                System.out.println("invalid input.try again");
                in = input.nextLine();
            }
            if (in.equals("1")) {
                System.out.println("1. player vs player\n2. player vs bot");
                String type = input.nextLine();
                while (!type.equals("1") && !type.equals("2")){
                    System.out.println("invalid input.try again");
                    type = input.nextLine();
                }
                if (type.equals("1")) {
                    players[0] = new Player(types.Human);
                    players[1] = new Player(types.Human);
                    setFirst30(players[0]);
                    setFirst30(players[1]);
                    chooseTenCartsHuman(players[0]);
                    chooseTenCartsHuman(players[1]);
                    while (true) {
                        printAllCarts();
                        System.out.println("turn of player " + (turn + 1) + " :");
                        //checks that player can do anything or not
                        if(!players[turn].canAttack() && players[turn].getRemainedHealingTimes() == 0){
                            System.out.println("you can't do anything");
                            System.out.println("all healing times used and energies are not enough");
                            turn = turn == 0 ? 1 : 0;
                            continue;
                        }
                        System.out.println("1. attack\n2. heal");
                        String choice = input.nextLine();
                        while (!choice.equals("1") && !choice.equals("2")) {
                            System.out.println("invalid input.try again");
                            choice = input.nextLine();
                        }
                        if (choice.equals("1")) {
                            attackHuman(turn);
                            turn = turn == 0 ? 1 : 0;
                        } else {
                            HashSet<Integer> needHealing = players[turn].needHealing();
                            if (needHealing.size() == 0) {
                                System.out.println("no cart needs healing. you can just attack");
                            }
                            else if(players[turn].getRemainedHealingTimes() == 0){          //checks remained healing times
                                System.out.println("you used up all your healing times. you can just attack");
                            }
                            else {
                                String needs = "";
                                for (Integer integer : needHealing) {
                                    needs = needs + (integer+1) + " ";
                                }
                                System.out.println("these need healing. choose one of them : " + needs);
                                int heal = Integer.parseInt(input.nextLine());
                                while (!needHealing.contains(heal-1)) {          //checks that index is correct or not
                                    System.out.println("wrong index.try again");
                                    heal = Integer.parseInt(input.nextLine());
                                }
                                players[turn].healCart(heal-1);
                                turn = turn == 0 ? 1 : 0;
                            }
                        }
                        if(players[0].getCartsNum() == 0){          //finishes game and tells the winner
                            System.out.println("player 2 won");
                            gameDone = true;
                            break;
                        }
                        if(players[1].getCartsNum() == 0) {          //finishes game and tells the winner
                            System.out.println("player 1 won");
                            gameDone = true;
                            break;
                        }
                        //checks that both players can't do anything and finishes game with draw result
                        if(!players[0].canAttack() && players[0].getRemainedHealingTimes() == 0 &&
                                !players[1].canAttack() && players[1].getRemainedHealingTimes() == 0) {
                            System.out.println("draw");
                            gameDone = true;
                            break;
                        }
                    }
                }
                else{
                    players[0] = new Player(types.Human);
                    players[1] = new Player(types.Robot);
                    setFirst30(players[0]);
                    setFirst30(players[1]);
                    chooseTenCartsHuman(players[0]);
                    chooseTenCartsRobot(players[1]);
                    while (true) {
                        if(turn == 0) {
                            printAllCarts();
                            System.out.println("turn of player 1 :");
                            //checks that player can do anything or not
                            if(!players[turn].canAttack() && players[turn].getRemainedHealingTimes() == 0){
                                System.out.println("you can't do anything");
                                System.out.println("all healing times used and energies are not enough");
                                turn = turn == 0 ? 1 : 0;
                                continue;
                            }
                            System.out.println("1. attack\n2. heal");
                            String choice = input.nextLine();
                            while (!choice.equals("1") && !choice.equals("2")) {
                                System.out.println("invalid input.try again");
                                choice = input.nextLine();
                            }
                            if (choice.equals("1")) {
                                attackHuman(turn);
                                turn = turn == 0 ? 1 : 0;
                            } else {
                                HashSet<Integer> needHealing = players[turn].needHealing();
                                if (needHealing.size() == 0) {
                                    System.out.println("no cart needs healing. you can just attack");
                                } else if (players[turn].getRemainedHealingTimes() == 0) {          //checks remained healing times
                                    System.out.println("you used up all your healing times. you can just attack");
                                } else {
                                    String needs = "";
                                    for (Integer integer : needHealing) {
                                        needs = needs + (integer+1) + " ";
                                    }
                                    System.out.println("these need healing. choose one of them : " + needs);
                                    int heal = Integer.parseInt(input.nextLine());
                                    while (!needHealing.contains(heal-1)) {          //checks that index is correct or not
                                        System.out.println("wrong index.try again");
                                        heal = Integer.parseInt(input.nextLine());
                                    }
                                    players[turn].healCart(heal-1);
                                    turn = turn == 0 ? 1 : 0;
                                }
                            }
                        }
                        else {
                            System.out.println("turn of player 2 (robot) :");
                            //checks that player can do anything or not
                            if(!players[turn].canAttack() && players[turn].getRemainedHealingTimes() == 0){
                                System.out.println("you can't do anything");
                                System.out.println("all healing times used and energies are not enough");
                                turn = turn == 0 ? 1 : 0;
                                continue;
                            }
                            HashSet<Integer> needHealing = players[turn].needHealing();
                            if(needHealing.size() == 0 || players[turn].getRemainedHealingTimes() == 0){
                                attackRobot(turn);
                            }
                            else {
                                int rand = random.nextInt(2) + 1;
                                if(rand == 1){
                                    attackRobot(turn);
                                }
                                else {
                                    int num = random.nextInt(needHealing.size());
                                    int counter = 0;
                                    for (Integer integer : needHealing) {
                                        if(counter == num){
                                            players[turn].healCart(integer);
                                            break;
                                        }
                                        counter++;
                                    }
                                }
                            }
                            turn = turn == 0 ? 1 : 0;
                        }
                        if(players[0].getCartsNum() == 0){          //finishes game and tells the winner
                            System.out.println("player 2 won");
                            gameDone = true;
                            break;
                        }
                        if(players[1].getCartsNum() == 0) {          //finishes game and tells the winner
                            System.out.println("player 1 won");
                            gameDone = true;
                            break;
                        }
                        //checks that both players can't do anything and finishes game with draw result
                        if(!players[0].canAttack() && players[0].getRemainedHealingTimes() == 0 &&
                                !players[1].canAttack() && players[1].getRemainedHealingTimes() == 0) {
                            System.out.println("draw");
                            gameDone = true;
                            break;
                        }
                    }
                }
            } else {
                break;
            }
            if(gameDone){
                System.out.println("press enter to back to first menu");
                String tmp = input.nextLine();
            }
        }
    }
}
