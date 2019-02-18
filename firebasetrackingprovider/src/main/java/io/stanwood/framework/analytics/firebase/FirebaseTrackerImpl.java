package io.stanwood.framework.analytics.firebase;


import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class FirebaseTrackerImpl extends FirebaseTracker {
    private FirebaseAnalytics firebaseAnalytics;

    protected FirebaseTrackerImpl(Builder builder) {
        super(builder);
    }

    @RequiresPermission(
            allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WAKE_LOCK"}
    )
    public static Builder builder(Application context) {
        return new Builder(context);
    }

    @Override
    @SuppressLint("MissingPermission")
    protected void enable(boolean enabled) {
        if (firebaseAnalytics == null) {
            this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled);
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        Bundle mapped = mapFunc.map(params);
        if (mapped != null) {
            firebaseAnalytics.logEvent(params.getEventName(), mapFunc.map(params));
        }
        Map<String, Object> mappedKeys = mapFunc.mapKeys(params);
        if (mappedKeys != null) {
            for (Map.Entry<String, Object> entry : mappedKeys.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (entry.getKey().equalsIgnoreCase(TrackingKey.USER_ID)) {
                    firebaseAnalytics.setUserId(entry.getValue().toString());
                } else {
                    firebaseAnalytics.setUserProperty(entry.getKey(), entry.getValue().toString());
                }
            }
        }
    }

    public static class Builder extends FirebaseTracker.Builder {
        Builder(Application context) {
            super(context);
        }

        @Override
        public FirebaseTrackerImpl build() {
            if (mapFunc == null) {
                mapFunc = new DefaultMapFunction();
            }
            return new FirebaseTrackerImpl(this);
        }
    }
}