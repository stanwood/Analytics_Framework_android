package io.stanwood.framework.analytics.debug;


import android.app.Application;
import androidx.annotation.NonNull;

import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;

public abstract class DebugViewTracker extends Tracker {
    public static final String TRACKER_NAME = "debugvieww";

    protected DebugViewTracker(Builder builder) {
        super(builder);
    }


    @Override
    final public String getTrackerName() {
        return TRACKER_NAME;
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
    public void track(@NonNull Throwable throwable) {
        //noop
    }

    public abstract static class Builder extends Tracker.Builder<Builder> {

        Builder(Application context) {
            super(context);
        }

        @Override
        public abstract DebugViewTracker build();

    }

}