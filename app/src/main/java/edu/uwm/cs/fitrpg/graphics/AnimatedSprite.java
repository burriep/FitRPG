package edu.uwm.cs.fitrpg.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.Queue;

/**
 * Created by SS Fink on 10/16/2017.
 */

public class AnimatedSprite
{
    ////////////////////////////////////////////////////////////
    // PUBLIC CONSTANTS FOR ANIMATION ROWS
    ////////////////////////////////////////////////////////////
    public static final int ANIMATION_WALK_NORTH = 0;
    public static final int ANIMATION_WALK_EAST = 1;
    public static final int ANIMATION_WALK_SOUTH = 2;
    public static final int ANIMATION_WALK_WEST = 3;
    public static final int ANIMATION_DIE = 4;
    public static final int ANIMATION_ATTACK_NORTH = 5;
    //////////////////////////////////////////////////////////////
    // END CONSTANTS
    //////////////////////////////////////////////////////////////


    private Bitmap animation;
    private Rect spriteRect;

    private ArrayList<Integer> animationQueue;

    private int numFrames, curFrame, spriteHeight, spriteWidth, scale = 1;
    private long timer;
    private float xpos, ypos, animSpeed = 5;
    private float transX = 0, transY = 0;
    private float transSpeed = 1;
    private boolean isLooped;
    private boolean isDisposed;
    private boolean onFinal = false;

    public AnimatedSprite()
    {
        animationQueue = new ArrayList<Integer>();
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

    public float getAnimSpeed(){return this.animSpeed;}

    public void setXpos(float x)
    {
        this.xpos = x;
    }

    public void setYpos(float y)
    {
        this.ypos = y;
    }

    public void setAnimSpeed(float animSpeed)
    {
        this.animSpeed = animSpeed;
    }

    public void setSpriteSheetRow(int y)
    {
        this.spriteRect.top = y*this.spriteHeight;
        this.spriteRect.bottom = y*this.spriteHeight +spriteHeight;

        if(y <= ANIMATION_WALK_WEST) setNumFrames(9);
        else setNumFrames(6);
    }

    public void setFinal(boolean onFinal)
    {
        this.onFinal = onFinal;
    }

    public void setDefaultAnimation(int animInt)
    {
        animationQueue.add(animInt);
        setSpriteSheetRow(animInt);
    }


    private void setNumFrames(int numFrames)
    {
        this.numFrames = numFrames;
    }


    public void triggerAnimation(int animInt)
    {
        animationQueue.add(animInt);
        numFrames = 6;
        curFrame = 0;
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

        if(onFinal && curFrame >= numFrames - 1 && animationQueue.size() <= 2)
        {
            onFinal = onFinal;
            return;

        }
            if (animationQueue.size() >= 2) {
                setSpriteSheetRow(animationQueue.get(1));
                isLooped = false;
            }

            timer += deltaTime;
            if (transX > 0) {
                xpos += deltaTime;
                transX -= deltaTime;
            }

            if (transY > 0) {
                ypos += deltaTime;
                transY -= deltaTime;
            }
            if (timer < animSpeed) return;
            timer = 0;
            curFrame++;
            if (isLooped)
                curFrame = curFrame % numFrames;
            else if (curFrame >= numFrames && !onFinal) isDisposed = true;
            spriteRect.left = curFrame * spriteWidth;
            spriteRect.right = spriteRect.left + spriteWidth;

            if (isDisposed) {
                animationQueue.remove(1);
                setSpriteSheetRow(animationQueue.get(0));
                curFrame = 0;
                numFrames = 9;
                isLooped = true;
                isDisposed = false;
            }
    }

    public void draw(Canvas canvas)
    {
        Rect dest = new Rect((int)getXpos(), (int)getYpos(), (int)getXpos() + spriteWidth * scale, (int)getYpos() + spriteHeight * scale);
        canvas.drawBitmap(animation, spriteRect, dest, null);
    }



}
