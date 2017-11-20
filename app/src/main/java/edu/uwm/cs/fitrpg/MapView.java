package edu.uwm.cs.fitrpg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
    private Pair[] nodePositions;
    private Pair[] adjustedNodePositions;
    private Pair screenDimensions = new Pair(1080, 1920);
    private Boolean[][] nodeConnections;


    private Paint paint = new Paint();
    private Drawable[] mapNodeImage;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MapView,
                0, 0);

        try {
            numOfNodes = a.getInteger(R.styleable.MapView_MapNumOfNodes, 0);
            currentNode = a.getInteger(R.styleable.MapView_MapStartingNode, 0);
            bossNode = a.getInteger(R.styleable.MapView_MapBossNode, 0);
            nodeSize = a.getInteger(R.styleable.MapView_MapNodeSize, 20);
        } finally {
            a.recycle();
        }
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5f);

        mapNodeImage = new Drawable[numOfNodes];
        nodePositions = new Pair[numOfNodes];
        adjustedNodePositions = new Pair[numOfNodes];
        nodeConnections = new Boolean[numOfNodes][numOfNodes];
        double angle = 0;
        int canvasCenterWidth = (int)screenDimensions.first/2;
        int canvasCenterHeight = (int)screenDimensions.second/2;
        int distanceFromCenterWidth = canvasCenterWidth/2;
        int distanceFromCenterHeight = canvasCenterHeight/2;

        for(int i = 0; i < numOfNodes; i++)
        {
            mapNodeImage[i] = getResources().getDrawable(R.drawable.map_node);
            angle = 2 * Math.PI * (i/(double)(numOfNodes));
            nodePositions[i] = new Pair(canvasCenterWidth - (int)Math.round(distanceFromCenterWidth * Math.cos(angle)),
                    canvasCenterHeight - (int)Math.round(distanceFromCenterHeight * Math.sin(angle)));
            adjustedNodePositions[i] = new Pair(50,0);
            for(int j = 0; j < numOfNodes; j++)
            {
                nodeConnections[i][j] = i != j;
            }
        }

    }

    public void SetNodeConnection(int baseNode, int connectedNode, Boolean isConnected)
    {
        if(baseNode != connectedNode && baseNode < numOfNodes && connectedNode < numOfNodes)
        {
            nodeConnections[baseNode][connectedNode] = isConnected;
            nodeConnections[connectedNode][baseNode] = isConnected;
        }
    }

    public void ToggleNodeConnections(int baseNode, int connectedNode)
    {
        if(baseNode != connectedNode && baseNode < numOfNodes && connectedNode < numOfNodes)
        {
            nodeConnections[baseNode][connectedNode] = ! nodeConnections[baseNode][connectedNode];
            nodeConnections[connectedNode][baseNode] = ! nodeConnections[connectedNode][baseNode];
        }
    }

    public void SetMultipleNodeConnections(int baseNode, Pair[] nodeConnectionPairs)
    {
        for(int i = 0; i < nodeConnectionPairs.length; i++)
        {
            SetNodeConnection(baseNode, (int)nodeConnectionPairs[i].first, (boolean)nodeConnectionPairs[i].second);
        }
    }

    public void SetAllNodeConnections(int baseNode, Boolean[] connections)
    {
        for(int i = 0; i < connections.length; i++)
        {
            SetNodeConnection(baseNode, i, connections[i]);
        }
    }

    public void ChangeNodePosition(int nodeToChangePos, Pair newPosition)
    {
        if(nodeToChangePos < numOfNodes)
        {
            nodePositions[nodeToChangePos] = new Pair(newPosition.first, newPosition.second);

        }
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        float progress = 0.0f;
        float startX, startY, endX, endY, midX, midY;
        float adjustmentX = (float)canvas.getWidth()/(int)screenDimensions.first;
        float adjustmentY = (float)canvas.getHeight()/(int)screenDimensions.second;

        for(int i = 0; i < numOfNodes; i++)
        {
            for(int j = i; j < numOfNodes; j++)
            {
                if(i != j && nodeConnections[i][j]) {
                    if (isTraveling && ((i == currentNode && j == destinationNode) || (j == currentNode && i == destinationNode))) {
                        progress = travelProgress / 100.0f;
                        if (i == currentNode) {
                            startX = (int) nodePositions[i].first * adjustmentX;
                            startY = (int) nodePositions[i].second * adjustmentY;
                            endX = (int) nodePositions[j].first * adjustmentX;
                            endY = (int) nodePositions[j].second * adjustmentY;
                        } else {
                            startX = (int) nodePositions[j].first * adjustmentX;
                            startY = (int) nodePositions[j].second * adjustmentY;
                            endX = (int) nodePositions[i].first * adjustmentX;
                            endY = (int) nodePositions[i].second * adjustmentY;
                        }
                        paint.setColor(Color.CYAN);
                        midX = ((endX - startX) * progress) + startX;
                        midY = ((endY - startY) * progress) + startY;
                        canvas.drawLine(startX, startY, midX, midY, paint);
                        paint.setColor(Color.BLACK);
                        canvas.drawLine(midX, midY, endX, endY, paint);
                    } else {
                        canvas.drawLine((int) nodePositions[i].first * adjustmentX, (int) nodePositions[i].second * adjustmentY,
                                (int) nodePositions[j].first * adjustmentX, (int) nodePositions[j].second * adjustmentY, paint);
                    }
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
            adjustedNodePositions[i] = new Pair((int)((int)nodePositions[i].first * adjustmentX), (int)((int)nodePositions[i].second * adjustmentY));
            mapNodeImage[i].setBounds((int)adjustedNodePositions[i].first-(nodeSize/2), (int)adjustedNodePositions[i].second-(nodeSize/2),
                    (int)adjustedNodePositions[i].first +(nodeSize/2), (int)adjustedNodePositions[i].second +(nodeSize/2));
            mapNodeImage[i].draw(canvas);
        }
    }

    public Pair getNodePosition(int nodeNum)
    {
        return nodePositions[nodeNum];
    }

    public Pair getAdjustedNodePosition(int nodeNum)
    {
        return adjustedNodePositions[nodeNum];
    }

    public int getNumOfNodes()
    {
        return numOfNodes;
    }

    public void setNumOfNodes(int val) {
        numOfNodes = val;
        invalidate();
        requestLayout();
    }

    public int getCurrentNode()
    {
        return currentNode;
    }

    public void setCurrentNode(int val) {
        currentNode = val;
        invalidate();
        requestLayout();
    }

    public boolean getIsTraveling()
    {
        return isTraveling;
    }

    public void setIsTraveling(boolean val) {
        isTraveling = val;
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
        return nodeConnections[currentNode][destinationNode];
    }
}
