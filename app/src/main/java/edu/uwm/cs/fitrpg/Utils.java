/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Changes to the sample code from Google have been made to customize it to the needs of this app.
 */

package edu.uwm.cs.fitrpg;

import android.content.Context;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

//<<<<<<< Updated upstream
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
//=======
//public class Utils {
//>>>>>>> Stashed changes

public class Utils {
    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param view
     * @param text The Snackbar text.
     */
    public static void showSnackbar(View view, final String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param view
     * @param mainText      The id for the string resource for the Snackbar text.
     * @param actionText    The text of the action item.
     * @param listener      The listener associated with the Snackbar action.
     */
    public static void showSnackbar(@NonNull View view, final String mainText, final String actionText, View.OnClickListener listener) {
        Snackbar.make(view, mainText, Snackbar.LENGTH_INDEFINITE)
                .setAction(actionText, listener).show();
    }

    public static String formatDuration(long ms) {
        long seconds = ms / 1000L;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        String result = "";
        if (days > 0) {
            result += days + days == 1 ? "day " : "days ";
        }
        // cleanup clock values
        hours = hours % 24;
        minutes = minutes % 60;
        seconds = seconds % 60;

        if (hours > 0) {
            result += String.format(Locale.ENGLISH, "%02d:", hours);
        }
        result += String.format(Locale.ENGLISH, "%02d:%02d", minutes, seconds);
        return result;
    }

    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }
}
