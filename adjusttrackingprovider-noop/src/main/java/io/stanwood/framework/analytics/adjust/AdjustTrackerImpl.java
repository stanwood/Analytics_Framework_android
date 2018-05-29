package io.stanwood.framework.analytics.adjust;


import android.app.Application;

public class AdjustTrackerImpl extends AdjustTracker {

    protected AdjustTrackerImpl(AdjustTracker.Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    public static class Builder extends AdjustTracker.Builder {
        Builder(Application context, String appKey) {
            super(context, appKey);
        }

        public AdjustTrackerImpl build() {
            return new AdjustTrackerImpl(this);
        }
    }
}
