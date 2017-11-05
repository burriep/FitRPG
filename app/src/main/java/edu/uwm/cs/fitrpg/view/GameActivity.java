package edu.uwm.cs.fitrpg.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.graphics.AnimatedSprite;
import edu.uwm.cs.fitrpg.graphics.Particle;
import edu.uwm.cs.fitrpg.graphics.Scene;
import edu.uwm.cs.fitrpg.graphics.GameView;

public class GameActivity extends Activity {
    private GameView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gv = new GameView(this);

        setUpScene(gv);

        setContentView(gv);
    }
    @Override
    protected void onPause()
    {
        super.onPause();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }





    private void setUpScene(GameView gv)
    {
        gv.getScene().setBackground(Color.parseColor("#4B0082"));
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.player_ss_test);
        bm = Bitmap.createScaledBitmap(bm, 576, 384,false);
        gv.getScene().spawnNewSprite(bm,64,64, 9,true,0,0);
        gv.getScene().spawnNewSprite(bm,64,64,9,true,250,250);
        gv.getScene().getSpriteByIndex(1).setScale(8);
        gv.getScene().getSpriteByIndex(0).setScale(8);
        gv.getScene().getSpriteByIndex(1).setSpriteSheetRow(2);
        gv.getScene().getSpriteByIndex(0).setSpriteSheetRow(5);
        gv.getScene().getSpriteByIndex(0).setNumFrames(6);
        gv.getScene().getSpriteByIndex(1).translate(200, 200, 1);
        gv.getScene().writeText("Click Me!", Color.WHITE, 500, 500, 64, true);
       //gv.getScene().removeSprite(gv.getScene().getSpriteByIndex(0));



    }


}

