package edu.uwm.cs.fitrpg.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import edu.uwm.cs.fitrpg.CombatActivity;
import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.FitnessChallenge;
import edu.uwm.cs.fitrpg.MapView;
import edu.uwm.cs.fitrpg.R;
import edu.uwm.cs.fitrpg.RpgChar;
import edu.uwm.cs.fitrpg.model.FitnessActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {


    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getActivity();
        myDB = new DatabaseHelper(context);
        return inflater.inflate(R.layout.activity_map, container, false);
    }


    private MapView mapView;                    //PS The map view object in app that controls the visuals, as well as stores the current node and boss node
    private View[] mapNodes;                    //PS Stores the buttons associated with the map nodes
    private boolean isTraveling;               //PS Local storage of whether the player is currently traveling, and thus whether a node button is clickable
    private int travelDuration = 10000;         //PS How long in milliseconds it takes currently to travel
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

    private boolean menuIsVisible = false;
    private View menuLayout;
    private TextView menuTopBarText;
    private TextView menuBodyText;
    private ProgressBar menuTravelProgressBar;
    private TextView menuTravelFitnessLog;
    private Button menuLeftButton;
    private Button menuRightButton;

    DatabaseHelper myDB;
    RpgChar playerChar;
    Date startTravelTime;
    Date endTravelTime;
    Date lastCheckedTime;
    int activityLines;
    FitnessChallenge[] challenges;
    Boolean[] challengeComplete;
    int countComplete;

    View fragmentView = getView();
    private Context context;

    @Override
    public void onStart() {
        super.onStart();

        Intent intent = getActivity().getIntent();
        playerChar = new RpgChar();

        basePlayerStamina = playerChar.getStamina();
        basePlayerStrength = playerChar.getStrength();
        basePlayerEndurance = playerChar.getEndurance();
        basePlayerDexterity = playerChar.getDexterity();
        basePlayerSpeed = playerChar.getSpeed();

        baseEnemyStamina = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStamina", baseEnemyStamina);
        baseEnemyStrength = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStrength", baseEnemyStrength);
        baseEnemyEndurance = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyEndurance", baseEnemyEndurance);
        baseEnemyDexterity = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyDexterity", baseEnemyDexterity);
        baseEnemySpeed = intent.getIntExtra("edu.uwm.cs.fitrpg.enemySpeed", baseEnemySpeed);

        //myDB = new DatabaseHelper(context);
        //PS TODO Get loop from DB
        loop = intent.getIntExtra("edu.uwm.cs.fitrpg.loopCount", 1);


        isTraveling = false;

        menuLayout = fragmentView.findViewById((R.id.MapMenuLayout));
        menuLayout.setVisibility(View.INVISIBLE);
        menuIsVisible = false;
        menuTopBarText = (TextView)fragmentView.findViewById(R.id.MapMenuTopBarText);
        menuBodyText = (TextView)fragmentView.findViewById(R.id.MapMenuBodyText);
        menuTravelFitnessLog = (TextView)fragmentView.findViewById(R.id.MapMenuTravelFitnessActivities);
        menuTravelProgressBar = (ProgressBar)fragmentView.findViewById(R.id.MapMenuTravelProgress);
        menuTravelProgressBar.setMax(100);
        menuLeftButton = (Button)fragmentView.findViewById(R.id.MapMenuLeftButton);
        menuRightButton = (Button)fragmentView.findViewById(R.id.MapMenuRightButton);
        mapView = (MapView)fragmentView.findViewById(R.id.MapViewCanvas);
        mapView.setCurrentNode(playerChar.getCurrentNode());

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
        passedContext = getActivity();

        //PS Example of how to change node position
        //mapView.ChangeNodePosition(0, new Pair(50,1900));
        //mapView.ChangeNodePosition(1, new Pair(50,50));
        //mapView.ChangeNodePosition(2, new Pair(500,1000));
        //mapView.ChangeNodePosition(3, new Pair(1000,1900));

        mapNodes = new View[mapView.getNumOfNodes()];
        endTravelTime = new Date();
        lastCheckedTime = new Date();
        lastCheckedTime.setTime(0);     //PS DEBUG CODE
        challenges = new FitnessChallenge[3];     //PS DEBUG CODE
        challengeComplete = new Boolean[3];     //PS DEBUG CODE
        countComplete = 0;     //PS DEBUG CODE
        for (int i = 0; i < challenges.length; i++)
        {
            challenges[i] = new FitnessChallenge();     //PS DEBUG CODE
            challengeComplete[i] = false;     //PS DEBUG CODE
        }

        final Handler placeButtonsHandler = new Handler();
        placeButtonsHandler.postDelayed(new Runnable() {
            public void run() {
                Button myButton;
                ConstraintSet lp_set = new ConstraintSet();
                ConstraintLayout ll = (ConstraintLayout) fragmentView.findViewById(R.id.buttonLayout);
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams((int) ((int) mapView.getNodeSize() * buttonScale), (int) ((int) mapView.getNodeSize() * buttonScale));

                for(int i = 0; i < mapNodes.length; i++) {
                    myButton = new Button(passedContext);
                    myButton.setId(i);
                    myButton.setBackgroundColor(Color.TRANSPARENT);
                    myButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ClickNode((v));
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
        ConstraintLayout ll = (ConstraintLayout) fragmentView.findViewById(R.id.buttonLayout);
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

    public void ClickNode(View view)
    {
        if(!menuIsVisible) {
            if(!isTraveling) {
                destinationNode = 3;
                for (int i = 0; i < mapNodes.length; i++) {
                    if (mapNodes[i] == view) {
                        destinationNode = i;
                        break;
                    }
                }
                menuLayout.setVisibility(View.VISIBLE);
                menuTravelProgressBar.setVisibility(View.GONE);
                menuTravelFitnessLog.setVisibility(View.GONE);
                menuRightButton.setVisibility(View.VISIBLE);
                menuIsVisible = true;
                final View passedView = view;
                if(destinationNode == mapView.getCurrentNode()) {
                    SQLiteDatabase readDb = myDB.getReadableDatabase();
                    menuTopBarText.setText("Current Node");
                    String tempMenuBodyText = "This is your current location\nChallenges:\n";
                    List<FitnessActivity> activities = FitnessActivity.getAllByDate(readDb, 1, lastCheckedTime, new Date());

                    for(int i = 0; i < activities.size(); i++)
                    {
                        for(int j = 0; j < challengeComplete.length; j++)
                        {
                            if(!challengeComplete[j])
                            {
                                challengeComplete[j] = challenges[j].checkComplete(activities.get(i).getType().getName());
                            }
                        }
                    }
                    for(int i = 0; i < challengeComplete.length; i++) {
                        tempMenuBodyText += challenges[i].getChallengeType();
                        if (challengeComplete[i]) {
                            tempMenuBodyText += " Complete!\n";
                        } else {
                            tempMenuBodyText += " Not Done\n";
                        }
                    }
                    menuBodyText.setText(tempMenuBodyText);
                    menuLeftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int tempComplete = 0;
                            for (int i = 0; i < challengeComplete.length; i++)
                            {
                                if(challengeComplete[i])
                                {
                                    tempComplete++;
                                }
                            }
                            CompleteNode(tempComplete);
                        }
                    });
                }
                else if (mapView.getConnectedToCurrentNode(destinationNode))
                {
                    menuTopBarText.setText(getResources().getString(R.string.confirm_movement_title_string));
                    menuBodyText.setText( "Travel to this node will take " + Integer.toString(travelDuration/1000) + " seconds\n" +
                            "Confirm Travel?");
                    menuLeftButton.setVisibility(View.VISIBLE);
                    menuLeftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            MoveCharacter((passedView));
                        }
                    });
                }
                else
                {
                    menuTopBarText.setText("Node is Too Far!");
                    menuBodyText.setText( "This node is not connected to your current location");
                    menuLeftButton.setVisibility(View.GONE);
                }
                menuRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CloseMenu();
                    }
                });
            }
        }
    }

    public void CloseMenu()
    {
        if(menuIsVisible)
        {
            menuLayout.setVisibility(View.INVISIBLE);
            menuIsVisible = false;
        }
    }

    public void CompleteNode(int val)
    {
        if(val <= countComplete)
        {
            CloseMenu();
        }
        else
        {
            val = val - countComplete;
            countComplete += val;
            menuBodyText.setText("Stats all increase by " + val + "!");
            playerChar.setStrength(playerChar.getStrength()+val);
            playerChar.setStamina(playerChar.getStamina()+val);
            playerChar.setSpeed(playerChar.getSpeed()+val);
            playerChar.setDexterity(playerChar.getDexterity()+val);
            playerChar.setEndurance(playerChar.getEndurance()+val);
            playerChar.dbPush();
            menuLeftButton.setVisibility(View.GONE);
        }
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
                mapView.setTravelProgress(0);
                menuTravelProgressBar.setProgress(0);
                menuTravelProgressBar.setVisibility(View.VISIBLE);
                menuTravelFitnessLog.setVisibility(View.VISIBLE);
                menuLeftButton.setVisibility(View.GONE);
                menuRightButton.setVisibility(View.GONE);
                //PS TODO Add DB calls telling that we are moving now
                StartMoving();
            }
        }
    }

    private void StartMoving()
    {
        //PS TODO move DB stuff to on create when updating travel on database
        final SQLiteDatabase readDb = myDB.getReadableDatabase();
        startTravelTime = new Date();
        lastCheckedTime = new Date();
        //lastCheckedTime.setTime(0);       //PS DEBUG CODE
        endTravelTime.setTime(startTravelTime.getTime()+travelDuration);
        final Handler mapTravelHandler = new Handler();
        activityLines = 0;
        mapTravelHandler.postDelayed(new Runnable() {
            public void run() {
                travelProgress += 500;
                mapView.setTravelProgress((travelProgress*100)/travelDuration);
                menuTravelProgressBar.setProgress((int)((travelProgress*100)/travelDuration));
                List<FitnessActivity> activities = FitnessActivity.getAllByDate(readDb, 1, lastCheckedTime, new Date());
                lastCheckedTime = new Date();
                for(int i = 0; i < activities.size(); i++)
                {
                    if(activityLines < 2) {
                        activityLines++;
                    }
                    else
                    {
                        String fitnessLogText = menuTravelFitnessLog.getText().toString();
                        fitnessLogText = "Fitness Activities:\n" + fitnessLogText.substring(fitnessLogText.indexOf("Date", fitnessLogText.indexOf("Date")+1));
                        menuTravelFitnessLog.setText(fitnessLogText);
                    }
                    menuTravelFitnessLog.append("\nDate: " + activities.get(i).getStartDate() + "\nActivity Type: " + activities.get(i).getType().getName());
                }
                if (travelProgress < travelDuration) {
                    mapTravelHandler.postDelayed(this, 500);
                }
                else
                {
                    StopMoving();
                }
            }
        }, 500);
    }

    private void StopMoving()
    {
        isTraveling = false;
        mapView.setCurrentNode(destinationNode);
        playerChar.setCurrentNode(destinationNode);
        //PS TODO Move to travel complete
        playerChar.dbPush();
        mapView.setIsTraveling(false);
        mapView.setTravelProgress(0);
        menuTravelProgressBar.setProgress(0);
        travelProgress = 0;
        TravelComplete();
    }

    private void TravelComplete()
    {
        startTravelTime.setTime(0);     //PS DEBUG CODE
        int[] updatedStats = playerChar.peekStatsFromActivities(startTravelTime, endTravelTime);
        final int strengthGain = updatedStats[0], enduranceGain = updatedStats[1], dexterityGain = updatedStats[2], speedGain = updatedStats[3], staminaGain = updatedStats[4] ;
        menuBodyText.setText("Travel Complete!\nGains: Sta +" + staminaGain + " Spd +" + speedGain + " Str +" + strengthGain + " End +" + enduranceGain + " Dex +" + dexterityGain);
        //PS TODO calculate gains
        menuLeftButton.setVisibility(View.VISIBLE);
        challenges = new FitnessChallenge[3];
        challengeComplete = new Boolean[3];
        countComplete = 0;
        lastCheckedTime.setTime(0);     //PS DEBUG CODE
        for (int i = 0; i < challenges.length; i++)
        {
            challenges[i] = new FitnessChallenge();
            challengeComplete[i] = false;
        }
        if(destinationNode == mapView.getBossNode())
        {
            menuLeftButton.setText(getResources().getString(R.string.combat_start_button));
        }
        menuLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerChar.updateStatsFromActivities(startTravelTime, endTravelTime);
                playerChar.dbPush();
                if(destinationNode == mapView.getBossNode())
                {
                    menuLeftButton.setText(getResources().getString(R.string.confirm_button_string));
                    LaunchCombat();
                }
                else {
                    CloseMenu();
                }
            }
        });

    }

    public void LaunchCombat()
    {
        Intent intent = new Intent(context, CombatActivity.class);

        intent.putExtra("edu.uwm.cs.fitrpg.enemyStamina", baseEnemyStamina);

        intent.putExtra("edu.uwm.cs.fitrpg.enemyStrength", baseEnemyStrength);

        intent.putExtra("edu.uwm.cs.fitrpg.enemyEndurance", baseEnemyEndurance);

        intent.putExtra("edu.uwm.cs.fitrpg.enemyDexterity", baseEnemyDexterity);

        intent.putExtra("edu.uwm.cs.fitrpg.enemySpeed", baseEnemySpeed);

        intent.putExtra("edu.uwm.cs.fitrpg.loopCount", loop);

        startActivity(intent);
        onStop();
    }

}
