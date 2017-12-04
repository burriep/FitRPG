package edu.uwm.cs.fitrpg;

import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import java.util.Random;
import java.util.ArrayList;

import edu.uwm.cs.fitrpg.activity.Home;

/**
 * Created by Jason on 11/8/17.
 */

public class GameBoard {
    public ArrayList<MapNode> nodeList = new ArrayList<MapNode>();
    public ArrayList<MapPath> pathList = new ArrayList<MapPath>();
    private int mapID;
    private DatabaseHelper db;
    public RpgChar player;

    public GameBoard() {
        db = new DatabaseHelper(Home.appCon);
        this.player = new RpgChar();
        boolean exists = dbPull(this.player.getCurrentMap());

        if (!exists) {
            Log.d("DBG", "Gameboard not found - new gameboard created");
            generateNewBoard(this.player.getCurrentMap(), this.player.getId());

            this.mapID = this.player.getCurrentMap();
            this.addNode(new MapNode(2, this.mapID, 0, 33, 33, 66, 66));
            this.addNode(new MapNode(3, this.mapID, 0, 33, 33, 66, 66));
            this.addPath(2, 3);
            this.dbPush();
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
            db.updateNode(1, this.mapID, temp.getNodeId(), temp.getNodeStatus(), temp.getX(), temp.getY(), temp.getAdjX(), temp.getAdjY());
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
        ArrayList<int[]> temp = db.getMapData(1, this.mapID);
        //temp2 will hold all paths between the map's nodes
        ArrayList<String[]> temp2 = db.getPathData(map_id);

        //check if we have nodes
        if (temp != null && temp2 != null) {
            nodeList.clear();
            for (int[] x : temp) {
                this.nodeList.add(new MapNode(x[1], x[0], x[2], x[3], x[4], x[5], x[6]));
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

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| PATH METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
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

    public void generateNewBoard(int map_id, int user_id) {
        //todo 1) pick a number of nodes to generate
        Random rand = new Random();
        int newNumOfNodes = rand.nextInt((12 - 4) + 1) + 4;

        int maxX = 1080;
        int maxY = 1920;

        int xMaxA = maxX - 150;
        int xMinA = 150;
        int yMaxA = 1745;
        int yMinA = 1255;

        //starting coordinates of node
        int xStart = xMinA / 2;
        int yStart = yMaxA / 2;

        //coordinate values to assign to new node
        int x = 0;
        int y = 0;

        int partitionSpacing = 0;

        int prevMaxX = xMinA;
        boolean paritionScheme = true;

        //todo 2) create logic that dynamically pics which x,y to place node
        for (int i = 0; i < newNumOfNodes; i++) {
            boolean prevPartition = paritionScheme;

            partitionSpacing = xMaxA - xMinA / newNumOfNodes;
            paritionScheme = rand.nextBoolean();


            if (i == 0 || i == newNumOfNodes - 1) {
                if (i == newNumOfNodes - 1) {
                    //ending node
                    y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                    x = rand.nextInt((maxX - xMaxA) + 1) + xMaxA;

                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0));

                    //last node partition was a single node - only one path to add
                    if (prevPartition) {
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                    //last node partition was double nodes
                    else {
                        this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                } else {
                    //initial case - draw Node0 at static point
                    x = xStart;
                    y = yStart;

                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0));

                }

            } else {
                //single node in partition
                if (paritionScheme) {
                    y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                    x = rand.nextInt((prevMaxX + partitionSpacing - prevMaxX) + 1) + prevMaxX;

                    //last node partition was a single node - only one path to add
                    if (prevPartition) {
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                    //last node partition was double nodes
                    else {
                        this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }

                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0));
                    prevPartition = true;

                }
                //two nodes in a partition
                else {
                    y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                    x = rand.nextInt((prevMaxX + partitionSpacing - prevMaxX) + 1) + prevMaxX;
                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0));

                    i++;

                    y = rand.nextInt((yMaxA - yMinA) + 1) + yMinA;
                    x = rand.nextInt((prevMaxX + partitionSpacing - prevMaxX) + 1) + prevMaxX;
                    this.nodeList.add(new MapNode(i, map_id, 0, x, y, 0, 0));

                    //last node partition was a single node - only one path to add
                    if (prevPartition) {
                        this.pathList.add(new MapPath(map_id, i - 2, i - 1, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }
                    //last node partition was double nodes
                    else {
                        this.pathList.add(new MapPath(map_id, i - 3, i - 1, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 2, i, 0, "", ""));
                        this.pathList.add(new MapPath(map_id, i - 1, i, 0, "", ""));
                    }

                    prevPartition = false;
                }

                prevMaxX = prevMaxX + partitionSpacing;
            }

        }

        this.player.setCurrentNode(0);
        this.player.setCurrentMap(map_id);

        //todo 4) push paths, nodes, update player's current node
        this.player.dbPush();
        this.dbPush();

    }
}