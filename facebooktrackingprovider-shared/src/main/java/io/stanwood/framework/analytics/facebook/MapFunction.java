package io.stanwood.framework.analytics.facebook;


import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;

public interface MapFunction {

    @Nullable
    Bundle map(TrackerParams params);

}
