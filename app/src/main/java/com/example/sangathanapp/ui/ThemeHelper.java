package com.example.sangathanapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeHelper {
    public static void applyTheme(Context context) {
        SharedPreferences sp = context.getSharedPreferences("APP", Context.MODE_PRIVATE);
        boolean isDarkMode = sp.getBoolean("DARK_MODE", true); // default to dark mode
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
