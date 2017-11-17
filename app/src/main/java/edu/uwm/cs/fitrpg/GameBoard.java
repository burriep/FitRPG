package edu.uwm.cs.fitrpg;

import android.provider.ContactsContract;

import java.util.ArrayList;

import edu.uwm.cs.fitrpg.view.HomeScreen;

/**
 * Created by Jason on 11/8/17.
 */

public class GameBoard {
    private ArrayList<MapNode> nodeList;
    private int mapID;
    private DatabaseHelper db;

    public GameBoard(int map_id)
    {
        db = new DatabaseHelper(HomeScreen.appCon);

        try
        {
            dbPull(map_id);
        }
        catch(Exception e)
        {
            this.mapID = map_id;

        }
    }

    //this is only used for creating db entries for each of the map's nodes
    public void dbCreate()
    {
        try
        {
            for(MapNode temp : nodeList)
            {
                db.createNode(1, this.mapID, temp.getNodeId(), temp.getNodeStatus(), temp.getX(), temp.getY());
            }
        }
        catch(Exception e)
        {
            System.out.println("Error creating node entries - some may already exist");
        }
    }

    public void dbPush()
    {
        for(MapNode temp : nodeList)
        {
            db.updateNode(1, this.mapID, temp.getNodeId(), temp.getNodeStatus(), temp.getX(), temp.getY());
        }
    }

    public void dbPull(int map_id)
    {
        //1. select all rows that have usr_id, map_id
        //2. parse the return and populate the arraylist

        ArrayList<int[]> temp = db.getMapData(1, this.mapID);

        if(temp != null)
        {
            for(int[] x: temp)
            {
                this.nodeList.add(new MapNode(x[1], x[0], x[2], x[3], x[4]));
            }
        }
        else
        {
            System.out.println("No Data Found");
        }

    }


}
