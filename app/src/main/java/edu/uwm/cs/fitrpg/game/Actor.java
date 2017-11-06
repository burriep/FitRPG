package edu.uwm.cs.fitrpg.game;

import android.graphics.Canvas;

import edu.uwm.cs.fitrpg.graphics.AnimatedSprite;

/**
 * Created by SS Fink on 10/31/2017.
 */

public class Actor
{
    private AnimatedSprite sprite;
    private int xpos, ypos, curHealth, maxHealth, strength, agility;

    public Actor(AnimatedSprite sprite, int xpos, int ypos, int health, int strength, int agility)
    {
        this.sprite = sprite;
        this.xpos = xpos;
        sprite.setXpos(xpos);
        this.ypos = ypos;
        sprite.setYpos(ypos);
        this.curHealth = health;
        this.maxHealth = health;
        this.strength = strength;
        this.agility = agility;
    }

    public AnimatedSprite getSprite() {return this.sprite;}
    public  int getXpos() { return this.xpos;}
    public  int getYpos() { return this.ypos;}
    public  int getCurHealth() { return  this.curHealth;}
    public  int getMaxHealth() {return this.maxHealth;}
    public  int getStrength() {return this.strength;}
    public  int getAgility() {return this.agility;}

    public void  setXpos(int xpos)
    {
        this.xpos = xpos;
        sprite.setXpos(xpos);
    }
    public void  setYpos(int ypos)
    {
        this.ypos = ypos;
        sprite.setYpos(ypos);
    }
    public void  setCurHealth(int curHealth) {this.curHealth = curHealth;}
    public void  setMaxHealth(int maxHealth) {this.maxHealth = maxHealth;}
    public void  setStrength(int strength) {this.strength = strength;}
    public void  setAgility(int agility) {this.agility = agility;}


    public void tick(float deltaTime)
    {
        float animSpeed = curHealth / maxHealth;
        sprite.setAnimSpeed(sprite.getAnimSpeed() * animSpeed);
        sprite.tick(deltaTime);
    }

    public void draw(Canvas canvas)
    {
        sprite.draw(canvas);
    }


}
