package io.stanwood.framework.analytics.firebase;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;

public class DefaultMapFunction implements MapFunction {
    @Nullable
    @Override
    public Bundle map(TrackerParams params) {
        return null;
    }

    @Nullable
    @Override
    public Map<String, Object> mapKeys(TrackerParams params) {
        return params.getCustomPropertys();
    }
}