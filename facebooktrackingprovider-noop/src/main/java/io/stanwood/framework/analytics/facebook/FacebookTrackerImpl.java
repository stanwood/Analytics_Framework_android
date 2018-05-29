package io.stanwood.framework.analytics.facebook;


import android.app.Application;

public class FacebookTrackerImpl extends FacebookTracker {

    protected FacebookTrackerImpl(FacebookTracker.Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
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
