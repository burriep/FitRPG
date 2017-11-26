package edu.uwm.cs.fitrpg;

import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import edu.uwm.cs.fitrpg.activity.Home;

/**
 * Created by Jason on 11/8/17.
 */

public class GameBoard {
    private ArrayList<MapNode> nodeList = new ArrayList<MapNode>();
    private int mapID;
    private DatabaseHelper db;
    private int loopCount = 0;

    public GameBoard(int map_id)
    {
        db = new DatabaseHelper(Home.appCon);
        boolean exists = dbPull(map_id);

        if(!exists)
        {
            this.mapID = map_id;
            this.dbPush();
        }
    }

    public void addNode(MapNode x)
    {
        this.nodeList.add(x);
        Log.d("SCS", "Node Added to GameBoard");
    }

    public ArrayList<MapNode> getNodes()
    {
        return this.nodeList;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| DATABASE METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/

    public void dbPush()
    {
        Log.d("DBG", "in GameBoard dbPush");
        for(MapNode temp : nodeList)
        {
            db.updateNode(1, this.mapID, temp.getNodeId(), temp.getNodeStatus(), temp.getX(), temp.getY(), this.loopCount);
        }
    }

    public boolean dbPull(int map_id)
    {
        //1. select all rows that have usr_id, map_id
        //2. parse the return and populate the arraylist
        Log.d("DBG", "in GameBoard dbPull");
        boolean ret = false;

        ArrayList<int[]> temp = db.getMapData(1, this.mapID);

        if(temp != null)
        {
            for(int[] x: temp)
            {
                this.nodeList.add(new MapNode(x[1], x[0], x[2], x[3], x[4]));
            }
            ret = true;
        }
        else
        {
            System.out.println("No Data Found");
        }

        return ret;
    }


}
