package edu.uwm.cs.fitrpg;

import edu.uwm.cs.fitrpg.view.HomeScreen;

/**
 * Created by Jason on 11/8/17.
 */

public class RpgChar
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
    public RpgChar()
    {
        //grab existing player data
        try
        {
            this.id = 1;
            dbPull();
        }
        //Main player doesn't exist in database - create new
        catch(Exception e)
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
            this.db = new DatabaseHelper(HomeScreen.appCon);
            dbPush();
        }
    }

    //constructor for enemies - use UNIQUE id's for new enemies, otherwise db constraint violation
    public RpgChar(int x)
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
        this.db = new DatabaseHelper(HomeScreen.appCon);
    }

    //this method will pull the RpgChar's stat-set from the db
    public void dbPull()
    {
        try{
            this.name = db.getName(this.id);
            this.strength = Integer.parseInt(db.getStrength(this.id));
            this.stamina = Integer.parseInt(db.getStamina(this.id));
            this.endurance = Integer.parseInt(db.getEndurance(this.id));
            this.speed = Integer.parseInt(db.getSpeed(this.id));
            this.dexterity = Integer.parseInt(db.getDexterity(this.id));
        }
        catch(Exception e)
        {
            System.out.println("Could not pull data from db");
        }

    }

    //this method will push the RpgChar's current stat-set to the db
    public void dbPush()
    {
        try
        {
            db.getReadableDatabase();
            db.setStrength(this.strength, this.id);
            db.setStamina(this.stamina,this.id);
            db.setSpeed(this.speed, this.id);
            db.setEndurance(this.endurance, this.id);
            db.setDexterity(this.dexterity, this.id);
            db.setCurrentNode(this.currentNode, this.id);
        }
        catch(Exception e)
        {
            System.out.println("Could not Push to DB");
        }
    }

    /*//////////////////////////////////     GETTERS AND SETTERS   /////////////////////////////////////*/
    public void setStrength(int x)
    {

        this.strength = x;
    }

    public void setEndurance(int x)
    {

        this.endurance = x;
    }

    public void setSpeed(int x)
    {

        this.speed = x;
    }

    public void setDexterity(int x)
    {

        this.dexterity = x;
    }

    public void setStamina(int x)
    {

        this.stamina = x;
    }

    public void setHealth(int x)
    {

        this.currentHealth = x;
    }

    public void setName(String x)
    {

        this.name = x;
    }

    public void setCurrentNode(int x)
    {
        this.currentNode = x;
    }

    public int getStrength()
    {

        return this.strength ;
    }

    public int getEndurance()
    {

        return this.endurance ;
    }

    public int getSpeed()
    {

        return this.speed;
    }

    public int getDexterity()
    {

        return this.dexterity;
    }

    public int getStamina()
    {

        return this.stamina;
    }

    public int getHealth()
    {

        return this.currentHealth;
    }

    public String getNme()
    {
        return this.name;
    }

    public int getCurrentNode()
    {
        return this.currentNode;
    }

    public int getId()
    {
        return this.id;
    }

    /*/////////////////////////////////////////////////////////////////////////////////////////////*/

    //just for quick testing
    public void createChar()
    {

        try
        {
            this.db.createChar(this.id, this.currentNode, this.name, this. strength, this.speed, this.dexterity, this.endurance, this.stamina);
        }
        catch(Exception e)
        {
            System.out.println("Failed to Create Character - DB entry may already exist");
        }
    }
}

