package edu.uwm.cs.fitrpg.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by SS Fink on 10/17/2017.
 */

public class Particle {

    private float curLife;
    private int x, y;
    private float speedX, speedY;
    private int color;
    private Behavior behavior;
    private Paint paint;
    public enum Behavior {RANDOM};

    public Particle(float life, float speedX, float speedY, int x, int y, int color, Behavior behavior)
    {
        this.curLife = life;
        this.speedX = speedX;
        this.speedY = speedY;
        this.x = x;
        this.y = y;
        this.color = color;
        this.behavior = behavior;
        this.paint = new Paint();
        paint.setColor(this.color);
        paint.setStyle((Paint.Style.FILL));
    }

    public float getCurLife()
    { return this.curLife;}



    public void tick(float deltaTime)
    {
        // Decrement the current life
        curLife -= deltaTime;
        // Advance the particle on the path based on behavior
        switch(behavior)
        {
            case RANDOM:
                break;
            default:
                // Make the particle move based on the axis speeds
                this.x += speedX;
                this.y += speedY;
                break;
        }

    }

    public void draw(Canvas canvas)
    {
        canvas.drawRect(x, y, x+1, y+1, paint);
    }

}
