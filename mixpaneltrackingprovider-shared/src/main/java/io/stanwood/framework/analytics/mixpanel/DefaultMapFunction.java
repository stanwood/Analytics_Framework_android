package io.stanwood.framework.analytics.mixpanel;

import android.support.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingEvent;

public class DefaultMapFunction implements MapFunction {

    @Nullable
    @Override
    public TrackerParams map(TrackerParams params) {
        return params;
    }

    @Nullable
    @Override
    public Map<String, Object> mapKeys(TrackerParams params) {
        if (params.getEventName().equals(TrackingEvent.IDENTIFY_USER)) {
            return params.getCustomPropertys();
        }
        return null;
    }
}