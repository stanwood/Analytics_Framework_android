package io.stanwood.framework.analytics.adjust;


import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;
import com.adjust.sdk.LogLevel;

import androidx.annotation.NonNull;
import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class AdjustTrackerImpl extends AdjustTracker {
    private boolean isInited;

    protected AdjustTrackerImpl(AdjustTracker.Builder builder) {
        super(builder);

        builder.context.registerActivityLifecycleCallbacks(new AdjustLifecycleCallbacks());
    }

    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        String eventToken = mapFunc.mapContentToken(params);
        if (!TextUtils.isEmpty(eventToken)) {
            AdjustEvent event = new AdjustEvent(eventToken);
            if (TrackingEvent.PURCHASE.equalsIgnoreCase(params.getEventName())) {
                event.setRevenue((double) params.getCustomPropertys().get(TrackingKey.PURCHASE_PRICE), params.getCustomPropertys().get(TrackingKey.PURCHASE_CURRENCY).toString());
            }
            Adjust.trackEvent(event);
        }
        String token = params.getCustomProperty(TrackingKey.PUSH_TOKEN);
        if (!TextUtils.isEmpty(token)) {
            Adjust.setPushToken(token, context);
        }
    }

    @Override
    protected void enable(boolean enabled) {
        if (enabled && !isInited) {
            isInited = true;
            String environment = AdjustConfig.ENVIRONMENT_SANDBOX;
            AdjustConfig config = new AdjustConfig(context, appKey, environment);
            config.setLogLevel(LogLevel.VERBOSE);
            Adjust.onCreate(config);
        } else if (isInited) {
            if (!enabled) {
                Adjust.gdprForgetMe(context);
            }
            Adjust.setEnabled(enabled);
        }
    }

    public static class Builder extends AdjustTracker.Builder {
        Builder(Application context, String appKey) {
            super(context, appKey);
        }

        public AdjustTrackerImpl build() {
            return new AdjustTrackerImpl(this);
        }
    }

    private static class AdjustLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {
            Adjust.onResume();
        }

        @Override
        public void onActivityPaused(Activity activity) {
            Adjust.onPause();
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    }

}
