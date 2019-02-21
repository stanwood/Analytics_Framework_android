package io.stanwood.framework.analytics.mixpanel;


import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONObject;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class MixpanelTrackerImpl extends MixpanelTracker {
    private MixpanelAPI mixpanelAPI;

    protected MixpanelTrackerImpl(Builder builder) {
        super(builder);
    }

    public static Builder builder(Application context, String appKey) {
        return new Builder(context, appKey);
    }

    @Override
    protected void enable(boolean enabled) {
        if (enabled) {
            if (mixpanelAPI == null) {
                mixpanelAPI = MixpanelAPI.getInstance(context, appKey);
            }
            mixpanelAPI.optInTracking();
        } else if (mixpanelAPI != null) {
            mixpanelAPI.optOutTracking();
        }
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        TrackerParams mapped = mapFunc.map(params);
        if (mapped != null) {
            JSONObject props = mapped.getCustomPropertys() != null ? new JSONObject(mapped.getCustomPropertys()) : null;
            mixpanelAPI.track(mapped.getEventName(), props);
        }
        Map<String, Object> mappedKeys = mapFunc.mapKeys(params);
        if (mappedKeys != null) {
            MixpanelAPI.People p = mixpanelAPI.getPeople();
            for (Map.Entry<String, Object> entry : mappedKeys.entrySet()) {
                String key = entry.getKey();
                if (TrackingKey.USER_EMAIL.equalsIgnoreCase(key)) {
                    p.set("$email", entry.getValue());
                } else if (TrackingKey.USER_ID.equalsIgnoreCase(key)) {
                    String userId = (String) entry.getValue();
                    p.identify(userId);
                    mixpanelAPI.identify(userId);
                    if (!TextUtils.isEmpty(senderId)) {
                        p.initPushHandling(senderId);
                    }
                } else {
                    p.set(key, entry.getValue());
                }
            }
        }
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
