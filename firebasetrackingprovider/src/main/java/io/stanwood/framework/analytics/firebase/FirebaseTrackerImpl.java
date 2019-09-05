package io.stanwood.framework.analytics.firebase;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import android.text.TextUtils;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class FirebaseTrackerImpl extends FirebaseTracker {
    private FirebaseAnalytics firebaseAnalytics;
    private Activity activity;
    private boolean isActivityStarted;
    private TrackerParams pendingScreenView;
    private Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            isActivityStarted = true;
        }

        @Override
        public void onActivityResumed(Activity activity) {
            FirebaseTrackerImpl.this.activity = activity;
            if (pendingScreenView != null) {
                setScreenName(activity, pendingScreenView);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            FirebaseTrackerImpl.this.activity = null;
        }

        @Override
        public void onActivityStopped(Activity activity) {
            isActivityStarted = false;
            pendingScreenView = null;
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    protected FirebaseTrackerImpl(Builder builder) {
        super(builder);
    }

    @RequiresPermission(
            allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE", "android.permission.WAKE_LOCK"}
    )
    public static Builder builder(Application context) {
        return new Builder(context);
    }

    private static Bundle toBundle(TrackerParams params) {
        Bundle bundle = new Bundle();
        if (TrackingEvent.PURCHASE.equalsIgnoreCase(params.getEventName())) {
            bundle.putString(FirebaseAnalytics.Param.VALUE, params.getCustomPropertys().get(TrackingKey.PURCHASE_PRICE).toString());
            bundle.putString(FirebaseAnalytics.Param.CURRENCY, params.getCustomPropertys().get(TrackingKey.PURCHASE_CURRENCY).toString());
            bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, params.getCustomPropertys().get(TrackingKey.PURCHASE_ORDERID).toString());
            return bundle;
        }
        if (!TextUtils.isEmpty(params.getItemId())) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, params.getItemId());
        }
        if (!TextUtils.isEmpty(params.getCategory())) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, params.getCategory());
        }
        if (!TextUtils.isEmpty(params.getContentType())) {
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, params.getContentType());
        }
        if (!TextUtils.isEmpty(params.getName())) {
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, params.getName());
        }
        if (params.getCustomPropertys() != null && !params.getCustomPropertys().isEmpty()) {
            for (Map.Entry<String, Object> entry : params.getCustomPropertys().entrySet()) {
                if (entry.getValue() != null) {
                    bundle.putString(entry.getKey(), entry.getValue().toString());
                }
            }
        }
        return bundle;
    }

    @Override
    @SuppressLint("MissingPermission")
    protected void enable(boolean enabled) {
        if (firebaseAnalytics == null) {
            this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
        }
        firebaseAnalytics.setAnalyticsCollectionEnabled(enabled);
        if (enabled) {
            context.registerActivityLifecycleCallbacks(lifecycleCallbacks);
        } else {
            context.unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
            activity = null;
        }
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        TrackerParams mapped = mapFunc.map(params);
        if (mapped != null) {
            if (TrackingEvent.SCREEN_VIEW.equalsIgnoreCase(mapped.getEventName())) {
                if (activity != null) {
                    setScreenName(activity, mapped);
                } else if (isActivityStarted) {
                    pendingScreenView = mapped;
                }
            } else {
                firebaseAnalytics.logEvent(mapped.getEventName(), toBundle(mapped));
            }
        }
        Map<String, Object> mappedKeys = mapFunc.mapKeys(params);
        if (mappedKeys != null) {
            for (Map.Entry<String, Object> entry : mappedKeys.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (TrackingKey.USER_ID.equalsIgnoreCase(entry.getKey())) {
                    firebaseAnalytics.setUserId(entry.getValue().toString());
                } else {
                    firebaseAnalytics.setUserProperty(entry.getKey(), entry.getValue().toString());
                }
            }
        }
    }

    private void setScreenName(Activity activity, TrackerParams params) {
        firebaseAnalytics.setCurrentScreen(activity, params.getName(), params.getCategory());
        pendingScreenView = null;
    }

    public static class Builder extends FirebaseTracker.Builder {
        Builder(Application context) {
            super(context);
        }

        @Override
        public FirebaseTrackerImpl build() {
            return new FirebaseTrackerImpl(this);
        }
    }

}