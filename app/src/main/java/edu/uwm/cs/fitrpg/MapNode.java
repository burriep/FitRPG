package edu.uwm.cs.fitrpg;
import edu.uwm.cs.fitrpg.DatabaseHelper;
import edu.uwm.cs.fitrpg.view.HomeScreen;

import android.graphics.Point;

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
        try
        {
            this.mapID = map;
            this.nodeID = id;
            dbPull();
        }
        catch(Exception e)
        {
            this.nodeID = id;
            this.mapID = map;
            this.x = x;
            this.y = y;
            this.nodeComplete = 0;
            db = new DatabaseHelper(HomeScreen.appCon);
            dbPush();
        }
    }

    public int getX()
    {

        return this.x;
    }

    public int getY()
    {

        return this.y;
    }

    public void setX(int x)
    {

        this.x = x;
    }

    public void setY(int y)
    {

        this.y= y;
    }

    public int getNodeStatus()
    {

        return this.nodeComplete;
    }

    //1 for complete, 0 for playable
    public void setNodeStatus(int x)
    {

        this.nodeComplete = x;
    }

    public int getMapId()
    {
        return this.mapID;
    }

    public int getNodeId()
    {
        return this.nodeID;
    }

    public void dbPull()
    {
        Point temp = db.getNodeCoord(this.mapID, this.nodeID, 1);

        this.x = temp.x;
        this.y = temp.y;
    }

    public void dbPush()
    {
        db.setNodeCoord(new Point(this.x,this.y), this.mapID, this.nodeID, 1);
        db.changeNodeStatus(this.nodeComplete, this.mapID, this.nodeID, 1);
    }


}
