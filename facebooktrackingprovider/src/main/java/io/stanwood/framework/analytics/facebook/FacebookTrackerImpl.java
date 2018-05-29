package io.stanwood.framework.analytics.facebook;


import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import io.stanwood.framework.analytics.generic.TrackerParams;

public class FacebookTrackerImpl extends FacebookTracker {

    private AppEventsLogger eventsLogger;

    protected FacebookTrackerImpl(FacebookTracker.Builder builder) {
        super(builder);
    }


    @RequiresPermission(
            allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"}
    )
    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    public void enable(boolean enabled) {
        if (enabled) {
            if (eventsLogger == null) {
                FacebookSdk.setApplicationId(appKey);
                FacebookSdk.sdkInitialize(context);
                FacebookSdk.addLoggingBehavior(LoggingBehavior.APP_EVENTS);
                FacebookSdk.setLimitEventAndDataUsage(context, false);
                eventsLogger = AppEventsLogger.newLogger(context);
                AppEventsLogger.activateApp(context);
            }
        } else {
            if (eventsLogger != null) {
                FacebookSdk.clearLoggingBehaviors();
                FacebookSdk.setLimitEventAndDataUsage(context, true);
                eventsLogger = null;
            }
        }
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        Bundle mapped = mapFunc.map(params);
        if (mapped != null) {
            eventsLogger.logEvent(params.getEventName(), mapped);
        }
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        //noop
    }

    public static class Builder extends FacebookTracker.Builder {
        Builder(Application context, String appKey) {
            super(context, appKey);
        }

        public FacebookTrackerImpl build() {
            return new FacebookTrackerImpl(this);
        }
    }
}
