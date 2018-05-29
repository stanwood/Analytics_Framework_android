package io.stanwood.framework.analytics.testfairy;


import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.text.TextUtils;

import com.testfairy.TestFairy;

import java.util.Map;

import io.stanwood.framework.analytics.generic.TrackerParams;
import io.stanwood.framework.analytics.generic.TrackingKey;

public class TestfairyTrackerImpl extends TestfairyTracker {
    private boolean isInited;

    TestfairyTrackerImpl(Builder builder) {
        super(builder);
    }

    @RequiresPermission(
            allOf = {"android.permission.INTERNET", "android.permission.ACCESS_NETWORK_STATE"}
    )
    public static TestfairyTrackerImpl.Builder builder(Application context, String appKey) {
        return new TestfairyTrackerImpl.Builder(context, appKey);
    }

    @Override
    public void track(@NonNull TrackerParams params) {
        String mapped = mapFunc.map(params);
        if (!TextUtils.isEmpty(mapped)) {
            TestFairy.addEvent(mapped);
        }
        Map<String, Object> mappedKeys = mapFunc.mapKeys(params);
        if (mappedKeys != null) {
            Object v;
            String userId = ((v = mappedKeys.get(TrackingKey.USER_ID)) != null) ? v.toString() : null;
            if (!TextUtils.isEmpty(userId)) {
                TestFairy.setUserId(userId);
            }
        }
    }

    @Override
    public void track(@NonNull Throwable throwable) {
        TestFairy.logThrowable(throwable);
    }

    @Override
    protected void enable(boolean enabled) {
        if (enabled && !isInited) {
            isInited = true;
            TestFairy.begin(context, appKey);
        } else if (!enabled && isInited) {
            TestFairy.stop();
            isInited = false;
        }
    }

    public static class Builder extends TestfairyTracker.Builder {

        Builder(Application context, String appKey) {
            super(context, appKey);
        }

        public TestfairyTracker build() {
            return new TestfairyTrackerImpl(this);
        }
    }
}