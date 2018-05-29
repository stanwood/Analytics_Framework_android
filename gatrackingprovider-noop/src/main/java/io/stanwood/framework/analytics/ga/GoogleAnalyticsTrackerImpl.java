package io.stanwood.framework.analytics.ga;


import android.app.Application;

public class GoogleAnalyticsTrackerImpl extends GoogleAnalyticsTracker {

    protected GoogleAnalyticsTrackerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }
    public static class Builder extends GoogleAnalyticsTracker.Builder<Builder> {
        Builder(Application context, String appKey) {
            super(context, appKey);
        }
        @Override
        public GoogleAnalyticsTracker build() {
            return new GoogleAnalyticsTrackerImpl(this);
        }
    }
}