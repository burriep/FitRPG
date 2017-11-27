package edu.uwm.cs.fitrpg;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import edu.uwm.cs.fitrpg.game.CombatUnit;

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

    private int loop = 1;

    private long lastAttackTime;

    private Button combatButton;

    private Boolean isTransferring = false;

    DatabaseHelper myDB;
    RpgChar playerChar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combat);
        isTransferring = false;

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        myDB = new DatabaseHelper(this);
        playerChar = new RpgChar();

        playerUnit = new CombatUnit(playerChar.getStamina(),
                playerChar.getStrength(),
                playerChar.getEndurance(),
                playerChar.getDexterity(),
                playerChar.getSpeed());

        SetPlayerDisplay();

        //PS TODO Get from database
        loop = intent.getIntExtra("edu.uwm.cs.fitrpg.loopCount", 1);

        //PS TODO Generate enemy types or something for DB and pull stats from there rather than intent?
        enemyUnit = new CombatUnit(intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStamina", 10) * loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStrength", 5)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyEndurance", 5)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemyDexterity", 5)* loop,
                intent.getIntExtra("edu.uwm.cs.fitrpg.enemySpeed", 5)* loop);

        SetEnemyDisplay();

        playerHealthBar = (ImageView)findViewById(R.id.CombatPlayerHealthBar);
        enemyHealthBar = (ImageView)findViewById(R.id.CombatEnemyHealthBar);
        defaultHealthBarWidth = playerHealthBar.getLayoutParams().width;
        combatLog = (TextView) findViewById(R.id.CombatLog);
        combatButton = (Button)findViewById(R.id.CombatRunRoundButton);

        combatLogLines = 0;
        lastAttackTime = 0;

        AutomateEnemyAttack();
    }

    private void SetPlayerDisplay()
    {
        TextView textHolder;

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
    }

    private void SetEnemyDisplay()
    {
        TextView textHolder;

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
    }

    private void AutomateEnemyAttack()
    {
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
    }

    public void RunCombatRound(View view)
    {
        if(playerUnit.GetCurrentHP() > 0 && enemyUnit.GetCurrentHP() > 0) {
            RunPlayerAttack();
        }
        else
        {
            if(!isTransferring) {
                isTransferring = true;
                playerChar.setCurrentNode(0);
                playerChar.dbPush();
                Intent intent = new Intent(this, MapActivity.class);
                intent.putExtra("edu.uwm.cs.fitrpg.enemyStamina", enemyUnit.GetStamina() / loop);

                intent.putExtra("edu.uwm.cs.fitrpg.enemyStrength", enemyUnit.GetStrength() / loop);

                intent.putExtra("edu.uwm.cs.fitrpg.enemyEndurance", enemyUnit.GetEndurance() / loop);

                intent.putExtra("edu.uwm.cs.fitrpg.enemyDexterity", enemyUnit.GetDexterity() / loop);

                intent.putExtra("edu.uwm.cs.fitrpg.enemySpeed", enemyUnit.GetSpeed() / loop);

                if (playerUnit.GetCurrentHP() > 0) {
                    //PS TODO set DB loop
                    loop++;
                }
                finish();

                intent.putExtra("edu.uwm.cs.fitrpg.loopCount", loop);
                startActivity(intent);
            }
        }
    }

    //PS This is the framework for an enemy attack vs a player without any of the text log stuff, to built upon with graphics
    public void BasicEnemyAttack()
    {
        if(enemyUnit.GetCurrentHP() > 0 && playerUnit.GetCurrentHP() > 0) {
            int damageAmount = enemyUnit.Attack(playerUnit);
        }
        playerHealthLabel.setText(Integer.toString(playerUnit.GetCurrentHP()) + "/" + Integer.toString(playerUnit.GetMaxHP()));
        playerHealthBar.getLayoutParams().width = (int)(defaultHealthBarWidth * ((double)playerUnit.GetCurrentHP()/(double)playerUnit.GetMaxHP()));
        if(playerUnit.GetCurrentHP() <= 0)
        {
            combatButton.setText("Continue");
        }
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
        if(playerUnit.GetCurrentHP() <= 0)
        {
            combatButton.setText("Continue");
        }
    }

    //PS This is the framework for a player attack vs an enemy without any of the text log stuff, to built upon with graphics
    public void BasicPlayerAttack()
    {
        long currentTime = System.currentTimeMillis();
        if(currentTime - lastAttackTime > (1/Math.log((double)(playerUnit.GetSpeed() + 1)) * 500) && playerUnit.GetCurrentHP() > 0 && enemyUnit.GetCurrentHP() > 0) {
            int damageAmount = playerUnit.Attack(enemyUnit);
            lastAttackTime = currentTime;
        }
        enemyHealthLabel.setText(Integer.toString(enemyUnit.GetCurrentHP()) + "/" + Integer.toString(enemyUnit.GetMaxHP()));
        enemyHealthBar.getLayoutParams().width = (int)(defaultHealthBarWidth * ((double)enemyUnit.GetCurrentHP()/(double)enemyUnit.GetMaxHP()));
        if(enemyUnit.GetCurrentHP() <= 0)
        {
            combatButton.setText("Continue");
        }
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
        if(enemyUnit.GetCurrentHP() <= 0)
        {
            combatButton.setText("Continue");
        }
    }
}
