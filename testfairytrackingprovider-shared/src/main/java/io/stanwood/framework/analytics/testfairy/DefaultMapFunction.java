package io.stanwood.framework.analytics.testfairy;

import android.support.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;

public class DefaultMapFunction implements MapFunction {


    @Nullable
    @Override
    public String map(TrackerParams params) {
        return String.format("%s -> [%s] [%s]", params.getEventName(), params.getName(), params.getItemId());
    }

    @Nullable
    @Override
    public Map<String, Object> mapKeys(TrackerParams params) {
        return params.getCustomPropertys();
    }
}