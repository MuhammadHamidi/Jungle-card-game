import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

enum types { Human , Robot}

/**
 * <h1>Making patient</h1>
 * Making an object from this class provides a player to use it in game class.
 * avery object from this class contains player's type , carts and
 * his remained healing times.
 *
 * @author mohammad hamidi
 */
public class Player {
    private types type;
    private ArrayList<Animal> carts = new ArrayList<>();
    private int remainedHealingTimes = 3;

    /**
     * this is a manual constructor that receives player's type
     * and makes a player with that.
     * @param type type of player
     */
    public Player(types type){
        this.type = type;
    }

    /**
     * this method receives an animal and adds it to our carts.
     * @param animal animal to add
     */
    public void addCart(Animal animal){
        carts.add(animal);
    }

    /**
     * this method receives an animal and checks that it's allowed to add or not.
     * max allowed amount of any cart is 5. so method returns false if 5 cart from
     * this animal already exist in carts else returns true.
     * @param animal animal to check
     * @return false if 5 cart from animal already exist , else true
     */
    public boolean allowedCart(Animal animal){
        int counter = 0;
        for (int i=0 ; i < carts.size() ; i++) {
            if(carts.get(i).getName().equals(animal.getName())) {
                counter++;
            }
            if(counter == 5) {
                return false;
            }
        }
        return true;
    }

    /**
     * this method prints name of player's first 30 carts.
     * this method has no return and no parameter.
     */
    public void printCartsNames(){
        int counter = 1;
        for (int i = 0; i < (carts.size()/5); i++) {
            for (int j = 0; j < 5; j++) {
                System.out.print(counter + ". " + carts.get(counter-1).getName() + "\t");
                counter++;
            }
            System.out.println();
        }
    }

    /**
     * this method prints all of player's carts with their information.
     * this method has no return and no parameter.
     */
    public void printCarts(){
        for (int i = 0; i < carts.size(); i++) {
            System.out.println("\t" + (i+1) + ". " + carts.get(i).toString());
        }
    }

    /**
     * this method receives a string[] of indexes that player chose them
     * and wants to start the game with these carts. method makes an arraylist
     * from carts with these indexes and assigns it to carts field.
     * @param carts string[] of indexes that player chose them
     */
    public void setCartsHuman(String[] carts) {
        int[] indexes = new int[10];
        for (int i=0 ; i<10 ; i++) {
            indexes[i] = Integer.parseInt(carts[i]) - 1;
        }
        ArrayList<Animal> newCarts = new ArrayList<>();
        for (int i=0 ; i<indexes.length ; i++) {
            newCarts.add(this.carts.get(indexes[i]));
        }
        this.carts = newCarts;
    }

    /**
     * this method receives a hashset of indexes that are randomly selected.
     * method makes an arraylist from carts with these indexes and assigns it to carts field.
     * @param indexes hashset of indexes that are randomly selected
     */
    public void setCartsRobot(HashSet<Integer> indexes){
        ArrayList<Animal> newCarts = new ArrayList<>();
        for (Integer integer : indexes) {
            newCarts.add(this.carts.get(integer));
        }
        this.carts = newCarts;
    }

    /**
     * this method returns a hashset from indexes of carts that needs to be healed.
     * @return hashset of need-healing carts' index
     */
    public HashSet<Integer> needHealing(){
        HashSet<Integer> need = new HashSet<>();
        for (int i=0 ; i < carts.size() ; i++) {
            if(carts.get(i).needsHealing())
                need.add(i);
        }
        return need;
    }

    /**
     * this method receives index of a cart and heals the cart.
     * @param index index of cart
     */
    public void healCart(int index){
        carts.get(index).healCart();
        System.out.println("cart ( " + carts.get(index).getName() + " with index " + (index+1) + " ) healed");
        remainedHealingTimes--;
    }

    /**
     * this method receives attackers hashmap and index of a new attacker.
     * after getting damage type from user , new attacker and damage type
     * will be added to the attackers hashmap.
     * @param attackers attackers hashmap
     * @param index index of new attacker
     */
    public void chooseDamageType(HashMap<Integer , Integer> attackers , int index){
        int type = carts.get(index).chooseDamageType();
        attackers.put(index , type);
    }

    /**
     * this method receives attackers hashmap and index of a new attacker.
     * after choosing damage type randomly , new attacker and damage type
     * will be added to the attackers hashmap.
     * @param attackers attackers hashmap
     * @param index index of a new attacker
     */
    public void chooseDamageTypeRobot(HashMap<Integer , Integer> attackers , int index){
        int type = carts.get(index).chooseDamageTypeRobot();
        attackers.put(index , type);
    }

    /**
     * this method receives attackers hashmap and checks that attackers
     * have enough energy for this attack or not. if even one of them
     * doesn't have enough energy method returns false , else returns true.
     * @param attackers attackers hashmap
     * @return true if even 1 cart doesn't have enough energy and false if every cart has enough energy
     */
    public boolean enoughEnergy(HashMap<Integer , Integer> attackers){
        double damage = sumOfAttackDamage(attackers);
        int num = attackers.keySet().size();
        damage = damage / num;
        for (Integer integer : attackers.keySet()) {
            if(carts.get(integer).getEnergy() < damage){
                return false;
            }
        }
        return true;
    }

    /**
     * this method receives attackers hashmap and calculates
     * amount of damage that target is going to suffer and returns it.
     * @param attackers attackers hashmap
     * @return amount of damage that target is going to suffer
     */
    public double sumOfAttackDamage(HashMap<Integer , Integer> attackers){
        double damage = 0;
        for (Integer integer : attackers.keySet()) {;
            damage += carts.get(integer).getDamage(attackers.get(integer));
        }
        return damage;
    }

    /**
     * this method receives attackers hashmap and decreases energy of
     * any attacker as much as average of attackers' damage amount.
     * @param attackers attackers hashmap
     */
    public void decreaseEnergy(HashMap<Integer , Integer> attackers){
        double damage = sumOfAttackDamage(attackers);
        int num = attackers.keySet().size();
        double energyCost = damage / num;
        for (Integer integer : attackers.keySet()) {
            carts.get(integer).decreaseEnergy(energyCost);
        }
    }

    /**
     * this method receives amount of damage and index of target cart for attacks.
     * then increases cart's health as much as damage amount and prints if cart is
     * still alive or not.
     * @param damage amount of damage
     * @param index index of target cart
     */
    public void damage(double damage , int index){
        boolean alive = carts.get(index).setHealthAfterDamage(damage);
        if(!alive){
            System.out.println("target ( " + carts.get(index).getName() + " with index " + (index+1) + " ) died");
            carts.remove(index);
        }
        else {
            System.out.println("target ( " + carts.get(index).getName() + " with index " + (index+1) + " ) damaged");
        }
    }

    /**
     * this method checks if any cart have enough energy to attend in attacks or not.
     * if even 1 cart can attack method returns true , else returns false.
     * @return true if even 1 cart can attack and false if no cart can't attack
     */
    public boolean canAttack(){
        for (int i = 0; i < carts.size(); i++) {
            if(carts.get(i).getEnergy() > carts.get(i).getNormalDamage())
                return true;
        }
        return false;
    }

    /**
     * this is getter method of num of alive carts
     * @return num of alive carts
     */
    public int getCartsNum(){
        return carts.size();
    }

    /**
     * this is getter method of remained healing times and returns
     * remained times that player can heal one of his carts
     * @return remained healing times
     */
    public int getRemainedHealingTimes() {
        return remainedHealingTimes;
    }
}
