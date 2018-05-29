package io.stanwood.framework.analytics.testfairy;

import android.app.Application;
import android.support.annotation.NonNull;

import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;

public abstract class TestfairyTracker extends Tracker {
    public static final String TRACKER_NAME = "testfairy";
    protected final MapFunction mapFunc;
    protected final String appKey;

    protected TestfairyTracker(Builder builder) {
        super(builder);
        if (builder.mapFunc == null) {
            mapFunc = new DefaultMapFunction();
        } else {
            mapFunc = builder.mapFunc;
        }
        this.appKey = builder.appKey;
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        // no-op
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        // no-op
    }

    @Override
    final public String getTrackerName() {
        return TRACKER_NAME;
    }

    @Override
    protected void enable(boolean enabled) {
        // no-op
    }

    public static abstract class Builder extends Tracker.Builder<Builder> {

        private String appKey;

        private MapFunction mapFunc = null;

        Builder(Application context, String appKey) {
            super(context);
            this.appKey = appKey;
            this.exceptionTrackingEnabled = true;
        }

        public abstract TestfairyTracker build();

        public Builder mapFunction(MapFunction func) {
            this.mapFunc = func;
            return this;
        }

        /**
         * Enables exception tracking: sends handled exceptions to crashlytics
         *
         * @param enable enables exception tracking , default true
         * @return the builder
         */
        public Builder setExceptionTrackingEnabled(boolean enable) {
            this.exceptionTrackingEnabled = enable;
            return this;
        }
    }
}
