package edu.uwm.cs.fitrpg;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Patrick Seaton on 10/30/2017.
 */

public class CombatActivity extends AppCompatActivity {

    private CombatUnit playerUnit;
    private CombatUnit enemyUnit;

    private int defaultHealthBarWidth;

    private ImageView playerHealthBar;
    private ImageView enemyHealthBar;
    private TextView playerHealthLabel;
    private TextView enemyHealthLabel;
    private TextView combatLog;
    private int combatLogLines;

    private long lastAttackTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();

        TextView textHolder;

        playerUnit = new CombatUnit(intent.getIntExtra("edu.uwm.cs.fitrpg.playerStamina", 0),
               intent.getIntExtra("edu.uwm.cs.fitrpg.playerStrength", 0),
                intent.getIntExtra("edu.uwm.cs.fitrpg.playerEndurance", 0),
                intent.getIntExtra("edu.uwm.cs.fitrpg.playerDexterity", 0),
                intent.getIntExtra("edu.uwm.cs.fitrpg.playerSpeed", 0));

        playerHealthLabel = (TextView) findViewById((R.id.CombatPlayerHealthValue));
        playerHealthLabel.setText(Integer.toString(playerUnit.GetCurrentHP()) + "/" + Integer.toString(playerUnit.GetMaxHP()));
        textHolder = (TextView) findViewById((R.id.CombatPlayerStaminaAmount));
        textHolder.setText(Integer.toString(playerUnit.GetStamina()) + " STA");
        textHolder = (TextView) findViewById((R.id.CombatPlayerStrengthAmount));
        textHolder.setText(Integer.toString(playerUnit.GetStrength()) + " STR");
        textHolder = (TextView) findViewById((R.id.CombatPlayerEnduranceAmount));
        textHolder.setText(Integer.toString(playerUnit.GetEndurance()) + " END");
        textHolder = (TextView) findViewById((R.id.CombatPlayerDexterityAmount));
        textHolder.setText(Integer.toString(playerUnit.GetDexterity()) + " DEX");
        textHolder = (TextView) findViewById((R.id.CombatPlayerSpeedAmount));
        textHolder.setText(Integer.toString(playerUnit.GetSpeed())+ " SPD");

        enemyUnit = new CombatUnit(intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStamina", 0),
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStrength", 0),
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyEndurance", 0),
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyDexterity", 0),
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemySpeed", 0));

        enemyHealthLabel = (TextView) findViewById((R.id.CombatEnemyHealthValue));
        enemyHealthLabel.setText(Integer.toString(enemyUnit.GetCurrentHP()) + "/" + Integer.toString(enemyUnit.GetMaxHP()));
        textHolder = (TextView) findViewById((R.id.CombatEnemyStaminaAmount));
        textHolder.setText(Integer.toString(enemyUnit.GetStamina()) + " STA");
        textHolder = (TextView) findViewById((R.id.CombatEnemyStrengthAmount));
        textHolder.setText(Integer.toString(enemyUnit.GetStrength()) + " STR");
        textHolder = (TextView) findViewById((R.id.CombatEnemyEnduranceAmount));
        textHolder.setText(Integer.toString(enemyUnit.GetEndurance()) + " END");
        textHolder = (TextView) findViewById((R.id.CombatEnemyDexterityAmount));
        textHolder.setText(Integer.toString(enemyUnit.GetDexterity()) + " DEX");
        textHolder = (TextView) findViewById((R.id.CombatEnemySpeedAmount));
        textHolder.setText(Integer.toString(enemyUnit.GetSpeed())+ " SPD");

        playerHealthBar = (ImageView)findViewById(R.id.CombatPlayerHealthBar);
        enemyHealthBar = (ImageView)findViewById(R.id.CombatEnemyHealthBar);
        defaultHealthBarWidth = playerHealthBar.getLayoutParams().width;
        combatLog = (TextView) findViewById(R.id.CombatLog);
        combatLogLines = 0;

        final Handler enemyAttackHandler = new Handler();
        enemyAttackHandler.postDelayed(new Runnable(){
            public void run(){
                RunEnemyAttack();
                if(playerUnit.GetCurrentHP() > 0)
                {
                    enemyAttackHandler.postDelayed(this, (int)(1/Math.log((double)(enemyUnit.GetSpeed() + 1)) * 1000));
                }
            }
        }, (int)(1/Math.log((double)(enemyUnit.GetSpeed() + 1)) * 1000));
        lastAttackTime = 0;
    }

    public void RunCombatRound(View view)
    {
        RunPlayerAttack();
    }

    public void RunEnemyAttack()
    {
        if(enemyUnit.GetCurrentHP() > 0 && playerUnit.GetCurrentHP() > 0) {
            int damageAmount = enemyUnit.Attack(playerUnit);
            String logAddition = "\nEnemy Attacked! ";
            if(damageAmount > 0)
            {
                logAddition += "Dealt " + Integer.toString(damageAmount) + " damage! ";
                if(playerUnit.GetCurrentHP() <= 0)
                {
                    logAddition += "Player was KO'd!";
                }
            }
            else
            {
                logAddition += "Enemy Missed! ";
            }
            combatLogLines++;
            String initialText = (String)combatLog.getText();
            if(combatLogLines > 6)
            {
                initialText = (String)initialText.subSequence(initialText.indexOf("\n")+1, initialText.length());
            }
            combatLog.setText(initialText + logAddition);
        }
        playerHealthLabel.setText(Integer.toString(playerUnit.GetCurrentHP()) + "/" + Integer.toString(playerUnit.GetMaxHP()));
        playerHealthBar.getLayoutParams().width = (int)(defaultHealthBarWidth * ((double)playerUnit.GetCurrentHP()/(double)playerUnit.GetMaxHP()));
    }

    public void RunPlayerAttack()
    {
        long currentTime = System.currentTimeMillis();
        String logAddition = "\n";
        if(currentTime - lastAttackTime > (1/Math.log((double)(playerUnit.GetSpeed() + 1)) * 500) && playerUnit.GetCurrentHP() > 0 && enemyUnit.GetCurrentHP() > 0) {
            int damageAmount = playerUnit.Attack(enemyUnit);
            logAddition += "Player Attacked! ";
            if(damageAmount > 0)
            {
                logAddition += "Dealt " + Integer.toString(damageAmount) + " damage! ";
                if(enemyUnit.GetCurrentHP() <= 0)
                {
                    logAddition += "Enemy was KO'd!";
                }
            }
            else
            {
                logAddition += "Player Missed! ";
            }
            lastAttackTime = currentTime;
        }
        else if(currentTime - lastAttackTime <= (1/Math.log((double)(playerUnit.GetSpeed() + 1)) * 500))
        {
            logAddition += "Player Attack is still on Cooldown!";
        }
        else if(playerUnit.GetCurrentHP() > 0)
        {
            logAddition += "Player is KO'd and unable to attack!";
        }
        else
        {
            logAddition += "Enemy is already KO'd!";
        }
        combatLogLines++;
        String initialText = (String)combatLog.getText();
        if(combatLogLines > 6)
        {
            initialText = (String)initialText.subSequence(initialText.indexOf("\n")+1, initialText.length());
        }
        combatLog.setText(initialText + logAddition);
        enemyHealthLabel.setText(Integer.toString(enemyUnit.GetCurrentHP()) + "/" + Integer.toString(enemyUnit.GetMaxHP()));
        enemyHealthBar.getLayoutParams().width = (int)(defaultHealthBarWidth * ((double)enemyUnit.GetCurrentHP()/(double)enemyUnit.GetMaxHP()));
    }
}
