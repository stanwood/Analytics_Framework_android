package io.stanwood.framework.analytics.mixpanel;


import android.support.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;

public interface MapFunction {
    @Nullable
    Map<String, String> map(TrackerParams params);

    @Nullable
    Map<String, Object> mapKeys(TrackerParams params);
}
