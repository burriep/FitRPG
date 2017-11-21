package edu.uwm.cs.fitrpg.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.provider.Settings;

/**
 * Created by SS Fink on 11/20/2017.
 */

public class CooldownBar
{

    private int xpos, ypos;
    private int width, height;
    private long maxCooldownTime;
    private long curCooldownTime;
    private boolean isReady;
    private Paint paint;
    private int angle = 0;
    private int cooldownColor = Color.parseColor("#A9A9A9");
    private int offCooldownColor = Color.BLUE;


    public CooldownBar(int x, int y)
    {
        paint = new Paint();
        paint.setColor(cooldownColor);
        width = 500;
        height = 500;
        xpos = x - width / 2;
        ypos = y - height / 2;

        isReady = false;
        maxCooldownTime = 500;
        curCooldownTime = 0;
    }

    public boolean isOffCooldown()
    {
        return isReady;
    }

    public void reset()
    {
        if (isReady)
        {
            isReady = false;
            paint.setColor(cooldownColor);
            curCooldownTime = 0;

        }
    }

    public void tick(float deltaTime)
    {
        curCooldownTime += deltaTime;

        if(curCooldownTime >= maxCooldownTime)
        {
            curCooldownTime = maxCooldownTime;
            isReady = true;
            paint.setColor(offCooldownColor);
        }

        float ratio = ((float)curCooldownTime / (float)maxCooldownTime);

        angle = (int)Math.floor(ratio * 360);
    }

    public void draw(Canvas c)
    {
        c.drawArc(xpos, ypos, width + xpos, height + ypos, 0, angle, true, paint);
    }

}
