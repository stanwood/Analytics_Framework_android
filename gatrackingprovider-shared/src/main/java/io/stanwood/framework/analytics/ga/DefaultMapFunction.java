package io.stanwood.framework.analytics.ga;

import android.support.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;

public class DefaultMapFunction implements MapFunction {

    @Nullable
    @Override
    public TrackerParams mapParams(TrackerParams params) {
        return params;
    }

    @Nullable
    @Override
    public Map<Integer, Object> mapKeys(TrackerParams params) {
        return null;
    }
}