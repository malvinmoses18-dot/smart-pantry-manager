package com.example.smartpantry.util;

import android.content.Context;
import android.content.SharedPreferences;


public final class Prefs {

    private static final String FILE_NAME = "smart_pantry_prefs";
    private static final String KEY_EXPIRY_ALERTS = "expiry_alerts";
    private static final String KEY_DEFAULT_UNIT = "default_unit";

    private Prefs() { }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

  
    public static boolean isExpiryAlertsOn(Context context) {
        return prefs(context).getBoolean(KEY_EXPIRY_ALERTS, true);
    }

    public static void setExpiryAlertsOn(Context context, boolean on) {
        prefs(context).edit().putBoolean(KEY_EXPIRY_ALERTS, on).apply();
    }

 
    public static String getDefaultUnit(Context context) {
        return prefs(context).getString(KEY_DEFAULT_UNIT, "g");
    }

    public static void setDefaultUnit(Context context, String unit) {
        prefs(context).edit().putString(KEY_DEFAULT_UNIT, unit).apply();
    }
}
