package io.stanwood.framework.analytics.ga;


import android.support.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;

public interface MapFunction {
    @Nullable
    TrackerParams mapParams(TrackerParams params);

    @Nullable
    Map<Integer, Object> mapKeys(TrackerParams params);
}
