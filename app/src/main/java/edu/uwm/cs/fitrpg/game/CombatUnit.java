package edu.uwm.cs.fitrpg.game;

import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.Random;

import edu.uwm.cs.fitrpg.graphics.AnimatedSprite;
import edu.uwm.cs.fitrpg.graphics.Healthbar;

/**
 * Created by Patrick Seaton on 10/27/2017.
 */

public class CombatUnit {

    private double staminaToHpConversion = 3;   //PS How much extra HP one point of Stamina is worth (I.E if Base is 20, and this is 3, and Stamina is 10, max HP is 50)
    private int baseHitChance = 80;             //PS If at even dexterity, how much chance an attack has to hit out of 100
    private int lowestHitChance = 33;           //PS The lowest the hit chance can possibly be out of 100
    private int strengthWeightVsEndurance = 2;  //PS The multiplier for strength before the target's endurance is taken into account

    private int stamina;
    private int strength;
    private int endurance;
    private int dexterity;
    private int speed;

    private int currentHP;
    private Random rand;

    // GRAPHICS VARIABLES
    private Healthbar healthbar;
    private AnimatedSprite sprite;
    private int x, y;
    private boolean hbEnabled = true;


    public int GetStamina(){return stamina;}
    public void SetStamina(int val){stamina = val;}
    public int GetStrength(){return strength;}
    public void SetStrength(int val){strength = val;}
    public int GetEndurance(){return endurance;}
    public void SetEndurance(int val){endurance = val;}
    public int GetDexterity(){return dexterity;}
    public void SetDexterity(int val){dexterity = val;}
    public int GetSpeed(){return speed;}
    public void SetSpeed(int val){speed = val;}
    public int getx(){return this.x;}
    public int getY(){return this.y;}


    public AnimatedSprite getSprite() {return sprite;}
    public Healthbar getHealthbar() {return healthbar;}

    public int GetCurrentHP(){return currentHP;}
    public int GetMaxHP(){return (int)(stamina * staminaToHpConversion);}

    public CombatUnit(int initStamina, int initStrength, int initEndurance, int initDexterity, int initSpeed, int x, int y)
    {
        stamina = initStamina;
        strength = initStrength;
        endurance = initEndurance;
        dexterity = initDexterity;
        speed = initSpeed;

        currentHP = (int)(stamina * staminaToHpConversion);


        this.x = x;
        this.y = y;


        healthbar = new Healthbar(x, y, GetMaxHP(), GetCurrentHP());

        rand = new Random();
    }

    public CombatUnit(int initStamina, int initStrength, int initEndurance, int initDexterity, int initSpeed)
    {
        stamina = initStamina;
        strength = initStrength;
        endurance = initEndurance;
        dexterity = initDexterity;
        speed = initSpeed;

        currentHP = (int)(stamina * staminaToHpConversion);



        rand = new Random();
    }

    public void Init(int initStamina, int initStrength, int initEndurance, int initDexterity, int initSpeed)
    {
        stamina = initStamina;
        strength = initStrength;
        endurance = initEndurance;
        dexterity = initDexterity;
        speed = initSpeed;

        currentHP = (int)(stamina * staminaToHpConversion);
    }

    public void initializeGraphics(Bitmap bm, int animDir)
    {
        sprite = new AnimatedSprite();
        sprite.init(bm, 64,64,9,true);
        sprite.setDefaultAnimation(animDir);
        sprite.setXpos(this.x);
        sprite.setYpos(this.y);

        healthbar = new Healthbar(x, y, GetMaxHP(), GetCurrentHP());
    }

    public int Attack(CombatUnit target)
    {
        int damageAmount = (strength * strengthWeightVsEndurance) - target.endurance;
        if(damageAmount < 1)
        {
            damageAmount = 1;
        }
        int hitChance = baseHitChance - (target.dexterity - dexterity);
        if(hitChance < lowestHitChance)
        {
            hitChance = lowestHitChance;
        }
        if(rand.nextInt(100) > hitChance)
        {
            damageAmount = 0;
        }
        target.Damage(damageAmount);
        return damageAmount;
    }

    public void Damage(int amount)
    {
        if(currentHP - amount <= 0)
        {
            healthbar.takeDamage(currentHP);
            currentHP -= currentHP;
        }
        else
        {
            currentHP -= amount;
            healthbar.takeDamage(amount);
        }
    }

    public void setHbEnabled(boolean bool)
    {
        hbEnabled = bool;
    }



    public void draw(Canvas c)
    {
        sprite.draw(c);
        if(hbEnabled)
            healthbar.draw(c);

    }

    public void tick(float deltaTime)
    {
        sprite.tick(deltaTime);
        healthbar.tick(deltaTime);

    }

}
