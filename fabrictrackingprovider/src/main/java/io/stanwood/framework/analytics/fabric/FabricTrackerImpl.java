package io.stanwood.framework.analytics.fabric;


import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;

import java.util.Map;

import io.fabric.sdk.android.Fabric;
import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class FabricTrackerImpl extends FabricTracker {
    private boolean isInited;

    protected FabricTrackerImpl(FabricTracker.Builder builder) {
        super(builder);
    }


    @RequiresPermission(
            allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"}
    )
    public static Builder builder(Application context) {
        return new Builder(context);
    }

    @Override
    protected void enable(boolean enabled) {
        // there is no way to disable after crashlytics is once inited
        if (enabled && !isInited) {
            isInited = true;
            Fabric.with(context, new Crashlytics.Builder()
                    .core(new CrashlyticsCore.Builder().build())
                    .build());
        }
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        String mapped = mapFunc.map(params);
        if (!TextUtils.isEmpty(mapped)) {
            Crashlytics.log(0, params.getEventName(), mapped);
        }
        Map<String, Object> mappedKeys = mapFunc.mapKeys(params);
        if (mappedKeys != null) {
            for (Map.Entry<String, Object> entry : mappedKeys.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (entry.getKey().equals(TrackingKey.USER_ID)) {
                    Crashlytics.setUserIdentifier((String) entry.getValue());
                } else if (entry.getKey().equals(TrackingKey.USER_EMAIL)) {
                    Crashlytics.setUserEmail((String) entry.getValue());
                } else if (entry.getValue() instanceof String) {
                    Crashlytics.setString(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof Integer) {
                    Crashlytics.setInt(entry.getKey(), (Integer) entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    Crashlytics.setBool(entry.getKey(), (Boolean) entry.getValue());
                } else if (entry.getValue() instanceof Long) {
                    Crashlytics.setLong(entry.getKey(), (Long) entry.getValue());
                } else if (entry.getValue() instanceof Float) {
                    Crashlytics.setFloat(entry.getKey(), (Float) entry.getValue());
                } else if (entry.getValue() instanceof Double) {
                    Crashlytics.setDouble(entry.getKey(), (Double) entry.getValue());
                }
            }
        }
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        Crashlytics.logException(throwable);
    }

    public static class Builder extends FabricTracker.Builder {
        Builder(Application context) {
            super(context);
        }

        public FabricTrackerImpl build() {
            return new FabricTrackerImpl(this);
        }
    }
}
