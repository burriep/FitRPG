package edu.uwm.cs.fitrpg.Audio;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by SS Fink on 11/13/2017.
 */

public class SFXPlayer
{
    SoundPool sp;
    SurfaceView sv;
    Hashtable<Integer, Integer> ResToSoundTable;

    public SFXPlayer(SurfaceView sv)
    {
        AudioAttributes aa = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MOVIE)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();
        sp = new SoundPool.Builder()
                .setMaxStreams(20)
                .setAudioAttributes(aa)
                .build();

        this.sv = sv;
        ResToSoundTable = new Hashtable<Integer, Integer>();
    }

    public void loadSound(int resID)
    {
        int sound  = sp.load(sv.getContext(), resID, 1);
        ResToSoundTable.put(resID, sound);

    }

    public void playSound(int resID)
    {
        int sound = ResToSoundTable.get(resID);
        sp.play(sound,0.99f, 0.99f, 1, 0, 0.99f);
    }


}
