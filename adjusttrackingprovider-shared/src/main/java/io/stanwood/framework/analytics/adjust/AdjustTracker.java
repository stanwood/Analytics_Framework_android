package io.stanwood.framework.analytics.adjust;


import android.app.Application;

import androidx.annotation.NonNull;
import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;

public abstract class AdjustTracker extends Tracker {
    public static final String TRACKER_NAME = "adjust";
    protected final String appKey;
    protected final MapFunction mapFunc;

    protected AdjustTracker(Builder builder) {
        super(builder);
        this.appKey = builder.appKey;
        if (builder.mapFunc == null) {
            mapFunc = new MapFunction();
        } else {
            mapFunc = builder.mapFunc;
        }
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        //noop
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        //noop
    }

    @Override
    final public String getTrackerName() {
        return TRACKER_NAME;
    }

    @Override
    protected void enable(boolean enabled) {
        //noop
    }

    public static class MapFunction {

        public String mapContentToken(TrackerParams params) {
            return null;
        }
    }

    public abstract static class Builder extends Tracker.Builder<Builder> {
        private String appKey;
        private MapFunction mapFunc = null;

        Builder(Application context, String appKey) {
            super(context);
            this.appKey = appKey;
        }

        public abstract AdjustTracker build();

        public Builder mapFunction(MapFunction func) {
            this.mapFunc = func;
            return this;
        }
    }
}
