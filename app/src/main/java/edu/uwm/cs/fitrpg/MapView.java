package edu.uwm.cs.fitrpg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Ronike on 11/4/2017.
 * Modified by Jason K. on 11/27/17.
 */

public class MapView extends View {

    private int numOfNodes = 0;
    //private int currentNode = 0;
    private boolean isTraveling = false;
    private int destinationNode = 0;
    private int travelProgress = 0;
    private int bossNode = 0;
    private int nodeSize = 0;
    private Pair screenDimensions = new Pair(1080, 1920);
    public static GameBoard board;
    private int loopCount;
    private int map_id = 1;	//PS TODO Remove?


    private Paint paint = new Paint();
    private Drawable mapNodeImage;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("DBG", "In MapView Constructor");
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MapView,
                0, 0);
        //initialize gameboard - if map exists in db, you'll get that gameboard, else a brand new one
        //later we will pass in a map_id variable, instead of 1
        if(this.board == null)
        {
            board = new GameBoard();
        }
        loopCount = board.getLoopCount();
        Log.d("DBG", "In Mapview - loopcount:" + this.loopCount);

        try {

            numOfNodes = board.getNumNodes();
            //currentNode = board.player.getCurrentNode();
            //bossNode = a.getInteger(R.styleable.MapView_MapBossNode, 0);
            nodeSize = a.getInteger(R.styleable.MapView_MapNodeSize, 20);

            /*numOfNodes = a.getInteger(R.styleable.MapView_MapNumOfNodes, 0);
            currentNode = a.getInteger(R.styleable.MapView_MapStartingNode, 0);
            bossNode = a.getInteger(R.styleable.MapView_MapBossNode, 0);
            nodeSize = a.getInteger(R.styleable.MapView_MapNodeSize, 20);*/
        } finally {
            a.recycle();
        }
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(5f);

        double angle = 0;
        int canvasCenterWidth = (int)screenDimensions.first/2;
        int canvasCenterHeight = (int)screenDimensions.second/2;
        int distanceFromCenterWidth = canvasCenterWidth/2;
        int distanceFromCenterHeight = canvasCenterHeight/2;

        mapNodeImage = getResources().getDrawable(R.drawable.map_node);

        Log.d("DBG", "In MapView - end of constructor");
    }

    public void RefreshMap()
    {
        board = new GameBoard();
        loopCount = board.getLoopCount();
        numOfNodes = board.getNumNodes();
    }

    public int GetMapID()
    {
        return map_id;
    }

    public void SetMapID(int id)
    {
        map_id = id;
    }

    public void SetMultipleNodeConnections(int baseNode, Pair[] nodeConnectionPairs)
    {
        for(int i = 0; i < nodeConnectionPairs.length; i++)
        {
            //todo
            //SetNodeConnection(baseNode, (int)nodeConnectionPairs[i].first, (boolean)nodeConnectionPairs[i].second);
        }
    }

    public void SetAllNodeConnections(int baseNode, Boolean[] connections)
    {
        for(int i = 0; i < numOfNodes; i++)
        {
            for(int j =i; j<numOfNodes; j++)
            {
                if(board.nodeList.get(i) != board.nodeList.get(j))
                {
                    //todo
                }
            }
        }
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float adjustmentX = (float)canvas.getWidth()/(int)screenDimensions.first;
        float adjustmentY = (float)canvas.getHeight()/(int)screenDimensions.second;

        Log.d("DBG", "In MapView - in onDraw");

        for(int i = 0; i < numOfNodes; i++)
        {


            for(int j = i; j < numOfNodes; j++)
            {
                if((board.nodeList.get(i).getNodeId() != board.nodeList.get(j).getNodeId()) && board.isConnected(board.nodeList.get(i).getNodeId(),board.nodeList.get(j).getNodeId())){
                    canvas.drawLine(board.nodeList.get(i).getX() * adjustmentX, board.nodeList.get(i).getY() * adjustmentY,
                            board.nodeList.get(j).getX() * adjustmentX, board.nodeList.get(j).getY() * adjustmentY, paint);

                }
            }
        }
        for(int i = 0; i < numOfNodes; i++)
        {
            if(board.getNodes().get(i).getNodeId() == board.getNodes().get(board.player.getCurrentNode()).getNodeId())
            {
                mapNodeImage.setColorFilter(getResources().getColor(R.color.cyan), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else if (board.getNodes().get(i).getIsBoss() == 1)
            {
                mapNodeImage.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else
            {
                mapNodeImage.setColorFilter(getResources().getColor(R.color.gold), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            Log.d("DBG", "In MapView - in onDraw - Drawing Node " + i + " Converted: " + board.getNodes().get(i).getNodeId());


            board.nodeList.get(i).setAdjX((int)(board.nodeList.get(i).getX()* adjustmentX));
            board.nodeList.get(i).setAdjY((int)(board.nodeList.get(i).getY()* adjustmentY));
            mapNodeImage.setBounds(board.nodeList.get(i).getAdjX()-(nodeSize/2),board.nodeList.get(i).getAdjY()-(nodeSize/2),board.nodeList.get(i).getAdjX()+(nodeSize/2),board.nodeList.get(i).getAdjY()+(nodeSize/2));
            mapNodeImage.draw(canvas);

        }
    }

    public void AdjustImage()
    {
        invalidate();
        requestLayout();
    }



    public void setNumOfNodes(int val) {
        numOfNodes = val;
        AdjustImage();
    }

    public int getNumOfNodes() {
        return board.getNumNodes();
    }

    public int getCurrentNode()
    {
        return board.player.getCurrentNode();
    }

    public void setCurrentNode(int val) {
        Log.d("DBG", "In MapView - Arg Node: " + val + " Converted Node: " + (board.getNodes().get(val).getNodeId()));
        board.player.setCurrentNode(val);
        AdjustImage();
    }

    public boolean getIsTraveling()
    {
        boolean ret = false;

        for(int i=0; i<board.pathList.size();i++)
        {
            if(board.pathList.get(i).getStatus() == 1)
            {
                ret = true;
                isTraveling = true;
            }
        }

        if(ret == false)
        {
            isTraveling = false;
        }

        return ret;


    }

    public void setIsTraveling(boolean val) {

        //if not traveling, set all path travel statuses to 0
        if(!val)
        {
            for(int i=0; i< board.pathList.size();i++)
            {
                board.pathList.get(i).setStatus(0);
            }
            isTraveling = false;
        }
        //else set the path between current and destination's traveling status to 1
        else
        {
            for(int i=0; i< board.pathList.size();i++)
            {

                if((board.pathList.get(i).getNodeA() == board.player.getCurrentNode() && board.pathList.get(i).getNodeB() == destinationNode)||(board.pathList.get(i).getNodeB() == board.player.getCurrentNode() && board.pathList.get(i).getNodeA() == destinationNode))
                {
                    board.pathList.get(i).setStatus(1);
                    isTraveling = true;
                }
            }
        }
        AdjustImage();
    }

    public int getDestinationNode()
    {
        return destinationNode;
    }

    public void setDestinationNode(int val) {
        destinationNode = val;
        AdjustImage();
    }

    public int getTravelProgress()
    {
        return travelProgress;
    }

    public void setTravelProgress(int val) {
        travelProgress = val;
        AdjustImage();
    }

    public int getBossNode()
    {
        return bossNode;
    }

    public void setBossNode(int val) {
        bossNode = val;
        AdjustImage();
    }

    public int getNodeSize()
    {
        return nodeSize;
    }

    public void setNodeSize(int val)
    {
        nodeSize = val;
        AdjustImage();
    }

    public Boolean getConnectedToCurrentNode(int destNode)
    {
		ArrayList<MapNode> nodes = board.getNodes();

        Log.d("DBG", "In MapView - Current Node: " + board.player.getCurrentNode() + " Dest Node: " + board.getNodes().get(destNode).getNodeId());
        return board.isConnected(nodes.get(board.player.getCurrentNode()).getNodeId(), nodes.get(destNode).getNodeId());

    }

    //this method will be used for when a player completes a board. a new board will be dynamically created and will replace this mapview's board
    public void newBoard()
    {
        this.board = new GameBoard(this.board.player.getCurrentMap()+1);
    }
}
