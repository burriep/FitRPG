package edu.uwm.cs.fitrpg.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by SS Fink on 10/16/2017.
 */

public class AnimatedSprite
{
    private Bitmap animation;
    private Rect spriteRect;

    private int numFrames, curFrame, spriteHeight, spriteWidth, scale = 1;
    private long timer;
    private float xpos, ypos, animSpeed = 5;
    private float transX = 0, transY = 0;
    private float transSpeed = 1;
    private boolean isLooped;
    private boolean isDisposed;

    public AnimatedSprite()
    {
        spriteRect = new Rect(0,0,0,0);
       timer = 0;
        curFrame = 0;
        xpos = 20;
        ypos = 20;
        isDisposed = false;
    }

    public void init(Bitmap bitmap, int spriteHeight, int spriteWidth, int numFrames, boolean isLooped)
    {
        this.animation = bitmap;
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;
        this.spriteRect.top = 0;
        this.spriteRect.bottom = spriteHeight;
        this.spriteRect.right = spriteWidth;
        this.spriteRect.left = 0;
        this.numFrames = numFrames;
        this.isLooped = isLooped;
    }

    public float getXpos()
    {
        return this.xpos;
    }

    public float getYpos()
    {
        return this.ypos;
    }

    public void setXpos(float x)
    {
        this.xpos = x;
    }

    public void setYpos(float y)
    {
        this.ypos = y;
    }

    ////////////////////////////////////////////////////////////
    //  SPRITE TRANSFORMATIONS
    ////////////////////////////////////////////////////////////

    public void translate(float x, float y, float speed)
    {
        this.transX = x;
        this.transY = y;
        if(transSpeed <= 0) transSpeed = 1;
        this.transSpeed = speed;
    }

    public void setScale(int scale)
    {
        if(scale == 0) return;
        this.scale = scale;
    }
    //////////////////////////////////////////////////////////////


    public void tick(float deltaTime)
    {
        timer += deltaTime;
        if(transX > 0)
        {
            xpos += deltaTime;
            transX -= deltaTime;
        }

        if(transY > 0)
        {
            ypos += deltaTime;
            transY -= deltaTime;
        }
       if(timer < animSpeed * 5) return;
        timer = 0;
        curFrame++;
        if(isLooped)
            curFrame = curFrame % numFrames;
        else if(curFrame >=  numFrames) isDisposed = true;
        spriteRect.left = curFrame*spriteWidth;
        spriteRect.right = spriteRect.left + spriteWidth;

    }

    public void draw(Canvas canvas)
    {


        Rect dest = new Rect((int)getXpos(), (int)getYpos(), (int)getXpos() + spriteWidth * scale, (int)getYpos() + spriteHeight * scale);
        canvas.drawBitmap(animation, spriteRect, dest, null);
    }



}
