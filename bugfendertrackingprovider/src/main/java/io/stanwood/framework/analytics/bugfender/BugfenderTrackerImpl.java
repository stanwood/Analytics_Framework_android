package io.stanwood.framework.analytics.bugfender;


import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.bugfender.sdk.Bugfender;

import io.stanwood.framework.analytics.generic.TrackerParams;

public class BugfenderTrackerImpl extends BugfenderTracker {
    public static final String TRACKER_NAME = "bugfender";
    private boolean isInited;

    protected BugfenderTrackerImpl(Builder builder) {
        super(builder);
    }

    @RequiresPermission(
            allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"}
    )
    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        Bugfender.d(params.getEventName(), String.format("[%s] [%s]", params.getName(), params.getItemId()));
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        Bugfender.sendIssue("Exception", throwable.getMessage());
    }

    @Override
    protected void enable(boolean enabled) {
        // there is no way to disable after bugfender is once inited
        if (enabled) {
            if (!isInited) {
                isInited = true;
                Bugfender.init(context, appKey, false);
            }
            if (enableLogcatLogging) {
                Bugfender.enableLogcatLogging();
            }
            if (enableUiLogging) {
                Bugfender.enableUIEventLogging(context);
            }
        }
    }

    public static class Builder extends BugfenderTracker.Builder {
        Builder(Application context, String appKey) {
            super(context, appKey);
        }

        public BugfenderTracker build() {
            return new BugfenderTrackerImpl(this);
        }

    }
}
