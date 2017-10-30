package edu.uwm.cs.fitrpg.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by SS Fink on 10/17/2017.
 */

public class Scene
{
    private ArrayList<AnimatedSprite> spriteList;
    private ArrayList<Particle> particleList;
    private ArrayList<Text> textList;
    private int bgColor = Color.BLACK;

    public Scene()
    {
        spriteList = new ArrayList<AnimatedSprite>();
        particleList = new ArrayList<Particle>();
        textList = new ArrayList<Text>();
    }


    // spawnNewSprite
    // Spawns a new animated sprite at the given location
    // the rest of the params are attributes needed for the
    // AnimatedSprite Class
    public void spawnNewSprite(Bitmap bitmap, int spriteHeight, int spriteWidth, int numFrames, boolean isLooped, int x, int y)
    {
        AnimatedSprite addThis = new AnimatedSprite();
        addThis.init(bitmap, spriteHeight, spriteWidth, numFrames, isLooped);
        addThis.setXpos(x);
        addThis.setYpos(y);
        spriteList.add(addThis);
    }


    public AnimatedSprite getSpriteByIndex(int i)
    {
        return spriteList.get(i);
    }


    // setBackground
    // sets the background of the scene to the
    // specified bitmap image
    public void setBackground(Bitmap bitmap)
    {

    }


    // setBackground
    // Sets the background of the scene to the
    // pass color
    public void setBackground(int color) {
        bgColor = color;
    }


    // generateBackground
    // Generates a background from the passed tilemap
    // needs the size of each tile in pixels
    public void generateBackground(Bitmap bitmap, int tileSize)
    {
        // TODO: Generate a background randomly from a given tilesheet
    }

    public void spawnParticles(int num, float life, float speedx, float speedy, int x, int y, int color, Particle.Behavior behavior)
    {
        for(int i = 0; i < num; ++i)
        {
            Particle addThis = new Particle(life, speedx, speedy, x, y, color, behavior);
            particleList.add(addThis);
        }

    }

    public void writeText(String text, int color, int x, int y, int size, boolean isScrolling)
    {
        Text addThis = new Text(text, x, y, size, color, isScrolling);
        textList.add(addThis);
    }

    public void removeText(Text text)
    {
        textList.remove(text);
    }

    // removeSprite
    // Removes the passed sprite from the ArrayList
    // thusly removing it from the scene
    public void removeSprite(AnimatedSprite kill)
    {
        spriteList.remove(kill);
    }


    public void draw(Canvas c)
    {
        // Draw the Background
        c.drawColor(bgColor);

        // Draw the Sprites
        if(!spriteList.isEmpty())
            for (AnimatedSprite s : spriteList)
            {
                s.draw(c);
            }

        // Draw the Particles
        if(!particleList.isEmpty())
        {
            for (Particle p : particleList)
            {
                p.draw(c);
            }
        }

        // Draw the Text
        if(!textList.isEmpty())
        {
            for (Text t : textList)
            {
                t.draw(c);
            }
        }

    }

    public void tick(float deltaTime)
    {
        // update the animatedSprites, if they exist
        if(!spriteList.isEmpty())
            for (AnimatedSprite s : spriteList)
            {
                s.tick(deltaTime);
            }

        // update the particles, if they exist
        if(!particleList.isEmpty())
        {
            for (Particle p : particleList)
            {
                if(p.getCurLife() <= 0) particleList.remove(p);
                p.tick(deltaTime);

            }
        }

        // update the text
        if(!textList.isEmpty())
        {
            for (Text t : textList)
            {
                t.tick(deltaTime);
            }
        }
    }







}
