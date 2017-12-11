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
import java.util.Random;

import edu.uwm.cs.fitrpg.Audio.SFXPlayer;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.game.CombatUnit;
import edu.uwm.cs.fitrpg.view.GameActivity;

import static edu.uwm.cs.fitrpg.R.*;

/**
 * Created by SS Fink on 10/17/2017.
 */

public class Scene
{
    private enum State {IN_COMBAT, VICTORY, DEFEAT}

    private State curState;
    private Resources res;
    private GameActivity listener;
    private ArrayList<AnimatedSprite> spriteList;
    private ArrayList<Particle> particleList;
    private ArrayList<CombatUnit> combatUnitList;
    private ArrayList<Text> textList;
    private long curenemyCd, maxenemyCd;
    private int bgColor = Color.BLACK;
    private BitmapDrawable bmDraw = null;
    private CooldownBar cdbar;
    private SFXPlayer sfx;
    private long timer = 0;
   

    public Scene(Resources res, SFXPlayer sfx)
    {
        spriteList = new ArrayList<AnimatedSprite>();
        particleList = new ArrayList<Particle>();
        textList = new ArrayList<Text>();
        combatUnitList = new ArrayList<CombatUnit>(2);
        cdbar = new CooldownBar(res.getDisplayMetrics().widthPixels / 2, res.getDisplayMetrics().heightPixels/2 );
        curState = State.IN_COMBAT;
        this.res = res;
        this.sfx = sfx;
    }

    public void addActivityListener(GameActivity ga)
    {
        this.listener = ga;
    }

    public void spawnPlayerCombatUnit(Bitmap bm, int initStamina, int initStrength, int initEndurance, int initDexterity, int initSpeed)
    {
        CombatUnit addThis = new CombatUnit(initStamina, initStrength, initEndurance, initDexterity, initSpeed, (res.getDisplayMetrics().widthPixels / 2) - (32*6), (res.getDisplayMetrics().heightPixels/ 8) * 5);
        addThis.initializeGraphics(bm, AnimatedSprite.ANIMATION_WALK_NORTH);
        addThis.getSprite().setScale(6);
        addThis.getHealthbar().setX(addThis.getx() + (32*6));
        addThis.getHealthbar().setY(addThis.getY() + (64*7));
        combatUnitList.add(0,addThis);

        long cdTime = (long)(((double)Math.log((double)(addThis.GetSpeed() + 1))));

        cdbar.setMaxCooldownTime((long)((1.0/cdTime) * 500));

    }

    public void spawnEnemyCombatUnit(Bitmap bm, int initStamina, int initStrength, int initEndurance, int initDexterity, int initSpeed)
    {
        CombatUnit addThis = new CombatUnit(initStamina, initStrength, initEndurance, initDexterity, initSpeed,(res.getDisplayMetrics().widthPixels / 2) - (32*6), (res.getDisplayMetrics().heightPixels/ 8));
        addThis.initializeGraphics(bm, AnimatedSprite.ANIMATION_WALK_SOUTH);
        addThis.getSprite().setScale(6);
        addThis.getHealthbar().setX(addThis.getx() + (32*6));
        addThis.getHealthbar().setY(addThis.getY());
        combatUnitList.add(addThis);

        maxenemyCd = (long)(((double)Math.log((double)(addThis.GetSpeed() + 1))));
        maxenemyCd = (long)((1.0/(double)maxenemyCd) * 500);
        curenemyCd = 0;

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
       // bm = Bitmap.createScaledBitmap(bm, width, height,false);

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
        addThis.setLife(Integer.MAX_VALUE);
        textList.add(addThis);
    }
    public void writeText(String text, int color, int x, int y, int size, Text.Behavior behavior,long life)
    {
        Text addThis = new Text(text, x, y, size, color, behavior);
        addThis.setLife(life);
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



        if(!combatUnitList.isEmpty())
        for (CombatUnit cu : combatUnitList)
        {
            cu.draw(c);
        }


        // Draw the Text
        if(!textList.isEmpty())
        {
            for (Text t : textList)
            {
                t.draw(c);
            }
        }

        // Draw the Particles
        if(!particleList.isEmpty())
        {
            for (Particle p : particleList)
            {
                p.draw(c);
            }
        }



    }

    public void tick(float deltaTime)
    {

        if(curState == State.IN_COMBAT)
            tickEnemies(deltaTime);

        ArrayList<Particle> killThese = new ArrayList<>();
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
            particleList.removeAll(killThese);
        }

        // update the text
        ArrayList<Text> removethis = new ArrayList<>();
        if(!textList.isEmpty())
        {
            for (Text t : textList)
            {
                t.tick(deltaTime);
                if(t.getYpos() < 0 || t.getXpos() < 0 || t.getLife() <= 0) removethis.add(t);
            }
        }

        textList.removeAll(removethis);

        if(!combatUnitList.isEmpty())
            for (CombatUnit cu : combatUnitList)
            {
                cu.tick(deltaTime);
            }


        cdbar.tick(deltaTime);


        if(curState == State.VICTORY)
        {
            timer += deltaTime;
            if(timer >=  115)
            {
                Random random = new Random();

                int xpos = random.nextInt(res.getDisplayMetrics().widthPixels + 1);
                int ypos = random.nextInt(res.getDisplayMetrics().heightPixels + 1);

                for (int i = 0; i < 20; ++i)
                {
                    int r = random.nextInt(255);
                    int g = random.nextInt(255);
                    int b = random.nextInt(255);

                    double xspeed = random.nextGaussian();
                    double yspeed = random.nextGaussian();

                    spawnParticles(1, 100, (float)(5*xspeed), (float)(5*yspeed), xpos, ypos, Color.rgb(r, g, b), Particle.Behavior.DEFAULT);
                }
                timer = 0;
            }
        }

        if(curState == State.DEFEAT)
        {
            timer += deltaTime;
            if(timer >=  200)
            {
                Random random = new Random();


                int ypos = 0;

                for (int i = 0; i < 10; ++i)
                {
                    int r = 255;
                    int g = 0;
                    int b = 0;
                    int xpos = random.nextInt(res.getDisplayMetrics().widthPixels + 1);
                    double xspeed = 0;
                    double yspeed = random.nextGaussian();

                    spawnParticles(1, 100, (float)(5*xspeed), (float)(5*yspeed), xpos, ypos, Color.rgb(r, g, b), Particle.Behavior.DEFAULT);
                }
                timer = 0;
            }
        }
    }

    private void tickEnemies(float deltaTime)
    {
        curenemyCd += deltaTime;
        if(curenemyCd >= maxenemyCd)
        {
            combatUnitList.get(1).getSprite().triggerAnimation(AnimatedSprite.ANIMATION_ATTACK_NORTH);
            int dmg = combatUnitList.get(1).Attack(combatUnitList.get(0));
            if(dmg <= 0)
            {
                writeText("MISS", Color.WHITE, combatUnitList.get(0).getx(), combatUnitList.get(0).getY(), 80, Text.Behavior.RISING, 100);
            }
            else
            {
                int color = Color.WHITE;
                int life = 100;
                if(combatUnitList.get(0).GetCurrentHP() == 0)
                {
                    color = Color.RED;
                    life *= 2;
                    defeatScene();
                }
                writeText(dmg + "", color, combatUnitList.get(0).getx(), combatUnitList.get(0).getY(), 80, Text.Behavior.RISING, life);
                sfx.playSound(raw.hit);

            }
            curenemyCd = 0;
        }
    }

    public void handleClick(float x, float y, SFXPlayer sfxPlayer) {
        if (cdbar.isOffCooldown() && curState == State.IN_COMBAT) {
            combatUnitList.get(0).getSprite().triggerAnimation(AnimatedSprite.ANIMATION_ATTACK_NORTH);
            int dmg = combatUnitList.get(0).Attack(combatUnitList.get(1));
            if (dmg <= 0) {
                writeText("MISS", Color.WHITE,
                        combatUnitList.get(1).getx() + combatUnitList.get(1).getSprite().getWidth(),
                        combatUnitList.get(1).getY() + combatUnitList.get(1).getSprite().getHeight(),
                        80, Text.Behavior.FALLING, 100);
            } else {
                int color = Color.WHITE;

                if(combatUnitList.get(1).GetCurrentHP() == 0)
                {
                    color = Color.RED;
                    victoryScene();
                }
                writeText( dmg + "", color, combatUnitList.get(1).getx() + combatUnitList.get(1).getSprite().getWidth(),
                        combatUnitList.get(1).getY() + combatUnitList.get(1).getSprite().getHeight(), 80, Text.Behavior.FALLING, 100);
                sfx.playSound(raw.hit);
            }
            cdbar.reset();
        } else if (curState == State.DEFEAT) {

            notifyActivity(0);
        } else if (curState == State.VICTORY)
        {
            notifyActivity(1);
        }
    }

    private void notifyActivity(int status)
    {
        // 0 = Defeat Status
        if(status == 0)
        {
            listener.onFinishAlert(0);
        }
        // 1 = Victory Status
        else if(status == 1)
        {
            listener.onFinishAlert(1);
        }
    }


    private void victoryScene()
    {
        combatUnitList.get(1).getSprite().triggerAnimation(AnimatedSprite.ANIMATION_DIE);
        combatUnitList.get(1).getSprite().setFinal(true);

        Random random = new Random();

        int xpos = combatUnitList.get(1).getx() + combatUnitList.get(1).getSprite().getWidth() / 2;
        int ypos = combatUnitList.get(1).getY()+ combatUnitList.get(1).getSprite().getHeight() / 2;;

        for (int i = 0; i < 20; ++i)
        {
            int r = 255;
            int g = 0;
            int b = 0;

            double xspeed = random.nextGaussian();
            double yspeed = random.nextGaussian();

            spawnParticles(1, 100, (float)(5*xspeed), (float)(5*yspeed), xpos, ypos, Color.rgb(r, g, b), Particle.Behavior.DEFAULT);
        }


        textList.clear();
        disableHealth();
        curState = State.VICTORY;
        setBackground(Color.BLACK);
        writeText("VICTORY", Color.BLACK, res.getDisplayMetrics().widthPixels/2, res.getDisplayMetrics().heightPixels/2, 160, Text.Behavior.SCROLLING);
        writeText("VICTORY", Color.parseColor("#FFD700"), res.getDisplayMetrics().widthPixels/2, res.getDisplayMetrics().heightPixels/2, 150, Text.Behavior.SCROLLING);

    }

    private void defeatScene()
    {
        combatUnitList.get(0).getSprite().triggerAnimation(AnimatedSprite.ANIMATION_DIE);
        combatUnitList.get(0).getSprite().setFinal(true);

        Random random = new Random();

        int xpos = combatUnitList.get(0).getx() + combatUnitList.get(0).getSprite().getWidth() / 2;
        int ypos = combatUnitList.get(0).getY()+ combatUnitList.get(0).getSprite().getHeight() / 2;;

        for (int i = 0; i < 20; ++i)
        {
            int r = 255;
            int g = 0;
            int b = 0;

            double xspeed = random.nextGaussian();
            double yspeed = random.nextGaussian();

            spawnParticles(1, 100, (float)(5*xspeed), (float)(5*yspeed), xpos, ypos, Color.rgb(r, g, b), Particle.Behavior.DEFAULT);
        }


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

    public CombatUnit getPlayer()
    {
        return combatUnitList.get(0);
    }

    public CombatUnit getEnemy()
    {
        return combatUnitList.get(1);
    }


}
