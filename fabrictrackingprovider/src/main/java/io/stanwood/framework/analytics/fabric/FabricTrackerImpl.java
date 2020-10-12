package io.stanwood.framework.analytics.fabric;


import android.app.Application;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.Map;

import androidx.annotation.RequiresPermission;
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
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        }
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        String mapped = mapFunc.map(params);
        if (!TextUtils.isEmpty(mapped)) {
            FirebaseCrashlytics.getInstance().log(params.getEventName() + ": " + mapped);
        }
        Map<String, Object> mappedKeys = mapFunc.mapKeys(params);
        if (mappedKeys != null) {
            for (Map.Entry<String, Object> entry : mappedKeys.entrySet()) {
                if (entry.getValue() == null) {
                    continue;
                }
                if (entry.getKey().equals(TrackingKey.USER_ID)) {
                    FirebaseCrashlytics.getInstance().setUserId((String) entry.getValue());
                } else if (entry.getKey().equals(TrackingKey.USER_EMAIL)) {
                    FirebaseCrashlytics.getInstance().setCustomKey(TrackingKey.USER_EMAIL, (String) entry.getValue());
                } else if (entry.getValue() instanceof String) {
                    FirebaseCrashlytics.getInstance().setCustomKey(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof Integer) {
                    FirebaseCrashlytics.getInstance().setCustomKey(entry.getKey(), (Integer) entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    FirebaseCrashlytics.getInstance().setCustomKey(entry.getKey(), (Boolean) entry.getValue());
                } else if (entry.getValue() instanceof Long) {
                    FirebaseCrashlytics.getInstance().setCustomKey(entry.getKey(), (Long) entry.getValue());
                } else if (entry.getValue() instanceof Float) {
                    FirebaseCrashlytics.getInstance().setCustomKey(entry.getKey(), (Float) entry.getValue());
                } else if (entry.getValue() instanceof Double) {
                    FirebaseCrashlytics.getInstance().setCustomKey(entry.getKey(), (Double) entry.getValue());
                }
            }
        }
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable);
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
