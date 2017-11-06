package edu.uwm.cs.fitrpg;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class MapActivity extends AppCompatActivity {

    private MapView mapView;                    //PS The map view object in app that controls the visuals, as well as stores the current node and boss node
    private View[] mapNodes;                    //PS Stores the buttons associated with the map nodes
    private boolean isTraveling;               //PS Local storage of whether the player is currently traveling, and thus whether a node button is clickable
    private int travelDuration = 1000;         //PS How long in milliseconds it takes currently to travel
    private int travelProgress = 0;            //PS A number between 0-100 representing the percentage of how far along the current travel is
    private int destinationNode = 0;           //PS The node the player is currently traveling to

    private int basePlayerStamina = 1000;      //PS A debug value for how much stamina the player will be told to have.  Set high so as to not die, even on higher loops.
    private int basePlayerStrength = 5;        //PS A debug value for how much strength the player will be told to have.
    private int basePlayerEndurance = 5;       //PS 
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

    //PS Unused currently
    public void ResetMap(int passedLoop)
    {
        isTraveling = false;
        mapView.setCurrentNode(0);
        mapView.setTravelProgress(0);
        mapView.setTravelProgress(0);
        loop = passedLoop;
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

                StartMoving();
            }
        }
    }

    private void StartMoving()
    {
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
                    StopMoving();
                }
            }
        }, 200);
    }

    private void StopMoving()
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
        finish();
    }
}
