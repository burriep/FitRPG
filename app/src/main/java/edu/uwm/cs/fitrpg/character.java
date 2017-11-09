package edu.uwm.cs.fitrpg;
import edu.uwm.cs.fitrpg.DatabaseHelper;

/**
 * Created by Jason on 11/8/17.
 */

public class character
{
    private int id;
    private String name;
    private int currentNode;
    private int currentHealth;
    private int strength;
    private int speed;
    private int dexterity;
    private int stamina;
    private int endurance;

    private DatabaseHelper db;

    //constructor for player - the player's usr_id will always be 1
    public void character()
    {
        this.id = 1;
        this.currentHealth = 0;
        this.name = "";
        this.currentNode = 0;
        this.strength = 0;
        this.speed = 0;
        this.dexterity = 0;
        this.stamina = 0;
        this.endurance = 0;
    }

    //constructor for enemies - use UNIQUE id's for new enemies, otherwise db constraint violation
    public void character(int x)
    {
        this.id = x;
        this.currentHealth = 0;
        this.name = "";
        this.currentNode = 0;
        this.strength = 0;
        this.speed = 0;
        this.dexterity = 0;
        this.stamina = 0;
        this.endurance = 0;
    }

    //this method will pull the character's stat-set from the db
    public void dbPull()
    {

        this.name = db.getName(this.id);
        this.strength = Integer.parseInt(db.getStrength(this.id));
        this.stamina = Integer.parseInt(db.getStamina(this.id));
        this.endurance = Integer.parseInt(db.getEndurance(this.id));
        this.speed = Integer.parseInt(db.getSpeed(this.id));
        this.dexterity = Integer.parseInt(db.getDexterity(this.id));

    }

    //this method will push the character's current stat-set to the db
    public void dbPush(int x)
    {

        db.setStrength(this.strength, this.id);
        db.setStamina(this.stamina,this.id);
        db.setSpeed(this.speed, this.id);
        db.setEndurance(this.endurance, this.id);
        db.setDexterity(this.dexterity, this.id);
        db.setCurrentNode(this.currentNode, this.id);
    }
}

