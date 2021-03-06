package edu.uwm.cs.fitrpg.view;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.MapActivity;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.RpgChar;
import edu.uwm.cs.fitrpg.activity.Home;
import edu.uwm.cs.fitrpg.game.CombatUnit;
import edu.uwm.cs.fitrpg.graphics.GameView;

public class GameActivity extends Activity {
    private GameView gv;
    private int loop;
    private boolean isTransferring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isTransferring = false;

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
        if(!isTransferring) {
            isTransferring = true;
            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("edu.uwm.cs.fitrpg.enemyStamina", gv.getScene().getEnemy().GetStamina() / loop);

            intent.putExtra("edu.uwm.cs.fitrpg.enemyStrength", gv.getScene().getEnemy().GetStrength() / loop);

            intent.putExtra("edu.uwm.cs.fitrpg.enemyEndurance", gv.getScene().getEnemy().GetEndurance() / loop);

            intent.putExtra("edu.uwm.cs.fitrpg.enemyDexterity", gv.getScene().getEnemy().GetDexterity() / loop);

            intent.putExtra("edu.uwm.cs.fitrpg.enemySpeed", gv.getScene().getEnemy().GetSpeed() / loop);

            SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
            RpgChar playerChar = RpgChar.get(db, 1);
            if (status > 0) {
                intent.putExtra("edu.uwm.cs.fitrpg.refreshMap", 2);

                playerChar.setLoopCount(loop+1);
                playerChar.setCurrentMap(playerChar.getCurrentMap()+1);
                playerChar.setCurrentNode(0);
                playerChar.update(db);
            }
            else
            {
                intent.putExtra("edu.uwm.cs.fitrpg.refreshMap", 1);
            }
            db.close();

            startActivity(intent);
            finish();
        }
    }




    private void setUpScene(GameView gv, Intent intent)
    {
        gv.getScene().setBackground(Color.parseColor("#4B0082"));

        gv.getScene().setBackground(R.drawable.dark_grass1, 128, 32);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.player_ss_test);
        bm = Bitmap.createScaledBitmap(bm, 576, 384,false);

        SQLiteDatabase db = new DatabaseHelper(Home.appCon).getReadableDatabase();
        RpgChar tempPlayer = RpgChar.get(db, 1);
        db.close();
        loop = tempPlayer.getLoopCount();
        // Build the player
        gv.getScene().spawnPlayerCombatUnit(bm, tempPlayer.getStamina() * 1,
                tempPlayer.getStrength(),
                tempPlayer.getEndurance(),
                tempPlayer.getDexterity(),
                tempPlayer.getSpeed());

        // Build the enemy
        bm = BitmapFactory.decodeResource(getResources(), R.drawable.enemy_orc);
        bm = Bitmap.createScaledBitmap(bm, 576, 384,false);
       gv.getScene().spawnEnemyCombatUnit(bm,intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStamina", 10) * loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStrength", 10)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyEndurance", 10)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyDexterity", 10)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemySpeed", 10)* loop);



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
