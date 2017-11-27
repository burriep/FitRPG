package edu.uwm.cs.fitrpg.game;

import android.graphics.Canvas;

import edu.uwm.cs.fitrpg.graphics.GameView;

/**
 * Created by SS Fink on 10/17/2017.
 */

public class GameThread extends Thread {
    private GameView view;
    private boolean running = false;
    private long beginTime;
    private long deltaTime;

    public GameThread(GameView view)
    {
        this.view = view;
        this.beginTime = 0;
        this.deltaTime = 0;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {

            Canvas c = null;
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    beginTime = System.currentTimeMillis();
                    view.tick(deltaTime);

                    view.draw(c);
                    deltaTime = System.currentTimeMillis() - beginTime;
                }
            } catch(Exception e)
            {
                e.printStackTrace();
            }finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
        }
    }
}
