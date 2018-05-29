package io.stanwood.framework.analytics.mixpanel;


import android.app.Application;

public class MixpanelTrackerImpl extends MixpanelTracker {

    protected MixpanelTrackerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    public static class Builder extends MixpanelTracker.Builder {
        Builder(Application context, String appKey) {
            super(context, appKey);
        }

        public MixpanelTrackerImpl build() {
            return new MixpanelTrackerImpl(this);
        }
    }
}
