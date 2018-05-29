package io.stanwood.framework.analytics.bugfender;


import android.app.Application;

public class BugfenderTrackerImpl extends BugfenderTracker {
    protected BugfenderTrackerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    public static class Builder extends BugfenderTracker.Builder {
        Builder(Application context, String appKey) {
            super(context, appKey);
        }

        public BugfenderTracker build() {
            return new BugfenderTrackerImpl(this);
        }

    }
}
