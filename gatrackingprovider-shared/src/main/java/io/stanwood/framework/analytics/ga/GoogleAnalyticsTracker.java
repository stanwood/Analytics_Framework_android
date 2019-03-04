package io.stanwood.framework.analytics.ga;


import android.annotation.SuppressLint;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;

public abstract class GoogleAnalyticsTracker extends Tracker {
    public static final String TRACKER_NAME = "ga";
    protected final String appKey;
    protected final int sampleRate;
    protected final boolean anonymizeIp;
    protected final boolean activityTracking;
    protected final boolean adIdCollection;
    protected final MapFunction mapFunc;
    protected GoogleAnalyticsTracker(Builder builder) {
        super(builder);
        this.appKey = builder.appKey;
        this.sampleRate = builder.sampleRate;
        this.activityTracking = builder.activityTracking;
        this.adIdCollection = builder.adIdCollection;
        this.anonymizeIp = builder.anonymizeIp;
        if (builder.mapFunc == null) {
            mapFunc = new MapFunction();
        } else {
            mapFunc = builder.mapFunc;
        }
    }

    @Override
    @SuppressLint("MissingPermission")
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

    public void setClientId(String id) {
        //noop
    }

    public static class MapFunction {

        @Nullable
        public TrackerParams mapParams(TrackerParams params) {
            return params;
        }

        @Nullable
        public Map<Integer, Object> mapKeys(TrackerParams params) {
            return null;
        }
    }

    public abstract static class Builder<T extends Tracker.Builder<T>> extends Tracker.Builder<T> {
        private int sampleRate = 100;
        private String appKey;
        private boolean activityTracking = false;
        private boolean adIdCollection = false;
        private boolean anonymizeIp = true;
        private MapFunction mapFunc = null;

        Builder(Application context, String appKey) {
            super(context);
            this.appKey = appKey;
        }

        public Builder autoActivityTracking(boolean enabled) {
            this.activityTracking = enabled;
            return this;
        }

        public Builder adIdCollection(boolean enabled) {
            this.adIdCollection = enabled;
            return this;
        }

        public Builder sampleRate(int sampleRate) {
            this.sampleRate = sampleRate;
            return this;
        }

        public Builder mapFunction(MapFunction func) {
            this.mapFunc = func;
            return this;
        }

        /**
         * Anonymize Users ip
         *
         * @param enabled enables / disabled ip anonymization , default true
         * @return the builder
         */
        public Builder anonymizeIp(boolean enabled) {
            this.anonymizeIp = enabled;
            return this;
        }

        /**
         * Enables exception tracking: sends handled exceptions to google analytics
         *
         * @param enable enables exception tracking , default false
         * @return the builder
         */
        public Builder setExceptionTrackingEnabled(boolean enable) {
            this.exceptionTrackingEnabled = enable;
            return this;
        }

        public abstract GoogleAnalyticsTracker build();
    }
}
