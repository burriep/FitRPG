package edu.uwm.cs.fitrpg;
import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.view.HomeScreen;

import android.graphics.Point;
import android.util.Log;

/**
 * Created by Jason on 11/8/17.
 */

public class MapNode {
    private int nodeID;
    private int mapID;
    private int x;
    private int y;
    private int nodeComplete;
    private DatabaseHelper db;

    public MapNode(int id, int map,int cmp, int x, int y)
    {
        db = new DatabaseHelper(HomeScreen.appCon);

        this.mapID = map;
        this.nodeID = id;
        boolean exists = dbPull();
        if(!exists)
        {
            this.nodeID = id;
            this.mapID = map;
            this.x = x;
            this.y = y;
            this.nodeComplete = 0;
            dbPush();
        }
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| GETTER METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public int getX()
    {

        return this.x;
    }

    public int getY()
    {

        return this.y;
    }



    public int getNodeStatus()
    {

        return this.nodeComplete;
    }
    public int getMapId()
    {
        return this.mapID;
    }

    public int getNodeId()
    {
        return this.nodeID;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| SETTER METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    //1 for complete, 0 for playable
    public void setNodeStatus(int x)
    {

        this.nodeComplete = x;
    }

    public void setX(int x)
    {

        this.x = x;
    }

    public void setY(int y)
    {

        this.y= y;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| DATABASE METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public boolean dbPull()
    {
        Log.d("DBG", "in MapNode dbPull");
        Point retCoords = db.getNodeCoord(this.mapID, this.nodeID, 1);
        int retStatus = db.getNodeStatus(this.mapID, this.nodeID, 1);

        if(retCoords != null && retStatus > -1)
        {
            this.x = retCoords.x;
            this.y = retCoords.y;
            this.nodeComplete = retStatus;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void dbPush()
    {
        Log.d("DBG", "in MapNode dbPush");
        db.setNodeCoord(new Point(this.x,this.y), this.mapID, this.nodeID, 1,this.nodeComplete);
    }

}
