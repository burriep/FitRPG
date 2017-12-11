package edu.uwm.cs.fitrpg;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.uwm.cs.fitrpg.activity.FitnessOverview;
import edu.uwm.cs.fitrpg.activity.Home;
import edu.uwm.cs.fitrpg.activity.SettingsActivity;
import edu.uwm.cs.fitrpg.model.FitnessActivity;
import edu.uwm.cs.fitrpg.model.FitnessChallengeLevel;
import edu.uwm.cs.fitrpg.util.Utils;
import edu.uwm.cs.fitrpg.view.GameActivity;


public class MapActivity extends AppCompatActivity {
    private int navigationIDTag;

    private MapView mapView;                    //PS The map view object in app that controls the visuals, as well as stores the current node and boss node
    private View[] mapNodes;                    //PS Stores the buttons associated with the map nodes
    private int travelDuration = 1000;         //PS How long in milliseconds it takes currently to travel
    private int travelProgress = 0;            //PS A number between 0-100 representing the percentage of how far along the current travel is
    private int destinationNode = 0;           //PS The node the player is currently traveling to

    private int basePlayerStamina = 1000;      //PS A debug value for how much stamina the player will be told to have.  Set high so as to not die, even on higher loops.
    private int basePlayerStrength = 5;        //PS A debug value for how much strength the player will be told to have.
    private int basePlayerEndurance = 5;       //PS
    private int basePlayerDexterity = 5;
    private int basePlayerSpeed = 5;

    private int baseEnemyStamina = 10;
    private int baseEnemyStrength = 10;
    private int baseEnemyEndurance = 10;
    private int baseEnemyDexterity = 10;
    private int baseEnemySpeed = 10;

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
    //RpgChar playerChar;
    Date startTravelTime;
    Date endTravelTime;
    Date lastCheckedTime;
    List<FitnessChallengeLevel> challenges;
    Boolean[] challengeComplete;
    int countComplete;
    int numberOfChallenges = 1;

    public static SimpleDateFormat mapDateFormat = new SimpleDateFormat(Utils.ISO_DATE_TIME_FORMAT);
    private boolean quitHandler = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Log.d("MapA", "in OnCreate");
        navigationIDTag = 0;
        Intent intent = getIntent();
        //playerChar = new RpgChar();
        quitHandler = false;

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(OnNavigationItemSelectedListener);

        navigation.getMenu().getItem(0).setChecked(false);
        navigation.getMenu().getItem(2).setChecked(true);

        baseEnemyStamina = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStamina", baseEnemyStamina);
        baseEnemyStrength = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyStrength", baseEnemyStrength);
        baseEnemyEndurance = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyEndurance", baseEnemyEndurance);
        baseEnemyDexterity = intent.getIntExtra("edu.uwm.cs.fitrpg.enemyDexterity", baseEnemyDexterity);
        baseEnemySpeed = intent.getIntExtra("edu.uwm.cs.fitrpg.enemySpeed", baseEnemySpeed);

        myDB = new DatabaseHelper(this);

        menuLayout = findViewById((R.id.MapMenuLayout));
        menuTopBarText = (TextView)findViewById(R.id.MapMenuTopBarText);
        menuBodyText = (TextView)findViewById(R.id.MapMenuBodyText);
        menuTravelFitnessLog = (TextView)findViewById(R.id.MapMenuTravelFitnessActivities);
        menuTravelProgressBar = (ProgressBar)findViewById(R.id.MapMenuTravelProgress);
        menuTravelProgressBar.setMax(100);
        menuLeftButton = (Button)findViewById(R.id.MapMenuLeftButton);
        menuRightButton = (Button)findViewById(R.id.MapMenuRightButton);
        mapView = (MapView)findViewById(R.id.MapViewCanvas);
        if(intent.getIntExtra("edu.uwm.cs.fitrpg.refreshMap", 0) != 0) {
            mapView.RefreshMap();
        }

        mapView.setCurrentNode(mapView.board.player.getCurrentNode());

        basePlayerStamina = mapView.board.player.getStamina();
        basePlayerStrength = mapView.board.player.getStrength();
        basePlayerEndurance = mapView.board.player.getEndurance();
        basePlayerDexterity = mapView.board.player.getDexterity();
        basePlayerSpeed = mapView.board.player.getSpeed();

        loop = mapView.board.player.getLoopCount();
        Log.d("MapA", "in OnCreate - Loop: " + loop);
        //PS TODO Check for in combat, travelling, in menu, etc.
        if(intent.getIntExtra("edu.uwm.cs.fitrpg.refreshMap", 0) == 1 || mapView.board.player.getChallengeFlag() == 2) {
            menuIsVisible = true;
            if(mapView.board.player.getChallengeFlag() != 2) {
                FitnessChallengeLevel.increaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                FitnessChallengeLevel.increaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                challenges = FitnessChallengeLevel.getRandomChallenges(myDB.getReadableDatabase(), MapView.board.player.getId(), 1);
                mapView.board.player.setLastCheckedTime(new Date());
                mapView.board.player.setChallengeFlag(2);
                mapView.board.player.setCurrentChallengeID(challenges.get(0).getFitnessTypeId());
                mapView.board.player.dbPush();
            }
            else
            {
                challenges = new ArrayList<>(numberOfChallenges);
                challenges.add(FitnessChallengeLevel.get(myDB.getReadableDatabase(), mapView.board.player.getId(), mapView.board.player.getCurrentChallengeID()));

            }
            menuTopBarText.setText("Game Over");
            menuBodyText.setText("Oh no, you lost! \n"
                    +                    "Would you like to do a difficult challenge to try again?\n"
                    +                    "Otherwise you will be placed at the beginning of the map.\n"
                    +                    "Challenge: " + challenges.get(0).toString());

            if(mapView.board.player.challengeIsCompleted(mapView.board.player.getLastCheckedTime(), new Date(), challenges.get(0))) {
                menuBodyText.append(" Complete!");
            }
            else
            {
                menuBodyText.append(" Not Yet Complete");

            }
            menuTravelProgressBar.setVisibility(View.GONE);
            menuLeftButton.setVisibility(View.VISIBLE);
            menuRightButton.setVisibility(View.VISIBLE);
            menuTravelFitnessLog.setVisibility(View.GONE);
            menuLeftButton.setText("Complete Challenge");
            menuRightButton.setText("Give Up");
            menuLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mapView.board.player.challengeIsCompleted(mapView.board.player.getLastCheckedTime(), new Date(), challenges.get(0))) {

                        FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                        FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                        mapView.board.player.setCurrentChallengeID(-1);
                        mapView.board.player.setChallengeFlag(0);
                        mapView.board.player.dbPush();
                        LaunchCombat();
                        CloseMenu();
                    }
                }
            });
            menuRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                    FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                    mapView.board.player.setCurrentNode(0);
                    mapView.setCurrentNode(0);
                    mapView.board.player.setCurrentChallengeID(-1);
                    mapView.board.player.setChallengeFlag(0);
                    mapView.board.player.dbPush();
                    CloseMenu();
                }
            });
        }
        else if (mapView.board.player.getChallengeFlag() == 1)
        {
            menuIsVisible = true;
            challenges = new ArrayList<>(numberOfChallenges);
            challenges.add(FitnessChallengeLevel.get(myDB.getReadableDatabase(), mapView.board.player.getId(), mapView.board.player.getCurrentChallengeID()));
            destinationNode = mapView.board.player.getChallengeDestinationNode();
            menuTopBarText.setText("Node Too Far");
            menuBodyText.setText("This node is too far from your current node \n"
                    +                    "Would you like to do a difficult challenge to travel here?\n"
                    +                    "Otherwise you cannot travel this far. \n"
                    +                    "Challenge: " + challenges.get(0).toString());
            if(mapView.board.player.challengeIsCompleted(mapView.board.player.getLastCheckedTime(), new Date(), challenges.get(0))) {
                menuBodyText.append(" Complete!");
            }
            else
            {
                menuBodyText.append(" Not Yet Complete");

            }
            menuTravelProgressBar.setVisibility(View.GONE);

            menuLeftButton.setVisibility(View.VISIBLE);
            menuRightButton.setVisibility(View.VISIBLE);
            menuTravelFitnessLog.setVisibility(View.GONE);

            menuLeftButton.setText("Complete Challenge");
            menuRightButton.setText("Cancel");
            menuLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mapView.board.player.challengeIsCompleted(mapView.board.player.getLastCheckedTime(), new Date(), challenges.get(0))) {

                        FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                        FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                        int index = 0;
                        mapView.board.player.setCurrentNode(index);
                        while (!mapView.getConnectedToCurrentNode(destinationNode)) {
                            index++;
                            mapView.board.player.setCurrentNode(index);
                        }
                        mapView.board.player.setCurrentChallengeID(-1);
                        mapView.board.player.setChallengeFlag(0);
                        mapView.board.player.dbPush();
                        MoveCharacter();
                    }
                }
            });
            menuRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                    FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                    mapView.board.player.setCurrentChallengeID(-1);
                    mapView.board.player.setChallengeFlag(0);
                    mapView.board.player.dbPush();
                    CloseMenu();
                }
            });
        }
        else if(intent.getIntExtra("edu.uwm.cs.fitrpg.refreshMap", 0) == 2) {
            menuIsVisible = true;
            menuTopBarText.setText("Increment Fitness Difficulty?");
            menuBodyText.setText("Congratulations on your Victory! \n"
            +                    "Would you like to increase your fitness difficulty level?\n"
            +                    "This will increase the minimum threshholds needed to gain stats, as well as increase the difficulty of challenges offerred");
            menuTravelProgressBar.setVisibility(View.GONE);
            menuTravelFitnessLog.setVisibility(View.GONE);
            menuLeftButton.setVisibility(View.VISIBLE);
            menuRightButton.setVisibility(View.VISIBLE);
            menuLeftButton.setText("Increment");
            menuRightButton.setText("Remain the Same");
            menuLeftButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FitnessChallengeLevel.increaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                    CloseMenu();
                }
            });
            menuRightButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CloseMenu();
                }
            });
        }
        else if(mapView.getIsTraveling())
        {
            Log.d("DBG", "Map Activity - In On Create - Is Traveling");

            menuIsVisible = true;
            menuBodyText.setText("Travel Time Remaining: Calculating...");
            menuTravelProgressBar.setVisibility(View.VISIBLE);
            menuTravelFitnessLog.setVisibility(View.VISIBLE);
            menuLeftButton.setVisibility(View.GONE);
            menuRightButton.setVisibility(View.GONE);
            menuTravelProgressBar.setProgress(0);

            MapPath path = mapView.getCurrentPath();
            lastCheckedTime = mapView.board.player.getLastCheckedTime();
            try {
                startTravelTime = mapDateFormat.parse(path.getStartTime());
                endTravelTime = mapDateFormat.parse(path.getEndTime());
            }
            catch(Exception e)
            {
                Log.d("ERR", "Error - Exception thrown: " + e.toString());
            }
            HandleMoving();
        }
        else {
            menuLayout.setVisibility(View.INVISIBLE);
            menuIsVisible = false;
            mapView.AdjustImage();
            lastCheckedTime = mapView.board.player.getLastCheckedTime();
        }
        passedContext = this;

        mapNodes = new View[mapView.getNumOfNodes()];

        final Handler placeButtonsHandler = new Handler();
        placeButtonsHandler.postDelayed(new Runnable() {
            public void run() {
                Button myButton;
                ConstraintSet lp_set = new ConstraintSet();
                ConstraintLayout ll = (ConstraintLayout) findViewById(R.id.buttonLayout);
                ConstraintLayout.LayoutParams lp = new ConstraintLayout.LayoutParams((int) ((int) mapView.getNodeSize() * buttonScale), (int) ((int) mapView.getNodeSize() * buttonScale));

                ArrayList<MapNode> nodes = mapView.board.getNodes();
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
                    lp_set.setTranslationX(myButton.getId(), mapView.board.getNodes().get(i).getAdjX()- (int) (mapView.getNodeSize() * buttonScale) / 2);
                    lp_set.setTranslationY(myButton.getId(),  mapView.board.getNodes().get(i).getAdjY() - (int) (mapView.getNodeSize() * buttonScale) / 2);

                    lp_set.applyTo(ll);
                    mapNodes[i] = (View)myButton;
                }
            }
        }, 200);
    }

    public void RedrawButton(int i)
    {
        ArrayList<MapNode> nodes = mapView.board.getNodes();
        ConstraintLayout ll = (ConstraintLayout) findViewById(R.id.buttonLayout);
        ConstraintSet lp_set = new ConstraintSet();
        lp_set.clone(ll);
        lp_set.setTranslationX(i, (int) nodes.get(i).getAdjX() - (int) (mapView.getNodeSize() * buttonScale) / 2);
        lp_set.setTranslationY(i, (int) nodes.get(i).getAdjY() - (int) (mapView.getNodeSize() * buttonScale) / 2);

        lp_set.applyTo(ll);
    }

    public void ClickNode(View view)
    {

        if(!menuIsVisible) {
            if(!mapView.getIsTraveling()) {
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
                Log.d("DBG", "in Click Node: Clicked: " + destinationNode + " Converted: " + mapView.board.getNodes().get(destinationNode).getNodeId() + " Current: " + mapView.getCurrentNode());
                if(mapView.board.getNodes().get(destinationNode).getNodeId() == mapView.board.getNodes().get(mapView.getCurrentNode()).getNodeId()) {
                    SetCurrentNodeChallenges();
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
                    FitnessChallengeLevel.increaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                    FitnessChallengeLevel.increaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                    challenges = FitnessChallengeLevel.getRandomChallenges(myDB.getReadableDatabase(), MapView.board.player.getId(), 1);

                    menuTopBarText.setText("Node Too Far");
                    menuBodyText.setText("This node is too far from your current node \n"
                            +                    "Would you like to do a difficult challenge to travel here?\n"
                            +                    "Otherwise you cannot travel this far. \n"
                            +                    "Challenge: " + challenges.get(0).toString());
                    mapView.board.player.setLastCheckedTime(new Date());
                    mapView.board.player.setCurrentChallengeID(challenges.get(0).getFitnessTypeId());
                    mapView.board.player.setChallengeFlag(1);
                    mapView.board.player.setChallengeDestinationNode(destinationNode);
                    mapView.board.player.dbPush();
                    if(mapView.board.player.challengeIsCompleted(mapView.board.player.getLastCheckedTime(), new Date(), challenges.get(0))) {
                        menuBodyText.append(" Complete!");
                    }
                    else
                    {
                        menuBodyText.append(" Not Yet Complete");

                    }
                    menuTravelProgressBar.setVisibility(View.GONE);
                    menuLeftButton.setVisibility(View.VISIBLE);
                    menuRightButton.setVisibility(View.VISIBLE);
                    menuLeftButton.setText("Complete Challenge");
                    menuRightButton.setText("Cancel");
                    menuLeftButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(mapView.board.player.challengeIsCompleted(mapView.board.player.getLastCheckedTime(), new Date(), challenges.get(0))) {

                                FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                                FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                                int index = 0;
                                mapView.board.player.setCurrentNode(index);
                                while (!mapView.getConnectedToCurrentNode(destinationNode)) {
                                    index++;
                                    mapView.board.player.setCurrentNode(index);
                                }
                                mapView.board.player.setCurrentChallengeID(-1);
                                mapView.board.player.setChallengeFlag(0);
                                mapView.board.player.dbPush();
                                MoveCharacter(passedView);
                            }
                        }
                    });
                    menuRightButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                            FitnessChallengeLevel.decreaseAllChallengeLevels(myDB.getReadableDatabase(), mapView.board.player.getId());
                            mapView.board.player.setCurrentChallengeID(-1);
                            mapView.board.player.setChallengeFlag(0);
                            mapView.board.player.dbPush();
                            CloseMenu();
                        }
                    });
                }
                }
                menuRightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CloseMenu();
                    }
                });

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
            mapView.board.player.setStrength(mapView.board.player.getStrength()+val);
            mapView.board.player.setStamina(mapView.board.player.getStamina()+val);
            mapView.board.player.setSpeed(mapView.board.player.getSpeed()+val);
            mapView.board.player.setDexterity(mapView.board.player.getDexterity()+val);
            mapView.board.player.setEndurance(mapView.board.player.getEndurance()+val);
            mapView.board.player.dbPush();
            mapView.board.getNodes().get(mapView.board.player.getCurrentNode()).setNodeStatus(1);
            mapView.board.dbPush();
            mapView.AdjustImage();
            menuLeftButton.setVisibility(View.GONE);
        }
    }

    public void SetCurrentNodeChallenges()
    {
        SQLiteDatabase readDb = myDB.getReadableDatabase();
        menuTopBarText.setText("Current Node");
        String tempMenuBodyText = "This is your current location\n";
        MapNode currentNodeInfo = mapView.board.getNodes().get(mapView.board.player.getCurrentNode());
        if(currentNodeInfo.getNodeStatus() != 1) {
            tempMenuBodyText += "Challenges:\n";
            if(currentNodeInfo.getChallengeID() < 0) {

                challenges = FitnessChallengeLevel.getRandomChallenges(readDb, MapView.board.player.getId(), numberOfChallenges);

                challengeComplete = new Boolean[numberOfChallenges];
                countComplete = 0;
                for (int i = 0; i < challenges.size(); i++) {
                    challengeComplete[i] = false;
                    Log.d("DBG", "in set node challenges - Setting Orig Fitness ID: " + challenges.get(i).getFitnessTypeId());
                    currentNodeInfo.setChallengeID(challenges.get(i).getFitnessTypeId());
                }
                mapView.board.dbPush();

            }
            else
            {
                challenges = new ArrayList<>(numberOfChallenges);
                challengeComplete = new Boolean[numberOfChallenges];
                countComplete = 0;
                for(int i = 0 ; i < numberOfChallenges; i++) {
                    challengeComplete[i] = false;

                    challenges.add(FitnessChallengeLevel.get(readDb, mapView.board.player.getId(), currentNodeInfo.getChallengeID()));
                }

            }

            List<FitnessActivity> activities = FitnessActivity.getAllByDate(readDb, 1, mapView.board.player.getLastCheckedTime(), new Date());

            for (int i = 0; i < numberOfChallenges; i++) {
                if (!challengeComplete[i]) {
                    challengeComplete[i] = MapView.board.player.challengeIsCompleted(mapView.board.player.getLastCheckedTime(), new Date(), challenges.get(i));
                }
            }
            for (int i = 0; i < challengeComplete.length; i++) {
                tempMenuBodyText += challenges.get(i).toString();
                if (challengeComplete[i]) {
                    tempMenuBodyText += " Complete!\n";
                } else {
                    tempMenuBodyText += " Not Done\n";
                }
            }
            menuLeftButton.setText("Complete Challenges");
            menuLeftButton.setVisibility(View.VISIBLE);
        }
        else
        {
            tempMenuBodyText += "You have already completed the challenges here!";
            menuLeftButton.setVisibility(View.GONE);
        }
        menuBodyText.setText(tempMenuBodyText);
    }

    public void MoveCharacter()
    {
        if(!mapView.getIsTraveling()) {
            mapView.setDestinationNode(destinationNode);
            mapView.setIsTraveling(true);
            mapView.setTravelProgress(0);
            menuTravelProgressBar.setProgress(0);
            menuBodyText.setText("Travel Time Remaining: " + travelDuration / 1000 + " seconds");
            menuTravelProgressBar.setVisibility(View.VISIBLE);
            menuTravelFitnessLog.setVisibility(View.VISIBLE);
            menuTravelFitnessLog.setText("Recent Fitness Activities:");
            menuLeftButton.setVisibility(View.GONE);
            menuRightButton.setVisibility(View.GONE);
            StartMoving();
        }
    }


    public void MoveCharacter(View view)
    {
        //PS TODO Adjust to work off database
        if(!mapView.getIsTraveling()) {
            destinationNode = 3;
            mapNodes[0].setVisibility(View.VISIBLE);
            for (int i = 0; i < mapNodes.length; i++) {
                if (mapNodes[i] == view) {
                    destinationNode = i;
                    break;
                }
            }
            mapView.setDestinationNode(destinationNode);
            mapView.setIsTraveling(true);
            mapView.setTravelProgress(0);
            menuTravelProgressBar.setProgress(0);
            menuBodyText.setText("Travel Time Remaining: " + travelDuration/1000 + " seconds");
            menuTravelProgressBar.setVisibility(View.VISIBLE);
            menuTravelFitnessLog.setVisibility(View.VISIBLE);
            menuTravelFitnessLog.setText("Recent Fitness Activities:");
            menuLeftButton.setVisibility(View.GONE);
            menuRightButton.setVisibility(View.GONE);
            StartMoving();
        }
    }

    private void StartMoving()
    {
        //PS TODO move DB stuff to on create when updating travel on database
        startTravelTime = new Date();
        lastCheckedTime = new Date();
        endTravelTime = new Date();
        mapView.board.player.setLastCheckedTime(new Date());
        endTravelTime.setTime(startTravelTime.getTime()+travelDuration);
        MapPath path = mapView.getCurrentPath();
        path.setStartTime(mapDateFormat.format(startTravelTime));
        path.setEndTime(mapDateFormat.format(endTravelTime));
        HandleMoving();
    }

    private void HandleMoving()
    {
        final SQLiteDatabase readDb = myDB.getReadableDatabase();
        final Handler mapTravelHandler = new Handler();
        mapTravelHandler.postDelayed(new Runnable() {
            public void run() {
                    if(!quitHandler) {
                        Date currentTime = new Date();

                        travelProgress = (int) (currentTime.getTime() - startTravelTime.getTime());
                        mapView.setTravelProgress((travelProgress * 100) / (int) (endTravelTime.getTime() - startTravelTime.getTime()));
                        menuTravelProgressBar.setProgress(mapView.getTravelProgress());
                        menuBodyText.setText("Travel Time Remaining: " + (endTravelTime.getTime() - currentTime.getTime()) / 1000 + " seconds");
                        List<FitnessActivity> activities = FitnessActivity.getAllByDate(readDb, 1, startTravelTime, currentTime);
                        mapView.board.player.setLastCheckedTime(new Date());
                        mapView.board.player.dbPush();
                        menuTravelFitnessLog.setText("Recent Fitness Activities:");
                        for (int i = 0; i < Math.min(2, activities.size()); i++) {
                            menuTravelFitnessLog.append("\nDate: " + activities.get(i).getStartDate() + "\nActivity Type: " + activities.get(i).getType().getName());
                        }
                        if (currentTime.getTime() < endTravelTime.getTime()) {
                            mapTravelHandler.postDelayed(this, 500);
                        } else {
                            StopMoving();
                        }
                    }
                /*travelProgress += 500;
                mapView.setTravelProgress((travelProgress*100)/travelDuration);
                menuTravelProgressBar.setProgress((int)((travelProgress*100)/travelDuration));*/
            }
        }, 500);
    }

    private void StopMoving()
    {
        mapView.setCurrentNode(destinationNode);
        //PS TODO Move to travel complete
        mapView.board.player.dbPush();
        mapView.setIsTraveling(false);
        mapView.setTravelProgress(0);
        menuTravelProgressBar.setProgress(0);
        travelProgress = 0;
        TravelComplete();
    }

    private void TravelComplete()
    {
        int[] updatedStats = mapView.board.player.peekStatsFromActivities(startTravelTime, endTravelTime);
        final int strengthGain = updatedStats[0], enduranceGain = updatedStats[1], dexterityGain = updatedStats[2], speedGain = updatedStats[3], staminaGain = updatedStats[4] ;
        menuBodyText.setText("Travel Complete!\nGains: Sta +" + staminaGain + " Spd +" + speedGain + " Str +" + strengthGain + " End +" + enduranceGain + " Dex +" + dexterityGain);
        menuLeftButton.setVisibility(View.VISIBLE);
        if(mapView.board.getNodes().get(destinationNode).getIsBoss()==1)
        {
            menuLeftButton.setText(getResources().getString(R.string.combat_start_button));
        }
        menuLeftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapView.board.player.updateStatsFromActivities(startTravelTime, endTravelTime);
                mapView.board.player.dbPush();
                if(mapView.board.getNodes().get(destinationNode).getIsBoss()==1)
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
        Intent intent = new Intent(this, GameActivity.class);

        intent.putExtra("edu.uwm.cs.fitrpg.enemyStamina", baseEnemyStamina);

        intent.putExtra("edu.uwm.cs.fitrpg.enemyStrength", baseEnemyStrength);

        intent.putExtra("edu.uwm.cs.fitrpg.enemyEndurance", baseEnemyEndurance);

        intent.putExtra("edu.uwm.cs.fitrpg.enemyDexterity", baseEnemyDexterity);

        intent.putExtra("edu.uwm.cs.fitrpg.enemySpeed", baseEnemySpeed);

        quitHandler = true;
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), Home.class);
        startActivity(intent);
        navigationIDTag = 1;
        quitHandler = true;
        finish();
    }

    public BottomNavigationView.OnNavigationItemSelectedListener OnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    intent = new Intent(getApplicationContext(), Home.class);
                    startActivity(intent);
                    navigationIDTag = 1;
                    quitHandler = true;
                    finish();
                    return true;
                case R.id.navigation_fitness:
                    intent = new Intent(getApplicationContext(), FitnessOverview.class);
                    startActivity(intent);
                    navigationIDTag = 2;
                    quitHandler = true;
                    finish();
                    return true;
                case R.id.navigation_game_map:
                    navigationIDTag = 3;
                    return true;
                case R.id.navigation_settings:
                    intent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(intent);
                    navigationIDTag = 4;
                    quitHandler = true;
                    finish();
                    return true;
            }
            return false;
        }
    };
}
