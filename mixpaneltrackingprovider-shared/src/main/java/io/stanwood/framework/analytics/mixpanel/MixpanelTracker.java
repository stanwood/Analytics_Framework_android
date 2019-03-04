package io.stanwood.framework.analytics.mixpanel;


import android.app.Application;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.stanwood.framework.analytics.generic.Tracker;
import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;

public abstract class MixpanelTracker extends Tracker {
    public static final String TRACKER_NAME = "mixpanel";
    protected final String appKey;
    protected final MapFunction mapFunc;
    protected final String senderId;

    protected MixpanelTracker(Builder builder) {
        super(builder);
        this.appKey = builder.appKey;
        this.senderId = builder.senderId;
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
    public void track(@NonNull Throwable throwable) {
        //noop
    }

    @Override
    final public String getTrackerName() {
        return TRACKER_NAME;
    }

    public static class MapFunction {

        @Nullable
        public TrackerParams map(TrackerParams params) {
            return params;
        }

        @Nullable
        public Map<String, Object> mapKeys(TrackerParams params) {
            if (params.getEventName().equals(TrackingEvent.IDENTIFY_USER)) {
                return params.getCustomPropertys();
            }
            return null;
        }
    }

    public abstract static class Builder extends Tracker.Builder<Builder> {
        private String appKey;
        private MapFunction mapFunc = null;
        private String senderId;

        Builder(Application context, String appKey) {
            super(context);
            this.appKey = appKey;
        }

        public abstract MixpanelTracker build();

        public Builder mapFunction(MapFunction func) {
            this.mapFunc = func;
            return this;
        }

        /**
         * Set to enable push handling
         *
         * @param senderId of the Google API Project that registered for Google Cloud Messaging
         *                 You can find your ID by looking at the URL of in your Google API Console
         *                 at https://code.google.com/apis/console/; it is the twelve digit number after
         *                 after "#project:" in the URL address bar on console pages.
         */
        public Builder senderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

    }
}
