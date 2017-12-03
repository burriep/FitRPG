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

/**
 * Created by Ronike on 11/4/2017.
 */

public class MapView extends View {

    private int numOfNodes = 0;
    private int currentNode = 0;
    private boolean isTraveling = false;
    private int destinationNode = 0;
    private int travelProgress = 0;
    private int bossNode = 0;
    private int nodeSize = 0;
    private Pair screenDimensions = new Pair(1080, 1920);
    public static GameBoard board;
    private int loopCount;


    private Paint paint = new Paint();
    private Drawable[] mapNodeImage;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("DBG", "In MapView Constructor");
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MapView,
                0, 0);
        //initialize gameboard - if map exists in db, you'll get that gameboard, else a brand new one
        //later we will pass in a map_id variable, instead of 1
        board = new GameBoard(1);
        loopCount = board.getLoopCount();
        Log.d("DBG", "In Mapview - loopcount:" + this.loopCount);

        try {

            numOfNodes = board.getNumNodes();
            currentNode = board.player.getCurrentNode();
            bossNode = a.getInteger(R.styleable.MapView_MapBossNode, 0);
            nodeSize = a.getInteger(R.styleable.MapView_MapNodeSize, 20);

            /*numOfNodes = a.getInteger(R.styleable.MapView_MapNumOfNodes, 0);
            currentNode = a.getInteger(R.styleable.MapView_MapStartingNode, 0);
            bossNode = a.getInteger(R.styleable.MapView_MapBossNode, 0);
            nodeSize = a.getInteger(R.styleable.MapView_MapNodeSize, 20);*/
        } finally {
            a.recycle();
        }
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);

        mapNodeImage = new Drawable[numOfNodes];
        double angle = 0;
        int canvasCenterWidth = (int)screenDimensions.first/2;
        int canvasCenterHeight = (int)screenDimensions.second/2;
        int distanceFromCenterWidth = canvasCenterWidth/2;
        int distanceFromCenterHeight = canvasCenterHeight/2;

        if(numOfNodes > 0)
        {
            for(int i = 0; i < numOfNodes; i++)
            {

                mapNodeImage[i] = getResources().getDrawable(R.drawable.map_node);
                angle = 2 * Math.PI * (i/(double)(numOfNodes));
                board.nodeList.get(i).setX(canvasCenterWidth - (int)Math.round(distanceFromCenterWidth * Math.cos(angle)));
                board.nodeList.get(i).setY(canvasCenterHeight - (int)Math.round(distanceFromCenterHeight * Math.sin(angle)));

            }
        }
        else
        {
            //todo need to create nodes
        }

        Log.d("DBG", "In MapView - end of constructor");
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
        float progress = 0.0f;
        float startX, startY, endX, endY, midX, midY;
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
            if(i == currentNode)
            {
                mapNodeImage[i].setColorFilter(getResources().getColor(R.color.cyan), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else if (i == bossNode)
            {
                mapNodeImage[i].setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
            }
            else
            {
                mapNodeImage[i].setColorFilter(getResources().getColor(R.color.gold), android.graphics.PorterDuff.Mode.MULTIPLY);
            }

            board.nodeList.get(i).setAdjX(board.nodeList.get(i).getX()* (int)adjustmentX);
            board.nodeList.get(i).setAdjY(board.nodeList.get(i).getY()* (int)adjustmentY);

            mapNodeImage[i].setBounds(board.nodeList.get(i).getAdjX()-(nodeSize/2),board.nodeList.get(i).getAdjY()-(nodeSize/2),board.nodeList.get(i).getAdjX()+(nodeSize/2),board.nodeList.get(i).getAdjY()+(nodeSize/2));
            mapNodeImage[i].draw(canvas);

        }
    }


    public void setNumOfNodes(int val) {
        numOfNodes = val;
        invalidate();
        requestLayout();
    }

    public int getNumOfNodes() {
        return board.getNumNodes();
    }

    public int getCurrentNode()
    {
        return board.player.getCurrentNode();
    }

    public void setCurrentNode(int val) {
        board.player.setCurrentNode(val);
        invalidate();
        requestLayout();
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
                if((board.pathList.get(i).getNodeA() == currentNode && board.pathList.get(i).getNodeB() == destinationNode)||(board.pathList.get(i).getNodeB() == currentNode && board.pathList.get(i).getNodeA() == destinationNode))
                {
                    board.pathList.get(i).setStatus(1);
                    isTraveling = true;
                }
            }
        }
        invalidate();
        requestLayout();
    }

    public int getDestinationNode()
    {
        return destinationNode;
    }

    public void setDestinationNode(int val) {
        destinationNode = val;
        invalidate();
        requestLayout();
    }

    public int getTravelProgress()
    {
        return travelProgress;
    }

    public void setTravelProgress(int val) {
        travelProgress = val;
        invalidate();
        requestLayout();
    }

    public int getBossNode()
    {
        return bossNode;
    }

    public void setBossNode(int val) {
        bossNode = val;
        invalidate();
        requestLayout();
    }

    public int getNodeSize()
    {
        return nodeSize;
    }

    public void setNodeSize(int val)
    {
        nodeSize = val;
        invalidate();
        requestLayout();
    }

    public Boolean getConnectedToCurrentNode(int destinationNode)
    {
        return board.isConnected(board.player.getCurrentNode(),destinationNode);

    }
}
