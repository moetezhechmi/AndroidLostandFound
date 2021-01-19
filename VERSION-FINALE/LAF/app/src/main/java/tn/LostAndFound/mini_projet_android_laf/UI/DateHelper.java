package tn.LostAndFound.mini_projet_android_laf.UI;

import android.content.Context;

import java.util.Date;

public class DateHelper {


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = System.currentTimeMillis();
        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "à l'instant";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "Il y'a 1 minute";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " Il y'a quelques minutes";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "Il y'a 1 heure";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " Il y";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }
    }
}
