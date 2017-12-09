package edu.uwm.cs.fitrpg;

import android.util.Log;

/**
 * Created by Jason on 11/28/17.
 *
 * This class will represent all of the logical paths between two connected nodes
 */

public class MapPath {
    private int mapId;
    private int nodeA;
    private int nodeB;
    private int travelStatus;
    private String startTime;
    private String endTime;


    MapPath(int mapId, int nodeA, int nodeB, int travelStatus, String startTime, String endTime)
    {
        this.mapId = mapId;
        this.nodeA = nodeA;
        this.nodeB = nodeB;
        this.travelStatus = travelStatus;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    /*|||||||||||||||||||||||||||||||||||||||||||||||||| GETTER METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public int getNodeA(){
        return this.nodeA;
    }

    public int getNodeB(){
        return this.nodeB;
    }
    public int getStatus(){
        return this.travelStatus;
    }

    public String getStartTime(){
        return this.startTime;
    }

    public String getEndTime(){
        return this.endTime;
    }

    public int getMapId()
    {
        return  this.mapId;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| SETTER METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/
    public void setNodeA(int x){
        this.nodeA = x;
    }

    public void setNodeB(int x){
        this.nodeB = x;
    }
    public void setStatus(int x){
        this.travelStatus = x;
    }

    public void setStartTime(String x){
        Log.d("DBG", "In MapPath - Start Time String: " + x);

        this.startTime = x;
    }

    public void setEndTime(String x){
        this.endTime = x;
    }

    /*|||||||||||||||||||||||||||||||||||||||||||||||||| PATH METHODS ||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||*/

}
