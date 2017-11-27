package edu.uwm.cs.fitrpg.model;


public class User {

    private String name;
    private int userID;
    private double weight, height;
    private String lastUpdateDate;

    public User(String name, int id) {
        this.name = name;
        userID = id;
    }

    // **NOTE** Need to create user table to hold user stats


    public String getName() {
        return name;
    }

    public int getUserID() {
        return userID;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void updateName(String newName) {
        name = newName;
    }

    public void updateUserID(int newID) {
        userID = newID;
    }

    public void setName(String n) {
        name = n;
    }

    public void setWeight(double w) {
        weight = w;
    }

    public void setHeight(double h) {
        height = h;
    }

    public void setLastUpdateDate(String d) {
        lastUpdateDate = d;
    }

    @Override
    public String toString() {
        return "ID: " + getUserID() + ": " + getName() + " Weight: " + getWeight() + ", Height: " + getHeight();
    }
}
