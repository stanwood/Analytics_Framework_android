package io.stanwood.framework.analytics.debug;


import android.app.Application;

public class DebugViewTrackerImpl extends DebugViewTracker {

    protected DebugViewTrackerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context) {
        return new Builder(context);
    }

    public static class Builder extends DebugViewTracker.Builder {

        Builder(Application context) {
            super(context);
        }

        @Override
        public DebugViewTrackerImpl build() {
            return new DebugViewTrackerImpl(this);
        }
    }

}