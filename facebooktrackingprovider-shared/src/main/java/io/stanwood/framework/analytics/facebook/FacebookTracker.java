package io.stanwood.framework.analytics.facebook;


import android.app.Application;
import android.support.annotation.NonNull;

import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;

public abstract class FacebookTracker extends Tracker {
    public static final String TRACKER_NAME = "facebook";
    protected final MapFunction mapFunc;
    protected final String appKey;

    protected FacebookTracker(Builder builder) {
        super(builder);
        if (builder.mapFunc == null) {
            mapFunc = new DefaultMapFunction();
        } else {
            mapFunc = builder.mapFunc;
        }
        this.appKey = builder.appKey;
    }

    @Override
    protected void enable(boolean enabled) {
        //noop
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        //noop
    }

    @Override
    final public String getTrackerName() {
        return TRACKER_NAME;
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        //noop
    }

    public static abstract class Builder extends Tracker.Builder<Builder> {
        private MapFunction mapFunc = null;
        private String appKey;

        Builder(Application context, String appKey) {
            super(context);
            this.appKey = appKey;
            this.exceptionTrackingEnabled = false;
        }

        public abstract FacebookTracker build();

        public Builder mapFunction(MapFunction func) {
            this.mapFunc = func;
            return this;
        }
    }
}
