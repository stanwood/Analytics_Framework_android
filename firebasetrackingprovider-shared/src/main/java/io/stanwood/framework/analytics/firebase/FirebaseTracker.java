package io.stanwood.framework.analytics.firebase;


import android.app.Application;
import android.support.annotation.NonNull;

import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;

public abstract class FirebaseTracker extends Tracker {
    public static final String TRACKER_NAME = "firebase";
    protected final MapFunction mapFunc;

    protected FirebaseTracker(Builder builder) {
        super(builder);
        mapFunc = builder.mapFunc;
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

    @Override
    final public String getTrackerName() {
        return TRACKER_NAME;
    }

    public static abstract class Builder extends Tracker.Builder<Builder> {
        protected MapFunction mapFunc = null;

        protected Builder(Application context) {
            super(context);
        }

        @Override
        public abstract FirebaseTracker build();

        public Builder mapFunction(MapFunction func) {
            this.mapFunc = func;
            return this;
        }
    }

}