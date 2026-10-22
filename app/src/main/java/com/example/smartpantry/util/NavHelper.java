package com.example.smartpantry.util;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.widget.Toast;

import com.example.smartpantry.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * Wires up the bottom navigation bar shared by the three top-level screens
 * (Pantry, Recipes, Settings). Each tap sends an Intent to the matching Activity.
 *
 * The target is named with setClassName() so this helper does not depend on the
 * other Activity classes at compile time.
 */
public final class NavHelper {

    private NavHelper() { }

    /** Call from onCreate: highlights the current tab and handles taps on the others. */
    public static void setup(final Activity activity, final int selectedItemId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottom_nav);
        nav.getMenu().findItem(selectedItemId).setChecked(true);

        nav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == selectedItemId) {
                return true;
            }

            String target;
            if (id == R.id.nav_pantry) {
                target = "MainActivity";
            } else if (id == R.id.nav_suggested) {
                target = "SuggestedRecipesActivity";
            } else if (id == R.id.nav_settings) {
                target = "SettingsActivity";
            } else {
                return false;
            }

            Intent intent = new Intent();
            intent.setClassName(activity, activity.getPackageName() + "." + target);
            // Reuse the screen if it is already open instead of stacking copies.
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            try {
                activity.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(activity, "That screen has not been added yet", Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        });
    }

    /** Call from onResume so the right tab is highlighted after coming back with the Back button. */
    public static void syncSelection(Activity activity, int selectedItemId) {
        BottomNavigationView nav = activity.findViewById(R.id.bottom_nav);
        nav.getMenu().findItem(selectedItemId).setChecked(true);
    }
}
