package edu.uwm.cs.fitrpg;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, CombatActivity.class);
        EditText textHolder = (EditText)findViewById(R.id.PlayerStamina);
        int statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.playerStamina", statValue);
        textHolder = (EditText)findViewById(R.id.EnemyStamina);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.enemyStamina", statValue);

        textHolder = (EditText)findViewById(R.id.PlayerStrength);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.playerStrength", statValue);
        textHolder = (EditText)findViewById(R.id.EnemyStrength);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.enemyStrength", statValue);

        textHolder = (EditText)findViewById(R.id.PlayerEndurance);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.playerEndurance", statValue);
        textHolder = (EditText)findViewById(R.id.EnemyEndurance);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.enemyEndurance", statValue);

        textHolder = (EditText)findViewById(R.id.PlayerDexterity);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.playerDexterity", statValue);
        textHolder = (EditText)findViewById(R.id.EnemyDexterity);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.enemyDexterity", statValue);

        textHolder = (EditText)findViewById(R.id.PlayerSpeed);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.playerSpeed", statValue);
        textHolder = (EditText)findViewById(R.id.EnemySpeed);
        statValue = Integer.parseInt(textHolder.getText().toString());
        intent.putExtra("edu.uwm.cs.fitrpg.enemySpeed", statValue);
        startActivity(intent);
    }
}
