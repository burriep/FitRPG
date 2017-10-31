package edu.uwm.cs.fitrpg;

/**
 * Created by Patrick Seaton on 10/27/2017.
 */

public class CombatUnit {

    private int stamina;
    private int strength;
    private int endurance;
    private int dexterity;
    private int speed;

    private int currentHP;
    private double staminaToHpConversion = 3;

    public int GetStamina(){return stamina;}
    public int GetStrength(){return strength;}
    public int GetEndurance(){return endurance;}
    public int GetDexterity(){return dexterity;}
    public int GetSpeed(){return speed;}

    public int GetCurrentHP(){return currentHP;}
    public int GetMaxHP(){return (int)(stamina * staminaToHpConversion);}

    public CombatUnit(int initStamina, int initStrength, int initEndurance, int initDexterity, int initSpeed)
    {
        stamina = initStamina;
        strength = initStrength;
        endurance = initEndurance;
        dexterity = initDexterity;
        speed = initSpeed;

        currentHP = (int)(stamina * staminaToHpConversion);
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

    public void Attack(CombatUnit target)
    {
        target.Damage((strength * 2) - target.endurance);
    }

    public void Damage(int amount)
    {
        currentHP -= amount;
        if(currentHP < 0)
        {
            currentHP = 0;
        }
    }
}
