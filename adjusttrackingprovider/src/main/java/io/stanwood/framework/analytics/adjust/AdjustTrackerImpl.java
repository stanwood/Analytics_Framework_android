package io.stanwood.framework.analytics.adjust;


import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.adjust.sdk.Adjust;
import com.adjust.sdk.AdjustConfig;
import com.adjust.sdk.AdjustEvent;

import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class AdjustTrackerImpl extends AdjustTracker {
    private boolean isInited;

    protected AdjustTrackerImpl(AdjustTracker.Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        String eventToken = mapFunc.mapContentToken(params);
        if (!TextUtils.isEmpty(eventToken)) {
            AdjustEvent event = new AdjustEvent(eventToken);
            if (params.getEventName().equalsIgnoreCase(TrackingEvent.PURCHASE)) {
                event.setRevenue((double) params.getCustomPropertys().get(TrackingKey.PURCHASE_PRICE), "EUR");
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
            String environment = isEnabled() ? AdjustConfig.ENVIRONMENT_PRODUCTION : AdjustConfig.ENVIRONMENT_SANDBOX;
            AdjustConfig config = new AdjustConfig(context, appKey, environment);
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
}
