package io.stanwood.framework.analytics.fabric;


import android.app.Application;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;

public abstract class FabricTracker extends Tracker {
    public static final String TRACKER_NAME = "fabric";
    protected final MapFunction mapFunc;

    protected FabricTracker(Builder builder) {
        super(builder);
        if (builder.mapFunc == null) {
            mapFunc = new MapFunction();
        } else {
            mapFunc = builder.mapFunc;
        }
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

    public static class MapFunction {


        @Nullable
        public String map(TrackerParams params) {
            return String.format("[%s] [%s]", params.getName(), params.getItemId());
        }

        @Nullable
        public Map<String, Object> mapKeys(TrackerParams params) {
            return params.getCustomPropertys();
        }
    }

    public static abstract class Builder extends Tracker.Builder<Builder> {
        private MapFunction mapFunc = null;

        Builder(Application context) {
            super(context);
            this.exceptionTrackingEnabled = true;
        }

        public abstract FabricTracker build();

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
