package com.thefinestartist.movingbutton.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Created by TheFinestArtist on 2/12/15.
 */
public class SoundUtil {

    private static final Object mSingletonLock = new Object();
    private static AudioManager audioManager = null;

    private static AudioManager getInstance(Context context) {
        synchronized (mSingletonLock) {
            if (audioManager != null)
                return audioManager;
            if (context != null)
                audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            return audioManager;
        }
    }

    public static void playSound(Context context, int volume) {
        if (volume == 0)
            return;
        getInstance(context).playSoundEffect(AudioManager.FX_KEY_CLICK, (float) volume / 100.0f);
    }
}
