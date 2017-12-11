package edu.uwm.cs.fitrpg;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;

import edu.uwm.cs.fitrpg.activity.Home;

import static java.lang.Math.abs;

/**
 * Created by Jason on 11/8/17.
 *
 * This class wil represent the gameboard that the user can play on. It will include the nodes, paths between nodes, and the character associated with the board.
 */

public class GameBoard {
    public ArrayList<MapNode> nodeList = new ArrayList<MapNode>();
    public ArrayList<MapPath> pathList = new ArrayList<MapPath>();
    private int mapID;
    private DatabaseHelper db;
    public RpgChar player;
    final private WindowManager w = (WindowManager) Home.appCon.getSystemService(Home.appCon.WINDOW_SERVICE);
    final private Display d = w.getDefaultDisplay();
    final private DisplayMetrics screenInfo = new DisplayMetrics();


    public GameBoard() {
        db = new DatabaseHelper(Home.appCon);
        this.player = new RpgChar();
        boolean exists = dbPull(this.player.getCurrentMap());

        if (!exists) {
            Log.d("DBG", "Gameboard not found - new gameboard created");
            generateNewBoard(this.player.getCurrentMap(), this.player.getId());

            this.mapID = this.player.getCurrentMap();

        } else {
            Log.d("DBG", "Gameboard found");
        }
    }

    public GameBoard(int x)
    {
        db = new DatabaseHelper(Home.appCon);
        this.player = new RpgChar();
        boolean exists = dbPull(x);

        if (!exists) {
            Log.d("DBG", "Gameboard not found - new gameboard created");
            generateNewBoard(x, this.player.getId());
        } else {
            Log.d("DBG", "Gameboard found");
        }
    }

    public void addNode(MapNode x) {
        this.nodeList.add(x);
        Log.d("SCS", "Node Added to GameBoard");
    }

    public int getNumNodes() {
        Log.d("DBG", "In Gameboard - returning number of nodes");
        return this.nodeList.size();
    }


    public ArrayList<MapNode> getNodes() {
        return this.nodeList;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| DATABASE METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/

    public void dbPush() {
        Log.d("DBG", "in GameBoard dbPush - Pushing Nodes");
        this.printNodes();
        for (MapNode temp : this.nodeList) {
            db.updateNode(1, this.mapID, temp.getNodeId(), temp.getNodeStatus(), temp.getX(), temp.getY(), temp.getAdjX(), temp.getAdjY(), temp.getChallengeID(), temp.getIsBoss());
        }

        Log.d("DBG", "in GameBoard dbPush - Pushing Paths");
        this.printPaths();
        this.printPaths();
        for (MapPath temp : this.pathList) {
            db.updatePath(this.mapID, temp.getNodeA(), temp.getNodeB(), temp.getStatus(), temp.getStartTime(), temp.getEndTime());
        }
    }

    public boolean dbPull(int map_id) {
        //1. select all rows that have usr_id, map_id
        //2. parse the return and populate the arraylist
        Log.d("DBG", "in GameBoard dbPull");
        boolean ret = false;

        //temp will hold all nodes associated with the map
        ArrayList<int[]> temp = db.getMapData(1, map_id);
        //temp2 will hold all paths between the map's nodes
        ArrayList<String[]> temp2 = db.getPathData(map_id);

        //check if we have nodes
        if (temp != null && temp2 != null) {
            nodeList.clear();
            for (int[] x : temp) {
                this.nodeList.add(new MapNode(x[1], x[0], x[2], x[3], x[4], x[5], x[6], x[7], x[8]));
            }

            //check if we have paths
            pathList.clear();
            for (String[] x : temp2) {
                this.pathList.add(new MapPath(Integer.parseInt(x[0]), Integer.parseInt(x[1]), Integer.parseInt(x[2]), Integer.parseInt(x[3]), x[4], x[5]));
            }

            this.printNodes();
            this.printPaths();
            ret = true;
        }

        return ret;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| GETTERS AND SETTERS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public boolean isConnected(int a, int b) {
        Log.d("DBG", "In gameboard - checking it path exists between " + a + "," + b);
        boolean ret = false;

        for (MapPath temp : this.pathList) {
            if ((temp.getNodeA() == a && temp.getNodeB() == b) || (temp.getNodeA() == b && temp.getNodeB() == a)) {
                ret = true;
                Log.d("DBG", "Path exists");
                break;
            }
        }

        return ret;
    }

    public void addPath(int baseNode, int connectedNode) {

        Log.d("DBG", "In GameBoard - setting node connection between " + baseNode + "," + connectedNode);
        if (baseNode != connectedNode && !this.isConnected(baseNode, connectedNode)) {
            this.pathList.add(new MapPath(this.getmapId(), baseNode, connectedNode, 0, "x", "x"));
            Log.d("SCS", "Path Added to GameBoard");
        }

        this.printPaths();

    }

    public void removePath(int a, int b) {
        Log.d("DBG", "In gameboard - removing path between " + a + "," + b);
        int[] found = new int[]{-1, -1};
        int j = 0;

        for (int i = 0; i < this.pathList.size(); i++) {
            if ((this.pathList.get(i).getNodeA() == a && this.pathList.get(i).getNodeB() == b) || (this.pathList.get(i).getNodeA() == b && this.pathList.get(i).getNodeB() == a)) {
                found[j] = i;
                j++;
            }
        }

        if (found[1] != -1) {
            this.pathList.remove(found[1]);
            db.deletePath(this.mapID, b, a);
            Log.d("DBG", "removed path[1] from pathlist");
        }
        if (found[0] != -1) {
            this.pathList.remove(found[0]);
            db.deletePath(this.mapID, a, b);
            Log.d("DBG", "removed path[0] from pathlist");
        } else {
            Log.d("DBG", "Path was not in pathlist - could not remove");
        }

    }

    public int getmapId() {
        Log.d("DBG", "In gameboard - returning mapId");
        return this.mapID;
    }

    public int getLoopCount() {
        Log.d("DBG", "In gameboard - returning loopCount");
        return this.player.getLoopCount();
    }

    public void toggleConnection(int baseNode, int connectedNode) {
        Log.d("DBG", "In MapView - toggling node connection between " + baseNode + "," + connectedNode);

        if (baseNode != connectedNode && this.isConnected(baseNode, connectedNode)) {
            this.removePath(baseNode, connectedNode);
        } else {
            addPath(baseNode, connectedNode);
        }
    }

    public void ChangeNodePosition(int nodeToChangePos, Pair newPosition) {
        Log.d("DBG", "In MapView - changing node position " + nodeToChangePos + ",(" + newPosition.first + "," + newPosition.second + ")");
        for (int i = 0; i < this.nodeList.size(); i++) {
            if (this.nodeList.get(i).getNodeId() == nodeToChangePos) {
                //TODO might be normal x and y
                this.nodeList.get(i).setX((int) newPosition.first);
                this.nodeList.get(i).setY((int) newPosition.second);
            }
        }
    }

    public Pair getNodePosition(int nodeNum) {
        Pair ret = null;
        for (int i = 0; i < this.nodeList.size(); i++) {
            if (this.nodeList.get(i).getNodeId() == nodeNum) {
                //TODO might be normal x and y
                ret = new Pair(this.nodeList.get(i).getX(), this.nodeList.get(i).getY());
            }
        }
        return ret;
    }

    public Pair getAdjustedNodePosition(int nodeNum) {
        Pair ret = null;
        Log.d("DBG", "In mapView - getting adjusted node position for node: " + nodeNum);
        for (int i = 0; i < this.nodeList.size(); i++) {
            if (this.nodeList.get(i).getNodeId() == nodeNum) {
                ret = new Pair(this.nodeList.get(i).getAdjX(), this.nodeList.get(i).getAdjY());
            }
        }

        return ret;
    }

    public void printPaths() {
        for (MapPath x : this.pathList) {
            Log.d("DBG", "Path: (" + x.getNodeA() + "," + x.getNodeB() + ")");
        }
    }

    public void printNodes() {
        for (MapNode x : this.nodeList) {
            Log.d("DBG", "Node: (" + x.getNodeId() + "," + x.getMapId() + ")");
        }
    }

    /*||||||||||||||||||||||||||||||||||||||||| GAMEBOARD AND NODE/PATH DYNAMIC GENERATION METHOD ||||||||||||||||||||||||||||||||||||||||||||||*/
    public void generateNewBoard(int map_id, int user_id) {

        d.getMetrics(screenInfo);
        Log.d("DBG", "Device Screen Dimensions: H: " + screenInfo.heightPixels + ", W: " + screenInfo.widthPixels);
        //generate the number of nodes to be placed on the gameboard
        Random rand = new Random();
        int newNumOfNodes = rand.nextInt((11 - 4) + 1) + 4;

        int maxX = screenInfo.widthPixels;
        int maxY = screenInfo.heightPixels;

        //viewport area that nodes can be drawn in
        int xMaxA = maxX - 50;
        int xMinA = 50;
        int yMaxA = maxY - 50;
        int yMinA = 150;

        //starting coordinates of node
        int xStart = xMinA + 25;
        int yStart = yMaxA / 2;

        //coordinate values to assign to new node
        int x = 0;
        int y = 0;

        //in double node situations - this will keep track of the first node, so that the second can be drawn in relation to it
        int prevY = 0;

        int partitionSpacing = ((xMaxA - xMinA) / newNumOfNodes);
        Log.d("DBG", "numofNodes: " + newNumOfNodes + ", spacing: " + partitionSpacing);

        //the minX of the current partition
        int prevMaxX = xStart;
        //true for 1 node in a partition, false for 2
        boolean paritionScheme = true;

        //dynamically create the coordinates for the nodes and create relevant paths between them
        for (int i = 1; i <= newNumOfNodes; i++)
        {
            Log.d("DBG", "i: " + i);
            //keeps track of the previous partition's structure (one or two nodes)
            boolean prevPartition = paritionScheme;
            //used to bound the node's y from
            int distFromMax = 0;
            int distFromMin = 0;

            //randomly pick if this partition should have 1 or 2 nodes (true == one, false == two)
            paritionScheme = rand.nextBoolean();

            //this handles the first and last node
            if (i == 1 || i == newNumOfNodes)
            {
                if (i == newNumOfNodes)
                {
                    //coordinates for ending node (Situation 1 of 2)
                    y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                    x = rand.nextInt(((maxX-50) - xMaxA) + 1) + xMaxA;
                    MapNode bossNode = new MapNode(i, map_id, 0, x, y, 0, 0,-1,1);
                    this.nodeList.add(bossNode);
                    Log.d("DBG", "End Node " + i + " New x: " + x + ", New y: "+ y);

                    //last node partition was a single node - only one path to add
                    if (prevPartition)
                    {
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                    //last node partition was double nodes
                    else
                    {
                        this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                }
                else
                {
                    //initial case - draw Node0 at static point
                    x = xStart;
                    y = yStart;

                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0,-1,0));
                    Log.d("DBG", "Start Node New x: " + x + ", New y: "+ y);

                }

            }
            //this handles all internal nodes
            else
            {
                //single node in partition
                if (paritionScheme)
                {
                    y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                    x = rand.nextInt(((prevMaxX + partitionSpacing) - (prevMaxX+50)) + 1) + (prevMaxX+50);

                    //last node partition was a single node - only one path to add
                    if (prevPartition)
                    {
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                    //last node partition was double nodes
                    else
                    {
                        this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                    Log.d("DBG", "Single New x: " + x + ", New y: "+ y);
                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0,-1,0));
                    paritionScheme = true;

                }
                //two nodes in a partition
                else
                {
                    //coordinates for nodeA
                    y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                    x = rand.nextInt(((prevMaxX + partitionSpacing) - (prevMaxX+50)) + 1) + (prevMaxX+50);
                    prevY = y;
                    Log.d("DBG", "Double New x: " + x + ", New y: "+ y);
                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0,-1,0));

                    i++;

                    //first coordinates to try for nodeB
                    y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                    x = rand.nextInt(((prevMaxX + partitionSpacing) - (prevMaxX+50)) + 1) + (prevMaxX+50);

                    //draw the second node in relation to the first
                    //nodeB is above nodeA
                    if(y < prevY)
                    {
                        //will nodeB be smaller than the minY with extra space
                        if(y > yMinA+100)
                        {
                            y -= 100;
                        }
                        //nodeB is close to minY - don't add extra space
                        else
                        {
                            ;
                        }
                    }
                    //nodeB is below nodeA
                    else if(y > prevY)
                    {
                        //will nodeB be larger than the maxY with extra space
                        if(y < yMaxA-100)
                        {
                            y += 100;
                        }
                        //nodeB is close to maxY - don't add extra space
                        else
                        {
                            ;
                        }

                    }
                    //y and prevY are the exact same
                    else
                    {
                        distFromMax = abs(yMaxA-y);
                        distFromMin = abs(yMinA-y);

                        //nodeB is closer to the minY - move towards max
                        if(distFromMax > distFromMin)
                        {
                            y += 100;
                        }
                        //nodeB is closer to maxY - move towards min
                        else if(distFromMax < distFromMin)
                        {
                             y -= 150;
                        }
                        //nodeB is right in the middle - move in either direction
                        else
                        {
                             y += 150;
                        }
                    }
                    Log.d("DBG", "Double New x2: " + x + ", New y2: "+ y);
                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0,-1,0));

                    //last node partition was a single node - only one path to add
                    if (prevPartition)
                    {
                        this.pathList.add(new MapPath(map_id, i - 2, i - 1, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                    //last node partition was double nodes
                    else
                    {
                        this.pathList.add(new MapPath(map_id, i - 3, i - 1, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }

                    //coordinates for ending node (Situation 2 of 2)
                    if(i >= newNumOfNodes)
                    {
                        i++;

                        y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                        x = rand.nextInt(((maxX-50) - xMaxA) + 1) + xMaxA;

                        MapNode bossNode = new MapNode(i, map_id, 0, x, y, 0, 0,-1,1);
                        this.nodeList.add(bossNode);
                        Log.d("DBG", "End Node2 New x: " + x + ", New y: "+ y);

                        //last node partition was a single node - only one path to add
                        if (prevPartition)
                        {
                            this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                        }
                        //last node partition was double nodes
                        else
                        {
                            this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                            this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                        }
                    }

                    paritionScheme = false;
                }

            }

            Log.d("DBG", "prevmaxX: " + prevMaxX);
            prevMaxX = prevMaxX + partitionSpacing;
            Log.d("DBG", "prevmaxX after adding partition space: " + prevMaxX);

        }

        this.player.setCurrentNode(0);
        this.player.setCurrentMap(map_id);
        this.mapID = map_id;

        this.player.dbPush();
        this.dbPush();

    }
}

