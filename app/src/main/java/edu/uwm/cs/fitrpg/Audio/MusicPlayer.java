package edu.uwm.cs.fitrpg.Audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.SurfaceView;

import edu.uwm.cs.fitrpg.R;

/**
 * Created by SS Fink on 11/12/2017.
 */

public class MusicPlayer
{
    private MediaPlayer player;

    // Default constructor for playing the "boss_test.mp3" in the app
    public MusicPlayer(SurfaceView sv)
    {
        player = MediaPlayer.create(sv.getContext(), R.raw.boss_test);
        player.start();
        player.setLooping(true);
    }

    public MusicPlayer(SurfaceView sv, int resID)
    {
        player = MediaPlayer.create(sv.getContext(), resID);
        player.start();
        player.setLooping(true);

    }

    public void stop()
    {
        player.stop();
        player.release();
        player = null;
    }


}
