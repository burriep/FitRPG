package edu.uwm.cs.fitrpg.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by SS Fink on 11/14/2017.
 */

public class Healthbar
{
    private int xpos, ypos;
    private int curHealth, maxHealth, deltaHealth;
    private int deltaSpeed;
    private int timer = 0;
    private int width = 500;
    private int height = 100;

    private int color = Color.GREEN;
    private Text healthText;
    private Paint paint;

    public Healthbar(int xpos, int ypos, int maxHealth, int curHealth)
    {
        this.xpos = xpos;
        this.ypos = ypos;
        this.maxHealth = maxHealth;
        this.curHealth = curHealth;
        this.deltaHealth = 0;
        this.deltaSpeed = 0;
        paint = new Paint();
    }

    public void setX(int x){this.xpos = x - width / 2;}
    public void setY(int y){this.ypos = y - height / 2;}

    public Healthbar(int xpos, int ypos, int maxHealth)
    {
        this.xpos = xpos;
        this.ypos = ypos;
        this.maxHealth = maxHealth;
        this.curHealth = maxHealth;
        this.deltaHealth = 0;
        this.deltaSpeed = 0;
        paint = new Paint();
    }

    public void takeDamage(int dmg)
    {
        this.deltaHealth += dmg;
    }

    public void tick(float deltaTime)
    {
        if(deltaHealth == 0) return;
        timer += deltaTime;


        // Calculate the appropriate speed health bar should decrease
        deltaSpeed = (curHealth / deltaHealth);
        if(timer < deltaSpeed) return;


        curHealth--;
        deltaHealth--;

        float curToMaxRatio = (float)curHealth / (float)maxHealth;

        if(curToMaxRatio > 0.67) color = Color.GREEN;
        else if(curToMaxRatio > 0.33) color = Color.YELLOW;
        else color = Color.RED;

    }

    public void draw(Canvas c)
    {
        // Draw the background rect
        paint.setColor(Color.BLACK);
        c.drawRect(xpos,ypos, xpos+width, ypos+height,paint);
        // Draw the "health" rect
        paint.setColor(color);
        c.drawRect(xpos + (width / 5),ypos + (height / 4), xpos+ (width / 5) + ((float)(width * 0.75) * ((float)curHealth / (float)maxHealth)), ypos+(float)(height * 0.75),paint);
        // Draw the text
        healthText = new Text(Integer.toString(curHealth), xpos + width / 10, (int)(ypos + height *0.70), 50, Color.WHITE, Text.Behavior.FULLTEXT);
        healthText.draw(c);

    }


}
