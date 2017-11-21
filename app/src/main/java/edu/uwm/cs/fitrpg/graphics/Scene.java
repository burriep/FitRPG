package edu.uwm.cs.fitrpg.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.MessagePattern;
import android.util.DisplayMetrics;

import java.util.ArrayList;

import edu.uwm.cs.fitrpg.Audio.SFXPlayer;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.game.CombatUnit;

import static edu.uwm.cs.fitrpg.R.*;

/**
 * Created by SS Fink on 10/17/2017.
 */

public class Scene
{
    private enum State {IN_COMBAT, VICTORY, DEFEAT};

    private State curState;
    Resources res;
    private ArrayList<AnimatedSprite> spriteList;
    private ArrayList<Particle> particleList;
    private ArrayList<CombatUnit> combatUnitList;
    private ArrayList<Text> textList;
    private int bgColor = Color.BLACK;
    private BitmapDrawable bmDraw = null;
    private CooldownBar cdbar;

    public Scene(Resources res)
    {
        spriteList = new ArrayList<AnimatedSprite>();
        particleList = new ArrayList<Particle>();
        textList = new ArrayList<Text>();
        combatUnitList = new ArrayList<CombatUnit>(2);
        cdbar = new CooldownBar(res.getDisplayMetrics().widthPixels / 2, res.getDisplayMetrics().heightPixels/2 );
        curState = State.IN_COMBAT;
        this.res = res;
    }

    public void spawnPlayerCombatUnit(Bitmap bm, int initStamina, int initStrength, int initEndurance, int initDexterity, int initSpeed)
    {
        CombatUnit addThis = new CombatUnit(initStamina, initStrength, initEndurance, initDexterity, initSpeed, (res.getDisplayMetrics().widthPixels / 2) - (32*6), (res.getDisplayMetrics().heightPixels/ 8) * 5);
        addThis.initializeGraphics(bm, AnimatedSprite.ANIMATION_WALK_NORTH);
        addThis.getSprite().setScale(6);
        addThis.getHealthbar().setX(addThis.getx() + (32*6));
        addThis.getHealthbar().setY(addThis.getY() + (64*7));
        combatUnitList.add(0,addThis);

    }

    public void spawnEnemyCombatUnit(Bitmap bm, int initStamina, int initStrength, int initEndurance, int initDexterity, int initSpeed)
    {
        CombatUnit addThis = new CombatUnit(initStamina, initStrength, initEndurance, initDexterity, initSpeed,(res.getDisplayMetrics().widthPixels / 2) - (32*6), (res.getDisplayMetrics().heightPixels/ 8));
        addThis.initializeGraphics(bm, AnimatedSprite.ANIMATION_WALK_SOUTH);
        addThis.getSprite().setScale(6);
        addThis.getHealthbar().setX(addThis.getx() + (32*6));
        addThis.getHealthbar().setY(addThis.getY());
        combatUnitList.add(addThis);

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
    public void setBackground(int resID, int width, int height)
    {
        Bitmap bm = BitmapFactory.decodeResource(res, resID);
        bm = Bitmap.createScaledBitmap(bm, width, height,false);

        bmDraw = new BitmapDrawable(res, bm);
        bmDraw.setTileModeX(Shader.TileMode.REPEAT);
        bmDraw.setTileModeY(Shader.TileMode.REPEAT);
        bmDraw.setBounds(0,0, res.getSystem().getDisplayMetrics().widthPixels, res.getSystem().getDisplayMetrics().heightPixels);
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

    public void writeText(String text, int color, int x, int y, int size, Text.Behavior behavior)
    {
        Text addThis = new Text(text, x, y, size, color, behavior);
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
        if(bmDraw != null){ bmDraw.draw(c);}

        if(curState == State.IN_COMBAT)
            cdbar.draw(c);
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

        if(!combatUnitList.isEmpty())
            for (CombatUnit cu : combatUnitList)
            {
                cu.draw(c);
            }


    }

    public void tick(float deltaTime)
    {

        ArrayList<Particle> killThese = new ArrayList<Particle>();
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
                if(p.getCurLife() <= 0) killThese.add(p);
                else p.tick(deltaTime);
            }

            // remove the marked particles
            for (Particle p: killThese)
            {
                particleList.remove(p);
            }
        }

        // update the text
        if(!textList.isEmpty())
        {
            for (Text t : textList)
            {
                t.tick(deltaTime);
                if(t.getYpos() < 0 || t.getXpos() < 0) textList.remove(t);
            }
        }

        if(!combatUnitList.isEmpty())
            for (CombatUnit cu : combatUnitList)
            {
                cu.tick(deltaTime);
            }


        cdbar.tick(deltaTime);
    }

    public void handleClick(float x, float y, SFXPlayer sfxPlayer)
    {
        if(cdbar.isOffCooldown())
        {
            combatUnitList.get(0).getSprite().triggerAnimation(AnimatedSprite.ANIMATION_ATTACK_NORTH);
            int dmg = combatUnitList.get(0).Attack(combatUnitList.get(1));
            if(dmg <= 0)
            {
                writeText("MISS", Color.WHITE, combatUnitList.get(0).getx(), combatUnitList.get(0).getY(), 80, Text.Behavior.RISING);
            }
            else
            {
                writeText(dmg + "", Color.WHITE, combatUnitList.get(0).getx(), combatUnitList.get(0).getY(), 80, Text.Behavior.RISING);
                if(combatUnitList.get(1).GetCurrentHP() == 0) victoryScene();

            }
            cdbar.reset();
        }
    }

    private void victoryScene()
    {
        textList.clear();
        disableHealth();
        curState = State.VICTORY;
        setBackground(drawable.test, 96, 32);
        writeText("VICTORY", Color.BLACK, res.getDisplayMetrics().widthPixels/2, res.getDisplayMetrics().heightPixels/2, 160, Text.Behavior.SCROLLING);
        writeText("VICTORY", Color.parseColor("#FFD700"), res.getDisplayMetrics().widthPixels/2, res.getDisplayMetrics().heightPixels/2, 150, Text.Behavior.SCROLLING);

    }

    private void defeatScene()
    {
        textList.clear();
        disableHealth();
        curState = State.DEFEAT;
        bmDraw = null;
        writeText("DEFEAT", Color.RED, res.getDisplayMetrics().widthPixels/2, res.getDisplayMetrics().heightPixels/2, 160, Text.Behavior.SCROLLING);
        writeText("DEFEAT", Color.WHITE, res.getDisplayMetrics().widthPixels/2, res.getDisplayMetrics().heightPixels/2, 150, Text.Behavior.SCROLLING);
        setBackground(Color.BLACK);
    }

    private void disableHealth()
    {
        for(CombatUnit cu : combatUnitList)
        {
           cu.setHbEnabled(false);
        }
    }


}
