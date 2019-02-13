package io.stanwood.analyticstest.analytics;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.perf.FirebasePerformance;

import java.util.HashMap;
import java.util.Map;

import io.stanwood.framework.analytics.BaseAnalyticsTracker;
import io.stanwood.framework.analytics.adjust.AdjustTrackerImpl;
import io.stanwood.framework.analytics.fabric.FabricTracker;
import io.stanwood.framework.analytics.fabric.FabricTrackerImpl;
import io.stanwood.framework.analytics.firebase.DefaultMapFunction;
import io.stanwood.framework.analytics.firebase.FirebaseTracker;
import io.stanwood.framework.analytics.firebase.FirebaseTrackerImpl;
import io.stanwood.framework.analytics.ga.GoogleAnalyticsTrackerImpl;
import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import io.stanwood.framework.analytics.mixpanel.MixpanelTrackerImpl;
import timber.log.Timber;

public class AdvancedAppTracker extends BaseAnalyticsTracker {
    private static AdvancedAppTracker instance;

    private AdvancedAppTracker(@NonNull Context context, @NonNull FabricTracker fabricTracker, @NonNull FirebaseTracker firebaseTracker,
                               @Nullable Tracker... optional) {
        super(context, fabricTracker, firebaseTracker, optional);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static synchronized void init(Application application) {
        if (instance == null) {
            FirebaseTracker firebaseTracker = FirebaseTrackerImpl.builder(application)
                    .mapFunction(new DefaultMapFunction() {
                        @Override
                        public Bundle map(TrackerParams params) {
                            Bundle bundle = new Bundle();
                            bundle.putString("category", params.getCategory());
                            bundle.putString("action", params.getName());
                            bundle.putString("label", params.getItemId());
                            return bundle;
                        }
                    }).build();
            Tracker adjustTracker = AdjustTrackerImpl.builder(application, "KEY")
                    .mapFunction(new io.stanwood.framework.analytics.adjust.DefaultMapFunction() {
                        @Override
                        public String mapContentToken(TrackerParams params) {
                            if (params.getEventName().equals(TrackingEvent.VIEW_ITEM) && params.getName().equals("home")) {
                                return "ADJUST_CONTENT_ID";
                            }
                            return null;
                        }
                    })
                    .build();
            Tracker mixpanelTracker = MixpanelTrackerImpl.builder(application, "KEY")
                    .mapFunction(new io.stanwood.framework.analytics.mixpanel.DefaultMapFunction() {
                        @Override
                        public TrackerParams map(TrackerParams params) {
                            return TrackerParams.builder(params.getEventName())
                                    .addCustomProperty("category", params.getCategory())
                                    .addCustomProperty("action", params.getName())
                                    .addCustomProperty("label", params.getItemId()).build();
                        }
                    })
                    .build();
            Tracker gaTracker = GoogleAnalyticsTrackerImpl.builder(application, "KEY")
                    .setExceptionTrackingEnabled(true)
                    .build();
            FabricTrackerImpl fabricTracker = FabricTrackerImpl.builder(application).build();
            instance = new AdvancedAppTracker(application, fabricTracker, firebaseTracker, mixpanelTracker, adjustTracker, gaTracker);
            FirebasePerformance.getInstance().setPerformanceCollectionEnabled(!BuildConfig.DEBUG);
        }
    }

    public static AdvancedAppTracker instance() {
        if (instance == null) {
            throw new IllegalArgumentException("Call init() first!");
        }
        return instance;
    }

    public void trackAdLoaded(String adId) {
        trackEvent(TrackerParams.builder("ad").setName("loaded").setId(adId).build());
    }

    public void trackShowDetails(String id, String name) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("id", id);
        map.put("name", name);
        trackScreenView("details", map);
    }
}