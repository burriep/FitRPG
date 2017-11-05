package edu.uwm.cs.fitrpg;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MapActivity extends AppCompatActivity {

    private MapView mapView;
    private View[] mapNodes;
    private boolean isTraveling;
    private int travelDuration = 1000;
    private int travelProgress = 0;
    private int destinationNode = 0;

    private int basePlayerStamina = 1000;
    private int basePlayerStrength = 5;
    private int basePlayerEndurance = 5;
    private int basePlayerDexterity = 5;
    private int basePlayerSpeed = 5;

    private int baseEnemyStamina = 10;
    private int baseEnemyStrength = 5;
    private int baseEnemyEndurance = 5;
    private int baseEnemyDexterity = 5;
    private int baseEnemySpeed = 5;

    private int loop = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Intent intent = getIntent();

        basePlayerStamina = intent.getIntExtra("edu.uwm.cs.fitrpg.playerStamina", basePlayerStamina);
        basePlayerStrength = intent.getIntExtra("edu.uwm.cs.fitrpg.playerStrength", basePlayerStrength);
        basePlayerEndurance = intent.getIntExtra("edu.uwm.cs.fitrpg.playerEndurance", basePlayerEndurance);
        basePlayerDexterity = intent.getIntExtra("edu.uwm.cs.fitrpg.playerDexterity", basePlayerDexterity);
        basePlayerSpeed = intent.getIntExtra("edu.uwm.cs.fitrpg.playerSpeed", basePlayerSpeed);

        baseEnemyStamina = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStamina", baseEnemyStamina);
        baseEnemyStrength = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStrength", baseEnemyStrength);
        baseEnemyEndurance = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyEndurance", baseEnemyEndurance);
        baseEnemyDexterity = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyDexterity", baseEnemyDexterity);
        baseEnemySpeed = intent.getIntExtra("edu.uwm.cs.fitrpg.enemySpeed", baseEnemySpeed);

        loop = intent.getIntExtra("edu.uwm.cs.fitrpg.loopCount", 1);

        isTraveling = false;
        mapView = (MapView)findViewById(R.id.MapViewCanvas);
        //PS TODO Remove this is a faked implementation for sprint 1 limited to 4 and only 4 nodes
        mapNodes = new View[4];
        mapNodes[0] = findViewById(R.id.MapNodeButton0);
        mapNodes[1] = findViewById(R.id.MapNodeButton1);
        mapNodes[2] = findViewById(R.id.MapNodeButton2);
        mapNodes[3] = findViewById(R.id.MapNodeButton3);
        //mapNodes = new View[mapView.getNumOfNodes()];
        //for(int i = 0; i < mapNodes.length; i++)
        //{
        //PS TODO: Create buttons for each node in later sprint
        //}
    }

    public void MoveCharacter(View view)
    {
        if(!isTraveling) {
            destinationNode = 3;
            mapNodes[0].setVisibility(View.VISIBLE);
            for (int i = 0; i < mapNodes.length; i++) {
                if (mapNodes[i] == view) {
                    destinationNode = i;
                    break;
                }
            }
            if(destinationNode != mapView.getCurrentNode()) {
                isTraveling = true;
                mapView.setDestinationNode(destinationNode);
                mapView.setIsTraveling(true);

                final Handler mapTravelHandler = new Handler();
                mapTravelHandler.postDelayed(new Runnable() {
                    public void run() {
                        travelProgress += 200;
                        mapView.setTravelProgress((travelProgress*100)/travelDuration);
                        if (travelProgress < travelDuration) {
                            mapTravelHandler.postDelayed(this, 200);
                        }
                        else
                        {
                            isTraveling = false;
                            mapView.setCurrentNode(destinationNode);
                            mapView.setIsTraveling(false);
                            mapView.setTravelProgress(0);
                            travelProgress = 0;
                            if(destinationNode == mapView.getBossNode())
                            {
                                LaunchCombat();
                            }
                        }
                    }
                }, 200);
            }
        }
    }

    public void LaunchCombat()
    {
        Intent intent = new Intent(this, CombatActivity.class);
        intent.putExtra("edu.uwm.cs.fitrpg.playerStamina", basePlayerStamina);
        intent.putExtra("edu.uwm.cs.fitrpg.enemyStamina", baseEnemyStamina);

        intent.putExtra("edu.uwm.cs.fitrpg.playerStrength", basePlayerStrength);
        intent.putExtra("edu.uwm.cs.fitrpg.enemyStrength", baseEnemyStrength);

        intent.putExtra("edu.uwm.cs.fitrpg.playerEndurance", basePlayerEndurance);
        intent.putExtra("edu.uwm.cs.fitrpg.enemyEndurance", baseEnemyEndurance);

        intent.putExtra("edu.uwm.cs.fitrpg.playerDexterity", basePlayerDexterity);
        intent.putExtra("edu.uwm.cs.fitrpg.enemyDexterity", baseEnemyDexterity);

        intent.putExtra("edu.uwm.cs.fitrpg.playerSpeed", basePlayerSpeed);
        intent.putExtra("edu.uwm.cs.fitrpg.enemySpeed", baseEnemySpeed);

        intent.putExtra("edu.uwm.cs.fitrpg.loopCount", loop);

        startActivity(intent);
    }
}
