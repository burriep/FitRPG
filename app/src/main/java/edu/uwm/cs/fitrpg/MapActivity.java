package edu.uwm.cs.fitrpg;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;


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
    private Context passedContext;
    private double buttonScale = 1.5;

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

        //Boolean[] connections = new Boolean[3];
        //connections[0]=false;
        //connections[1]=true;
        //connections[2]=false;
        //connections[3]=false;
        //mapView.SetAllNodeConnections(2, connections);

        //Pair[] nodeConnectionPair = new Pair[2];
        //nodeConnectionPair[0] = new Pair(0, false);
        //nodeConnectionPair[1] = new Pair(3, true);
        //mapView.SetMultipleNodeConnections(2, nodeConnectionPair);

        mapView.ToggleNodeConnections(2, 0);
        mapView.ToggleNodeConnections(1, 3);
        passedContext = this;

        mapView.ChangeNodePosition(0, new Pair(50,1900));
        mapView.ChangeNodePosition(1, new Pair(50,50));
        mapView.ChangeNodePosition(2, new Pair(500,1000));
        mapView.ChangeNodePosition(3, new Pair(1000,1900));

        mapNodes = new View[mapView.getNumOfNodes()];

        final Handler placeButtonsHandler = new Handler();
        placeButtonsHandler.postDelayed(new Runnable() {
            public void run() {
                Button myButton;
                ConstraintSet lp_set = new ConstraintSet();
                ConstraintLayout ll = (ConstraintLayout) findViewById(R.id.buttonLayout);
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams((int) ((int) mapView.getNodeSize() * buttonScale), (int) ((int) mapView.getNodeSize() * buttonScale));

                for(int i = 0; i < mapNodes.length; i++) {
                    myButton = new Button(passedContext);
                    myButton.setId(i);
                    myButton.setBackgroundColor(Color.TRANSPARENT);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MoveCharacter((v));
                        }
                    });

                    ll.addView(myButton, lp);
                    lp_set.clone(ll);
                    lp_set.setTranslationX(myButton.getId(), (int) mapView.getAdjustedNodePosition(i).first - (int) (mapView.getNodeSize() * buttonScale) / 2);
                    lp_set.setTranslationY(myButton.getId(), (int) mapView.getAdjustedNodePosition(i).second - (int) (mapView.getNodeSize() * buttonScale) / 2);

                    lp_set.applyTo(ll);
                    mapNodes[i] = (View)myButton;
                }
            }
        }, 200);
    }

    public void RedrawButton(int i)
    {
        ConstraintLayout ll = (ConstraintLayout) findViewById(R.id.buttonLayout);
        ConstraintSet lp_set = new ConstraintSet();
        lp_set.clone(ll);
        lp_set.setTranslationX(i, (int) mapView.getAdjustedNodePosition(i).first - (int) (mapView.getNodeSize() * buttonScale) / 2);
        lp_set.setTranslationY(i, (int) mapView.getAdjustedNodePosition(i).second - (int) (mapView.getNodeSize() * buttonScale) / 2);

        lp_set.applyTo(ll);
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
            if(destinationNode != mapView.getCurrentNode() && mapView.getConnectedToCurrentNode(destinationNode)) {
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
