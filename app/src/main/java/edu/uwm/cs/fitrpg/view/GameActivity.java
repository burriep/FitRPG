package edu.uwm.cs.fitrpg.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.game.CombatUnit;
import edu.uwm.cs.fitrpg.graphics.GameView;

public class GameActivity extends Activity {
    private GameView gv;
    private int loop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        gv = new GameView(this);

        setUpScene(gv, getIntent());

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
    @Override
    public void onBackPressed()
    {this.finish();}



    public void onFinishAlert(int status)
    {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("edu.uwm.cs.fitrpg.playerStamina", gv.getScene().getPlayer().GetStamina());
        intent.putExtra("edu.uwm.cs.fitrpg.enemyStamina", gv.getScene().getPlayer().GetStamina()/loop);

        intent.putExtra("edu.uwm.cs.fitrpg.playerStrength", gv.getScene().getPlayer().GetStrength());
        intent.putExtra("edu.uwm.cs.fitrpg.enemyStrength", gv.getScene().getEnemy().GetStrength()/loop);

        intent.putExtra("edu.uwm.cs.fitrpg.playerEndurance", gv.getScene().getPlayer().GetEndurance());
        intent.putExtra("edu.uwm.cs.fitrpg.enemyEndurance", gv.getScene().getEnemy().GetEndurance()/loop);

        intent.putExtra("edu.uwm.cs.fitrpg.playerDexterity", gv.getScene().getPlayer().GetDexterity());
        intent.putExtra("edu.uwm.cs.fitrpg.enemyDexterity", gv.getScene().getEnemy().GetDexterity()/loop);

        intent.putExtra("edu.uwm.cs.fitrpg.playerSpeed", gv.getScene().getPlayer().GetSpeed());
        intent.putExtra("edu.uwm.cs.fitrpg.enemySpeed", gv.getScene().getEnemy().GetSpeed()/loop);

        if (status > 0)
        {
            loop++;
        }
        finish();

        intent.putExtra("edu.uwm.cs.fitrpg.loopCount", loop);
        startActivity(intent);
    }




    private void setUpScene(GameView gv, Intent intent)
    {
        gv.getScene().setBackground(Color.parseColor("#4B0082"));

        gv.getScene().setBackground(R.drawable.dark_grass1, 128, 32);

        loop = intent.getIntExtra("edu.uwm.cs.fitrpg.mapLoop", 1);
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.player_ss_test);
        bm = Bitmap.createScaledBitmap(bm, 576, 384,false);


        // Build the player
        gv.getScene().spawnPlayerCombatUnit(bm, intent.getIntExtra("edu.uwm.cs.fitrpg.playerStamina", 10),
                intent.getIntExtra("edu.uwm.cs.fitrpg.playerStrength", 5),
                intent.getIntExtra("edu.uwm.cs.fitrpg.playerEndurance", 5),
                intent.getIntExtra("edu.uwm.cs.fitrpg.playerDexterity", 5),
                intent.getIntExtra("edu.uwm.cs.fitrpg.playerSpeed", 5));

        // Build the enemy
       gv.getScene().spawnEnemyCombatUnit(bm,intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStamina", 10) * loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStrength", 5)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyEndurance", 5)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyDexterity", 5)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemySpeed", 5)* loop);



       gv.getScene().addActivityListener(this);

      /*  Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.player_ss_test);
        bm = Bitmap.createScaledBitmap(bm, 576, 384,false);
        gv.getScene().spawnNewSprite(bm,64,64, 9,true,0,0);
        gv.getScene().spawnNewSprite(bm,64,64,9,true,250,250);
        gv.getScene().getSpriteByIndex(1).setScale(8);
        gv.getScene().getSpriteByIndex(0).setScale(8);
        gv.getScene().getSpriteByIndex(1).setSpriteSheetRow(2);
        gv.getScene().getSpriteByIndex(0).setSpriteSheetRow(5);
        gv.getScene().getSpriteByIndex(0).setNumFrames(6);
        gv.getScene().getSpriteByIndex(1).translate(200, 200, 1);
        gv.getScene().writeText("Click Me!", Color.WHITE, 500, 500, 64, Text.Behavior.RISING);
       //gv.getScene().removeSprite(gv.getScene().getSpriteByIndex(0));
*/


    }


}

