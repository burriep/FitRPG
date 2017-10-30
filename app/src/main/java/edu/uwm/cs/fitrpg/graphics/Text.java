package edu.uwm.cs.fitrpg.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.Settings;

/**
 * Created by SS Fink on 10/17/2017.
 */

public class Text
{
    private boolean isScrolling;
    private float size;
    private int color;
    private String fullText;
    private String displayText;
    private int i = 1;
    private int xpos, ypos;
    private int speed  = 5;
    private long timer = 0;
    Paint paint;

    public Text(String text, int x, int y, float size, int color, boolean isScrolling)
    {
        paint = new Paint();
        this.fullText = new String(text);
        displayText = new String();
        this.color = color;
        this.size = size;
        this.xpos = x;
        this.ypos = y;
        this.isScrolling = isScrolling;
    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }


    public void tick(float deltaTime)
    {
        if(!isScrolling)
        {
            displayText = fullText;
            return;
        }
        if(fullText.equals(displayText)) return;
        if(i < fullText.length()) {
            timer += deltaTime;
            if(timer < 5*speed) return;

            displayText = fullText.substring(0, i++);
            timer = 0;
        }
    }

    public void draw(Canvas canvas)
    {
        //canvas.drawPaint(paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setTextSize(size);
        canvas.drawText(displayText, xpos, ypos, paint);
    }

}
