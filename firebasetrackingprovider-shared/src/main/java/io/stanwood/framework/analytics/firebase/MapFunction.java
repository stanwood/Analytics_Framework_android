package io.stanwood.framework.analytics.firebase;


import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;

public interface MapFunction {
    @Nullable
    TrackerParams map(TrackerParams params);

    @Nullable
    Map<String, Object> mapKeys(TrackerParams keys);

}
