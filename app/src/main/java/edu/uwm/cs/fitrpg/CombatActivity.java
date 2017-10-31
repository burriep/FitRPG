package edu.uwm.cs.fitrpg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CombatActivity extends AppCompatActivity {

    private CombatUnit playerUnit;
    private CombatUnit enemyUnit;

    private int defaultHealthBarWidth;

    private ImageView playerHealthBar;
    private ImageView enemyHealthBar;
    private TextView playerHealthLabel;
    private TextView enemyHealthLabel;
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

        defaultHealthBarWidth = findViewById(R.id.CombatEnemyHealthBarEmpty).getWidth();
        playerHealthBar = (ImageView)findViewById(R.id.CombatPlayerHealthBar);
        enemyHealthBar = (ImageView)findViewById(R.id.CombatEnemyHealthBar);

    }

    public void RunCombatRound(View view)
    {
        if(playerUnit.GetCurrentHP() > 0) {
            playerUnit.Attack(enemyUnit);
        }
        if(enemyUnit.GetCurrentHP() > 0) {
            enemyUnit.Attack(playerUnit);
        }
        playerHealthLabel.setText(Integer.toString(playerUnit.GetCurrentHP()) + "/" + Integer.toString(playerUnit.GetMaxHP()));
        enemyHealthLabel.setText(Integer.toString(enemyUnit.GetCurrentHP()) + "/" + Integer.toString(enemyUnit.GetMaxHP()));
    }
}
