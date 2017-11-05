package edu.uwm.cs.fitrpg.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

//import edu.uwm.cs.fitrpg.game.GameThread;

/**
 * Created by SS Fink on 10/17/2017.
 */
public class GameView extends SurfaceView
{
    private SurfaceHolder holder;
    private GameThread thread;
    private Scene scene;

    public GameView(Context context)
    {
        super(context);
        thread = new GameThread(this);
        scene = new Scene();
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback()
        {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder)
            {
                boolean retry = true;
                thread.setRunning(false);
                try {
                    thread.join();
                    retry = false;
                }catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder)
            {
                thread.setRunning(true);
                thread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
            {}
        });
    }

    public void draw(Canvas canvas)
    {
        super.draw(canvas);
        canvas.drawColor(Color.RED);
        scene.draw(canvas);
    }

    public void tick(float deltaTime)
    {
        scene.tick(deltaTime);
    }

    public Scene getScene()
    {
        return this.scene;
    }

    public boolean onTouchEvent(MotionEvent e)
    {
        int action = e.getAction();
        float x,y;
        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
                x = e.getX();
                y = e.getY();
                scene.spawnParticles(10, 100, 1, 1, (int)x, (int)y, Color.parseColor("#FFFFFF"), Particle.Behavior.DEFAULT);
                return true;
            default:
                return super.onTouchEvent(e);
        }


    }



}