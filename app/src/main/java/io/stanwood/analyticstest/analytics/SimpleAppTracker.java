package io.stanwood.analyticstest.analytics;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.perf.FirebasePerformance;

import io.stanwood.framework.analytics.BaseAnalyticsTracker;
import io.stanwood.framework.analytics.fabric.FabricTracker;
import io.stanwood.framework.analytics.fabric.FabricTrackerImpl;
import io.stanwood.framework.analytics.firebase.FirebaseTracker;
import io.stanwood.framework.analytics.firebase.FirebaseTrackerImpl;
import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.testfairy.TestfairyTracker;
import io.stanwood.framework.analytics.testfairy.TestfairyTrackerImpl;
import timber.log.Timber;

public class SimpleAppTracker extends BaseAnalyticsTracker {
    private static SimpleAppTracker instance;

    private SimpleAppTracker(@NonNull Context context, @NonNull FabricTracker fabricTracker, @NonNull FirebaseTracker firebaseTracker,
                             @NonNull TestfairyTracker testfairyTracker, @Nullable Tracker... optional) {
        super(context, fabricTracker, firebaseTracker, testfairyTracker, optional);
    }

    public static synchronized void init(Application application) {
        if (instance == null) {
            instance = new SimpleAppTracker(application, FabricTrackerImpl.builder(application).build(),
                    FirebaseTrackerImpl.builder(application).setExceptionTrackingEnabled(true).build(),
                    TestfairyTrackerImpl.builder(application, "KEY").build());
            FirebasePerformance.getInstance().setPerformanceCollectionEnabled(!BuildConfig.DEBUG);
            if (BuildConfig.DEBUG) {
                Timber.plant(new Timber.DebugTree());
            }
        }
    }

    public static SimpleAppTracker instance() {
        if (instance == null) {
            throw new IllegalArgumentException("Call init() first!");
        }
        return instance;
    }
}