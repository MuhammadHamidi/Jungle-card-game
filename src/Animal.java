import java.util.Random;
import java.util.Scanner;

/**
 * <h1>Making Cart</h1>
 * Making an object from this class provides an animal cart to use it in game.
 * avery object from this class contains animals name , energy , health and
 * some variables about existence or strength of animal's different damages.
 *
 * @author mohammad hamidi
 */
public class Animal {
    private String name;
    private boolean hasNormalDamage;
    private int normalDamage;
    private boolean hasHardDamage;
    private int hardDamage;
    private double energy;
    private final double maxEnergy;
    private double health;

    Random random = new Random();

    /**
     * this is a manual constructor that receives all of non-boolean animal's fields
     * and makes an animal with them.
     * @param name animal's name
     * @param normalDamage strength of animal's normalDamage
     * @param hardDamage strength of animal's hardDamage
     * @param energy animal's energy
     * @param health animal's health
     */
    public Animal(String name , int normalDamage , int hardDamage , double energy , double health){
        this.name = name;
        this.normalDamage = normalDamage;
        hasNormalDamage = normalDamage != 0;
        this.hardDamage = hardDamage;
        hasHardDamage = hardDamage != 0;
        this.energy = energy;
        maxEnergy = energy;
        this.health = health;
    }

    /**
     * this is a manual to string of object that returns animal's information
     * @return to string of object
     */
    @Override
    public String toString() {
        return  name + " , normalDamage: " + normalDamage + " , hardDamage: " + hardDamage +
                " , energy: " + energy + " , health: " + health;
    }

    /**
     * this method tells that animal needs to be healed or not.
     * if it needs method returns true , else returns false.
     * @return true if animal needs to be healed , else false
     */
    public boolean needsHealing(){
        if(energy != maxEnergy)
            return true;
        return false;
    }

    /**
     * this method heals cart and assigns max energy to energy.
     * this method has no return and no parameter.
     */
    public void healCart(){
        energy = maxEnergy;
    }

    /**
     * this method gets a damage type from user and returns it to set on attackers hashmap.
     * if only one damage type is active for animal it will be returned automatically.
     * @return a damage type to set on attackers hashmap
     */
    public int chooseDamageType(){
        if(!hasNormalDamage){
            System.out.println("hard damage is selected because this animal only has hard damage");
            return 2;
        }
        else if(!hasHardDamage){
            System.out.println("normal damage is selected because this animal only has normal damage");
            return 1;
        }
        else {
            System.out.println("choose type of damage:     1. normal     2. hard");
            Scanner input = new Scanner(System.in);
            String type = input.nextLine();
            while (!type.equals("1") && !type.equals("2")){
                System.out.println("invalid input. try again");
                type = input.nextLine();
            }
            if(type.equals("1"))
                return 1;
            else
                return 2;
        }
    }

    /**
     * this method choose a damage type randomly and returns it to set on attackers hashmap.
     * if only one damage type is active for animal it will be returned automatically.
     * @return a damage type to set on attackers hashmap
     */
    public int chooseDamageTypeRobot(){
        if(!hasNormalDamage){
            return 2;
        }
        else if(!hasHardDamage){
            return 1;
        }
        else {
            int rand = random.nextInt(2) + 1;
            return rand;
        }
    }

    /**
     * this method receives a type of damage and returns desired damage amount.
     * here 1 refers to normal damage and 2 refers to hard damage.
     * @param type type of damage
     * @return desired damage amount
     */
    public int getDamage(int type){
        if(type == 1)
            return normalDamage;
        else
            return hardDamage;
    }

    /**
     * this method receives damage amount for attacks and decreases the health
     * as much as damage amount. then returns carts being alive .if health was
     * bigger than 0 method returns true , else returns false.
     * @param damage damage amount for attacks
     * @return true if health was bigger than 0 , else false
     */
    public boolean setHealthAfterDamage(double damage){
        health -= damage ;
        if(health > 0)
            return true;
        else
            return false;
    }

    /**
     * this method receives amount of energy cost and decreases the energy
     * as much as this cost.
     * @param decreaseEnergy amount of energy cost
     */
    public void decreaseEnergy(double decreaseEnergy){
        energy -= decreaseEnergy;
    }

    /**
     * this is getter method of energy and returns animal's energy
     * @return animal's energy
     */
    public double getEnergy() {
        return energy;
    }

    /**
     * this is getter method of normal damage and returns animal's normal damage strength
     * @return animal's normal damage strength
     */
    public int getNormalDamage() {
        return normalDamage;
    }

    /**
     * this is getter method of name and returns animal's name
     * @return animal's name
     */
    public String getName() {
        return name;
    }
}
