package edu.uwm.cs.fitrpg.graphics;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.provider.Settings;

/**
 * Created by SS Fink on 10/17/2017.
 */

public class Text
{

    public enum Behavior {SCROLLING, RISING, FULLTEXT}

    private Behavior behavior;
    private float size;
    private int color;
    private String fullText;
    private String displayText;
    private int i = 1;
    private int xpos, ypos;
    private int speed  = 5;
    private long timer = 0;
    Paint paint;

    public Text(String text, int x, int y, float size, int color, Behavior behavior)
    {
        paint = new Paint();
        this.fullText = new String(text);
        this.behavior = behavior;
        if(this.behavior!=Behavior.SCROLLING) displayText = new String(text);
        else displayText = new String();
        this.color = color;
        this.size = size;
        this.xpos = x;
        this.ypos = y;

    }

    public void setSpeed(int speed)
    {
        this.speed = speed;
    }

    public int getYpos() {return this.ypos;}
    public int getXpos() {return this.xpos;}


    public void tick(float deltaTime)
    {
        timer += deltaTime;
        if (timer < 5 * speed) return;
        switch (behavior) {
            case SCROLLING:
            {
                if (fullText.equals(displayText)) return;
                if (i <= fullText.length()) {
                    displayText = fullText.substring(0, i++);
                    timer = 0;
                }
                break;
            }
            case RISING:
            {
                this.ypos -= speed;
                timer = 0;
                break;
            }
            default:
                break;
        }
    }

    public void draw(Canvas canvas)
    {
        //canvas.drawPaint(paint);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(displayText, xpos, ypos, paint);
    }

}
