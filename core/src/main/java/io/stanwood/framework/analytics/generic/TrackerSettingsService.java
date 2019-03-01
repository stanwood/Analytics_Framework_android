package io.stanwood.framework.analytics.generic;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

class TrackerSettingsService {
    private final Context context;
    private final SharedPreferences preferences;

    TrackerSettingsService(@NonNull Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("tracker_settings", Context.MODE_PRIVATE);
    }

    boolean isTrackerEnabled(@NonNull String trackerName, boolean defaultValue) {
        return preferences.getBoolean(trackerName, defaultValue);
    }

    void storeTrackerState(@NonNull String trackerName, boolean enabled) {
        preferences.edit().putBoolean(trackerName, enabled).apply();
    }

    boolean isConfigAvailable(@NonNull String trackerName){
        return preferences.contains(trackerName);
    }
}
