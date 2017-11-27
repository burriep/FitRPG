package edu.uwm.cs.fitrpg;

import java.util.Random;

/**
 * Created by Patrick Seaton on 11/26/2017.
 */

public class FitnessChallenge {

    private String[] activityTypes = {"Running", "Walking", "Swimming", "Bicycling", "Dancing", "Tennis", "Racquetball", "Basketball", "Soccer",
            "Jumping jacks", "Stretches", "Yoga", "Pilates", "Jumping rope", "Pushups", "Situps", "Lifting weights", "Climbing stairs"};
    private String challengeType;

    public FitnessChallenge()
    {
        chooseRandom();
    }

    public void chooseRandom()
    {
        Random rand = new Random();

        challengeType = activityTypes[rand.nextInt(activityTypes.length)];
    }

    public Boolean checkComplete(String type){return type.equals(challengeType);}

    public String getChallengeType(){return challengeType;}

}
