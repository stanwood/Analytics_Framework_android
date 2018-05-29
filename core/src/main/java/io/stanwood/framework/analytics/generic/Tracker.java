package io.stanwood.framework.analytics.generic;


import android.app.Application;
import android.support.annotation.NonNull;

public abstract class Tracker {
    protected Application context;
    protected boolean exceptionTrackingEnabled;
    private boolean enabled;

    protected Tracker(Builder builder) {
        this.context = builder.context;
        this.exceptionTrackingEnabled = builder.exceptionTrackingEnabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
        enable(enabled);
    }

    void trackEvent(@NonNull TrackerParams params) {
        if (enabled) {
            track(params);
        }
    }

    void trackException(@NonNull Throwable throwable) {
        if (!exceptionTrackingEnabled) {
            return;
        }
        if (enabled) {
            track(throwable);
        }
    }

    /**
     * Tracks a full-fledged event.
     *
     * @param params the {@link TrackerParams}
     */
    public abstract void track(@NonNull TrackerParams params);

    /**
     * Tracks an exception.
     *
     * @param throwable the exception
     */
    public abstract void track(@NonNull Throwable throwable);

    public abstract String getTrackerName();

    protected abstract void enable(boolean enabled);

    public abstract static class Builder<T extends Builder<T>> {
        protected boolean exceptionTrackingEnabled = false;
        private Application context;

        protected Builder(Application context) {
            this.context = context;
        }

        /**
         * Constructs the tracker
         *
         * @return the tracker
         */
        abstract public Tracker build();
    }
}
