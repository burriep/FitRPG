package edu.uwm.cs.fitrpg;
import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.activity.Home;

import android.graphics.Point;
import android.util.Log;

/**
 * Created by Jason on 11/8/17.
 *
 * This class will represent each individual node that will be drawn on the map
 */

public class MapNode {
    private int nodeID;
    private int mapID;
    private int x;
    private int y;
    private int adjX;
    private int adjY;
    private int nodeComplete;
    private DatabaseHelper db;
    private int isBoss = 0;

    public MapNode(int id, int map,int cmp, int x, int y, int adjX, int adjY)
    {
        db = new DatabaseHelper(Home.appCon);

        this.mapID = map;
        this.nodeID = id;
        boolean exists = dbPull();
        if(!exists)
        {
            this.nodeID = id;
            this.mapID = map;
            this.x = x;
            this.y = y;
            this.adjX = adjX;
            this.adjY = adjY;
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

    public int getAdjX()
    {

        return this.adjX;
    }

    public int getAdjY()
    {

        return this.adjY;
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
    public int getIsBoss(){return isBoss;}

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

    public void setAdjX(int x)
    {

        this.adjX = x;
    }

    public void setAdjY(int y)
    {

        this.adjY= y;
    }

    public void setIsBoss(int flag)
    {
        isBoss = flag;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| DATABASE METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public boolean dbPull()
    {
        Log.d("DBG", "in MapNode dbPull");
        int[] retCoords = db.getNodeCoord(this.mapID, this.nodeID, 1);
        int retStatus = db.getNodeStatus(this.mapID, this.nodeID, 1);

        if(retCoords != null && retStatus > -1)
        {
            this.x = retCoords[0];
            this.y = retCoords[1];
            this.adjX = retCoords[2];
            this.adjY = retCoords[3];
            this.nodeComplete = retStatus;
            return true;
        }
        else
        {
            Log.d("DBG", "dbPull was not successful");
            return false;
        }
    }

    public void dbPush()
    {
        Log.d("DBG", "in MapNode dbPush");
        db.setNodeCoord(new Point(this.x,this.y), new Point(this.adjX,this.adjY), this.mapID, this.nodeID, 1,this.nodeComplete);
    }
}
