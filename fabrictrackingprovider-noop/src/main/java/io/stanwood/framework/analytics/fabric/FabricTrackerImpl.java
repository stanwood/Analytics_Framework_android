package io.stanwood.framework.analytics.fabric;


import android.app.Application;

public class FabricTrackerImpl extends FabricTracker {

    protected FabricTrackerImpl(FabricTracker.Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context) {
        return new Builder(context);
    }

    public static class Builder extends FabricTracker.Builder {
        Builder(Application context) {
            super(context);
            this.exceptionTrackingEnabled = true;
        }
        public FabricTrackerImpl build() {
            return new FabricTrackerImpl(this);
        }
    }
}
