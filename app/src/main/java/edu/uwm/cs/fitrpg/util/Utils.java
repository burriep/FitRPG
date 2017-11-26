/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Changes to the sample code from Google have been made to customize it to the needs of this app.
 */

package edu.uwm.cs.fitrpg.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import edu.uwm.cs.fitrpg.R;

public class Utils {
    public final static String SP_KEY_LOCATION_UPDATES_REQUESTED = "edu.uwm.cs.fitrpg.shared-preferences-location-updates-requested";
    public final static String SP_KEY_DISTANCE_UPDATES = "edu.uwm.cs.fitrpg.shared-preferences-track-realtime-distance";
    public final static String SP_KEY_AVG_SPEED_UPDATES = "edu.uwm.cs.fitrpg.shared-preferences-track-realtime-avg-speed";
    public final static String SP_KEY_TOP_SPEED_UPDATES = "edu.uwm.cs.fitrpg.shared-preferences-track-realtime-top-speed";
    public final static String SP_KEY_DURATION_SECONDS_UPDATES = "edu.uwm.cs.fitrpg.shared-preferences-track-realtime-duration-seconds";

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
     * @param mainText   The id for the string resource for the Snackbar text.
     * @param actionText The text of the action item.
     * @param listener   The listener associated with the Snackbar action.
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

    /**
     * Returns true if requesting location updates, otherwise returns false.
     *
     * @param context The {@link Context}.
     */
    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SP_KEY_LOCATION_UPDATES_REQUESTED, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     *
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(SP_KEY_LOCATION_UPDATES_REQUESTED, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     *
     * @param location The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }

    public static int getIntegerField(EditText field, int defaultValue) {
        int value = defaultValue;
        CharSequence chars = field.getText();
        String text = chars == null ? "" : chars.toString().trim();
        if (text.length() > 0) {
            try {
                value = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                // TODO: handle error
            }
        }
        return value;
    }

    public static double getDoubleField(EditText field, double defaultValue) {
        double value = defaultValue;
        CharSequence chars = field.getText();
        String text = chars == null ? "" : chars.toString().trim();
        if (text.length() > 0) {
            try {
                value = Double.parseDouble(text);
            } catch (NumberFormatException e) {
                // TODO: handle error
            }
        }
        return value;
    }
}
