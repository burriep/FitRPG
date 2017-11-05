package edu.uwm.cs.fitrpg;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
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
        for(int i = 0; i < numOfNodes; i++)
        {
            mapNodeImage[i] = getResources().getDrawable(R.drawable.map_node);

        }

    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        int canvasCenterWidth = canvas.getWidth()/2;
        int canvasCenterHeight = canvas.getHeight()/2;
        int distanceFromCenterWidth = canvasCenterWidth/2;
        int distanceFromCenterHeight = canvasCenterHeight/2;
        int[] nodePositions = new int[numOfNodes * 2];
        double angle = 0;
        float progress = 0.0f;
        float startX, startY, endX, endY, midX, midY;

        for(int i = 0; i < numOfNodes * 2; i += 2)
        {
            angle = 2 * Math.PI * (i/(double)(numOfNodes*2));
            nodePositions[i] = canvasCenterWidth - (int)Math.round(distanceFromCenterWidth * Math.cos(angle));
            nodePositions[i+1] = canvasCenterHeight - (int)Math.round(distanceFromCenterHeight * Math.sin(angle));
        }

        for(int i = 0; i < numOfNodes; i++)
        {
            for(int j = i; j < numOfNodes; j++)
            {
                if(isTraveling && ((i == currentNode && j == destinationNode) || (j == currentNode && i == destinationNode)))
                {
                    progress = travelProgress/100.0f;
                    if(i == currentNode) {
                        startX = nodePositions[i * 2];
                        startY = nodePositions[i * 2 + 1];
                        endX = nodePositions[j * 2];
                        endY = nodePositions[j * 2 + 1];
                    }
                    else
                    {
                        startX = nodePositions[j * 2];
                        startY = nodePositions[j * 2 + 1];
                        endX = nodePositions[i * 2];
                        endY = nodePositions[i * 2 + 1];
                    }
                    paint.setColor(Color.CYAN);
                    midX = ((endX - startX) * progress) + startX;
                    midY = ((endY - startY) * progress) + startY;
                    canvas.drawLine(startX, startY, midX, midY, paint);
                    paint.setColor(Color.BLACK);
                    canvas.drawLine(midX, midY, endX, endY, paint);
                }
                else
                {
                    canvas.drawLine(nodePositions[i * 2], nodePositions[i * 2 + 1], nodePositions[j * 2], nodePositions[j * 2 + 1], paint);
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
            mapNodeImage[i].setBounds(nodePositions[i*2]-(nodeSize/2), nodePositions[i*2+1]-(nodeSize/2), nodePositions[i*2]+(nodeSize/2), nodePositions[i*2+1]+(nodeSize/2));
            mapNodeImage[i].draw(canvas);
        }
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
}
