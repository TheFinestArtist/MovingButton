package com.thefinestartist.movingbutton.utils;

import android.content.Context;
import android.os.Vibrator;

import com.thefinestartist.movingbutton.enums.VibrationStrength;

/**
 * Created by TheFinestArtist on 2/12/15.
 */
public class VibrateUtil {

    private static int VIBRATION_WEAKEST = 2;
    private static int VIBRATION_WEAK = 4;
    private static int VIBRATION_NORMAL = 6;
    private static int VIBRATION_STRONG = 8;
    private static int VIBRATION_STRONGEST = 10;

    private static final Object mSingletonLock = new Object();
    private static Vibrator vibrator = null;

    private static Vibrator getInstance(Context context) {
        synchronized (mSingletonLock) {
            if (vibrator != null)
                return vibrator;
            if (context != null)
                vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            return vibrator;
        }
    }

    public static void vibtate(Context context, VibrationStrength vibrationStrength) {
        switch (vibrationStrength) {
            case WEAKEST:
                getInstance(context).vibrate(VIBRATION_WEAKEST);
                break;
            case WEAK:
                getInstance(context).vibrate(VIBRATION_WEAK);
                break;
            case NORMAL:
                getInstance(context).vibrate(VIBRATION_NORMAL);
                break;
            case STRONG:
                getInstance(context).vibrate(VIBRATION_STRONG);
                break;
            case STRONGEST:
                getInstance(context).vibrate(VIBRATION_STRONGEST);
                break;
            case NONE:
            default:
                break;
        }
    }
}
