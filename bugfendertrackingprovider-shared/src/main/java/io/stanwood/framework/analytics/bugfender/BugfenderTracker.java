package io.stanwood.framework.analytics.bugfender;


import android.app.Application;
import androidx.annotation.NonNull;

import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;

public abstract class BugfenderTracker extends Tracker {
    public static final String TRACKER_NAME = "bugfender";
    protected final String appKey;
    protected final boolean enableUiLogging;
    protected final boolean enableLogcatLogging;

    protected BugfenderTracker(Builder builder) {
        super(builder);
        this.appKey = builder.appKey;
        this.enableUiLogging = builder.enableUiLogging;
        this.enableLogcatLogging = builder.enableLogcatLogging;
    }

    @Override
    public void track(@NonNull TrackerParams params) {

    }

    @Override
    public void track(@NonNull Throwable throwable) {

    }

    @Override
    final public String getTrackerName() {
        return TRACKER_NAME;
    }

    @Override
    protected void enable(boolean enabled) {

    }

    public abstract static class Builder extends Tracker.Builder<Builder> {
        private boolean enableUiLogging;
        private boolean enableLogcatLogging;
        private String appKey;

        Builder(Application context, String appKey) {
            super(context);
            this.appKey = appKey;
        }

        public Builder enableUiLogging(boolean enable) {
            this.enableUiLogging = enable;
            return this;
        }

        public Builder enableLogcatLogging(boolean enable) {
            this.enableLogcatLogging = enable;
            return this;
        }

        /**
         * Enables exception tracking: sends a new issue to bugfender , default false
         *
         * @param enable enables exception tracking
         * @return the builder
         */
        public Builder setExceptionTrackingEnabled(boolean enable) {
            this.exceptionTrackingEnabled = enable;
            return this;
        }

        public abstract BugfenderTracker build();

    }
}
